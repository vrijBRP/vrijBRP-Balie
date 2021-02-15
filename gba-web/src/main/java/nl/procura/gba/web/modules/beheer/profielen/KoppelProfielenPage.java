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

package nl.procura.gba.web.modules.beheer.profielen;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.modules.beheer.overig.ProfielKoppelTabel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Deze klasse wordt gebruikt in beheer om 'koppelbaar aan profiel' objecten te koppelen
 * aan profielen.
 *

 * <p>
 * 2012
 */

public class KoppelProfielenPage<K extends KoppelbaarAanProfiel> extends KoppelPage {

  private ProfielKoppelTabel<K> table = null;
  private final List<K>         koppelList;
  private final KoppelForm      form;
  private final String          type;

  public KoppelProfielenPage(List<K> koppelList, String type) {

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
          + "De status is 'Gekoppeld' als <b>alle</b> geselecteerde " + type + " gekoppeld zijn aan het profiel.");

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

        KoppelProfielenPage.this.couple(koppelActie, wholeTable);
      }
    };

  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<Profiel> selectedProfiles = getSelectedProfiles(wholeTable);

    koppelActieProfielen(koppelActie, koppelList, selectedProfiles);

    table.setTableStatus(koppelActie, wholeTable ? table.getRecords() : table.getSelectedRecords());
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelProfielenPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<Profiel> getSelectedProfiles(boolean wholeTable) {

    List<Profiel> l;

    if (wholeTable) {
      l = table.getAllValues(Profiel.class);
    } else {
      l = table.getSelectedValues(Profiel.class);
    }

    return l;
  }

  private void setTable() {

    table = new ProfielKoppelTabel<K>(koppelList) {

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
