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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "filerecord")
public class FileRecord extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_filerecord",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "filerecord",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_filerecord")
  @Column(name = "c_filerecord",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cFileRecord;

  @Column(name = "c_fileimport")
  private Long cFileImport;

  @Column(name = "content")
  private byte[] content;

  public Long getcFileRecord() {
    return cFileRecord;
  }

  public void setcFileRecord(Long cFileRecord) {
    this.cFileRecord = cFileRecord;
  }

  public Long getcFileImport() {
    return cFileImport;
  }

  public void setcFileImport(Long cFileImport) {
    this.cFileImport = cFileImport;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}
