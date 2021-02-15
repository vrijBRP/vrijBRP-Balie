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
@Table(name = "pl_hist")
public class PlHist extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PlHistPK id;

  @Column(precision = 1)
  private BigDecimal bron;

  @Column()
  private String oms;

  @Column(name = "tijdstempel",
      precision = 131089)
  private BigDecimal timestamp;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr",
      nullable = false,
      insertable = false,
      updatable = false)
  private Usr usr;

  public PlHist() {
  }

  @Override
  public PlHistPK getId() {
    return this.id;
  }

  public void setId(PlHistPK id) {
    this.id = id;
  }

  public BigDecimal getBron() {
    return this.bron;
  }

  public void setBron(BigDecimal bron) {
    this.bron = bron;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public BigDecimal getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(BigDecimal timestamp) {
    this.timestamp = timestamp;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

}
