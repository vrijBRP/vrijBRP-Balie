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
@Table(name = "vog_belang")
public class VogBelang extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_vog_belang",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "vog_belang",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_vog_belang")
  @Column(name = "c_vog_belang",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cVogBelang;

  @Column(name = "c_land_b",
      precision = 131089)
  private BigDecimal cLandB;

  @Column(name = "hnr_b",
      precision = 131089)
  private BigDecimal hnrB;

  @Column(name = "hnr_t_b")
  private String hnrTB;

  @Column(name = "instelling_b")
  private String instellingB;

  @Column(name = "naam_b")
  private String naamB;

  @Column(name = "pc_b")
  private String pcB;

  @Column(name = "plaats_b")
  private String plaatsB;

  @Column(name = "straat_b")
  private String straatB;

  @Column(name = "tel_b")
  private String telB;

  public VogBelang() {
  }

  public Long getCVogBelang() {
    return this.cVogBelang;
  }

  public void setCVogBelang(Long cVogBelang) {
    this.cVogBelang = cVogBelang;
  }

  public BigDecimal getCLandB() {
    return this.cLandB;
  }

  public void setCLandB(BigDecimal cLandB) {
    this.cLandB = cLandB;
  }

  public BigDecimal getHnrB() {
    return this.hnrB;
  }

  public void setHnrB(BigDecimal hnrB) {
    this.hnrB = hnrB;
  }

  public String getHnrTB() {
    return this.hnrTB;
  }

  public void setHnrTB(String hnrTB) {
    this.hnrTB = hnrTB;
  }

  public String getInstellingB() {
    return this.instellingB;
  }

  public void setInstellingB(String instellingB) {
    this.instellingB = instellingB;
  }

  public String getNaamB() {
    return this.naamB;
  }

  public void setNaamB(String naamB) {
    this.naamB = naamB;
  }

  public String getPcB() {
    return this.pcB;
  }

  public void setPcB(String pcB) {
    this.pcB = pcB;
  }

  public String getPlaatsB() {
    return this.plaatsB;
  }

  public void setPlaatsB(String plaatsB) {
    this.plaatsB = plaatsB;
  }

  public String getStraatB() {
    return this.straatB;
  }

  public void setStraatB(String straatB) {
    this.straatB = straatB;
  }

  public String getTelB() {
    return this.telB;
  }

  public void setTelB(String telB) {
    this.telB = telB;
  }

}
