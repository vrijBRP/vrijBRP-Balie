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

package nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.HuwLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.java.reflection.ReflectionUtil;

public class HuwelijksLocatie extends HuwLocatie implements Geldigheid, DatabaseTable {

  private static final long serialVersionUID = -2699045488639619407L;

  private final HuwelijksLocatieContactpersoon contactpersoon;
  private List<HuwelijksLocatieOptie>          opties = null;

  public HuwelijksLocatie() {
    setHuwelijksLocatie("");
    setSoort("");
    setToelichting("");
    contactpersoon = new HuwelijksLocatieContactpersoon(this);
  }

  public static HuwelijksLocatie getDefault() {
    HuwelijksLocatie g = new HuwelijksLocatie();
    g.setCodeHuwelijksLocatie(BaseEntity.DEFAULT);
    return g;
  }

  public List<String> getAliassen() {
    List<String> aliassen = new ArrayList<>();
    for (String alias : StringUtils.defaultIfBlank(getAlias(), "").split(",")) {
      if (StringUtils.isNotBlank(alias)) {
        aliassen.add(alias.trim());
      }
    }

    return aliassen;
  }

  public void setAliassen(List<String> aliassen) {
    setAlias(StringUtils.join(aliassen, ","));
  }

  public Long getCodeHuwelijksLocatie() {
    return getCHuwLocatie();
  }

  public void setCodeHuwelijksLocatie(long code) {
    setCHuwLocatie(code);
  }

  public HuwelijksLocatieContactpersoon getContactpersoon() {
    return contactpersoon;
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getdEnd());
  }

  @Override
  public void setDatumEinde(DateTime dateTime) {
    setdEnd(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  @Override
  public void setDatumIngang(DateTime dateTime) {
    setdIn(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public String getHuwelijksLocatie() {
    return getHuwLocatie();
  }

  public void setHuwelijksLocatie(String huwelijksLocatie) {
    setHuwLocatie(huwelijksLocatie);
  }

  public HuwelijksLocatieSoort getLocatieSoort() {
    return HuwelijksLocatieSoort.get(getSoort());
  }

  public void setLocatieSoort(HuwelijksLocatieSoort soort) {
    setSoort(soort.getCode());
  }

  public List<HuwelijksLocatieOptie> getOpties() {

    if (opties == null) {
      opties = new ArrayList<>(ReflectionUtil.deepCopyBeans(HuwelijksLocatieOptie.class, getHuwLocatieOpties()));
    }

    return opties;
  }
}
