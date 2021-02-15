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
@Table(name = "pl_mut")
@NamedQuery(name = "PlMut.findByBsn", query = "select p from PlMut p where p.bsn = :bsn order by p.cPlMut")
public class PlMut extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_pl_mut",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "pl_mut",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_pl_mut")
  @Column(name = "c_pl_mut",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cPlMut;

  @Column(name = "d_in",
      nullable = false,
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      nullable = false,
      precision = 131089)
  private BigDecimal tIn;

  @Column(name = "anr",
      nullable = false,
      precision = 131089)
  private BigDecimal anr;

  @Column(name = "bsn",
      nullable = false,
      precision = 131089)
  private BigDecimal bsn;

  @Column(name = "cat",
      nullable = false,
      precision = 131089)
  private BigDecimal cat;

  @Column(name = "set",
      nullable = false,
      precision = 131089)
  private BigDecimal set;

  @Column(name = "explanation",
      nullable = false)
  private String explanation;

  @Column(name = "descr_cat",
      nullable = false)
  private String descrCat;

  @Column(name = "descr_action",
      nullable = false)
  private String descrAction;

  @Column(name = "descr_set",
      nullable = false)
  private String descrSet;

  @Column(name = "ind_verwerkt",
      nullable = false)
  private BigDecimal indVerwerkt;

  @Column(name = "zaak_id",
      nullable = false)
  private String zaakId;

  @Column(name = "action",
      nullable = false)
  private BigDecimal action;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_location")
  private Location location;

  @Column(name = "descr_rec",
      nullable = false)
  private String descrRec;

  @OneToMany(mappedBy = "plMut")
  private List<PlMutRec> plMutRecs;

  public PlMut() {
  }

  public Long getCPlMut() {
    return cPlMut;
  }

  public void setCPlMut(Long cPlMut) {
    this.cPlMut = cPlMut;
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

  public BigDecimal getAnr() {
    return anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public BigDecimal getBsn() {
    return bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getCat() {
    return cat;
  }

  public void setCat(BigDecimal cat) {
    this.cat = cat;
  }

  public BigDecimal getSet() {
    return set;
  }

  public void setSet(BigDecimal set) {
    this.set = set;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public String getDescrCat() {
    return descrCat;
  }

  public void setDescrCat(String descrCat) {
    this.descrCat = descrCat;
  }

  public String getDescrAction() {
    return descrAction;
  }

  public void setDescrAction(String descrAction) {
    this.descrAction = descrAction;
  }

  public String getDescrSet() {
    return descrSet;
  }

  public void setDescrSet(String descrSet) {
    this.descrSet = descrSet;
  }

  public BigDecimal getIndVerwerkt() {
    return indVerwerkt;
  }

  public void setIndVerwerkt(BigDecimal indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public BigDecimal getAction() {
    return action;
  }

  public void setAction(BigDecimal action) {
    this.action = action;
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

  public String getDescrRec() {
    return descrRec;
  }

  public void setDescrRec(String descrRec) {
    this.descrRec = descrRec;
  }

  public List<PlMutRec> getPlMutRecs() {
    return plMutRecs;
  }

  public void setPlMutRecs(List<PlMutRec> plMutRecs) {
    this.plMutRecs = plMutRecs;
  }
}
