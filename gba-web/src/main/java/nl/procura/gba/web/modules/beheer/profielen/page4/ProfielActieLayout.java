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

package nl.procura.gba.web.modules.beheer.profielen.page4;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class ProfielActieLayout extends KoppelPage {

  private ActieTabel       table;
  private final Profiel    profiel;
  private final KoppelForm form;

  public ProfielActieLayout(Profiel profiel, ProfielActieType profielActieType) {

    super("Overzicht van acties van profiel " + profiel.getProfiel());
    this.profiel = profiel;

    setSpacing(true);
    setMargin(false);

    setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen.");
    form = new KoppelForm();

    HorizontalLayout hL = new HorizontalLayout();
    hL.addComponent(form);

    setTable(new ActieTabel(profiel, profielActieType) {

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

  public ActieTabel getTable() {
    return table;
  }

  public void setTable(ActieTabel table) {
    this.table = table;
  }

  @Override
  public void onPreviousPage() {

    Page4Profielen page = VaadinUtils.getChild(getWindow(), Page4Profielen.class);
    page.getNavigation().goBackToPage(page.getNavigation().getPreviousPage());
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        ProfielActieLayout.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<Actie> allSelectedActions = getSelectedActions(wholeTable);

    koppelOntkoppelProfielen(koppelActie, allSelectedActions, profiel);
    setTableStatus(wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, "profielen", false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        ProfielActieLayout.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<Actie> getSelectedActions(boolean wholeTable) {

    List<Actie> l;

    if (wholeTable) {
      l = table.getAllValues(Actie.class);
    } else {
      l = table.getSelectedValues(Actie.class);
    }

    return l;
  }

  private void setTableStatus(List<Record> selectedRecords, KoppelActie koppelActie) {

    for (Record r : selectedRecords) {
      table.setRecordValue(r, 0, koppelActie.getStatus());
    }
  }
}
