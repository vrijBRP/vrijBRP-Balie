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

package nl.procura.gbaws.web.rest.v1_0.tabellen;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "codes", "historie" })
public class GbaWsRestTabellenVraag {

  private List<Integer> codes    = new ArrayList<>();
  private boolean       historie = false;

  public GbaWsRestTabellenVraag() {
  }

  public GbaWsRestTabellenVraag(boolean historie, List<Integer> codes) {
    this.historie = historie;
    this.codes = codes;
  }

  public List<Integer> getCodes() {
    return codes;
  }

  public void setCodes(List<Integer> codes) {
    this.codes = codes;
  }

  public boolean isHistorie() {
    return historie;
  }

  public void setHistorie(boolean historie) {
    this.historie = historie;
  }
}
