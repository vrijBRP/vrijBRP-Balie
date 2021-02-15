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

package nl.procura.gba.web.windows;

import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.GbaFragmentUtility;
import nl.procura.gba.web.components.layouts.footer.GbaWebNormalFooterLayout;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.window.WindowListener;
import nl.procura.vaadin.component.window.windowEvents.WindowEvent;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;
import nl.procura.vaadin.component.window.windowEvents.WindowLoad;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.layout.DynamicMainLayout;
import nl.procura.vaadin.theme.twee.window.ApplicationWindow;

public class GbaWindow extends ApplicationWindow implements FragmentChangedListener, WindowListener {

  private final static Logger      logger          = LoggerFactory.getLogger(GbaWindow.class.getName());
  private final GbaFragmentUtility fragmentUtility = new GbaFragmentUtility();

  public GbaWindow(String caption, String name) {

    super(caption, name, new DynamicMainLayout(GbaApplication.getInstance().isDynamicHeight()));
    addComponent(fragmentUtility);
    fragmentUtility.addListener(this);
  }

  @Override
  public void event(WindowEvent event) {

    if (event.isEvent(WindowInit.class)) {
      getMainLayout().setFooterLayout(new GbaWebNormalFooterLayout());
      logger.debug("INIT WINDOW: " + this);

      // Selecteer eerste module.
      GbaAccordionTab navigatie = VaadinUtils.getChild(getWindow(), GbaAccordionTab.class);

      if (navigatie != null && navigatie.getAllLinks().size() > 0) {
        navigatie.getAllLinks().get(0).select();
      }
    } else if (event.isEvent(WindowLoad.class)) {
      logger.debug("LOAD WINDOW: " + this);
    }
  }

  @Override
  public void fragmentChanged(FragmentChangedEvent source) {
    gotoFragment(source.getUriFragmentUtility().getFragment());
  }

  public GbaFragmentUtility getFragmentUtility() {
    return fragmentUtility;
  }

  public GbaApplication getGbaApplication() {
    return (GbaApplication) getApplication();
  }

  @Override
  public boolean isName(String... names) {
    for (String name : names) {
      if (eq(getName(), name)) {
        return true;
      }
    }

    return false;
  }

  protected void gotoFragment(String fragment) {

    String newFragment = StringUtils.substringBefore(fragment, "?");
    List<GbaAccordionTab> navigaties = VaadinUtils.getChildrenBF(getMainLayout().getNavigationLayout(),
        GbaAccordionTab.class);

    if (fil(newFragment)) {
      for (GbaAccordionTab navigatie : navigaties) {
        AccordionLink link;
        if (navigatie != null) {
          link = navigatie.getLink(newFragment);
          if (link != null) {
            link.select();
            return;
          }
        }
      }
    }
  }
}
