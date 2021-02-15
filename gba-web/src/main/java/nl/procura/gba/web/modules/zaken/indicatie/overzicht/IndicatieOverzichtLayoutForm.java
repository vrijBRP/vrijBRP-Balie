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

package nl.procura.gba.web.modules.zaken.indicatie.overzicht;

import static nl.procura.gba.web.modules.zaken.indicatie.overzicht.IndicatieOverzichtLayoutBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieAanvraag;

public class IndicatieOverzichtLayoutForm extends GbaForm<IndicatieOverzichtLayoutBean> {

  public IndicatieOverzichtLayoutForm(IndicatieAanvraag aanvraag) {

    setCaption("Overige gegevens");
    setOrder(ACTIE, INDICATIE, TOELICHTING);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    IndicatieOverzichtLayoutBean b = new IndicatieOverzichtLayoutBean();
    b.setActie(aanvraag.getActie().getOmschrijving());
    b.setIndicatie(aanvraag.getIndicatie().getOmschrijving());
    b.setToelichting(aanvraag.getInhoud());

    setBean(b);
  }
}
