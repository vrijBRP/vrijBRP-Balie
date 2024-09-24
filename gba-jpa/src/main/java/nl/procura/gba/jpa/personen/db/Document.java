/*
 * Copyright 2024 - 2025 Procura B.V.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "document")
public class Document extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_document",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "document",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_document")
  @Column(name = "c_document",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDocument;

  @Column(name = "all_allowed",
      precision = 131089)
  private BigDecimal allAllowed;

  @Column(name = "sb_allowed",
      precision = 131089)
  private BigDecimal sbAllowed;

  @Column(length = 2147483647)
  private String attributen;

  @Column()
  private String bestand;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  @Column()
  private String document;

  @Column(length = 2147483647)
  private String formats;

  @Column(length = 2147483647)
  private String omschrijving;

  @Column(length = 2147483647)
  private String path;

  @Column(precision = 131089)
  private BigDecimal prot;

  @Column(precision = 131089)
  private BigDecimal standaard;

  @Column(precision = 131089)
  private BigDecimal save;

  @Column(name = "document_type")
  private String type;

  @Column(name = "v_document",
      precision = 131089)
  private BigDecimal vDocument;

  @Column(name = "aantal")
  private int aantal = -1;

  @Column(name = "dms_naam")
  private String alias;

  @Column(name = "vertr")
  private int vertr = -1;

  @Column(name = "document_dms_type")
  private String documentDmsType;

  @ManyToMany
  @BatchFetch(BatchFetchType.IN)
  @JoinTable(name = "document_stempel",
      joinColumns = { @JoinColumn(name = "c_document",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_stempel",
          nullable = false) })
  @OrderBy("zIndex ASC")
  private List<Stempel> stempels;

  @ManyToMany
  @BatchFetch(BatchFetchType.IN)
  @JoinTable(name = "document_kenmerk",
      joinColumns = { @JoinColumn(name = "c_document",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_kenmerk",
          nullable = false) })
  private List<Kenmerk> kenmerks;

  @ManyToMany
  @JoinTable(name = "document_usr",
      joinColumns = { @JoinColumn(name = "c_document",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_usr",
          nullable = false) })
  private List<Usr> usrs;

  @OneToMany(mappedBy = "document")
  @BatchFetch(BatchFetchType.IN)
  private List<Kassa> kassas;

  @ManyToMany(mappedBy = "documents")
  @BatchFetch(BatchFetchType.IN)
  private List<Printoptie> printopties;

  @ManyToMany(mappedBy = "documents")
  @BatchFetch(BatchFetchType.IN)
  private List<Koppelenum> koppelenums;

  @OneToMany(mappedBy = "document")
  private List<UittAanvr> uittAanvrs;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_translation")
  private Translation translation;

  public Document() {
  }

  public Document(Long cDocument) {
    this();
    this.cDocument = cDocument;
  }

  public Long getCDocument() {
    return this.cDocument;
  }

  public void setCDocument(Long cDocument) {
    this.cDocument = cDocument;
  }

  public BigDecimal getAllAllowed() {
    return this.allAllowed;
  }

  public void setAllAllowed(BigDecimal allAllowed) {
    this.allAllowed = allAllowed;
  }

  public String getAttributen() {
    return this.attributen;
  }

  public void setAttributen(String attributen) {
    this.attributen = attributen;
  }

  public String getBestand() {
    return this.bestand;
  }

  public void setBestand(String bestand) {
    this.bestand = bestand;
  }

  public BigDecimal getDEnd() {
    return this.dEnd;
  }

  public void setDEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public String getDocument() {
    return this.document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public String getFormats() {
    return this.formats;
  }

  public void setFormats(String formats) {
    this.formats = formats;
  }

  public String getOmschrijving() {
    return this.omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public BigDecimal getProt() {
    return this.prot;
  }

  public void setProt(BigDecimal prot) {
    this.prot = prot;
  }

  public BigDecimal getSave() {
    return this.save;
  }

  public void setSave(BigDecimal save) {
    this.save = save;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDocumentDmsType() {
    return documentDmsType;
  }

  public void setDocumentDmsType(String documentDmsType) {
    this.documentDmsType = documentDmsType;
  }

  public BigDecimal getVDocument() {
    return this.vDocument;
  }

  public void setVDocument(BigDecimal vDocument) {
    this.vDocument = vDocument;
  }

  public List<Usr> getUsrs() {
    return this.usrs;
  }

  public void setUsrs(List<Usr> usrs) {
    this.usrs = usrs;
  }

  public List<Kassa> getKassas() {
    return this.kassas;
  }

  public void setKassas(List<Kassa> kassas) {
    this.kassas = kassas;
  }

  public List<Printoptie> getPrintopties() {
    return this.printopties;
  }

  public void setPrintopties(List<Printoptie> printopties) {
    this.printopties = printopties;
  }

  public List<UittAanvr> getUittAanvrs() {
    return this.uittAanvrs;
  }

  public void setUittAanvrs(List<UittAanvr> uittAanvrs) {
    this.uittAanvrs = uittAanvrs;
  }

  public BigDecimal getStandaard() {
    return standaard;
  }

  public void setStandaard(BigDecimal standaard) {
    this.standaard = standaard;
  }

  public List<Koppelenum> getKoppelenumen() {
    return koppelenums;
  }

  public void setKoppelenumen(List<Koppelenum> koppelenums) {
    this.koppelenums = koppelenums;
  }

  public int getAantal() {
    return aantal > 1 && aantal <= 5 ? aantal : 1;
  }

  public void setAantal(int aantal) {
    this.aantal = aantal;
  }

  public List<Stempel> getStempels() {
    return stempels;
  }

  public void setStempels(List<Stempel> stempels) {
    this.stempels = stempels;
  }

  public List<Kenmerk> getKenmerken() {
    return kenmerks;
  }

  public void setKenmerks(List<Kenmerk> kenmerks) {
    this.kenmerks = kenmerks;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public int getVertr() {
    return vertr;
  }

  public void setVertr(int vertr) {
    this.vertr = vertr;
  }

  public BigDecimal getSbAllowed() {
    return sbAllowed;
  }

  public void setSbAllowed(BigDecimal sbAllowed) {
    this.sbAllowed = sbAllowed;
  }

  public Translation getTranslation() {
    return translation;
  }

  public void setTranslation(Translation translation) {
    this.translation = translation;
  }
}
