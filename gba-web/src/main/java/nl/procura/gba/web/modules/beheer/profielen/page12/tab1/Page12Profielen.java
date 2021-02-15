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

package nl.procura.gba.web.modules.beheer.profielen.page12.tab1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.modules.beheer.profielen.page12.ZaakConfiguratiesTab;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page12Profielen extends KoppelPage {

  private static final int INDEX_STATUS = 0;
  private Profiel          profiel      = null;
  private Page12Table      table        = null;

  private KoppelForm form = null;

  public Page12Profielen(Profiel profiel) {

    super("Tonen configuraties");
    setProfiel(profiel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setMargin(true);
      setSpacing(true);
      setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen.");

      form = new KoppelForm();
      HorizontalLayout hL = new HorizontalLayout();
      hL.addComponent(form);

      setTable();

      hL.addComponent(new GbaIndexedTableFilterLayout(table));
      hL.setExpandRatio(form, 1f);
      addComponent(hL);

      addExpandComponent(table);

    } else if (event.isEvent(LoadPage.class)) {
      table.init();
    }

    super.event(event);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  @Override
  public void onPreviousPage() {
    VaadinUtils.getParent(this, ZaakConfiguratiesTab.class).getNavigation().goBackToPreviousPage();
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {
    new KoppelProcedure(table, koppelActie, "indicaties", true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {
        Page12Profielen.this.koppel(koppelActie, wholeTable);
      }
    };
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {
    new KoppelProcedure(table, koppelActie, "indicaties", false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {
        Page12Profielen.this.koppel(koppelActie, wholeTable);
      }
    };
  }

  private List<ZaakConfiguratie> getSelectedIndicaties(boolean wholeTable) {
    List<ZaakConfiguratie> l;
    if (wholeTable) {
      l = table.getAllValues(ZaakConfiguratie.class);
    } else {
      l = table.getSelectedValues(ZaakConfiguratie.class);
    }
    return l;
  }

  private Object getStatus(KoppelActie koppelActie) {
    if (KOPPEL.equals(koppelActie)) {
      return setClass("green", "Gekoppeld");
    }
    // ontkoppelen
    return setClass("red", "Niet-gekoppeld");
  }

  private void koppel(KoppelActie koppelActie, boolean wholeTable) {
    koppelOntkoppelProfielen(koppelActie, getSelectedIndicaties(wholeTable), profiel);
    setTableStatus(wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
  }

  private void setTable() {

    table = new Page12Table(getProfiel()) {

      @Override
      public void setContainerDataSource(Container newDataSource) {
        super.setContainerDataSource(newDataSource);
        form.check(this);
      }

      @Override
      public void setRecordValue(Record record, Object propertyId, Object value) {
        super.setRecordValue(record, propertyId, value);
        form.check(this);
      }
    };
  }

  private void setTableStatus(List<Record> selectedRecords, KoppelActie koppelActie) {
    for (Record r : selectedRecords) {
      table.setRecordValue(r, INDEX_STATUS, getStatus(koppelActie));
    }
  }
}
