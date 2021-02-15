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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.components.layouts.tablefilter.sort.ZaakSortField;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page3.Page3Zaken;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.zakenregister.ZaakItem;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page2Zaken extends ZakenregisterPage<Zaak> {

  private final Button buttonReload = new Button("Herladen (F5)");

  private Table            table         = null;
  private final ZaakItem   zaakItem;
  private Page2ZakenOpties opties        = null;
  private List<ZaakKey>    zaakIds       = new ArrayList<>();
  private ZaakSortering    zaakSortering = ZaakSortering.DATUM_INGANG_NIEUW_OUD;

  public Page2Zaken(ZaakItem zaakItem) {

    super(null, "Zakenregister - " + astr(zaakItem).toLowerCase());

    this.zaakItem = zaakItem;

    setSpacing(true);
    setHeight("450px");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Table();

      setInfo("", "Zaken ouder dan één jaar worden niet getoond in dit scherm.");

      addExpandComponent(table);

      buttonReload.addListener(this);
      getMainbuttons().addComponent(buttonReload);

      ZaakSortField sortField = new ZaakSortField();
      sortField.addListener(new FieldChangeListener<ZaakSortering>() {

        @Override
        public void onChange(ZaakSortering value) {
          Page2Zaken.this.zaakSortering = value;
          onSearch();
        }
      });

      Page2Zaken.this.zaakSortering = sortField.getValue();

      GbaIndexedTableFilterLayout filterLayout = new GbaIndexedTableFilterLayout(table, sortField);
      getMainbuttons().addComponent(filterLayout, 1);

    } else if (event.isEvent(LoadPage.class)) {
      onSearch();
    }

    opties = VaadinUtils.addOrReplaceComponent(getMainbuttons(), new Page2ZakenOpties(table));

    getMainbuttons().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);

    super.event(event);
  }

  private ZaakItem getZaakItem() {
    return zaakItem;
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
    zaakItem.setSortering(zaakSortering);
    zaakIds = getApplication().getServices().getZakenregisterService().getZaakIds(getZaakItem());
    table.setSortering(zaakSortering);
    table.init();
    super.onSearch();
  }

  public class Table extends Page2ZakenTable {

    private Table() {
      setHeight("100%");
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
      record.getValues().get(7).setValue(zaak.getDatumIngang());
      record.getValues().get(8).setValue(zaak.getDatumTijdInvoer());
    }
  }
}
