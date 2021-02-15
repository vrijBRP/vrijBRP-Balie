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

@Entity
@Table(name = "doss_erk")
public class DossErk extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_erk",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossErk;

  @Column(name = "afstamming_stap",
      precision = 131089)
  private BigDecimal afstammingStap;

  @Column(name = "c_land_afstam_recht",
      precision = 131089)
  private BigDecimal cLandAfstamRecht;

  @Column(name = "keuze_naam_gesl")
  private String keuzeNaamGesl;

  @Column(name = "keuze_naam_voorv")
  private String keuzeNaamVoorv;

  @Column(name = "keuze_naam_tp")
  private String keuzeNaamTp;

  @Column(name = "b_naamskeuze",
      precision = 131089)
  private BigDecimal bNaamskeuze;

  @Column(name = "b_eerste_kind",
      precision = 131089)
  private BigDecimal bEersteKind;

  @Column(name = "t_naamskeuze",
      precision = 131089)
  private BigDecimal tNaamskeuze;

  @Column(name = "b_geboorte",
      precision = 131089)
  private BigDecimal bGeboorte;

  @Column(name = "c_land_naam_recht",
      precision = 131089)
  private BigDecimal cLandNaamRecht;

  @Column(name = "toestemming_stap",
      precision = 131089)
  private BigDecimal toestemmingStap;

  @Column(name = "toest_id")
  private String toestId;

  @Column(name = "rechtbank")
  private String rechtbank;

  @Column(name = "c_land_toest_recht_moeder",
      precision = 131089)
  private BigDecimal cLandToestRechtMoeder;

  @Column(name = "c_land_toest_recht_kind",
      precision = 131089)
  private BigDecimal cLandToestRechtKind;

  @Column(name = "type_erkenning")
  private String typeErkenning;

  @Column(name = "c_gem_erk",
      precision = 131089)
  private BigDecimal cGemErk;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_erk",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @OneToMany(mappedBy = "dossErk")
  private List<DossGeb> dossGebs;

  @OneToMany(mappedBy = "dossErkGeb")
  private List<DossGeb> dossErkGebs;

  public DossErk() {
  }

  public Long getCDossErk() {
    return this.cDossErk;
  }

  public void setCDossErk(Long cDossErk) {
    this.cDossErk = cDossErk;
  }

  public BigDecimal getAfstammingStap() {
    return this.afstammingStap;
  }

  public void setAfstammingStap(BigDecimal afstammingStap) {
    this.afstammingStap = afstammingStap;
  }

  public String getKeuzeNaamVoorv() {
    return keuzeNaamVoorv;
  }

  public void setKeuzeNaamVoorv(String keuzevoorv) {
    this.keuzeNaamVoorv = keuzevoorv;
  }

  public String getKeuzeNaamGesl() {
    return this.keuzeNaamGesl;
  }

  public void setKeuzeNaamGesl(String keuzeNaamGesl) {
    this.keuzeNaamGesl = keuzeNaamGesl;
  }

  public BigDecimal getToestemmingStap() {
    return this.toestemmingStap;
  }

  public void setToestemmingStap(BigDecimal toestemmingStap) {
    this.toestemmingStap = toestemmingStap;
  }

  public Doss getDoss() {
    return this.doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public List<DossGeb> getDossGebs() {
    return this.dossGebs;
  }

  public void setDossGebs(List<DossGeb> dossGebs) {
    this.dossGebs = dossGebs;
  }

  public String getTypeErkenning() {
    return typeErkenning;
  }

  public void setTypeErkenning(String typeErkenning) {
    this.typeErkenning = typeErkenning;
  }

  public BigDecimal getcLandToestRechtMoeder() {
    return cLandToestRechtMoeder;
  }

  public void setcLandToestRechtMoeder(BigDecimal cLand) {
    this.cLandToestRechtMoeder = cLand;
  }

  public BigDecimal getcLandNaamRecht() {
    return cLandNaamRecht;
  }

  public void setcLandNaamRecht(BigDecimal cLand) {
    this.cLandNaamRecht = cLand;
  }

  public BigDecimal getcLandAfstamRecht() {
    return cLandAfstamRecht;
  }

  public void setcLandAfstamRecht(BigDecimal cLand) {
    this.cLandAfstamRecht = cLand;
  }

  public BigDecimal getbNaamskeuze() {
    return bNaamskeuze;
  }

  public void setbNaamskeuze(BigDecimal bNaamskeuze) {
    this.bNaamskeuze = bNaamskeuze;
  }

  public BigDecimal getbGeboorte() {
    return bGeboorte;
  }

  public void setbGeboorte(BigDecimal bGeboorte) {
    this.bGeboorte = bGeboorte;
  }

  public String getToestId() {
    return toestId;
  }

  public void setToestId(String toestId) {
    this.toestId = toestId;
  }

  public String getRechtbank() {
    return rechtbank;
  }

  public void setRechtbank(String rechtbank) {
    this.rechtbank = rechtbank;
  }

  public BigDecimal getcLandToestRechtKind() {
    return cLandToestRechtKind;
  }

  public void setcLandToestRechtKind(BigDecimal cLandToestRechtKind) {
    this.cLandToestRechtKind = cLandToestRechtKind;
  }

  public BigDecimal gettNaamskeuze() {
    return tNaamskeuze;
  }

  public void settNaamskeuze(BigDecimal tNaamskeuze) {
    this.tNaamskeuze = tNaamskeuze;
  }

  public BigDecimal getbEersteKind() {
    return bEersteKind;
  }

  public void setbEersteKind(BigDecimal bEersteKind) {
    this.bEersteKind = bEersteKind;
  }

  public BigDecimal getcGemErk() {
    return cGemErk;
  }

  public void setcGemErk(BigDecimal cGemErk) {
    this.cGemErk = cGemErk;
  }

  public String getKeuzeNaamTp() {
    return keuzeNaamTp;
  }

  public void setKeuzeNaamTp(String keuzeNaamTp) {
    this.keuzeNaamTp = keuzeNaamTp;
  }

  public List<DossGeb> getDossErkGebs() {
    return dossErkGebs;
  }

  public void setDossErkGebs(List<DossGeb> dossErkGebs) {
    this.dossErkGebs = dossErkGebs;
  }
}
