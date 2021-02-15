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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.TAAK;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.ZAAK_ID;

import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaak;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakVraag;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBetreftForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenPage;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public abstract class BsmVerwerkenPage extends BsmUitvoerenPage {

  private final Zaak zaak;

  public BsmVerwerkenPage(Zaak zaak) {
    this.zaak = zaak;
    getProgressBean().setZaakId(zaak.getZaakId());
  }

  @Override
  public GbaForm getBetreftForm() {
    return new BsmUitvoerenBetreftForm(TAAK, ZAAK_ID);
  }

  @Override
  public BsmRestTaak getBsmRestTaak() {
    return getApplication().getServices().getBsmService().getBsmRestTaak(zaak);
  }

  @Override
  public BsmRestTaakVraag getBsmVraag() {
    BsmRestTaakVraag bsmRestVraag = new BsmRestTaakVraag(getBsmRestTaak().getTaak());
    bsmRestVraag.addParameter(GbaRestElementType.ZAAKID, zaak.getZaakId());
    return bsmRestVraag;
  }
}
