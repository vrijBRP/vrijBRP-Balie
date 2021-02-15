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

package nl.procura.gba.web.modules.bs.huwelijk.page75;

import static nl.procura.gba.web.modules.bs.huwelijk.page75.Page65HuwelijkBean1.DATUM_TIJD_VERBINTENIS;
import static nl.procura.gba.web.modules.bs.huwelijk.page75.Page65HuwelijkBean1.DATUM_VOORNEMEN;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;

public class Page65HuwelijkForm1 extends GbaForm<Page65HuwelijkBean1> {

  public Page65HuwelijkForm1(DossierHuwelijk zaakDossier) {

    setReadonlyAsText(true);
    setOrder(DATUM_VOORNEMEN, DATUM_TIJD_VERBINTENIS);
    setCaption("Voornemen");
    setColumnWidths("160px", "");

    Page65HuwelijkBean1 bean = new Page65HuwelijkBean1();
    bean.setDatumVoornemen(zaakDossier.getDatumVoornemen().getDate());
    bean.setDatumTijdVerbintenis(zaakDossier.getDatumVerbintenis().toString());

    setBean(bean);
  }

  @Override
  public Page65HuwelijkBean1 getNewBean() {
    return new Page65HuwelijkBean1();
  }
}
