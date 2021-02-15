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

package nl.procura.gba.web.modules.persoonslijst.overig.mark.page1;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.ProfileMarkingsWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Marking extends NormalPageTemplate {

  protected final Button buttonAll = new Button("Overige personen / adressen");

  private BasePLExt         pl;
  private Page1MarkingTable table;

  public Page1Marking(BasePLExt pl) {
    this.pl = pl;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonAll.addListener((Button.ClickListener) clickEvent -> {
        getParentWindow().addWindow(new ProfileMarkingsWindow(() -> table.init()));
      });

      addButton(buttonAll, 1f);
      addButton(buttonClose);

      setInfo("", "Geef aan of deze persoon en/of het adres moet worden gemarkeerd voor risicoanalyses." +
          "<hr/>Dubbelklik op de regel om de markering uit of aan te zetten.");

      table = new Page1MarkingTable(pl);
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
