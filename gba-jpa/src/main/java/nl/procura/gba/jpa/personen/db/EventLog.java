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

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.*;

import lombok.Getter;
import lombok.ToString;

/**
 * Event log entry.
 * <p>
 * Doesn't have payload now, but can be added later.
 */
@Entity
@Table(name = "event_log")
@ToString
@Getter
public class EventLog {

  @Id
  @TableGenerator(name = "table_gen_event_log",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "event_log",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_event_log")
  @Column(name = "c_event_log", nullable = false, unique = true)
  private Long cEventLog;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private EventType type;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "object_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private EventObjectType objectType;

  @Column(name = "object_id", nullable = false)
  private String objectId;

  @Column(name = "time_stamp", nullable = false)
  private Timestamp timeStamp;

  /**
   * Required empty constructor for JPA.
   */
  public EventLog() {
  }

  public EventLog(EventType type, Long userId, EventObjectType objectType, String objectId) {
    this.type = requireNonNull(type);
    this.userId = requireNonNull(userId);
    this.objectType = requireNonNull(objectType);
    this.objectId = requireNonNull(objectId);
    this.timeStamp = Timestamp.from(Instant.now());
  }
}
