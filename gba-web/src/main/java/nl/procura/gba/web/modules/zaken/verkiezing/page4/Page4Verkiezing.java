/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.verkiezing.page4;

import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class Page4Verkiezing extends NormalPageTemplate {

  private final List<KiesrWijz> wijzigingen;

  public Page4Verkiezing(List<KiesrWijz> wijzigingen) {
    this.wijzigingen = wijzigingen;
    addButton(buttonClose);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event instanceof InitPage) {
      addExpandComponent(new Table());
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {
      addColumn("&nbsp;", 30);
      addColumn("Datum", 80);
      addColumn("Tijd", 70);
      addColumn("Stempas", 100);
      addColumn("Aanduiding");
      addColumn("Opmerking");
      addColumn("Gebruiker");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      int nr = wijzigingen.size();
      for (KiesrWijz kiesrWijz : wijzigingen) {
        IndexedTable.Record r = addRecord(kiesrWijz);
        r.addValue(nr--);
        r.addValue(new ProcuraDate(kiesrWijz.getdIn().toString()).getFormatDate());
        r.addValue(new ProcuraDate().setStringTime(kiesrWijz.gettIn().toString()).getFormatTime());
        r.addValue(kiesrWijz.getKiesrStem().getPasNr());
        r.addValue(KiezersregisterActieType.get(kiesrWijz.getActietype()));
        r.addValue(kiesrWijz.getOpm());
        r.addValue(kiesrWijz.getUsr().getUsrfullname());
      }

      super.setRecords();
    }
  }
}
