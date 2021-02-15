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
@Table(name = "zaak_id")
@NamedQuery(name = "ZaakId.findAll",
    query = "SELECT z FROM ZaakId z")
public class ZaakId extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private ZaakIdPK id;

  @Column(name = "extern_id",
      nullable = false)
  private String externId;

  @Column(name = "zaak_type")
  private long zaakTypeCode;

  public ZaakId() {
  }

  @Override
  public ZaakIdPK getId() {
    return this.id;
  }

  public void setId(ZaakIdPK id) {
    this.id = id;
  }

  public String getExternId() {
    return this.externId;
  }

  public void setExternId(String externId) {
    this.externId = externId;
  }

  public long getZaakTypeCode() {
    return zaakTypeCode;
  }

  public void setZaakTypeCode(long zaakTypeCode) {
    this.zaakTypeCode = zaakTypeCode;
  }
}
