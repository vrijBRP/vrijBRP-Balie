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

package nl.procura.gba.web.modules.zaken.curatele.page2;

import static nl.procura.gba.common.MiscUtils.to;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.curatele.page1.CuratelePersoon;
import nl.procura.gba.web.modules.zaken.curatele.page1.CurateleUtils;
import nl.procura.gba.web.modules.zaken.curatele.page3.Page3Curatele;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.rechtspraak.namespaces.ccr.CCRWS.Curandus.Beschikkingen.Beschikking;

public class Page2Curatele extends ButtonPageTemplate {

  private final CuratelePersoon persoon;
  private Page2CurateleForm     form  = null;
  private Page2CurateleTable1   table = null;

  public Page2Curatele(CuratelePersoon curatelePersoon) {

    this.persoon = curatelePersoon;

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev, 1f);
      addButton(buttonClose);

      form = new Page2CurateleForm(persoon);

      table = new Page2CurateleTable1();

      addComponent(form);
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    to(getWindow(), GbaModalWindow.class).closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public class Page2CurateleTable1 extends GbaTable {

    public Page2CurateleTable1() {
    }

    @Override
    public void onClick(Record record) {

      getNavigation().goToPage(new Page3Curatele((Beschikking) record.getObject()));
    }

    @Override
    public void setColumns() {

      setSelectable(true);

      addColumn("Nr", 30);
      addColumn("Instantie");
      addColumn("Datum", 80);
      addColumn("Soort");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nr = 0;
      for (Beschikking p : persoon.getPersoon().getCurandus().getBeschikkingen().getBeschikking()) {

        nr++;
        Record r = addRecord(p);

        r.addValue(nr);
        r.addValue(p.getInstantie());
        r.addValue(CurateleUtils.getDatum(p.getDatumBeschikking()).getValue().getDescr());
        r.addValue(p.getBeschikkingsoort());
      }

      super.setRecords();
    }
  }
}
