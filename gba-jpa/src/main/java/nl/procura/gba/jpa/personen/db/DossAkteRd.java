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
@Table(name = "doss_akte_rd")
public class DossAkteRd extends nl.procura.gba.jpa.personen.db.BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_akte_rd",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_akte_rd",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_akte_rd")
  @Column(name = "c_doss_akte_rd",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossAkteRd;

  @Column()
  private String registerdeel;

  @Column(precision = 131089)
  private BigDecimal max;

  @Column(precision = 131089)
  private BigDecimal min;

  @Column()
  private String oms;

  @Column(precision = 131089)
  private BigDecimal registersoort;

  @ManyToOne
  @JoinColumn(name = "c_doss_akte_rd_cat",
      nullable = false)
  private DossAkteRdCat dossAkteRdCat;

  public DossAkteRd() {
  }

  public Long getCDossAkteRd() {
    return this.cDossAkteRd;
  }

  public void setCDossAkteRd(Long cDossAkteRd) {
    this.cDossAkteRd = cDossAkteRd;
  }

  public BigDecimal getMax() {
    return this.max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  public BigDecimal getMin() {
    return this.min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public DossAkteRdCat getDossAkteRdCat() {
    return this.dossAkteRdCat;
  }

  public void setDossAkteRdCat(DossAkteRdCat dossAkteRdCat) {
    this.dossAkteRdCat = dossAkteRdCat;
  }

  public BigDecimal getRegistersoort() {
    return registersoort;
  }

  public void setRegistersoort(BigDecimal registersoort) {
    this.registersoort = registersoort;
  }

  public String getRegisterdeel() {
    return registerdeel;
  }

  public void setRegisterdeel(String registerdeel) {
    this.registerdeel = registerdeel;
  }

}
