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

@Entity
@Table(name = "document_afn")
public class DocumentAfn extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_document_afn",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "document_afn",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_document_afn")
  @Column(name = "c_document_afn",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDocumentAfn;

  @Column(name = "document_afn")
  private String documentAfn;

  @Column(name = "tav_aanhef")
  private String tavAanhef;

  @Column(name = "tav_voorl")
  private String tavVoorl;

  @Column(name = "tav_naam")
  private String tavNaam;

  @Column(name = "adres")
  private String adres;

  @Column(name = "pc")
  private String pc;

  @Column(name = "plaats")
  private String plaats;

  @Column(name = "c_grondslag",
      precision = 131089)
  private BigDecimal cGrondslag;

  @Column(name = "verstrek",
      precision = 131089)
  private BigDecimal verstrek;

  @Column(name = "c_tk",
      precision = 131089)
  private BigDecimal cTk;

  @Column(name = "email")
  private String email;

  public DocumentAfn() {
  }

  public Long getCDocumentAfn() {
    return this.cDocumentAfn;
  }

  public void setCDocumentAfn(Long cDocumentAfn) {
    this.cDocumentAfn = cDocumentAfn;
  }

  public String getDocumentAfn() {
    return this.documentAfn;
  }

  public void setDocumentAfn(String documentAfn) {
    this.documentAfn = documentAfn;
  }

  public String getPc() {
    return pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public BigDecimal getcGrondslag() {
    return cGrondslag;
  }

  public void setcGrondslag(BigDecimal cGrondslag) {
    this.cGrondslag = cGrondslag;
  }

  public BigDecimal getcTk() {
    return cTk;
  }

  public void setcTk(BigDecimal cTk) {
    this.cTk = cTk;
  }

  public BigDecimal getVerstrek() {
    return verstrek;
  }

  public void setVerstrek(BigDecimal verstrek) {
    this.verstrek = verstrek;
  }

  public String getTavAanhef() {
    return tavAanhef;
  }

  public void setTavAanhef(String tavAanhef) {
    this.tavAanhef = tavAanhef;
  }

  public String getTavVoorl() {
    return tavVoorl;
  }

  public void setTavVoorl(String tavVoorl) {
    this.tavVoorl = tavVoorl;
  }

  public String getTavNaam() {
    return tavNaam;
  }

  public void setTavNaam(String tavNaam) {
    this.tavNaam = tavNaam;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
