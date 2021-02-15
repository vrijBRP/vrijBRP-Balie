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

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

/**

 * <p>
 * 2012
 * <p>
 * Deze klasse zorgt voor het verwijderen van records uit een tabel waarin ook mappen mogen voorkomen.
 */
public abstract class DeleteRecordsFromTable {

  private final GbaTable table;
  private List<Record>   selectedRecords = new ArrayList<>();
  private int            numberOfSelectedRecords;            // inclusief Dirs!!
  private int            numberOfSelectedDirs;
  private int            totalNumberOfSelectedRecords;       // alle records, inclusief die in mapjes

  public DeleteRecordsFromTable(GbaTable table) {

    this.table = table;
    checkSelectedRecords(table);
  }

  @SuppressWarnings("unused")
  protected void deleteRecord(Dir dir) {
  } // Override

  @SuppressWarnings("unused")
  protected void deleteRecord(Record record) {
  } // Override

  protected int getTotalNumberOfSelectedRecords() {
    return table.getSelectedRecords().size();
  }

  private void checkSelectedRecords(GbaTable table) {

    if (table.isSelectedRecords()) {
      selectedRecords = table.getSelectedRecords();
      deleteRecords();
    } else {
      new Message(table.getWindow(), "Geen records geselecteerd", Message.TYPE_WARNING_MESSAGE);
    }
  }

  private void deleteRecords() {

    SelectedRecords selectedRecords = new SelectedRecords();

    numberOfSelectedRecords = selectedRecords.getNumberOfSelectedRecords();
    numberOfSelectedDirs = selectedRecords.getNumberOfSelectedDirs();
    totalNumberOfSelectedRecords = getTotalNumberOfSelectedRecords();

    if (numberOfSelectedRecords > 0) {
      giveAppropriateDeleteDialog();
    }
  }

  private void giveAppropriateDeleteDialog() {

    if (numberOfSelectedDirs == numberOfSelectedRecords) { // alleen mapjes geselecteerd

      table.getApplication().getParentWindow().addWindow(new DeleteDirsFromTableDialog(table));
    } else if (numberOfSelectedDirs > 0) { // mapjes en niet-mapjes geselecteerd

      table.getApplication().getParentWindow().addWindow(new DeleteDirsAndNonDirsFromTableDialog(table));
    } else { // alleen niet-mapjes geselecteerd

      table.getApplication().getParentWindow().addWindow(new DeleteNonDirsFromTableDialog(table));
    }
  }

  private void giveDeleteMessage() {

    String message;

    if (totalNumberOfSelectedRecords == 1) {
      message = "Er is één record verwijderd.";
    } else { // meer dan 1 record
      message = "Er zijn " + totalNumberOfSelectedRecords + " records verwijderd.";
    }

    new Message(table.getWindow(), message, Message.TYPE_SUCCESS);

  }

  private class DeleteDirsAndNonDirsFromTableDialog extends ConfirmDialog {

    private final GbaTable table;

    private DeleteDirsAndNonDirsFromTableDialog(GbaTable table) {

      super(numberOfSelectedRecords > 1 ? "De geselecteerde records verwijderen?"
          : "Het geselecteerde record verwijderen?");
      this.table = table;
    }

    @Override
    public void buttonYes() {
      close();

      table.getWindow().addWindow(new ConfirmDialog(
          numberOfSelectedDirs > 1 ? "De geselecteerde mappen met <br> <b>ALLE INHOUD</b> verwijderen?"
              : "De geselecteerde map met <br> <b> ALLE INHOUD </b> verwijderen?") {

        @Override
        public void buttonYes() {

          for (Record r : selectedRecords) {
            if (r.getObject() instanceof Dir) {
              Dir dir = (Dir) r.getObject();
              deleteRecord(dir);
              table.deleteRecord(r); // merk op dat 'terugmapje' al verwijderd is uit selectedRecords
            } else {
              deleteRecord(r);
              table.deleteRecord(r);
            }
          }

          giveDeleteMessage();

          close();
        }
      });
    }
  }

  private class DeleteDirsFromTableDialog extends ConfirmDialog {

    private final GbaTable table;

    private DeleteDirsFromTableDialog(GbaTable table) {
      super(numberOfSelectedDirs > 1 ? "De geselecteerde mappen met <br> <b>ALLE INHOUD</b> verwijderen?"
          : "De geselecteerde map met <br> <b> ALLE INHOUD </b> verwijderen?");

      this.table = table;
    }

    @Override
    public void buttonYes() {
      close();

      for (Record r : selectedRecords) {
        Dir dir = (Dir) r.getObject();
        deleteRecord(dir);
        table.deleteRecord(r); // merk op dat het 'terugmapje' al verwijderd is
      }

      giveDeleteMessage();
    }
  }

  private class DeleteNonDirsFromTableDialog extends ConfirmDialog {

    private final GbaTable table;

    private DeleteNonDirsFromTableDialog(GbaTable table) {
      super(numberOfSelectedRecords > 1 ? "De geselecteerde records verwijderen?"
          : "Het geselecteerde record verwijderen?");
      this.table = table;
    }

    @Override
    public void buttonYes() {
      close();

      for (Record r : selectedRecords) {
        deleteRecord(r);
        table.deleteRecord(r); // 'terugmpje' is al verwijderd uit selectedRecords
      }

      giveDeleteMessage();
    }
  }

  /**
   * Deze klasse telt het aantal geselecteerde records en het aantal geselecteerde
   * mapjes in de tabel. Het 'terugmapje' wordt hierbij genegeerd. Hier wordt
   * het 'terugmapje' ook verwijderd van de geselecteerde records.
   *
  
   * <p>
   * 2012
   */
  private class SelectedRecords {

    private int numberOfSelectedRecords;
    private int numberOfSelectedDirs;

    private SelectedRecords() {

      for (Record r : selectedRecords) {
        if (r.getObject() instanceof Dir) {
          Dir dir = (Dir) r.getObject();
          if (!dir.isParentDir()) { // we negeren het 'terugmapje' !
            numberOfSelectedRecords++;
            numberOfSelectedDirs++;
          }
        } else {
          numberOfSelectedRecords++;
        }
      }

      removeParentDirFromSelectedRecords();
    }

    public int getNumberOfSelectedDirs() {
      return numberOfSelectedDirs;
    }

    public int getNumberOfSelectedRecords() {
      return numberOfSelectedRecords;
    }

    private void removeParentDirFromSelectedRecords() {
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
  }
}
