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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "gv")
public class Gv extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_gv",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "gv",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_gv")
  @Column(name = "c_gv",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGv;

  @Column(precision = 131089)
  private BigDecimal anr;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_wijz",
      precision = 131089)
  private BigDecimal dWijz;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "aanvrager")
  private String aanvrager;

  @Column(name = "tav_aanhef")
  private String tavAanhef;

  @Column(name = "tav_voorl")
  private String tavVoorl;

  @Column(name = "tav_naam")
  private String tavNaam;

  @Column(name = "adres")
  private String adres;

  @Column(name = "email")
  private String email;

  @Column(name = "pc")
  private String pc;

  @Column(name = "plaats")
  private String plaats;

  @Column(name = "kenmerk")
  private String kenmerk;

  @Column(name = "c_grondslag",
      precision = 131089)
  private BigDecimal cGrondslag;

  @Column(name = "c_afweging",
      precision = 131089)
  private BigDecimal cAfweging;

  @Column(name = "motivering_tk")
  private String motiveringTk;

  @Column(name = "c_toek",
      precision = 131089)
  private BigDecimal cToek;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @OneToMany(mappedBy = "gv")
  private List<GvProce> gvProces;

  public Gv() {
  }

  public Long getCGv() {
    return this.cGv;
  }

  public void setCGv(Long cGv) {
    this.cGv = cGv;
  }

  public BigDecimal getAnr() {
    return this.anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getDWijz() {
    return this.dWijz;
  }

  public void setDWijz(BigDecimal dWijz) {
    this.dWijz = dWijz;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public String getBron() {
    return bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
  }

  public String getLeverancier() {
    return leverancier;
  }

  public void setLeverancier(String leverancier) {
    this.leverancier = leverancier;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
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

  public String getKenmerk() {
    return kenmerk;
  }

  public void setKenmerk(String kenmerk) {
    this.kenmerk = kenmerk;
  }

  public BigDecimal getcGrondslag() {
    return cGrondslag;
  }

  public void setcGrondslag(BigDecimal cGrondslag) {
    this.cGrondslag = cGrondslag;
  }

  public String getAanvrager() {
    return aanvrager;
  }

  public void setAanvrager(String aanvrager) {
    this.aanvrager = aanvrager;
  }

  public BigDecimal getcAfweging() {
    return cAfweging;
  }

  public void setcAfweging(BigDecimal cAfweging) {
    this.cAfweging = cAfweging;
  }

  public BigDecimal getcToek() {
    return cToek;
  }

  public void setcToek(BigDecimal cToek) {
    this.cToek = cToek;
  }

  public String getMotiveringTk() {
    return motiveringTk;
  }

  public void setMotiveringTk(String motiveringTk) {
    this.motiveringTk = motiveringTk;
  }

  public List<GvProce> getGvProces() {
    return gvProces;
  }

  public void setGvProces(List<GvProce> gvProces) {
    this.gvProces = gvProces;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
