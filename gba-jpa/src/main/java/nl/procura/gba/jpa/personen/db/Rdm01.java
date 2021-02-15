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

import nl.procura.gba.jpa.personen.converters.BigDecimalBooleanConverter;

@Entity
@Table(name = "rdm01")
public class Rdm01 extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_rdm01",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rdm01",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rdm01")
  @Column(name = "rdm01_id",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long rdm01Id;

  @Column(name = "aanvr_nr")
  private String aanvrNr;

  @Column()
  private String anr;

  @Column(name = "c_raas",
      precision = 131089)
  private BigDecimal cRaas;

  @Column(name = "cl_i",
      length = 1)
  private String clI;

  @Column(name = "vermeld_tp")
  private BigDecimal vermeldTp;

  @Column(name = "vermeld_land")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private boolean vermeldLand;

  @Column(name = "cl_v",
      precision = 131089)
  private BigDecimal clV;

  @Column(name = "toest_aantal",
      precision = 2)
  private BigDecimal toestAantal;

  @Column(name = "cl_xib",
      precision = 131089)
  private BigDecimal clXib;

  @Column(name = "cl_xii")
  private String clXii;

  @Column(name = "d_aanvr",
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "d_afsl",
      precision = 131089)
  private BigDecimal dAfsl;

  @Column(name = "d_deliv",
      precision = 131089)
  private BigDecimal dDeliv;

  @Column(name = "d_geld_end",
      precision = 131089)
  private BigDecimal dGeldEnd;

  @Column(name = "d_vb_doc",
      precision = 131089)
  private BigDecimal dVbDoc;

  @Column(name = "d_verstrek",
      precision = 131089)
  private BigDecimal dVerstrek;

  @Column(name = "doc_nr_ks")
  private String docNrKs;

  @Column()
  private String email;

  @Column(name = "id_doc_nr")
  private String idDocNr;

  @Column(name = "id_doc_type")
  private String idDocType;

  @Column(name = "id_vaststelling")
  private String idVaststelling;

  @Column(name = "id_vragen",
      precision = 131089)
  private BigDecimal idVragen;

  @Column(name = "ind_gratis",
      precision = 131089)
  private BigDecimal indGratis;

  @Column(name = "ind_spoed",
      precision = 131089)
  private BigDecimal indSpoed;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(precision = 131089)
  private BigDecimal lengte;

  @Column(name = "termijn_geld",
      precision = 131089)
  private BigDecimal termijnGeld;

  @Column(name = "nr_nl_doc")
  private String nrNlDoc;

  @Column(name = "nr_vb_doc")
  private String nrVbDoc;

  @Column(name = "s_cl_iv")
  private String sClIv;

  @Column(name = "s_cl_xa")
  private String sClXa;

  @Column(name = "s_cl_xb")
  private String sClXb;

  @Column(name = "s_niet_aanw")
  private String sNietAanw;

  @Column(name = "stat_afsl",
      precision = 131089)
  private BigDecimal statAfsl;

  @Column(name = "stat_deliv",
      precision = 131089)
  private BigDecimal statDeliv;

  @Column(name = "t_aanvr",
      precision = 131089)
  private BigDecimal tAanvr;

  @Column(name = "t_afsl",
      precision = 131089)
  private BigDecimal tAfsl;

  @Column(name = "t_deliv",
      precision = 131089)
  private BigDecimal tDeliv;

  @Column(name = "telnr_mob")
  private String telnrMob;

  @Column(name = "telnr_thuis")
  private String telnrThuis;

  @Column(name = "telnr_werk")
  private String telnrWerk;

  @Column(name = "toest_derde_naam")
  private String toestDerdeNaam;

  @Column(name = "toest_curator_naam")
  private String toestCuratorNaam;

  @Column(name = "toest_ouder1_anr")
  private String toestOuder1Anr;

  @Column(name = "toest_ouder1_naam")
  private String toestOuder1Naam;

  @Column(name = "toest_ouder2_anr")
  private String toestOuder2Anr;

  @Column(name = "toest_ouder2_naam")
  private String toestOuder2Naam;

  @Column(name = "toest_ouder1")
  private BigDecimal toestOuder1;

  @Column(name = "toest_ouder2")
  private BigDecimal toestOuder2;

  @Column(name = "toest_derde")
  private BigDecimal toestDerde;

  @Column(name = "toest_curator")
  private BigDecimal toestCurator;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "zkarg_nl_doc")
  private String zkargNlDoc;

  @Column(name = "buitenl_doc",
      precision = 131089)
  private BigDecimal indBezitBuitenlDoc;

  @Column(name = "ind_signal",
      precision = 131089)
  private BigDecimal indSignal;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_afhaal_location")
  private Location afhaalLocation;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_aanvr")
  private Usr usr1;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_uitgifte")
  private Usr usr2;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "bsn",
      precision = 131089)
  private BigDecimal bsn;

  public Rdm01() {
  }

  public Rdm01(String nummer) {
    setAanvrNr(nummer);
  }

  public Long getRdm01Id() {
    return this.rdm01Id;
  }

  public void setRdm01Id(Long rdm01Id) {
    this.rdm01Id = rdm01Id;
  }

  public String getAanvrNr() {
    return this.aanvrNr;
  }

  public void setAanvrNr(String aanvrNr) {
    this.aanvrNr = aanvrNr;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public BigDecimal getCRaas() {
    return this.cRaas;
  }

  public void setCRaas(BigDecimal cRaas) {
    this.cRaas = cRaas;
  }

  public String getClI() {
    return this.clI;
  }

  public void setClI(String clI) {
    this.clI = clI;
  }

  public BigDecimal getVermeldTp() {
    return vermeldTp;
  }

  public void setVermeldTp(BigDecimal clTp) {
    this.vermeldTp = clTp;
  }

  public BigDecimal getClV() {
    return this.clV;
  }

  public void setClV(BigDecimal clV) {
    this.clV = clV;
  }

  public BigDecimal getClXib() {
    return this.clXib;
  }

  public void setClXib(BigDecimal clXib) {
    this.clXib = clXib;
  }

  public String getClXii() {
    return this.clXii;
  }

  public void setClXii(String clXii) {
    this.clXii = clXii;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
  }

  public BigDecimal getDAfsl() {
    return this.dAfsl;
  }

  public void setDAfsl(BigDecimal dAfsl) {
    this.dAfsl = dAfsl;
  }

  public BigDecimal getDDeliv() {
    return this.dDeliv;
  }

  public void setDDeliv(BigDecimal dDeliv) {
    this.dDeliv = dDeliv;
  }

  public BigDecimal getDGeldEnd() {
    return this.dGeldEnd;
  }

  public void setDGeldEnd(BigDecimal dGeldEnd) {
    this.dGeldEnd = dGeldEnd;
  }

  public BigDecimal getDVbDoc() {
    return this.dVbDoc;
  }

  public void setDVbDoc(BigDecimal dVbDoc) {
    this.dVbDoc = dVbDoc;
  }

  public BigDecimal getDVerstrek() {
    return this.dVerstrek;
  }

  public void setDVerstrek(BigDecimal dVerstrek) {
    this.dVerstrek = dVerstrek;
  }

  public String getDocNrKs() {
    return this.docNrKs;
  }

  public void setDocNrKs(String docNrKs) {
    this.docNrKs = docNrKs;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIdDocNr() {
    return this.idDocNr;
  }

  public void setIdDocNr(String idDocNr) {
    this.idDocNr = idDocNr;
  }

  public String getIdDocType() {
    return this.idDocType;
  }

  public void setIdDocType(String idDocType) {
    this.idDocType = idDocType;
  }

  public BigDecimal getIdVragen() {
    return this.idVragen;
  }

  public void setIdVragen(BigDecimal idVragen) {
    this.idVragen = idVragen;
  }

  public BigDecimal getIndGratis() {
    return this.indGratis;
  }

  public void setIndGratis(BigDecimal indGratis) {
    this.indGratis = indGratis;
  }

  public BigDecimal getIndSpoed() {
    return this.indSpoed;
  }

  public void setIndSpoed(BigDecimal indSpoed) {
    this.indSpoed = indSpoed;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public BigDecimal getLengte() {
    return this.lengte;
  }

  public void setLengte(BigDecimal lengte) {
    this.lengte = lengte;
  }

  public String getNrNlDoc() {
    return this.nrNlDoc;
  }

  public void setNrNlDoc(String nrNlDoc) {
    this.nrNlDoc = nrNlDoc;
  }

  public String getNrVbDoc() {
    return this.nrVbDoc;
  }

  public void setNrVbDoc(String nrVbDoc) {
    this.nrVbDoc = nrVbDoc;
  }

  public String getSClIv() {
    return this.sClIv;
  }

  public void setSClIv(String sClIv) {
    this.sClIv = sClIv;
  }

  public String getSClXa() {
    return this.sClXa;
  }

  public void setSClXa(String sClXa) {
    this.sClXa = sClXa;
  }

  public String getSClXb() {
    return this.sClXb;
  }

  public void setSClXb(String sClXb) {
    this.sClXb = sClXb;
  }

  public String getSNietAanw() {
    return this.sNietAanw;
  }

  public void setSNietAanw(String sNietAanw) {
    this.sNietAanw = sNietAanw;
  }

  public BigDecimal getStatAfsl() {
    return this.statAfsl;
  }

  public void setStatAfsl(BigDecimal statAfsl) {
    this.statAfsl = statAfsl;
  }

  public BigDecimal getStatDeliv() {
    return this.statDeliv;
  }

  public void setStatDeliv(BigDecimal statDeliv) {
    this.statDeliv = statDeliv;
  }

  public BigDecimal getTAanvr() {
    return this.tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
  }

  public BigDecimal getTAfsl() {
    return this.tAfsl;
  }

  public void setTAfsl(BigDecimal tAfsl) {
    this.tAfsl = tAfsl;
  }

  public BigDecimal getTDeliv() {
    return this.tDeliv;
  }

  public void setTDeliv(BigDecimal tDeliv) {
    this.tDeliv = tDeliv;
  }

  public String getTelnrMob() {
    return this.telnrMob;
  }

  public void setTelnrMob(String telnrMob) {
    this.telnrMob = telnrMob;
  }

  public String getTelnrThuis() {
    return this.telnrThuis;
  }

  public void setTelnrThuis(String telnrThuis) {
    this.telnrThuis = telnrThuis;
  }

  public String getTelnrWerk() {
    return this.telnrWerk;
  }

  public void setTelnrWerk(String telnrWerk) {
    this.telnrWerk = telnrWerk;
  }

  public String getToestDerdeNaam() {
    return this.toestDerdeNaam;
  }

  public void setToestDerdeNaam(String toestDerdeNaam) {
    this.toestDerdeNaam = toestDerdeNaam;
  }

  public String getToestOuder1Anr() {
    return this.toestOuder1Anr;
  }

  public void setToestOuder1Anr(String toestOuder1Anr) {
    this.toestOuder1Anr = toestOuder1Anr;
  }

  public String getToestOuder1Naam() {
    return this.toestOuder1Naam;
  }

  public void setToestOuder1Naam(String toestOuder1Naam) {
    this.toestOuder1Naam = toestOuder1Naam;
  }

  public String getToestOuder2Anr() {
    return this.toestOuder2Anr;
  }

  public void setToestOuder2Anr(String toestOuder2Anr) {
    this.toestOuder2Anr = toestOuder2Anr;
  }

  public String getToestOuder2Naam() {
    return this.toestOuder2Naam;
  }

  public void setToestOuder2Naam(String toestOuder2Naam) {
    this.toestOuder2Naam = toestOuder2Naam;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public String getZkargNlDoc() {
    return this.zkargNlDoc;
  }

  public void setZkargNlDoc(String zkargNlDoc) {
    this.zkargNlDoc = zkargNlDoc;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Usr getUsr1() {
    return this.usr1;
  }

  public void setUsr1(Usr usr1) {
    this.usr1 = usr1;
  }

  public Usr getUsr2() {
    return this.usr2;
  }

  public void setUsr2(Usr usr2) {
    this.usr2 = usr2;
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

  public String getToestCuratorNaam() {
    return toestCuratorNaam;
  }

  public void setToestCuratorNaam(String toestCuratorNaam) {
    this.toestCuratorNaam = toestCuratorNaam;
  }

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public String getIdVaststelling() {
    return idVaststelling;
  }

  public void setIdVaststelling(String idVaststelling) {
    this.idVaststelling = idVaststelling;
  }

  public BigDecimal getTermijnGeld() {
    return termijnGeld;
  }

  public void setTermijnGeld(BigDecimal termijnGeld) {
    this.termijnGeld = termijnGeld;
  }

  public Location getAfhaalLocation() {
    return afhaalLocation;
  }

  public void setAfhaalLocation(Location afhaalLocation) {
    this.afhaalLocation = afhaalLocation;
  }

  public BigDecimal getToestAantal() {
    return toestAantal;
  }

  public void setToestAantal(BigDecimal toestAantal) {
    this.toestAantal = toestAantal;
  }

  public BigDecimal getIndBezitBuitenlDoc() {
    return indBezitBuitenlDoc;
  }

  public void setIndBezitBuitenlDoc(BigDecimal indBezitBuitenlDoc) {
    this.indBezitBuitenlDoc = indBezitBuitenlDoc;
  }

  public boolean isVermeldLand() {
    return vermeldLand;
  }

  public void setVermeldLand(boolean vermeldLand) {
    this.vermeldLand = vermeldLand;
  }

  public BigDecimal getToestOuder1() {
    return toestOuder1;
  }

  public void setToestOuder1(BigDecimal toestOuder1Type) {
    this.toestOuder1 = toestOuder1Type;
  }

  public BigDecimal getToestOuder2() {
    return toestOuder2;
  }

  public void setToestOuder2(BigDecimal toestOuder2) {
    this.toestOuder2 = toestOuder2;
  }

  public BigDecimal getToestDerde() {
    return toestDerde;
  }

  public void setToestDerde(BigDecimal toestDerdeType) {
    this.toestDerde = toestDerdeType;
  }

  public BigDecimal getToestCurator() {
    return toestCurator;
  }

  public void setToestCurator(BigDecimal toestCuratorType) {
    this.toestCurator = toestCuratorType;
  }

  public BigDecimal getIndSignal() {
    return indSignal;
  }

  public void setIndSignal(BigDecimal indSignal) {
    this.indSignal = indSignal;
  }
}
