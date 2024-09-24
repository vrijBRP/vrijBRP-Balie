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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search.favorieten;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.function.Consumer;

import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorie;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieService;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

/**
 * Favorieten
 */
public class FavorietenPage extends NormalPageTemplate {

  private final PersoonHistorieType type;
  private final Consumer<String>    nrConsumer;
  private FavorietenTable           table = null;

  public FavorietenPage(PersoonHistorieType type, Consumer<String> nrConsumer) {
    this.nrConsumer = nrConsumer;
    setSpacing(true);
    setMargin(true);
    this.type = type;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      GbaTextField searchField = new GbaTextField();
      searchField.setWidth("100%");
      searchField.setInputPrompt("Filter...");
      searchField.setImmediate(true);

      table = new FavorietenTable() {

        @Override
        public void onClick(Record record) {
          PersoonHistorie ph = (PersoonHistorie) record.getObject();
          goToPl(ph);
          ((ModalWindow) getWindow()).closeWindow();
        }

        @Override
        public void setRecords() {
          Services services = getApplication().getServices();
          PersoonHistorieService db = services.getPersoonHistorieService();
          for (PersoonHistorie ph : db.getPersoonHistorie(type, services.getGebruiker())) {
            Record record = addRecord(ph);
            record.addValue(ph.getOmschrijving());
          }

          super.setRecords();
        }
      };

      searchField.addListener((TextChangeListener) e -> {
        table.setFilter(IndexedTableFilterLayout.class, new IndexedTableFilter(e.getText(), false));
      });

      addComponent(searchField);
      addComponent(table);
    } else if (event.isEvent(AfterBackwardReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (keyCode == KeyCode.F5) {
      onEnter();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {
  }

  @Override
  public void onEnter() {

    Record record = table.getSelectedRecord();
    if (record != null) {
      table.onClick(record);
    }

    super.onEnter();
  }

  private void goToPl(PersoonHistorie ph) {
    if (nrConsumer != null) {
      nrConsumer.accept(astr(ph.getNummer()));
    } else {
      getApplication().goToPl(getWindow().getParent(), "", ph.getDatabron(), astr(ph.getNummer()));
    }
  }
}
