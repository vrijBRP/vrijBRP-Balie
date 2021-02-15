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
@Table(name = "idvaststelling")
public class Idvaststelling extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_idvaststelling",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "idvaststelling",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_idvaststelling")
  @Column(name = "c_idvaststelling",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cIdvaststelling;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @Column()
  private String documentnr;

  @Column(name = "s_verificatie",
      length = 2147483647)
  private String sVerificatie;

  @Column()
  private String soort;

  @Column(precision = 131089)
  private BigDecimal verificatie;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  public Idvaststelling() {
  }

  public Long getCIdvaststelling() {
    return this.cIdvaststelling;
  }

  public void setCIdvaststelling(Long cIdvaststelling) {
    this.cIdvaststelling = cIdvaststelling;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getTIn() {
    return tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public String getDocumentnr() {
    return this.documentnr;
  }

  public void setDocumentnr(String documentnr) {
    this.documentnr = documentnr;
  }

  public String getSVerificatie() {
    return this.sVerificatie;
  }

  public void setSVerificatie(String sVerificatie) {
    this.sVerificatie = sVerificatie;
  }

  public String getSoort() {
    return this.soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public BigDecimal getVerificatie() {
    return this.verificatie;
  }

  public void setVerificatie(BigDecimal verificatie) {
    this.verificatie = verificatie;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

}
