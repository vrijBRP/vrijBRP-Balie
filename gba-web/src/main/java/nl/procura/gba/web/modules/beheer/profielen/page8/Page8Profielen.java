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

package nl.procura.gba.web.modules.beheer.profielen.page8;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page8Profielen extends KoppelPage {

  private static final int    INDEX_STATUS = 0;
  private Profiel             profiel      = null;
  private GBACategorieenTable table        = null;

  private KoppelForm form = null;

  public Page8Profielen(Profiel profiel) {
    super("Tonen BRP historische categorieën");
    setProfiel(profiel);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

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
    getNavigation().goBackToPreviousPage();
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        Page8Profielen.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<PleCategorieContainer> pleCategorieContainers = getSelectedPleCategorieContainers(wholeTable);
    List<PleCategorie> allSelected = getSelectedPleCategories(pleCategorieContainers);
    koppelOntkoppelProfielen(koppelActie, allSelected, profiel);

    setTableStatus(wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        Page8Profielen.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<PleCategorieContainer> getSelectedPleCategorieContainers(boolean wholeTable) {

    List<PleCategorieContainer> l;

    if (wholeTable) {
      l = table.getAllValues(PleCategorieContainer.class);
    } else {
      l = table.getSelectedValues(PleCategorieContainer.class);
    }

    return l;
  }

  private List<PleCategorie> getSelectedPleCategories(List<PleCategorieContainer> pleCategorieContainers) {

    Set<PleCategorie> selectedPleCategories = new HashSet<>(); // set voorkomt dubbele items

    for (PleCategorieContainer pleCategorieContainer : pleCategorieContainers) {
      selectedPleCategories.add(pleCategorieContainer.getPleCategorie());
    }

    return new ArrayList<>(selectedPleCategories);
  }

  private Object getStatus(KoppelActie koppelActie) {

    if (KOPPEL.equals(koppelActie)) {
      return setClass("green", "Wel historie");
    }

    // ontkoppelen
    return setClass("red", "Geen historie");
  }

  private void setTable() {

    table = new GBACategorieenTable(getProfiel()) {

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
