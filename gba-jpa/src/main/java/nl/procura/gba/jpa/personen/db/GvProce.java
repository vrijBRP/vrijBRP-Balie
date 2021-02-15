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
@Table(name = "gv_proces")
public class GvProce extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_gv_proces",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "gv_proces",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_gv_proces")
  @Column(name = "c_gv_proces",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGvProces;

  @Column(name = "c_procesactie",
      precision = 131089)
  private BigDecimal cProcesactie;

  @Column(name = "c_reactie",
      precision = 131089)
  private BigDecimal cReactie;

  @Column(name = "d_end_termijn",
      precision = 131089)
  private BigDecimal dEndTermijn;

  @Column(length = 2147483647)
  private String motivering;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  @ManyToOne
  @JoinColumn(name = "c_gv",
      nullable = false)
  private Gv gv;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  public GvProce() {
  }

  public Long getCGvProces() {
    return this.cGvProces;
  }

  public void setCGvProces(Long cGvProces) {
    this.cGvProces = cGvProces;
  }

  public BigDecimal getCProcesactie() {
    return this.cProcesactie;
  }

  public void setCProcesactie(BigDecimal cProcesactie) {
    this.cProcesactie = cProcesactie;
  }

  public BigDecimal getCReactie() {
    return this.cReactie;
  }

  public void setCReactie(BigDecimal cReactie) {
    this.cReactie = cReactie;
  }

  public BigDecimal getDEndTermijn() {
    return this.dEndTermijn;
  }

  public void setDEndTermijn(BigDecimal dEndTermijn) {
    this.dEndTermijn = dEndTermijn;
  }

  public String getMotivering() {
    return this.motivering;
  }

  public void setMotivering(String motivering) {
    this.motivering = motivering;
  }

  public Gv getGv() {
    return this.gv;
  }

  public void setGv(Gv gv) {
    this.gv = gv;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }
}
