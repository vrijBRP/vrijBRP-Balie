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
import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;
import static nl.procura.gba.web.rest.v2.model.zaken.huwelijk.GbaRestHuwelijkOptieType.TEKST;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.ALLES;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.huwelijk.processen.HuwelijkProcessen;
import nl.procura.gba.web.rest.v2.model.base.HeeftContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.huwelijk.*;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijkOptie;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Bsn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestHuwelijkService extends GbaRestAbstractService {

  @Transactional
  public Zaak addHuwelijk(GbaRestZaak restZaak) {
    Dossier dossier = (Dossier) getHuwelijkService().getNewZaak();
    getRestServices().getZaakService().addGenericZaak(restZaak, dossier);
    updateHuwelijk(restZaak, dossier);

    getRestServices()
        .getZaakService()
        .addZaakIds(restZaak, dossier);

    return dossier;
  }

  @Transactional
  public Zaak updateHuwelijk(GbaRestZaak restZaak, Dossier dossier) {
    DossierHuwelijk huwelijk = (DossierHuwelijk) dossier.getZaakDossier();
    GbaRestHuwelijk restHuwelijk = restZaak.getHuwelijk();

    setPartners(huwelijk, restHuwelijk);
    setPlanning(restHuwelijk, huwelijk);
    setLocatie(restHuwelijk, huwelijk);
    setGetuigen(restHuwelijk, huwelijk);
    setAmbtenaren(restHuwelijk, huwelijk);
    setNaamgebruik(restHuwelijk, huwelijk);
    dossier.reset();

    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(HuwelijkProcessen
        .getProcesStatussen(dossier)
        .getMessages());

    DossierService dossierService = getServices().getDossierService();
    dossierService.saveDossier(dossier);
    dossierService.saveZaakDossier(huwelijk);
    getHuwelijkService().save(dossier);

    return dossier;
  }

  private void setPartners(DossierHuwelijk huwelijk, GbaRestHuwelijk restHuwelijk) {
    GbaRestHuwelijkPartner partner1 = restHuwelijk.getPartner1();
    GbaRestHuwelijkPartner partner2 = restHuwelijk.getPartner2();
    setPersoon(partner1.getPersoon(), huwelijk.getPartner1());
    setPersoon(partner2.getPersoon(), huwelijk.getPartner2());
    getRestServices().getContactService().setContactGegevens(partner1.getPersoon());
    getRestServices().getContactService().setContactGegevens(partner2.getPersoon());
  }

  private void setPersoon(HeeftContactgegevens persoon, DossierPersoon dossierPersoon) {
    Bsn bsnObj = new Bsn(persoon.getBsn());
    if (!bsnObj.isCorrect()) {
      throw new ProException(ERROR, "Bsn {0} is incorrect", bsnObj.getDefaultBsn());
    }
    if (dossierPersoon.getBurgerServiceNummer().isCorrect()
        && !bsnObj.getLongBsn().equals(dossierPersoon.getBurgerServiceNummer().getLongValue())) {
      throw new ProException("BSN's van de partners kunnen niet meer worden aangepast");
    }
    BasePLExt persoonslijst = getPersonenWsService().getPersoonslijst(bsnObj.getDefaultBsn());
    if (persoonslijst.getCats().isEmpty()) {
      throw new ProException(ERROR, "Geen persoon met bsn {0} gevonden", bsnObj.getFormatBsn());
    } else {
      BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
    }
  }

  private void setPlanning(GbaRestHuwelijk restHuwelijk, DossierHuwelijk huwelijk) {
    GbaRestHuwelijkPlanning planning = restHuwelijk.getPlanning();
    GbaRestHuwelijkVerbintenisType soort = planning.getSoort();
    Integer datumVerb = planning.getDatumVerbintenis();
    Integer tijdVerb = planning.getTijdVerbintenis();
    Integer datumVoorn = planning.getDatumVoornemen();
    String toelichting = planning.getToelichting();
    huwelijk.setSoortVerbintenis(SoortVerbintenis.get(soort.getCode()));
    huwelijk.setDatumVerbintenis(new DateTime(datumVerb.longValue()));
    huwelijk.setTijdVerbintenis(new DateTime(0, tijdVerb.longValue(), TIME_4_DIGITS));
    huwelijk.setDatumVoornemen(new DateTime(-1L));
    if (datumVoorn != null) {
      huwelijk.setDatumVoornemen(new DateTime(datumVoorn.longValue()));
    }
    huwelijk.setToelichtingVerbintenis(toelichting);
  }

  private void setLocatie(GbaRestHuwelijk restHuwelijk, DossierHuwelijk huwelijk) {
    getHuwelijkService().resetLocatie(huwelijk);

    // Add location
    GbaRestHuwelijkLocatie locatie = restHuwelijk.getLocatie();
    if (locatie != null && StringUtils.isNotBlank(locatie.getNaam())) {
      HuwelijksLocatie huwelijksLocatie = getLocatie(locatie);
      huwelijk.setHuwelijksLocatie(huwelijksLocatie);
      setOpties(locatie.getOpties(), huwelijk);
    }
  }

  private HuwelijksLocatie getLocatie(GbaRestHuwelijkLocatie locatie) {
    AliasCollection locaties = new AliasCollection(locatie.getAliassen(), locatie.getNaam());
    for (HuwelijksLocatie huwLocatie : getHuwelijkService().getHuwelijksLocaties(ALLES)) {
      if (new AliasCollection(huwLocatie.getAliassen(), huwLocatie.getHuwelijksLocatie()).matches(locaties)) {
        return huwLocatie;
      }
    }
    throw new ProException(ERROR, "Geen huwelijkslocatie gevonden voor " + locaties);
  }

  private void setOpties(List<GbaRestHuwelijkOptie> opties, DossierHuwelijk huwelijk) {
    if (huwelijk.getHuwelijksLocatie() != null && opties != null) {
      List<DossierHuwelijkOptie> beschikbareOpties = getHuwelijkService().getDossierHuwelijksOpties(huwelijk);
      List<DossierHuwelijkOptie> gewijzigdeOpties = new ArrayList<>();
      for (GbaRestHuwelijkOptie optie : opties) {
        DossierHuwelijkOptie dossierOptie = getOptie(optie, beschikbareOpties);
        dossierOptie.setWaarde(optie.getWaarde());
        gewijzigdeOpties.add(dossierOptie);
      }
      huwelijk.setOpties(gewijzigdeOpties);
    }
  }

  private DossierHuwelijkOptie getOptie(GbaRestHuwelijkOptie optie, List<DossierHuwelijkOptie> dossierHuwelijksOpties) {
    AliasCollection opties = new AliasCollection(optie.getAliassen(), optie.getNaam());
    for (DossierHuwelijkOptie dossierOptie : dossierHuwelijksOpties) {
      HuwelijksLocatieOptie huwOpt = dossierOptie.getOptie();
      if (new AliasCollection(huwOpt.getAliassen(), huwOpt.getHuwelijksLocatieOptie()).matches(opties)) {
        return dossierOptie;
      }
    }

    throw new ProException(ERROR, "Geen huwelijksoptie gevonden voor " + opties);
  }

  private void setGetuigen(GbaRestHuwelijk restHuwelijk, DossierHuwelijk huwelijk) {
    huwelijk.resetGemeenteGetuigen();
    getHuwelijkService().deleteGetuigen(huwelijk);

    // Add witnesses
    GbaRestHuwelijkGetuigen getuigen = restHuwelijk.getGetuigen();
    if (getuigen != null) {
      if (getuigen.getAantalGemeenteGetuigen() != null) {
        huwelijk.setGemeenteGetuigen(getuigen.getAantalGemeenteGetuigen());
      }

      int index = 1;
      for (GbaRestHuwelijkGetuige getuige : getuigen.getEigenGetuigen()) {
        DossierPersoon persoon = new DossierPersoon(GETUIGE);
        setGetuige(index++, getuige, persoon);
        huwelijk.getDossier().toevoegenPersoon(persoon);
      }
    }
  }

  private void setGetuige(int index, GbaRestHuwelijkGetuige getuige, DossierPersoon dossierPersoon) {
    getRestServices().getDossierService().toDossierPersoon(getuige.getPersoon(), dossierPersoon);
    if (getuige.getPersoon().getBsn() != null) {
      String bsn = getuige.getPersoon().getBsn().toString();
      BasePLExt persoonslijst = getPersonenWsService().getPersoonslijst(bsn);
      if (persoonslijst.getCats().isEmpty()) {
        throw new ProException(ERROR, "Geen getuige met bsn {0} gevonden", bsn);
      } else {
        BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
        dossierPersoon.setVolgorde((long) index);
      }
    }
  }

  private void setAmbtenaren(GbaRestHuwelijk restHuwelijk, DossierHuwelijk huwelijk) {
    huwelijk.resetAmbtenaren();
    GbaRestHuwelijkAmbtenaren ambtenaren = restHuwelijk.getAmbtenaren();
    if (ambtenaren != null) {
      if (ambtenaren.getVoorkeuren().size() > 0) {
        setAmbtenaar(ambtenaren.getVoorkeuren().get(0), huwelijk.getAmbtenaar1());
      }
      if (ambtenaren.getVoorkeuren().size() > 1) {
        setAmbtenaar(ambtenaren.getVoorkeuren().get(1), huwelijk.getAmbtenaar2());
      }
      if (ambtenaren.getVoorkeuren().size() > 2) {
        throw new ProException(ERROR, "Maximaal 2 ambtenaren toegestaan");
      }
    }
  }

  private void setAmbtenaar(GbaRestHuwelijkAmbtenaar restPersoon, DossierPersoon persoon) {
    if (restPersoon != null) {
      for (HuwelijksAmbtenaar ambtenaar : getHuwelijkService().getHuwelijksAmbtenaren(ALLES)) {
        if (new AliasCollection(ambtenaar.getAliassen(), ambtenaar.getHuwelijksAmbtenaar())
            .matches(new AliasCollection(restPersoon.getAliassen(), restPersoon.getNaam()))) {
          BsnFieldValue bsn = ambtenaar.getBurgerServiceNummer();
          if (bsn.isCorrect()) {
            BasePLExt persoonslijst = getPersonenWsService().getPersoonslijst(bsn.getStringValue());
            BsPersoonUtils.kopieDossierPersoon(persoonslijst, persoon);
            persoon.setAktenaam(ambtenaar.getHuwelijksAmbtenaar());
            return;
          }
        }
      }

      throw new ProException(ERROR, "Geen ambtenaar genaamd {0} gevonden", restPersoon.getNaam());
    } else {
      BsPersoonUtils.reset(persoon);
    }
  }

  private void setNaamgebruik(GbaRestHuwelijk restHuwelijk, DossierHuwelijk huwelijk) {
    huwelijk.resetNaamGebruikPartner1();
    huwelijk.resetNaamGebruikPartner2();

    // Add name use
    GbaRestHuwelijkNaamgebruik naamgebruik1 = restHuwelijk.getPartner1().getNaamgebruik();
    GbaRestHuwelijkNaamgebruik naamgebruik2 = restHuwelijk.getPartner2().getNaamgebruik();

    if (naamgebruik1 != null) {
      huwelijk.setNaamGebruikPartner1(getNaamgebruikType(naamgebruik1, huwelijk.getPartner1()));
      huwelijk.setTitelPartner1(new FieldValue(naamgebruik1.getTitelPredikaat()));
      huwelijk.setVoorvPartner1(naamgebruik1.getVoorvoegsel());
      huwelijk.setNaamPartner1(naamgebruik1.getGeslachtsnaam());
    }

    if (naamgebruik2 != null) {
      huwelijk.setNaamGebruikPartner2(getNaamgebruikType(naamgebruik2, huwelijk.getPartner2()));
      huwelijk.setTitelPartner2(new FieldValue(naamgebruik2.getTitelPredikaat()));
      huwelijk.setVoorvPartner2(naamgebruik2.getVoorvoegsel());
      huwelijk.setNaamPartner2(naamgebruik2.getGeslachtsnaam());
    }
  }

  private String getNaamgebruikType(GbaRestHuwelijkNaamgebruik naamgebruik, DossierPersoon partner) {
    if (partner != null && partner.isVolledig() && naamgebruik.getType() != null) {
      String type = naamgebruik.getType().getCode();
      if (isNaamgebruikGewijzigd(partner, type)) {
        return type;
      }
    }
    return null;
  }

  public boolean isNaamgebruikGewijzigd(DossierPersoon persoon, String nieuwNaamgebruikCode) {
    return persoon.getNaamgebruik() == null || !persoon.getNaamgebruik().equalsIgnoreCase(nieuwNaamgebruikCode);
  }

  public GbaRestHuwelijk toGbaRestHuwelijk(Dossier zaak) {
    GbaRestDossierService dossierService = getRestServices().getDossierService();
    DossierHuwelijk huw = (DossierHuwelijk) zaak.getZaakDossier();
    GbaRestHuwelijk restHuw = new GbaRestHuwelijk();

    GbaRestHuwelijkPartner partner1 = new GbaRestHuwelijkPartner();
    partner1.setPersoon(dossierService.getRestPersoon(zaak, PARTNER1));
    partner1.setNaamgebruik(toGbaRestHuwelijkNaam(huw.getNaamGebruikPartner1(),
        huw.getTitelPartner1(),
        huw.getVoorvPartner1(),
        huw.getNaamPartner1()));

    GbaRestHuwelijkPartner partner2 = new GbaRestHuwelijkPartner();
    partner2.setPersoon(dossierService.getRestPersoon(zaak, PARTNER2));
    partner2.setNaamgebruik(toGbaRestHuwelijkNaam(huw.getNaamGebruikPartner2(),
        huw.getTitelPartner2(),
        huw.getVoorvPartner2(),
        huw.getNaamPartner2()));

    restHuw.setPartner1(partner1);
    restHuw.setPartner2(partner2);
    restHuw.setPlanning(toGbaRestPlanning(huw));
    restHuw.setLocatie(toGbaRestLocatie(huw));
    restHuw.setAmbtenaren(toGbaRestHuwelijkAmbtenaren(huw));
    restHuw.setGetuigen(toGbaRestHuwelijkGetuigen(huw));
    return restHuw;
  }

  private GbaRestHuwelijkPlanning toGbaRestPlanning(DossierHuwelijk huw) {
    GbaRestHuwelijkPlanning restVerb = new GbaRestHuwelijkPlanning();
    if (StringUtils.isNotBlank(huw.getSoortVerbintenis().getCode())) {
      restVerb.setSoort(toEnum(GbaRestHuwelijkVerbintenisType.values(), huw.getSoortVerbintenis().getCode()));
    }
    restVerb.setToelichting(huw.getToelichtingVerbintenis());
    restVerb.setDatumVerbintenis(huw.getDatumVerbintenis().getIntDate());
    restVerb.setTijdVerbintenis(huw.getTijdVerbintenis().getIntTime());

    if (huw.getDatumVoornemen() != null && huw.getDatumVoornemen().getIntDate() >= 0) {
      restVerb.setDatumVoornemen(huw.getDatumVoornemen().getIntDate());
    }
    return restVerb;
  }

  private GbaRestHuwelijkLocatie toGbaRestLocatie(DossierHuwelijk huw) {
    GbaRestHuwelijkLocatie gbaRestLoc = new GbaRestHuwelijkLocatie();
    gbaRestLoc.setNaam(huw.getHuwelijksLocatie().getHuwelijksLocatie());
    gbaRestLoc.setAliassen(huw.getHuwelijksLocatie().getAliassen());
    gbaRestLoc.setOpties(toGbaRestOpties(huw.getOpties()));
    return gbaRestLoc;
  }

  private List<GbaRestHuwelijkOptie> toGbaRestOpties(List<DossierHuwelijkOptie> opties) {
    List<GbaRestHuwelijkOptie> restOpties = new ArrayList<>();
    for (DossierHuwelijkOptie dossierHuwelijkOptie : opties) {
      HuwelijksLocatieOptie optie = dossierHuwelijkOptie.getOptie();
      GbaRestHuwelijkOptie restOptie = new GbaRestHuwelijkOptie();
      restOptie.setType(isBlank(optie.getType())
          ? TEKST
          : toEnum(GbaRestHuwelijkOptieType.values(), optie.getType()));
      restOptie.setNaam(optie.getHuwelijksLocatieOptie());
      restOptie.setAliassen(optie.getAliassen());
      restOptie.setWaarde(dossierHuwelijkOptie.getWaarde());
      restOptie.setOmschrijving(optie.getHuwelijksLocatieOptieOms());
      restOpties.add(restOptie);
    }
    return restOpties;
  }

  private GbaRestHuwelijkAmbtenaren toGbaRestHuwelijkAmbtenaren(DossierHuwelijk huw) {
    GbaRestHuwelijkAmbtenaren restAmbt = new GbaRestHuwelijkAmbtenaren();
    if (StringUtils.isNotBlank(huw.getAmbtenaar1().getAktenaam())) {
      restAmbt.addVoorkeur(toGbaRestHuwelijkAmbtenaar(huw.getAmbtenaar1()));
    }
    if (StringUtils.isNotBlank(huw.getAmbtenaar2().getAktenaam())) {
      restAmbt.addVoorkeur(toGbaRestHuwelijkAmbtenaar(huw.getAmbtenaar2()));
    }
    restAmbt.setToegekend(toGbaRestHuwelijkAmbtenaar(huw.getAmbtenaar3()));
    return restAmbt;
  }

  private GbaRestHuwelijkAmbtenaar toGbaRestHuwelijkAmbtenaar(DossierPersoon ambtenaar) {
    GbaRestHuwelijkAmbtenaar restAmbt = new GbaRestHuwelijkAmbtenaar();
    restAmbt.setNaam(ambtenaar.getAktenaam());
    HuwelijksAmbtenaar ambt = getHuwelijkService().getHuwelijksambtenaar(ambtenaar.getBurgerServiceNummer());
    if (ambt != null) {
      restAmbt.setNaam(ambt.getNaam());
      restAmbt.setTelefoon(ambt.getTelefoon());
      restAmbt.setEmail(ambt.getEmail());
      restAmbt.setAliassen(ambt.getAliassen());
    }
    return restAmbt;
  }

  private GbaRestHuwelijkGetuigen toGbaRestHuwelijkGetuigen(DossierHuwelijk huwelijk) {
    GbaRestHuwelijkGetuigen restHuw = new GbaRestHuwelijkGetuigen();
    if (huwelijk.getGemeenteGetuigen() >= 0) {
      restHuw.setAantalGemeenteGetuigen(huwelijk.getGemeenteGetuigen());
    }
    if (!huwelijk.getGetuigen().isEmpty()) {
      huwelijk.getGetuigen().stream()
          .filter(DossierPersoon::isVolledig)
          .map(getuige -> new GbaRestHuwelijkGetuige(getRestDossierService().getRestPersoon(getuige)))
          .forEach(restHuw::addEigenGetuige);
    }
    return restHuw;
  }

  private GbaRestHuwelijkNaamgebruik toGbaRestHuwelijkNaam(String naamgebruik,
      FieldValue titel, String voorv, String geslachtsnaam) {
    GbaRestHuwelijkNaamgebruik restNg = new GbaRestHuwelijkNaamgebruik();
    if (StringUtils.isNotBlank(naamgebruik)) {
      restNg.setType(toEnum(GbaRestHuwelijkNaamgebruikType.values(), naamgebruik));
    }
    restNg.setTitelPredikaat(titel.getStringValue());
    restNg.setVoorvoegsel(voorv);
    restNg.setGeslachtsnaam(geslachtsnaam);
    return restNg;
  }

  private HuwelijkService getHuwelijkService() {
    return getServices().getHuwelijkService();
  }

  private PersonenWsService getPersonenWsService() {
    return getServices().getPersonenWsService();
  }

  private GbaRestDossierService getRestDossierService() {
    return getRestServices().getDossierService();
  }

  public static class AliasCollection extends ArrayList<String> {

    public AliasCollection(List<String> listValues, String... values) {
      if (listValues != null) {
        addAll(listValues);
      }
      if (values != null) {
        addAll(Arrays.asList(values));
      }
    }

    @Override
    public String toString() {
      return get(0);
    }

    public boolean matches(AliasCollection collection) {
      return this.stream()
          .anyMatch(value -> collection.stream()
              .anyMatch(otherValue -> value != null && otherValue != null
                  && value.trim().equalsIgnoreCase(otherValue.trim())));
    }
  }
}
