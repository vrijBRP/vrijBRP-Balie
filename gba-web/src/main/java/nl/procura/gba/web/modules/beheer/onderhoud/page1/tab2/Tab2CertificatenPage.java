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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab2;

import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab2CertificatenPage extends OnderhoudTabPage {

  public Tab2CertificatenPage() {
    super("Certificaten");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addComponent(new Fieldset("Certificaten", new InfoLayout("Dubbelklik op een regel voor meer informatie")));
      addExpandComponent(new Tab2CertificatenTable());
    }

    super.event(event);
  }
}
