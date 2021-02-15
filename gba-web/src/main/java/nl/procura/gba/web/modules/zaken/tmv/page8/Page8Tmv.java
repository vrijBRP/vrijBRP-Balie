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

package nl.procura.gba.web.modules.zaken.tmv.page8;

import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvTabPage;
import nl.procura.gba.web.modules.zaken.tmv.layouts.reacties.TmvReactieLayout;
import nl.procura.gba.web.modules.zaken.tmv.page8.Page8TmvForm1.Bean;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingReactie;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page8Tmv extends TmvTabPage {

  private Page8TmvForm1 form = null;

  public Page8Tmv(TerugmeldingAanvraag tmv) {

    this(tmv, new TerugmeldingReactie());
  }

  public Page8Tmv(TerugmeldingAanvraag tmv, TerugmeldingReactie tmvR) {

    super(tmv);
    setMargin(false);

    addButton(buttonPrev);
    addButton(buttonSave);

    form = new Page8TmvForm1(tmv, tmvR);

    addComponent(form);
  }

  @Override
  public void onSave() {

    form.commit();

    TerugmeldingReactie rImpl = form.getTmvReactie();
    Bean b = form.getBean();

    rImpl.setDatumTijdInvoer(b.getDatumTijd());
    rImpl.setIngevoerdDoor(b.getGebruiker());
    rImpl.setTerugmReactie(b.getReactie());

    boolean isNieuw = !rImpl.isStored();

    getServices().getTerugmeldingService().saveReactie(getTmv(), rImpl);

    if (isNieuw) {
      getTmv().getReacties().add(rImpl);
    }

    new Message(getWindow(), "Reactie is opgeslagen.", Message.TYPE_SUCCESS);

    VaadinUtils.getParent(Page8Tmv.this, TmvReactieLayout.class).onReload();

    onPreviousPage();

    super.onSave();
  }
}
