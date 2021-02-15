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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.TextArea;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class PresentievraagToelichtingWindow extends GbaModalWindow {

  private final TextArea toelichtingVeld = new TextArea();
  private final String   standaardText;

  public PresentievraagToelichtingWindow(String defaultText) {
    super("Toelichting op de presentievraag", "520px");
    this.standaardText = defaultText;
    setClosable(false);
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public abstract void onSave(String comment);

  public class Page1 extends ButtonPageTemplate {

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void attach() {
      super.attach();
      toelichtingVeld.focus();
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        buttonSave.setCaption("Ok");
        buttonSave.addListener(this);

        setInfo("Wilt u nog iets kwijt over deze presentievraag?",
            "Wellicht de reden waarom u deze vraag heeft gesteld (eerste inschrijving, etc)");

        toelichtingVeld.setValue(standaardText);
        toelichtingVeld.setSizeFull();

        addComponent(toelichtingVeld);
        addComponent(buttonSave);
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      closeWindow();
    }

    @Override
    public void onSave() {
      PresentievraagToelichtingWindow.this.onSave(astr(toelichtingVeld.getValue()));
      closeWindow();
    }
  }
}
