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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page3;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page3QuickSearch extends NormalPageTemplate {

  private RelatieLijst                  lijst;
  private final QuickSearchPersonConfig config;

  public Page3QuickSearch(QuickSearchPersonConfig config) {
    this.config = config;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      PersonenWsService service = getServices().getPersonenWsService();
      lijst = service.getRelatieLijst(service.getPersoonslijst(config.getId()), false);
      Table table = new Table();
      addExpandComponent(table);
    }
    super.event(event);
  }

  @Override
  public void attach() {
    super.attach();
    VaadinUtils.resetHeight(getWindow());
    getWindow().setWidth("900px");
    getWindow().center();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  class Table extends GbaTable {

    @Override
    public void onDoubleClick(IndexedTable.Record record) {
      config.getSelectListener().select(record.getObject(BasePLExt.class));
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Relatie", 80);
      addColumn("Persoon", 250);
      addColumn("Adres");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      for (Relatie relatie : lijst.getSortedRelaties()) {
        if (relatie.getRelatieType() != RelatieType.AANGEVER) {
          if (!config.isSameAddress() || relatie.isHuisgenoot()) {
            IndexedTable.Record record = addRecord(relatie.getPl());
            record.addValue(relatie.getRelatieType().getOms());
            record.addValue(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
            record.addValue(relatie.getPl().getVerblijfplaats().getAdres().getAdres());
          }
        }
      }
    }
  }
}
