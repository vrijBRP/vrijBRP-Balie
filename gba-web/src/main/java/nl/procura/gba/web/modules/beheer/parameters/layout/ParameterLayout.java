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

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.gebruikers.page4.Page4Gebruikers;
import nl.procura.gba.web.modules.beheer.profielen.page11.Page11Profielen;
import nl.procura.vaadin.functies.VaadinUtils;

public class ParameterLayout<T extends GbaForm> extends NormalPageTemplate {

  private GbaApplication gbaApplication = null;
  private GbaForm        form           = null;

  public ParameterLayout(GbaApplication gbaApplication, String title) {
    super(title);

    setGbaApplication(gbaApplication);
    setSpacing(true);
  }

  public T getForm() {
    return (T) form;
  }

  public void setForm(T form) {
    this.form = form;
  }

  public GbaApplication getGbaApplication() {
    return gbaApplication;
  }

  public void setGbaApplication(GbaApplication gbaApplication) {
    this.gbaApplication = gbaApplication;
  }

  protected boolean isModuleGebruiker() {
    return VaadinUtils.getChild(getWindow(), Page4Gebruikers.class) != null;
  }

  protected boolean isModuleProfiel() {
    return VaadinUtils.getChild(getWindow(), Page11Profielen.class) != null;
  }

  protected boolean isModuleAlgemeen() {
    return !isModuleGebruiker() && !isModuleProfiel();
  }

  protected boolean isPreviousButton() {
    return (isModuleGebruiker() || isModuleProfiel());
  }
}
