/*
 * Copyright 2024 - 2025 Procura B.V.
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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Window;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

/**

 * <p>
 * 2012
 * <p>
 * Standaardprocedure, inclusief melding bij geen geselecteerde records, voor het verwijderen van records uit een tabel.
 */

public class DeleteProcedure<T> {

  private static final int EEN = 1;

  protected String       MELDING_GEEN_RECORDS_OM_TE_VERWIJDEREN = "Er zijn geen records om te verwijderen";
  protected final String GEEN_RECORDS_GESELECTEERD              = "Geen records geselecteerd";
  protected String       RECORD_VERWIJDEREN                     = "Het {0} record verwijderen?";
  protected String       RECORDS_VERWIJDEREN                    = "De {0} records verwijderen?";
  protected String       RECORD_IS_VERWIJDERD                   = "Het record is verwijderd";
  protected String       RECORDS_ZIJN_VERWIJDERD                = "De records zijn verwijderd";

  /**
   * Opent window in window naar keuze. Je kunt geen window openen in subwindow
   */
  public DeleteProcedure(GbaTable table) {
    this(table, false);
  }

  public DeleteProcedure(GbaTable table, boolean askAll) {

    updateMessages();

    Window window = table.getWindow();

    if (window.isModal() && window.getParent() != null) {
      window = window.getParent();
    }

    List<Record> records = new ArrayList<>();

    if (table.getRecords().isEmpty()) {

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

  protected void afterDelete() {
  }

  protected void beforeDelete() {
  }

  @SuppressWarnings("unused")
  protected void beforeDelete(T object) {
  }

  @SuppressWarnings("unused")
  protected void deleteValue(T value) {
  } // Override

  @SuppressWarnings("unused")
  protected void deleteValues(List<T> values) {
  }

  protected void updateMessages() {
  }

  private String getMessage(int count, boolean selected) {

    String sel = selected ? "geselecteerde " : " ";

    switch (count) {

      case EEN:
        return MessageFormat.format(RECORD_VERWIJDEREN, sel);

      default:
        if (selected) {
          return MessageFormat.format(RECORDS_VERWIJDEREN, count + " " + sel);
        }
        return MessageFormat.format(RECORDS_VERWIJDEREN, count + " " + sel);
    }
  }

  public class DeleteDialog extends ConfirmDialog {

    private final GbaTable     table;
    private final List<Record> records;

    private DeleteDialog(GbaTable table, String message, List<Record> records) {
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

      table.init();

      afterDelete();

      new Message(getWindow().getParent(),
          (records.size() > 1) ? RECORDS_ZIJN_VERWIJDERD : RECORD_IS_VERWIJDERD,
          Message.TYPE_SUCCESS);

      close();
    }
  }
}
