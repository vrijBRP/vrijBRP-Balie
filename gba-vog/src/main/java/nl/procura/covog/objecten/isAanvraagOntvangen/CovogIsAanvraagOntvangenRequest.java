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

package nl.procura.covog.objecten.isAanvraagOntvangen;

public class CovogIsAanvraagOntvangenRequest {

  private String aanvraagNummer;
  private String relatieCode;
  private String identificatieCode;

  public CovogIsAanvraagOntvangenRequest() {
  }

  public CovogIsAanvraagOntvangenRequest(String aanvraagNummer, String relatieCode, String identificatieCode) {

    this.aanvraagNummer = aanvraagNummer;
    this.relatieCode = relatieCode;
    this.identificatieCode = identificatieCode;
  }

  public String getAanvraagNummer() {
    return aanvraagNummer;
  }

  public void setAanvraagNummer(String aanvraagNummer) {
    this.aanvraagNummer = aanvraagNummer;
  }

  public String getRelatieCode() {
    return relatieCode;
  }

  public void setRelatieCode(String relatieCode) {
    this.relatieCode = relatieCode;
  }

  public String getIdentificatieCode() {
    return identificatieCode;
  }

  public void setIdentificatieCode(String identificatieCode) {
    this.identificatieCode = identificatieCode;
  }
}
