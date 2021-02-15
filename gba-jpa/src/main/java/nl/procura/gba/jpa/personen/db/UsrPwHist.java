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
@Table(name = "usr_pw_hist")
public class UsrPwHist extends BaseEntity<UsrPwHistPK> {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private UsrPwHistPK id;

  @Lob
  @Column(nullable = false)
  private byte[] pw;

  @Column(name = "reset_pw",
      precision = 131089)
  private BigDecimal resetPw;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr",
      nullable = false,
      insertable = false,
      updatable = false)
  private Usr usr;

  @Column(name = "pw_version",
      precision = 131089)
  private BigDecimal pwVersion;

  public UsrPwHist() {
  }

  @Override
  public UsrPwHistPK getId() {
    return this.id;
  }

  public void setId(UsrPwHistPK id) {
    this.id = id;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public byte[] getPw() {
    return pw;
  }

  public void setPw(byte[] pw) {
    this.pw = pw;
  }

  public BigDecimal getResetPw() {
    return resetPw;
  }

  public void setResetPw(BigDecimal resetPw) {
    this.resetPw = resetPw;
  }

  public BigDecimal getPwVersion() {
    return pwVersion;
  }

  public void setPwVersion(BigDecimal pwVersion) {
    this.pwVersion = pwVersion;
  }
}
