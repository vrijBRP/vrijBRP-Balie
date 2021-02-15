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

package nl.procura.gba.web.modules.zaken.gpk.page4;

import static nl.procura.gba.web.modules.zaken.gpk.page4.Page4GpkBean.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;

public class Page4GpkForm extends ReadOnlyForm {

  public Page4GpkForm(GpkAanvraag aanvraag) {

    setCaption("Overige gegevens");
    setOrder(NUMMER, KAARTTYPE, VERVALDATUM, PRINTDATUM, AFGEVER);
    setColumnWidths(WIDTH_130, "");

    Page4GpkBean b = new Page4GpkBean();

    b.setNummer(aanvraag.getNr());
    b.setKaartType(aanvraag.getKaart().getOms());
    b.setPrintDatum(aanvraag.getDatumPrint().toString());
    b.setVervalDatum(aanvraag.getDatumVerval().toString());
    b.setAfgever(aanvraag.getAfgever());

    setBean(b);
  }
}
