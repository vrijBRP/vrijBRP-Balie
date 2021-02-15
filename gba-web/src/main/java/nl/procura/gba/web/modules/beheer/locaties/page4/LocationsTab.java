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

package nl.procura.gba.web.modules.beheer.locaties.page4;

import java.util.List;
import java.util.Set;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class LocationsTab extends NormalPageTemplate {

  private static final int    MAX_PRINTOPTIESTOSHOW = 3;
  private GbaTable            locTable              = null;
  private final List<Locatie> locList;

  public LocationsTab(List<Locatie> locations) {
    this.locList = locations;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      locTable = new GbaTable() {

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("ID", 40);
          addColumn("Locatie", 150);
          addColumn("Omschrijving");
          addColumn("Aantal", 100);
          addColumn("Gekoppelde printopties");
        }
      };

      addComponent(locTable);
    } else if (event.isEvent(LoadPage.class)) {

      locTable.getRecords().clear();

      for (Locatie loc : locList) {

        Record r = locTable.addRecord(loc);
        Set<PrintOptie> printOpties = getServices().getLocatieService().getPrintOptions(loc);
        String poToShow = MiscUtils.abbreviate(printOpties, MAX_PRINTOPTIESTOSHOW);

        r.addValue(loc.getCLocation());
        r.addValue(loc.getLocatie());
        r.addValue(loc.getOmschrijving());
        r.addValue(printOpties.size());
        r.addValue(poToShow);
      }

      locTable.reloadRecords();
    }

    super.event(event);
  }
}
