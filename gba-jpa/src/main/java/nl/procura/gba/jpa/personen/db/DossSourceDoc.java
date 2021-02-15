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

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@Entity
@Table(name = "doss_source_doc")
public class DossSourceDoc extends BaseEntity {

  public static final String DATE_TYPE_NOT_SET   = "";
  public static final String SOURCE_TYPE_NOT_SET = "";

  private static final long serialVersionUID = -9108652979855689770L;

  @Id
  @TableGenerator(name = "table_gen_doss_source_doc",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_source_doc",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_doss_source_doc")
  @Column(name = "c_doss_source_doc", unique = true, nullable = false, precision = 131_089)
  private BigDecimal cDossSourceDoc;

  @Column(name = "d_type", length = 1)
  private String validityDateType;

  @Column(name = "d_in")
  private BigDecimal validityDate;

  @Column(name = "type", length = 1)
  private String docType;

  @Column(name = "number")
  private String docNumber;

  @Column(name = "municipality", length = 10)
  private String docMun;

  @Column(name = "description")
  private String docDescr;

  // empty constructor required for persistence
  protected DossSourceDoc() {
  }

  public static DossSourceDoc newNotSetSourceDocument() {
    return new DossSourceDoc(null, DATE_TYPE_NOT_SET, null, SOURCE_TYPE_NOT_SET, null, null, null);
  }
}
