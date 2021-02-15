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

import lombok.EqualsAndHashCode;

@Entity
@Table(name = "action")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Action extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_action",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "action",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_action")
  @Column(name = "c_action",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAction;

  @Column()
  @EqualsAndHashCode.Include
  private String action;

  @Column(name = "action_type")
  @EqualsAndHashCode.Include
  private String actionType;

  @Column(length = 2147483647)
  private String descr;

  @ManyToMany(mappedBy = "actions")
  private List<Profile> profiles;

  public Action() {
  }

  public Long getCAction() {
    return this.cAction;
  }

  public void setCAction(Long cAction) {
    this.cAction = cAction;
  }

  public String getAction() {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getActionType() {
    return this.actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public List<Profile> getProfiles() {
    return this.profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
