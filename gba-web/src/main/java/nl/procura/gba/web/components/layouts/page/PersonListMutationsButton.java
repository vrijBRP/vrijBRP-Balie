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

import nl.procura.gba.web.modules.zaken.personmutations.WindowPersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutationsService;

public class PersonListMutationsButton extends MainButton<PersonListMutationsService> {

  @Override
  protected void doCheck() {
    setStyleName("link buttonlink indicatie");
    if (getApplication() != null) {
      setCaption("PL mutaties");
      addStyleName("no");
    }
  }

  @Override
  protected String getListenerId() {
    return "mutatiesListener";
  }

  @Override
  protected PersonListMutationsService getService() {
    return getApplication().getServices().getPersonListMutationService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new WindowPersonListMutations());
  }
}
