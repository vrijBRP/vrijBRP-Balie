/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.components.layouts;

import java.util.Optional;

import com.vaadin.ui.Component;

import nl.procura.gba.web.modules.hoofdmenu.requestinbox.sidelayout.RequestInboxSideLayout;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.HLayout;

public class MainRightSideLayout extends GbaVerticalLayout {

  private final String id;
  private Component    inboxLayout;

  public MainRightSideLayout(String id) {
    this.id = id;
    setSizeFull();
    setStyleName("contentLayout");
  }

  @Override
  public void attach() {
    if (getComponentCount() == 0) {
      HLayout hLayout = new HLayout();
      hLayout.setStyleName("right-side-layout");
      hLayout.setSpacing(false);
      hLayout.setSizeFull();
      hLayout.addExpandComponent(new MainModuleContainer());
      addSideLayout(hLayout);

      ServiceListener meldingListener = new ServiceListener() {

        @Override
        public void action(ServiceEvent event) {
          addSideLayout(hLayout);
        }

        @Override
        public String getId() {
          return MainRightSideLayout.class.getName() + id;
        }
      };

      getApplication().getServices().getRequestInboxService().setListener(meldingListener);
      addComponent(hLayout);
    }
    super.attach();
  }

  private void addSideLayout(HLayout hLayout) {
    if (inboxLayout != null) {
      hLayout.removeComponent(inboxLayout);
    }
    Optional<RequestInboxItem> inboxRecord = getApplication().getServices().getRequestInboxService().getMyRecord();
    inboxRecord.ifPresent(record -> {
      inboxLayout = new RequestInboxSideLayout(record);
      hLayout.add(inboxLayout);
    });
  }
}
