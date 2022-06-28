/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.common.DateTime.TimeType.TIME_4_DIGITS;
import static nl.procura.gba.web.components.containers.Container.LAND;
import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.setIfPresent;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AANGEVER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.OVERLEDENE;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.jpa.personen.db.DossOverlUitt;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.processen.OverlijdenGemeenteProcessen;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.processen.LijkvindingProcessen;
import nl.procura.gba.web.rest.v2.model.base.HeeftContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.*;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestDocumentType;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestOverlijdenAangifte;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestOverlijdenInGemeente;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestLijkvinding;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestLijkvindingAangifte;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestSchriftelijkeAangeverType;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.enums.CommunicatieType;
import nl.procura.gba.web.services.bs.algemeen.enums.Doodsoorzaak;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.*;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.SchriftelijkeAangever;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.standard.exceptions.ProException;
import nl.procura.validation.Bsn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestOverlijdenService extends GbaRestAbstractService {

  @Transactional
  public Zaak addOverlijden(GbaRestZaak restZaak) {
    Dossier dossier;
    ZaakType zaakType = ZaakType.get(restZaak.getAlgemeen().getType().getCode());
    dossier = (Dossier) getOverlijdenService(zaakType).getNewZaak();
    getRestServices().getZaakService().addGenericZaak(restZaak, dossier);

    switch (zaakType) {
      case OVERLIJDEN_IN_GEMEENTE:
        updateOverlijdenInGemeente(restZaak, dossier);
        break;
      case LIJKVINDING:
        updateLijkvinding(restZaak, dossier);
        break;
      default:
        throw new IllegalArgumentException("Onbekend zaaktype voor overlijden: "
            + restZaak.getAlgemeen().getType());
    }

    getRestServices()
        .getZaakService()
        .addZaakIds(restZaak, dossier);

    return dossier;
  }

  @Transactional
  public void updateOverlijdenInGemeente(GbaRestZaak restZaak, Dossier dossier) {
    Validate.notNull(restZaak.getOverlijden(), "Element zaak is niet gezet");
    GbaRestOverlijdenInGemeente rest = restZaak.getOverlijden().getOverlijdenInGemeente();
    Validate.notNull(restZaak.getOverlijden(), "Element overlijdenInGemeente is niet gezet");
    DossierOverlijdenGemeente zaak = (DossierOverlijdenGemeente) dossier.getZaakDossier();
    setAangever(zaak, rest.getAangever());
    setOverledene(zaak, rest.getOverledene());
    setAangifte(rest, zaak);
    setLijkbezorging(rest.getLijkbezorging(), zaak);
    setVerzoek(rest.getVerzoek(), zaak);

    dossier.reset();
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(OverlijdenGemeenteProcessen
        .getProcesStatussen(dossier)
        .getMessages());

    updateZaakDossier(dossier);
  }

  @Transactional
  public void updateLijkvinding(GbaRestZaak restZaak, Dossier dossier) {
    Validate.notNull(restZaak.getOverlijden(), "Element overlijden is niet gezet");
    GbaRestLijkvinding rest = restZaak.getOverlijden().getLijkvinding();
    Validate.notNull(rest, "Element lijkvinding is niet gezet");
    DossierLijkvinding zaak = (DossierLijkvinding) dossier.getZaakDossier();
    setOverledene(zaak, rest.getOverledene());
    setAangifte(rest, zaak);
    setLijkbezorging(rest.getLijkbezorging(), zaak);
    setVerzoek(rest.getVerzoek(), zaak);

    dossier.reset();
    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(LijkvindingProcessen
        .getProcesStatussen(dossier)
        .getMessages());

    updateZaakDossier(dossier);
  }

  private void updateZaakDossier(Dossier dossier) {
    DossierService dossierService = getServices().getDossierService();
    dossierService.saveDossier(dossier);
    dossierService.saveZaakDossier(dossier.getZaakDossier());
    getOverlijdenService(dossier.getType()).save(dossier);
  }

  private void setAangever(AbstractDossierOverlijden zaak, GbaRestPersoon aangever) {
    Validate.notNull(aangever, "Element aangever is niet gezet");
    setPersoon(aangever, zaak.getAangever());
  }

  private void setOverledene(AbstractDossierOverlijden zaak, GbaRestPersoon overledene) {
    Validate.notNull(overledene, "Element overledene is niet gezet");
    setPersoon(overledene, zaak.getOverledene());
    // Store name+birth fields
    DossierOverlijdenVerzoek verzoek = zaak.getVerzoek();
    setIfPresent(overledene.getVoornamen(), verzoek::setVoorn);
    setIfPresent(overledene.getGeslachtsnaam(), verzoek::setGeslNaam);
    setIfPresent(overledene.getVoorvoegsel(), verzoek::setVoorv);
    setIfPresent(overledene.getTitelPredikaat(), verzoek::setTitel);
    setIfPresent(overledene.getGeboortedatum(), verzoek::setGeboortedatum);
    setIfPresent(overledene.getGeboorteplaats(), value -> verzoek.setGeboorteplaats(value.getWaarde()));
    setIfPresent(overledene.getGeboorteland(), value -> verzoek.setGeboorteland(Integer.valueOf(value.getWaarde())));
  }

  private void setPersoon(HeeftContactgegevens persoon, DossierPersoon dossierPersoon) {
    Bsn bsnObj = new Bsn(persoon.getBsn());
    if (!bsnObj.isCorrect()) {
      throw new ProException(ERROR, "Bsn {0} is incorrect", bsnObj.getDefaultBsn());
    }
    if (dossierPersoon.getBurgerServiceNummer().isCorrect()
        && !bsnObj.getLongBsn().equals(dossierPersoon.getBurgerServiceNummer().getLongValue())) {
      throw new ProException("BSN's van de personen kunnen niet meer worden aangepast");
    }
    BasePLExt persoonslijst = getPersonenWsService().getPersoonslijst(bsnObj.getDefaultBsn());
    if (persoonslijst.getCats().isEmpty()) {
      throw new ProException(ERROR, "Geen persoon met bsn {0} gevonden", bsnObj.getFormatBsn());
    } else {
      BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
    }
  }

  private void setAangifte(GbaRestOverlijdenInGemeente restOverlijden, DossierOverlijdenGemeente zaak) {
    GbaRestOverlijdenAangifte rest = restOverlijden.getAangifte();
    Validate.notNull(rest, "Element aangifte is niet gezet");
    checkGemeenteCode(rest.getPlaats().getWaarde());
    setIfPresent(rest.getPlaats(), value -> zaak.setPlaatsOverlijden(PLAATS.get(value.getWaarde())));
    setIfPresent(rest.getDatum(), value -> zaak.setDatumOverlijden(new DateTime(value)));
    setIfPresent(rest.getTijd(),
        value -> zaak.setTijdOverlijden(new DateTime(0, value.longValue(), TIME_4_DIGITS)));
    setIfPresent(rest.getDocumentType(), value -> zaak.setOntvangenDocument(OntvangenDocument.get(value.getCode())));
  }

  private void checkGemeenteCode(String overlijdenPlaatsCode) {
    String gemeenteCode = getServices().getGebruiker().getGemeenteCode();
    if (NumberUtils.toLong(gemeenteCode) != NumberUtils.toLong(overlijdenPlaatsCode)) {
      throw new IllegalArgumentException(String.format("Plaats van overlijden (%s) komt " +
          "niet overeen met de gemeentecode (%s)", overlijdenPlaatsCode, gemeenteCode));
    }
  }

  private void setAangifte(GbaRestLijkvinding restLijkvinding, DossierLijkvinding zaak) {
    GbaRestLijkvindingAangifte rest = restLijkvinding.getAangifte();
    Validate.notNull(rest, "Element aangifte is niet gezet");
    checkGemeenteCode(rest.getPlaats().getWaarde());
    setIfPresent(rest.getSchriftelijkeAangever(),
        value -> zaak.setSchriftelijkeAangever(SchriftelijkeAangever.get(value.getCode())));
    setIfPresent(rest.getPlaats(), value -> zaak.setPlaatsLijkvinding(PLAATS.get(rest.getPlaats().getWaarde())));
    setIfPresent(rest.getDatum(), value -> zaak.setDatumLijkvinding(new DateTime(value)));
    setIfPresent(rest.getTijd(),
        value -> zaak.setTijdLijkvinding(new DateTime(0, value.longValue(), TIME_4_DIGITS)));
    setIfPresent(rest.getToevoeging(), zaak::setPlaatsToevoeging);
    setIfPresent(rest.getDocumentType(), value -> zaak.setOntvangenDocument(OntvangenDocument.get(value.getCode())));
  }

  private void setLijkbezorging(GbaRestLijkbezorging rest, DossierOverlijdenLijkbezorging zaak) {
    Validate.notNull(rest, "Element lijkbezorging is niet gezet");
    setIfPresent(rest.getDatum(), value -> zaak.setDatumLijkbezorging(new DateTime(value)));
    setIfPresent(rest.getTijd(),
        value -> zaak.setTijdLijkbezorging(new DateTime(0, value.longValue(), TIME_4_DIGITS)));
    setIfPresent(rest.getLijkbezorgingType(),
        value -> zaak.setWijzeLijkBezorging(WijzeLijkbezorging.get(value.getCode())));
    setIfPresent(rest.getBuitenBenelux(), zaak::setBuitenBenelux);
    setIfPresent(rest.getDoodsoorzaakType(), value -> zaak.setDoodsoorzaak(Doodsoorzaak.get(value.getCode())));
    setIfPresent(rest.getLandVanBestemming(), value -> zaak.setLandBestemming(LAND.get(value.getWaarde())));
    setIfPresent(rest.getPlaatsVanBestemming(), zaak::setPlaatsBestemming);
    setIfPresent(rest.getVia(), zaak::setViaBestemming);
    setIfPresent(rest.getVervoermiddel(), zaak::setVervoermiddel);
    setIfPresent(rest.getPlaatsVanOntleding(), zaak::setPlaatsOntleding);
  }

  private void setVerzoek(GbaRestVerzoek rest, DossierOverlijden zaak) {
    Validate.notNull(zaak.getVerzoek(), "Element verzoek is niet gezet");
    DossierOverlijdenVerzoek verzoek = zaak.getVerzoek();
    verzoek.setVerzoekInd(true);
    setCorrespondentie(rest.getCorrespondentie(), zaak.getVerzoek().getCorrespondentie());
    setUittreksels(rest.getUittreksels(), zaak.getVerzoek());
  }

  private void setCorrespondentie(GbaRestOverlijdenCorrespondentie rest, DossCorrDest correspondence) {
    Validate.notNull(rest, "Element correspondentie is niet gezet");
    setIfPresent(rest.getType(),
        value -> correspondence.setCommunicatieType(CommunicatieType.get(value.getCode()).getCode()));
    setIfPresent(rest.getOrganisatie(), correspondence::setOrganisatie);
    setIfPresent(rest.getAfdeling(), correspondence::setAfdeling);
    setIfPresent(rest.getNaam(), correspondence::setNaam);
    setIfPresent(rest.getEmail(), correspondence::setEmail);
    setIfPresent(rest.getTelefoon(), correspondence::setTelefoon);
    setIfPresent(rest.getStraat(), correspondence::setStraat);
    setIfPresent(rest.getHnr(), correspondence::setHnr);
    setIfPresent(rest.getHnrL(), correspondence::setHnrL);
    setIfPresent(rest.getHnrT(), correspondence::setHnrT);
    setIfPresent(rest.getPostcode(), correspondence::setPostcode);
    setIfPresent(rest.getPlaats(), correspondence::setPlaats);
  }

  private void setUittreksels(List<GbaRestOverlijdenUittreksel> rest, DossierOverlijdenVerzoek verzoek) {
    Validate.notNull(rest, "Element uittreksels is niet gezet");
    if (CommunicatieType.EMAIL == CommunicatieType.get(verzoek.getCorrespondentie().getCommunicatieType())) {
      if (rest.stream().anyMatch(uitt -> uitt.getAantal() != 1)) {
        throw new ProException(WARNING, "Als er via e-mail wordt gecorrespondeerd dan moet altijd 1 aantal " +
            "per document worden opgegeven");
      }
    }
    for (GbaRestOverlijdenUittreksel restUitt : rest) {
      checkUittrekselByCode(restUitt);
      DossOverlUitt uitt = new DossOverlUitt();
      setIfPresent(restUitt.getCode(), uitt::setUittCode);
      setIfPresent(restUitt.getOmschrijving(), uitt::setUittDescr);
      setIfPresent(restUitt.getAantal(), uitt::setUittAmount);
      verzoek.getUittreksels().add(uitt);
    }
  }

  private void checkUittrekselByCode(GbaRestOverlijdenUittreksel uitt) {
    List<DocumentRecord> documenten = getServices().getDocumentService().getDocumentenByAlias(uitt.getCode());
    if (documenten.size() > 1) {
      throw new ProException(WARNING, "Meerdere documenten gevonden met DMS code ''{0}''", uitt.getCode());

    } else if (documenten.isEmpty()) {
      throw new ProException(WARNING, "Document niet gevonden met DMS code ''{0}''", uitt.getCode());
    }
  }

  public GbaRestOverlijden toGbaRestOverlijden(Dossier zaak) {
    GbaRestDossierService dossierService = getRestServices().getDossierService();
    GbaRestOverlijden restOverl = new GbaRestOverlijden();

    switch (zaak.getType()) {
      case OVERLIJDEN_IN_GEMEENTE:
        DossierOverlijdenGemeente overl = (DossierOverlijdenGemeente) zaak.getZaakDossier();
        GbaRestOverlijdenInGemeente restOverlGemeente = new GbaRestOverlijdenInGemeente();
        restOverlGemeente.setAangever(dossierService.getRestPersoon(zaak, AANGEVER));
        restOverlGemeente.setOverledene(dossierService.getRestPersoon(zaak, OVERLEDENE));
        restOverlGemeente.setAangifte(toGbaRestAangifte(overl));
        restOverlGemeente.setVerzoek(toGbaRestVerzoek(overl));
        restOverlGemeente.setLijkbezorging(toGbaRestLijkbezorging(overl));
        restOverl.setOverlijdenInGemeente(restOverlGemeente);
        break;
      case LIJKVINDING:
        DossierLijkvinding lijkv = (DossierLijkvinding) zaak.getZaakDossier();
        GbaRestLijkvinding restLijkvinding = new GbaRestLijkvinding();
        restLijkvinding.setOverledene(dossierService.getRestPersoon(zaak, OVERLEDENE));
        restLijkvinding.setAangifte(toGbaRestAangifte(lijkv));
        restLijkvinding.setVerzoek(toGbaRestVerzoek(lijkv));
        restLijkvinding.setLijkbezorging(toGbaRestLijkbezorging(lijkv));
        restOverl.setLijkvinding(restLijkvinding);
        break;
      default:
        throw new IllegalArgumentException("Onbekend zaaktype voor overlijden: " + zaak.getType());
    }

    return restOverl;
  }

  private GbaRestVerzoek toGbaRestVerzoek(AbstractDossierOverlijden zaak) {
    GbaRestVerzoek rest = new GbaRestVerzoek();
    if (zaak != null) {
      rest.setCorrespondentie(toGbaRestCorrespondentie(zaak.getVerzoek().getCorrespondentie()));
      rest.setUittreksels(toGbaRestUittreksels(zaak.getVerzoek().getUittreksels()));
    }
    return rest;
  }

  private List<GbaRestOverlijdenUittreksel> toGbaRestUittreksels(List<DossOverlUitt> uittreksels) {
    List<GbaRestOverlijdenUittreksel> restUitt = new ArrayList<>();
    for (DossOverlUitt uitt : uittreksels) {
      GbaRestOverlijdenUittreksel gbaUitt = new GbaRestOverlijdenUittreksel();
      setIfPresent(uitt.getUittCode(), gbaUitt::setCode);
      setIfPresent(uitt.getUittDescr(), gbaUitt::setOmschrijving);
      setIfPresent(uitt.getUittAmount(), gbaUitt::setAantal);
      restUitt.add(gbaUitt);
    }
    return restUitt;
  }

  private GbaRestOverlijdenCorrespondentie toGbaRestCorrespondentie(DossCorrDest correspondence) {
    GbaRestOverlijdenCorrespondentie rest = new GbaRestOverlijdenCorrespondentie();
    if (correspondence != null) {
      if (CommunicatieType.get(correspondence.getCommunicatieType()) != CommunicatieType.NVT) {
        GbaRestCommunicatieType[] communicatieTypes = GbaRestCommunicatieType.values();
        setIfPresent(correspondence.getOrganisatie(), rest::setOrganisatie);
        setIfPresent(correspondence.getAfdeling(), rest::setAfdeling);
        setIfPresent(CommunicatieType.get(correspondence.getCommunicatieType()), communicatieTypes, rest::setType);
        setIfPresent(correspondence.getNaam(), rest::setNaam);
        setIfPresent(correspondence.getEmail(), rest::setEmail);
        setIfPresent(correspondence.getTelefoon(), rest::setTelefoon);
        setIfPresent(correspondence.getStraat(), rest::setStraat);
        setIfPresent(correspondence.getHnr(), rest::setHnr);
        setIfPresent(correspondence.getHnrL(), rest::setHnrL);
        setIfPresent(correspondence.getHnrT(), rest::setHnrT);
        setIfPresent(correspondence.getPostcode(), rest::setPostcode);
        setIfPresent(correspondence.getPlaats(), rest::setPlaats);
      }
    }
    return rest;
  }

  private GbaRestLijkbezorging toGbaRestLijkbezorging(DossierOverlijdenLijkbezorging zaak) {
    GbaRestLijkbezorging rest = new GbaRestLijkbezorging();
    GbaRestLijkbezorgingType[] lijkbezorgingTypes = GbaRestLijkbezorgingType.values();
    setIfPresent(zaak.getDatumLijkbezorging().getIntDate(), rest::setDatum);
    setIfPresent(zaak.getTijdLijkbezorging().getIntTime(), rest::setTijd);
    setIfPresent(zaak.getWijzeLijkBezorging(), lijkbezorgingTypes, rest::setLijkbezorgingType);
    setIfPresent(zaak.isBuitenBenelux(), rest::setBuitenBenelux);
    setIfPresent(zaak.getDoodsoorzaak(), GbaRestDoodsoorzaakType.values(), rest::setDoodsoorzaakType);
    setIfPresent(zaak.getLandBestemming(), rest::setLandVanBestemming);
    setIfPresent(zaak.getPlaatsBestemming(), rest::setPlaatsVanBestemming);
    setIfPresent(zaak.getViaBestemming(), rest::setVia);
    setIfPresent(zaak.getVervoermiddel(), rest::setVervoermiddel);
    setIfPresent(zaak.getPlaatsOntleding(), rest::setPlaatsVanOntleding);
    return rest;
  }

  private GbaRestOverlijdenAangifte toGbaRestAangifte(DossierOverlijdenGemeente zaak) {
    GbaRestOverlijdenAangifte rest = new GbaRestOverlijdenAangifte();
    setIfPresent(zaak.getPlaatsOverlijden(), rest::setPlaats);
    setIfPresent(zaak.getDatumOverlijden().getIntDate(), rest::setDatum);
    setIfPresent(zaak.getTijdOverlijden().getIntTime(), rest::setTijd);
    setIfPresent(zaak.getOntvangenDocument(), GbaRestDocumentType.values(), rest::setDocumentType);
    return rest;
  }

  private GbaRestLijkvindingAangifte toGbaRestAangifte(DossierLijkvinding zaak) {
    GbaRestLijkvindingAangifte rest = new GbaRestLijkvindingAangifte();
    GbaRestSchriftelijkeAangeverType[] aangeverTypes = GbaRestSchriftelijkeAangeverType.values();
    setIfPresent(zaak.getSchriftelijkeAangever(), aangeverTypes, rest::setSchriftelijkeAangever);
    setIfPresent(zaak.getPlaatsLijkvinding(), rest::setPlaats);
    setIfPresent(zaak.getDatumLijkvinding().getIntDate(), rest::setDatum);
    setIfPresent(zaak.getTijdLijkvinding().getIntTime(), rest::setTijd);
    setIfPresent(zaak.getOntvangenDocument(), GbaRestDocumentType.values(), rest::setDocumentType);
    setIfPresent(zaak.getPlaatsToevoeging(), rest::setToevoeging);
    return rest;
  }

  private OverlijdenService getOverlijdenService(ZaakType zaakType) {
    switch (zaakType) {
      case OVERLIJDEN_IN_GEMEENTE:
        return getServices().getOverlijdenGemeenteService();
      case LIJKVINDING:
        return getServices().getLijkvindingService();
      default:
        throw new IllegalArgumentException("Onbekend zaaktype voor overlijden: " + zaakType);
    }
  }

  private PersonenWsService getPersonenWsService() {
    return getServices().getPersonenWsService();
  }

}
