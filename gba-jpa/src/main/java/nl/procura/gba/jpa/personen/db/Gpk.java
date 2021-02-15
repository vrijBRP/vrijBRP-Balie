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
@Table(name = "gpk")
public class Gpk extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_gpk",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "gpk",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_gpk")
  @Column(name = "c_gpk",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGpk;

  @Column()
  private String afgever;

  @Column(precision = 131089)
  private BigDecimal anr;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "d_aanvr",
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "d_print",
      precision = 131089)
  private BigDecimal dPrint;

  @Column(name = "d_verval",
      precision = 131089)
  private BigDecimal dVerval;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column()
  private String kaarttype;

  @Column()
  private String nr;

  @Column(name = "t_aanvr",
      precision = 131089)
  private BigDecimal tAanvr;

  @Column(name = "zaak_id")
  private String zaakId;

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

  public Gpk() {
  }

  public Long getCGpk() {
    return this.cGpk;
  }

  public void setCGpk(Long cGpk) {
    this.cGpk = cGpk;
  }

  public String getAfgever() {
    return this.afgever;
  }

  public void setAfgever(String afgever) {
    this.afgever = afgever;
  }

  public BigDecimal getAnr() {
    return this.anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
  }

  public BigDecimal getDPrint() {
    return this.dPrint;
  }

  public void setDPrint(BigDecimal dPrint) {
    this.dPrint = dPrint;
  }

  public BigDecimal getDVerval() {
    return this.dVerval;
  }

  public void setDVerval(BigDecimal dVerval) {
    this.dVerval = dVerval;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getKaarttype() {
    return this.kaarttype;
  }

  public void setKaarttype(String kaarttype) {
    this.kaarttype = kaarttype;
  }

  public String getNr() {
    return this.nr;
  }

  public void setNr(String nr) {
    this.nr = nr;
  }

  public BigDecimal getTAanvr() {
    return this.tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
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

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}
