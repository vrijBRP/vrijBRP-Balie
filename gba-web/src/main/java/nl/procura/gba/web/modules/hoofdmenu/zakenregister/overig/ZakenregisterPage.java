/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenTab;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class ZakenregisterPage<T extends Zaak> extends NormalPageTemplate {

  private Zaak zaak;

  public ZakenregisterPage(T zaak, String title) {
    super(title);
    this.zaak = zaak;
    setMargin(true);
  }

  @SuppressWarnings("unchecked")
  public T getZaak() {
    return (T) zaak;
  }

  public void setZaak(T zaak) {
    this.zaak = zaak;
  }

  /**
   * Ga terug naar de vorige pagina. Tenzij er geen pagina's zijn.
   */
  @Override
  public void onPreviousPage() {
    if (buttonPrev.getParent() != null) {
      if (getNavigation().getPreviousPage() != null) {
        getNavigation().goBackToPreviousPage();
      } else {
        getNavigation().goToPage(Page4ZakenTab.class);
        getNavigation().removeOtherPages();
      }
    }
  }
}
