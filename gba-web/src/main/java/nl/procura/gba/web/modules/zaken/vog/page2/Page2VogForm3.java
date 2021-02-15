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

package nl.procura.gba.web.modules.zaken.vog.page2;

import static nl.procura.gba.web.modules.zaken.vog.page2.Page2VogBean3.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagDoel;

public class Page2VogForm3 extends Page2VogForm {

  public Page2VogForm3(VogAanvraag aanvraag) {

    setCaption("Doel");
    setOrder(DOEL, FUNCTIE, OMSCHRIJVING);
    setColumnWidths(WIDTH_130, "");

    Page2VogBean3 b = new Page2VogBean3();

    VogAanvraagDoel d = aanvraag.getDoel();

    b.setDoel(astr(d.getDoel().getCVogDoelTab()) + " - " + astr(d.getDoel().getOms()));
    b.setFunctie(d.getFunctie());
    b.setOmschrijving(d.getDoelTekst());

    setBean(b);
  }
}
