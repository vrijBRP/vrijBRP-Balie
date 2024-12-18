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

package nl.procura.gba.web.modules.zaken.correspondentie.page3;

import static nl.procura.gba.web.modules.zaken.correspondentie.page3.Page3CorrespondentieBean.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;

public class Page3CorrespondentieForm extends ReadOnlyForm {

  public Page3CorrespondentieForm(CorrespondentieZaak zaak) {

    setCaption("Gegevens correspondentie");
    setOrder(OMSCHRIJVING, AFSLUIT_TYPE, TOELICHTING);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    Page3CorrespondentieBean b = new Page3CorrespondentieBean();

    b.setOmschrijving(zaak.getOmschrijving());
    b.setAfsluitType(astr(zaak.getAfsluitType()));
    b.setToelichting(zaak.getToelichting());

    setBean(b);
  }
}
