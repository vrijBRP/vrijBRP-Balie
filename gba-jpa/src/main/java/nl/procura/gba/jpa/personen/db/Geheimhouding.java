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
@Table(name = "geheimhouding")
public class Geheimhouding extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_geheimhouding",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "geheimhouding",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_geheimhouding")
  @Column(name = "c_geheimhouding",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGeheimhouding;

  @Column()
  private String anr;

  @Column(name = "anr_aangever")
  private String anrAangever;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "bsn_aangever",
      precision = 131089)
  private BigDecimal bsnAangever;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_wijz",
      precision = 131089)
  private BigDecimal dWijz;

  @Column(precision = 131089)
  private BigDecimal geheimhouding;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

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

  public Geheimhouding() {
  }

  public Long getCGeheimhouding() {
    return this.cGeheimhouding;
  }

  public void setCGeheimhouding(Long cGeheimhouding) {
    this.cGeheimhouding = cGeheimhouding;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public String getAnrAangever() {
    return this.anrAangever;
  }

  public void setAnrAangever(String anrAangever) {
    this.anrAangever = anrAangever;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getBsnAangever() {
    return this.bsnAangever;
  }

  public void setBsnAangever(BigDecimal bsnAangever) {
    this.bsnAangever = bsnAangever;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getGeheimhouding() {
    return this.geheimhouding;
  }

  public void setGeheimhouding(BigDecimal geheimhouding) {
    this.geheimhouding = geheimhouding;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
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

  public BigDecimal getDWijz() {
    return dWijz;
  }

  public void setDWijz(BigDecimal dWijz) {
    this.dWijz = dWijz;
  }

  public String getGoedkeuring() {
    return goedkeuring;
  }

  public void setGoedkeuring(String goedkeuring) {
    this.goedkeuring = goedkeuring;
  }

}
