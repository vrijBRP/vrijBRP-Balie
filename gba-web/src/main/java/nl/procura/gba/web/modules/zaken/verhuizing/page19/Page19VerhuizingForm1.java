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

package nl.procura.gba.web.modules.zaken.verhuizing.page19;

import static nl.procura.gba.web.modules.zaken.verhuizing.page19.Page19VerhuizingBean1.*;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page19VerhuizingForm1 extends GbaForm<Page19VerhuizingBean1> {

  public Page19VerhuizingForm1() {

    setCaption("Emigratiegegevens");
    setOrder(AANVANG, ADRES1, ADRES2, ADRES3, LAND, DUUR);
    setColumnWidths("200px", "");
    setBean(new Page19VerhuizingBean1());
  }

  public GbaNativeSelect getDuurVeld() {
    return (GbaNativeSelect) getField(DUUR);
  }
}
