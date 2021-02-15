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
@Table(name = "belangh")
@NamedQuery(name = "Belangh.findAll", query = "select g from Belangh g where g.cBelangh > 0 order by g.naam, g.tavNaam")
public class Belangh extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_belangh",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "belangh",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_belangh")
  @Column(name = "c_belangh",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cBelangh;

  @Column(name = "naam")
  private String naam;

  @Column(name = "tav_aanhef")
  private String tavAanhef;

  @Column(name = "tav_voorl")
  private String tavVoorl;

  @Column(name = "tav_naam")
  private String tavNaam;

  @Column(name = "adres")
  private String adres;

  @Column(name = "pc")
  private String pc;

  @Column(name = "plaats")
  private String plaats;

  @Column(name = "c_land",
      precision = 131089)
  private BigDecimal cLand;

  @Column(name = "tel")
  private String tel;

  @Column(name = "email")
  private String email;

  @Column(name = "type")
  private String type;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  public Belangh() {
  }

  public Long getcBelangh() {
    return cBelangh;
  }

  public void setcBelangh(Long cBelangh) {
    this.cBelangh = cBelangh;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public String getPc() {
    return pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public BigDecimal getcLand() {
    return cLand;
  }

  public void setcLand(BigDecimal cLand) {
    this.cLand = cLand;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTavAanhef() {
    return tavAanhef;
  }

  public void setTavAanhef(String tavAanhef) {
    this.tavAanhef = tavAanhef;
  }

  public String getTavVoorl() {
    return tavVoorl;
  }

  public void setTavVoorl(String tavVoorl) {
    this.tavVoorl = tavVoorl;
  }

  public String getTavNaam() {
    return tavNaam;
  }

  public void setTavNaam(String tavNaam) {
    this.tavNaam = tavNaam;
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
