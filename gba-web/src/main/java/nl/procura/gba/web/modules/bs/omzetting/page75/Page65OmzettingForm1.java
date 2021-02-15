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

package nl.procura.gba.web.modules.bs.omzetting.page75;

import static nl.procura.gba.web.modules.bs.omzetting.page75.Page65OmzettingBean1.DATUM_TIJD_VERBINTENIS;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;

public class Page65OmzettingForm1 extends GbaForm<Page65OmzettingBean1> {

  public Page65OmzettingForm1(DossierOmzetting zaakDossier) {

    setReadonlyAsText(true);

    setOrder(DATUM_TIJD_VERBINTENIS);
    setCaption("Voornemen");
    setColumnWidths("160px", "");

    Page65OmzettingBean1 bean = new Page65OmzettingBean1();
    bean.setDatumTijdVerbintenis(zaakDossier.getDatumVerbintenis().toString());
    setBean(bean);
  }

  @Override
  public Page65OmzettingBean1 getNewBean() {
    return new Page65OmzettingBean1();
  }
}
