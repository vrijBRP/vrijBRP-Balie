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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page14;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.ZaakAttribuutPage;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumentenBuilder;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page14Zaken extends ZaakAttribuutPage {

  public Page14Zaken() {
    super("Zakenregister - onbehandelde zaken",
        new InfoLayout("", "Openstaande zaken zonder een behandelaar."));
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return ZaakArgumentenBuilder.nieuweZaken(getServices());
  }
}
