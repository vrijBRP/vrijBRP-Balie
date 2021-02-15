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

package nl.procura.gba.web.modules.beheer.gebruikers.page12;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.gebruikers.page1.Page1Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2Gebruikers;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page12Gebruikers extends NormalPageTemplate {

  private final Button      buttonCheck = new Button("Controleer opnieuw (F3)");
  private final List<Check> checks      = new ArrayList<>();
  private Table             table       = null;

  public Page12Gebruikers() {

    super("Controleer de gebruikergegevens");

    addButton(buttonPrev);
    addButton(buttonCheck);

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Voor een correcte werking van de applicatie moeten de gebruikergegevens volledig zijn ingevoerd. "
          + "Dit houdt in dat ze een unieke naam en e-mailadres moeten hebben. Verder zouden ze gekoppeld moeten zijn aan een profiel");

      table = new Table();
      addComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    } else if (event.isEvent(AfterReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonCheck) {
      doCheck();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1Gebruikers.class);
  }

  private void doCheck() {

    checks.clear();

    getApplication().getParentWindow(getWindow()).addWindow(
        new GebruikerControleWindow(checks, getServices().getGebruikerService()) {

          @Override
          public void closeWindow() {

            table.init();

            super.closeWindow();
          }
        });
  }

  public static class Check {

    private final String    opmerking;
    private final Gebruiker gebruiker;

    public Check(String opmerking, Gebruiker gebruiker) {
      super();
      this.opmerking = opmerking;
      this.gebruiker = gebruiker;
    }

    public Gebruiker getGebruiker() {
      return gebruiker;
    }

    public String getOpmerking() {
      return opmerking;
    }
  }

  public class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      Gebruiker gebruiker = (Gebruiker) record.getObject();
      Gebruiker volGebruiker = getServices().getGebruikerService().getGebruikerByCode(gebruiker.getCUsr(), true);
      getNavigation().goToPage(new Page2Gebruikers(volGebruiker));
    }

    @Override
    public void setColumns() {

      setSelectable(true);

      addColumn("Nr", 30);
      addColumn("Gebruiker", 100);
      addColumn("Naam", 200);
      addColumn("Status", 100);
      addColumn("Opmerking").setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nummer = 0;

      for (Check check : checks) {

        nummer++;

        Gebruiker gebruiker = check.getGebruiker();
        String opmerking = check.getOpmerking();

        Record record = addRecord(gebruiker);
        record.addValue(nummer);
        record.addValue(gebruiker.getGebruikersnaam());
        record.addValue(gebruiker.getNaam());
        record.addValue(gebruiker.getGeldigheidStatus());

        record.addValue(emp(opmerking) ? setClass(true, "geen") : setClass(false, opmerking));
      }

      super.setRecords();
    }
  }
}
