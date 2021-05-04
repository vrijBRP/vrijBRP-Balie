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

import javax.persistence.*;

@Entity
@Table(name = "document_dms_type")
public class DocumentDmsType extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_document_dms_type",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "document_dms_type",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_document_dms_type")
  @Column(name = "c_document_dms_type",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDocumentDmsType;

  @Column(name = "document_dms_type")
  private String documentDmsType;

  @Column(name = "zaak_types")
  private String zaakTypes;

  public DocumentDmsType() {
  }

  public Long getcDocumentDmsType() {
    return cDocumentDmsType;
  }

  public void setcDocumentDmsType(Long cDocumentDmsType) {
    this.cDocumentDmsType = cDocumentDmsType;
  }

  public String getDocumentDmsType() {
    return documentDmsType;
  }

  public void setDocumentDmsType(String documentDmsType) {
    this.documentDmsType = documentDmsType;
  }

  public String getZaakTypes() {
    return zaakTypes;
  }

  public void setZaakTypes(String zaakTypes) {
    this.zaakTypes = zaakTypes;
  }
}
