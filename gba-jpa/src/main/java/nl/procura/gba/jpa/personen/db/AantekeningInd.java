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
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "aantekening_ind")
public class AantekeningInd extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_aantekening_ind",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "aantekening_ind",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_aantekening_ind")
  @Column(name = "c_aantekening_ind",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAantekeningInd;

  @Column(name = "aantekening_ind")
  private String aantekeningInd;

  @Column()
  private String oms;

  @Column()
  private String probev;

  @Column()
  private String button;

  @OneToMany(mappedBy = "aantekeningInd")
  private Set<AantekeningHist> aantekeningHists;

  @ManyToMany(mappedBy = "aantekeningInds")
  private List<Profile> profiles;

  @OneToMany(mappedBy = "aantekeningInd")
  private List<Indicatie> indicaties;

  public AantekeningInd() {
  }

  public Long getCAantekeningInd() {
    return this.cAantekeningInd;
  }

  public void setCAantekeningInd(Long cAantekeningInd) {
    this.cAantekeningInd = cAantekeningInd;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getProbev() {
    return this.probev;
  }

  public void setProbev(String probev) {
    this.probev = probev;
  }

  public Set<AantekeningHist> getAantekeningHists() {
    return this.aantekeningHists;
  }

  public void setAantekeningHists(Set<AantekeningHist> aantekeningHists) {
    this.aantekeningHists = aantekeningHists;
  }

  public AantekeningHist addAantekeningHist(AantekeningHist aantekeningHist) {
    getAantekeningHists().add(aantekeningHist);
    aantekeningHist.setAantekeningInd(this);

    return aantekeningHist;
  }

  public AantekeningHist removeAantekeningHist(AantekeningHist aantekeningHist) {
    getAantekeningHists().remove(aantekeningHist);
    aantekeningHist.setAantekeningInd(null);

    return aantekeningHist;
  }

  public List<Indicatie> getIndicaties() {
    return indicaties;
  }

  public void setIndicaties(List<Indicatie> indicaties) {
    this.indicaties = indicaties;
  }

  public Indicatie addIndicaty(Indicatie indicaty) {
    getIndicaties().add(indicaty);
    indicaty.setAantekeningInd(this);

    return indicaty;
  }

  public Indicatie removeIndicaty(Indicatie indicaty) {
    getIndicaties().remove(indicaty);
    indicaty.setAantekeningInd(null);

    return indicaty;
  }

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }

  public String getAantekeningInd() {
    return aantekeningInd;
  }

  public void setAantekeningInd(String aantekeningInd) {
    this.aantekeningInd = aantekeningInd;
  }

  public String getButton() {
    return button;
  }

  public void setButton(String button) {
    this.button = button;
  }
}
