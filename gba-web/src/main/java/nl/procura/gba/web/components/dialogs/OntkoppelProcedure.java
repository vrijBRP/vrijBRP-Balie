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

package nl.procura.gba.web.components.dialogs;

import nl.procura.gba.web.components.layouts.table.GbaTable;

public class OntkoppelProcedure<T> extends DeleteProcedure<T> {

  public OntkoppelProcedure(GbaTable table) {
    super(table);
  }

  public OntkoppelProcedure(GbaTable table, boolean askAll) {
    super(table, askAll);
  }

  @Override
  protected void updateMessages() {

    MELDING_GEEN_RECORDS_OM_TE_VERWIJDEREN = "Er zijn geen records om te ontkoppelen";
    RECORD_VERWIJDEREN = "Het {0} record ontkoppelen?";
    RECORDS_VERWIJDEREN = "De {0} records ontkoppelen?";
    RECORD_IS_VERWIJDERD = "Het record is ontkoppeld";
    RECORDS_ZIJN_VERWIJDERD = "De records zijn ontkoppeld";
  }
}
