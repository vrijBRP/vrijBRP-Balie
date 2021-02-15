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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import static nl.procura.gba.web.modules.zaken.verhuizing.page2.Page2VerhuizingBean2.ADRES;
import static nl.procura.gba.web.modules.zaken.verhuizing.page2.Page2VerhuizingBean2.DUUR;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class Page2VerhuizingForm2 extends ReadOnlyForm {

  public Page2VerhuizingForm2(VerhuisAanvraag aanvraag) {

    setCaption("Emigratiegegevens");
    setOrder(ADRES, DUUR);
    setColumnWidths(WIDTH_130, "");

    Page2VerhuizingBean2 b = new Page2VerhuizingBean2();
    b.setAdres(aanvraag.getEmigratie().getAdres());
    b.setDuur(aanvraag.getEmigratie().getDuur());

    setBean(b);
  }
}
