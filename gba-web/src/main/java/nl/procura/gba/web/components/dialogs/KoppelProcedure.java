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

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

/**

 * <p>
 * 2012
 * <p>
 * Deze klasse verzorgt het koppelen/ontkoppelen van objecten
 */
public abstract class KoppelProcedure {

  private final GbaTable    table;
  private final KoppelActie koppelActie;
  private final String      type;       // type is type objecten waaraan gekoppeld wordt
  private final boolean     heleTabel;  // geef 'true' mee als je alle records wilt koppelen

  public KoppelProcedure(GbaTable table, KoppelActie koppelActie, String type, boolean heleTabel) {

    this.table = table;
    this.koppelActie = koppelActie;
    this.type = type;
    this.heleTabel = heleTabel;

    if (heleTabel) {
      checkRecords();
    } else {
      checkSelectedRecords();
    }
  }

  public abstract void koppel(KoppelActie koppelActie, boolean heleTabel);

  private void bevestigDialog(int numberOfDirs) {

    if (heleTabel) {
      if (numberOfDirs > 0) {
        table.getWindow().addWindow(new KoppelAllesDialog(numberOfDirs, koppelActie));
      } else {
        table.getWindow().addWindow(new KoppelAllesDialog(koppelActie));
      }
    } else {
      table.getWindow().addWindow(new KoppelMapDialog(numberOfDirs, koppelActie));
    }
  }

  private void checkForDirs(List<Record> selectedRecords) {

    if (selectedRecords.isEmpty()) {
      new Message(table.getWindow(), "Er zijn geen records in de tabel", Message.TYPE_WARNING_MESSAGE);
    }

    removeParentDir(selectedRecords); // we willen 'terugmapje' negeren

    if (selectedRecords.size() != 0) { // als er geen records zijn, niks doen!

      int numberOfDirs = 0;
      for (Record r : selectedRecords) {
        if (r.getObject() instanceof Dir) {
          numberOfDirs++;
        }
      }

      coupleOrGiveConfirmDialog(numberOfDirs, koppelActie);
    }
  }

  private void checkRecords() {

    checkForDirs(table.getFilteredRecords());
  }

  private void checkSelectedRecords() {

    if (table.isSelectedRecords()) {

      checkForDirs(table.getSelectedRecords());
    } else {

      new Message(table.getWindow(), "Geen records geselecteerd", Message.TYPE_WARNING_MESSAGE);
    }
  }

  private void coupleOrGiveConfirmDialog(int numberOfDirs, KoppelActie koppelActie) {

    if (numberOfDirs > 0 || heleTabel) {
      bevestigDialog(numberOfDirs);
    } else {
      // geen mapjes geselecteerd
      koppel(koppelActie, heleTabel);
    }
  }

  private void removeParentDir(List<Record> selectedRecords) {

    for (Record r : selectedRecords) {
      if (r.getObject() instanceof Dir) {
        Dir dir = (Dir) r.getObject();
        if (dir.isParentDir()) {
          selectedRecords.remove(r);
          break;
        }
      }
    }
  }

  private class KoppelAllesDialog extends ConfirmDialog {

    static final String beginQuestionOneDir       = "Weet u zeker dat u <b> ALLE </b> items uit de map wilt ";
    static final String beginQuestionMultipleDirs = "Weet u zeker dat u <b> ALLE </b> items uit de mappen wilt ";

    private KoppelAllesDialog(int numberOfDirs, KoppelActie koppelActie) {

      super(numberOfDirs > 1 ? String.format("%s%s aan de geselecteerde %s?", beginQuestionMultipleDirs,
          koppelActie.toString().toLowerCase(), type)
          : String.format(
              "%s%s aan de geselecteerde %s?", beginQuestionOneDir, koppelActie.toString().toLowerCase(), type),
          500);

    }

    private KoppelAllesDialog(KoppelActie koppelActie) {
      super("Weet u zeker dat u alle records in de tabel wilt " + koppelActie.toString().toLowerCase() + "?",
          500);
    }

    @Override
    public void buttonYes() {

      close();
      koppel(koppelActie, heleTabel);
    }
  }

  private class KoppelMapDialog extends ConfirmDialog {

    static final String beginQuestionOneDir       = "Weet u zeker dat u <b> ALLE </b> items uit de geselecteerde map wilt ";
    static final String beginQuestionMultipleDirs = "Weet u zeker dat u <b> ALLE </b> items uit de geselecteerde mappen wilt ";

    private KoppelMapDialog(int numberOfDirs, KoppelActie koppelActie) {

      super(numberOfDirs > 1 ? String.format("%s%s aan de geselecteerde %s?", beginQuestionMultipleDirs,
          koppelActie.toString().toLowerCase(), type)
          : String.format(
              "%s%s aan de geselecteerde %s?", beginQuestionOneDir, koppelActie.toString().toLowerCase(), type),
          500);

    }

    @Override
    public void buttonYes() {

      close();
      koppel(koppelActie, heleTabel);
    }
  }
}
