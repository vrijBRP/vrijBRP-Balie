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
@Table(name = "terugmelding")
public class Terugmelding extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_terugmelding",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "terugmelding",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_terugmelding")
  @Column(name = "c_terugmelding",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cTerugmelding;

  @Column(nullable = false)
  private String anr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_afh")
  private Usr usrAfh;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_toev")
  private Usr usrToev;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_verantw")
  private Usr usrVerantw;

  @Column(name = "d_end",
      nullable = false,
      precision = 131089)
  private BigDecimal dEnd;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "d_in",
      nullable = false,
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_rap",
      nullable = false,
      precision = 131089)
  private BigDecimal dRap;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(length = 2147483647)
  private String resultaat;

  @Column(nullable = false,
      length = 20)
  private String snr;

  @Column(name = "t_end",
      nullable = false,
      precision = 131089)
  private BigDecimal tEnd;

  @Column(name = "t_in",
      nullable = false,
      precision = 131089)
  private BigDecimal tIn;

  @Column(length = 2147483647)
  private String terugmelding;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @OneToMany(mappedBy = "terugmelding")
  private List<TerugmDetail> terugmDetails;

  @OneToMany(mappedBy = "terugmelding")
  @OrderBy("dIn DESC, tIn DESC")
  private List<TerugmReactie> terugmReacties;

  @OneToMany(mappedBy = "terugmelding")
  private List<TerugmTmvRel> terugmTmvRels;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  public Terugmelding() {
  }

  public Long getCTerugmelding() {
    return this.cTerugmelding;
  }

  public void setCTerugmelding(Long cTerugmelding) {
    this.cTerugmelding = cTerugmelding;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public BigDecimal getDEnd() {
    return this.dEnd;
  }

  public void setDEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getDRap() {
    return this.dRap;
  }

  public void setDRap(BigDecimal dRap) {
    this.dRap = dRap;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getResultaat() {
    return this.resultaat;
  }

  public void setResultaat(String resultaat) {
    this.resultaat = resultaat;
  }

  public String getSnr() {
    return this.snr;
  }

  public void setSnr(String snr) {
    this.snr = snr;
  }

  public BigDecimal getTEnd() {
    return this.tEnd;
  }

  public void setTEnd(BigDecimal tEnd) {
    this.tEnd = tEnd;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getTerugmelding() {
    return this.terugmelding;
  }

  public void setTerugmelding(String terugmelding) {
    this.terugmelding = terugmelding;
  }

  public List<TerugmDetail> getTerugmDetails() {
    return this.terugmDetails;
  }

  public void setTerugmDetails(List<TerugmDetail> terugmDetails) {
    this.terugmDetails = terugmDetails;
  }

  public List<TerugmReactie> getTerugmReacties() {
    return this.terugmReacties;
  }

  public void setTerugmReacties(List<TerugmReactie> terugmReacties) {
    this.terugmReacties = terugmReacties;
  }

  public List<TerugmTmvRel> getTerugmTmvRels() {
    return this.terugmTmvRels;
  }

  public void setTerugmTmvRels(List<TerugmTmvRel> terugmTmvRels) {
    this.terugmTmvRels = terugmTmvRels;
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

  public Usr getUsrToev() {
    return usrToev;
  }

  public void setUsrToev(Usr usrToev) {
    this.usrToev = usrToev;
  }

  public Usr getUsrAfh() {
    return usrAfh;
  }

  public void setUsrAfh(Usr usrAfh) {
    this.usrAfh = usrAfh;
  }

  public Usr getUsrVerantw() {
    return usrVerantw;
  }

  public void setUsrVerantw(Usr usrVerantw) {
    this.usrVerantw = usrVerantw;
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

}
