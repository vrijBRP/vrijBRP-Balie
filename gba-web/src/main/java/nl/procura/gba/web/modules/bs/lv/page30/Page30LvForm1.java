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

package nl.procura.gba.web.modules.bs.lv.page30;

import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean1.DATUM;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean1.SOORT;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page30LvForm1 extends GbaForm<Page30LvBean1> {

  public Page30LvForm1() {
    setCaption("Soort");
    setColumnWidths("220px", "");
    setOrder(SOORT, DATUM);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
  }

  @Override
  public Page30LvBean1 getNewBean() {
    return new Page30LvBean1();
  }
}
