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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean3.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;

public class Page30OntbindingForm3 extends GbaForm<Page30OntbindingBean3> {

  public Page30OntbindingForm3() {

    setCaption("Gegevens ontvangen document(en)");
    setColumnWidths("200px", "");
    setOrder(DATUM_VERKLARING, ONDERTEKEND_DOOR, DATUM_ONDERTEKENING);
  }

  public void setBean(DossierOntbinding zaakDossier) {
    Page30OntbindingBean3 bean = new Page30OntbindingBean3();
    bean.setDatumVerklaring(zaakDossier.getDatumVerklaring().getDate());
    bean.setOndertekendDoor(zaakDossier.getOndertekendDoor());
    bean.setDatumOndertekening(zaakDossier.getDatumOndertekening().getDate());
    setBean(bean);
  }

  @Override
  public Page30OntbindingBean3 getNewBean() {
    return new Page30OntbindingBean3();
  }
}
