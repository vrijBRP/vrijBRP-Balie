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
@Table(name = "doss_overl_aangever")
public class DossOverlAangever extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_overl_aangever",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_overl_aangever",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_overl_aangever")
  @Column(name = "doss_c_overl_aangever",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long codeOverlijdenAangever;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "c_geb_land",
      precision = 131089)
  private BigDecimal cGebLand;

  @Column(name = "d_geb",
      precision = 131089)
  private BigDecimal dGeb;

  @Column(name = "geb_plaats")
  private String gebPlaats;

  @Column(name = "c_geb_plaats",
      precision = 131089)
  private BigDecimal cGebPlaats;

  @Column(name = "geslachtsnaam")
  private String geslachtsnaam;

  @Column(name = "gesl")
  private String gesl;

  @Column(name = "tp")
  private String tp;

  @Column(name = "voorn")
  private String voornamen;

  @Column(name = "voorv")
  private String voorvoegsel;

  @Column(name = "email")
  private String email;

  @Column(name = "tel")
  private String tel;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  public DossOverlAangever() {
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getCGebLand() {
    return this.cGebLand;
  }

  public void setCGebLand(BigDecimal cGebLand) {
    this.cGebLand = cGebLand;
  }

  public BigDecimal getDGeb() {
    return this.dGeb;
  }

  public void setDGeb(BigDecimal dGeb) {
    this.dGeb = dGeb;
  }

  public String getGebPlaats() {
    return this.gebPlaats;
  }

  public void setGebPlaats(String gebPlaats) {
    this.gebPlaats = gebPlaats;
  }

  public String getTp() {
    return tp;
  }

  public void setTp(String tp) {
    this.tp = tp;
  }

  public Long getCodeOverlijdenAangever() {
    return codeOverlijdenAangever;
  }

  public void setCodeOverlijdenAangever(Long codeOverlijdenAangever) {
    this.codeOverlijdenAangever = codeOverlijdenAangever;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public String getGesl() {
    return gesl;
  }

  public void setGesl(String gesl) {
    this.gesl = gesl;
  }

  public BigDecimal getCGebPlaats() {
    return cGebPlaats;
  }

  public void setCGebPlaats(BigDecimal cGebPlaats) {
    this.cGebPlaats = cGebPlaats;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getdEnd() {
    return dEnd;
  }

  public void setdEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }
}
