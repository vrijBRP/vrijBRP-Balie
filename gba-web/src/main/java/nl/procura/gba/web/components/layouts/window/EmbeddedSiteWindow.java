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

package nl.procura.gba.web.components.layouts.window;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

public class EmbeddedSiteWindow extends GbaModalWindow {

  private final String url;

  public EmbeddedSiteWindow(String caption, String url) {
    super(caption, "90%");
    setHeight("90%");
    this.url = url;
  }

  @Override
  public void attach() {
    Embedded browser = new Embedded(null, new ExternalResource(url));
    browser.setType(Embedded.TYPE_BROWSER);
    browser.setSizeFull();

    VerticalLayout v = new VerticalLayout();
    v.setSizeFull();
    v.addComponent(browser);
    setContent(v);

    super.attach();
  }
}
