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

package nl.procura.gba.web.windows.home.navigatie;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.modules.hoofdmenu.bsm.ModuleBsm;
import nl.procura.gba.web.modules.hoofdmenu.kassa.ModuleKassa;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.ModuleZoeken;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakNumbers;

public class HoofdAccordionTab extends GbaAccordionTab {

  public HoofdAccordionTab(GbaApplication application) {

    super("Hoofdmenu", application);

    addLink(ModuleZoeken.class);
    addLink(ModuleKassa.class);
    addLink(ModuleBsm.class);
  }

  @Override
  protected String getCount(ZaakType[] zaakTypes, ZaakNumbers zdb) {

    ZaakArgumenten z = new ZaakArgumenten(ZaakStatusType.INCOMPLEET, ZaakStatusType.OPGENOMEN);
    z.addTypen(zaakTypes);

    int count = zdb.getZakenCount(z);
    return (count > 0) ? ("<b>" + count + "</b>") : "0";
  }
}
