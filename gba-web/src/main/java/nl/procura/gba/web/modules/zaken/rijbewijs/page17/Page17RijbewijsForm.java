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

package nl.procura.gba.web.modules.zaken.rijbewijs.page17;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page17.Page17RijbewijsBean1.IND_BEZORGEN;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page17.Page17RijbewijsBean1.OPM_BEZORGEN;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page17RijbewijsForm extends GbaForm<Page17RijbewijsBean1> {

  public Page17RijbewijsForm() {
    setCaption("Thuisbezorgen");
    setOrder(IND_BEZORGEN, OPM_BEZORGEN);
    setReadThrough(true);
    setReadonlyAsText(false);
    setBean(new Page17RijbewijsBean1());
  }
}
