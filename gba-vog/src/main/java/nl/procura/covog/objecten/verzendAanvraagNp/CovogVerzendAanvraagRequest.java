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

package nl.procura.covog.objecten.verzendAanvraagNp;

public class CovogVerzendAanvraagRequest {

  protected CovogAanvraag aanvraag;
  private String          relatieCode;
  private String          identificatieCode;

  public CovogVerzendAanvraagRequest() {
  }

  public CovogVerzendAanvraagRequest(String relatieCode, String identificatieCode, CovogAanvraag aanvraag) {
    this.relatieCode = relatieCode;
    this.identificatieCode = identificatieCode;
    this.aanvraag = aanvraag;
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

  public CovogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(CovogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }
}
