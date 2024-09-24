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

package nl.procura.gba.web.modules.beheer.overig;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.commons.core.exceptions.ProException;

/**
 * Class die controleert of er records in een tabel zijn geselecteerd.
 * Zo niet, dan wordt een exception geworpen.
 *

 * <p>
 * 2012
 */
public class TableSelectionCheck {

  public static void checkSelection(GbaTable table) {

    if (!table.isSelectedRecords()) {
      throw new ProException(SELECT, WARNING, "Geen records geselecteerd.");
    }
  }
}
