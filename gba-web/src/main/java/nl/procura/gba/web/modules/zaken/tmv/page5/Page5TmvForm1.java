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

package nl.procura.gba.web.modules.zaken.tmv.page5;

import static nl.procura.gba.web.modules.zaken.tmv.page5.Page5TmvBean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class Page5TmvForm1 extends GbaForm<Page5TmvBean1> {

  public Page5TmvForm1(TerugmeldingAanvraag tmv) {

    setCaption("Afhandeling");
    setColumnWidths(WIDTH_130, "");
    setOrder(MELDING, VERANTWOORDELIJKE, RAPPELDATUM, RESULTAAT, STATUS);
    setReadonlyAsText(false);

    Page5TmvBean1 bean = new Page5TmvBean1();
    bean.setMelding(tmv.getTerugmelding());
    bean.setVerantwoordelijke(tmv.getVerantwoordelijke());
    bean.setRappeldatum(new DateFieldValue(tmv.getDatumRappel().getLongDate()));
    bean.setResultaat(tmv.getResultaat());
    bean.setStatus(tmv.getStatus());
    setBean(bean);
  }
}
