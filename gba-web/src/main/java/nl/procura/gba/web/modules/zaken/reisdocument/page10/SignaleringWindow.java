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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringBean.*;

import nl.procura.burgerzaken.vrsclient.model.Signalering;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;

public class SignaleringWindow extends GbaModalWindow {

  public SignaleringWindow(SignaleringResult signaleringResult) {
    setCaption(signaleringResult.getDescription() + " (Esc om te sluiten)");
    setWidth(1000, UNITS_PIXELS);
    VLayout layout = new VLayout()
        .margin(true)
        .spacing(true);
    signaleringResult.getNote()
        .ifPresent(note -> layout.addComponent(new InfoLayout("Er is sprake van een signalering", note)));
    int nr = 1;
    for (Signalering signalering : signaleringResult.getSignaleringen()) {
      ReadOnlyForm<SignaleringBean> form = new ReadOnlyForm<SignaleringBean>() {

        @Override
        public SignaleringBean getNewBean() {
          return new SignaleringBean(signalering);
        }
      };
      form.setOrder(REGISTRATIEDATUM, ARTIKELEN, INSTANTIE, ZAAKNUMMER, CONTACTPERSOON, TELEFOON, EMAIL);
      form.setCaption(new StringBuilder()
          .append("Signalering ")
          .append(nr++)
          .append(" van ")
          .append(signaleringResult.getSignaleringen().size()).toString());
      layout.addComponent(form);
    }

    ScrollLayout scrollLayout = new ScrollLayout(layout);
    scrollLayout.setSizeUndefined();
    setContent(scrollLayout);
  }

  @Override
  public void attach() {
    super.attach();
    bringToFront();
  }
}
