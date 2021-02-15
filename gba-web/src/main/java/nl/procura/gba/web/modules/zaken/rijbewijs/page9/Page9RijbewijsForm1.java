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

package nl.procura.gba.web.modules.zaken.rijbewijs.page9;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page9.Page9RijbewijsBean1.SPOED;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page9.Page9RijbewijsBean1.STATUS;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;

public class Page9RijbewijsForm1 extends GbaForm<Page9RijbewijsBean1> {

  private final RijbewijsAanvraag aanvraag;

  public Page9RijbewijsForm1(RijbewijsAanvraag aanvraag) {
    this.aanvraag = aanvraag;

    setReadonlyAsText(false);
    setOrder(SPOED, STATUS);

    setBean(new Page9RijbewijsBean1(aanvraag));
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    // Bij omwisseling niet te wijzigen
    boolean isOmwisseling = aanvraag.getSoortAanvraag()
        .getCode() >= RijbewijsAanvraagSoort.OMWISSELING_BUITENLANDS_RIJBEWIJS.getCode();

    getField(SPOED).setEnabled(!isOmwisseling);
  }
}
