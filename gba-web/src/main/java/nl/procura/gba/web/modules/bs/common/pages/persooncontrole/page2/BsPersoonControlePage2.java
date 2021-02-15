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

package nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page2;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.DossierPersoonSynchronizer.Element;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page1.BsPersoonControlePage1.SyncRecord;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Controle op persoonsgegevens
 */
public class BsPersoonControlePage2 extends ButtonPageTemplate {

  private final SyncRecord syncRecord;
  private ElementenTable   table = null;

  public BsPersoonControlePage2(SyncRecord syncRecord) {
    this.syncRecord = syncRecord;
    buttonClose.addListener(this);

    H2 h2 = new H2(syncRecord.getHuidigePersoon().getNaam().getPred_adel_voorv_gesl_voorn());
    HLayout hLayout = new HLayout().widthFull().add(h2).add(buttonClose);
    hLayout.setComponentAlignment(buttonClose, Alignment.MIDDLE_RIGHT);
    addComponent(hLayout);
    addComponent(new Label("Wijzigingen: <b>" + syncRecord.getWijzigingen() + "</b>", Label.CONTENT_XHTML));
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      setTable(new ElementenTable());

      addExpandComponent(getTable());
    }

    super.event(event);
  }

  public ElementenTable getTable() {
    return table;
  }

  public void setTable(ElementenTable table) {
    this.table = table;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  public class ElementenTable extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Gegeven");
      addColumn("&nbsp;", 15);
      addColumn("Oud gegeven", 300).setUseHTML(true);
      addColumn("Nieuw gegeven", 300).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      List<Element> elementen = syncRecord.getSynchronizer().getElementen();
      for (Element element : elementen) {

        Record r = addRecord(element);
        r.addValue(element.getNaam());
        r.addValue(getIcon(element));

        Value oudeWaarde = r.addValue(formatWaarde(element.getHuidigeWaarde()));
        if (!element.isMatch()) {
          oudeWaarde.setStyleName("red");
        }

        String nieuw = formatWaarde(element.getNieuweWaarde());
        Value nieuweWaarde = r.addValue(syncRecord.getSynchronizer().isPersoonGevonden() ? nieuw : "");
        if (!element.isMatch()) {
          nieuweWaarde.setStyleName("green");
        }
      }

      super.setRecords();
    }

    /**
     * Wijzig 'False' naar Nee en 'True' naar Ja.
     */
    private String formatWaarde(Object waarde) {
      if (Boolean.FALSE.equals(waarde)) {
        return "Nee";
      } else if (Boolean.TRUE.equals(waarde)) {
        return "Ja";
      }
      return astr(waarde);
    }

    private Embedded getIcon(Element element) {
      return new Embedded("",
          new ThemeResource(Icons.getIcon(element.isMatch() ? Icons.ICON_OK : Icons.ICON_WARN)));
    }
  }
}
