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

public class RdwAanvraagMessage extends RdwMessage {

  private long   codeLandVestiging  = 0;
  private long   codeLandVertrek    = 0;
  private long   codeVerblijfstitel = 0;
  private long   datumVertrek       = 0;
  private long   datumVestiging     = 0;
  private String soortId            = "";
  private String email              = "";
  private String telThuis           = "";
  private String telWerk            = "";
  private String telMobiel          = "";

  public long getCodeLandVestiging() {
    return codeLandVestiging;
  }

  public void setCodeLandVestiging(long codeLandVestiging) {
    this.codeLandVestiging = codeLandVestiging;
  }

  public long getCodeLandVertrek() {
    return codeLandVertrek;
  }

  public void setCodeLandVertrek(long codeLandVertrek) {
    this.codeLandVertrek = codeLandVertrek;
  }

  public long getCodeVerblijfstitel() {
    return codeVerblijfstitel;
  }

  public void setCodeVerblijfstitel(long codeVerblijfstitel) {
    this.codeVerblijfstitel = codeVerblijfstitel;
  }

  public long getDatumVertrek() {
    return datumVertrek;
  }

  public void setDatumVertrek(long datumVertrek) {
    this.datumVertrek = datumVertrek;
  }

  public long getDatumVestiging() {
    return datumVestiging;
  }

  public void setDatumVestiging(long datumVestiging) {
    this.datumVestiging = datumVestiging;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelThuis() {
    return telThuis;
  }

  public void setTelThuis(String telThuis) {
    this.telThuis = telThuis;
  }

  public String getTelWerk() {
    return telWerk;
  }

  public void setTelWerk(String telWerk) {
    this.telWerk = telWerk;
  }

  public String getTelMobiel() {
    return telMobiel;
  }

  public void setTelMobiel(String telMobiel) {
    this.telMobiel = telMobiel;
  }

  public String getSoortId() {
    return soortId;
  }

  public void setSoortId(String soortId) {
    this.soortId = soortId;
  }
}
