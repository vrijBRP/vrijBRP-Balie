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
@Table(name = "nrd_status")
public class NrdStatus extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private NrdStatusPK id;

  @Column(name = "c_usr",
      precision = 131089)
  private BigDecimal cUsr;

  @Column(length = 2147483647)
  private String opm;

  @Column(name = "rdw_d_stat",
      precision = 131089)
  private BigDecimal rdwDStat;

  @Column(name = "rdw_t_stat",
      precision = 131089)
  private BigDecimal rdwTStat;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_aanvr",
      nullable = false,
      insertable = false,
      updatable = false)
  private Nrd nrd;

  public NrdStatus() {
  }

  @Override
  public NrdStatusPK getId() {
    return this.id;
  }

  public void setId(NrdStatusPK id) {
    this.id = id;
  }

  public BigDecimal getCUsr() {
    return this.cUsr;
  }

  public void setCUsr(BigDecimal cUsr) {
    this.cUsr = cUsr;
  }

  public String getOpm() {
    return this.opm;
  }

  public void setOpm(String opm) {
    this.opm = opm;
  }

  public BigDecimal getRdwDStat() {
    return this.rdwDStat;
  }

  public void setRdwDStat(BigDecimal rdwDStat) {
    this.rdwDStat = rdwDStat;
  }

  public BigDecimal getRdwTStat() {
    return this.rdwTStat;
  }

  public void setRdwTStat(BigDecimal rdwTStat) {
    this.rdwTStat = rdwTStat;
  }

  public Nrd getNrd() {
    return this.nrd;
  }

  public void setNrd(Nrd nrd) {
    this.nrd = nrd;
  }

}
