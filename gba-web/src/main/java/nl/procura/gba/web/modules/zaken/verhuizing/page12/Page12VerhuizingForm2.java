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

package nl.procura.gba.web.modules.zaken.verhuizing.page12;

import static nl.procura.gba.web.modules.zaken.verhuizing.page12.Page12VerhuizingBean2.BSN;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page12VerhuizingForm2 extends GbaForm<Page12VerhuizingBean2> {

  public Page12VerhuizingForm2() {

    setReadonlyAsText(true);
    setCaption("Zoek adres aan de hand van persoonsgegevens.");
    setOrder(BSN);
    setColumnWidths(WIDTH_130, "");

    setBean(new Page12VerhuizingBean2());
  }
}
