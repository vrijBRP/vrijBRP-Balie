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

package nl.procura.gba.web.rest.v1_0.algemeen;

import java.beans.Transient;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "vraagElement" })
public class GbaRestElementVraag {

  @XmlElement(name = "id")
  private String id = "";

  @XmlElement(name = "element")
  private GbaRestElement vraagElement = new GbaRestElement("vraag");

  public GbaRestElementVraag() {
  }

  public GbaRestElementVraag(String id) {
    this();
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GbaRestElement getVraagElement() {
    return vraagElement;
  }

  public void setVraagElement(GbaRestElement content) {
    this.vraagElement = content;
  }

  @Transient
  protected String get(GbaRestElement parent, String child, boolean required) {
    if (!required && !parent.is(child)) {
      return "";
    }

    return parent.get(child).getWaarde();
  }
}
