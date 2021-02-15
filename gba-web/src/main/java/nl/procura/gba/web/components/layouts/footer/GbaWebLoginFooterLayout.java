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

package nl.procura.gba.web.components.layouts.footer;

import static nl.procura.gba.common.MiscUtils.getBuildText;
import static nl.procura.gba.common.MiscUtils.getCopyright;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordlost.PasswordLostWindow;
import nl.procura.gba.web.services.beheer.email.EmailType;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.label.Ruler;

public class GbaWebLoginFooterLayout extends GbaWebFooterTemplate {

  public GbaWebLoginFooterLayout() {
    setMargin(true);
  }

  @Override
  public void attach() {

    if (getComponentCount() == 0) {

      VerticalLayout verticalLayout = new VerticalLayout();
      verticalLayout.setSpacing(true);

      if (getApplication().getServices().getEmailService().isGeactiveerd(EmailType.WACHTWOORD_VERGETEN)) {

        Button wachtwoordButton = new Button("Wachtwoord vergeten?",
            (ClickListener) event -> getWindow().addWindow(new PasswordLostWindow()));

        wachtwoordButton.setStyleName(GbaWebTheme.BUTTON_LINK);

        verticalLayout.setWidth("90%");
        verticalLayout.setStyleName("wachtwoordlayout");
        verticalLayout.addComponent(wachtwoordButton);
        verticalLayout.setComponentAlignment(wachtwoordButton, Alignment.TOP_CENTER);
        verticalLayout.addComponent(new Ruler());
      }

      Label label = new Label(String.format("%s<br/>%s", getBuildText(), getCopyright()), Label.CONTENT_XHTML);
      label.setWidth("100%");
      verticalLayout.addComponent(label);

      if (isTestOmgeving()) {
        Component warning = getTestOmgevingWarning();
        verticalLayout.addComponent(warning);
        verticalLayout.setComponentAlignment(warning, Alignment.MIDDLE_CENTER);
      }

      addComponent(verticalLayout);
      setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    super.attach();
  }
}
