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

package nl.procura.gba.web.components.layouts.form.document.email.page1.keuze;

import nl.procura.gba.web.common.misc.email.EmailAddress;
import nl.procura.gba.web.components.layouts.form.document.email.page1.EmailPreviewContainer;
import nl.procura.gba.web.components.layouts.form.document.email.page1.keuze.page1.Page1EmailKeuze;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class PrintEmailKeuzeWindow extends GbaModalWindow {

  private final EmailPreviewContainer container;
  private final EmailAddress          emailAdres;

  public PrintEmailKeuzeWindow(EmailPreviewContainer container, EmailAddress emailAdres) {

    this.container = container;
    this.emailAdres = emailAdres;

    setCaption("E-mailadressen");
    setWidth("700px");
    setHeight("285px");
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(new Page1EmailKeuze(container, emailAdres));
  }
}
