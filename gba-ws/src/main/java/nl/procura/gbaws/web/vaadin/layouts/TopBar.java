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

package nl.procura.gbaws.web.vaadin.layouts;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import nl.procura.vaadin.theme.twee.layout.TopBarLayout;

@SuppressWarnings("serial")
public class TopBar extends TopBarLayout {

  private final Label name_label = new Label("", Label.CONTENT_XHTML);

  public TopBar() {

    HorizontalLayout l = new HorizontalLayout();
    l.setStyleName("left");
    Label app_label = new Label("Burgerlijke stand");
    app_label.setStyleName("separator");
    l.addComponent(app_label);

    Label gem_label = new Label("Gemeente Procura");
    gem_label.setStyleName("separator");
    l.addComponent(gem_label);

    Label loc_label = new Label("Balie 1");
    l.addComponent(loc_label);

    HorizontalLayout r = new HorizontalLayout();
    r.setStyleName("right");

    name_label.setStyleName("separator");
    r.addComponent(name_label);

    Label msg_label = new Label("U heeft geen <b>nieuwe berichten</b>", Label.CONTENT_XHTML);
    r.addComponent(msg_label);

    addComponent(l);
    addComponent(r);
    setComponentAlignment(l, Alignment.TOP_LEFT);
    setComponentAlignment(r, Alignment.TOP_RIGHT);
  }

  @Override
  public void attach() {

    super.attach();

    name_label.setValue("Hallo: <b>" + getApplication().getUser() + "</b>");
  }
}
