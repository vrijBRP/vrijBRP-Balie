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

package nl.procura.gbaws.web.vaadin.layouts.navigation;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.UriFragmentUtility;

import nl.procura.gbaws.web.vaadin.module.MainModuleContainer;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.layout.accordion.AccordionTab;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

@SuppressWarnings("serial")
public class TemplateAccordionTab extends AccordionTab {

  @Override
  public void onLinkSelect(AccordionLink link) {

    if (link.getUrl().isFragment()) {

      // Fragment
      link.activate();
      UriFragmentUtility uriHandler = VaadinUtils.getChild(getWindow(), UriFragmentUtility.class);
      uriHandler.setFragment(link.getUrl().getFragment(), false);

      if (link.getId() != null) {

        MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);
        mainModule.getNavigation().getPages().clear();
        mainModule.getNavigation().addPage((Class<?>) link.getId());
      } else {
        new Message(getWindow(), "Geen pagina.", Message.TYPE_WARNING_MESSAGE);
      }
    } else if (link.getUrl().isExternal()) {

      // Externe link
      getWindow().open(new ExternalResource(link.getUrl().getValue()));
    } else {

      // Interne link
      getWindow().open(new ExternalResource(getWindow().getApplication().getURL() + link.getUrl().getValue()));
    }
  }
}
