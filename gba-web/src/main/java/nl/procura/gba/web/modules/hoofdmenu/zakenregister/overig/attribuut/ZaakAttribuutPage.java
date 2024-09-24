/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.components.layouts.tablefilter.sort.ZaakSortField;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.ZakenregisterUtils;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenOpties;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page3.Page3Zaken;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public abstract class ZaakAttribuutPage extends ZakenregisterPage<Zaak> {

  private final Button buttonReload = new Button("Herladen (F5)");

  private Table            table         = null;
  private Page2ZakenOpties opties        = null;
  private List<ZaakKey>    zaakIds       = new ArrayList<>();
  private ZaakSortering    zaakSortering = ZaakSortering.DATUM_INGANG_NIEUW_OUD;
  private final InfoLayout info;

  public ZaakAttribuutPage(String caption, InfoLayout info) {
    super(null, caption);
    this.info = info;
    setSpacing(true);
    setHeight("450px");
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new Table();
      addComponent(info);
      addExpandComponent(table);

      buttonReload.addListener(this);
      getMainbuttons().addComponent(buttonReload);

      ZaakSortField sortField = new ZaakSortField();
      sortField.addListener(new FieldChangeListener<ZaakSortering>() {

        @Override
        public void onChange(ZaakSortering value) {
          ZaakAttribuutPage.this.zaakSortering = value;
          onSearch();
        }
      });

      ZaakAttribuutPage.this.zaakSortering = sortField.getValue();

      GbaIndexedTableFilterLayout filterLayout = new GbaIndexedTableFilterLayout(table, sortField);
      getMainbuttons().addComponent(filterLayout, 1);

    } else if (event.isEvent(LoadPage.class)) {
      onSearch();
    }

    opties = VaadinUtils.addOrReplaceComponent(getMainbuttons(), new Page2ZakenOpties(table));
    getMainbuttons().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F5, buttonReload)) {
      onSearch();

    } else if (keyCode == KeyCode.F8) {
      opties.delete();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    VaadinUtils.getParent(table, ZakenregisterPage.class).getNavigation().goToPage(new Page3Zaken(table));
    super.onNextPage();
  }

  @Override
  public void onSearch() {
    table.init();
    ZakenregisterUtils.getZakenregisterAccordionTab(getWindow()).ifPresent(ZakenregisterAccordionTab::recountTree);
    super.onSearch();
  }

  protected abstract ZaakArgumenten getZaakArgumenten();

  public class Table extends Page2ZakenTable {

    private Table() {
      setHeight("100%");
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nr", 50);
      addColumn("Zaaktype");
      addColumn("Gebruiker / profielen").setCollapsed(true);
      addColumn("Gebruiker", 170).setCollapsed(false);
      addColumn("Profielen").setCollapsed(true);
      addColumn("Bron", 130).setCollapsed(true);
      addColumn("Leverancier", 130).setCollapsed(true);
      addColumn("Status", 130).setUseHTML(true);
      addColumn("Datum ingang", 100);
      addColumn("Ingevoerd op", 130);
    }

    @Override
    public void init() {
      zaakIds = getApplication().getServices().getZakenService().getZaakKeys(getZaakArgumenten());
      table.setSortering(zaakSortering);
      super.init();
    }

    @Override
    public void setRecords() {
      for (ZaakKey zaakId : zaakIds) {
        addRecord(zaakId).addValues(9);
      }
      super.setRecords();
    }

    @Override
    protected void loadZaak(int nr, Record record, Zaak zaak) {
      record.getValues().get(0).setValue(nr);
      record.getValues().get(1).setValue(ZaakUtils.getTypeEnOmschrijving(zaak));
      record.getValues().get(2).setValue(getIngevoerdDoor(zaak));
      record.getValues().get(3).setValue(ZaakUtils.getIngevoerdDoorGebruiker(zaak));
      record.getValues().get(4).setValue(getIngevoerdDoorProfielen(zaak));
      record.getValues().get(5).setValue(zaak.getBron());
      record.getValues().get(6).setValue(zaak.getLeverancier());
      record.getValues().get(7).setValue(ZaakUtils.getStatus(zaak.getStatus()));
      record.getValues().get(8).setValue(zaak.getDatumIngang());
      record.getValues().get(9).setValue(zaak.getDatumTijdInvoer());
    }
  }
}
