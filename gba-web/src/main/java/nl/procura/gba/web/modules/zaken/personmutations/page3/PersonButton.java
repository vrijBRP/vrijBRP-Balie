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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class PersonButton extends Button {

  public PersonButton() {
    super("Persoon");

    addListener((ClickListener) event -> {
      GbaApplication app = (GbaApplication) getApplication();
      Services services = app.getServices();
      BasePLExt pl = services.getPersonenWsService().getHuidige();
      app.getParentWindow().addWindow(new PersonWindow(pl));
    });
  }

  public static class PersonWindow extends GbaModalWindow {

    public PersonWindow(BasePLExt pl) {
      super("Algemene persoonsgegevens", "700px");
      TableLayout layout = new TableLayout();
      layout.setColumnWidths("90px", "");

      layout.addLabel("BSN");
      layout.addData(new Label(pl.getPersoon().getBsn().getDescr()));

      layout.addLabel("A-nummer");
      layout.addData(new Label(pl.getPersoon().getAnr().getDescr()));

      layout.addLabel("Geslachtsnaam");
      layout.addData(new Label(pl.getPersoon().getNaam().getGeslachtsnaam().getValue().getDescr()));

      layout.addLabel("Voorvoegsel");
      layout.addData(new Label(pl.getPersoon().getNaam().getVoorvoegsel().getValue().getDescr()));

      layout.addLabel("Voornamen");
      layout.addData(new Label(pl.getPersoon().getNaam().getVoornamen().getValue().getDescr()));

      layout.addLabel("Geboorte");
      layout.addData(new Label(pl.getPersoon().getGeboorte().getDatumLeeftijdPlaatsLand()));

      layout.addLabel("Adres");
      layout.addData(new Label(pl.getVerblijfplaats().getAdres().getAdresPcWplGem()));

      addComponent(new VLayout(layout).margin(true));
    }
  }
}
