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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "aant3")
public class Aant3 extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private Aant3PK id;

  @Column(length = 2147483647)
  private String aant;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "country",
      precision = 131089)
  private BigDecimal country;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_aantek3",
      nullable = false,
      insertable = false,
      updatable = false)
  private Aantek3 aantek3;

  public Aant3() {
  }

  @Override
  public Aant3PK getId() {
    return this.id;
  }

  public void setId(Aant3PK id) {
    this.id = id;
  }

  public String getAant() {
    return this.aant;
  }

  public void setAant(String aant) {
    this.aant = aant;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public Aantek3 getAantek3() {
    return this.aantek3;
  }

  public void setAantek3(Aantek3 aantek3) {
    this.aantek3 = aantek3;
  }

  public BigDecimal getCountry() {
    return country;
  }

  public void setCountry(BigDecimal country) {
    this.country = country;
  }
}
