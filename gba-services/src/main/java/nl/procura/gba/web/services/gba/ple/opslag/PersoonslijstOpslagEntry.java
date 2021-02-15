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

package nl.procura.gba.web.services.gba.ple.opslag;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.services.gba.templates.ZoekProfielType;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class PersoonslijstOpslagEntry {

  private final boolean         zoekIndicaties;
  private final PLEDatasource   databron;
  private final ZoekProfielType profielType;
  private BasePLExt             pl = new BasePLExt();

  private long anr = -1;
  private long bsn = -1;

  public PersoonslijstOpslagEntry(ZoekProfielType profielType,
      PLEDatasource databron,
      boolean zoekIndicaties,
      BasePLExt pl) {

    this.anr = along(pl.getPersoon().getAnr().getCode());
    this.bsn = along(pl.getPersoon().getBsn().getCode());

    this.profielType = profielType;
    this.databron = databron;
    this.zoekIndicaties = zoekIndicaties;
    this.pl = pl;
  }

  public PersoonslijstOpslagEntry(ZoekProfielType profielType, PLEDatasource databron,
      boolean zoekIndicaties, long nummer) {

    this.profielType = profielType;
    this.databron = databron;
    this.zoekIndicaties = zoekIndicaties;

    if (Anummer.isCorrect(String.valueOf(nummer))) {
      this.anr = nummer;
    } else if (Bsn.isCorrect(String.valueOf(nummer))) {
      this.bsn = nummer;
    }
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof PersoonslijstOpslagEntry) {

      PersoonslijstOpslagEntry entry = (PersoonslijstOpslagEntry) obj;
      boolean isDatabron = (getDatabron() == entry.getDatabron());
      boolean isProfielType = (getProfielType() == entry.getProfielType());
      boolean isIndicaties = (isZoekIndicaties() == entry.isZoekIndicaties());
      boolean isBsn = (pos(getBsn()) && pos(entry.getBsn()) && along(getBsn()) == along(
          entry.getBsn()));
      boolean isAnr = (pos(getAnr()) && pos(entry.getAnr()) && along(getAnr()) == along(
          entry.getAnr()));

      return isDatabron && isProfielType && (isBsn || isAnr) && isIndicaties;
    }

    return false;
  }

  public long getAnr() {
    return anr;
  }

  public long getBsn() {
    return bsn;
  }

  public PLEDatasource getDatabron() {
    return databron;
  }

  public BasePLExt getPl() {
    return pl;
  }

  public ZoekProfielType getProfielType() {
    return profielType;
  }

  public boolean isZoekIndicaties() {
    return zoekIndicaties;
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder();
    toString.append("[");
    if (pos(anr)) {
      toString.append("anr=");
      toString.append(anr);
      toString.append(", ");
    }
    if (pos(bsn)) {
      toString.append("bsn=");
      toString.append(bsn);
    }
    toString.append("]");
    return toString.toString();
  }
}
