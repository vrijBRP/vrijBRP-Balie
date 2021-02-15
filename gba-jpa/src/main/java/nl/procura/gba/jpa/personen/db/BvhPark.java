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
@Table(name = "bvh_park")
public class BvhPark extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_bvh_park",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "bvh_park",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_bvh_park")
  @Column(name = "c_bvh_park",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cBvhPark;

  @Column(length = 1)
  private String aangifte;

  @Column(name = "aangifte_anr")
  private String aangifteAnr;

  @Column(name = "aangifte_bsn",
      precision = 131089)
  private BigDecimal aangifteBsn;

  @Column(name = "aangifte_accept",
      precision = 131089)
  private BigDecimal aangifteAccept;

  @Column(name = "aant_pers",
      precision = 131089)
  private BigDecimal aantPers;

  @Column()
  private String anr;

  @Column(name = "b_adr1")
  private String bAdr1;

  @Column(name = "b_adr2")
  private String bAdr2;

  @Column(name = "b_adr3")
  private String bAdr3;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "c_gem_deel",
      precision = 131089)
  private BigDecimal cGemDeel;

  @Column(name = "c_gem_herkomst",
      precision = 131089)
  private BigDecimal cGemHerkomst;

  @Column(name = "c_locatie",
      precision = 131089)
  private BigDecimal cLocatie;

  @Column(name = "c_straat",
      precision = 131089)
  private BigDecimal cStraat;

  @Column(name = "d_aanv",
      precision = 131089)
  private BigDecimal dAanv;

  @Column(name = "d_opn",
      precision = 131089)
  private BigDecimal dOpn;

  @Column(name = "d_vertrek",
      precision = 131089)
  private BigDecimal dVertrek;

  @Column(name = "d_vestiging",
      precision = 131089)
  private BigDecimal dVestiging;

  @Column(length = 2147483647)
  private String duur;

  @Column(name = "func_adr",
      length = 1)
  private String funcAdr;

  @Column()
  private String gemeentedeel;

  @Column()
  private String woonplaats;

  @Column(length = 5)
  private String hnr;

  @Column(name = "hnr_a",
      length = 2)
  private String hnrA;

  @Column(name = "hnr_l",
      length = 1)
  private String hnrL;

  @Column(name = "hnr_t",
      length = 4)
  private String hnrT;

  @Column(name = "hoofd_bsn")
  private String hoofdBsn;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "l_vertrek",
      precision = 131089)
  private BigDecimal lVertrek;

  @Column(name = "l_vestiging",
      precision = 131089)
  private BigDecimal lVestiging;

  @Column()
  private String locatie;

  @Column(precision = 131089)
  private BigDecimal midoffice;

  @Column(name = "nwe_bestem",
      length = 2147483647)
  private String nweBestem;

  @Column(length = 6)
  private String pc;

  @Column(length = 2147483647)
  private String rechtsfeiten;

  @Column()
  private String straat;

  @Column(name = "t_opn",
      precision = 131089)
  private BigDecimal tOpn;

  @Column(name = "toest_anr")
  private String toestAnr;

  @Column(name = "toest_bsn")
  private String toestBsn;

  @Column(name = "toest_geg",
      precision = 131089)
  private BigDecimal toestGeg;

  @Column(name = "verhuis_type",
      precision = 131089)
  private BigDecimal verhuisType;

  @Column(name = "wijze_bewon")
  private String wijzeBewon;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "goedkeuring",
      length = 1)
  private String goedkeuring;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @Column(name = "a_addr_obj",
      nullable = false,
      length = 2147483647)
  private String aanduidingAdresseerbaarObject = "";

  @Column(name = "idc_nr_aand",
      nullable = false,
      length = 2147483647)
  private String idCodeNummeraanduiding = "";

  @Column(name = "aangever_toelichting",
      length = 2147483647)
  private String aangeverToelichting;

  @Column(name = "toest_anders")
  private String toestAnders;

  @Column(name = "geen_verwerking")
  private int geenVerwerking;

  public BvhPark() {
  }

  public Long getCBvhPark() {
    return this.cBvhPark;
  }

  public void setCBvhPark(Long cBvhPark) {
    this.cBvhPark = cBvhPark;
  }

  public String getAangifte() {
    return this.aangifte;
  }

  public void setAangifte(String aangifte) {
    this.aangifte = aangifte;
  }

  public String getAangifteAnr() {
    return this.aangifteAnr;
  }

  public void setAangifteAnr(String aangifteAnr) {
    this.aangifteAnr = aangifteAnr;
  }

  public BigDecimal getAangifteBsn() {
    return this.aangifteBsn;
  }

  public void setAangifteBsn(BigDecimal aangifteBsn) {
    this.aangifteBsn = aangifteBsn;
  }

  public BigDecimal getAantPers() {
    return this.aantPers;
  }

  public void setAantPers(BigDecimal aantPers) {
    this.aantPers = aantPers;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public String getBAdr1() {
    return this.bAdr1;
  }

  public void setBAdr1(String bAdr1) {
    this.bAdr1 = bAdr1;
  }

  public String getBAdr2() {
    return this.bAdr2;
  }

  public void setBAdr2(String bAdr2) {
    this.bAdr2 = bAdr2;
  }

  public String getBAdr3() {
    return this.bAdr3;
  }

  public void setBAdr3(String bAdr3) {
    this.bAdr3 = bAdr3;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getCGemDeel() {
    return this.cGemDeel;
  }

  public void setCGemDeel(BigDecimal cGemDeel) {
    this.cGemDeel = cGemDeel;
  }

  public BigDecimal getCGemHerkomst() {
    return this.cGemHerkomst;
  }

  public void setCGemHerkomst(BigDecimal cGemHerkomst) {
    this.cGemHerkomst = cGemHerkomst;
  }

  public BigDecimal getCLocatie() {
    return this.cLocatie;
  }

  public void setCLocatie(BigDecimal cLocatie) {
    this.cLocatie = cLocatie;
  }

  public BigDecimal getCStraat() {
    return this.cStraat;
  }

  public void setCStraat(BigDecimal cStraat) {
    this.cStraat = cStraat;
  }

  public BigDecimal getDAanv() {
    return this.dAanv;
  }

  public void setDAanv(BigDecimal dAanv) {
    this.dAanv = dAanv;
  }

  public BigDecimal getDOpn() {
    return this.dOpn;
  }

  public void setDOpn(BigDecimal dOpn) {
    this.dOpn = dOpn;
  }

  public BigDecimal getDVertrek() {
    return this.dVertrek;
  }

  public void setDVertrek(BigDecimal dVertrek) {
    this.dVertrek = dVertrek;
  }

  public BigDecimal getDVestiging() {
    return this.dVestiging;
  }

  public void setDVestiging(BigDecimal dVestiging) {
    this.dVestiging = dVestiging;
  }

  public String getDuur() {
    return this.duur;
  }

  public void setDuur(String duur) {
    this.duur = duur;
  }

  public String getFuncAdr() {
    return this.funcAdr;
  }

  public void setFuncAdr(String funcAdr) {
    this.funcAdr = funcAdr;
  }

  public String getGemeentedeel() {
    return this.gemeentedeel;
  }

  public void setGemeentedeel(String gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public String getHnr() {
    return this.hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
  }

  public String getHnrA() {
    return this.hnrA;
  }

  public void setHnrA(String hnrA) {
    this.hnrA = hnrA;
  }

  public String getHnrL() {
    return this.hnrL;
  }

  public void setHnrL(String hnrL) {
    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return this.hnrT;
  }

  public void setHnrT(String hnrT) {
    this.hnrT = hnrT;
  }

  public String getHoofdBsn() {
    return this.hoofdBsn;
  }

  public void setHoofdBsn(String hoofdBsn) {
    this.hoofdBsn = hoofdBsn;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public BigDecimal getLVertrek() {
    return this.lVertrek;
  }

  public void setLVertrek(BigDecimal lVertrek) {
    this.lVertrek = lVertrek;
  }

  public BigDecimal getLVestiging() {
    return this.lVestiging;
  }

  public void setLVestiging(BigDecimal lVestiging) {
    this.lVestiging = lVestiging;
  }

  public String getLocatie() {
    return this.locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public BigDecimal getMidoffice() {
    return this.midoffice;
  }

  public void setMidoffice(BigDecimal midoffice) {
    this.midoffice = midoffice;
  }

  public String getNweBestem() {
    return this.nweBestem;
  }

  public void setNweBestem(String nweBestem) {
    this.nweBestem = nweBestem;
  }

  public String getPc() {
    return this.pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getRechtsfeiten() {
    return this.rechtsfeiten;
  }

  public void setRechtsfeiten(String rechtsfeiten) {
    this.rechtsfeiten = rechtsfeiten;
  }

  public String getStraat() {
    return this.straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public BigDecimal getTOpn() {
    return this.tOpn;
  }

  public void setTOpn(BigDecimal tOpn) {
    this.tOpn = tOpn;
  }

  public String getToestAnr() {
    return this.toestAnr;
  }

  public void setToestAnr(String toestAnr) {
    this.toestAnr = toestAnr;
  }

  public String getToestBsn() {
    return this.toestBsn;
  }

  public void setToestBsn(String toestBsn) {
    this.toestBsn = toestBsn;
  }

  public BigDecimal getToestGeg() {
    return this.toestGeg;
  }

  public void setToestGeg(BigDecimal toestGeg) {
    this.toestGeg = toestGeg;
  }

  public BigDecimal getVerhuisType() {
    return this.verhuisType;
  }

  public void setVerhuisType(BigDecimal verhuisType) {
    this.verhuisType = verhuisType;
  }

  public String getWijzeBewon() {
    return this.wijzeBewon;
  }

  public void setWijzeBewon(String wijzeBewon) {
    this.wijzeBewon = wijzeBewon;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
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

  public BigDecimal getAangifteAccept() {
    return aangifteAccept;
  }

  public void setAangifteAccept(BigDecimal aangifteAccept) {
    this.aangifteAccept = aangifteAccept;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getAanduidingAdresseerbaarObject() {
    return aanduidingAdresseerbaarObject;
  }

  public void setAanduidingAdresseerbaarObject(String aanduidingAdresseerbaarObject) {
    this.aanduidingAdresseerbaarObject = aanduidingAdresseerbaarObject;
  }

  public String getIdCodeNummeraanduiding() {
    return idCodeNummeraanduiding;
  }

  public void setIdCodeNummeraanduiding(String idCodeNummeraanduiding) {
    this.idCodeNummeraanduiding = idCodeNummeraanduiding;
  }

  public String getAangeverToelichting() {
    return aangeverToelichting;
  }

  public void setAangeverToelichting(String aangeverToelichting) {
    this.aangeverToelichting = aangeverToelichting;
  }

  public String getToestAnders() {
    return toestAnders;
  }

  public void setToestAnders(String toestAnders) {
    this.toestAnders = toestAnders;
  }

  public String getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(String woonplaats) {
    this.woonplaats = woonplaats;
  }

  public String getGoedkeuring() {
    return goedkeuring;
  }

  public void setGoedkeuring(String goedkeuring) {
    this.goedkeuring = goedkeuring;
  }

  public int getGeenVerwerking() {
    return geenVerwerking;
  }

  public void setGeenVerwerking(int geenVerwerking) {
    this.geenVerwerking = geenVerwerking;
  }
}
