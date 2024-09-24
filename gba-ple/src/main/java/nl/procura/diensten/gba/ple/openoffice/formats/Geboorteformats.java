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

package nl.procura.diensten.gba.ple.openoffice.formats;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.common.MiscUtils;

public class Geboorteformats {

  private String geboortedatum  = "";
  private String geboorteland   = "";
  private String geboorteplaats = "";

  public Geboorteformats setValues(String geboortedatum, String geboorteplaats, String geboorteland) {
    this.geboortedatum = astr(geboortedatum);
    this.geboorteplaats = astr(geboorteplaats);
    this.geboorteland = astr(geboorteland);
    return this;
  }

  public String getDatum_plaats() {
    return trim(geboortedatum + " " + geboorteplaats);
  }

  public String getDatum_te_plaats() {
    return trim(geboortedatum + " te " + geboorteplaats);
  }

  public String getDatum_plaats_land() {

    String geb_land = (fil(geboorteland) && !eq(geboorteland.toLowerCase(),
        "nederland")) ? "(" + geboorteland + ")" : "";
    return trim(geboortedatum + " " + geboorteplaats + " " + geb_land);
  }

  public String getDatum_te_plaats_land() {

    String geb_land = (fil(geboorteland) && !eq(geboorteland.toLowerCase(),
        "nederland")) ? "(" + geboorteland + ")" : "";
    return trim(geboortedatum + " te " + geboorteplaats + " " + geb_land);
  }

  public String getPlaats_land() {

    String geb_land = (fil(geboorteland) && !eq(geboorteland.toLowerCase(),
        "nederland")) ? ", " + geboorteland : "";
    return trim(geboorteplaats + " " + geb_land);
  }

  public String getDatum_leeftijd() {
    return fil(geboortedatum) ? (geboortedatum + ", " + getLeeftijd() + " jaar") : "";
  }

  public String getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(String geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getLeeftijd() {
    return astr(MiscUtils.getLeeftijd(geboortedatum, "0"));
  }

  public String getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(String geboorteland) {
    this.geboorteland = geboorteland;
  }

  public String getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(String geboorteplaats) {
    this.geboorteplaats = geboorteplaats;
  }
}
