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
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "doss_ra")
public class DossRiskAnalysis extends BaseEntity<BigDecimal> {

  @Id
  @Column(name = "c_doss_ra",
      unique = true,
      nullable = false)
  private Long cDossRa;

  @Column(name = "ref_case_id")
  private String refCaseId;

  @Column(name = "ref_case_type")
  private BigDecimal refCaseType;

  @Column(name = "ref_case_descr")
  private String refCaseDescr;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_ra",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @ManyToOne
  @JoinColumn(name = "c_rp",
      referencedColumnName = "c_rp",
      nullable = false)
  private RiskProfile riskProfile;

  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "dossRiskAnalysis")
  private List<DossRiskAnalysisSubject> subjects;

  public Long getcDossRa() {
    return cDossRa;
  }

  public void setcDossRa(Long cDossRa) {
    this.cDossRa = cDossRa;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public RiskProfile getRiskProfile() {
    return riskProfile;
  }

  public void setRiskProfile(RiskProfile riskProfile) {
    this.riskProfile = riskProfile;
  }

  public String getRefCaseId() {
    return refCaseId;
  }

  public void setRefCaseId(String relocationCaseId) {
    this.refCaseId = relocationCaseId;
  }

  public BigDecimal getRefCaseType() {
    return refCaseType;
  }

  public void setRefCaseType(BigDecimal refCaseType) {
    this.refCaseType = refCaseType;
  }

  public String getRefCaseDescr() {
    return refCaseDescr;
  }

  public void setRefCaseDescr(String refCaseDescr) {
    this.refCaseDescr = refCaseDescr;
  }

  public List<DossRiskAnalysisSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<DossRiskAnalysisSubject> items) {
    this.subjects = items;
  }
}
