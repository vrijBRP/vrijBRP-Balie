/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.types;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;

public class FileImporterDataWindow extends GbaModalWindow {

  public FileImporterDataWindow(FileImportRecord importRecord) {
    super(true);

    setCaption("Gegevens in dit record");
    setWidth("700px");
    setHeight("500px");

    TableLayout layout = new TableLayout();
    layout.setColumnWidths("90px", "");
    importRecord.getValues()
        .forEach((key, value) -> {
          layout.addLabel(key);
          layout.addData(new Label(value.getValue()));
        });

    Button button = new Button("Sluiten (Esc)", (ClickListener) clickEvent -> closeWindow());
    button.focus();

    setContent(new VLayout()
        .spacing(true)
        .margin(true)
        .add(button)
        .addExpandComponent(new ScrollLayout(layout)));
  }
}
