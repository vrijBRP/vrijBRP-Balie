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

package nl.procura.gbaws.web.vaadin.layouts.dialogs;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Window;

import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

/**

 * <p>
 * 2012/2015
 * <p>
 * Standaardprocedure, inclusief melding bij geen geselecteerde records, voor het verwijderen van records uit een tabel.
 */

public class DeleteProcedure<T> {

  private static final int EEN = 1;

  protected String MELDING_GEEN_RECORDS_OM_TE_VERWIJDEREN = "Er zijn geen records om te verwijderen";
  protected String GEEN_RECORDS_GESELECTEERD              = "Geen records geselecteerd";
  protected String RECORD_VERWIJDEREN                     = "record verwijderen?";
  protected String RECORDS_VERWIJDEREN                    = "records verwijderen?";
  protected String RECORD_IS_VERWIJDERD                   = "Record is verwijderd";
  protected String RECORDS_ZIJN_VERWIJDERD                = "Records zijn verwijderd";

  /**
   * Opent window in window naar keuze. Je kunt geen window openen in subwindow
   */
  public DeleteProcedure(IndexedTable table) {
    this(table, false);
  }

  public DeleteProcedure(IndexedTable table, boolean askAll) {

    updateMessages();

    Window window = table.getWindow();

    if (window.isModal() && window.getParent() != null) {
      window = window.getParent();
    }

    List<Record> records = new ArrayList<>();

    if (table.getRecords().size() == 0) {

      throw new ProException(WARNING, MELDING_GEEN_RECORDS_OM_TE_VERWIJDEREN);
    } else if (table.isSelectedRecords()) {

      records.addAll(table.getSelectedRecords());
    } else {

      if (askAll) {
        records.addAll(table.getRecords());
      } else {
        throw new ProException(WARNING, GEEN_RECORDS_GESELECTEERD);
      }
    }

    for (Record r : records) {
      beforeDelete((T) r.getObject());
    }

    String message = getMessage(records.size(), table.isSelectedRecords());

    window.addWindow(new DeleteDialog(table, message, records));
  }

  protected void updateMessages() {
  }

  private String getMessage(int count, boolean selected) {

    String sel = selected ? "geselecteerde " : " ";

    switch (count) {

      case EEN:
        return "Het " + sel + RECORD_VERWIJDEREN;

      default:
        if (selected) {
          return "De " + count + " " + sel + RECORDS_VERWIJDEREN;
        }
        return "Alle " + count + " " + sel + RECORDS_VERWIJDEREN;
    }
  }

  @SuppressWarnings("unused")
  protected void beforeDelete(T object) {
  }

  protected void beforeDelete() {
  }

  protected void afterDelete() {
  }

  @SuppressWarnings("unused")
  protected void deleteValue(T value) {
  } // Override

  @SuppressWarnings("unused")
  protected void deleteValues(List<T> values) {
  }

  public class DeleteDialog extends ConfirmDialog {

    private static final long serialVersionUID = -6849494631726202577L;

    private IndexedTable table;
    private List<Record> records;

    private DeleteDialog(IndexedTable table, String message, List<Record> records) {

      super(message);

      this.records = records;
      this.table = table;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void buttonYes() {

      beforeDelete();

      List<T> list = new ArrayList<>();

      for (Record r : records) {
        list.add((T) r.getObject());
      }

      deleteValues(list);

      for (Record r : records) {
        deleteValue((T) r.getObject());
        table.deleteRecord(r);
      }

      afterDelete();

      new Message(getWindow().getParent(), (records.size() > 1) ? RECORDS_ZIJN_VERWIJDERD : RECORD_IS_VERWIJDERD,
          Message.TYPE_SUCCESS);

      close();
    }
  }
}
