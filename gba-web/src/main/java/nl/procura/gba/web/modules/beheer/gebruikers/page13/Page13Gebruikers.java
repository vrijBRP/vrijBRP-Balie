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

package nl.procura.gba.web.modules.beheer.gebruikers.page13;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

import lombok.Data;

public class Page13Gebruikers extends NormalPageTemplate {

  private final List<Gebruiker>     gebruikers;
  private final MakeMapForm         form;
  private Table                     table  = null;
  private final List<PasswordReset> resets = new ArrayList<>();

  public Page13Gebruikers(List<Gebruiker> gebruikers) {
    super("Gebruikers: wachtwoord resetten");
    addButton(buttonPrev, buttonSave);
    buttonSave.setCaption("Reset de wachtwoorden");
    this.gebruikers = gebruikers;
    form = new MakeMapForm(gebruikers);
    addComponent(form);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      gebruikers.stream().map(PasswordReset::new).forEach(resets::add);
      table = new Table();
      addExpandComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    for (PasswordReset reset : resets) {
      GebruikerService gebruikerService = getServices().getGebruikerService();
      String nieuwWachtwoord = gebruikerService.generateWachtwoord();
      gebruikerService.setWachtwoord(reset.gebruiker, nieuwWachtwoord, true);
      reset.setWachtwoord(nieuwWachtwoord);
    }
    table.init();
  }

  @Data
  public static class PasswordReset {

    private final Gebruiker gebruiker;
    private String          wachtwoord;

    public PasswordReset(Gebruiker gebruiker) {
      super();
      this.gebruiker = gebruiker;
    }
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(false);
      addColumn("Nr", 30);
      addColumn("Gebruiker", 150);
      addColumn("Naam", 200);
      addColumn("E-mail", 200);
      addColumn("Nieuw tijdelijk wachtwoord");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      int nummer = 0;
      for (PasswordReset change : resets) {
        nummer++;
        Gebruiker gebruiker = change.getGebruiker();
        IndexedTable.Record record = addRecord(gebruiker);
        record.addValue(nummer);
        record.addValue(gebruiker.getGebruikersnaam());
        record.addValue(gebruiker.getNaam());
        record.addValue(gebruiker.getEmail());
        record.addValue(change.getWachtwoord());
      }

      super.setRecords();
    }
  }
}
