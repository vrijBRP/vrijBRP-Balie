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
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.procura.gba.jpa.personen.converters.BigDecimalBooleanConverter;
import nl.procura.gba.jpa.personen.converters.BigDecimalDateConverter;
import nl.procura.gba.jpa.personen.converters.BigDecimalStringConverter;

@Entity
@Table(name = "doss_onderz")
public class DossOnderz extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_onderz",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossOnderz;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_onderz",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @Column(name = "aanl_bron")
  private BigDecimal aanlBron;

  @Column(name = "aanl_relatie")
  private String aanlRelatie;

  @Column(name = "aanl_tmv_nr")
  private String aanlTmvNr;

  @Column(name = "aanl_inst")
  private String aanlInst;

  @Column(name = "aanl_inst_naam")
  private String aanlInstNaam;

  @Column(name = "aanl_inst_aanhef")
  private String aanlInstAanhef;

  @Column(name = "aanl_inst_voorl")
  private String aanlInstVoorl;

  @Column(name = "aanl_inst_adres")
  private String aanlInstAdres;

  @Column(name = "aanl_inst_pc")
  private String aanlInstPc;

  @Column(name = "aanl_inst_plaats")
  private String aanlInstPlaats;

  @Column(name = "aanl_kenmerk")
  private String aanlKenmerk;

  @Column(name = "aanl_afdeling")
  private String aanlAfdeling;

  @Column(name = "aanl_d_meld_ontv")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanlDMeldOntv;

  @Column(name = "aanl_aard")
  private BigDecimal aanlAard;

  @Column(name = "aanl_aard_anders")
  private String aanlAardAnders;

  @Column(name = "aanl_vermoed_adres")
  private BigDecimal aanlVermoedAdres;

  @Column(name = "aanl_adres")
  private String aanlAdres;

  @Column(name = "aanl_hnr")
  @Convert(converter = BigDecimalStringConverter.class)
  private String aanlHnr;

  @Column(name = "aanl_hnr_l")
  private String aanlHnrL;

  @Column(name = "aanl_hnr_t")
  private String aanlHnrT;

  @Column(name = "aanl_hnr_a")
  private String aanlHnrA;

  @Column(name = "aanl_pc")
  private String aanlPc;

  @Column(name = "aanl_plaats")
  private String aanlPlaats;

  @Column(name = "aanl_c_gem")
  private BigDecimal aanlCGem;

  @Column(name = "aanl_buitenl1")
  private String aanlBuitenl1;

  @Column(name = "aanl_buitenl2")
  private String aanlBuitenl2;

  @Column(name = "aanl_buitenl3")
  private String aanlBuitenl3;

  @Column(name = "aanl_c_land")
  private BigDecimal aanlCLand;

  @Column(name = "aanl_aant_pers")
  private BigDecimal aanlAantPers;

  @Column(name = "beoord_d_end_term")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date beoordDEndTerm;

  @Column(name = "beoord_binnen_term")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean beoordBinnenTerm;

  @Column(name = "beoord_reden_term")
  private String redenTerm;

  @Column(name = "onderz_d_aanvang")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date onderzDAanvang;

  @Column(name = "onderz_aand_geg")
  private String onderzAandGeg;

  @Column(name = "deelres_d_aanvang")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date deelresDAanvang;

  @Column(name = "deelres_aand_geg")
  private String deelresAandGeg;

  @Column(name = "oversl_gedeg_onderzoek")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean onderzGedegOnderzoek;

  @Column(name = "oversl_reden")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean overslReden;

  @Column(name = "oversl_toel")
  private String overslToel;

  @Column(name = "fase1_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date fase1dIn;

  @Column(name = "fase1_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date fase1dEnd;

  @Column(name = "fase1_reactie")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean fase1Reactie;

  @Column(name = "fase1_toel")
  private String fase1Toel;

  @Column(name = "fase1_vervolg")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean fase1Vervolg;

  @Column(name = "fase2_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date fase2dIn;

  @Column(name = "fase2_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date fase2dEnd;

  @Column(name = "fase2_onderz_gewenst")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean fase2OnderzGewenst;

  @Column(name = "fase2_d_uitv_onderzoek")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date fase2dUitvOnderzoek;

  @Column(name = "fase2_toel")
  private String fase2toel;

  @Column(name = "res_onderz_betrok")
  private BigDecimal resOnderzBetrok;

  @Column(name = "res_onderz_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date resOnderzdEnd;

  @Column(name = "res_onderz_nogmaals")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean resOnderzNogmaals;

  @Column(name = "res_toel")
  private String resToel;

  @Column(name = "res_adres")
  private String resAdres;

  @Column(name = "res_adres_gelijk")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean resAdresGelijk;

  @Column(name = "res_hnr")
  @Convert(converter = BigDecimalStringConverter.class)
  private String resHnr;

  @Column(name = "res_hnr_l")
  private String resHnrL;

  @Column(name = "res_hnr_t")
  private String resHnrT;

  @Column(name = "res_hnr_a")
  private String resHnrA;

  @Column(name = "res_pc")
  private String resPc;

  @Column(name = "res_plaats")
  private String resPlaats;

  @Column(name = "res_c_gem")
  private BigDecimal resCGem;

  @Column(name = "res_buitenl1")
  private String resBuitenl1;

  @Column(name = "res_buitenl2")
  private String resBuitenl2;

  @Column(name = "res_buitenl3")
  private String resBuitenl3;

  @Column(name = "res_c_land")
  private BigDecimal resCLand;

  @Column(name = "res_aant_pers")
  private BigDecimal resAantPers;

  @Column(name = "aanschr_fase")
  private BigDecimal aanschrFase;

  @Column(name = "aanschr_fase1_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrFase1dIn;

  @Column(name = "aanschr_fase1_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrFase1dEnd;

  @Column(name = "aanschr_fase2_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrFase2dIn;

  @Column(name = "aanschr_fase2_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrFase2dEnd;

  @Column(name = "aanschr_extra_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrExtradIn;

  @Column(name = "aanschr_extra_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrExtradEnd;

  @Column(name = "aanschr_voorn_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrVoorndIn;

  @Column(name = "aanschr_voorn_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrVoorndEnd;

  @Column(name = "aanschr_besluit_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date aanschrbesluitdIn;

  @OneToMany(cascade = { CascadeType.REMOVE },
      mappedBy = "dossOnderz")
  private List<DossOnderzBron> dossOnderzBronnen;

  public DossOnderz() {
  }

  public Long getcDossOnderz() {
    return cDossOnderz;
  }

  public void setcDossOnderz(Long cDossOnderz) {
    this.cDossOnderz = cDossOnderz;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public BigDecimal getAanlBron() {
    return aanlBron;
  }

  public void setAanlBron(BigDecimal aanlBron) {
    this.aanlBron = aanlBron;
  }

  public String getAanlTmvNr() {
    return aanlTmvNr;
  }

  public void setAanlTmvNr(String aanlTmvNr) {
    this.aanlTmvNr = aanlTmvNr;
  }

  public String getAanlInst() {
    return aanlInst;
  }

  public void setAanlInst(String aanlInst) {
    this.aanlInst = aanlInst;
  }

  public String getAanlInstNaam() {
    return aanlInstNaam;
  }

  public void setAanlInstNaam(String aanlInstNaam) {
    this.aanlInstNaam = aanlInstNaam;
  }

  public String getAanlInstAanhef() {
    return aanlInstAanhef;
  }

  public void setAanlInstAanhef(String aanlInstTav) {
    this.aanlInstAanhef = aanlInstTav;
  }

  public String getAanlInstVoorl() {
    return aanlInstVoorl;
  }

  public void setAanlInstVoorl(String aanlInstVoorl) {
    this.aanlInstVoorl = aanlInstVoorl;
  }

  public String getAanlInstAdres() {
    return aanlInstAdres;
  }

  public void setAanlInstAdres(String aanlInstAdres) {
    this.aanlInstAdres = aanlInstAdres;
  }

  public String getAanlInstPc() {
    return aanlInstPc;
  }

  public void setAanlInstPc(String aanlInstPc) {
    this.aanlInstPc = aanlInstPc;
  }

  public String getAanlInstPlaats() {
    return aanlInstPlaats;
  }

  public void setAanlInstPlaats(String aanlInstPlaats) {
    this.aanlInstPlaats = aanlInstPlaats;
  }

  public String getAanlKenmerk() {
    return aanlKenmerk;
  }

  public void setAanlKenmerk(String aanlKenmerk) {
    this.aanlKenmerk = aanlKenmerk;
  }

  public String getAanlAfdeling() {
    return aanlAfdeling;
  }

  public void setAanlAfdeling(String aanlAfdeling) {
    this.aanlAfdeling = aanlAfdeling;
  }

  public Date getAanlDMeldOntv() {
    return aanlDMeldOntv;
  }

  public void setAanlDMeldOntv(Date aanlDMeldOntv) {
    this.aanlDMeldOntv = aanlDMeldOntv;
  }

  public BigDecimal getAanlAard() {
    return aanlAard;
  }

  public void setAanlAard(BigDecimal aanlAard) {
    this.aanlAard = aanlAard;
  }

  public String getAanlAardAnders() {
    return aanlAardAnders;
  }

  public void setAanlAardAnders(String aanlAardAnders) {
    this.aanlAardAnders = aanlAardAnders;
  }

  public BigDecimal getAanlVermoedAdres() {
    return aanlVermoedAdres;
  }

  public void setAanlVermoedAdres(BigDecimal aanlVermoedAdres) {
    this.aanlVermoedAdres = aanlVermoedAdres;
  }

  public String getAanlAdres() {
    return aanlAdres;
  }

  public void setAanlAdres(String aanlAdres) {
    this.aanlAdres = aanlAdres;
  }

  public String getAanlHnr() {
    return aanlHnr;
  }

  public void setAanlHnr(String aanlHnr) {
    this.aanlHnr = aanlHnr;
  }

  public String getAanlHnrL() {
    return aanlHnrL;
  }

  public void setAanlHnrL(String aanlHnrL) {
    this.aanlHnrL = aanlHnrL;
  }

  public String getAanlHnrT() {
    return aanlHnrT;
  }

  public void setAanlHnrT(String aanlHnrT) {
    this.aanlHnrT = aanlHnrT;
  }

  public String getAanlHnrA() {
    return aanlHnrA;
  }

  public void setAanlHnrA(String aanlHnrA) {
    this.aanlHnrA = aanlHnrA;
  }

  public String getAanlPc() {
    return aanlPc;
  }

  public void setAanlPc(String aanlPc) {
    this.aanlPc = aanlPc;
  }

  public String getAanlPlaats() {
    return aanlPlaats;
  }

  public void setAanlPlaats(String aanlPlaats) {
    this.aanlPlaats = aanlPlaats;
  }

  public BigDecimal getAanlCGem() {
    return aanlCGem;
  }

  public void setAanlCGem(BigDecimal aanlCGem) {
    this.aanlCGem = aanlCGem;
  }

  public String getAanlBuitenl1() {
    return aanlBuitenl1;
  }

  public void setAanlBuitenl1(String aanlBuitenl1) {
    this.aanlBuitenl1 = aanlBuitenl1;
  }

  public String getAanlBuitenl2() {
    return aanlBuitenl2;
  }

  public void setAanlBuitenl2(String aanlBuitenl2) {
    this.aanlBuitenl2 = aanlBuitenl2;
  }

  public String getAanlBuitenl3() {
    return aanlBuitenl3;
  }

  public void setAanlBuitenl3(String aanlBuitenl3) {
    this.aanlBuitenl3 = aanlBuitenl3;
  }

  public BigDecimal getAanlCLand() {
    return aanlCLand;
  }

  public void setAanlCLand(BigDecimal aanlCLand) {
    this.aanlCLand = aanlCLand;
  }

  public Date getBeoordDEndTerm() {
    return beoordDEndTerm;
  }

  public void setBeoordDEndTerm(Date beoordDEndTerm) {
    this.beoordDEndTerm = beoordDEndTerm;
  }

  public Boolean getBeoordBinnenTerm() {
    return beoordBinnenTerm;
  }

  public void setBeoordBinnenTerm(Boolean beoordBinnenTerm) {
    this.beoordBinnenTerm = beoordBinnenTerm;
  }

  public String getRedenTerm() {
    return redenTerm;
  }

  public void setRedenTerm(String redenTerm) {
    this.redenTerm = redenTerm;
  }

  public Date getOnderzDAanvang() {
    return onderzDAanvang;
  }

  public void setOnderzDAanvang(Date onderzDAanvang) {
    this.onderzDAanvang = onderzDAanvang;
  }

  public String getOnderzAandGeg() {
    return onderzAandGeg;
  }

  public void setOnderzAandGeg(String onderzAandGeg) {
    this.onderzAandGeg = onderzAandGeg;
  }

  public Boolean getOnderzGedegOnderzoek() {
    return onderzGedegOnderzoek;
  }

  public void setOnderzGedegOnderzoek(Boolean onderzGedegOnderzoek) {
    this.onderzGedegOnderzoek = onderzGedegOnderzoek;
  }

  public Boolean getOverslReden() {
    return overslReden;
  }

  public void setOverslReden(Boolean overslReden) {
    this.overslReden = overslReden;
  }

  public String getOverslToel() {
    return overslToel;
  }

  public void setOverslToel(String overslToel) {
    this.overslToel = overslToel;
  }

  public Date getFase1dIn() {
    return fase1dIn;
  }

  public void setFase1dIn(Date fase1dIn) {
    this.fase1dIn = fase1dIn;
  }

  public Date getFase1dEnd() {
    return fase1dEnd;
  }

  public void setFase1dEnd(Date fase1dEnd) {
    this.fase1dEnd = fase1dEnd;
  }

  public Boolean getFase1Reactie() {
    return fase1Reactie;
  }

  public void setFase1Reactie(Boolean fase1Reactie) {
    this.fase1Reactie = fase1Reactie;
  }

  public String getFase1Toel() {
    return fase1Toel;
  }

  public void setFase1Toel(String fase1Toel) {
    this.fase1Toel = fase1Toel;
  }

  public Boolean getFase1Vervolg() {
    return fase1Vervolg;
  }

  public void setFase1Vervolg(Boolean fase1Vervolg) {
    this.fase1Vervolg = fase1Vervolg;
  }

  public Date getFase2dIn() {
    return fase2dIn;
  }

  public void setFase2dIn(Date fase2dIn) {
    this.fase2dIn = fase2dIn;
  }

  public Date getFase2dEnd() {
    return fase2dEnd;
  }

  public void setFase2dEnd(Date fase2dEnd) {
    this.fase2dEnd = fase2dEnd;
  }

  public Boolean getFase2OnderzGewenst() {
    return fase2OnderzGewenst;
  }

  public void setFase2OnderzGewenst(Boolean fase2OnderzGewenst) {
    this.fase2OnderzGewenst = fase2OnderzGewenst;
  }

  public Date getFase2dUitvOnderzoek() {
    return fase2dUitvOnderzoek;
  }

  public void setFase2dUitvOnderzoek(Date fase2dUitvOnderzoek) {
    this.fase2dUitvOnderzoek = fase2dUitvOnderzoek;
  }

  public String getFase2toel() {
    return fase2toel;
  }

  public void setFase2toel(String fase2toel) {
    this.fase2toel = fase2toel;
  }

  public BigDecimal getResOnderzBetrok() {
    return resOnderzBetrok;
  }

  public void setResOnderzBetrok(BigDecimal resOnderzBetrok) {
    this.resOnderzBetrok = resOnderzBetrok;
  }

  public Date getResOnderzdEnd() {
    return resOnderzdEnd;
  }

  public void setResOnderzdEnd(Date resOnderzdEnd) {
    this.resOnderzdEnd = resOnderzdEnd;
  }

  public Boolean getResOnderzNogmaals() {
    return resOnderzNogmaals;
  }

  public void setResOnderzNogmaals(Boolean resOnderzNogmaals) {
    this.resOnderzNogmaals = resOnderzNogmaals;
  }

  public String getResToel() {
    return resToel;
  }

  public void setResToel(String resToel) {
    this.resToel = resToel;
  }

  public String getResAdres() {
    return resAdres;
  }

  public void setResAdres(String resAdres) {
    this.resAdres = resAdres;
  }

  public String getResHnr() {
    return resHnr;
  }

  public void setResHnr(String resHnr) {
    this.resHnr = resHnr;
  }

  public String getResHnrL() {
    return resHnrL;
  }

  public void setResHnrL(String resHnrL) {
    this.resHnrL = resHnrL;
  }

  public String getResHnrT() {
    return resHnrT;
  }

  public void setResHnrT(String resHnrT) {
    this.resHnrT = resHnrT;
  }

  public String getResHnrA() {
    return resHnrA;
  }

  public void setResHnrA(String resHnrA) {
    this.resHnrA = resHnrA;
  }

  public String getResPc() {
    return resPc;
  }

  public void setResPc(String resPc) {
    this.resPc = resPc;
  }

  public String getResPlaats() {
    return resPlaats;
  }

  public void setResPlaats(String resPlaats) {
    this.resPlaats = resPlaats;
  }

  public BigDecimal getResCGem() {
    return resCGem;
  }

  public void setResCGem(BigDecimal resCGem) {
    this.resCGem = resCGem;
  }

  public String getResBuitenl1() {
    return resBuitenl1;
  }

  public void setResBuitenl1(String resBuitenl1) {
    this.resBuitenl1 = resBuitenl1;
  }

  public String getResBuitenl2() {
    return resBuitenl2;
  }

  public void setResBuitenl2(String resBuitenl2) {
    this.resBuitenl2 = resBuitenl2;
  }

  public String getResBuitenl3() {
    return resBuitenl3;
  }

  public void setResBuitenl3(String resBuitenl3) {
    this.resBuitenl3 = resBuitenl3;
  }

  public BigDecimal getResCLand() {
    return resCLand;
  }

  public void setResCLand(BigDecimal resCLand) {
    this.resCLand = resCLand;
  }

  public BigDecimal getAanschrFase() {
    return aanschrFase;
  }

  public void setAanschrFase(BigDecimal aanschrFase) {
    this.aanschrFase = aanschrFase;
  }

  public Date getAanschrFase1dIn() {
    return aanschrFase1dIn;
  }

  public void setAanschrFase1dIn(Date aanschrFase1dIn) {
    this.aanschrFase1dIn = aanschrFase1dIn;
  }

  public Date getAanschrFase1dEnd() {
    return aanschrFase1dEnd;
  }

  public void setAanschrFase1dEnd(Date aanschrFase1dEnd) {
    this.aanschrFase1dEnd = aanschrFase1dEnd;
  }

  public Date getAanschrFase2dIn() {
    return aanschrFase2dIn;
  }

  public void setAanschrFase2dIn(Date aanschrFase2dIn) {
    this.aanschrFase2dIn = aanschrFase2dIn;
  }

  public Date getAanschrFase2dEnd() {
    return aanschrFase2dEnd;
  }

  public void setAanschrFase2dEnd(Date aanschrFase2dEnd) {
    this.aanschrFase2dEnd = aanschrFase2dEnd;
  }

  public Date getAanschrExtradIn() {
    return aanschrExtradIn;
  }

  public void setAanschrExtradIn(Date aanschrExtradIn) {
    this.aanschrExtradIn = aanschrExtradIn;
  }

  public Date getAanschrExtradEnd() {
    return aanschrExtradEnd;
  }

  public void setAanschrExtradEnd(Date aanschrExtradEnd) {
    this.aanschrExtradEnd = aanschrExtradEnd;
  }

  public Date getAanschrVoorndIn() {
    return aanschrVoorndIn;
  }

  public void setAanschrVoorndIn(Date aanschrVoorndIn) {
    this.aanschrVoorndIn = aanschrVoorndIn;
  }

  public Date getAanschrVoorndEnd() {
    return aanschrVoorndEnd;
  }

  public void setAanschrVoorndEnd(Date aanschrVoorndEnd) {
    this.aanschrVoorndEnd = aanschrVoorndEnd;
  }

  public Date getAanschrbesluitdIn() {
    return aanschrbesluitdIn;
  }

  public void setAanschrbesluitdIn(Date aanschrbesluitdIn) {
    this.aanschrbesluitdIn = aanschrbesluitdIn;
  }

  public List<DossOnderzBron> getDossOnderzBronnen() {
    return dossOnderzBronnen;
  }

  public void setDossOnderzBronnen(List<DossOnderzBron> dossOnderzBronnen) {
    this.dossOnderzBronnen = dossOnderzBronnen;
  }

  public String getAanlRelatie() {
    return aanlRelatie;
  }

  public void setAanlRelatie(String aanlRelatie) {
    this.aanlRelatie = aanlRelatie;
  }

  public Boolean getResAdresGelijk() {
    return resAdresGelijk;
  }

  public void setResAdresGelijk(Boolean resAdresGelijk) {
    this.resAdresGelijk = resAdresGelijk;
  }

  public BigDecimal getAanlAantPers() {
    return aanlAantPers;
  }

  public void setAanlAantPers(BigDecimal aanlAantPers) {
    this.aanlAantPers = aanlAantPers;
  }

  public BigDecimal getResAantPers() {
    return resAantPers;
  }

  public void setResAantPers(BigDecimal resAantPers) {
    this.resAantPers = resAantPers;
  }

  public Date getDeelresDAanvang() {
    return deelresDAanvang;
  }

  public void setDeelresDAanvang(Date deelresDAanvang) {
    this.deelresDAanvang = deelresDAanvang;
  }

  public String getDeelresAandGeg() {
    return deelresAandGeg;
  }

  public void setDeelresAandGeg(String deelresAandGeg) {
    this.deelresAandGeg = deelresAandGeg;
  }
}
