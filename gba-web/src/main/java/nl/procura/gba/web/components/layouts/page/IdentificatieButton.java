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

import nl.procura.gba.web.modules.zaken.identificatie.IdentificatieWindow;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieService;

public class IdentificatieButton extends MainButton<IdentificatieService> {

  public IdentificatieButton() {
    setCaption("Identificatie");
    setWidth("90px");
  }

  @Override
  protected void doCheck() {
    setStyleName("link buttonlink indicatie");
    if (getApplication() != null) {
      if (getService().isVastGesteld(getServices().getPersonenWsService().getHuidige())) {
        setDescription("Van deze PL is de identiteit vastgesteld.");
        addStyleName("yes");
      } else {
        setDescription("Van deze PL is de identiteit nog niet vastgesteld.");
        addStyleName("no");
      }
    }
  }

  @Override
  protected String getListenerId() {
    return "idListener";
  }

  @Override
  protected IdentificatieService getService() {
    return getServices().getIdentificatieService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new IdentificatieWindow());
  }
}
