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

package nl.procura.gba.web.modules.beheer.gebruikers.page4;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DATABASE;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.page1.ParameterTree;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4Gebruikers extends ButtonPageTemplate {

  private final HorizontalLayout hLayout     = new HorizontalLayout();
  private Component              actieLayout = new HorizontalLayout();
  private Gebruiker              gebruiker   = null;

  public Page4Gebruikers(Gebruiker gebruiker) {
    setGebruiker(gebruiker);
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        ParameterBean parameterBean = new ParameterBean();
        parameterBean.setFieldValues(getServices().getParameterService().getAllParameters(),
            getGebruiker().getCUsr(), 0);

        Tree tree = new Tree(parameterBean);

        tree.setWidth("190px");
        hLayout.addComponent(tree);
        hLayout.addComponent(actieLayout);
        hLayout.setExpandRatio(actieLayout, 1L);
        hLayout.setWidth("100%");
        addComponent(hLayout);
      }
    } catch (IllegalAccessException e) {
      throw new ProException(DATABASE, ERROR, "Fout bij ophalen van de parameters", e);
    }

    super.event(event);
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  public class Tree extends ParameterTree {

    private Tree(ParameterBean parameterBean) {
      super(parameterBean, gebruiker, null);
    }

    @Override
    public void setNewComponent(Component component) {

      hLayout.replaceComponent(actieLayout, component);
      hLayout.setExpandRatio(component, 1L);
      actieLayout = component;

      super.setNewComponent(component);
    }
  }
}
