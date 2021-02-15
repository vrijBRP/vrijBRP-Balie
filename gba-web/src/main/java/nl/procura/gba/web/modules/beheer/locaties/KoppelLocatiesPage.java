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

package nl.procura.gba.web.modules.beheer.locaties;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.modules.beheer.overig.LocatieKoppelTabel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.locatie.KoppelbaarAanLocatie;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Deze klasse wordt wordt gebruikt in beheer bij het koppelen van 'koppelbaar aan locatie objecten'
 * aan locaties. Het type van K kan dus Gebruiker of PrintOptie zijn.
 *

 * <p>
 * 2012
 */

public class KoppelLocatiesPage<K extends KoppelbaarAanLocatie> extends KoppelPage {

  private LocatieKoppelTabel<K> table = null;
  private final List<K>         koppelList;
  private final KoppelForm      form;
  private final String          type;

  public KoppelLocatiesPage(List<K> koppelList, String type) {

    super("");
    this.koppelList = koppelList;
    form = new KoppelForm();
    this.type = type;
    setMargin(false);
  }

  /**
   * Nodig als deze pagina ergens ingevoegd wordt waar al een 'vorige' knop aanwezig is.
   */
  public void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      HorizontalLayout hL = new HorizontalLayout();
      hL.addComponent(form);

      setTable();
      setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen. <br>"
          + "De status is 'Gekoppeld' als <b>alle</b> geselecteerde " + type + " gekoppeld zijn aan de locatie.");

      hL.addComponent(new GbaIndexedTableFilterLayout(table));
      hL.setExpandRatio(form, 1f);

      addComponent(hL);
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelLocatiesPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<Locatie> selectedLocs = getSelectedLocs(wholeTable);

    koppelActieLocaties(koppelActie, koppelList, selectedLocs);

    table.setTableStatus(koppelActie, wholeTable ? table.getRecords() : table.getSelectedRecords());
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelLocatiesPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<Locatie> getSelectedLocs(boolean wholeTable) {

    List<Locatie> l;

    if (wholeTable) {
      l = table.getAllValues(Locatie.class);
    } else {
      l = table.getSelectedValues(Locatie.class);
    }

    return l;
  }

  private void setTable() {

    table = new LocatieKoppelTabel<K>(koppelList) {

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
}
