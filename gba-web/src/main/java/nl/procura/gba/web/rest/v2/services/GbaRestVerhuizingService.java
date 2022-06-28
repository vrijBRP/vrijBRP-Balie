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

import static nl.procura.gba.common.ZaakType.VERHUIZING;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.toBsn;
import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.toTableRecord;
import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;
import static nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestVerhuisduurType.KORTER;
import static nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestVerhuisduurType.LANGER;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestTabelWaarde;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.*;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.inwoning.GbaRestInwoningVraag;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.verhuizing.*;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever.AangifteStatus;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever.ToestemmingStatus;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GbaRestVerhuizingService extends GbaRestAbstractService {

  public GbaRestVerhuizingService(GbaRestServices restServices, Services services) {
    super(restServices, services);
  }

  public Zaak addVerhuizing(GbaRestZaak restZaak) {
    VerhuizingService service = getServices().getVerhuizingService();
    VerhuisAanvraag va = (VerhuisAanvraag) service.getNewZaak();
    getRestServices().getZaakService().addGenericZaak(restZaak, va);

    GbaRestVerhuizing restVerh = restZaak.getVerhuizing();
    VerhuisType typeVerhuizing = VerhuisType.get(restVerh.getType().getCode());
    va.setTypeVerhuizing(typeVerhuizing);
    va.setBestemdVoorMidoffice(false);

    // Declarant
    GbaRestAangever restAangever = restVerh.getAangever();
    if (restAangever != null) {
      VerhuisAangever aangever = new VerhuisAangever(va);
      aangever.setAnummer(new AnrFieldValue(-1L));
      aangever.setBurgerServiceNummer(new BsnFieldValue(restAangever.getBsn()));
      aangever.setToelichting(restAangever.getToelichting());
      aangever.setAmbtshalve(false); // Derived
      aangever.setHoofdInstelling(false); // Derived
      getRestServices().getContactService().addContactInfo(restAangever);
      va.setAangever(aangever);
    }

    // New Address
    va.setBestemmingHuidigeBewoners(restVerh.getBestemmmingHuidigeBewoners());
    GbaRestBinnenverhuizing restBinnen = restVerh.getBinnenverhuizing();
    if (typeVerhuizing == VerhuisType.BINNENGEMEENTELIJK) {
      if (restBinnen != null) {
        toNieuwAdres(va, restBinnen.getNieuwAdres());
      } else {
        throw new ProException("Element 'binnenverhuizing' is leeg");
      }
    }

    FieldValue gemHerkomst = null;
    if (typeVerhuizing == VerhuisType.INTERGEMEENTELIJK) {
      GbaRestBuitenverhuizing restBuiten = restVerh.getBuitenverhuizing();
      if (restBuiten == null) {
        throw new ProException("Element 'buitenverhuizing' is leeg");
      }
      toNieuwAdres(va, restBuiten.getNieuwAdres());
      GbaRestTabelWaarde restGemHerkomst = restBuiten.getGemeenteVanHerkomst();
      gemHerkomst = new FieldValue(restGemHerkomst.getWaarde(), restGemHerkomst.getOmschrijving());
    }

    if (typeVerhuizing == VerhuisType.HERVESTIGING) {
      GbaRestHervestiging restHervest = restVerh.getHervestiging();
      if (restHervest == null) {
        throw new ProException("Element 'hervestiging' is leeg");
      }
      toNieuwAdres(va, restHervest.getNieuwAdres());
      GbaRestTabelWaarde land = restHervest.getLand();
      va.getHervestiging().setDatumHervestiging(new DateTime(restHervest.getDatum().longValue()));
      va.getHervestiging().setDuur(restHervest.getDuur().getCode());
      va.getHervestiging().setRechtsfeiten(restHervest.getRechtsfeiten());
      va.getHervestiging().setLand(new FieldValue(land.getWaarde(), land.getOmschrijving()));
    }

    if (typeVerhuizing == VerhuisType.EMIGRATIE) {
      GbaRestEmigratie restEmigr = restVerh.getEmigratie();
      if (restEmigr == null) {
        throw new ProException("Element 'emigratie' is leeg");
      }
      GbaRestTabelWaarde land = restEmigr.getLand();
      va.getEmigratie().setAdres1(restEmigr.getAdres1());
      va.getEmigratie().setAdres2(restEmigr.getAdres2());
      va.getEmigratie().setAdres3(restEmigr.getAdres3());
      va.getEmigratie().setDuur(restEmigr.getDuur().getCode());
      va.getEmigratie().setDuur(restEmigr.getDuur().getCode());
      va.getEmigratie().setDatumVertrek(new DateTime(restEmigr.getDatumVertrek().longValue()));
      va.getEmigratie().setLand(new FieldValue(land.getWaarde(), land.getOmschrijving()));
    }

    // Main occupant
    GbaRestHoofdbewoner restHb = restVerh.getHoofdbewoner();
    if (restHb != null) {
      va.getHoofdbewoner().setBurgerServiceNummer(new BsnFieldValue(restHb.getBsn()));
    }
    setInwoning(va, restVerh.getInwoning());

    // Relocators
    if (restVerh.getVerhuizers() == null || restVerh.getVerhuizers().isEmpty()) {
      throw new ProException("Element 'verhuizers' is leeg");
    }
    for (GbaRestVerhuizer restVerhuizer : restVerh.getVerhuizers()) {
      VerhuisPersoon vp = new VerhuisPersoon();
      vp.setAangifte(AangifteSoort.get(restVerhuizer.getAangifte().getCode()));
      vp.setAnummer(new AnrFieldValue(-1L));
      vp.setBurgerServiceNummer(new BsnFieldValue(restVerhuizer.getBsn()));
      vp.setGeenVerwerking(BooleanUtils.isFalse(restVerhuizer.getVerwerken()));
      vp.setGemeenteHerkomst(gemHerkomst);
      va.getPersonen().add(vp);
    }
    boolean noConsent = ToestemmingStatus.NIET_INGEVULD == va.getToestemminggever().getToestemmingStatus();
    if (va.isSprakeVanInwoning() && noConsent) {
      service.save(va, false, "In afwachting van toestemming voor inwoning");
    } else {
      service.save(va);
    }

    getRestServices()
        .getZaakService()
        .addZaakIds(restZaak, va);

    return va;
  }

  public void updateInwoning(GbaRestInwoningVraag request) {
    VerhuisAanvraag verh = getRestServices()
        .getZaakService()
        .getZaakByZaakId(request.getZaakId(), VERHUIZING);

    setInwoning(verh, request.getInwoning());
    getServices().getVerhuizingService().saveToestemming(verh);
  }

  public GbaRestVerhuizing toGbaRestVerhuizing(VerhuisAanvraag zaak) {
    GbaRestVerhuizing restVerh = new GbaRestVerhuizing();
    restVerh.setType(toEnum(GbaRestVerhuisType.values(), zaak.getTypeVerhuizing().getCode()));
    restVerh.setBestemmmingHuidigeBewoners(zaak.getBestemmingHuidigeBewoners());
    restVerh.setAangever(getAangever(zaak));
    restVerh.setHoofdbewoner(getHoofdbewoner(zaak));
    restVerh.setInwoning(getInwoning(zaak));
    restVerh.setBinnenverhuizing(getBinnenverhuizing(zaak));
    restVerh.setBuitenverhuizing(getBuitenverhuizing(zaak));
    restVerh.setEmigratie(getEmigratie(zaak));
    restVerh.setHervestiging(getHervestiging(zaak));
    restVerh.setVerhuizers(getVerhuizers(zaak));
    return restVerh;
  }

  private void toNieuwAdres(VerhuisAanvraag va, GbaRestVerhuizingBinnenlandsAdres restVa) {
    VerhuisAanvraagAdres nieuwAdres = va.getNieuwAdres();
    nieuwAdres.setAantalPersonen(restVa.getAantalPersonen());
    nieuwAdres.setFunctieAdres(FunctieAdres.get(restVa.getFunctieAdres().getCode()));
    nieuwAdres.setHnr(restVa.getHnr());
    nieuwAdres.setHnrL(restVa.getHnrL());
    nieuwAdres.setHnrT(restVa.getHnrT());
    nieuwAdres.setHnrA("");
    nieuwAdres.setPc(new FieldValue(restVa.getPostcode()));
    nieuwAdres.setStraat(new FieldValue(restVa.getStraat()));
    GbaRestTabelWaarde gemeente = restVa.getGemeente();
    String woonplaats = restVa.getWoonplaats();

    if (woonplaats != null) {
      nieuwAdres.setWoonplaats(new FieldValue(woonplaats));
    }
    if (gemeente != null) {
      nieuwAdres.setGemeente(new FieldValue(gemeente.getWaarde(), gemeente.getOmschrijving()));
    }
  }

  private List<GbaRestVerhuizer> getVerhuizers(VerhuisAanvraag zaak) {
    List<GbaRestVerhuizer> list = new ArrayList<>();
    for (VerhuisPersoon p : zaak.getPersonen()) {
      GbaRestVerhuizer verhuizer = new GbaRestVerhuizer();
      verhuizer.setBsn(toBsn(p.getBurgerServiceNummer()));
      verhuizer.setAangifte(toEnum(GbaRestAangifteSoort.values(), p.getAangifte().getCode()));
      verhuizer.setVerwerken(!p.isGeenVerwerking());
      list.add(verhuizer);
    }
    return list;
  }

  private GbaRestBinnenverhuizing getBinnenverhuizing(VerhuisAanvraag zaak) {
    if (zaak.getTypeVerhuizing() == VerhuisType.BINNENGEMEENTELIJK) {
      GbaRestBinnenverhuizing restVerh = new GbaRestBinnenverhuizing();
      restVerh.setNieuwAdres(toBinnenlandsAdres(zaak.getNieuwAdres()));
      return restVerh;
    }
    return null;
  }

  private GbaRestBuitenverhuizing getBuitenverhuizing(VerhuisAanvraag zaak) {
    if (zaak.getTypeVerhuizing() == VerhuisType.INTERGEMEENTELIJK) {
      GbaRestBuitenverhuizing restVerh = new GbaRestBuitenverhuizing();
      restVerh.setGemeenteVanHerkomst(toTableRecord(PLAATS.get(zaak.getCGemHerkomst())));
      restVerh.setNieuwAdres(toBinnenlandsAdres(zaak.getNieuwAdres()));
      return restVerh;
    }
    return null;
  }

  private GbaRestHervestiging getHervestiging(VerhuisAanvraag zaak) {
    VerhuisHerVestiging hervestiging = zaak.getHervestiging();
    if (zaak.getTypeVerhuizing() == VerhuisType.HERVESTIGING && hervestiging != null) {
      GbaRestHervestiging restVerh = new GbaRestHervestiging();
      restVerh.setNieuwAdres(toBinnenlandsAdres(zaak.getNieuwAdres()));
      restVerh.setDatum(hervestiging.getDatumHervestiging().getIntDate());
      restVerh.setDuur(hervestiging.getDuur().toLowerCase().contains("korter") ? KORTER : LANGER);
      restVerh.setLand(toTableRecord(hervestiging.getLand()));
      restVerh.setRechtsfeiten(hervestiging.getRechtsfeiten());
      return restVerh;
    }
    return null;
  }

  private GbaRestEmigratie getEmigratie(VerhuisAanvraag zaak) {
    VerhuisEmigratie emigratie = zaak.getEmigratie();
    if (zaak.getTypeVerhuizing() == VerhuisType.EMIGRATIE && emigratie != null) {
      GbaRestEmigratie restVerh = new GbaRestEmigratie();
      restVerh.setAdres1(emigratie.getAdres1());
      restVerh.setAdres2(emigratie.getAdres2());
      restVerh.setAdres3(emigratie.getAdres3());
      restVerh.setDatumVertrek(emigratie.getDatumVertrek().getIntDate());
      restVerh.setDuur(emigratie.getDuur().toLowerCase().contains("korter") ? KORTER : LANGER);
      restVerh.setLand(toTableRecord(emigratie.getLand()));
      return restVerh;
    }
    return null;
  }

  private GbaRestVerhuizingBinnenlandsAdres toBinnenlandsAdres(VerhuisAanvraagAdres ha) {
    if (ha != null) {
      if (StringUtils.isNotBlank(ha.getPc().getStringValue())) {
        GbaRestVerhuizingBinnenlandsAdres restAdres = new GbaRestVerhuizingBinnenlandsAdres();
        restAdres.setGemeente(toTableRecord(ha.getGemeente()));
        restAdres.setWoonplaats(ha.getWoonplaats().getDescription());
        restAdres.setLocatie(ha.getLocatie().getDescription());
        restAdres.setPostcode(ha.getPc().getStringValue());
        restAdres.setStraat(ha.getStraat().getStringValue());
        restAdres.setAantalPersonen(ha.getAantalPersonen());
        restAdres.setFunctieAdres(toEnum(GbaRestFunctieAdres.values(), ha.getFunctieAdres().getCode()));
        restAdres.setHnr((int) ha.getHnr());
        restAdres.setHnrL(ha.getHnrL());
        restAdres.setHnrT(ha.getHnrT());
        return restAdres;
      }
    }
    return null;
  }

  private GbaRestAangever getAangever(VerhuisAanvraag zaak) {
    VerhuisAangever aangever = zaak.getAangever();
    if (aangever != null && aangever.getBurgerServiceNummer().isCorrect()) {
      GbaRestAangever restAang = new GbaRestAangever();
      restAang.setBsn(toBsn(aangever.getBurgerServiceNummer()));
      restAang.setContactgegevens(getRestServices().getContactService().toGbaRestContactgegevens(restAang));
      restAang.setAmbtshalve(aangever.isAmbtshalve());
      restAang.setHoofdInstelling(aangever.isHoofdInstelling());
      restAang.setToelichting(aangever.getToelichting());
      return restAang;
    }
    return null;
  }

  private GbaRestHoofdbewoner getHoofdbewoner(VerhuisAanvraag zaak) {
    VerhuisHoofdbewoner hoofdbewoner = zaak.getHoofdbewoner();
    if (hoofdbewoner != null && hoofdbewoner.getBurgerServiceNummer().isCorrect()) {
      return new GbaRestHoofdbewoner(toBsn(hoofdbewoner.getBurgerServiceNummer()));
    }
    return null;
  }

  private GbaRestInwoning getInwoning(VerhuisAanvraag zaak) {
    GbaRestInwoning restInw = new GbaRestInwoning();
    restInw.setSprakeVanInwoning(zaak.isSprakeVanInwoning());
    VerhuisToestemminggever toest = zaak.getToestemminggever();
    if (toest != null) {
      restInw.setToestemminggever(getToestemminggever(toest));
      restInw.setAangifteStatus(toEnum(GbaRestAangifteStatus.values(),
          toest.getAangifteStatus().name()));
      restInw.setToestemmingStatus(toEnum(GbaRestToestemmingStatus.values(),
          toest.getToestemmingStatus().name()));
    }

    return restInw;
  }

  private GbaRestToestemminggever getToestemminggever(VerhuisToestemminggever toest) {
    if (toest != null) {
      if (toest.getBurgerServiceNummer().isCorrect() || StringUtils.isNotBlank(toest.getAnders())) {
        GbaRestToestemminggever restToest = new GbaRestToestemminggever();
        restToest.setBsn(toBsn(toest.getBurgerServiceNummer()));
        restToest.setAnders(toest.getAnders());
        return restToest;
      }
    }
    return null;
  }

  private void setInwoning(VerhuisAanvraag va, GbaRestInwoning restInwoning) {
    va.setSprakeVanInwoning(false);
    if (restInwoning != null) {
      va.setSprakeVanInwoning(BooleanUtils.isTrue(restInwoning.getSprakeVanInwoning()));
      GbaRestToestemminggever restTg = restInwoning.getToestemminggever();
      if (restTg != null && BooleanUtils.isTrue(restInwoning.getSprakeVanInwoning())) {
        VerhuisToestemminggever toestemminggever = va.getToestemminggever();
        toestemminggever.setAangifteStatus(AangifteStatus.valueOf(restInwoning.getAangifteStatus().getCode()));
        toestemminggever.setToestemmingStatus(ToestemmingStatus.valueOf(restInwoning.getToestemmingStatus().getCode()));
        toestemminggever.setBurgerServiceNummer(new BsnFieldValue(restTg.getBsn()));
        toestemminggever.setAnders(restTg.getAnders());
      }
    }
  }
}
