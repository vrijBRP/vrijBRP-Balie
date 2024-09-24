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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "inbox")
@NamedQuery(name = "Inbox.findAll",
    query = "SELECT i FROM Inbox i")
public class Inbox extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_inbox",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "inbox",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_inbox")
  @Column(name = "c_inbox",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cInbox;

  @Column()
  private String bestandsnaam;

  @Column(length = 4000)
  private String omschrijving;

  @Column(name = "d_invoer",
      precision = 131089)
  private BigDecimal dInvoer;

  @Column(name = "t_invoer",
      precision = 131089)
  private BigDecimal tInvoer;

  @Column(name = "d_ingang",
      precision = 131089)
  private BigDecimal dIngang;

  @Column(name = "nieuw")
  private boolean nieuw = true;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "zaak_id_extern")
  private String zaakIdExtern;

  @Column(name = "verwerk_id")
  private String verwerkId;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @OneToMany(mappedBy = "inbox")
  private List<InboxBestand> inboxBestands;

  public Inbox() {
  }

  public Long getCInbox() {
    return this.cInbox;
  }

  public void setCInbox(Long cInbox) {
    this.cInbox = cInbox;
  }

  public String getBestandsnaam() {
    return this.bestandsnaam;
  }

  public void setBestandsnaam(String bestandsnaam) {
    this.bestandsnaam = bestandsnaam;
  }

  public String getOmschrijving() {
    return this.omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getBron() {
    return this.bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
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

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public List<InboxBestand> getInboxBestands() {
    return this.inboxBestands;
  }

  public void setInboxBestands(List<InboxBestand> inboxBestands) {
    this.inboxBestands = inboxBestands;
  }

  public InboxBestand addInboxBestand(InboxBestand inboxBestand) {
    getInboxBestands().add(inboxBestand);
    inboxBestand.setInbox(this);
    return inboxBestand;
  }

  public InboxBestand removeInboxBestand(InboxBestand inboxBestand) {
    getInboxBestands().remove(inboxBestand);
    inboxBestand.setInbox(null);
    return inboxBestand;
  }

  public boolean isNieuw() {
    return nieuw;
  }

  public void setNieuw(boolean nieuw) {
    this.nieuw = nieuw;
  }

  public BigDecimal getdInvoer() {
    return dInvoer;
  }

  public void setdInvoer(BigDecimal dInvoer) {
    this.dInvoer = dInvoer;
  }

  public BigDecimal gettInvoer() {
    return tInvoer;
  }

  public void settInvoer(BigDecimal tInvoer) {
    this.tInvoer = tInvoer;
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

  public String getZaakIdExtern() {
    return zaakIdExtern;
  }

  public void setZaakIdExtern(String zaakIdExtern) {
    this.zaakIdExtern = zaakIdExtern;
  }

  public String getVerwerkId() {
    return verwerkId;
  }

  public void setVerwerkId(String verwerkId) {
    this.verwerkId = verwerkId;
  }

  public BigDecimal getdIngang() {
    return dIngang;
  }

  public void setdIngang(BigDecimal dIngang) {
    this.dIngang = dIngang;
  }
}
