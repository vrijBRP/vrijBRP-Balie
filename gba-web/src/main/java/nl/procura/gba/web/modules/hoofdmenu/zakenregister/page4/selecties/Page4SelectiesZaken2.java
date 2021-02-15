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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties;

import static nl.procura.gba.web.services.zaken.selectie.SelectieColumn.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.SelDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenTable;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.selectie.Selectie;
import nl.procura.gba.web.services.zaken.selectie.SelectieColumn;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4SelectiesZaken2 extends NormalPageTemplate {

  protected final Button     buttonChange = new Button("Aanpassen");
  private final Selectie     selectie;
  private Table1             table        = null;
  private SelDao.Result      result       = new SelDao.Result();
  private SelectieInfoLayout info;

  public Page4SelectiesZaken2(Selectie selectie) {
    super("Zakenregister - selecties");
    this.selectie = selectie;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonPrev.setWidth("120px");
      buttonChange.setWidth("120px");

      addButton(buttonPrev);
      if (getApplication().isProfielActie(ProfielActie.UPDATE_SELECTIES)) {
        addButton(buttonChange);
      }

      info = new SelectieInfoLayout(selectie, null);
      addComponent(info);

      table = new Table1();
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      addComponent(new Fieldset("Zoekresultaten"));
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (buttonChange == button) {
      onChange();
    }
    super.handleEvent(button, keyCode);
  }

  private void onChange() {
    getNavigation().goToPage(new Page4SelectiesZaken3(selectie));
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void setResults() {
    result = SelDao.getFromStatement(selectie.getStatement());
  }

  private List<ZaakKey> getZaakIds() {
    List<ZaakKey> zaakKeys = new ArrayList<>();
    for (SelDao.Row row : result.getRows()) {
      for (SelDao.Value val : row.getValues()) {
        SelectieColumn selectieColumn = SelectieColumn.getByName(val.getName());
        if (ZAAK_ID.equals(selectieColumn)) {
          zaakKeys.add(new ZaakKey(val.getValue()));
        }
      }
    }
    return zaakKeys;
  }

  public class Table1 extends Page4ZakenTable {

    @Override
    public void onLazyLoad(List<Record> records) {
      lazyLoad(records, false);
    }

    @Override
    public void setColumns() {

      setSelectable(true);

      addColumn("Nr", 30);
      addColumn("Verloopdatum", 220).setUseHTML(true);
      addColumn("Zaaktype");
      addColumn("Status", 120).setUseHTML(true);
      addColumn("Datum ingang", 100);
      addColumn("Gebruiker", 150);
    }

    @Override
    public void setRecords() {

      try {
        setResults();
        List<ZaakKey> zaakKeys = getZaakIds();

        int i = zaakKeys.size();
        if (zaakKeys.size() > 0) {
          for (ZaakKey zaakKey : zaakKeys) {
            Record r = table.addRecord(zaakKey).addValues(10);
            r.setValue(0, i);
            i--;
          }
        }
        updateInfoLayout(null);
      } catch (Exception e) {
        updateInfoLayout(e);
      }

      super.setRecords();
    }

    private void updateInfoLayout(Exception e) {
      SelectieInfoLayout newInfo = new SelectieInfoLayout(selectie, e);
      replaceComponent(info, newInfo);
      info = newInfo;
    }

    @Override
    protected void loadZaak(int nr, Record record, Zaak zaak) {
      record.setValue(1, ZaakUtils.getDatumEnDagenTekst(getVerloopDatum(zaak)));
      record.setValue(2, ZaakUtils.getTypeEnOmschrijving(zaak));
      record.setValue(3, ZaakUtils.getStatus(zaak.getStatus()));
      record.setValue(4, pos(zaak.getDatumIngang().getLongDate()) ? astr(zaak.getDatumIngang()) : "Onbekend");
      record.setValue(5, getGebruiker(zaak));

      if (!isVerloopDatum()) {
        getColumns().get(1).setCollapsed(true);
      }
      if (!isGebruiker()) {
        getColumns().get(5).setCollapsed(true);
      }
    }

    private boolean isVerloopDatum() {
      return result.isColumn(VERLOOPDATUM.getName());
    }

    private String getVerloopDatum(Zaak zaak) {
      return result.getRows()
          .stream()
          .filter(row -> row.hasValue(ZAAK_ID.getName(), zaak.getZaakId()) &&
              row.hasValue(VERLOOPDATUM.getName()))
          .findFirst()
          .map(row -> row.getValue(VERLOOPDATUM.getName()).getValue())
          .orElse("");
    }

    private boolean isGebruiker() {
      return result.isColumn(GEBRUIKER.getName());
    }

    private String getGebruiker(Zaak zaak) {
      return result.getRows()
          .stream()
          .filter(row -> row.hasValue(ZAAK_ID.getName(), zaak.getZaakId()) &&
              row.hasValue(GEBRUIKER.getName()))
          .findFirst()
          .map(row -> row.getValue(GEBRUIKER.getName()).getValue())
          .orElse("");
    }
  }
}
