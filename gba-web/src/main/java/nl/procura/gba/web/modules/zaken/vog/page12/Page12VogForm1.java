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

package nl.procura.gba.web.modules.zaken.vog.page12;

import static nl.procura.gba.web.modules.zaken.vog.page12.Page12VogBean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class Page12VogForm1 extends GbaForm<Page12VogBean1> {

  public Page12VogForm1(VogAanvraag aanvraag) {

    setReadonlyAsText(true);
    setCaption("Doel van de aanvraag");
    setOrder(DOEL, OMSCHRIJVING, FUNCTIE);
    setColumnWidths(WIDTH_130, "");

    Page12VogBean1 b = new Page12VogBean1();
    b.setDoel(aanvraag.getDoel().getDoel());
    b.setFunctie(aanvraag.getDoel().getFunctie());
    b.setOmschrijving(aanvraag.getDoel().getDoelTekst());

    setBean(b);
  }
}
