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
@Table(name = "reisdoc")
public class Reisdoc extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_reisdoc",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "reisdoc",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_reisdoc")
  @Column(name = "c_reisdoc",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cReisdoc;

  @Column(precision = 131089)
  private BigDecimal bijschr;

  @Column(precision = 131089)
  private BigDecimal id;

  @Column(precision = 131089)
  private BigDecimal ks;

  @Column(name = "lt_toest",
      precision = 131089)
  private BigDecimal ltToest;

  @Column()
  private String reisdoc;

  @Column()
  private String zkarg;

  @OneToMany(mappedBy = "reisdoc")
  private List<Kassa> kassas;

  public Reisdoc() {
  }

  public Reisdoc(Long cReisdoc) {
    this();
    this.cReisdoc = cReisdoc;
  }

  public Long getCReisdoc() {
    return this.cReisdoc;
  }

  public void setCReisdoc(Long cReisdoc) {
    this.cReisdoc = cReisdoc;
  }

  public BigDecimal getBijschr() {
    return this.bijschr;
  }

  public void setBijschr(BigDecimal bijschr) {
    this.bijschr = bijschr;
  }

  @Override
  public BigDecimal getId() {
    return this.id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public BigDecimal getKs() {
    return this.ks;
  }

  public void setKs(BigDecimal ks) {
    this.ks = ks;
  }

  public BigDecimal getLtToest() {
    return this.ltToest;
  }

  public void setLtToest(BigDecimal ltToest) {
    this.ltToest = ltToest;
  }

  public String getReisdoc() {
    return this.reisdoc;
  }

  public void setReisdoc(String reisdoc) {
    this.reisdoc = reisdoc;
  }

  public String getZkarg() {
    return this.zkarg;
  }

  public void setZkarg(String zkarg) {
    this.zkarg = zkarg;
  }

  public List<Kassa> getKassas() {
    return this.kassas;
  }

  public void setKassas(List<Kassa> kassas) {
    this.kassas = kassas;
  }

}
