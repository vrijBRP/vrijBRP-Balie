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
import java.util.Optional;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doss_pers")
public class DossPer extends BaseEntity implements DossPerEntity, DossNatEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_pers",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_pers",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_doss_pers")
  @Column(name = "c_doss_pers", unique = true, nullable = false, precision = 131_089)
  private Long cDossPers;

  @Column(name = "v_doss_pers", precision = 131_089)
  private Long vDossPers;

  @Column(precision = 131_089)
  private BigDecimal anr;

  @Column(precision = 131_089)
  private BigDecimal bsn;

  @Column(name = "burg_staat")
  private String burgStaat;

  @Column(name = "c_geb_land", precision = 131_089)
  private BigDecimal cGebLand;

  @Column(name = "c_immigratie_land", precision = 131_089)
  private BigDecimal cImmigratieLand;

  @Column(name = "c_issue_country", precision = 131089)
  private BigDecimal cIssueCountry;

  @Column(name = "c_geb_plaats", precision = 131_089)
  private BigDecimal cGebPlaats;

  @Column(name = "c_woon_gemeente", precision = 131_089)
  private BigDecimal cWoonGemeente;

  @Column(name = "c_land", precision = 131_089)
  private BigDecimal cLand;

  @Column(name = "c_vbt", precision = 131_089)
  private BigDecimal cVbt;

  @Column(name = "d_burg_staat", precision = 131_089)
  private BigDecimal dBurgStaat;

  @Column(name = "d_geb", precision = 131_089)
  private BigDecimal dGeb;

  @Column(name = "d_overl", precision = 131_089)
  private BigDecimal dOverl;

  @Column(name = "d_immigratie", precision = 131_089)
  private BigDecimal dImmigratie;

  @Column(name = "d_moment", precision = 131_089)
  private BigDecimal dMoment;

  @Column(name = "geb_plaats")
  private String gebPlaats;

  @Column(name = "geb_plaats_akte")
  private String gebPlaatsAkte;

  @Column(name = "geb_land_akte")
  private String gebLandAkte;

  @Column(precision = 131_089)
  private BigDecimal geheim;

  @Column(name = "documentnr")
  private String idDocNr;

  @Column()
  private String soort;

  @Column()
  private int curatele;

  @Column()
  private String gesl;

  @Column()
  private String geslachtsnaam;

  @Column()
  private String aktenaam;

  @Column(precision = 131_089)
  private BigDecimal hnr;

  @Column(name = "hnr_a")
  private String hnrA;

  @Column(name = "hnr_l")
  private String hnrL;

  @Column(name = "hnr_t")
  private String hnrT;

  @Column(length = 10)
  private String pc;

  @Column(name = "woon_plaats")
  private String woonPlaats;

  @Column(name = "woon_plaats_akte")
  private String woonPlaatsAkte;

  @Column(name = "woon_land_akte")
  private String woonLandAkte;

  @Column()
  private String straat;

  @Column(name = "t_geb", precision = 131_089)
  private BigDecimal tGeb;

  @Column(length = 2147483647)
  private String toelichting;

  @Column()
  private String tp;

  @Column(name = "type_persoon", precision = 131_089)
  private BigDecimal typePersoon;

  @Column()
  private String voorn;

  @Column(length = 1)
  private String ng;

  @Column()
  private String voorv;

  @Column(name = "voorn_controle", precision = 131_089)
  private BigDecimal voornControle;

  @Column(name = "b_naamskeuze", precision = 131_089)
  private BigDecimal bNaamskeuze;

  @Column(name = "geb_akte_nr")
  private String gebAkteNr;

  @Column(name = "geb_akte_brp_nr")
  private String gebAkteBrpNr;

  @Column(name = "geb_akte_jaar", precision = 131_089)
  private BigDecimal gebAkteJaar;

  @Column(name = "c_geb_akte_plaats", precision = 131_089)
  private BigDecimal cGebAktePlaats;

  @Column(name = "checked", precision = 1)
  private int checked;

  @Column(name = "userid")
  private String uid;

  @Column(name = "d_huw_sluit", precision = 131_089)
  private BigDecimal dHuwSluitDatum;

  @Column(name = "c_p_huw_sluit", precision = 131_089)
  private BigDecimal cHuwSluitPlaats;

  @Column(name = "p_huw_sluit")
  private String huwSluitPlaats;

  @Column(name = "l_huw_sluit", precision = 131_089)
  private BigDecimal cHuwSluitLand;

  @Column(name = "d_huw_ontb", precision = 131_089)
  private BigDecimal dHuwOntbDatum;

  @Column(name = "c_p_huw_ontb", precision = 131_089)
  private BigDecimal cHuwOntbPlaats;

  @Column(name = "p_huw_ontb")
  private String huwOntbPlaats;

  @Column(name = "l_huw_ontb", precision = 131_089)
  private BigDecimal cHuwOntbLand;

  @Column(name = "rdn_huw_ontb", length = 1)
  private String huwOntbRdn;

  @Column(name = "srt_huw", length = 1)
  private String srtHuw;

  @Column(name = "p_voorn")
  private String pVoorn;

  @Column(name = "p_voorv")
  private String pVoorv;

  @Column(name = "p_naam")
  private String pNaam;

  @Column(name = "p_tp")
  private String pTp;

  @Column(name = "prev_type", length = 1)
  @Getter
  @Setter
  private String prevType;

  @Column(name = "prev_description", length = 255)
  @Getter
  @Setter
  private String prevDescription;

  @Column(name = "bron", length = 255)
  @Getter
  @Setter
  private String bron;

  @ManyToMany
  @JoinTable(name = "doss_pers_akte",
      joinColumns = {
          @JoinColumn(name = "c_doss_pers", nullable = false) },
      inverseJoinColumns = {
          @JoinColumn(name = "c_doss_akte", nullable = false) })
  private List<DossAkte> dossAktes;

  @ManyToOne
  @JoinColumn(name = "c_doss")
  private Doss doss;

  @ManyToOne
  @JoinColumn(name = "c_doss_parent_pers")
  private DossPer parentDossPer;

  @OneToMany(mappedBy = "parentDossPer", cascade = { CascadeType.REMOVE })
  private List<DossPer> dossPers;

  @OneToMany(mappedBy = "dossPer", cascade = { CascadeType.REMOVE })
  private List<DossNatio> dossNatios;

  @OneToMany(mappedBy = "dossPers", orphanRemoval = true, cascade = CascadeType.ALL)
  @Getter
  private List<DossTravelDoc> dossTravelDocs;

  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "c_doss_source_doc")
  @Setter
  private DossSourceDoc dossSourceDoc;

  @ManyToMany
  @JoinTable(name = "doss_pers_presentievraag",
      joinColumns = { @JoinColumn(name = "c_doss_pers",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_presentievraag",
          nullable = false) })
  @Getter
  @Setter
  private List<PresVraag> presentievragen;

  public DossPer() {
  }

  public BigDecimal getDOverl() {
    return dOverl;
  }

  public void setDOverl(BigDecimal dOverl) {
    this.dOverl = dOverl;
  }

  public BigDecimal getdHuwSluitDatum() {
    return dHuwSluitDatum;
  }

  public void setdHuwSluitDatum(BigDecimal dHuwSluitDatum) {
    this.dHuwSluitDatum = dHuwSluitDatum;
  }

  public BigDecimal getcHuwSluitPlaats() {
    return cHuwSluitPlaats;
  }

  public void setcHuwSluitPlaats(BigDecimal cHuwSluitPlaats) {
    this.cHuwSluitPlaats = cHuwSluitPlaats;
  }

  public BigDecimal getcHuwSluitLand() {
    return cHuwSluitLand;
  }

  public void setcHuwSluitLand(BigDecimal cHuwSluitLand) {
    this.cHuwSluitLand = cHuwSluitLand;
  }

  public BigDecimal getdHuwOntbDatum() {
    return dHuwOntbDatum;
  }

  public void setdHuwOntbDatum(BigDecimal dHuwOntbDatum) {
    this.dHuwOntbDatum = dHuwOntbDatum;
  }

  public BigDecimal getcHuwOntbPlaats() {
    return cHuwOntbPlaats;
  }

  public void setcHuwOntbPlaats(BigDecimal cHuwOntbPlaats) {
    this.cHuwOntbPlaats = cHuwOntbPlaats;
  }

  public BigDecimal getcHuwOntbLand() {
    return cHuwOntbLand;
  }

  public void setcHuwOntbLand(BigDecimal cHuwOntbLand) {
    this.cHuwOntbLand = cHuwOntbLand;
  }

  public String getHuwOntbRdn() {
    return huwOntbRdn;
  }

  public void setHuwOntbRdn(String huwOntbRdn) {
    this.huwOntbRdn = huwOntbRdn;
  }

  public Long getCDossPers() {
    return cDossPers;
  }

  public void setCDossPers(Long cDossPers) {
    this.cDossPers = cDossPers;
  }

  public BigDecimal getAnr() {
    return anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public String getBurgStaat() {
    return burgStaat;
  }

  public void setBurgStaat(String burgStaat) {
    this.burgStaat = burgStaat;
  }

  public BigDecimal getCGebLand() {
    return cGebLand;
  }

  public void setCGebLand(BigDecimal cGebLand) {
    this.cGebLand = cGebLand;
  }

  public BigDecimal getCGebPlaats() {
    return cGebPlaats;
  }

  public void setCGebPlaats(BigDecimal cGebPlaats) {
    this.cGebPlaats = cGebPlaats;
  }

  public BigDecimal getCLand() {
    return cLand;
  }

  public void setCLand(BigDecimal cLand) {
    this.cLand = cLand;
  }

  public BigDecimal getCVbt() {
    return cVbt;
  }

  public void setCVbt(BigDecimal cVbt) {
    this.cVbt = cVbt;
  }

  public BigDecimal getDBurgStaat() {
    return dBurgStaat;
  }

  public void setDBurgStaat(BigDecimal dBurgStaat) {
    this.dBurgStaat = dBurgStaat;
  }

  public BigDecimal getDGeb() {
    return dGeb;
  }

  public void setDGeb(BigDecimal dGeb) {
    this.dGeb = dGeb;
  }

  public BigDecimal getDMoment() {
    return dMoment;
  }

  public void setDMoment(BigDecimal dMoment) {
    this.dMoment = dMoment;
  }

  public String getGebPlaats() {
    return gebPlaats;
  }

  public void setGebPlaats(String gebPlaats) {
    this.gebPlaats = gebPlaats;
  }

  public BigDecimal getGeheim() {
    return geheim;
  }

  public void setGeheim(BigDecimal geheim) {
    this.geheim = geheim;
  }

  public String getGesl() {
    return gesl;
  }

  public void setGesl(String gesl) {
    this.gesl = gesl;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public BigDecimal getHnr() {
    return hnr;
  }

  public void setHnr(BigDecimal hnr) {
    this.hnr = hnr;
  }

  public String getHnrA() {
    return hnrA;
  }

  public void setHnrA(String hnrA) {
    this.hnrA = hnrA;
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {
    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {
    this.hnrT = hnrT;
  }

  public String getPc() {
    return pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getWoonPlaats() {
    return woonPlaats;
  }

  public void setWoonPlaats(String woonPlaats) {
    this.woonPlaats = woonPlaats;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public BigDecimal getTGeb() {
    return tGeb;
  }

  public void setTGeb(BigDecimal tGeb) {
    this.tGeb = tGeb;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public String getTp() {
    return tp;
  }

  public void setTp(String tp) {
    this.tp = tp;
  }

  public BigDecimal getTypePersoon() {
    return typePersoon;
  }

  public void setTypePersoon(BigDecimal typePersoon) {
    this.typePersoon = typePersoon;
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

  public List<DossAkte> getDossAktes() {
    return dossAktes;
  }

  public void setDossAktes(List<DossAkte> dossAktes) {
    this.dossAktes = dossAktes;
  }

  public BigDecimal getbNaamskeuze() {
    return bNaamskeuze;
  }

  public void setbNaamskeuze(BigDecimal bNaamskeuze) {
    this.bNaamskeuze = bNaamskeuze;
  }

  public BigDecimal getGebAkteJaar() {
    return gebAkteJaar;
  }

  public void setGebAkteJaar(BigDecimal gebAkteJaar) {
    this.gebAkteJaar = gebAkteJaar;
  }

  public String getGebAkteNr() {
    return gebAkteNr;
  }

  public void setGebAkteNr(String gebAkteNr) {
    this.gebAkteNr = gebAkteNr;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public DossPer getParentDossPer() {
    return parentDossPer;
  }

  @Override
  public void setParentDossPer(DossPer parentDossPer) {
    this.parentDossPer = parentDossPer;
  }

  @Override
  public List<DossPer> getDossPers() {
    return dossPers;
  }

  public void setDossPers(List<DossPer> dossPers) {
    this.dossPers = dossPers;
  }

  @Override
  public List<DossNatio> getDossNats() {
    return dossNatios;
  }

  public void setDossNatios(List<DossNatio> dossNatios) {
    this.dossNatios = dossNatios;
  }

  public BigDecimal getCWoonGemeente() {
    return cWoonGemeente;
  }

  public void setCWoonGemeente(BigDecimal cWoonGemeente) {
    this.cWoonGemeente = cWoonGemeente;
  }

  public String getGebPlaatsAkte() {
    return gebPlaatsAkte;
  }

  public void setGebPlaatsAkte(String gebPlaatsAkte) {
    this.gebPlaatsAkte = gebPlaatsAkte;
  }

  public int getCuratele() {
    return curatele;
  }

  public void setCuratele(int curatele) {
    this.curatele = curatele;
  }

  public BigDecimal getDImmigratie() {
    return dImmigratie;
  }

  public void setDImmigratie(BigDecimal dImmigratie) {
    this.dImmigratie = dImmigratie;
  }

  public BigDecimal getCImmigratieLand() {
    return cImmigratieLand;
  }

  public void setCImmigratieLand(BigDecimal cImmigratieLand) {
    this.cImmigratieLand = cImmigratieLand;
  }

  public int getChecked() {
    return checked;
  }

  public void setChecked(int checked) {
    this.checked = checked;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getAktenaam() {
    return aktenaam;
  }

  public void setAktenaam(String aktenaam) {
    this.aktenaam = aktenaam;
  }

  public BigDecimal getcGebAktePlaats() {
    return cGebAktePlaats;
  }

  public void setcGebAktePlaats(BigDecimal cGebAktePlaats) {
    this.cGebAktePlaats = cGebAktePlaats;
  }

  public String getWoonPlaatsAkte() {
    return woonPlaatsAkte;
  }

  public void setWoonPlaatsAkte(String woonPlaatsAkte) {
    this.woonPlaatsAkte = woonPlaatsAkte;
  }

  public BigDecimal getVoornControle() {
    return voornControle;
  }

  public void setVoornControle(BigDecimal voornControle) {
    this.voornControle = voornControle;
  }

  public String getWoonLandAkte() {
    return woonLandAkte;
  }

  public void setWoonLandAkte(String woonLandAkte) {
    this.woonLandAkte = woonLandAkte;
  }

  public String getGebLandAkte() {
    return gebLandAkte;
  }

  public void setGebLandAkte(String gebLandAkte) {
    this.gebLandAkte = gebLandAkte;
  }

  public String getNg() {
    return ng;
  }

  public void setNg(String ng) {
    this.ng = ng;
  }

  public String getHuwSluitPlaats() {
    return huwSluitPlaats;
  }

  public void setHuwSluitPlaats(String huwSluitPlaats) {
    this.huwSluitPlaats = huwSluitPlaats;
  }

  public String getHuwOntbPlaats() {
    return huwOntbPlaats;
  }

  public void setHuwOntbPlaats(String huwOntbPlaats) {
    this.huwOntbPlaats = huwOntbPlaats;
  }

  public String getSrtHuw() {
    return srtHuw;
  }

  public void setSrtHuw(String srtHuw) {
    this.srtHuw = srtHuw;
  }

  public Long getvDossPers() {
    return vDossPers;
  }

  public void setvDossPers(Long vDossPers) {
    this.vDossPers = vDossPers;
  }

  public String getGebAkteBrpNr() {
    return gebAkteBrpNr;
  }

  public void setGebAkteBrpNr(String gebAkteBrpNr) {
    this.gebAkteBrpNr = gebAkteBrpNr;
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

  public String getpNaam() {
    return pNaam;
  }

  public void setpNaam(String pNaam) {
    this.pNaam = pNaam;
  }

  public String getpTp() {
    return pTp;
  }

  public void setpTp(String pTp) {
    this.pTp = pTp;
  }

  public void removeDossTravelDoc(DossTravelDoc travelDoc) {
    dossTravelDocs.remove(travelDoc);
  }

  public void addDossTravelDoc(DossTravelDoc travelDoc) {
    dossTravelDocs.add(travelDoc);
  }

  public void removePresentievraag(PresVraag presVraag) {
    presentievragen.remove(presVraag);
  }

  public void addPresentievraag(PresVraag presVraag) {
    presentievragen.add(presVraag);
  }

  public void removeDossSourceDoc() {
    dossSourceDoc = null;
  }

  public Optional<DossSourceDoc> getDossSourceDoc() {
    return Optional.ofNullable(dossSourceDoc);
  }

  public BigDecimal getCIssueCountry() {
    return cIssueCountry;
  }

  public void setCIssueCountry(BigDecimal cIssueCountry) {
    this.cIssueCountry = cIssueCountry;
  }

  public String getIdDocNr() {
    return idDocNr;
  }

  public void setIdDocNr(String idDocNr) {
    this.idDocNr = idDocNr;
  }

  public String getSoort() {
    return soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public boolean hasPresenceQuestion() {
    return presentievragen != null && !presentievragen.isEmpty();
  }
}
