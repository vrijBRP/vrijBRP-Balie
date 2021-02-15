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

package nl.procura.gba.web.rest.v1_0.zaak.identificatie;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "internId", "type", "externId" })
public class GbaRestZaakIdentificatieToevoegenVraag {

  private GbaRestZaakIdType type     = GbaRestZaakIdType.ONBEKEND;
  private String            internId = "";
  private String            externId = "";

  public String getInternId() {
    return internId;
  }

  public void setInternId(String internId) {
    this.internId = internId;
  }

  public GbaRestZaakIdType getType() {
    return type;
  }

  public void setType(GbaRestZaakIdType type) {
    this.type = type;
  }

  public String getExternId() {
    return externId;
  }

  public void setExternId(String externId) {
    this.externId = externId;
  }
}
