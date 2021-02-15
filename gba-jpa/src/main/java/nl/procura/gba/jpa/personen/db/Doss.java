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
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "doss")
public class Doss extends BaseEntity implements DossPerEntity, DossNatEntity, EventLogEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss")
  @Column(name = "c_doss",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDoss;

  @Column(name = "d_aanvr",
      precision = 131089)
  private BigDecimal dAanvr;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column()
  private String dossiernummer;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "ind_verwerkt",
      precision = 131089)
  private BigDecimal indVerwerkt;

  @Column(name = "t_aanvr",
      precision = 131089)
  private BigDecimal tAanvr;

  @Column(name = "type_doss",
      precision = 131089)
  private BigDecimal typeDoss;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "commentaar")
  private String comment;

  @Column(name = "pagina",
      length = 100)
  private String pagina;

  @Column(name = "descr",
      length = 255)
  private String descr;

  @OneToMany(mappedBy = "doss")
  private List<DossAkte> dossAktes;

  @OneToMany(mappedBy = "doss",
      cascade = { CascadeType.REMOVE })
  @OrderBy("cDossPers ASC")
  private List<DossPer> dossPers;

  @OneToMany(mappedBy = "doss")
  private List<DossVereiste> dossVereistes;

  @OneToMany(mappedBy = "doss",
      cascade = { CascadeType.REMOVE })
  @OrderBy("cDossNatio ASC")
  private List<DossNatio> dossNats;

  @OneToMany(mappedBy = "doss",
      cascade = { CascadeType.REMOVE })
  @OrderBy("cDossDoc ASC")
  private List<DossDoc> dossDocs;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  public Doss() {
  }

  public void reset() {
    setPagina("reset");
  }

  public Long getCDoss() {
    return this.cDoss;
  }

  public void setCDoss(Long cDoss) {
    this.cDoss = cDoss;
  }

  public BigDecimal getDAanvr() {
    return this.dAanvr;
  }

  public void setDAanvr(BigDecimal dAanvr) {
    this.dAanvr = dAanvr;
  }

  public String getDossiernummer() {
    return this.dossiernummer;
  }

  public void setDossiernummer(String dossiernummer) {
    this.dossiernummer = dossiernummer;
  }

  public BigDecimal getIndVerwerkt() {
    return this.indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public BigDecimal getTAanvr() {
    return this.tAanvr;
  }

  public void setTAanvr(BigDecimal tAanvr) {
    this.tAanvr = tAanvr;
  }

  public BigDecimal getTypeDoss() {
    return this.typeDoss;
  }

  public void setTypeDoss(BigDecimal typeDoss) {
    this.typeDoss = typeDoss;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public List<DossAkte> getDossAktes() {
    return this.dossAktes;
  }

  public void setDossAktes(List<DossAkte> dossAktes) {
    this.dossAktes = dossAktes;
  }

  public List<DossDoc> getDossDocs() {
    return this.dossDocs;
  }

  public void setDossDocs(List<DossDoc> dossDocs) {
    this.dossDocs = dossDocs;
  }

  public String getLeverancier() {
    return leverancier;
  }

  public void setLeverancier(String leverancier) {
    this.leverancier = leverancier;
  }

  public String getBron() {
    return bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public List<DossPer> getDossPers() {
    return dossPers;
  }

  public void setDossPers(List<DossPer> dossPers) {
    this.dossPers = dossPers;
  }

  @Override
  public void setParentDossPer(DossPer dossPer) {

  }

  @Override
  public List<DossNatio> getDossNats() {
    return dossNats;
  }

  public void setDossNats(List<DossNatio> dossNats) {
    this.dossNats = dossNats;
  }

  public List<DossVereiste> getDossVereistes() {
    return dossVereistes;
  }

  public void setDossVereistes(List<DossVereiste> dossVereistes) {
    this.dossVereistes = dossVereistes;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public BigDecimal getDIn() {
    return dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getPagina() {
    return pagina;
  }

  public void setPagina(String pagina) {
    this.pagina = pagina;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Override
  public EventObjectType getObjectType() {
    return EventObjectType.ZAAK;
  }

  @Override
  public String getObjectId() {
    return zaakId;
  }
}
