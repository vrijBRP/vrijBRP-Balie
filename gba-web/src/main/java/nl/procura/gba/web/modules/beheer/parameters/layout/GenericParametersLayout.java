/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.vaadin.functies.VaadinUtils.getChild;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.parameters.ModuleParameters;
import nl.procura.gba.web.modules.beheer.parameters.layout.importexport.ImportExportDialog;
import nl.procura.gba.web.modules.beheer.parameters.layout.importexport.Page1ParametersPopup;
import nl.procura.gba.web.modules.beheer.parameters.page1.Page1Parameters;
import nl.procura.vaadin.component.layout.page.PageNavigation;
import nl.procura.vaadin.functies.VaadinUtils;

public class GenericParametersLayout extends DatabaseParameterLayout {

  private final Page1ParametersPopup opties;

  public GenericParametersLayout(GbaApplication gbaApplication, String naam, String category) {
    super(gbaApplication, naam, category);
    Page1ParametersPopup popup = new Page1ParametersPopup(() -> importExport(true), () -> importExport(false));
    opties = VaadinUtils.addOrReplaceComponent(getButtonLayout(), popup);
    getButtonLayout().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);
  }

  private void importExport(boolean doImport) {
    ImportExportDialog window = new ImportExportDialog(doImport);
    window.addListener((CloseListener) closeEvent -> {
      ModuleParameters moduleParameters = getChild(getApplication().getParentWindow(), ModuleParameters.class);
      PageNavigation navigation = moduleParameters.getPages().getNavigation();
      navigation.removePage(Page1Parameters.class);
      navigation.goToPage(Page1Parameters.class);
    });
    getParentWindow().addWindow(window);
  }

  @Override
  public void attach() {
    opties.setVisible(isModuleAlgemeen());
    super.attach();
  }
}
