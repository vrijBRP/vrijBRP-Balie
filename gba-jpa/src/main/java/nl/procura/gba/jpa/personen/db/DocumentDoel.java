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
@Table(name = "document_doel")
public class DocumentDoel extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_document_doel",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "document_doel",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_document_doel")
  @Column(name = "c_document_doel",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDocumentDoel;

  @Column(name = "document_doel")
  private String documentDoel;

  public DocumentDoel() {
  }

  public Long getCDocumentDoel() {
    return this.cDocumentDoel;
  }

  public void setCDocumentDoel(Long cDocumentDoel) {
    this.cDocumentDoel = cDocumentDoel;
  }

  public String getDocumentDoel() {
    return this.documentDoel;
  }

  public void setDocumentDoel(String documentDoel) {
    this.documentDoel = documentDoel;
  }

}
