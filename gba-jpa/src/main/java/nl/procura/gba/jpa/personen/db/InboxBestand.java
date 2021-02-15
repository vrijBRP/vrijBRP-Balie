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
@Table(name = "inbox_bestand")
@NamedQuery(name = "InboxBestand.findByInboxId",
    query = "SELECT i FROM InboxBestand i where i.inbox.cInbox = :c_inbox")
public class InboxBestand extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_inbox_bestand",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "inbox_bestand",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_inbox_bestand")
  @Column(name = "c_inbox_bestand",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cInboxBestand;

  private byte[] bestand;

  @ManyToOne
  @JoinColumn(name = "c_inbox")
  private Inbox inbox;

  public InboxBestand() {
  }

  public Long getCInboxBestand() {
    return this.cInboxBestand;
  }

  public void setCInboxBestand(Long cInboxBestand) {
    this.cInboxBestand = cInboxBestand;
  }

  public byte[] getBestand() {
    return this.bestand;
  }

  public void setBestand(byte[] bestand) {
    this.bestand = bestand;
  }

  public Inbox getInbox() {
    return this.inbox;
  }

  public void setInbox(Inbox inbox) {
    this.inbox = inbox;
  }
}
