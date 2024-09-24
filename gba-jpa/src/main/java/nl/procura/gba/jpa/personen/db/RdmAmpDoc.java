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
@Table(name = "rdm_amp_doc")
public class RdmAmpDoc extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_rdm_amp_doc",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rdm_amp_doc",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rdm_amp_doc")
  @Column(name = "c_rdm_amp_doc",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long id;

  @Column(name = "doc_nr")
  private String docNr;

  @Column(name = "doc_type")
  private String docType;

  @Column(name = "d_end_geld")
  private Long dEndGeld;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_rdm_amp")
  private RdmAmp rdmAmp;
}
