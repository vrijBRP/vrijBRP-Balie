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

package nl.procura.gba.web.services.zaken.documenten.printopties;

public enum PrintOptieType {

  COMMAND("command", "Linux commando"),
  LOCAL_PRINTER("local", "Lokale printer"),
  NETWORK_PRINTER("external", "Netwerkprinter"),
  MIJN_OVERHEID("mijn-overheid", "Mijn overheid"),
  POPUP("popup", "Popup");

  private String code;
  private String descr;

  PrintOptieType(String code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public boolean is(String code) {
    return this.code.equals(code);
  }
}
