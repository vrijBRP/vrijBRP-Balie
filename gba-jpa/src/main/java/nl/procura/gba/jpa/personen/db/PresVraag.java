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
@Table(name = "presentievraag")
@NamedQuery(name = "PresVraag.findByZaakId",
    query = "select p from PresVraag p where p.cPresentievraag > 0 and p.zaakId = :zaakid order by p.cPresentievraag")
public class PresVraag extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_presentievraag",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "presentievraag",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_presentievraag")
  @Column(name = "c_presentievraag",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cPresentievraag;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "vraag")
  private String vraag;

  @Column(name = "antwoord")
  private String antwoord;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @Column(name = "versie")
  private String versie;

  @Column(name = "toelichting")
  private String toelichting;

  @ManyToMany(mappedBy = "presentievragen")
  @BatchFetch(BatchFetchType.IN)
  private List<DossPer> dossPers;

  public PresVraag() {
  }

  public Long getcPresentievraag() {
    return cPresentievraag;
  }

  public void setcPresentievraag(Long cPresentievraag) {
    this.cPresentievraag = cPresentievraag;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal gettIn() {
    return tIn;
  }

  public void settIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
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

  public String getVersie() {
    return versie;
  }

  public void setVersie(String versie) {
    this.versie = versie;
  }

  public String getVraag() {
    return vraag;
  }

  public void setVraag(String vraag) {
    this.vraag = vraag;
  }

  public String getAntwoord() {
    return antwoord;
  }

  public void setAntwoord(String antwoord) {
    this.antwoord = antwoord;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public List<DossPer> getDossPers() {
    return dossPers;
  }

  public void setDossPers(List<DossPer> dossPers) {
    this.dossPers = dossPers;
  }
}
