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
@Table(name = "aantekening_hist")
public class AantekeningHist extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_aantekening_hist",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "aantekening_hist",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_aantekening_hist")
  @Column(name = "c_aantekening_hist",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAantekeningHist;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "status")
  private int status = 1;

  @Column(length = 2147483647)
  private String inhoud;

  @Column()
  private String onderwerp;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @ManyToOne
  @JoinColumn(name = "c_aantekening")
  private Aantekening aantekening;

  @ManyToOne
  @JoinColumn(name = "c_aantekening_ind")
  private AantekeningInd aantekeningInd;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  public AantekeningHist() {
  }

  public Long getCAantekeningHist() {
    return this.cAantekeningHist;
  }

  public void setCAantekeningHist(Long cAantekeningHist) {
    this.cAantekeningHist = cAantekeningHist;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getInhoud() {
    return this.inhoud;
  }

  public void setInhoud(String inhoud) {
    this.inhoud = inhoud;
  }

  public String getOnderwerp() {
    return this.onderwerp;
  }

  public void setOnderwerp(String onderwerp) {
    this.onderwerp = onderwerp;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public AantekeningInd getAantekeningInd() {
    return this.aantekeningInd;
  }

  public void setAantekeningInd(AantekeningInd aantekeningInd) {
    this.aantekeningInd = aantekeningInd;
  }

  public Aantekening getAantekening() {
    return aantekening;
  }

  public void setAantekening(Aantekening aantekening) {
    this.aantekening = aantekening;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
