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
@Table(name = "indicatie")
public class Indicatie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_indicatie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "indicatie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_indicatie")
  @Column(name = "c_indicatie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cIndicatie;

  @Column(precision = 131089)
  private BigDecimal anr;

  @Column()
  private String bron;

  @Column(name = "actie_type",
      length = 1)
  private String actieType;

  @Column(length = 2147483647)
  private String inhoud;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column()
  private String leverancier;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @Column(name = "zaak_id")
  private String zaakId;

  @ManyToOne
  @JoinColumn(name = "c_aantekening_ind")
  private AantekeningInd aantekeningInd;

  public Indicatie() {
  }

  public Long getCIndicatie() {
    return this.cIndicatie;
  }

  public void setCIndicatie(Long cIndicatie) {
    this.cIndicatie = cIndicatie;
  }

  public BigDecimal getAnr() {
    return this.anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public String getBron() {
    return this.bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getLeverancier() {
    return this.leverancier;
  }

  public void setLeverancier(String leverancier) {
    this.leverancier = leverancier;
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

  public AantekeningInd getAantekeningInd() {
    return this.aantekeningInd;
  }

  public void setAantekeningInd(AantekeningInd aantekeningInd) {
    this.aantekeningInd = aantekeningInd;
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

  public String getInhoud() {
    return inhoud;
  }

  public void setInhoud(String inhoud) {
    this.inhoud = inhoud;
  }

  public String getActieType() {
    return actieType;
  }

  public void setActieType(String actieType) {
    this.actieType = actieType;
  }
}
