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
@Table(name = "correspondentie")
public class Correspondentie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_correspondentie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "correspondentie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_correspondentie")
  @Column(name = "c_correspondentie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cCorrespondentie;

  @Column(precision = 131089)
  private BigDecimal anr;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "route")
  private String route;

  @Column(precision = 131089)
  private BigDecimal correspondentie;

  @Column(name = "anders")
  private String anders;

  @Column(name = "toelichting")
  private String toelichting;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "type_afsl",
      length = 50)
  private String typeAfsl;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  public Correspondentie() {
  }

  public Long getCCorrespondentie() {
    return this.cCorrespondentie;
  }

  public void setCCorrespondentie(Long cCorrespondentie) {
    this.cCorrespondentie = cCorrespondentie;
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

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
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

  public BigDecimal getDIn() {
    return dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getTIn() {
    return tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getAnders() {
    return anders;
  }

  public void setAnders(String anders) {
    this.anders = anders;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public BigDecimal getCorrespondentie() {
    return correspondentie;
  }

  public void setCorrespondentie(BigDecimal correspondentie) {
    this.correspondentie = correspondentie;
  }

  public String getTypeAfsl() {
    return typeAfsl;
  }

  public void setTypeAfsl(String typeAfsl) {
    this.typeAfsl = typeAfsl;
  }
}
