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

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "content")
  private byte[] content;

  public FileRecord() {
    setUuid("dataimport-" + UUID.randomUUID());
  }

  public FileRecord(Long cFileImport, byte[] content) {
    this();
    this.cFileImport = cFileImport;
    this.content = content;
  }

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_fileimport",
      nullable = false,
      insertable = false,
      updatable = false)
  private FileImport fileImport;
}
