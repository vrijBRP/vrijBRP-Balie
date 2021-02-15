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

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "zaak_rel")
@NamedQuery(name = "ZaakRel.findAll",
    query = "SELECT z FROM ZaakRel z")
public class ZaakRel extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private ZaakRelPK id;

  @Column(precision = 131089,
      name = "zaak_type")
  private BigDecimal zType;

  @Column(precision = 131089,
      name = "zaak_type_rel")
  private BigDecimal zTypeRel;

  public ZaakRel() {
  }

  public ZaakRel(ZaakRelPK pk) {
    setId(pk);
  }

  @Override
  public ZaakRelPK getId() {
    return this.id;
  }

  public void setId(ZaakRelPK id) {
    this.id = id;
  }

  public BigDecimal getzType() {
    return zType;
  }

  public void setzType(BigDecimal zType) {
    this.zType = zType;
  }

  public BigDecimal getzTypeRel() {
    return zTypeRel;
  }

  public void setzTypeRel(BigDecimal zTypeRel) {
    this.zTypeRel = zTypeRel;
  }
}
