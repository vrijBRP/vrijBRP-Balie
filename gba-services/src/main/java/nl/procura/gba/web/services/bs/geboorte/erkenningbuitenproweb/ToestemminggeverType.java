/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum ToestemminggeverType {

  MOEDER("M", "Moeder uit wie het kind is geboren", "Moeder uit wie het kind is geboren (tot 12 jr.)"),
  MOEDER_EN_KIND("O", "Moeder uit wie het kind is geboren en kind",
      "Moeder uit wie het kind is geboren en kind (tot 16 jr.)"),
  MOEDER_EN_RECHTBANK("P", "Moeder uit wie het kind is geboren en rechtbank",
      "Moeder uit wie het kind is geboren en rechtbank (tot 16 jr.)"),
  KIND("K", "Kind", "Kind (vanaf 16 jr.)"),
  KIND_EN_RECHTBANK("S", "Kind en rechtbank", "Kind en rechtbank (tot 16 jr.)"),
  NVT("N", "N.v.t."),
  RECHTBANK("R", "Rechtbank"),
  ONBEKEND("", "Onbekend");

  private String code        = "";
  private String oms         = "";
  private String toelichting = "";

  ToestemminggeverType(String code, String oms) {
    this(code, oms, oms);
  }

  ToestemminggeverType(String code, String oms, String toelichting) {

    setCode(code);
    setOms(oms);
    setToelichting(toelichting);
  }

  public static ToestemminggeverType get(String code) {

    for (ToestemminggeverType a : values()) {
      if (equalsIgnoreCase(a.getCode(), code)) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public boolean is(ToestemminggeverType... types) {
    for (ToestemminggeverType type : types) {
      if (type == this) {
        return true;
      }
    }
    return false;
  }

  /**
   * Als er sprake is van toestemming door hoge raad of gerechtshof dan het woord rechtbank weglaten
   */
  public String getToestemmingMetRechtbank(String rechtbank) {
    String value = getOms() + " " + rechtbank;
    if (value.toLowerCase().matches(".*(hoge|gerechtshof).*")) {
      value = value.replaceAll("([Rr])echtbank", "");
    }
    return value;
  }

  @Override
  public String toString() {
    return getToelichting();
  }
}
