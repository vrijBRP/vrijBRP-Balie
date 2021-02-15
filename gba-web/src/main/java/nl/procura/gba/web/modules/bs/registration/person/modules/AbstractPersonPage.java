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

package nl.procura.gba.web.modules.bs.registration.person.modules;

import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class AbstractPersonPage extends NormalPageTemplate {

  private final DossierPersoon           person;
  private final Consumer<DossierPersoon> addPersonListener;

  protected AbstractPersonPage(DossierPersoon person, Consumer<DossierPersoon> addPersonListener) {
    this.person = person;
    this.addPersonListener = addPersonListener;
    super.setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event instanceof LoadPage) {
      loadPage();
    }
    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  protected void loadPage() {
  }

  /**
   * Called when next page button is pressed. Should only be overridden when a page has sub-pages.
   */
  public void onNextPage(Runnable next) {
    next.run();
  }

  /**
   * Check page and when valid, run next runnable.
   */
  public abstract void checkPage(Runnable next);

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  public DossierPersoon getPerson() {
    return person;
  }

  protected Consumer<DossierPersoon> getAddPersonListener() {
    return addPersonListener;
  }
}
