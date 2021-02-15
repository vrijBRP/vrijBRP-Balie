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
@Table(name = "nrd")
public class Nrd extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_nrd",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "nrd",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_nrd")
  @Column(name = "c_aanvr",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAanvr;

  @Column(name = "aanvr_nr",
      length = 2147483647)
  private String aanvrNr;

  @Column(length = 2147483647)
  private String anr;

  @Column(name = "c_naamgebruik",
      length = 1)
  private String cNaamgebruik;

  @Column(name = "c_raas",
      precision = 131089)
  private BigDecimal cRaas;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_uitgifte")
  private Usr usrUitgifte;

  @Column(name = "c_vbt",
      precision = 131089)
  private BigDecimal cVbt;

  @Column(name = "d_aanvr",
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "d_vertrek",
      precision = 131089)
  private BigDecimal dVertrek;

  @Column(name = "d_vestiging",
      precision = 131089)
  private BigDecimal dVestiging;

  @Column()
  private String email;

  @Column(name = "gba_best",
      length = 1)
  private String gbaBest;

  @Column(name = "id_doc_nr")
  private String idDocNr;

  @Column(name = "id_doc_type")
  private String idDocType;

  @Column(name = "id_vragen",
      precision = 131089)
  private BigDecimal idVragen;

  @Column(name = "ind_185dagen",
      length = 1)
  private String ind185dagen;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "l_vertrek",
      precision = 131089)
  private BigDecimal lVertrek;

  @Column(name = "l_vestiging",
      precision = 131089)
  private BigDecimal lVestiging;

  @Column(length = 2147483647)
  private String nationaliteiten;

  @Column(name = "pv_verlies")
  private String pvVerlies;

  @Column(name = "rbw_nr")
  private String rbwNr;

  @Column(name = "rbw_nr_verv",
      length = 2147483647)
  private String rbwNrVerv;

  @Column(name = "rdn_aanvr",
      precision = 131089)
  private BigDecimal rdnAanvr;

  @Column(name = "soort_id")
  private String soortId;

  @Column(name = "spoed_afh",
      length = 2147483647)
  private String spoedAfh;

  @Column(name = "srt_aanvr",
      precision = 131089)
  private BigDecimal srtAanvr;

  @Column(name = "t_aanvr",
      precision = 131089)
  private BigDecimal tAanvr;

  @Column(name = "telnr_mob")
  private String telnrMob;

  @Column(name = "telnr_thuis")
  private String telnrThuis;

  @Column(name = "telnr_werk")
  private String telnrWerk;

  @Column(length = 2147483647)
  private String x;

  @Column(name = "zaak_id")
  private String zaakId;

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
  private Usr usr;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "bsn",
      precision = 131089)
  private BigDecimal bsn;

  @OneToMany(mappedBy = "nrd")
  private List<NrdStatus> nrdStatuses;

  @Column(name = "vermeld_tp")
  private BigDecimal vermeldTp;

  public Nrd() {
  }

  public Nrd(String aanvraagnummer) {
    setAanvrNr(aanvraagnummer);
  }

  public Long getCAanvr() {
    return this.cAanvr;
  }

  public void setCAanvr(Long cAanvr) {
    this.cAanvr = cAanvr;
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

  public String getCNaamgebruik() {
    return this.cNaamgebruik;
  }

  public void setCNaamgebruik(String cNaamgebruik) {
    this.cNaamgebruik = cNaamgebruik;
  }

  public BigDecimal getCRaas() {
    return this.cRaas;
  }

  public void setCRaas(BigDecimal cRaas) {
    this.cRaas = cRaas;
  }

  public BigDecimal getCVbt() {
    return this.cVbt;
  }

  public void setCVbt(BigDecimal cVbt) {
    this.cVbt = cVbt;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
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

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGbaBest() {
    return this.gbaBest;
  }

  public void setGbaBest(String gbaBest) {
    this.gbaBest = gbaBest;
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

  public String getInd185dagen() {
    return this.ind185dagen;
  }

  public void setInd185dagen(String ind185dagen) {
    this.ind185dagen = ind185dagen;
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

  public String getNationaliteiten() {
    return this.nationaliteiten;
  }

  public void setNationaliteiten(String nationaliteiten) {
    this.nationaliteiten = nationaliteiten;
  }

  public String getPvVerlies() {
    return this.pvVerlies;
  }

  public void setPvVerlies(String pvVerlies) {
    this.pvVerlies = pvVerlies;
  }

  public String getRbwNr() {
    return this.rbwNr;
  }

  public void setRbwNr(String rbwNr) {
    this.rbwNr = rbwNr;
  }

  public String getRbwNrVerv() {
    return this.rbwNrVerv;
  }

  public void setRbwNrVerv(String rbwNrVerv) {
    this.rbwNrVerv = rbwNrVerv;
  }

  public BigDecimal getRdnAanvr() {
    return this.rdnAanvr;
  }

  public void setRdnAanvr(BigDecimal rdnAanvr) {
    this.rdnAanvr = rdnAanvr;
  }

  public String getSoortId() {
    return this.soortId;
  }

  public void setSoortId(String soortId) {
    this.soortId = soortId;
  }

  public String getSpoedAfh() {
    return this.spoedAfh;
  }

  public void setSpoedAfh(String spoedAfh) {
    this.spoedAfh = spoedAfh;
  }

  public BigDecimal getSrtAanvr() {
    return this.srtAanvr;
  }

  public void setSrtAanvr(BigDecimal srtAanvr) {
    this.srtAanvr = srtAanvr;
  }

  public BigDecimal getTAanvr() {
    return this.tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
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

  public String getX() {
    return this.x;
  }

  public void setX(String x) {
    this.x = x;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public List<NrdStatus> getNrdStatuses() {
    return this.nrdStatuses;
  }

  public void setNrdStatuses(List<NrdStatus> nrdStatuses) {
    this.nrdStatuses = nrdStatuses;
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

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public Usr getUsrUitgifte() {
    return usrUitgifte;
  }

  public void setUsrUitgifte(Usr usrUitgifte) {
    this.usrUitgifte = usrUitgifte;
  }

  public Location getAfhaalLocation() {
    return afhaalLocation;
  }

  public void setAfhaalLocation(Location afhaalLocation) {
    this.afhaalLocation = afhaalLocation;
  }

  public BigDecimal getVermeldTp() {
    return vermeldTp;
  }

  public void setVermeldTp(BigDecimal vermeldTp) {
    this.vermeldTp = vermeldTp;
  }
}
