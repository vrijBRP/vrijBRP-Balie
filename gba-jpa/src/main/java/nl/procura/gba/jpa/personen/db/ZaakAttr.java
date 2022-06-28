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

package nl.procura.gba.jpa.personen.db;

import javax.persistence.*;

@Entity
@Table(name = "zaak_attr")
@NamedQuery(name = "ZaakAttr.findAll",
    query = "SELECT z FROM ZaakAttr z")
public class ZaakAttr extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private ZaakAttrPK id;

  @Column
  private String oms;

  @Column
  private String waarde;

  public ZaakAttr() {
  }

  @Override
  public ZaakAttrPK getId() {
    return this.id;
  }

  public void setId(ZaakAttrPK id) {
    this.id = id;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getWaarde() {
    return waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }
}
