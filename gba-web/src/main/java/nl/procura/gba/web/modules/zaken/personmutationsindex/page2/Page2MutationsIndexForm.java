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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page2;

import static nl.procura.gba.web.modules.zaken.personmutationsindex.page2.Page2MutationsIndexBean.*;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationRestElement;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page2MutationsIndexForm extends GbaForm<Page2MutationsIndexBean> {

  public Page2MutationsIndexForm(MutationRestElement mut) {
    setCaption("Zoeken");
    setColumnWidths(WIDTH_130, "");
    setOrder(NAAM, ANR, DATUM_TIJD_MUTATIE, CAT, STATUS, DATUM_TIJD_GOEDKEURING);

    Page2MutationsIndexBean bean = new Page2MutationsIndexBean();
    bean.setNaam(mut.getName().getWaarde());
    bean.setAnr(mut.getAnr().getOmschrijving());
    bean.setDatumTijdMutatie(getWaarde(mut.getDateMutation(), mut.getTimeMutation()));
    bean.setCat(getWaardeOms(mut.getCat()));
    bean.setStatus(getWaardeOms(mut.getStatusMutation()));
    bean.setDatumTijdGoedkeuring(mut.getDateApproval().map(BsmRestElement::getOmschrijving).orElse("") + " "
        + mut.getTimeApproval().map(BsmRestElement::getOmschrijving).orElse(""));
    setBean(bean);
  }

  private String getWaarde(BsmRestElement e1, BsmRestElement e2) {
    return e1.getOmschrijving() + " " + e2.getOmschrijving();
  }

  private String getWaardeOms(BsmRestElement e) {
    return e.getWaarde() + " - " + e.getOmschrijving();
  }
}
