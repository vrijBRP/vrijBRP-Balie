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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.standard.Globalfunctions;

@Entity
@Table(name = "rp_rule")
public class RiskProfileRule extends BaseEntity<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_rp_rule",
      nullable = false)
  @TableGenerator(name = "table_gen_rp_rule",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rp_rule",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rp_rule")
  private Long cRpRule;

  @Column(name = "name",
      nullable = false)
  private String name;

  @Column(name = "type",
      nullable = false)
  private BigDecimal type;

  @Column(name = "attr",
      nullable = false,
      length = -1)
  private String attr;

  @Column(name = "score",
      nullable = false)
  private BigDecimal score;

  @Column(name = "vnr",
      nullable = false)
  private BigDecimal vnr;

  @ManyToOne
  @JoinColumn(name = "c_rp",
      referencedColumnName = "c_rp",
      nullable = false)
  private RiskProfile riskProfile;

  public Long getcRpRule() {
    return cRpRule;
  }

  public void setcRpRule(Long cRpRule) {
    this.cRpRule = cRpRule;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getType() {
    return type;
  }

  public void setType(BigDecimal type) {
    this.type = type;
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public BigDecimal getVnr() {
    return vnr;
  }

  public void setVnr(BigDecimal vnr) {
    this.vnr = vnr;
  }

  public RiskProfile getRiskProfile() {
    return riskProfile;
  }

  public void setRiskProfile(RiskProfile riskProfile) {
    this.riskProfile = riskProfile;
  }

  @Transient
  public RiskProfileRuleType getRuleType() {
    return RiskProfileRuleType.get(getType());
  }

  @Transient
  public void setRuleType(RiskProfileRuleType type) {
    setType(Globalfunctions.toBigDecimal(type.getCode()));
  }

  @Transient
  public Properties getAttributes() {
    Properties properties = new Properties();
    if (attr != null) {
      try {
        properties.load(new StringReader(attr));
      } catch (IOException e) {
        throw new IllegalArgumentException("Error loading properties", e);
      }
    }
    return properties;
  }

  @Transient
  public void setAttributes(Properties properties) {
    StringWriter writer = new StringWriter();
    try {
      properties.store(writer, null);
      writer.flush();
      writer.close();
      this.attr = writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error storing properties", e);
    }
  }

  @Transient
  public int getIntAttribute(String attr) {
    return Integer.parseInt(getAttribute(attr));
  }

  @Transient
  public String getAttribute(String attr) {
    return getAttributes().getProperty(attr.toLowerCase());
  }
}
