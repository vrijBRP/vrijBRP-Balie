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

import lombok.EqualsAndHashCode;

@Entity
@Table(name = "gba_element")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GbaElement extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_gba_element",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "gba_element",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_gba_element")
  @Column(name = "c_gba_element",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGbaElement;

  @EqualsAndHashCode.Include
  @Column(nullable = false,
      precision = 131089)
  private BigDecimal category;

  @EqualsAndHashCode.Include
  @Column(nullable = false,
      precision = 131089)
  private BigDecimal element;

  @ManyToMany(mappedBy = "gbaElements")
  private List<Profile> profiles;

  public GbaElement() {
  }

  public Long getCGbaElement() {
    return this.cGbaElement;
  }

  public void setCGbaElement(Long cGbaElement) {
    this.cGbaElement = cGbaElement;
  }

  public BigDecimal getCategory() {
    return this.category;
  }

  public void setCategory(BigDecimal category) {
    this.category = category;
  }

  public BigDecimal getElement() {
    return this.element;
  }

  public void setElement(BigDecimal element) {
    this.element = element;
  }

  public List<Profile> getProfiles() {
    return this.profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
