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
@Table(name = "doss_vereiste")
public class DossVereiste extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private DossVereistePK id;

  @Column(length = 2147483647)
  private String oms;

  @Column(precision = 131089)
  private BigDecimal overrule;

  @Column()
  private String voldaan;

  @Column()
  private String naam;

  @Column(name = "doss_vereiste")
  private String dossVereiste;

  @ManyToOne
  @JoinColumn(name = "c_doss",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  public DossVereiste() {
  }

  @Override
  public DossVereistePK getId() {
    return this.id;
  }

  public void setId(DossVereistePK id) {
    this.id = id;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public BigDecimal getOverrule() {
    return this.overrule;
  }

  public void setOverrule(BigDecimal overrule) {
    this.overrule = overrule;
  }

  public String getVoldaan() {
    return this.voldaan;
  }

  public void setVoldaan(String voldaan) {
    this.voldaan = voldaan;
  }

  public Doss getDoss() {
    return this.doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getDossVereiste() {
    return dossVereiste;
  }

  public void setDossVereiste(String dossVereiste) {
    this.dossVereiste = dossVereiste;
  }

}
