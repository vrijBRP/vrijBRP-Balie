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
@Table(name = "rp")
public class RiskProfile extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_rp", nullable = false)
  @TableGenerator(name = "table_gen_rp",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rp",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rp")
  private Long cRp;

  @Column(name = "name",
      nullable = false)
  private String name;

  @Column(name = "threshold",
      nullable = false)
  private BigDecimal threshold;

  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "riskProfile")
  @OrderBy("vnr")
  private List<RiskProfileRule> rules;

  public Long getcRp() {
    return cRp;
  }

  public void setcRp(Long cRp) {
    this.cRp = cRp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getThreshold() {
    return threshold;
  }

  public void setThreshold(BigDecimal threshold) {
    this.threshold = threshold;
  }

  public List<RiskProfileRule> getRules() {
    return rules;
  }

  public void setRules(List<RiskProfileRule> rules) {
    this.rules = rules;
  }

}
