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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ZaakAttrPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "zaak_id",
      unique = true,
      nullable = false)
  private String zaakId;

  @Column(name = "zaak_attr",
      unique = true,
      nullable = false)
  private String zaakAttr;

  @Column(name = "c_usr",
      unique = true,
      nullable = false)
  private long cUsr;

  public ZaakAttrPK() {
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public String getZaakAttr() {
    return this.zaakAttr;
  }

  public void setZaakAttr(String zaakAttr) {
    this.zaakAttr = zaakAttr;
  }

  public long getcUsr() {
    return cUsr;
  }

  public void setcUsr(long cUsr) {
    this.cUsr = cUsr;
  }
}
