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
@Table(name = "terugm_reactie")
public class TerugmReactie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_terugm_reactie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "terugm_reactie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_terugm_reactie")
  @Column(name = "c_terugm_reactie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cTerugmReactie;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @Column(name = "d_in",
      nullable = false,
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "t_in",
      nullable = false,
      precision = 131089)
  private BigDecimal tIn;

  @Column(name = "terugm_reactie",
      length = 2147483647)
  private String terugmReactie;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_terugmelding",
      nullable = false)
  private Terugmelding terugmelding;

  public TerugmReactie() {
  }

  public Long getCTerugmReactie() {
    return this.cTerugmReactie;
  }

  public void setCTerugmReactie(Long cTerugmReactie) {
    this.cTerugmReactie = cTerugmReactie;
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

  public String getTerugmReactie() {
    return this.terugmReactie;
  }

  public void setTerugmReactie(String terugmReactie) {
    this.terugmReactie = terugmReactie;
  }

  public Terugmelding getTerugmelding() {
    return this.terugmelding;
  }

  public void setTerugmelding(Terugmelding terugmelding) {
    this.terugmelding = terugmelding;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

}
