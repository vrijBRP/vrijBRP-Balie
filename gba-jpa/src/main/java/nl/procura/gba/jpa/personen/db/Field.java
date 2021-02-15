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
@Table(name = "field")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Field extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_field",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "field",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_field")
  @Column(name = "c_field",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cField;

  @Column(length = 2147483647)
  private String descr;

  @EqualsAndHashCode.Include
  @Column()
  private String field;

  @EqualsAndHashCode.Include
  @Column(name = "field_type")
  private String fieldType;

  @ManyToMany(mappedBy = "fields")
  private List<Profile> profiles;

  public Field() {
  }

  public Long getCField() {
    return this.cField;
  }

  public void setCField(Long cField) {
    this.cField = cField;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getFieldType() {
    return this.fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public List<Profile> getProfiles() {
    return this.profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
