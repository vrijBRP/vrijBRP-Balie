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

package nl.procura.gba.web.services.beheer.gebruiker.info;

import static nl.procura.standard.Globalfunctions.*;

import java.util.Comparator;
import java.util.List;

import nl.procura.gba.jpa.personen.dao.UsrInfoDao;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.UsrInfo;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class GebruikerInfoService extends AbstractService {

  private static final long GEEN_GEBRUIKER = 0;

  public GebruikerInfoService() {
    super("Profiel-gebruiker-info");
  }

  @ThrowException("Fout bij ophalen van gebruiker-info")
  public GebruikerInformatie getGebruikerInfo(Gebruiker gebruiker) {

    GebruikerInformatie infoList = new GebruikerInformatie();

    for (GebruikerInfoType t : GebruikerInfoType.values()) {

      GebruikerInfo type = setInfo(infoList, t.getKey());
      type.setKey(t.getKey());
      type.setDescr(t.getDescr());
      type.setValue("");
    }

    List<Object[]> list = UsrInfoDao.findDistinctInfo();

    for (Object[] row : list) {

      GebruikerInfo type = setInfo(infoList, astr(row[0]));
      type.setDescr(astr(row[1]));
      type.setValue("");
    }

    for (UsrInfo info : UsrInfoDao.findUsrInfo(gebruiker.getCUsr())) {

      GebruikerInfo type = setInfo(infoList, info.getKey());

      if (info.getUsr().getCUsr().equals(GEEN_GEBRUIKER)) {
        type.setStandaardWaarde(info.getValue());
      } else if (info.getUsr().getCUsr() > GEEN_GEBRUIKER) {
        type.setGebruikerWaarde(info.getValue());
      }

      if (info.getUsr().getCUsr() > type.getUsr().getCUsr()) {
        type.setCUsrInfo(info.getCUsrInfo());
        type.setUsr(info.getUsr());
        type.setValue(info.getValue());
      }
    }

    infoList.getAlles().sort(new GebruikerInfoComparator());

    return infoList;
  }

  public boolean heeftInfo(Gebruiker gebruiker, GebruikerInfoType type) {
    GebruikerInfo info = getGebruikerInfo(gebruiker).getInfo(type);
    return info != null && fil(info.getWaarde());
  }

  /**
   * Extra gebruikersgegevens opslaan. Gebruiker is de geraadpleegde gebruiker en in info staat of de gebruikergegevens
   * voor deze gebruiker of voor iedereen moeten worden opgeslagen. Moet het gegeven voor iedereen opgeslagen worden dan wordt
   * er een nieuw Usr object aangemaakt.
   */
  @ThrowException("Fout bij het opslaan van de gebruiker-info")
  @Transactional
  public void save(GebruikerInfo info) {

    UsrInfo usrInfo = new UsrInfo();
    usrInfo.setKey(info.getInfo());
    long cUsr = info.getGebruiker() == null ? 0 : info.getGebruiker().getCUsr();

    if (emp(info.getWaarde()) && cUsr > 0) { // als de waarde voor een gebruiker leeg is, dan
      verwijderGebruikerInfo(info); // wordt dit record uit de database verwijderd
    } else {

      Usr u = findEntity(Usr.class, cUsr);
      usrInfo.setUsr(u);

      usrInfo = findRecord(usrInfo); // overschrijf met databaseItem als deze bestaat
      usrInfo.setValue(trim(info.getWaarde()));
      usrInfo.setDescr(info.getOmschrijving());

      saveEntity(usrInfo);
    }
  }

  public void save(GebruikerInfoType type, Gebruiker gebruiker, String waarde) {

    GebruikerInfo info = getGebruikerInfo(gebruiker).getInfo(type);
    info.setGebruiker(gebruiker);
    info.setWaarde(waarde);

    save(info);
  }

  @ThrowException("Fout bij het verwijderen van gebruiker-info")
  @Transactional
  public void deleteAlles(GebruikerInfo info) {

    UsrInfo usrInfo = new UsrInfo();
    usrInfo.setKey(info.getInfo());

    for (UsrInfo ui : findEntity(usrInfo)) {
      removeEntity(ui);
    }
  }

  private UsrInfo findRecord(UsrInfo usrInfo) {

    for (UsrInfo ui : findEntity(usrInfo)) {
      usrInfo = ui;
    }

    return usrInfo;
  }

  private GebruikerInfo getInfo(GebruikerInformatie informatie, String key) {

    for (GebruikerInfo p : informatie.getAlles()) {
      if (eq(p.getInfo(), key)) {
        return p;
      }
    }

    return null;
  }

  private GebruikerInfo setInfo(GebruikerInformatie informatie, String key) {

    GebruikerInfo index = getInfo(informatie, key);

    if (index == null) {
      index = new GebruikerInfo(key);
      Usr usr = new Usr();
      usr.setCUsr(Usr.EMPTY);
      index.setUsr(usr);
      informatie.getAlles().add(index);
    }

    return index;
  }

  @ThrowException("Fout bij het opslaan van de gebruiker-info")
  private void verwijderGebruikerInfo(GebruikerInfo gebrInfo) {

    UsrInfo usrInfo = new UsrInfo();
    usrInfo.setKey(gebrInfo.getInfo());

    Gebruiker gebr = gebrInfo.getGebruiker();
    UsrInfo usrInfoForGebr = zoekGebruikerInfo(usrInfo,
        gebr); // zoek het desbetreffende item bij de gebruiker: gebruiker == null

    if (usrInfoForGebr.getKey() != null) { // daadwerkelijk een item gevonden
      removeEntity(usrInfoForGebr);
    }
  }

  private UsrInfo zoekGebruikerInfo(UsrInfo usrInfo, Gebruiker gebruiker) {

    boolean recordExists = false;
    UsrInfo usrInfoForGebr = new UsrInfo();
    long cUsr = (gebruiker == null) ? 0 : gebruiker.getCUsr();

    usrInfoForGebr.setKey(usrInfo.getKey());
    usrInfoForGebr.setUsr(findEntity(Usr.class, cUsr));

    for (UsrInfo ui : findEntity(usrInfoForGebr)) {
      usrInfoForGebr = ui;
      recordExists = true;
    }

    if (recordExists) {
      return usrInfoForGebr;
    }

    return new UsrInfo();
  }

  public static class GebruikerInfoComparator implements Comparator<GebruikerInfo> {

    @Override
    public int compare(GebruikerInfo o1, GebruikerInfo o2) {
      return o1.getOmschrijving().compareTo(o2.getOmschrijving());
    }
  }
}
