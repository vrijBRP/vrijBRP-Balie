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
@Table(name = "huw_ambt")
public class HuwAmbt extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_huw_ambt",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "huw_ambt",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_huw_ambt")
  @Column(name = "c_huw_ambt",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cHuwAmbt;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column()
  private String naam;

  @Column(name = "email")
  private String email;

  @Column(name = "tel")
  private String tel;

  @Column(length = 2147483647)
  private String oms;

  @Column(name = "alias",
      length = 1000)
  private String alias;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  public HuwAmbt() {
  }

  public Long getCHuwAmbt() {
    return this.cHuwAmbt;
  }

  public void setCHuwAmbt(Long cHuwAmbt) {
    this.cHuwAmbt = cHuwAmbt;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public String getNaam() {
    return this.naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public BigDecimal getdEnd() {
    return dEnd;
  }

  public void setdEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }
}
