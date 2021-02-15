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

package nl.procura.gba.web.modules.zaken.geheim.overzicht;

import static nl.procura.gba.web.modules.zaken.geheim.overzicht.GeheimOverzichtLayoutBean.KEUZE;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;

public class GeheimOverzichtLayoutForm extends ReadOnlyForm {

  public GeheimOverzichtLayoutForm(GeheimAanvraag geheim) {

    setCaption("Overige gegevens");
    setOrder(KEUZE);
    setColumnWidths(WIDTH_130, "");

    GeheimOverzichtLayoutBean b = new GeheimOverzichtLayoutBean();
    b.setKeuze(astr(geheim.getGeheimType().getOms()));
    setBean(b);
  }
}
