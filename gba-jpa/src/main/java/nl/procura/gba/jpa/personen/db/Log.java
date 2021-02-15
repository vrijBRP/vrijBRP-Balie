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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "log")
public class Log extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_log",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "log",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_log")
  @Column(name = "c_log",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cLog;

  @Column(precision = 131089)
  private BigDecimal blok;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column()
  private String ip;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @Column()
  private String usr;

  @Column(name = "user_agent",
      length = 500)
  private String userAgent;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usrBean;

  public Log() {
  }

  public Long getCLog() {
    return this.cLog;
  }

  public void setCLog(Long cLog) {
    this.cLog = cLog;
  }

  public BigDecimal getBlok() {
    return this.blok;
  }

  public void setBlok(BigDecimal blok) {
    this.blok = blok;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getUsr() {
    return this.usr;
  }

  public void setUsr(String usr) {
    this.usr = usr;
  }

  public Usr getUsrBean() {
    return this.usrBean;
  }

  public void setUsrBean(Usr usrBean) {
    this.usrBean = usrBean;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }
}
