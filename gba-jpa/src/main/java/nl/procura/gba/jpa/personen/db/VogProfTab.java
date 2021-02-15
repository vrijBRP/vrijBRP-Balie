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
@Table(name = "vog_prof_tab")
public class VogProfTab extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_vog_prof_tab",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cVogProfTab;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column()
  private String oms;

  @Column(name = "vog_prof_tab")
  private String vogProfTab;

  @OneToMany(mappedBy = "vogProfTab")
  private List<VogAanvr> vogAanvrs;

  public VogProfTab() {
  }

  public Long getCVogProfTab() {
    return this.cVogProfTab;
  }

  public void setCVogProfTab(Long cVogProfTab) {
    this.cVogProfTab = cVogProfTab;
  }

  public BigDecimal getDEnd() {
    return this.dEnd;
  }

  public void setDEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getVogProfTab() {
    return this.vogProfTab;
  }

  public void setVogProfTab(String vogProfTab) {
    this.vogProfTab = vogProfTab;
  }

  public List<VogAanvr> getVogAanvrs() {
    return this.vogAanvrs;
  }

  public void setVogAanvrs(List<VogAanvr> vogAanvrs) {
    this.vogAanvrs = vogAanvrs;
  }

}
