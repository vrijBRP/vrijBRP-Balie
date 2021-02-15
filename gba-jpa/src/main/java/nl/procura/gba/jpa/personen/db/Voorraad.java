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
@NamedQueries({ @NamedQuery(name = "Voorraad.getByNumber",
    query = "select v from Voorraad v where v.nummer = :nummer"),
    @NamedQuery(name = "Voorraad.get",
        query = "select v from Voorraad v where v.type = :type and v.status = :status"),
    @NamedQuery(name = "Voorraad.getCount",
        query = "select count(v) from Voorraad v where v.type = :type and v.status = :status") })
@Table(name = "voorraad")
public class Voorraad extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true,
      nullable = false,
      precision = 131089)
  private Long nummer;

  @Column(precision = 131089)
  private BigDecimal status;

  @Column(name = "voorraad_type",
      precision = 131089)
  private BigDecimal type;

  @Column(length = 2147483647)
  private String melding;

  public Voorraad() {
  }

  public Long getNummer() {
    return this.nummer;
  }

  public void setNummer(Long nummer) {
    this.nummer = nummer;
  }

  public BigDecimal getStatus() {
    return this.status;
  }

  public void setStatus(BigDecimal status) {
    this.status = status;
  }

  public BigDecimal getType() {
    return this.type;
  }

  public void setType(BigDecimal type) {
    this.type = type;
  }

  public String getMelding() {
    return melding;
  }

  public void setMelding(String melding) {
    this.melding = melding;
  }

}
