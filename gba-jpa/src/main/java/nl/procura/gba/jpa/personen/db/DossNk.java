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
@Table(name = "doss_nk")
public class DossNk extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_nk",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossNk;

  @Column(name = "keuze_naam_gesl")
  private String keuzeNaamGesl;

  @Column(name = "keuze_naam_voorv")
  private String keuzeNaamVoorv;

  @Column(name = "keuze_naam_tp")
  private String keuzeNaamTp;

  @Column(name = "b_eerste_kind",
      precision = 131089)
  private BigDecimal eersteKind;

  @Column(name = "person_type",
      precision = 131089)
  private BigDecimal personType;

  @Column(name = "c_land_naam_recht",
      precision = 131089)
  private BigDecimal cLandNaamRecht;

  @Column(name = "type")
  private String type;

  @Column(name = "c_gem",
      precision = 131089)
  private BigDecimal cGemNk;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_nk",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @OneToMany(mappedBy = "dossNk")
  private List<DossGeb> dossGebs;

  public DossNk() {
  }

  public Long getCDossNk() {
    return this.cDossNk;
  }

  public void setCDossNk(Long cDossNk) {
    this.cDossNk = cDossNk;
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

  public String getType() {
    return type;
  }

  public void setType(String typeNaamskeuze) {
    this.type = typeNaamskeuze;
  }

  public BigDecimal getcLandNaamRecht() {
    return cLandNaamRecht;
  }

  public void setcLandNaamRecht(BigDecimal cLand) {
    this.cLandNaamRecht = cLand;
  }

  public BigDecimal getPersonType() {
    return personType;
  }

  public void setPersonType(BigDecimal tNaamskeuze) {
    this.personType = tNaamskeuze;
  }

  public BigDecimal getEersteKind() {
    return eersteKind;
  }

  public void setEersteKind(BigDecimal beerstekind) {
    this.eersteKind = beerstekind;
  }

  public BigDecimal getcCGemNk() {
    return cGemNk;
  }

  public void setcCGemNk(BigDecimal cGemNk) {
    this.cGemNk = cGemNk;
  }

  public String getKeuzeNaamTp() {
    return keuzeNaamTp;
  }

  public void setKeuzeNaamTp(String keuzeNaamTp) {
    this.keuzeNaamTp = keuzeNaamTp;
  }
}
