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

package nl.procura.rdw.functions;

public class ProcesMelding {

  private RdwProces       proces       = null;
  private String          systeem      = "RDW";
  private long            nr           = 0;
  private long            ripNr        = 0;
  private String          meldingKort  = "Onbekend";
  private RdwMeldingSoort meldingSoort = RdwMeldingSoort.ONBEKEND;
  private String          meldingVar   = "Onbekend";

  public String getSysteem() {
    return systeem;
  }

  public void setSysteem(String systeem) {
    this.systeem = systeem;
  }

  public long getNr() {
    return nr;
  }

  public void setNr(long nr) {
    this.nr = nr;
  }

  public String getMeldingKort() {
    return meldingKort;
  }

  public void setMeldingKort(String meldingKort) {
    this.meldingKort = meldingKort;
  }

  public String getMeldingVar() {
    return meldingVar;
  }

  public void setMeldingVar(String meldingVar) {
    this.meldingVar = meldingVar;
  }

  public RdwMeldingSoort getMeldingSoort() {
    return meldingSoort;
  }

  public void setMeldingSoort(RdwMeldingSoort meldingSoort) {
    this.meldingSoort = meldingSoort;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("ProcesMelding [systeem=");
    builder.append(systeem);
    builder.append(", nr=");
    builder.append(nr);
    builder.append(", meldingKort=");
    builder.append(meldingKort);
    builder.append(", meldingSoort=");
    builder.append(meldingSoort);
    builder.append(", meldingVar=");
    builder.append(meldingVar);
    builder.append("]");

    return builder.toString();
  }

  public RdwProces getProces() {
    return proces;
  }

  public void setProces(RdwProces proces) {
    this.proces = proces;
  }

  public long getRipNr() {
    return ripNr;
  }

  public void setRipNr(long ripNr) {
    this.ripNr = ripNr;
  }
}
