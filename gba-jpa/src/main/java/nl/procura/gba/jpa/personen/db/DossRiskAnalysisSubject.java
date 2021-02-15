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
@Table(name = "doss_ra_subject")
public class DossRiskAnalysisSubject extends BaseEntity<BigDecimal> {

  @Id
  @Column(name = "c_doss_ra_subject",
      nullable = false)
  @TableGenerator(name = "table_gen_doss_ra_subject",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_ra_subject",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_ra_subject")
  private BigDecimal cDossRaSubject;

  @Column(name = "score",
      nullable = false)
  private BigDecimal score;

  @Column(name = "log",
      nullable = false)
  private String log;

  @ManyToOne
  @JoinColumn(name = "c_doss_ra",
      referencedColumnName = "c_doss_ra",
      nullable = false)
  private DossRiskAnalysis dossRiskAnalysis;

  @Column(name = "c_doss_pers",
      nullable = false)
  private Long cDossPers;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_pers",
      nullable = false,
      insertable = false,
      updatable = false)
  private DossPer person;

  public BigDecimal getcCDossRaSubject() {
    return cDossRaSubject;
  }

  public void setcCDossRaSubject(BigDecimal cDossRaItem) {
    this.cDossRaSubject = cDossRaItem;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal bsn) {
    this.score = bsn;
  }

  public DossRiskAnalysis getDossRiskAnalysis() {
    return dossRiskAnalysis;
  }

  public void setDossRiskAnalysis(DossRiskAnalysis dossRaByCDossRiskAnalysis) {
    this.dossRiskAnalysis = dossRaByCDossRiskAnalysis;
  }

  public DossPer getPerson() {
    return person;
  }

  public void setPerson(DossPer person) {
    this.person = person;
  }

  public Long getcDossPers() {
    return cDossPers;
  }

  public void setcDossPers(Long cDossPers) {
    this.cDossPers = cDossPers;
  }

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }
}
