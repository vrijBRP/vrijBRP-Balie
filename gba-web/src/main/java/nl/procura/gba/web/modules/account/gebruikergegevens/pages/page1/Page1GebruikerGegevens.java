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

package nl.procura.gba.web.modules.account.gebruikergegevens.pages.page1;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.account.meldingen.pages.ModulePageTemplate;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1GebruikerGegevens extends ModulePageTemplate {

  private final List<Gegeven> gegevens = new ArrayList<>();
  private GbaTable            table    = null;

  public Page1GebruikerGegevens() {

    super("Gebruikergegevens");

    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void setColumns() {

          addStyleName("gebruikersgegevens");
          setSelectable(true);

          GbaApplication application = Page1GebruikerGegevens.this.getApplication();
          String userName = application.getServices().getGebruiker().getNaam();

          addColumn("Omschr.", 300);
          addColumn("Algemene waarde", 500);
          addColumn("Waarde voor " + userName).setClassType(Component.class);
        }

        @Override
        public void setRecords() {

          GebruikerInfoService db = getApplication().getServices().getGebruikerInfoService();

          for (GebruikerInfo info : db.getGebruikerInfo(
              getApplication().getServices().getGebruiker()).getAlles()) {

            Gegeven g = new Gegeven(info);

            gegevens.add(g);

            Record r = addRecord(g.getInfo());

            r.addValue(g.getInfo().getOmschrijving());
            r.addValue(g.getInfo().getStandaardWaarde());
            r.addValue(g.getField());
          }
        }
      };

      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    for (Gegeven g : gegevens) {

      g.getInfo().setGebruiker(getApplication().getServices().getGebruiker());
      g.getInfo().setWaarde(astr(g.getField().getValue()));
      getApplication().getServices().getGebruikerInfoService().save(g.getInfo());
      getApplication().getServices().getGebruikerService().reload(getApplication().getServices().getGebruiker());
    }

    successMessage("De gegevens zijn opgeslagen.");
  }

  class Gegeven {

    private GebruikerInfo info;
    private TextField     field;

    private Gegeven(GebruikerInfo info) {

      setInfo(info);

      TextField tf = new TextField();
      tf.setValue(info.getGebruikerWaarde());
      tf.setWidth("100%");
      setField(tf);
    }

    public TextField getField() {
      return field;
    }

    public void setField(TextField field) {
      this.field = field;
    }

    public GebruikerInfo getInfo() {
      return info;
    }

    public void setInfo(GebruikerInfo info) {
      this.info = info;
    }
  }
}
