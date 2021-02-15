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
@Table(name = "uitt_aanvr")
public class UittAanvr extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_uitt_aanvr",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "uitt_aanvr",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_uitt_aanvr")
  @Column(name = "c_uitt_aanvr",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cUittAanvr;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column()
  private String anr;

  @Column(name = "bsn",
      precision = 131089)
  private BigDecimal bsn;

  @Column(name = "rel_bsn",
      precision = 131089)
  private BigDecimal relBsn;

  @Column(name = "d_aanvr",
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "document_afn")
  private String documentAfn;

  @Column(name = "document_doel")
  private String documentDoel;

  @Column(name = "id_doc_nr")
  private String idDocNr;

  @Column(name = "id_doc_type")
  private String idDocType;

  @Column(name = "id_vragen",
      precision = 131089)
  private BigDecimal idVragen;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "rel_anr")
  private String relAnr;

  @Column(precision = 131089)
  private BigDecimal relatie;

  @Column(name = "t_aanvr",
      precision = 131089)
  private BigDecimal tAanvr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_document")
  private Document document;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_aanvr")
  private Usr usr;

  public UittAanvr() {
  }

  public Long getCUittAanvr() {
    return this.cUittAanvr;
  }

  public void setCUittAanvr(Long cUittAanvr) {
    this.cUittAanvr = cUittAanvr;
  }

  public String getAnr() {
    return this.anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
  }

  public String getDocumentAfn() {
    return this.documentAfn;
  }

  public void setDocumentAfn(String documentAfn) {
    this.documentAfn = documentAfn;
  }

  public String getDocumentDoel() {
    return this.documentDoel;
  }

  public void setDocumentDoel(String documentDoel) {
    this.documentDoel = documentDoel;
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

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getRelAnr() {
    return this.relAnr;
  }

  public void setRelAnr(String relAnr) {
    this.relAnr = relAnr;
  }

  public BigDecimal getRelatie() {
    return this.relatie;
  }

  public void setRelatie(BigDecimal relatie) {
    this.relatie = relatie;
  }

  public BigDecimal getTAanvr() {
    return this.tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
  }

  public Document getDocument() {
    return this.document;
  }

  public void setDocument(Document document) {
    this.document = document;
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

  public BigDecimal getRelBsn() {
    return relBsn;
  }

  public void setRelBsn(BigDecimal relBsn) {
    this.relBsn = relBsn;
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

}
