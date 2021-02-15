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

import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "printoptie")
public class Printoptie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_printoptie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "printoptie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_printoptie")
  @Column(name = "c_printoptie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cPrintoptie;

  @Column(length = 2147483647)
  private String cmd;

  @Column()
  private String kleur;

  @Column(length = 2147483647)
  private String locatie;

  @Column()
  private String media;

  @Column(length = 2147483647)
  private String oms;

  @Column()
  private String orientatie;

  @Column()
  private String printoptie;

  @Column()
  private String zijde;

  @Column(name = "bsm_id")
  private String bsmId;

  @Column(name = "mo_berichttype")
  private String moBerichttype;

  @Column(name = "type")
  private String type;

  @ManyToMany
  @BatchFetch(BatchFetchType.IN)
  @JoinTable(name = "printoptie_document",
      joinColumns = { @JoinColumn(name = "c_printoptie",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_document",
          nullable = false) })
  private List<Document> documents;

  @ManyToMany
  @BatchFetch(BatchFetchType.IN)
  @JoinTable(name = "printoptie_location",
      joinColumns = { @JoinColumn(name = "c_printoptie",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_location",
          nullable = false) })
  private List<Location> locations;

  public Printoptie() {
  }

  public Long getCPrintoptie() {
    return this.cPrintoptie;
  }

  public void setCPrintoptie(Long cPrintoptie) {
    this.cPrintoptie = cPrintoptie;
  }

  public String getCmd() {
    return this.cmd;
  }

  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  public String getKleur() {
    return this.kleur;
  }

  public void setKleur(String kleur) {
    this.kleur = kleur;
  }

  public String getLocatie() {
    return this.locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getMedia() {
    return this.media;
  }

  public void setMedia(String media) {
    this.media = media;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getOrientatie() {
    return this.orientatie;
  }

  public void setOrientatie(String orientatie) {
    this.orientatie = orientatie;
  }

  public String getPrintoptie() {
    return this.printoptie;
  }

  public void setPrintoptie(String printoptie) {
    this.printoptie = printoptie;
  }

  public String getZijde() {
    return this.zijde;
  }

  public void setZijde(String zijde) {
    this.zijde = zijde;
  }

  public List<Document> getDocuments() {
    return this.documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public List<Location> getLocations() {
    return this.locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  public String getBsmId() {
    return bsmId;
  }

  public void setBsmId(String bsmId) {
    this.bsmId = bsmId;
  }

  public String getMoBerichttype() {
    return moBerichttype;
  }

  public void setMoBerichttype(String moBerichttype) {
    this.moBerichttype = moBerichttype;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
