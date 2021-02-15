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

@Entity
@Table(name = "sms")
public class Sms extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_sms",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "sms",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_sms")
  @Column(name = "c_sms",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cSms;

  @Column(precision = 131089,
      name = "active")
  private BigDecimal active;

  @Column(precision = 131089,
      name = "auto")
  private BigDecimal auto;

  @Column(name = "sender_id")
  private String senderId;

  @Column(name = "content")
  private String content;

  @Column(name = "type")
  private String type;

  public Sms() {
  }

  public Long getCSms() {
    return cSms;
  }

  public void setCSms(Long cSms) {
    this.cSms = cSms;
  }

  public BigDecimal getActive() {
    return active;
  }

  public void setActive(BigDecimal active) {
    this.active = active;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getAuto() {
    return auto;
  }

  public void setAuto(BigDecimal auto) {
    this.auto = auto;
  }
}
