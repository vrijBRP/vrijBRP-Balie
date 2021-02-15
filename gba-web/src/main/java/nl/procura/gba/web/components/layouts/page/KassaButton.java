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

import nl.procura.gba.web.modules.zaken.kassa.WindowKassa;
import nl.procura.gba.web.services.beheer.kassa.KassaService;

public class KassaButton extends MainButton<KassaService> {

  @Override
  protected void doCheck() {
    setStyleName("link buttonlink indicatie");
    if (getApplication() != null) {
      int count = getService().getProductenInWinkelwagen().size();
      setCaption(count > 1 ? "Kassa (" + count + ")" : "Kassa");
      addStyleName(count > 0 ? "warn" : "no");
    }
  }

  @Override
  protected String getListenerId() {
    return "kassaListener";
  }

  @Override
  protected KassaService getService() {
    return getApplication().getServices().getKassaService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new WindowKassa());
  }
}
