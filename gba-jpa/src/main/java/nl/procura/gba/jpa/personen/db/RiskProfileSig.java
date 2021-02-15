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

import lombok.Data;

@Data
@Entity
@Table(name = "rp_sig")
public class RiskProfileSig extends BaseEntity<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_rp_sig",
      nullable = false)
  @TableGenerator(name = "table_gen_rp_sig",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rp_sig",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rp_sig")
  private Long cRpSig;

  @Column(name = "bsn",
      nullable = false)
  private BigDecimal bsn;

  @Column(name = "pc",
      nullable = false)
  private String pc;

  @Column(name = "hnr",
      nullable = false)
  private BigDecimal hnr;

  @Column(name = "hnr_l",
      nullable = false)
  private String hnrL;

  @Column(name = "hnr_t",
      nullable = false)
  private String hnrT;

  @Column(name = "hnr_a",
      nullable = false)
  private String hnrA;

  @Column(name = "type",
      nullable = false)
  private BigDecimal type;

  @Column(name = "label",
      nullable = false)
  private String label;

  public RiskProfileSig() {
  }

  public RiskProfileSig(BigDecimal bsn) {
    this.bsn = bsn;
  }
}
