/*
 * Copyright 2024 - 2025 Procura B.V.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "translation")
@NamedQuery(name = "Translation.findAll",
    query = "SELECT s FROM Translation s")
public class Translation extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_translation",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "translation",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_translation")
  @Column(name = "c_translation",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cTranslation;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "translation")
  private List<TranslationRec> translations;

  public Translation() {
  }

  public Translation(long id) {
    this.cTranslation = id;
  }
}
