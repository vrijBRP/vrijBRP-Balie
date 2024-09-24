/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.common;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.layout.VLayout;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class ZaakPersonChoiceWindow extends GbaModalWindow {

  private final List<PersonChoice> choices = new ArrayList<>();

  public ZaakPersonChoiceWindow(String caption, String width) {
    super(caption, width);
    VLayout layout = new VLayout();
    layout.setMargin(true);
    setContent(layout);
  }

  @Override
  public void attach() {
    VLayout content = (VLayout) getContent();
    content.removeAllComponents();
    for (PersonChoice choice : choices) {
      Button button = new Button(choice.getLabel());
      button.addListener((ClickListener) clickEvent -> choice.getChoiceListener().run());
      button.setSizeFull();
      content.add(button);
    }

    super.attach();
  }

  protected void goToZaak(Dossier dossier) {
    getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, dossier);
    getGbaApplication().openWindow(getApplication().getMainWindow(), new HomeWindow(), "bs.onderzoek");
  }

  protected void addChoice(String label, Runnable choiceListener) {
    choices.add(new PersonChoice(label, choiceListener));
  }

  @Data
  @RequiredArgsConstructor
  private static class PersonChoice {

    private final String   label;
    private final Runnable choiceListener;
  }
}
