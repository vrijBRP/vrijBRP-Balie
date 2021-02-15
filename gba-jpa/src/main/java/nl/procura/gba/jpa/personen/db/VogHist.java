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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vog_hist")
public class VogHist extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private VogHistPK id;

  @Column(name = "d_geb_a",
      precision = 131089)
  private BigDecimal dGebA;

  @Column(name = "gesl_a")
  private String geslA;

  @Column(name = "l_geb_a",
      precision = 131089)
  private BigDecimal lGebA;

  @Column(name = "bsn_a",
      precision = 131089)
  private BigDecimal bsnA;

  @Column(name = "naam_a")
  private String naamA;

  @Column(name = "p_geb_a")
  private String pGebA;

  @Column(name = "voorn_a")
  private String voornA;

  @Column(name = "voorv_a")
  private String voorvA;

  public VogHist() {
  }

  @Override
  public VogHistPK getId() {
    return this.id;
  }

  public void setId(VogHistPK id) {
    this.id = id;
  }

  public BigDecimal getDGebA() {
    return this.dGebA;
  }

  public void setDGebA(BigDecimal dGebA) {
    this.dGebA = dGebA;
  }

  public String getGeslA() {
    return this.geslA;
  }

  public void setGeslA(String geslA) {
    this.geslA = geslA;
  }

  public BigDecimal getLGebA() {
    return this.lGebA;
  }

  public void setLGebA(BigDecimal lGebA) {
    this.lGebA = lGebA;
  }

  public String getNaamA() {
    return this.naamA;
  }

  public void setNaamA(String naamA) {
    this.naamA = naamA;
  }

  public String getPGebA() {
    return this.pGebA;
  }

  public void setPGebA(String pGebA) {
    this.pGebA = pGebA;
  }

  public String getVoornA() {
    return this.voornA;
  }

  public void setVoornA(String voornA) {
    this.voornA = voornA;
  }

  public String getVoorvA() {
    return this.voorvA;
  }

  public void setVoorvA(String voorvA) {
    this.voorvA = voorvA;
  }

  public BigDecimal getBsnA() {
    return bsnA;
  }

  public void setBsnA(BigDecimal bsnA) {
    this.bsnA = bsnA;
  }

}
