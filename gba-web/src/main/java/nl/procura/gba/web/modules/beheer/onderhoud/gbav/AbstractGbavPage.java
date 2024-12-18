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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class AbstractGbavPage extends NormalPageTemplate {

  private PersonenWsService personenWsService;

  @Override
  public void attach() {
    super.attach();
    personenWsService = getApplication().getServices().getPersonenWsService();
  }

  @Override
  public void event(PageEvent event) {
    setMargin(true);
    super.event(event);
  }

  public PersonenWsService personenWsService() {
    return personenWsService;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
