/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.beheer.locatie;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Printoptie;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanPrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.NaturalComparator;

public class Locatie extends Location
    implements KoppelbaarAanGebruiker, KoppelbaarAanPrintOptie, Comparable<Locatie>, DatabaseTable {

  private static final String BEGIN_ERROR_COUPLE_MESSAGE = "Object van type ";
  private static final String END_ERROR_COUPLE_MESSAGE   = " kan niet gekoppeld worden aan een locatie.";
  private boolean             matchesIp                  = false;

  public Locatie() {
  }

  public Locatie(Long cLocation) {
    this();
    setCLocation(cLocation);
  }

  public static Locatie getDefault() {
    Locatie g = new Locatie();
    g.setCLocation(BaseEntity.DEFAULT);
    return g;
  }

  @Override
  public int compareTo(Locatie o) {
    return NaturalComparator.compareTo(getLocatie(), o.getLocatie());
  }

  public List<String> getIpAdressen() {
    List<String> l = new ArrayList<>();
    for (String ip : getIp().split(",|\r|\n")) {
      if (fil(ip.trim())) {
        l.add(ip.trim());
      }
    }

    return l;
  }

  public String getLocatie() {
    return getLocation();
  }

  public void setLocatie(String locatie) {
    setLocation(locatie);
  }

  public LocatieType getLocatieType() {
    return LocatieType.get(getType().longValue());
  }

  public void setLocatieType(LocatieType type) {
    setType(toBigDecimal(type.getCode()));
  }

  public String getOmschrijving() {
    return getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    setDescr(omschrijving);
  }

  @Override
  public boolean isGekoppeld(Gebruiker gebruiker) {
    return isStored() && MiscUtils.contains(gebruiker, getUsrs());
  }

  public <K extends KoppelbaarAanLocatie> boolean isGekoppeld(K object) {

    if (object instanceof PrintOptie) {
      PrintOptie printOptie = (PrintOptie) object;
      return isGekoppeld(printOptie);
    } else if (object instanceof Gebruiker) {
      Gebruiker user = (Gebruiker) object;
      return isGekoppeld(user);
    } else {
      throw new IllegalArgumentException(
          BEGIN_ERROR_COUPLE_MESSAGE + object.getClass() + END_ERROR_COUPLE_MESSAGE);
    }
  }

  public <K extends KoppelbaarAanLocatie> boolean isGekoppeld(List<K> objectList) {
    for (K object : objectList) {
      if (!isGekoppeld(object)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isGekoppeld(PrintOptie printOptie) {
    return MiscUtils.contains(printOptie, getPrintopties());
  }

  public boolean isMatchesIp() {
    return matchesIp;
  }

  public void setMatchesIp(boolean matchesIp) {
    this.matchesIp = matchesIp;
  }

  @Override
  public boolean isPrintOptiesGekoppeld(List<PrintOptie> printopties) {

    for (PrintOptie po : printopties) {
      if (!isGekoppeld(po)) {
        return false;
      }
    }
    return true;
  }

  public <K extends KoppelbaarAanLocatie> void koppelActie(K koppelobject, KoppelActie koppelActie) {
    if (koppelobject instanceof Gebruiker) {
      koppelActie((Gebruiker) koppelobject, koppelActie);
    } else if (koppelobject instanceof PrintOptie) {
      koppelActie((PrintOptie) koppelobject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_ERROR_COUPLE_MESSAGE + koppelobject.getClass() + END_ERROR_COUPLE_MESSAGE);
    }
  }

  @Override
  public void koppelActie(Gebruiker gebruiker, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getUsrs().add(ReflectionUtil.deepCopyBean(nl.procura.gba.jpa.personen.db.Usr.class, gebruiker));
    } else {
      getUsrs().remove(gebruiker);
    }
  }

  @Override
  public void koppelActie(PrintOptie printOptie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getPrintopties().add(ReflectionUtil.deepCopyBean(Printoptie.class, printOptie));
    } else {
      getPrintopties().remove(printOptie);
    }
  }

  @Override
  public String toString() {
    return getLocation();
  }
}
