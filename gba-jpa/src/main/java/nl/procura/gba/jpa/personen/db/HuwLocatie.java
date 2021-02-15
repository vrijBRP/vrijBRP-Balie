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
@Table(name = "huw_locatie")
public class HuwLocatie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_huw_locatie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "huw_locatie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_huw_locatie")
  @Column(name = "c_huw_locatie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cHuwLocatie;

  @Column(name = "huw_locatie")
  private String huwLocatie;

  @Column()
  private String soort;

  @Column(length = 2147483647)
  private String toelichting;

  @Column(name = "alias",
      length = 1000)
  private String alias;

  @Column(name = "cp_tav_aanhef")
  private String tavAanhef;

  @Column(name = "cp_tav_voorl")
  private String tavVoorl;

  @Column(name = "cp_tav_naam")
  private String tavNaam;

  @Column(name = "cp_adres")
  private String adres;

  @Column(name = "cp_pc")
  private String pc;

  @Column(name = "cp_plaats")
  private String plaats;

  @Column(name = "cp_c_land",
      precision = 131089)
  private BigDecimal cLand;

  @Column(name = "cp_tel")
  private String tel;

  @Column(name = "cp_email")
  private String email;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  @OneToMany(mappedBy = "huwLocatie")
  private List<DossHuw> dossHuws;

  @ManyToMany(mappedBy = "huwLocaties")
  private List<HuwLocatieOptie> huwLocatieOpties;

  public HuwLocatie() {
  }

  public Long getCHuwLocatie() {
    return this.cHuwLocatie;
  }

  public void setCHuwLocatie(Long cHuwLocatie) {
    this.cHuwLocatie = cHuwLocatie;
  }

  public String getHuwLocatie() {
    return this.huwLocatie;
  }

  public void setHuwLocatie(String huwLocatie) {
    this.huwLocatie = huwLocatie;
  }

  public String getSoort() {
    return this.soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public String getToelichting() {
    return this.toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public List<DossHuw> getDossHuws() {
    return this.dossHuws;
  }

  public void setDossHuws(List<DossHuw> dossHuws) {
    this.dossHuws = dossHuws;
  }

  public List<HuwLocatieOptie> getHuwLocatieOpties() {
    return this.huwLocatieOpties;
  }

  public void setHuwLocatieOpties(List<HuwLocatieOptie> huwLocatieOpties) {
    this.huwLocatieOpties = huwLocatieOpties;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
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
