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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "doss_geb")
public class DossGeb extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_geb",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossGeb;

  @Column(name = "c_land_afstam_recht",
      precision = 131089)
  private BigDecimal cLandAfstamRecht;

  @Column(name = "afstamming_stap",
      precision = 131089)
  private BigDecimal afstammingStap;

  @Column(name = "c_land_afstam_vb",
      precision = 131089)
  private BigDecimal cLandAfstamVb;

  @Column(name = "gezin_type")
  private String gezinType;

  @Column(name = "c_land_naam_recht",
      precision = 131089)
  private BigDecimal cLandNaamRecht;

  @Column(name = "reden_verplicht")
  private String redenVerplicht;

  @Column(name = "reden_tardieve")
  private String redenTardieve;

  @Column(precision = 131089)
  private BigDecimal tardieve;

  @Column(name = "type_erkenning")
  private String typeErkenning;

  @Column(name = "d_in_erkenning",
      precision = 131089)
  private BigDecimal dInErkenning;

  @Column(name = "akte_erkenning")
  private String akteErkenning;

  @Column(name = "toest_id_erkenning")
  private String toestIdErkenning;

  @Column(name = "rechtbank_erk")
  private String rechtbankErk;

  @Column(name = "c_land_afstam_recht_erk",
      precision = 131089)
  private BigDecimal cLandAfstamRechtErk;

  @Column(name = "c_land_erk",
      precision = 131089)
  private BigDecimal cLandErk;

  @Column(name = "c_gem_erk",
      precision = 131089)
  private BigDecimal cGemErk;

  @Column(name = "plaats_erk")
  private String plaatsErk;

  @Column(name = "c_gem_geb",
      precision = 131089)
  private BigDecimal cGemGeb;

  @Column(name = "keuze_naam_gesl")
  private String keuzeNaamGesl;

  @Column(name = "keuze_naam_voorv")
  private String keuzeNaamVoorv;

  @Column(name = "keuze_naam_tp")
  private String keuzeNaamTp;

  @Column(name = "b_naamskeuze",
      precision = 131089)
  private BigDecimal bNaamskeuze;

  @Column(name = "b_eerste_kind",
      precision = 131089)
  private BigDecimal bEersteKind;

  @Column(name = "t_naamskeuze",
      precision = 131089)
  private BigDecimal tNaamskeuze;

  @Column(name = "t_naamskeuze_erkenning",
      precision = 131089)
  private BigDecimal tNaamskeuzeErkenning;

  @Column(name = "b_naamskeuze_erkenning",
      precision = 131089)
  private BigDecimal bNaamskeuzeErkenning;

  @Column(name = "wijze_bez")
  private String wijzeBez;

  @Column(name = "d_lijkbez",
      nullable = false,
      precision = 131089)
  private BigDecimal dLijkbez;

  @Column(name = "t_lijkbez",
      nullable = false,
      precision = 131089)
  private BigDecimal tLijkbez;

  @Column(name = "p_ontleding")
  private String pOntleding;

  @Column(name = "b_buit_bnlx",
      nullable = false,
      precision = 1)
  private BigDecimal bBuitBnlx;

  @Column(name = "c_land_best",
      nullable = false,
      precision = 131089)
  private BigDecimal cLandBest;

  @Column(name = "vervrmid")
  private String vervrmid;

  @Column(name = "termijn_bez",
      nullable = false)
  private String termijnBez;

  @Column(name = "ontv_doc1")
  private String ontvDoc1;

  @Column(name = "plaats_best")
  private String plaatsBestemming;

  @Column(name = "via")
  private String viaBestemming;

  @Column(name = "doodsoorzaak")
  private String doodsoorz;

  @Column(name = "verzoek_ind")
  private boolean verzoekInd;

  @Column(name = "verzoek_bsn_vader_duo_moeder")
  private BigDecimal verzoekBsnVaderDuoMoeder;

  @Column(name = "verzoek_keuze_naam_gesl")
  private String verzoekKeuzeNaamGesl;

  @Column(name = "verzoek_keuze_naam_voorv")
  private String verzoekKeuzeNaamVoorv;

  @Column(name = "verzoek_keuze_naam_titel")
  private String verzoekKeuzeNaamTitel;

  @Column(name = "type_nk")
  private String typeNk;

  @Column(name = "akte_nk")
  private String akteNk;

  @Column(name = "plaats_nk")
  private String plaatsNk;

  @Column(name = "d_in_nk")
  private BigDecimal dInNk;

  @Column(name = "c_gem_nk")
  private BigDecimal cGemNk;

  @Column(name = "c_land_nk")
  private BigDecimal cLandNk;

  @Column(name = "person_type_nk")
  private BigDecimal personTypeNk;

  @Column(name = "bijz_nk")
  private String bijzNk;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_geb",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_erk")
  private DossErk dossErk;

  @ManyToOne(cascade = { CascadeType.REMOVE })
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_erk_geb")
  private DossErk dossErkGeb;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_nk")
  private DossNk dossNk;

  public DossGeb() {
  }

  public Long getCDossGeb() {
    return this.cDossGeb;
  }

  public void setCDossGeb(Long cDossGeb) {
    this.cDossGeb = cDossGeb;
  }

  public BigDecimal getAfstammingStap() {
    return this.afstammingStap;
  }

  public void setAfstammingStap(BigDecimal afstammingStap) {
    this.afstammingStap = afstammingStap;
  }

  public String getGezinType() {
    return this.gezinType;
  }

  public void setGezinType(String gezinType) {
    this.gezinType = gezinType;
  }

  public String getRedenVerplicht() {
    return this.redenVerplicht;
  }

  public void setRedenVerplicht(String redenVerplicht) {
    this.redenVerplicht = redenVerplicht;
  }

  public BigDecimal getTardieve() {
    return this.tardieve;
  }

  public void setTardieve(BigDecimal tardieve) {
    this.tardieve = tardieve;
  }

  public Doss getDoss() {
    return this.doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public DossErk getDossErk() {
    return this.dossErk;
  }

  public void setDossErk(DossErk dossErk) {
    this.dossErk = dossErk;
  }

  public String getRedenTardieve() {
    return redenTardieve;
  }

  public void setRedenTardieve(String redenTardieve) {
    this.redenTardieve = redenTardieve;
  }

  public BigDecimal getdInErkenning() {
    return dInErkenning;
  }

  public void setdInErkenning(BigDecimal dInErkenning) {
    this.dInErkenning = dInErkenning;
  }

  public String getAkteErkenning() {
    return akteErkenning;
  }

  public void setAkteErkenning(String akteErkenning) {
    this.akteErkenning = akteErkenning;
  }

  public String getToestIdErkenning() {
    return toestIdErkenning;
  }

  public void setToestIdErkenning(String toestIdErkenning) {
    this.toestIdErkenning = toestIdErkenning;
  }

  public String getTypeErkenning() {
    return typeErkenning;
  }

  public void setTypeErkenning(String typeErkenning) {
    this.typeErkenning = typeErkenning;
  }

  public BigDecimal getcGemErk() {
    return cGemErk;
  }

  public void setcGemErk(BigDecimal cGemErk) {
    this.cGemErk = cGemErk;
  }

  public String getRechtbankErk() {
    return rechtbankErk;
  }

  public void setRechtbankErk(String rechtbankErk) {
    this.rechtbankErk = rechtbankErk;
  }

  public BigDecimal getcLandAfstamVb() {
    return cLandAfstamVb;
  }

  public void setcLandAfstamVb(BigDecimal cLand) {
    this.cLandAfstamVb = cLand;
  }

  public BigDecimal getcLandAfstamRecht() {
    return cLandAfstamRecht;
  }

  public void setcLandAfstamRecht(BigDecimal cLand) {
    this.cLandAfstamRecht = cLand;
  }

  public BigDecimal getcLandNaamRecht() {
    return cLandNaamRecht;
  }

  public void setcLandNaamRecht(BigDecimal cLand) {
    this.cLandNaamRecht = cLand;
  }

  public BigDecimal getcLandAfstamRechtErk() {
    return cLandAfstamRechtErk;
  }

  public void setcLandAfstamRechtErk(BigDecimal cLand) {
    this.cLandAfstamRechtErk = cLand;
  }

  public DossErk getDossErkGeb() {
    return dossErkGeb;
  }

  public void setDossErkGeb(DossErk dossErkGeb) {
    this.dossErkGeb = dossErkGeb;
  }

  public BigDecimal gettNaamskeuze() {
    return tNaamskeuze;
  }

  public void settNaamskeuze(BigDecimal tNaamskeuze) {
    this.tNaamskeuze = tNaamskeuze;
  }

  public BigDecimal getbEersteKind() {
    return bEersteKind;
  }

  public void setbEersteKind(BigDecimal bEersteKind) {
    this.bEersteKind = bEersteKind;
  }

  public BigDecimal getbNaamskeuze() {
    return bNaamskeuze;
  }

  public void setbNaamskeuze(BigDecimal bNaamskeuze) {
    this.bNaamskeuze = bNaamskeuze;
  }

  public String getKeuzeNaamVoorv() {
    return keuzeNaamVoorv;
  }

  public void setKeuzeNaamVoorv(String keuzeNaamVoorv) {
    this.keuzeNaamVoorv = keuzeNaamVoorv;
  }

  public String getKeuzeNaamGesl() {
    return keuzeNaamGesl;
  }

  public void setKeuzeNaamGesl(String keuzeNaamGesl) {
    this.keuzeNaamGesl = keuzeNaamGesl;
  }

  public BigDecimal gettNaamskeuzeErkenning() {
    return tNaamskeuzeErkenning;
  }

  public void settNaamskeuzeErkenning(BigDecimal tNaamskeuzeErkenning) {
    this.tNaamskeuzeErkenning = tNaamskeuzeErkenning;
  }

  public BigDecimal getbNaamskeuzeErkenning() {
    return bNaamskeuzeErkenning;
  }

  public void setbNaamskeuzeErkenning(BigDecimal bNaamskeuzeErkenning) {
    this.bNaamskeuzeErkenning = bNaamskeuzeErkenning;
  }

  public BigDecimal getcGemGeb() {
    return cGemGeb;
  }

  public void setcGemGeb(BigDecimal cGemGeb) {
    this.cGemGeb = cGemGeb;
  }

  public String getWijzeBez() {
    return wijzeBez;
  }

  public void setWijzeBez(String wijzeBez) {
    this.wijzeBez = wijzeBez;
  }

  public BigDecimal getdLijkbez() {
    return dLijkbez;
  }

  public void setdLijkbez(BigDecimal dLijkbez) {
    this.dLijkbez = dLijkbez;
  }

  public BigDecimal gettLijkbez() {
    return tLijkbez;
  }

  public void settLijkbez(BigDecimal tLijkbez) {
    this.tLijkbez = tLijkbez;
  }

  public BigDecimal getbBuitBnlx() {
    return bBuitBnlx;
  }

  public void setbBuitBnlx(BigDecimal bBuitBnlx) {
    this.bBuitBnlx = bBuitBnlx;
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

  public String getTermijnBez() {
    return termijnBez;
  }

  public void setTermijnBez(String termijnBez) {
    this.termijnBez = termijnBez;
  }

  public String getOntvDoc1() {
    return ontvDoc1;
  }

  public void setOntvDoc1(String ontvDoc1) {
    this.ontvDoc1 = ontvDoc1;
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

  public String getKeuzeNaamTp() {
    return keuzeNaamTp;
  }

  public void setKeuzeNaamTp(String keuzeNaamTp) {
    this.keuzeNaamTp = keuzeNaamTp;
  }

  public String getPlaatsErk() {
    return plaatsErk;
  }

  public void setPlaatsErk(String plaatsErk) {
    this.plaatsErk = plaatsErk;
  }

  public BigDecimal getcLandErk() {
    return cLandErk;
  }

  public void setcLandErk(BigDecimal cLandErk) {
    this.cLandErk = cLandErk;
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

  public BigDecimal getVerzoekBsnVaderDuoMoeder() {
    return verzoekBsnVaderDuoMoeder;
  }

  public boolean isVerzoekInd() {
    return verzoekInd;
  }

  public void setVerzoekInd(boolean checkOn) {
    this.verzoekInd = checkOn;
  }

  public void setVerzoekBsnVaderDuoMoeder(BigDecimal apiBsnVaderDuoMoeder) {
    this.verzoekBsnVaderDuoMoeder = apiBsnVaderDuoMoeder;
  }

  public String getVerzoekKeuzeNaamGesl() {
    return verzoekKeuzeNaamGesl;
  }

  public void setVerzoekKeuzeNaamGesl(String apiKeuzeNaamGesl) {
    this.verzoekKeuzeNaamGesl = apiKeuzeNaamGesl;
  }

  public String getVerzoekKeuzeNaamVoorv() {
    return verzoekKeuzeNaamVoorv;
  }

  public void setVerzoekKeuzeNaamVoorv(String apiKeuzeNaamVoorv) {
    this.verzoekKeuzeNaamVoorv = apiKeuzeNaamVoorv;
  }

  public String getVerzoekKeuzeNaamTitel() {
    return verzoekKeuzeNaamTitel;
  }

  public void setVerzoekKeuzeNaamTitel(String checkKeuzeNaamTitel) {
    this.verzoekKeuzeNaamTitel = checkKeuzeNaamTitel;
  }

  public DossNk getDossNk() {
    return dossNk;
  }

  public void setDossNk(DossNk dossNk) {
    this.dossNk = dossNk;
  }

  public String getTypeNk() {
    return typeNk;
  }

  public void setTypeNk(String typeNk) {
    this.typeNk = typeNk;
  }

  public String getAkteNk() {
    return akteNk;
  }

  public void setAkteNk(String akteNk) {
    this.akteNk = akteNk;
  }

  public String getPlaatsNk() {
    return plaatsNk;
  }

  public void setPlaatsNk(String plaatsNk) {
    this.plaatsNk = plaatsNk;
  }

  public BigDecimal getdInNk() {
    return dInNk;
  }

  public void setdInNk(BigDecimal dInNk) {
    this.dInNk = dInNk;
  }

  public BigDecimal getcGemNk() {
    return cGemNk;
  }

  public void setcGemNk(BigDecimal cGemNk) {
    this.cGemNk = cGemNk;
  }

  public BigDecimal getcLandNk() {
    return cLandNk;
  }

  public void setcLandNk(BigDecimal cLandNk) {
    this.cLandNk = cLandNk;
  }

  public BigDecimal getPersonTypeNk() {
    return personTypeNk;
  }

  public void setPersonTypeNk(BigDecimal personTypeNk) {
    this.personTypeNk = personTypeNk;
  }

  public String getBijzNk() {
    return bijzNk;
  }

  public void setBijzNk(String bijzNk) {
    this.bijzNk = bijzNk;
  }
}
