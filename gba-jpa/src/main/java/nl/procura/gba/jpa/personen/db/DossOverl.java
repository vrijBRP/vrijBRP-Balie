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
@Table(name = "doss_overl")
public class DossOverl extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_overl",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossOverl;

  @Column(name = "b_buit_bnlx",
      nullable = false,
      precision = 1)
  private BigDecimal bBuitBnlx;

  @Column(name = "c_land_best",
      nullable = false,
      precision = 131089)
  private BigDecimal cLandBest;

  @Column(name = "c_overl_gem",
      nullable = false,
      precision = 131089)
  private BigDecimal cOverlGem;

  @Column(name = "d_lijkbez",
      nullable = false,
      precision = 131089)
  private BigDecimal dLijkbez;

  @Column(name = "d_lijkvin",
      nullable = false,
      precision = 131089)
  private BigDecimal dLijkvin;

  @Column(name = "d_overl",
      nullable = false,
      precision = 131089)
  private BigDecimal dOverl;

  @Column(name = "ontv_doc",
      nullable = false)
  private String ontvDoc;

  @Column(name = "ontv_doc1")
  private String ontvDoc1;

  @Column(name = "plaats_best")
  private String plaatsBestemming;

  @Column(name = "via")
  private String viaBestemming;

  @Column(name = "doodsoorzaak")
  private String doodsoorz;

  @Column(name = "t_lijkbez",
      nullable = false,
      precision = 131089)
  private BigDecimal tLijkbez;

  @Column(name = "t_lijkvin",
      nullable = false,
      precision = 131089)
  private BigDecimal tLijkvin;

  @Column(name = "t_overl",
      nullable = false,
      precision = 131089)
  private BigDecimal tOverl;

  @Column(name = "termijn_bez",
      nullable = false)
  private String termijnBez;

  @Column(name = "vervrmid")
  private String vervrmid;

  @Column(name = "wijze_bez")
  private String wijzeBez;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_overl",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @Column(name = "just_aang",
      nullable = false)
  private String cJustAang;

  @Column(name = "plaats_toev",
      length = 2147483647)
  private String plaatsToev;

  @Column(name = "d_ontvangst",
      nullable = false,
      precision = 131089)
  private BigDecimal dOntvangst;

  @Column(name = "afgever",
      nullable = false)
  private String cAfgever;

  @Column(name = "btl_plaats")
  private String btlPlaats;

  @Column(name = "p_ontleding")
  private String pOntleding;

  @Column(name = "c_land_overl",
      nullable = false,
      precision = 131089)
  private BigDecimal cLandOverl;

  @Column(name = "type_doc")
  private String typeDoc;

  @Column(name = "c_land_afg",
      nullable = false,
      precision = 131089)
  private BigDecimal cLandAfg;

  @Column(name = "beschr")
  private String beschr;

  @Column(name = "b_voldoet",
      nullable = false,
      precision = 1)
  private BigDecimal bVoldoet;

  @Column(name = "verzoek_ind")
  private boolean verzoekInd;

  @Column(name = "verzoek_overl_voorn")
  private String verzoekOverlVoorn;

  @Column(name = "verzoek_overl_gesl_naam")
  private String verzoekOverlGeslNaam;

  @Column(name = "verzoek_overl_voorv")
  private String verzoekOverlVoorv;

  @Column(name = "verzoek_overl_titel")
  private String verzoekOverlTitel;

  @Column(name = "verzoek_overl_d_geb")
  private BigDecimal verzoekOverlGeboortedatum;

  @Column(name = "verzoek_overl_p_geb")
  private String verzoekOverlGeboorteplaats;

  @Column(name = "verzoek_overl_l_geb")
  private BigDecimal verzoekOverlGeboorteland;

  @OneToMany(cascade = CascadeType.REMOVE,
      mappedBy = "dossOverl")
  private List<DossOverlUitt> dossOverlUitts;

  @OneToOne(cascade = CascadeType.REMOVE)
  @PrimaryKeyJoinColumn(referencedColumnName = "c_doss_corr_dest")
  private DossCorrDest dossCorrDest;

  public DossOverl() {
  }

  public Long getcDossOverl() {
    return cDossOverl;
  }

  public void setcDossOverl(Long cDossOverl) {
    this.cDossOverl = cDossOverl;
  }

  public BigDecimal getbBuitBnlx() {
    return bBuitBnlx;
  }

  public void setbBuitBnlx(BigDecimal bBuitBnlx) {
    this.bBuitBnlx = bBuitBnlx;
  }

  public BigDecimal getcOverlGem() {
    return cOverlGem;
  }

  public void setcOverlGem(BigDecimal cOverlGem) {
    this.cOverlGem = cOverlGem;
  }

  public BigDecimal getdLijkbez() {
    return dLijkbez;
  }

  public void setdLijkbez(BigDecimal dLijkbez) {
    this.dLijkbez = dLijkbez;
  }

  public BigDecimal getdLijkvin() {
    return dLijkvin;
  }

  public void setdLijkvin(BigDecimal dLijkvin) {
    this.dLijkvin = dLijkvin;
  }

  public BigDecimal getdOverl() {
    return dOverl;
  }

  public void setdOverl(BigDecimal dOverl) {
    this.dOverl = dOverl;
  }

  public String getOntvDoc() {
    return ontvDoc;
  }

  public void setOntvDoc(String ontvDoc) {
    this.ontvDoc = ontvDoc;
  }

  public String getOntvDoc1() {
    return ontvDoc1;
  }

  public void setOntvDoc1(String ontvDoc1) {
    this.ontvDoc1 = ontvDoc1;
  }

  public BigDecimal gettLijkbez() {
    return tLijkbez;
  }

  public void settLijkbez(BigDecimal tLijkbez) {
    this.tLijkbez = tLijkbez;
  }

  public BigDecimal gettLijkvin() {
    return tLijkvin;
  }

  public void settLijkvin(BigDecimal tLijkvin) {
    this.tLijkvin = tLijkvin;
  }

  public BigDecimal gettOverl() {
    return tOverl;
  }

  public void settOverl(BigDecimal tOverl) {
    this.tOverl = tOverl;
  }

  public String getTermijnBez() {
    return termijnBez;
  }

  public void setTermijnBez(String termijnBez) {
    this.termijnBez = termijnBez;
  }

  public String getWijzeBez() {
    return wijzeBez;
  }

  public void setWijzeBez(String wijzeBez) {
    this.wijzeBez = wijzeBez;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public BigDecimal getcLandBest() {
    return cLandBest;
  }

  public void setcLandBest(BigDecimal cLandBest) {
    this.cLandBest = cLandBest;
  }

  public String getVervrmid() {
    return vervrmid;
  }

  public void setVervrmid(String vervrmid) {
    this.vervrmid = vervrmid;
  }

  public String getcJustAang() {
    return cJustAang;
  }

  public void setcJustAang(String cJustAang) {
    this.cJustAang = cJustAang;
  }

  public String getPlaatsToev() {
    return plaatsToev;
  }

  public void setPlaatsToev(String plaatsToev) {
    this.plaatsToev = plaatsToev;
  }

  public BigDecimal getdOntvangst() {
    return dOntvangst;
  }

  public void setdOntvangst(BigDecimal dOntvangst) {
    this.dOntvangst = dOntvangst;
  }

  public String getcAfgever() {
    return cAfgever;
  }

  public void setcAfgever(String cAfgever) {
    this.cAfgever = cAfgever;
  }

  public String getBtlPlaats() {
    return btlPlaats;
  }

  public void setBtlPlaats(String btlPlaats) {
    this.btlPlaats = btlPlaats;
  }

  public BigDecimal getcLandOverl() {
    return cLandOverl;
  }

  public void setcLandOverl(BigDecimal cLandOverl) {
    this.cLandOverl = cLandOverl;
  }

  public String getTypeDoc() {
    return typeDoc;
  }

  public void setTypeDoc(String typeDoc) {
    this.typeDoc = typeDoc;
  }

  public BigDecimal getcLandAfg() {
    return cLandAfg;
  }

  public void setcLandAfg(BigDecimal cLandAfg) {
    this.cLandAfg = cLandAfg;
  }

  public String getBeschr() {
    return beschr;
  }

  public void setBeschr(String beschr) {
    this.beschr = beschr;
  }

  public BigDecimal getbVoldoet() {
    return bVoldoet;
  }

  public void setbVoldoet(BigDecimal bVoldoet) {
    this.bVoldoet = bVoldoet;
  }

  public String getPlaatsBestemming() {
    return plaatsBestemming;
  }

  public void setPlaatsBestemming(String plaatsBestemming) {
    this.plaatsBestemming = plaatsBestemming;
  }

  public String getViaBestemming() {
    return viaBestemming;
  }

  public void setViaBestemming(String viaBestemming) {
    this.viaBestemming = viaBestemming;
  }

  public String getDoodsoorz() {
    return doodsoorz;
  }

  public void setDoodsoorz(String doodsoorz) {
    this.doodsoorz = doodsoorz;
  }

  public String getpOntleding() {
    return pOntleding;
  }

  public void setpOntleding(String pOntleding) {
    this.pOntleding = pOntleding;
  }

  public boolean isVerzoekInd() {
    return verzoekInd;
  }

  public void setVerzoekInd(boolean verzoekInd) {
    this.verzoekInd = verzoekInd;
  }

  public String getVerzoekOverlVoorn() {
    return verzoekOverlVoorn;
  }

  public void setVerzoekOverlVoorn(String verzoekOverlVoorn) {
    this.verzoekOverlVoorn = verzoekOverlVoorn;
  }

  public String getVerzoekOverlGeslNaam() {
    return verzoekOverlGeslNaam;
  }

  public void setVerzoekOverlGeslNaam(String verzoekOverlGeslNaam) {
    this.verzoekOverlGeslNaam = verzoekOverlGeslNaam;
  }

  public String getVerzoekOverlVoorv() {
    return verzoekOverlVoorv;
  }

  public void setVerzoekOverlVoorv(String verzoekOverlVoorv) {
    this.verzoekOverlVoorv = verzoekOverlVoorv;
  }

  public String getVerzoekOverlTitel() {
    return verzoekOverlTitel;
  }

  public void setVerzoekOverlTitel(String verzoekOverlTitel) {
    this.verzoekOverlTitel = verzoekOverlTitel;
  }

  public BigDecimal getVerzoekOverlGeboortedatum() {
    return verzoekOverlGeboortedatum;
  }

  public void setVerzoekOverlGeboortedatum(BigDecimal verzoekOverlGeboortedatum) {
    this.verzoekOverlGeboortedatum = verzoekOverlGeboortedatum;
  }

  public String getVerzoekOverlGeboorteplaats() {
    return verzoekOverlGeboorteplaats;
  }

  public void setVerzoekOverlGeboorteplaats(String verzoekOverlGeboorteplaats) {
    this.verzoekOverlGeboorteplaats = verzoekOverlGeboorteplaats;
  }

  public BigDecimal getVerzoekOverlGeboorteland() {
    return verzoekOverlGeboorteland;
  }

  public void setVerzoekOverlGeboorteland(BigDecimal verzoekOverlGeboorteland) {
    this.verzoekOverlGeboorteland = verzoekOverlGeboorteland;
  }

  public List<DossOverlUitt> getDossOverlUitts() {
    return dossOverlUitts;
  }

  public void setDossOverlUitts(List<DossOverlUitt> dossOverlUitts) {
    this.dossOverlUitts = dossOverlUitts;
  }

  public DossCorrDest getDossCorrDest() {
    return dossCorrDest;
  }

  public void setDossCorrDest(DossCorrDest dossCorrDest) {
    this.dossCorrDest = dossCorrDest;
  }
}
