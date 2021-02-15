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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnection;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab1OnderhoudPage extends OnderhoudTabPage {

  private Tab1VerbindingenTable verbindingenTable;

  public Tab1OnderhoudPage() {
    super("Algemene informatie");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addComponent(new Fieldset("RDW account"));
      setInfo("", "Klik op de regel in de tabel hieronder voor meer informatie");
      addComponent(new Tab1RdwTable());

      addComponent(new Fieldset("GBA-V accounts"));
      setInfo("", "Klik op een regel in de tabel hieronder voor meer informatie");
      addComponent(new Tab1GbavTable());

      addComponent(new Fieldset("Verbindingen"));
      addComponent(new Button("Verbindingen controleren",
          (Button.ClickListener) bEvent -> verbindingenTable.setConnections(getConnections(true))));

      verbindingenTable = new Tab1VerbindingenTable();
      verbindingenTable.setConnections(getConnections(false));
      addExpandComponent(verbindingenTable);
    }

    super.event(event);
  }

  private List<SslRestConnection> getConnections(boolean b) {
    return getApplication()
        .getServices().getOnderhoudService()
        .getSSLConnections(b)
        .getConnections();
  }
}
