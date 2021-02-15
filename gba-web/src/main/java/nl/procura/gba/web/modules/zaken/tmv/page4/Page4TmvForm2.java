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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;

public class Page4TmvForm2 extends ReadOnlyForm {

  private static final String VERANTWOORDELIJKE = "verantwoordelijke";
  private static final String RAPPELDATUM       = "rappeldatum";
  private static final String STATUS            = "status";
  private static final String RESULTAAT         = "resultaat";

  public Page4TmvForm2(TerugmeldingAanvraag tmv) {

    setCaption("Interne behandeling");
    setOrder(VERANTWOORDELIJKE, RAPPELDATUM, STATUS, RESULTAAT);
    setColumnWidths("200px", "");

    setReadonlyAsText(false);

    Page4TmvBean2 bean = new Page4TmvBean2();

    bean.setVerantwoordelijke(astr(tmv.getVerantwoordelijke()));
    bean.setRappeldatum(tmv.getDatumRappel().toString());

    String status = tmv.getStatus().getOms();

    if (tmv.getStatus().isMinimaal(ZaakStatusType.VERWERKT)) {

      if (pos(tmv.getDatumTijdAfhandeling().getLongDate())) {
        status += (" op " + tmv.getDatumTijdAfhandeling().toString());

        if (pos(tmv.getAfgehandeldDoor().getValue())) {
          status += (" door " + tmv.getAfgehandeldDoor().getDescription());
        }
      }
    }

    bean.setStatus(status);
    bean.setResultaat(tmv.getResultaat());

    setBean(bean);
  }
}
