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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.search.window;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.component.layout.table.TableLayout.ColumnType;

public class BvBsnWindow extends ModalWindow {

  public BvBsnWindow(String title, BvBsnActionTemplate actie) {

    if (!actie.getVerwerking().isSucces()) {
      title = title + " (Actie mislukt)";
    }

    setCaption(title);
    setWidth("500px");
    setCloseShortcut(KeyCode.ESCAPE, null);

    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setWidth("100%");
    hLayout.setSpacing(true);
    hLayout.addComponent(BUTTON_CLOSE);
    hLayout.setComponentAlignment(BUTTON_CLOSE, Alignment.MIDDLE_RIGHT);

    addComponent(hLayout);

    parseOutputMessage(actie.getOutputMessage());
  }

  private void parseOutputMessage(String s) {

    TableLayout fl = new TableLayout();
    String title = "";

    for (String line : s.split("\n")) {

      String[] p = line.split("\\|");

      if (p.length == 3) {

        String type = p[0];
        String label = p[1];
        String val = p[2];

        if (!title.equals(type)) {
          title = type;

          fl = new TableLayout(title);
          fl.setColumnWidths("100px", "");
          addComponent(fl);
        }

        Column col = fl.addColumn(ColumnType.LABEL);
        col.addText(label);

        col = fl.addColumn(ColumnType.DATA);
        col.addText(val);
      }
    }
  }
}
