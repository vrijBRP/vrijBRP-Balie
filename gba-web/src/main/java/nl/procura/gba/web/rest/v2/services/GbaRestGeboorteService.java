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

import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.toTableRecord;
import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.geboorte.processen.GeboorteProcessen;
import nl.procura.gba.web.rest.v2.model.base.GbaRestGeslacht;
import nl.procura.gba.web.rest.v2.model.base.HeeftContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.namenrecht.*;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.erkenning.GbaRestErkenningsType;
import nl.procura.gba.web.rest.v2.model.zaken.geboorte.*;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Bsn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestGeboorteService extends GbaRestAbstractService {

  @Transactional
  public Zaak addGeboorte(GbaRestZaak restZaak) {
    GeboorteService service = getServices().getGeboorteService();
    Dossier dossier = (Dossier) service.getNewZaak();
    DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();
    getRestServices().getZaakService().addGenericZaak(restZaak, dossier);

    GbaRestGeboorte restGeboorte = restZaak.getGeboorte();
    setPersoon(restGeboorte.getAangever(), geboorte.getAangever());
    setPersoon(restGeboorte.getMoeder(), geboorte.getMoeder());
    setPersoon(restGeboorte.getVaderOfDuoMoeder(), geboorte.getVader());
    getRestServices().getContactService().setContactGegevens(restGeboorte.getAangever());
    setKinderen(restGeboorte.getKinderen(), geboorte);
    setAangifte(restGeboorte, geboorte);
    setVerzoek(restGeboorte, geboorte);
    dossier.reset();

    ZaakCommentaren commentaar = dossier.getCommentaren();
    commentaar.verwijderen().toevoegenWarn(GeboorteProcessen
        .getProcesStatussen(dossier)
        .getMessages());

    // Save
    DossierService dossierService = getServices().getDossierService();
    dossierService.saveDossier(dossier);
    dossierService.saveZaakDossier(geboorte);
    service.save(dossier);

    getRestServices()
        .getZaakService()
        .addZaakIds(restZaak, dossier);

    return dossier;
  }

  public static GbaRestNamenrecht toGbaRestGeboorteNamenrecht(DossierNamenrecht geboorte) {
    GbaRestNamenrecht restNr = new GbaRestNamenrecht();
    restNr.setGeslachtsnaam(geboorte.getKeuzeGeslachtsnaam());
    restNr.setVoorvoegsel(geboorte.getKeuzeVoorvoegsel());
    restNr.setTitelPredikaat(geboorte.getKeuzeTitel().getStringValue());

    if (geboorte.isNaamRechtBepaald()) {
      restNr.setLandToegepastRecht(toTableRecord(geboorte.getLandNaamRecht()));
      restNr.setEersteKind(toEnum(GbaRestEersteKindType.values(),
          geboorte.getEersteKindType().getCode()));
      restNr.setNaamsPersoonType(toEnum(GbaRestNaamsPersoonType.values(),
          geboorte.getNaamskeuzePersoon().getCode()));
      restNr.setNaamskeuze(toEnum(GbaRestNaamskeuzeType.values(),
          geboorte.getNaamskeuzeType().getCode()));
    }
    return restNr;
  }

  private void setPersoon(HeeftContactgegevens persoon, DossierPersoon dossierPersoon) {
    if (persoon != null) {
      Bsn bsnObj = new Bsn(persoon.getBsn());
      if (!bsnObj.isCorrect()) {
        throw new ProException(ERROR, "Bsn {0} is incorrect", bsnObj.getDefaultBsn());
      }
      BasePLExt persoonslijst = getServices().getPersonenWsService().getPersoonslijst(bsnObj.getDefaultBsn());
      if (persoonslijst.getCats().isEmpty()) {
        throw new ProException(ERROR, "Geen persoon met bsn {0} gevonden", bsnObj.getFormatBsn());
      } else {
        BsPersoonUtils.kopieDossierPersoon(persoonslijst, dossierPersoon);
      }
    }
  }

  private void setKinderen(List<GbaRestKind> restKinderen, DossierGeboorte geboorte) {
    FieldValue gemeente = PLAATS.get(getServices().getGebruiker().getGemeenteCode());
    if (!pos(gemeente.getValue())) {
      throw new ProException(WARNING, "Kan de huidige gemeente niet bepalen.");
    }

    for (GbaRestKind restKind : restKinderen) {
      if (pos(new ProcuraDate().diffInDays(new ProcuraDate(restKind.getGeboortedatum())))) {
        throw new ProException(WARNING, "De geboortedatum kan niet in de toekomst liggen.");
      }

      DossierPersoon kind = new DossierPersoon();
      kind.setVoornaam(restKind.getVoornamen());
      kind.setGeslacht(Geslacht.get(restKind.getGeslacht().getCode()));
      kind.setDatumGeboorte(new GbaDateFieldValue(new DateTime(restKind.getGeboortedatum())));
      kind.setTijdGeboorte(new DateTime(0, along(restKind.getGeboortetijd())));
      kind.setGeboorteplaats(gemeente);
      kind.setGeboorteland(Landelijk.getNederland());
      getServices().getDossierService().addKind(geboorte, kind);
    }
  }

  private void setAangifte(GbaRestGeboorte restGeboorte, DossierGeboorte geboorte) {
    GbaRestGezinssituatieType gezinssituatie = restGeboorte.getGezinssituatie();
    if (gezinssituatie != null) {
      switch (gezinssituatie) {
        case BINNEN_HETERO_HUWELIJK:
          geboorte.setGezinssituatie(GezinssituatieType.BINNEN_HETERO_HUWELIJK);
          break;
        default:
          throw new ProException("Onbekende waarde: gezinssituatie " + gezinssituatie);
      }
    }

    GbaRestGeboorteAangifte aangifte = restGeboorte.getAangifte();
    if (aangifte != null) {
      GbaRestRedenVerplichtOfBevoegdType redenVerplicht = aangifte.getRedenVerplichtOfBevoegd();
      if (redenVerplicht != null) {
        geboorte.setRedenVerplichtBevoegd(RedenVerplicht.get(aangifte.getRedenVerplichtOfBevoegd().getCode()));
      }
    }

    GbaRestNamenrecht namenrecht = restGeboorte.getNamenrecht();
    if (namenrecht != null) {
      GbaRestNaamsPersoonType naamskeuzePersoon = namenrecht.getNaamsPersoonType();
      if (naamskeuzePersoon != null) {
        switch (naamskeuzePersoon) {
          case MOEDER:
            geboorte.setNaamskeuzePersoon(NaamsPersoonType.MOEDER);
            break;
          case VADER_OF_DUO_MOEDER:
            geboorte.setNaamskeuzePersoon(NaamsPersoonType.VADER_DUO_MOEDER);
            break;
          default:
            throw new ProException("Onbekende waarde: naamskeuze persoon " + naamskeuzePersoon);
        }
      }

      geboorte.setKeuzeGeslachtsnaam(namenrecht.getGeslachtsnaam());
      geboorte.setKeuzeVoorvoegsel(namenrecht.getVoorvoegsel());
      geboorte.setKeuzeTitel(new FieldValue(namenrecht.getTitelPredikaat()));
    }
  }

  private void setVerzoek(GbaRestGeboorte restGeboorte, DossierGeboorte geboorte) {
    GbaRestGeboorteVerzoek restVerzoek = restGeboorte.getVerzoek();
    if (restVerzoek != null) {
      geboorte.setVerzoekInd(true);
      if (restVerzoek.getVaderOfDuoMoeder() != null) {
        Bsn bsnObj = new Bsn(restVerzoek.getVaderOfDuoMoeder().getBsn());
        if (bsnObj.isCorrect()) {
          geboorte.setVerzoekBsnVaderDuoMoeder(BigDecimal.valueOf(bsnObj.getLongBsn()));
        }
      }

      geboorte.setVerzoekKeuzeNaamGesl(restVerzoek.getGeslachtsnaam());
      geboorte.setVerzoekKeuzeNaamVoorv(restVerzoek.getVoorvoegsel());
      geboorte.setVerzoekKeuzeNaamTitel(restVerzoek.getTitelPredikaat());
    }
  }

  public GbaRestGeboorte toGbaRestGeboorte(Dossier zaak) {
    GbaRestDossierService dossierService = getRestServices().getDossierService();
    DossierGeboorte geboorte = (DossierGeboorte) zaak.getZaakDossier();
    GbaRestGeboorte restGeb = new GbaRestGeboorte();
    restGeb.setAangever(dossierService.getRestPersoon(zaak, AANGEVER));
    restGeb.setMoeder(dossierService.getRestPersoon(zaak, MOEDER));
    restGeb.setVaderOfDuoMoeder(dossierService.getRestPersoon(zaak, VADER_DUO_MOEDER));
    restGeb.setAangifte(toGbaRestGeboorteAangifte(geboorte));
    restGeb.setKinderen(toGbaRestKinderen(zaak));
    restGeb.setGezinssituatie(toEnum(GbaRestGezinssituatieType.values(),
        geboorte.getGezinssituatie().getCode()));
    restGeb.setAfstamming(toGbaRestGeboorteAfstamming(geboorte));
    restGeb.setNamenrecht(toGbaRestGeboorteNamenrecht(geboorte));
    restGeb.setErkenning(toGbaRestGeboorteErkenning(geboorte));
    restGeb.setNationaliteiten(dossierService.toGbaRestPersoonNatios(zaak));
    restGeb.setAktes(dossierService.toGbaRestAktes(zaak));
    restGeb.setVerzoek(toGbaRestGeboorteVerzoek(geboorte));
    return restGeb;
  }

  private GbaRestAfstamming toGbaRestGeboorteAfstamming(DossierGeboorte geboorte) {
    GbaRestAfstamming restAfst = new GbaRestAfstamming();
    restAfst.setWoonlandKind(toTableRecord(geboorte.getVerblijfsLandAfstamming()));
    restAfst.setToegepastRechtLand(toTableRecord(geboorte.getLandAfstammingsRecht()));
    return restAfst;
  }

  private List<GbaRestKind> toGbaRestKinderen(Dossier zaak) {
    List<GbaRestKind> restKinderen = new ArrayList<>();
    for (DossierPersoon persoon : zaak.getPersonen(KIND)) {
      if (persoon.isVolledig()) {
        GbaRestKind restKind = new GbaRestKind();
        restKind.setVoornamen(persoon.getVoornaam());
        restKind.setGeslacht(toEnum(GbaRestGeslacht.values(), persoon.getGeslacht().getAfkorting()));
        restKind.setGeboortedatum(persoon.getDatumGeboorte().toInt());
        restKind.setGeboortetijd(persoon.getTijdGeboorte().getIntTime());
        restKinderen.add(restKind);
      }
    }
    return restKinderen;
  }

  private GbaRestGeboorteErkenning toGbaRestGeboorteErkenning(DossierGeboorte geboorte) {
    GbaRestGeboorteErkenning restErk = new GbaRestGeboorteErkenning();
    restErk.setErkenningsType(toEnum(GbaRestErkenningsType.values(), geboorte.getErkenningsType().getCode()));
    if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      restErk.setZaakId(geboorte.getErkenningVoorGeboorte().getDoss().getZaakId());
    } else if (geboorte.getVragen().heeftErkenningBijGeboorte()) {
      restErk.setZaakId(geboorte.getErkenningBijGeboorte().getDoss().getZaakId());
    }
    return restErk;
  }

  private GbaRestGeboorteVerzoek toGbaRestGeboorteVerzoek(DossierGeboorte geboorte) {
    if (!geboorte.isVerzoekInd()) {
      return null;
    }
    GbaRestGeboorteVerzoek restVerzoek = new GbaRestGeboorteVerzoek();
    GbaRestPersoon vaderDuoMoeder = new GbaRestPersoon();
    long bsnVaderDuoMoeder = geboorte.getVerzoekBsnVaderDuoMoeder().longValue();
    if (bsnVaderDuoMoeder > 0) {
      vaderDuoMoeder.setBsn(geboorte.getVerzoekBsnVaderDuoMoeder().longValue());
    }
    restVerzoek.setVaderOfDuoMoeder(vaderDuoMoeder);
    restVerzoek.setGeslachtsnaam(geboorte.getVerzoekKeuzeNaamGesl());
    restVerzoek.setVoorvoegsel(geboorte.getVerzoekKeuzeNaamVoorv());
    restVerzoek.setTitelPredikaat(geboorte.getVerzoekKeuzeNaamTitel());
    return restVerzoek;
  }

  private GbaRestGeboorteAangifte toGbaRestGeboorteAangifte(DossierGeboorte geboorte) {
    GbaRestGeboorteAangifte restAangifte = new GbaRestGeboorteAangifte();
    restAangifte.setRedenVerplichtOfBevoegd(toEnum(GbaRestRedenVerplichtOfBevoegdType.values(),
        geboorte.getRedenVerplichtBevoegd().getCode()));
    restAangifte.setTardieveAangifte(geboorte.isTardieveAangifte());
    restAangifte.setTardieveAangifteReden(geboorte.getTardieveReden());
    return restAangifte;
  }
}
