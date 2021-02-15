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

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "rp_type")
public class RiskProfileType implements Serializable {

  @Id
  @Column(name = "c_rp_type", nullable = false)
  @TableGenerator(name = "table_gen_rp_type",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rp_type",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_rp_type")
  private Long cRpType;

  @Column(name = "c_rp", nullable = false)
  private Long cRp;

  @Enumerated(EnumType.STRING)
  private RiskProfileZaakType type;

  public RiskProfileType() {
  }

  public RiskProfileType(Long cRp, RiskProfileZaakType type) {
    this.cRp = cRp;
    this.type = type;
  }
}
