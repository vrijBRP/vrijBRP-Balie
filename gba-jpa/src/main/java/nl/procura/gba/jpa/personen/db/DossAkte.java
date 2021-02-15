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
@Table(name = "doss_akte")
public class DossAkte extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_akte",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_akte",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_akte")
  @Column(name = "c_doss_akte",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossAkte;

  @Column(nullable = false,
      precision = 131089)
  private BigDecimal jaar;

  @Column(precision = 131089,
      nullable = false,
      name = "d_in")
  private BigDecimal dIn;

  @Column(nullable = false,
      length = 1)
  private String registerdeel;

  @Column(nullable = false,
      length = 1,
      name = "akte_aand")
  private String akteAand;

  @Column(nullable = false,
      precision = 131089)
  private BigDecimal registersoort;

  @Column(nullable = false,
      precision = 4)
  private BigDecimal vnr;

  @Column(name = "voorn")
  private String voorn;

  @Column(name = "voorv")
  private String voorv;

  @Column(name = "naam")
  private String geslachtsnaam;

  @Column(name = "p_voorn")
  private String pVoorn;

  @Column(name = "p_voorv")
  private String pVoorv;

  @Column(name = "p_naam")
  private String pGeslachtsnaam;

  @Column(precision = 131089,
      nullable = false,
      name = "d_feit")
  private BigDecimal dFeit;

  @Column(name = "geslacht",
      length = 1)
  private String geslacht;

  @Column(name = "p_geslacht",
      length = 1)
  private String pGeslacht;

  @Column(name = "bsn")
  private BigDecimal bsn;

  @Column(name = "p_bsn")
  private BigDecimal pBsn;

  @Column(name = "invoer_type",
      length = 1)
  private String invType;

  @Column(name = "d_geb",
      precision = 131089)
  private BigDecimal dGeb;

  @Column(name = "p_d_geb",
      precision = 131089)
  private BigDecimal pDGeb;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss",
      nullable = false)
  private Doss doss;

  @ManyToMany(mappedBy = "dossAktes")
  private List<DossPer> dossPers;

  public DossAkte() {
  }

  public Long getCDossAkte() {
    return this.cDossAkte;
  }

  public void setCDossAkte(Long cDossAkte) {
    this.cDossAkte = cDossAkte;
  }

  public BigDecimal getJaar() {
    return this.jaar;
  }

  public void setJaar(BigDecimal jaar) {
    this.jaar = jaar;
  }

  public String getRegisterdeel() {
    return this.registerdeel;
  }

  public void setRegisterdeel(String registerdeel) {
    this.registerdeel = registerdeel;
  }

  public BigDecimal getRegistersoort() {
    return this.registersoort;
  }

  public void setRegistersoort(BigDecimal registersoort) {
    this.registersoort = registersoort;
  }

  public BigDecimal getVnr() {
    return this.vnr;
  }

  public void setVnr(BigDecimal vnr) {
    this.vnr = vnr;
  }

  public Doss getDoss() {
    return this.doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public List<DossPer> getDossPers() {
    return this.dossPers;
  }

  public void setDossPers(List<DossPer> dossPers) {
    this.dossPers = dossPers;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getVoorn() {
    return voorn;
  }

  public void setVoorn(String voorn) {
    this.voorn = voorn;
  }

  public String getVoorv() {
    return voorv;
  }

  public void setVoorv(String voorv) {
    this.voorv = voorv;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getpVoorn() {
    return pVoorn;
  }

  public void setpVoorn(String pVoorn) {
    this.pVoorn = pVoorn;
  }

  public String getpVoorv() {
    return pVoorv;
  }

  public void setpVoorv(String pVoorv) {
    this.pVoorv = pVoorv;
  }

  public String getpGeslachtsnaam() {
    return pGeslachtsnaam;
  }

  public void setpGeslachtsnaam(String pGeslachtsnaam) {
    this.pGeslachtsnaam = pGeslachtsnaam;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getpGeslacht() {
    return pGeslacht;
  }

  public void setpGeslacht(String pGeslacht) {
    this.pGeslacht = pGeslacht;
  }

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getpBsn() {
    return pBsn;
  }

  public void setpBsn(BigDecimal pBsn) {
    this.pBsn = pBsn;
  }

  public BigDecimal getdFeit() {
    return dFeit;
  }

  public void setdFeit(BigDecimal dType) {
    this.dFeit = dType;
  }

  public String getInvType() {
    return invType;
  }

  public void setInvType(String invType) {
    this.invType = invType;
  }

  public String getAkteAand() {
    return akteAand;
  }

  public void setAkteAand(String akteAand) {
    this.akteAand = akteAand;
  }

  public BigDecimal getDGeb() {
    return dGeb;
  }

  public void setDGeb(BigDecimal dGeb) {
    this.dGeb = dGeb;
  }

  public BigDecimal getPDGeb() {
    return pDGeb;
  }

  public void setPDGeb(BigDecimal pDGeb) {
    this.pDGeb = pDGeb;
  }
}
