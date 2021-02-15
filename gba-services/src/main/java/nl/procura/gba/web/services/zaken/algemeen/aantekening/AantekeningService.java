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

package nl.procura.gba.web.services.zaken.algemeen.aantekening;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus.OPEN;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.*;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.AantekeningDao;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.GpkDao;
import nl.procura.gba.jpa.personen.db.Aantekening;
import nl.procura.gba.jpa.personen.db.AantekeningHist;
import nl.procura.gba.jpa.personen.db.AantekeningInd;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;

public class AantekeningService extends AbstractService {

  public AantekeningService() {
    super("Aantekeningen");
  }

  @ThrowException("Fout bij ophalen aantekening indicaties")
  public List<PlAantekeningIndicatie> getAantekeningIndicaties() {
    return copyList(findEntity(new AantekeningInd()), PlAantekeningIndicatie.class);
  }

  public AantekeningHistorie getPersoonAantekeningen(PlAantekeningStatus... statussen) {

    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(getServices().getPersonenWsService().getHuidige());

    List<PlAantekening> aantekeningen = new ArrayList<>();
    aantekeningen.addAll(getAantekeningen(zaakArgumenten));

    // Kladblokaantekening ook toevoegen
    PlAantekening kladblokAantekening = getKladblokAantekening();

    if (kladblokAantekening != null) {
      aantekeningen.add(0, kladblokAantekening);
    }

    AantekeningHistorie historie = new AantekeningHistorie();
    historie.setAantekeningen(getAantekeningen(aantekeningen, statussen));

    return historie;
  }

  public Set<PlAantekeningIndicatie> getToegestaneIndicaties() {
    Set<PlAantekeningIndicatie> indicaties = new HashSet<>();
    for (Profiel profiel : getServices().getGebruiker().getProfielen().getAlle()) {
      for (PlAantekeningIndicatie indicatie : profiel.getIndicaties().getAlle()) {
        if (indicatie.isAantekening()) {
          indicaties.add(indicatie);
        }
      }
    }

    return indicaties;
  }

  public AantekeningHistorie getZaakAantekeningen(Zaak zaak) {
    AantekeningHistorie aantekeningen = new AantekeningHistorie();
    aantekeningen.setAantekeningen(getAantekeningen(new ZaakArgumenten(zaak)));
    return aantekeningen;
  }

  @Transactional
  @ThrowException("Fout bij opslaan")
  public void save(PlAantekening plAantekening, PlAantekeningHistorie plAantekeningHistorie) {

    PlAantekeningHistorie laatste = plAantekening.getLaatsteHistorie();

    if (laatste != null) {
      boolean isInhoud = laatste.getInhoud().equals(plAantekeningHistorie.getInhoud());
      boolean isStatus = (laatste.getHistorieStatus() == plAantekeningHistorie.getHistorieStatus());

      if (isInhoud && isStatus) {
        throw new ProException(ProExceptionType.ENTRY, ProExceptionSeverity.INFO,
            "De inhoud moet eerst worden gewijzigd");
      }
    }

    saveEntity(plAantekening);

    Aantekening aantekening = copy(plAantekening, Aantekening.class);
    AantekeningHist aantekeningHist = copy(plAantekeningHistorie, AantekeningHist.class);
    aantekeningHist.setAantekening(aantekening);
    aantekeningHist.setUsr(GenericDao.find(Usr.class, along(plAantekeningHistorie.getGebruiker().getValue())));

    saveEntity(aantekeningHist);

    plAantekeningHistorie.setCAantekeningHist(aantekeningHist.getCAantekeningHist());

    plAantekening.getHistorie().add(0, plAantekeningHistorie);

    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij opslaan aantekening indicatie")
  public void save(PlAantekeningIndicatie indicatie) {
    saveEntity(indicatie);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(PlAantekening plAantekening) {
    removeEntity(plAantekening);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen aantekening indicatie")
  public void delete(PlAantekeningIndicatie indicatie) {
    removeEntity(indicatie);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    removeEntities(getZaakAantekeningen(zaak).getAantekeningen());
  }

  private List<PlAantekening> getAantekeningen(List<PlAantekening> aantekeningen, PlAantekeningStatus... statussen) {
    List<PlAantekening> openAantekeningen = new ArrayList<>();
    for (PlAantekening aantekening : aantekeningen) {
      if (aantekening.getLaatsteHistorie().getHistorieStatus().is(statussen)) {
        openAantekeningen.add(aantekening);
      }
    }
    return openAantekeningen;
  }

  private List<PlAantekening> getAantekeningen(ZaakArgumenten zaakArgumenten) {

    List<PlAantekening> aantekeningen = new ArrayList<>();
    Set<PlAantekeningIndicatie> toegestaneIndicaties = getToegestaneIndicaties();

    for (Aantekening g : AantekeningDao.find(getArgumentenToMap(zaakArgumenten))) {

      PlAantekening aa = copy(g, PlAantekening.class);
      boolean toevoegen = g.getAantekeningHists().size() > 0;

      for (PlAantekeningHistorie ah : copyList(g.getAantekeningHists(), PlAantekeningHistorie.class)) {

        ah.setTijdstip(new DateTime(ah.getDIn(), ah.getTIn()));
        ah.setGebruiker(getGebruiker(ah.getUsr().getCUsr()));
        aa.getHistorie().add(ah);

        // Tonen als aantekeningindicatie voorkomt in lijst met
        // toegestane indicaties voor de ingelogde gebruiker

        if (!toegestaneIndicaties.contains(ah.getAantekeningInd())) {
          toevoegen = false;
        }
      }

      if (toevoegen) {
        Collections.sort(aa.getHistorie());
        aantekeningen.add(aa);
      }
    }

    return aantekeningen;
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    ConditionalMap map = new ConditionalMap();
    if (!zaakArgumenten.isAll()) {
      map.putLong(AantekeningDao.BSN, zaakArgumenten.getBsn());
      map.putString(GpkDao.ZAAK_ID, zaakArgumenten.getZaakKey().getZaakId());
    }

    return map;
  }

  private PlAantekening getKladblokAantekening() {
    String kladblokInhoud = getKladblokInhoud();
    PlAantekening kladblok = null;

    if (fil(kladblokInhoud)) {
      kladblok = new PlAantekening();
      PlAantekeningHistorie historie = new PlAantekeningHistorie();

      historie.setOnderwerp("Aantekening op de persoonslijst");
      historie.setIndicatie(new PlAantekeningIndicatie(true));
      historie.setInhoud(kladblokInhoud);
      historie.setHistorieStatus(OPEN);

      kladblok.getHistorie().add(historie);
    }

    return kladblok;
  }

  private String getKladblokInhoud() {
    StringBuilder sb = new StringBuilder();
    BasePLExt pl = getServices().getPersonenWsService().getHuidige();
    BasePLCat soort = pl.getCat(GBACat.KLADBLOK);

    for (BasePLSet set : soort.getSets()) {
      for (BasePLRec r : set.getRecs()) {
        String line = r.getElemVal(GBAElem.KLADBLOK_REGEL).getDescr();
        sb.append(line).append("\n");
      }
    }

    return sb.toString();
  }
}
