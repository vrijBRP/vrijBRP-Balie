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
@Table(name = "doss_ontb")
public class DossOntb extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_ontb",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossOntb;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_ontb",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @Column(name = "soort_vb",
      length = 1)
  private String soortVb;

  @Column(name = "d_vb",
      precision = 131089)
  private BigDecimal dVb;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "p_vb")
  private String plaatsVb;

  @Column(name = "l_vb")
  private BigDecimal landVb;

  @Column(name = "akte_nr")
  private String akteNr;

  @Column(name = "brp_akte_nr")
  private String brpAkteNr;

  @Column(name = "akte_plaats")
  private String aktePlaats;

  @Column(name = "akte_jaar")
  private BigDecimal akteJaar;

  @Column(name = "uitspraak")
  private String uitspraak;

  @Column(name = "d_uitspraak",
      precision = 131089)
  private BigDecimal dUitspraak;

  @Column(name = "d_gewijsde")
  private BigDecimal dGewijsde;

  @Column(name = "verzoek_door")
  private String verzoekDoor;

  @Column(name = "verzoek_door_oms")
  private String verzoekDoorOms;

  @Column(name = "d_verzoek")
  private BigDecimal dVerzoek;

  @Column(name = "binnen_termijn")
  private BigDecimal binnenTerm;

  @Column(name = "soort_einde_vb")
  private String soortEindeVb;

  @Column(name = "d_verklaring")
  private BigDecimal dVerklaring;

  @Column(name = "ondert_door")
  private String ondertDoor;

  @Column(name = "d_ondert")
  private BigDecimal dOndert;

  @Column(name = "p1_voorv")
  private String p1Voorv;

  @Column(name = "p2_voorv")
  private String p2Voorv;

  @Column(name = "p1_titel")
  private String p1Titel;

  @Column(name = "p2_titel")
  private String p2Titel;

  @Column(name = "p1_naam")
  private String p1Naam;

  @Column(name = "p1_ng",
      length = 1)
  private String p1Ng;

  @Column(name = "p2_naam")
  private String p2Naam;

  @Column(name = "p2_ng")
  private String p2Ng;

  @Column(name = "adv_naam")
  private String advNaam;

  @Column(name = "adv_tav_aanhef")
  private String advTavAanhef;

  @Column(name = "adv_tav_voorl")
  private String advTavVoorl;

  @Column(name = "adv_tav_naam")
  private String advTavNaam;

  @Column(name = "adv_adres")
  private String advAdres;

  @Column(name = "adv_pc")
  private String advPc;

  @Column(name = "adv_plaats")
  private String advPlaats;

  @Column(name = "adv_land")
  private String advLand;

  @Column(name = "adv_kenmerk")
  private String advKenmerk;

  @Column(name = "adv_kenmerk2")
  private String advKenmerk2;

  @Column(name = "c_ontb_gem",
      precision = 131089)
  private BigDecimal cOntbGem;

  public DossOntb() {
  }

  public Long getCDossOntb() {
    return this.cDossOntb;
  }

  public void setCDossOntb(Long cDossOntb) {
    this.cDossOntb = cDossOntb;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public String getSoortVb() {
    return soortVb;
  }

  public void setSoortVb(String soortVb) {
    this.soortVb = soortVb;
  }

  public BigDecimal getdVb() {
    return dVb;
  }

  public void setdVb(BigDecimal dVb) {
    this.dVb = dVb;
  }

  public String getPlaatsVb() {
    return plaatsVb;
  }

  public void setPlaatsVb(String plaatsVb) {
    this.plaatsVb = plaatsVb;
  }

  public BigDecimal getLandVb() {
    return landVb;
  }

  public void setLandVb(BigDecimal landVb) {
    this.landVb = landVb;
  }

  public String getAkteNr() {
    return akteNr;
  }

  public void setAkteNr(String akteNr) {
    this.akteNr = akteNr;
  }

  public String getAktePlaats() {
    return aktePlaats;
  }

  public void setAktePlaats(String aktePlaats) {
    this.aktePlaats = aktePlaats;
  }

  public BigDecimal getAkteJaar() {
    return akteJaar;
  }

  public void setAkteJaar(BigDecimal akteJaar) {
    this.akteJaar = akteJaar;
  }

  public String getUitspraak() {
    return uitspraak;
  }

  public void setUitspraak(String uitspraak) {
    this.uitspraak = uitspraak;
  }

  public BigDecimal getdGewijsde() {
    return dGewijsde;
  }

  public void setdGewijsde(BigDecimal dGewijsde) {
    this.dGewijsde = dGewijsde;
  }

  public String getVerzoekDoor() {
    return verzoekDoor;
  }

  public void setVerzoekDoor(String verzoekDoor) {
    this.verzoekDoor = verzoekDoor;
  }

  public BigDecimal getdVerzoek() {
    return dVerzoek;
  }

  public void setdVerzoek(BigDecimal dVerzoek) {
    this.dVerzoek = dVerzoek;
  }

  public BigDecimal getBinnenTerm() {
    return binnenTerm;
  }

  public void setBinnenTerm(BigDecimal binnenTermijn) {
    this.binnenTerm = binnenTermijn;
  }

  public String getSoortEindeVb() {
    return soortEindeVb;
  }

  public void setSoortEindeVb(String soortEindeVb) {
    this.soortEindeVb = soortEindeVb;
  }

  public BigDecimal getdVerklaring() {
    return dVerklaring;
  }

  public void setdVerklaring(BigDecimal dVerklaring) {
    this.dVerklaring = dVerklaring;
  }

  public BigDecimal getdOndert() {
    return dOndert;
  }

  public void setdOndert(BigDecimal dOndert) {
    this.dOndert = dOndert;
  }

  public String getOndertDoor() {
    return ondertDoor;
  }

  public void setOndertDoor(String ondertDoor) {
    this.ondertDoor = ondertDoor;
  }

  public String getVerzoekDoorOms() {
    return verzoekDoorOms;
  }

  public void setVerzoekDoorOms(String verzoekDoorOms) {
    this.verzoekDoorOms = verzoekDoorOms;
  }

  public String getP1Voorv() {
    return p1Voorv;
  }

  public void setP1Voorv(String p1Voorv) {
    this.p1Voorv = p1Voorv;
  }

  public String getP2Voorv() {
    return p2Voorv;
  }

  public void setP2Voorv(String p2Voorv) {
    this.p2Voorv = p2Voorv;
  }

  public String getP1Titel() {
    return p1Titel;
  }

  public void setP1Titel(String p1Titel) {
    this.p1Titel = p1Titel;
  }

  public String getP2Titel() {
    return p2Titel;
  }

  public void setP2Titel(String p2Titel) {
    this.p2Titel = p2Titel;
  }

  public String getP1Naam() {
    return p1Naam;
  }

  public void setP1Naam(String p1Naam) {
    this.p1Naam = p1Naam;
  }

  public String getP1Ng() {
    return p1Ng;
  }

  public void setP1Ng(String p1Ng) {
    this.p1Ng = p1Ng;
  }

  public String getP2Naam() {
    return p2Naam;
  }

  public void setP2Naam(String p2Naam) {
    this.p2Naam = p2Naam;
  }

  public String getP2Ng() {
    return p2Ng;
  }

  public void setP2Ng(String p2Ng) {
    this.p2Ng = p2Ng;
  }

  public BigDecimal getdUitspraak() {
    return dUitspraak;
  }

  public void setdUitspraak(BigDecimal dUitspraak) {
    this.dUitspraak = dUitspraak;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getAdvNaam() {
    return advNaam;
  }

  public void setAdvNaam(String advNaam) {
    this.advNaam = advNaam;
  }

  public String getAdvAdres() {
    return advAdres;
  }

  public void setAdvAdres(String advAdres) {
    this.advAdres = advAdres;
  }

  public String getAdvPc() {
    return advPc;
  }

  public void setAdvPc(String advPc) {
    this.advPc = advPc;
  }

  public String getAdvPlaats() {
    return advPlaats;
  }

  public void setAdvPlaats(String advPlaats) {
    this.advPlaats = advPlaats;
  }

  public String getAdvKenmerk() {
    return advKenmerk;
  }

  public void setAdvKenmerk(String advKenmerk) {
    this.advKenmerk = advKenmerk;
  }

  public String getAdvTavAanhef() {
    return advTavAanhef;
  }

  public void setAdvTavAanhef(String advTavAanhef) {
    this.advTavAanhef = advTavAanhef;
  }

  public String getAdvTavVoorl() {
    return advTavVoorl;
  }

  public void setAdvTavVoorl(String advTavVoorl) {
    this.advTavVoorl = advTavVoorl;
  }

  public String getAdvTavNaam() {
    return advTavNaam;
  }

  public void setAdvTavNaam(String advTavNaam) {
    this.advTavNaam = advTavNaam;
  }

  public String getBrpAkteNr() {
    return brpAkteNr;
  }

  public void setBrpAkteNr(String brpAkteNr) {
    this.brpAkteNr = brpAkteNr;
  }

  public String getAdvLand() {
    return advLand;
  }

  public void setAdvLand(String advLand) {
    this.advLand = advLand;
  }

  public String getAdvKenmerk2() {
    return advKenmerk2;
  }

  public void setAdvKenmerk2(String advKenmerk2) {
    this.advKenmerk2 = advKenmerk2;
  }

  public BigDecimal getcOntbGem() {
    return cOntbGem;
  }

  public void setcOntbGem(BigDecimal cHuwGem) {
    this.cOntbGem = cHuwGem;
  }
}
