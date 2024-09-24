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

package nl.procura.gba.web.components.layouts.page;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Button;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.ShortCutMenu;
import nl.procura.gba.web.components.layouts.page.buttons.ActieButton;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class NormalPageTemplate extends ButtonPageTemplate {

  private MainButtons mainbuttons = null;

  public NormalPageTemplate() {
    this("");
  }

  public NormalPageTemplate(String title) {
    setTitle(title);
    setMainbuttons(new MainButtons(getTitle()));
    if (fil(getTitle())) {
      addComponent(getMainbuttons());
    }
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      initPage();
    }

    super.event(event);
  }

  protected void initPage() {
    if (isTru(getServices().getParameterService().getParm(ParameterConstant.TEST))) {
      getMainbuttons().addSubTitel(getClass().getSimpleName());
    }

    checkButtons();
  }

  public MainButtons getMainbuttons() {
    return mainbuttons;
  }

  public void setMainbuttons(MainButtons mainbuttons) {
    this.mainbuttons = mainbuttons;
  }

  @Override
  public Services getServices() {
    return getApplication().getServices();
  }

  @Override
  public void onMenu() {
    ShortCutMenu.handleEvent(getApplication());
  }

  @Override
  public void onHome() {
    getApplication().onHome();
  }

  /**
   * Controleert of de button is toegestaan.
   */
  private <T> T checkButton(T component) {

    if (component instanceof Button) {

      Button button = (Button) component;
      ProfielActieType actieType = getActieType(button);

      if (actieType != null) { // Heeft button een annotatie (updaten / verwijderen)
        if (button.getParent() != null) { // Is button toegevoegd aan parent
          ModuleTemplate template = VaadinUtils.getParent(this, ModuleTemplate.class);

          if (template != null) { // Heeft pagina een module
            ModuleAnnotation moduleAnnotation = template.getClass().getAnnotation(ModuleAnnotation.class);

            if (moduleAnnotation != null) { // Heeft module een annotatie
              ProfielActie profielActie = moduleAnnotation.profielActie();
              getApplication().getServices().getGebruiker().getProfielen().isProfielActie(profielActie);

              if (profielActie != ProfielActie.ONBEKEND) { // Bestaat de profielactie
                ProfielActie nieuwActie = ProfielActie.getActie(actieType, profielActie);

                if (nieuwActie != null && !getApplication().isProfielActie(nieuwActie)) {
                  button.setEnabled(false);
                }
              }
            }
          }
        }
      }
    } else {
      throw new ProException(ProExceptionSeverity.WARNING, "Component '{0}' is geen button.", component);
    }

    return component;
  }

  /**
   * Controleert of delete / new / save buttons wel aan moet staan op basis van de module annotatie
   */
  private void checkButtons() {

    for (Button button : VaadinUtils.getChildrenBF(this, Button.class)) {
      checkButton(button);
    }
  }

  /**
   * Valt deze button onder een update of verwijder actie.
   */
  private ProfielActieType getActieType(Button button) {

    if (button instanceof ActieButton) {
      return ((ActieButton) button).getProfielAkteType();
    }

    return null;
  }
}
