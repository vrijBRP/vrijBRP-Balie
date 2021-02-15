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

package nl.procura.gba.web.modules.zaken.rijbewijs.page1;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.buttons.ActieButton;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page12.Page12Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page3.Page3Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page7.Page7Rijbewijs;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1658ToAanvraag;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.messages.P1658;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht rijbewijzen
 */
public class Page1Rijbewijs extends RijbewijsPage {

  private final Button   buttonRaadplegen        = new Button("Raadplegen");
  private final Button   buttonLaatste           = new ActieButton(ProfielActieType.UPDATE,
      "Laatste aanvraag");
  private final Button   buttonUitreikenAfkeuren = new ActieButton(ProfielActieType.UPDATE,
      "Uitreiken / Niet uitreiken");
  private ZakenListTable table                   = null;

  public Page1Rijbewijs() {

    super("Rijbewijsaanvragen");
    setMargin(true);
  }

  @Override
  public void event(nl.procura.vaadin.component.layout.page.pageEvents.PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);

      if (getPl().getDatasource().is(PLEDatasource.PROCURA)) {
        addButton(buttonNew);
      }

      addButton(buttonStatus);
      addButton(buttonDel);

      table = new ZakenListTable(new ZaakArgumenten(getPl(), ZaakType.RIJBEWIJS)) {

        @Override
        public void onDoubleClick(Record record) {
          selectRecord(record);
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          setMultiSelect(true);

          addColumn("Nr", 30);
          addColumn("Ingevoerd op", 130);
          addColumn("Status", 120).setUseHTML(true);
          addColumn("Ingevoerd door", 200);
          addColumn("Aanvraagnummer", 150);
          addColumn("Soort aanvraag");
          addColumn("Actuele status CRB");
        }

        @Override
        protected void loadZaak(Record record, ZaakRecord zaakRecord) {
          RijbewijsAanvraag zaak = (RijbewijsAanvraag) zaakRecord.getZaak();
          zaak = getServices().getRijbewijsService().getStandardZaak(zaak);
          record.getValues().get(0).setValue(zaakRecord.getNr());
          record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
          record.getValues().get(2).setValue(zaak.getStatus());
          record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
          record.getValues().get(4).setValue(zaak.getAanvraagNummer());
          record.getValues().get(5).setValue(zaak.getSoortAanvraag());
          record.getValues().get(6).setValue(zaak.getStatussen().getStatus().getStatus());
        }

        @Override
        public void onReload() {
          table.init();
        }
      };

      OptieLayout ol = new OptieLayout();

      ol.getLeft().addExpandComponent(table);
      ol.getRight().setWidth("200px");

      if (getPl().getDatasource().is(PLEDatasource.PROCURA)) {
        ol.getRight().addButton(buttonLaatste, this);
        ol.getRight().addButton(buttonRaadplegen, this);
      }

      ol.getRight().addButton(buttonUitreikenAfkeuren, this);

      table.getSearchLayout().addToLayout(getButtonLayout());

      addExpandComponent(ol);
    } else if (event.isEvent(AfterReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonLaatste) {
      stuur1658F1Aanvraag(); // Laatste aanvraag
    } else if (button == buttonUitreikenAfkeuren) {
      stuur1658F1Uitreiking(); // Laatste aanvraag
    } else if (button == buttonRaadplegen) {
      stuur0252f1();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    table.init();
  }

  @Override
  public void onNew() {
    if (buttonNew.getParent() != null) {
      IdentificatieContactUtils.startProcess(this, new Page3Rijbewijs(), true);
    }

    super.onNew();
  }

  private P1658 getp1658f2() {
    P1658 p1658 = new P1658();
    p1658.newF1(getPl().getPersoon().getAnr().getVal());
    return sendMessage(p1658) ? p1658 : null;
  }

  private void selectRecord(Record record) {
    RijbewijsAanvraag zaak = (RijbewijsAanvraag) record.getObject(ZaakRecord.class).getZaak();
    getNavigation().goToPage(new Page2Rijbewijs(zaak));
  }

  /**
   * Raadplegen persoonsgegevens op fiscaal nummer (bsn)
   */
  private void stuur0252f1() {
    P0252 p0252 = new P0252();
    p0252.newF1(getPl().getPersoon().getBsn().getVal());

    if (sendMessage(p0252)) {
      getNavigation().goToPage(new Page4Rijbewijs(p0252, false));
    }
  }

  /**
   * Raadpleeg meest recente aanvraag
   */
  private void stuur1658F1Aanvraag() {
    P1658 p1658 = getp1658f2();
    if (p1658 != null) {
      RijbewijsAanvraag aanvraag = P1658ToAanvraag.get(p1658, getPl(), getServices());
      getNavigation().goToPage(new Page7Rijbewijs(aanvraag, p1658));
    }
  }

  /**
   * Raadpleeg meest recente aanvraag. Tbv de uitreiking / afkeuring
   */
  private void stuur1658F1Uitreiking() {
    P1658 p1658 = getp1658f2();
    if (p1658 != null) {
      getNavigation().goToPage(new Page12Rijbewijs(p1658));
    }
  }
}
