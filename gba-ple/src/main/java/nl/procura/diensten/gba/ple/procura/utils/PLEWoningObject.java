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

package nl.procura.diensten.gba.ple.procura.utils;

import nl.procura.gba.jpa.probev.db.Vbo;

public class PLEWoningObject {

  private int    ref_pl     = 0;
  private int    ref_cat    = 0;
  private int    c_straat   = 0;
  private int    hnr        = 0;
  private String hnr_l      = "";
  private String hnr_t      = "";
  private String hnr_a      = "";
  private int    c_gem_deel = 0;
  private int    c_locatie  = 0;

  // tijdelijk
  public boolean matches(Vbo obj) {

    if (obj.getGemDeel().getCGemDeel() != getC_gem_deel()) {
      return false;
    }

    if (obj.getStraat().getCStraat() != getC_straat()) {
      return false;
    }

    if (obj.getLocatie().getCLocatie() != getC_locatie()) {
      return false;
    }

    if (obj.getHnr().intValue() != getHnr()) {
      return false;
    }

    if (!obj.getHnrA().trim().equals(getHnr_a().trim())) {
      return false;
    }

    if (!obj.getHnrT().trim().equals(getHnr_t().trim())) {
      return false;
    }

    return obj.getHnrL().trim().equals(getHnr_l().trim());
  }

  public int getC_straat() {
    return c_straat;
  }

  public void setC_straat(int c_straat) {
    this.c_straat = c_straat;
  }

  public int getHnr() {
    return hnr;
  }

  public void setHnr(int hnr) {
    this.hnr = hnr;
  }

  public String getHnr_l() {
    return hnr_l;
  }

  public void setHnr_l(String hnr_l) {
    this.hnr_l = hnr_l;
  }

  public String getHnr_t() {
    return hnr_t;
  }

  public void setHnr_t(String hnr_t) {
    this.hnr_t = hnr_t;
  }

  public String getHnr_a() {
    return hnr_a;
  }

  public void setHnr_a(String hnr_a) {
    this.hnr_a = hnr_a;
  }

  public int getC_gem_deel() {
    return c_gem_deel;
  }

  public void setC_gem_deel(int c_gem_deel) {
    this.c_gem_deel = c_gem_deel;
  }

  public int getC_locatie() {
    return c_locatie;
  }

  public void setC_locatie(int c_locatie) {
    this.c_locatie = c_locatie;
  }

  public int getRef_pl() {
    return ref_pl;
  }

  public void setRef_pl(int ref_pl) {
    this.ref_pl = ref_pl;
  }

  public int getRef_cat() {
    return ref_cat;
  }

  public void setRef_cat(int ref_cat) {
    this.ref_cat = ref_cat;
  }
}
