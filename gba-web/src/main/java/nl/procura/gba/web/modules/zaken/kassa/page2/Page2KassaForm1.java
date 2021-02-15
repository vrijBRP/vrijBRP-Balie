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

package nl.procura.gba.web.modules.zaken.kassa.page2;

import static nl.procura.gba.web.modules.zaken.kassa.page2.Page2KassaBean1.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;

public class Page2KassaForm1 extends ReadOnlyForm {

  public Page2KassaForm1(BasePLExt pl) {

    setCaption("Persoonsgegevens");

    setColumnWidths("100px", "");
    setOrder(NAAM, ADRES, PC);

    Page2KassaBean1 b = new Page2KassaBean1();

    b.setNaam(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    b.setAdres(pl.getVerblijfplaats().getAdres().getAdres());
    b.setPc(pl.getVerblijfplaats().getAdres().getPcWpl());

    setBean(b);
  }
}
