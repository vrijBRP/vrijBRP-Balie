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

package nl.procura.gba.web.services.zaken.algemeen.contact;

public enum ZaakContactpersoonType {

  AANGEVER("Aangever"),
  PARTNER("Partner"),
  PARTNER_1("Partner 1"),
  PARTNER_2("Partner 2"),
  MOEDER("Moeder"),
  AMBTENAAR("Toegekende ambtenaar"),
  ERKENNER("Erkenner"),
  VADER_ERKENNER("Vader / Erkenner"),
  MEEVERHUIZER("Meeverhuizer"),
  BETROKKENE_ONDERZOEK("Betrokkene bij onderzoek"),
  INSCHRIJVER("Inschrijver"),
  ONBEKEND("Onbekend");

  private String descr;

  ZaakContactpersoonType(String descr) {
    this.descr = descr;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Override
  public String toString() {
    return descr;
  }
}
