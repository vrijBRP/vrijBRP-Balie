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

package nl.procura.gba.web.modules.zaken.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PagingLayoutConfig {

  private boolean  showPeriod   = false;
  private boolean  showSorting  = false;
  private String[] columnWidths = new String[]{};

  public static PagingLayoutConfig getDefault() {
    return new PagingLayoutConfig()
        .setShowPeriod(true)
        .setShowSorting(true)
        .setColumnWidths(new String[]{ "200px", "100px", "60px", "40px", "100px",
            "70px", "60px", "70px", "100px", "70px", "" });
  }
}
