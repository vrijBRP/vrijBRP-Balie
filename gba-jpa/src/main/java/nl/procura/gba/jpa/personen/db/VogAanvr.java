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
@Table(name = "vog_aanvr")
public class VogAanvr extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_vog_aanvr",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "vog_aanvr",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_vog_aanvr")
  @Column(name = "c_vog_aanvr",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cVogAanvr;

  @Column()
  private String aanschrijf;

  @Column(name = "aanvr_id",
      precision = 131089)
  private BigDecimal aanvrId;

  @Column(name = "adres_afw_a",
      precision = 131089)
  private BigDecimal adresAfwA;

  @Column(nullable = false,
      precision = 131089)
  private BigDecimal anr;

  @Column(name = "bijzonder_tekst",
      length = 2147483647)
  private String bijzonderTekst;

  @Column(name = "burg_advies")
  private String burgAdvies;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "c_gem",
      nullable = false,
      precision = 131089)
  private BigDecimal cGem;

  @Column(name = "c_land_a",
      precision = 131089)
  private BigDecimal cLandA;

  @Column(name = "c_land_b",
      precision = 131089)
  private BigDecimal cLandB;

  @Column(name = "c_loc",
      nullable = false,
      precision = 131089)
  private BigDecimal cLoc;

  @Column(name = "d_aanvr",
      nullable = false,
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "t_aanvr",
      nullable = false,
      precision = 131089)
  private BigDecimal tAanvr;

  @Column(name = "d_geb_a",
      precision = 131089)
  private BigDecimal dGebA;

  @Column(name = "doel_func")
  private String doelFunc;

  @Column(name = "doel_tekst",
      length = 2147483647)
  private String doelTekst;

  @Column(name = "email_a")
  private String emailA;

  @Column(name = "gesl_a")
  private String geslA;

  @Column(name = "hnr_a",
      precision = 131089)
  private BigDecimal hnrA;

  @Column(name = "hnr_b",
      precision = 131089)
  private BigDecimal hnrB;

  @Column(name = "hnr_l_a")
  private String hnrLA;

  @Column(name = "hnr_l_b")
  private String hnrLB;

  @Column(name = "hnr_t_a")
  private String hnrTA;

  @Column(name = "hnr_t_b")
  private String hnrTB;

  @Column(name = "ind_verwerkt",
      nullable = false,
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "instelling_b")
  private String instellingB;

  @Column(name = "l_geb_a",
      precision = 131089)
  private BigDecimal lGebA;

  @Column(name = "naam_a")
  private String naamA;

  @Column(name = "naam_b")
  private String naamB;

  @Column(name = "omstandig_tekst",
      length = 2147483647)
  private String omstandigTekst;

  @Column(name = "p_geb_a")
  private String pGebA;

  @Column(name = "pc_a")
  private String pcA;

  @Column(name = "pc_b")
  private String pcB;

  @Column(name = "plaats_a")
  private String plaatsA;

  @Column(name = "plaats_b")
  private String plaatsB;

  @Column(name = "straat_a")
  private String straatA;

  @Column(name = "straat_b")
  private String straatB;

  @Column(name = "tel_b")
  private String telB;

  @Column(name = "tel_mob_a")
  private String telMobA;

  @Column(name = "tel_thuis_a")
  private String telThuisA;

  @Column(name = "tel_werk_a")
  private String telWerkA;

  @Column(name = "toelichting_tekst",
      length = 2147483647)
  private String toelichtingTekst;

  @Column(name = "v_aanvr",
      nullable = false,
      precision = 131089)
  private BigDecimal vAanvr;

  @Column(name = "vog_advies_tekst",
      length = 2147483647)
  private String vogAdviesTekst;

  @Column(name = "vog_persist_tekst",
      length = 2147483647)
  private String vogPersistTekst;

  @Column(name = "bsn_a",
      precision = 131089)
  private BigDecimal bsnA;

  @Column(name = "natio_a")
  private String natioA;

  @Column(name = "voorn_a")
  private String voornA;

  @Column(name = "voorv_a")
  private String voorvA;

  @Column(name = "goedkeuring",
      length = 1)
  private String goedkeuring;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "vog_doel")
  private VogDoelTab vogDoelTab;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_scr_prof")
  private VogProfTab vogProfTab;

  @ManyToMany
  @JoinTable(name = "vog_func_asp",
      joinColumns = { @JoinColumn(name = "c_vog_aanvr",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_vog_func_tab",
          nullable = false) })
  private List<VogFuncTab> vogFuncTabs;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  public VogAanvr() {
  }

  @Override
  public Long getUniqueKey() {
    return getCVogAanvr();
  }

  public Long getCVogAanvr() {
    return this.cVogAanvr;
  }

  public void setCVogAanvr(Long cVogAanvr) {
    this.cVogAanvr = cVogAanvr;
  }

  public String getAanschrijf() {
    return this.aanschrijf;
  }

  public void setAanschrijf(String aanschrijf) {
    this.aanschrijf = aanschrijf;
  }

  public BigDecimal getAanvrId() {
    return this.aanvrId;
  }

  public void setAanvrId(BigDecimal aanvrId) {
    this.aanvrId = aanvrId;
  }

  public BigDecimal getAdresAfwA() {
    return this.adresAfwA;
  }

  public void setAdresAfwA(BigDecimal adresAfwA) {
    this.adresAfwA = adresAfwA;
  }

  public BigDecimal getAnr() {
    return this.anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public String getBijzonderTekst() {
    return this.bijzonderTekst;
  }

  public void setBijzonderTekst(String bijzonderTekst) {
    this.bijzonderTekst = bijzonderTekst;
  }

  public String getBurgAdvies() {
    return this.burgAdvies;
  }

  public void setBurgAdvies(String burgAdvies) {
    this.burgAdvies = burgAdvies;
  }

  public BigDecimal getCGem() {
    return this.cGem;
  }

  public void setCGem(BigDecimal cGem) {
    this.cGem = cGem;
  }

  public BigDecimal getCLandA() {
    return this.cLandA;
  }

  public void setCLandA(BigDecimal cLandA) {
    this.cLandA = cLandA;
  }

  public BigDecimal getCLandB() {
    return this.cLandB;
  }

  public void setCLandB(BigDecimal cLandB) {
    this.cLandB = cLandB;
  }

  public BigDecimal getCLoc() {
    return this.cLoc;
  }

  public void setCLoc(BigDecimal cLoc) {
    this.cLoc = cLoc;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
  }

  public BigDecimal getTAanvr() {
    return tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
  }

  public BigDecimal getDGebA() {
    return this.dGebA;
  }

  public void setDGebA(BigDecimal dGebA) {
    this.dGebA = dGebA;
  }

  public String getDoelFunc() {
    return this.doelFunc;
  }

  public void setDoelFunc(String doelFunc) {
    this.doelFunc = doelFunc;
  }

  public String getDoelTekst() {
    return this.doelTekst;
  }

  public void setDoelTekst(String doelTekst) {
    this.doelTekst = doelTekst;
  }

  public String getEmailA() {
    return this.emailA;
  }

  public void setEmailA(String emailA) {
    this.emailA = emailA;
  }

  public String getGeslA() {
    return this.geslA;
  }

  public void setGeslA(String geslA) {
    this.geslA = geslA;
  }

  public BigDecimal getHnrA() {
    return this.hnrA;
  }

  public void setHnrA(BigDecimal hnrA) {
    this.hnrA = hnrA;
  }

  public BigDecimal getHnrB() {
    return this.hnrB;
  }

  public void setHnrB(BigDecimal hnrB) {
    this.hnrB = hnrB;
  }

  public String getHnrLA() {
    return this.hnrLA;
  }

  public void setHnrLA(String hnrLA) {
    this.hnrLA = hnrLA;
  }

  public String getHnrLB() {
    return this.hnrLB;
  }

  public void setHnrLB(String hnrLB) {
    this.hnrLB = hnrLB;
  }

  public String getHnrTA() {
    return this.hnrTA;
  }

  public void setHnrTA(String hnrTA) {
    this.hnrTA = hnrTA;
  }

  public String getHnrTB() {
    return this.hnrTB;
  }

  public void setHnrTB(String hnrTB) {
    this.hnrTB = hnrTB;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getInstellingB() {
    return this.instellingB;
  }

  public void setInstellingB(String instellingB) {
    this.instellingB = instellingB;
  }

  public BigDecimal getLGebA() {
    return this.lGebA;
  }

  public void setLGebA(BigDecimal lGebA) {
    this.lGebA = lGebA;
  }

  public String getNaamA() {
    return this.naamA;
  }

  public void setNaamA(String naamA) {
    this.naamA = naamA;
  }

  public String getNaamB() {
    return this.naamB;
  }

  public void setNaamB(String naamB) {
    this.naamB = naamB;
  }

  public String getOmstandigTekst() {
    return this.omstandigTekst;
  }

  public void setOmstandigTekst(String omstandigTekst) {
    this.omstandigTekst = omstandigTekst;
  }

  public String getPGebA() {
    return this.pGebA;
  }

  public void setPGebA(String pGebA) {
    this.pGebA = pGebA;
  }

  public String getPcA() {
    return this.pcA;
  }

  public void setPcA(String pcA) {
    this.pcA = pcA;
  }

  public String getPcB() {
    return this.pcB;
  }

  public void setPcB(String pcB) {
    this.pcB = pcB;
  }

  public String getPlaatsA() {
    return this.plaatsA;
  }

  public void setPlaatsA(String plaatsA) {
    this.plaatsA = plaatsA;
  }

  public String getPlaatsB() {
    return this.plaatsB;
  }

  public void setPlaatsB(String plaatsB) {
    this.plaatsB = plaatsB;
  }

  public String getStraatA() {
    return this.straatA;
  }

  public void setStraatA(String straatA) {
    this.straatA = straatA;
  }

  public String getStraatB() {
    return this.straatB;
  }

  public void setStraatB(String straatB) {
    this.straatB = straatB;
  }

  public String getTelB() {
    return this.telB;
  }

  public void setTelB(String telB) {
    this.telB = telB;
  }

  public String getTelMobA() {
    return this.telMobA;
  }

  public void setTelMobA(String telMobA) {
    this.telMobA = telMobA;
  }

  public String getTelThuisA() {
    return this.telThuisA;
  }

  public void setTelThuisA(String telThuisA) {
    this.telThuisA = telThuisA;
  }

  public String getTelWerkA() {
    return this.telWerkA;
  }

  public void setTelWerkA(String telWerkA) {
    this.telWerkA = telWerkA;
  }

  public String getToelichtingTekst() {
    return this.toelichtingTekst;
  }

  public void setToelichtingTekst(String toelichtingTekst) {
    this.toelichtingTekst = toelichtingTekst;
  }

  public BigDecimal getVAanvr() {
    return this.vAanvr;
  }

  public void setVAanvr(BigDecimal vAanvr) {
    this.vAanvr = vAanvr;
  }

  public String getVogAdviesTekst() {
    return this.vogAdviesTekst;
  }

  public void setVogAdviesTekst(String vogAdviesTekst) {
    this.vogAdviesTekst = vogAdviesTekst;
  }

  public String getVogPersistTekst() {
    return this.vogPersistTekst;
  }

  public void setVogPersistTekst(String vogPersistTekst) {
    this.vogPersistTekst = vogPersistTekst;
  }

  public String getVoornA() {
    return this.voornA;
  }

  public void setVoornA(String voornA) {
    this.voornA = voornA;
  }

  public String getVoorvA() {
    return this.voorvA;
  }

  public void setVoorvA(String voorvA) {
    this.voorvA = voorvA;
  }

  public VogDoelTab getVogDoelTab() {
    return this.vogDoelTab;
  }

  public void setVogDoelTab(VogDoelTab vogDoelTab) {
    this.vogDoelTab = vogDoelTab;
  }

  public VogProfTab getVogProfTab() {
    return this.vogProfTab;
  }

  public void setVogProfTab(VogProfTab vogProfTab) {
    this.vogProfTab = vogProfTab;
  }

  public List<VogFuncTab> getVogFuncTabs() {
    return this.vogFuncTabs;
  }

  public void setVogFuncTabs(List<VogFuncTab> vogFuncTabs) {
    this.vogFuncTabs = vogFuncTabs;
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

  public BigDecimal getBsnA() {
    return bsnA;
  }

  public void setBsnA(BigDecimal bsnA) {
    this.bsnA = bsnA;
  }

  public String getNatioA() {
    return natioA;
  }

  public void setNatioA(String natioA) {
    this.natioA = natioA;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getGoedkeuring() {
    return goedkeuring;
  }

  public void setGoedkeuring(String goedkeuring) {
    this.goedkeuring = goedkeuring;
  }
}
