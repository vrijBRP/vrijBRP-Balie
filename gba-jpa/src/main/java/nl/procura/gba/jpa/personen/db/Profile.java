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
@Table(name = "profile")
public class Profile extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_profile",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "profile",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_profile")
  @Column(name = "c_profile",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cProfile;

  @Column(length = 2147483647)
  private String descr;

  @Column()
  private String profile;

  @ManyToMany
  @JoinTable(name = "profile_action",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_action",
          nullable = false) })
  private List<Action> actions;

  @ManyToMany
  @JoinTable(name = "profile_field",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_field") })
  private List<Field> fields;

  @ManyToMany
  @JoinTable(name = "profile_gba_element",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_gba_element") })
  private List<GbaElement> gbaElements;

  @ManyToMany
  @JoinTable(name = "profile_gba_category",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_gba_category") })
  private List<GbaCategory> gbaCategories;

  @OneToMany(mappedBy = "profile")
  private List<Parm> parms;

  @ManyToMany
  @JoinTable(name = "profile_aantekening_ind",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_aantekening_ind") })
  private List<AantekeningInd> aantekeningInds;

  @ManyToMany
  @JoinTable(name = "profile_zaak_conf",
      joinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_zaak_conf") })
  private List<ZaakConf> zaakConfs;

  @ManyToMany(mappedBy = "profiles")
  @BatchFetch(BatchFetchType.IN)
  private List<Usr> usrs;

  public Profile() {
  }

  public Profile(Long cProfile) {
    setCProfile(cProfile);
  }

  public Long getCProfile() {
    return this.cProfile;
  }

  public void setCProfile(Long cProfile) {
    this.cProfile = cProfile;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getProfile() {
    return this.profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public List<Action> getActions() {
    return this.actions;
  }

  public void setActions(List<Action> actions) {
    this.actions = actions;
  }

  public List<Field> getFields() {
    return this.fields;
  }

  public void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public List<GbaElement> getGbaElements() {
    return this.gbaElements;
  }

  public void setGbaElements(List<GbaElement> gbaElements) {
    this.gbaElements = gbaElements;
  }

  public List<Usr> getUsrs() {
    return this.usrs;
  }

  public void setUsrs(List<Usr> usrs) {
    this.usrs = usrs;
  }

  public List<GbaCategory> getGbaCategories() {
    return gbaCategories;
  }

  public void setGbaCategories(List<GbaCategory> gbaCategories) {
    this.gbaCategories = gbaCategories;
  }

  public List<AantekeningInd> getAantekeningInds() {
    return aantekeningInds;
  }

  public void setAantekeningInds(List<AantekeningInd> aantekeningInds) {
    this.aantekeningInds = aantekeningInds;
  }

  public List<Parm> getParms() {
    return parms;
  }

  public void setParms(List<Parm> parms) {
    this.parms = parms;
  }

  public List<ZaakConf> getZaakConfs() {
    return zaakConfs;
  }

  public void setZaakConfs(List<ZaakConf> zaakConfs) {
    this.zaakConfs = zaakConfs;
  }
}
