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

package nl.procura.gba.web.modules.beheer.profielen.page4;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielPageTemplate;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4Profielen extends ModuleProfielPageTemplate {

  private final HorizontalLayout hLayout     = new HorizontalLayout();
  private Component              actieLayout = new HorizontalLayout();
  private Profiel                profiel;

  public Page4Profielen(Profiel profiel) {
    setProfiel(profiel);
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        Tree actionTree = new Tree(profiel);
        actionTree.setWidth("150px");
        hLayout.addComponent(actionTree);
        hLayout.addComponent(actieLayout);
        hLayout.setExpandRatio(actieLayout, 1L);
        hLayout.setWidth("100%");
        addExpandComponent(hLayout);
      }
    } catch (Exception e) {
      throw new ProException(DATABASE, ERROR, "Fout bij ophalen van de acties", e);
    }

    super.event(event);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {

    this.profiel = profiel;
  }

  public class Tree extends ActionTree {

    private Tree(Profiel profiel) {
      super(profiel);
    }

    @Override
    public void setNewComponent(ProfielActieLayout component) {

      hLayout.replaceComponent(actieLayout, component);
      hLayout.setExpandRatio(component, 1L);
      actieLayout = component;
    }
  }
}
