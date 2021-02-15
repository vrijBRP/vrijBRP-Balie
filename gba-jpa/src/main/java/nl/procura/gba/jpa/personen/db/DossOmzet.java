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
@Table(name = "doss_omzet")
public class DossOmzet extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_omzet",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossOmzet;

  @Column(name = "ceremonie_toel",
      length = 2147483647)
  private String ceremonieToel;

  @Column(name = "d_end_status",
      precision = 131089)
  private BigDecimal dEndStatus;

  @Column(name = "d_sl",
      precision = 131089)
  private BigDecimal dSl;

  @Column(name = "c_huw_gem",
      precision = 131089)
  private BigDecimal cHuwGem;

  @Column(name = "gem_getuigen",
      precision = 131089)
  private BigDecimal gemGetuigen;

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

  @Column(name = "p1_recht",
      precision = 131089)
  private BigDecimal p1Recht;

  @Column(name = "p2_naam")
  private String p2Naam;

  @Column(name = "p2_ng")
  private String p2Ng;

  @Column(name = "p2_recht",
      precision = 131089)
  private BigDecimal p2Recht;

  @Column(name = "status_sl",
      length = 1)
  private String statusSl;

  @Column(name = "t_sl",
      precision = 131089)
  private BigDecimal tSl;

  @Column(name = "wijze_vn",
      length = 1)
  private String wijzeVn;

  @Column(name = "datum_gps")
  private BigDecimal datumGps;

  @Column(name = "plaats_gps")
  private String plaatsGps;

  @Column(name = "land_gps")
  private BigDecimal landGps;

  @Column(name = "akte_nr_gps")
  private String akteNrGps;

  @Column(name = "akte_brp_nr_gps")
  private String akteBrpNrGps;

  @Column(name = "akte_plaats_gps")
  private String aktePlaatsGps;

  @Column(name = "akte_jaar_gps")
  private BigDecimal akteJaarGps;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_omzet",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_huw_locatie")
  private HuwLocatie huwLocatie;

  @OneToMany(cascade = { CascadeType.REMOVE },
      mappedBy = "dossHuw")
  private List<DossOmzetOptie> dossHuwOpties;

  public DossOmzet() {
  }

  public Long getCDossOmzet() {
    return this.cDossOmzet;
  }

  public void setCDossOmzet(Long cDossOmzet) {
    this.cDossOmzet = cDossOmzet;
  }

  public String getCeremonieToel() {
    return this.ceremonieToel;
  }

  public void setCeremonieToel(String ceremonieToel) {
    this.ceremonieToel = ceremonieToel;
  }

  public BigDecimal getDEndStatus() {
    return this.dEndStatus;
  }

  public void setDEndStatus(BigDecimal dEndStatus) {
    this.dEndStatus = dEndStatus;
  }

  public BigDecimal getDSl() {
    return this.dSl;
  }

  public void setDSl(BigDecimal dSl) {
    this.dSl = dSl;
  }

  public BigDecimal getGemGetuigen() {
    return this.gemGetuigen;
  }

  public void setGemGetuigen(BigDecimal gemGetuigen) {
    this.gemGetuigen = gemGetuigen;
  }

  public String getP1Naam() {
    return this.p1Naam;
  }

  public void setP1Naam(String p1Naam) {
    this.p1Naam = p1Naam;
  }

  public String getP1Ng() {
    return this.p1Ng;
  }

  public void setP1Ng(String p1Ng) {
    this.p1Ng = p1Ng;
  }

  public BigDecimal getP1Recht() {
    return this.p1Recht;
  }

  public void setP1Recht(BigDecimal p1Recht) {
    this.p1Recht = p1Recht;
  }

  public String getP2Naam() {
    return this.p2Naam;
  }

  public void setP2Naam(String p2Naam) {
    this.p2Naam = p2Naam;
  }

  public String getP2Ng() {
    return this.p2Ng;
  }

  public void setP2Ng(String p2Ng) {
    this.p2Ng = p2Ng;
  }

  public BigDecimal getP2Recht() {
    return this.p2Recht;
  }

  public void setP2Recht(BigDecimal p2Recht) {
    this.p2Recht = p2Recht;
  }

  public String getStatusSl() {
    return this.statusSl;
  }

  public void setStatusSl(String statusSl) {
    this.statusSl = statusSl;
  }

  public BigDecimal getTSl() {
    return this.tSl;
  }

  public void setTSl(BigDecimal tSl) {
    this.tSl = tSl;
  }

  public String getWijzeVn() {
    return this.wijzeVn;
  }

  public void setWijzeVn(String wijzeVn) {
    this.wijzeVn = wijzeVn;
  }

  public Doss getDoss() {
    return this.doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public HuwLocatie getHuwLocatie() {
    return this.huwLocatie;
  }

  public void setHuwLocatie(HuwLocatie huwLocatie) {
    this.huwLocatie = huwLocatie;
  }

  public List<DossOmzetOptie> getDossOmzetOpties() {
    return this.dossHuwOpties;
  }

  public void setDossOmzetOpties(List<DossOmzetOptie> dossHuwOpties) {
    this.dossHuwOpties = dossHuwOpties;
  }

  public BigDecimal getcHuwGem() {
    return cHuwGem;
  }

  public void setcHuwGem(BigDecimal cHuwGem) {
    this.cHuwGem = cHuwGem;
  }

  public String getP2Voorv() {
    return p2Voorv;
  }

  public void setP2Voorv(String p2Voorv) {
    this.p2Voorv = p2Voorv;
  }

  public String getP1Voorv() {
    return p1Voorv;
  }

  public void setP1Voorv(String p1Voorv) {
    this.p1Voorv = p1Voorv;
  }

  public String getP2Titel() {
    return p2Titel;
  }

  public void setP2Titel(String p2Titel) {
    this.p2Titel = p2Titel;
  }

  public String getP1Titel() {
    return p1Titel;
  }

  public void setP1Titel(String p1Titel) {
    this.p1Titel = p1Titel;
  }

  public BigDecimal getDatumGps() {
    return datumGps;
  }

  public void setDatumGps(BigDecimal datumGps) {
    this.datumGps = datumGps;
  }

  public String getPlaatsGps() {
    return plaatsGps;
  }

  public void setPlaatsGps(String plaatsGps) {
    this.plaatsGps = plaatsGps;
  }

  public BigDecimal getLandGps() {
    return landGps;
  }

  public void setLandGps(BigDecimal landGps) {
    this.landGps = landGps;
  }

  public String getAkteNrGps() {
    return akteNrGps;
  }

  public void setAkteNrGps(String akteNrGps) {
    this.akteNrGps = akteNrGps;
  }

  public String getAktePlaatsGps() {
    return aktePlaatsGps;
  }

  public void setAktePlaatsGps(String aktePlaatsGps) {
    this.aktePlaatsGps = aktePlaatsGps;
  }

  public BigDecimal getAkteJaarGps() {
    return akteJaarGps;
  }

  public void setAkteJaarGps(BigDecimal akteJaarGps) {
    this.akteJaarGps = akteJaarGps;
  }

  public String getAkteBrpNrGps() {
    return akteBrpNrGps;
  }

  public void setAkteBrpNrGps(String akteBrpNrGps) {
    this.akteBrpNrGps = akteBrpNrGps;
  }
}
