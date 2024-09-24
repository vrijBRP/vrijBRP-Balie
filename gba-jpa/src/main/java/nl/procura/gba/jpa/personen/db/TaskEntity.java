/*
 * Copyright 2022 - 2023 Procura B.V.
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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "task")
public class TaskEntity extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_task",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "task",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_task")
  @Column(name = "c_task",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cTask;

  @Column(name = "type")
  private Integer type;

  @Column(name = "event")
  private Integer event;

  @Column(name = "zaak_id")
  private String zaakId;

  @Column(name = "c_usr")
  private Long cUsr;

  @Column(name = "execution")
  private Integer execution;

  @Column(name = "status")
  private Integer status;

  @Column(name = "descr")
  private String descr;

  @Column(name = "remarks")
  private String remarks;

  @Override
  public Long getUniqueKey() {
    return getCTask();
  }
}
