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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST_OMGEVING;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class GbaWebFooterTemplate extends VerticalLayout {

  public GbaWebFooterTemplate() {
    setWidth("100%");
    setStyleName(GbaWebTheme.LAYOUT_FOOTER);
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  protected Label getTestOmgevingWarning() {

    Label label = new Label("Testomgeving");
    label.setStyleName("footerwarning");
    label.setDescription("Dit is de testomgeving van deze applicatie");
    label.setSizeUndefined();

    return label;
  }

  protected boolean isTestOmgeving() {

    ParameterService parameters = getApplication().getServices().getParameterService();
    return isTru(parameters.getGebruikerParameters(Gebruiker.getDefault()).get(TEST_OMGEVING).getValue());
  }
}
