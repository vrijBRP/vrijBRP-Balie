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
@Table(name = "email")
public class Email extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_email",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "email",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_email")
  @Column(name = "c_email",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cEmail;

  @Column(precision = 131089)
  private BigDecimal actief;

  @Column(name = "geldig")
  private int geldigheid = 0;

  @Column()
  private String email;

  @Column()
  private String onderwerp;

  @Column()
  private String van;

  @Column()
  private String bcc;

  @Column(name = "antwoord_naar")
  private String antwoordNaar;

  @Column(name = "inhoud")
  private String inhoud;

  @Column(name = "content_type",
      length = 1)
  private String contentType;

  public Email() {
  }

  public Long getCEmail() {
    return cEmail;
  }

  public void setCEmail(Long cEmail) {
    this.cEmail = cEmail;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getOnderwerp() {
    return onderwerp;
  }

  public void setOnderwerp(String onderwerp) {
    this.onderwerp = onderwerp;
  }

  public String getVan() {
    return van;
  }

  public void setVan(String van) {
    this.van = van;
  }

  public String getAntwoordNaar() {
    return antwoordNaar;
  }

  public void setAntwoordNaar(String antwoordNaar) {
    this.antwoordNaar = antwoordNaar;
  }

  public String getInhoud() {
    return inhoud;
  }

  public void setInhoud(String inhoud) {
    this.inhoud = inhoud;
  }

  public BigDecimal getActief() {
    return actief;
  }

  public void setActief(BigDecimal actief) {
    this.actief = actief;
  }

  public int getGeldigheid() {
    return geldigheid;
  }

  public void setGeldigheid(int geldigheid) {
    this.geldigheid = geldigheid;
  }

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
