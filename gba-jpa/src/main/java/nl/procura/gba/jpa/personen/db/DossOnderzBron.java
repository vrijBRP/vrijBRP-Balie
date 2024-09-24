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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import nl.procura.gba.jpa.personen.converters.BigDecimalStringConverter;

@Entity
@Table(name = "doss_onderz_bron")
public class DossOnderzBron extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_onderz_bron",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_onderz_bron",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_onderz_bron")
  @Column(name = "c_doss_onderz_bron",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossOnderzBron;

  @Column(name = "bron")
  private String bron;

  @Column(name = "gesprek")
  private String gesprek;

  @Column(name = "adr_type")
  private BigDecimal adrType;

  @Column(name = "adr")
  private String adr;

  @Column(name = "adr_hnr")
  @Convert(converter = BigDecimalStringConverter.class)
  private String hnr;

  @Column(name = "adr_hnr_l")
  private String hnrL;

  @Column(name = "adr_hnr_t")
  private String hnrT;

  @Column(name = "adr_hnr_a")
  private String hnrA;

  @Column(name = "adr_pc")
  private String pc;

  @Column(name = "adr_plaats")
  private String plaats;

  @Column(name = "adr_c_gem")
  private BigDecimal cGem;

  @Column(name = "adr_buitenl1")
  private String buitenl1;

  @Column(name = "adr_buitenl2")
  private String buitenl2;

  @Column(name = "adr_buitenl3")
  private String buitenl3;

  @Column(name = "adr_c_land")
  private BigDecimal cLand;

  @Column(name = "inst")
  private String inst;

  @Column(name = "inst_afdeling")
  private String instAfdeling;

  @Column(name = "inst_naam")
  private String instNaam;

  @Column(name = "inst_aanhef")
  private String instAanhef;

  @Column(name = "inst_voorl")
  private String instVoorl;

  @Column(name = "inst_email")
  private String instEmail;

  @Column(name = "summ_type")
  private BigDecimal summType;

  @Column(name = "inst_bsn_betrok")
  private BigDecimal instBsnBetrok;

  @Column(name = "inst_bsn_rel")
  private BigDecimal instBsnRel;

  @Column(name = "inst_aanschr")
  private String instAanschr;

  @Column(name = "address_source")
  private String addressSource;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_onderz")
  private DossOnderz dossOnderz;

  public DossOnderzBron() {
  }

  public Long getcDossOnderzBron() {
    return cDossOnderzBron;
  }

  public void setcDossOnderzBron(Long cDossOnderzBron) {
    this.cDossOnderzBron = cDossOnderzBron;
  }

  public String getBron() {
    return bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
  }

  public String getGesprek() {
    return gesprek;
  }

  public void setGesprek(String gesprek) {
    this.gesprek = gesprek;
  }

  public BigDecimal getAdrType() {
    return adrType;
  }

  public void setAdrType(BigDecimal adresType) {
    this.adrType = adresType;
  }

  public String getAdr() {
    return adr;
  }

  public void setAdr(String adr) {
    this.adr = adr;
  }

  public String getHnr() {
    return hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
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

  public String getHnrA() {
    return hnrA;
  }

  public void setHnrA(String hnrA) {
    this.hnrA = hnrA;
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

  public BigDecimal getcGem() {
    return cGem;
  }

  public void setcGem(BigDecimal cGem) {
    this.cGem = cGem;
  }

  public String getBuitenl1() {
    return buitenl1;
  }

  public void setBuitenl1(String buitenl1) {
    this.buitenl1 = buitenl1;
  }

  public String getBuitenl2() {
    return buitenl2;
  }

  public void setBuitenl2(String buitenl2) {
    this.buitenl2 = buitenl2;
  }

  public String getBuitenl3() {
    return buitenl3;
  }

  public void setBuitenl3(String buitenl3) {
    this.buitenl3 = buitenl3;
  }

  public BigDecimal getcLand() {
    return cLand;
  }

  public void setcLand(BigDecimal cLand) {
    this.cLand = cLand;
  }

  public DossOnderz getDossOnderz() {
    return dossOnderz;
  }

  public void setDossOnderz(DossOnderz dossOnderz) {
    this.dossOnderz = dossOnderz;
  }

  public String getInst() {
    return inst;
  }

  public void setInst(String inst) {
    this.inst = inst;
  }

  public String getInstNaam() {
    return instNaam;
  }

  public void setInstNaam(String instNaam) {
    this.instNaam = instNaam;
  }

  public String getInstAanhef() {
    return instAanhef;
  }

  public void setInstAanhef(String instTav) {
    this.instAanhef = instTav;
  }

  public String getInstVoorl() {
    return instVoorl;
  }

  public void setInstVoorl(String instVoorl) {
    this.instVoorl = instVoorl;
  }

  public String getInstEmail() {
    return instEmail;
  }

  public void setInstEmail(String instEmail) {
    this.instEmail = instEmail;
  }

  public String getInstAfdeling() {
    return instAfdeling;
  }

  public void setInstAfdeling(String instAfdeling) {
    this.instAfdeling = instAfdeling;
  }

  public BigDecimal getInstBsnBetrok() {
    return instBsnBetrok;
  }

  public void setInstBsnBetrok(BigDecimal instBsnBetrok) {
    this.instBsnBetrok = instBsnBetrok;
  }

  public BigDecimal getInstBsnRel() {
    return instBsnRel;
  }

  public void setInstBsnRel(BigDecimal instBsnRel) {
    this.instBsnRel = instBsnRel;
  }

  public String getInstAanschr() {
    return instAanschr;
  }

  public void setInstAanschr(String instAanschr) {
    this.instAanschr = instAanschr;
  }

  public BigDecimal getSummType() {
    return summType;
  }

  public void setSummType(BigDecimal summType) {
    this.summType = summType;
  }

  public String getAddressSource() {
    return addressSource;
  }

  public void setAddressSource(String addressSource) {
    this.addressSource = addressSource;
  }
}
