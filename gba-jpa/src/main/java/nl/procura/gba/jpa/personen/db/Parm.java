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

import java.util.Objects;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "parm")
public class Parm extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_parm",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "parm",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_parm")
  @Column(name = "c_parm",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cParm;

  @Column()
  private String parm;

  @Column(length = 2147483647)
  private String value;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_profile")
  private Profile profile;

  public Parm() {
  }

  public static Parm newDefault(String parm, String value) {
    Parm p = new Parm();
    p.setParm(parm);
    p.setValue(value);
    p.setUsr(new Usr(0L));
    p.setProfile(new Profile(0L));
    return p;
  }

  public boolean isParm(String key) {
    return Objects.equals(getParm(), key);
  }

  public boolean isUserCode(long cUsr) {
    return Objects.equals(getUsr().getCUsr(), cUsr);
  }

  public boolean isProfileCode(long cProfile) {
    return Objects.equals(getProfile().getCProfile(), cProfile);
  }

  public Long getCParm() {
    return this.cParm;
  }

  public void setCParm(Long cParm) {
    this.cParm = cParm;
  }

  public String getParm() {
    return this.parm;
  }

  public void setParm(String parm) {
    this.parm = parm;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }
}
