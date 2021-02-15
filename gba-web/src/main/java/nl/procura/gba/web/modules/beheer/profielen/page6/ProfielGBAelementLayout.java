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

package nl.procura.gba.web.modules.beheer.profielen.page6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class ProfielGBAelementLayout extends KoppelPage {

  private static final int  INDEX_STATUS = 0;
  private Profiel           profiel      = null;
  private GBAelementenTable table        = null;

  private final KoppelForm form;

  public ProfielGBAelementLayout(Profiel profiel, GBACat gbaCategorie) {

    super("Overzicht van gekoppelde BRP-elementen van profiel " + profiel.getProfiel());

    setProfiel(profiel);

    setSpacing(true);
    setMargin(false);

    setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen.");
    form = new KoppelForm();

    HorizontalLayout hL = new HorizontalLayout();
    hL.addComponent(form);

    setTable(new GBAelementenTable(profiel, gbaCategorie) {

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
    });

    hL.addComponent(new GbaIndexedTableFilterLayout(table));
    hL.setExpandRatio(form, 1f);

    addComponent(hL);
    addExpandComponent(table);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  public GBAelementenTable getTable() {
    return table;
  }

  public void setTable(GBAelementenTable table) {
    this.table = table;
  }

  @Override
  public void onPreviousPage() {

    Page6Profielen page = VaadinUtils.getChild(getWindow(), Page6Profielen.class);
    page.getNavigation().goBackToPage(page.getNavigation().getPreviousPage());
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        ProfielGBAelementLayout.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<PleElementContainer> pleContainers = getSelectedPleContainers(wholeTable);
    List<PleElement> allSelected = getSelectedPleElements(pleContainers);
    koppelOntkoppelProfielen(koppelActie, allSelected, profiel);

    setTableStatus(wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        ProfielGBAelementLayout.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<PleElementContainer> getSelectedPleContainers(boolean wholeTable) {

    List<PleElementContainer> l;

    if (wholeTable) {
      l = table.getAllValues(PleElementContainer.class);
    } else {
      l = table.getSelectedValues(PleElementContainer.class);
    }

    return l;
  }

  private List<PleElement> getSelectedPleElements(List<PleElementContainer> pleContainers) {

    Set<PleElement> selectedPleElements = new HashSet<>(); // set voorkomt dubbele items

    for (PleElementContainer pleContainer : pleContainers) {
      selectedPleElements.add(pleContainer.getPleElement());
    }

    return new ArrayList<>(selectedPleElements);
  }

  private void setTableStatus(List<Record> selectedRecords, KoppelActie koppelActie) {

    for (Record r : selectedRecords) {
      table.setRecordValue(r, INDEX_STATUS, koppelActie.getStatus());
    }
  }
}
