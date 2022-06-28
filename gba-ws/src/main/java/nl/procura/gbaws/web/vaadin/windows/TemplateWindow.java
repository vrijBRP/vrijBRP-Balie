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

package nl.procura.gbaws.web.vaadin.windows;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;

import nl.procura.gbaws.web.vaadin.layouts.navigation.TemplateAccordionTab;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.component.window.WindowListener;
import nl.procura.vaadin.component.window.windowEvents.WindowEvent;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.vaadin.theme.twee.layout.FooterLayout;
import nl.procura.vaadin.theme.twee.window.ApplicationWindow;

@SuppressWarnings("serial")
public class TemplateWindow extends ApplicationWindow implements FragmentChangedListener, WindowListener {

  final UriFragmentUtility uriHandler = new UriFragmentUtility();

  public TemplateWindow(String caption, String name) {

    super(caption, name);

    addComponent(uriHandler);

    uriHandler.addListener(this);

    addFooter();
  }

  @Override
  public void fragmentChanged(FragmentChangedEvent source) {

    String fragment = source.getUriFragmentUtility().getFragment();

    TemplateAccordionTab navigatie = VaadinUtils.getChild(getWindow(), TemplateAccordionTab.class);

    if (fil(fragment)) {
      AccordionLink link = navigatie.getLink(fragment);

      if (link == null) {
        new Message(getWindow(), "Info", "Module: " + fragment + " niet gevonden.", Message.TYPE_ERROR_MESSAGE);
      } else {
        link.select();
      }
    }
  }

  @Override
  public void event(WindowEvent event) {
    if (event instanceof WindowInit) {
      TemplateAccordionTab navigatie = VaadinUtils.getChild(getWindow(), TemplateAccordionTab.class);
      if (!navigatie.getAllLinks().isEmpty()) {
        navigatie.getAllLinks().get(0).select();
      }
    }
  }

  private void addFooter() {
    FooterLayout footerLayout = new FooterLayout();
    footerLayout.setMargin(false);
    footerLayout.setStyleName(ProcuraTheme.LAYOUT_FIXED_FOOTER);
    getAppLayout().addComponent(footerLayout);
  }
}
