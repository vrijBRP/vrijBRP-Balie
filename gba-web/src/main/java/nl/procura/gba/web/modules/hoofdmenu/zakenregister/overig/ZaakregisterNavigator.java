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

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.zaken.common.ZaakPageIndex;
import nl.procura.gba.web.modules.zaken.common.ZaakWindow;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.layout.page.PageNavigation;

public class ZaakregisterNavigator {

  public static void navigatoTo(Zaak zaak, GbaPageTemplate page, boolean resetNavigatie) {
    if (fil(zaak.getZaakId())) {
      ZakenService zaken = page.getApplication().getServices().getZakenService();

      List<Zaak> minimaleZaken = zaken.getMinimaleZaken(new ZaakArgumenten(zaak));
      for (Zaak vollZaak : zaken.getVolledigeZaken(minimaleZaken)) {
        GbaPageTemplate selectPage = ZaakPageIndex.selectPage(vollZaak);

        if (page.getWindow() instanceof HomeWindow) {
          PageNavigation nav = page.getParentLayout().getNavigation();
          nav.goToPage(selectPage);

          if (resetNavigatie) {
            nav.removeOtherPages();
          }

        } else {
          page.getParentWindow().addWindow(new ZaakWindow(vollZaak));
        }
        break;
      }
    }
  }
}
