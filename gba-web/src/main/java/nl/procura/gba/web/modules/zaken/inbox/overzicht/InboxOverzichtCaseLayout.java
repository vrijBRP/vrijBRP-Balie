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

package nl.procura.gba.web.modules.zaken.inbox.overzicht;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.ColumnType;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

import lombok.Builder;

/**
 * Layout for inbox cases
 */
public class InboxOverzichtCaseLayout {

  private final Consumer<String>    idListener;
  private final int                 numberOfColumns;
  private final Map<String, Object> data;

  @Builder
  public InboxOverzichtCaseLayout(Consumer<String> idListener, int numberOfColumns, Map<String, Object> data) {
    this.idListener = idListener;
    this.numberOfColumns = numberOfColumns;
    this.data = data;
  }

  public TableLayout getLayout() {
    TableLayout layout = new TableLayout();
    layout.setColumnWidths("");
    if (numberOfColumns > 1) {
      layout.setColumnWidths("140px", "");
      if (data.size() > 8) {
        layout.setColumnWidths("140px", "400px", "140px", "");
        int i = 1;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
          layout.addLabel(entry.getKey());
          // Add empty column for final odd row
          if (i % 2 == 1 && i == data.size()) {
            layout.addColumn(ColumnType.DATA, 3).addComponent(getDataComponent(astr(entry.getValue())));
          } else {
            layout.addData(getDataComponent(astr(entry.getValue())));
          }
          i++;
        }
        return layout;
      }
    }

    layout.addStyleName(ProcuraTheme.AUTOSCROLL);
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      layout.addLabel(entry.getKey());
      layout.addData(getDataComponent(astr(entry.getValue())));
    }
    return layout;
  }

  private Component getDataComponent(String value) {
    Label data = new Label(value);
    if (value.length() > 6 && (Bsn.isCorrect(value) || Anr.isCorrect(value))) {
      Button idButton = new NativeButton("Zoek", event -> idListener.accept(value));
      idButton.setStyleName(GbaWebTheme.BUTTON_SMALL);
      HLayout dataLayout = new HLayout(data, idButton).widthFull().expand(data, 1F).margin(false);
      dataLayout.setComponentAlignment(idButton, Alignment.MIDDLE_CENTER);
      return dataLayout;
    }
    return new Label(value);
  }
}
