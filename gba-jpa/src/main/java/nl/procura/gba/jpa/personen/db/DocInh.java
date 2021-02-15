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
@Table(name = "doc_inh")
public class DocInh extends BaseEntity<DocInhPK> implements VersionEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private DocInhPK id;

  @Column(name = "aand_vi",
      length = 1)
  private String aandVi;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @Column(name = "d_inneming",
      unique = true,
      nullable = false,
      precision = 131089)
  private long dInneming;

  @Column(name = "doc_type")
  private String docType;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "ind_rijbewijs",
      precision = 131089)
  private BigDecimal indRijbewijs;

  @Column(name = "pv_nr")
  private String pvNr;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "pv_oms",
      length = 2147483647)
  private String pvOms;

  @Column(name = "t_inneming",
      precision = 131089)
  private BigDecimal tInneming;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @Column(name = "bsn",
      precision = 131089)
  private BigDecimal bsn;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "version_ts")
  private Long versionTs;

  public DocInh() {
  }

  @Override
  public DocInhPK getId() {
    return this.id;
  }

  public void setId(DocInhPK id) {
    this.id = id;
  }

  public String getAandVi() {
    return this.aandVi;
  }

  public void setAandVi(String aandVi) {
    this.aandVi = aandVi;
  }

  public String getDocType() {
    return this.docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getPvNr() {
    return this.pvNr;
  }

  public void setPvNr(String pvNr) {
    this.pvNr = pvNr;
  }

  public String getPvOms() {
    return this.pvOms;
  }

  public void setPvOms(String pvOms) {
    this.pvOms = pvOms;
  }

  public BigDecimal getTInneming() {
    return this.tInneming;
  }

  public void setTInneming(BigDecimal tInneming) {
    this.tInneming = tInneming;
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

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public String getZaakId() {
    return zaakId;
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

  public long getDInneming() {
    return this.dInneming;
  }

  public void setDInneming(long dInneming) {
    this.dInneming = dInneming;
  }

  public BigDecimal getIndRijbewijs() {
    return indRijbewijs;
  }

  public void setIndRijbewijs(BigDecimal indRijbewijs) {
    this.indRijbewijs = indRijbewijs;
  }

  @Override
  public Long getVersionTs() {
    return versionTs;
  }

  @Override
  public void setVersionTs(Long versionTs) {
    this.versionTs = versionTs;
  }
}
