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

package nl.procura.gba.web.modules.beheer.gebruikers.page5;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.gebruikers.page6.Page6Gebruikers;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoService;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**

 * <p>
 * 18 aug. 2011
 */

public class Page5Gebruikers extends NormalPageTemplate {

  private Gebruiker gebruiker = null;
  private GbaTable  table     = null;

  public Page5Gebruikers(Gebruiker gebruiker) {

    super("Extra gebruikergegevens");

    setGebruiker(gebruiker);
    final String gebrNaam = gebruiker.getNaam();

    addButton(buttonPrev, buttonNew, buttonDel);

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        try {

          GebruikerInfo gebrInfo = (GebruikerInfo) record.getObject();
          getNavigation().goToPage(new Page6Gebruikers(getGebruiker(), gebrInfo, table));
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);
        addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

        addColumn("Betreft", 100);
        addColumn("Omschr.");
        addColumn("Algemene waarde");
        addColumn("Waarde voor " + gebrNaam);
      }

      @Override
      public void setRecords() {

        GebruikerInfoService db = getServices().getGebruikerInfoService();

        for (GebruikerInfo info : db.getGebruikerInfo(getGebruiker()).getAlles()) {
          GebruikerInfo infoImpl = castToGebruikerInfoImpl(info);

          if (infoImpl != null) {
            Record r = addRecord(info);
            r.addValue(info.isIedereen() ? "Iedereen" : getGebruiker().getNaam());
            r.addValue(info.getOmschrijving());
            r.addValue(info.getStandaardWaarde());
            r.addValue(info.getGebruikerWaarde());
          }
        }
      }

      private GebruikerInfo castToGebruikerInfoImpl(GebruikerInfo info) {

        GebruikerInfo infoImpl = null;

        if (info instanceof GebruikerInfo) {
          infoImpl = info;
        }

        return infoImpl;
      }
    };

    addExpandComponent(table);

    getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  @Override
  public void onDelete() {

    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Page5Gebruikers.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page6Gebruikers(gebruiker, table));
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  protected void deleteRecord(Record r) {

    for (GebruikerInfo gebrInfo : table.getSelectedValues(GebruikerInfo.class)) {
      if (GebruikerInfoType.exists(gebrInfo.getInfo())) {
        throw new ProException(SELECT, WARNING, "U kunt geen standaardoptie verwijderen");
      }
    }
    getServices().getGebruikerInfoService().deleteAlles((GebruikerInfo) r.getObject());
  }
}
