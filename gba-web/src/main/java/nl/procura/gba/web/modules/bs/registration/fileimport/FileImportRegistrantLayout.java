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

package nl.procura.gba.web.modules.bs.registration.fileimport;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportDataWindow;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.theme.ProcuraWindow;

public class FileImportRegistrantLayout extends HLayout {

  public FileImportRegistrantLayout() {
    setWidth("100%");
    setVisible(false);
    setSpacing(true);
    setStyleName("registrant-import-layout");
  }

  public FileImportRegistrantLayout(FileImportRegistrant registrant) {
    this();
    setRegistrant(registrant);
  }

  public void setRegistrant(FileImportRegistrant importRegistrant) {
    setVisible(true);
    removeAllComponents();
    Label label = new Label(importRegistrant.getSummary());
    label.setSizeUndefined();
    addComponent(label);
    Button button = new Button("(Toon alle gegevens)", (ClickListener) clickEvent -> {
      ProcuraWindow parentWindow = ((GbaApplication) getApplication()).getParentWindow();
      parentWindow.addWindow(new FileImportDataWindow(importRegistrant.getType().getImporter()
          .createLayout(importRegistrant.getRecord(), null)));
    });
    button.setStyleName(GbaWebTheme.BUTTON_LINK);
    addComponent(button);
    setExpandRatio(button, 1f);
    setComponentAlignment(label, Alignment.MIDDLE_LEFT);
  }
}
