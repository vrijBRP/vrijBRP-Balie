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

package nl.procura.gba.web.modules.zaken.tmv.page3;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.tmv.layouts.reacties.TmvReactieLayout;
import nl.procura.gba.web.modules.zaken.tmv.page10.Page10Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page4.Page4Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page5.Page5Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page6.Page6Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page9.Page9Tmv;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

public class Page3Tmv extends ZakenListPage<TerugmeldingAanvraag> {

  public Page3Tmv() {
    super("Terugmeldingen");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.TERUGMELDING);
  }

  @Override
  public void onNew() {
    getApplication().getProcess().startProcess();
    getNavigation().goToPage(new Page9Tmv());
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("&nbsp;", 20).setClassType(Embedded.class);
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Verwerkt", 80);
    table.addColumn("Reacties", 70);
    table.addColumn("Toegevoegd door", 120);
    table.addColumn("Verwerkt door", 120);
    table.addColumn("Melding");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<TerugmeldingAanvraag> zaakRecord) {
    TerugmeldingAanvraag zaak = zaakRecord.getZaak();
    zaak = Services.getInstance().getZakenService().getVolledigeZaak(zaak);
    getNavigation().goToPage(new Page3TmvTabPage(zaak));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<TerugmeldingAanvraag> zaakRecord) {

    TerugmeldingAanvraag zaak = zaakRecord.getZaak();
    TableImage res = null;

    if (fil(zaak.getWaarschuwing().getMsg())) {
      res = new TableImage(Icons.getIcon(Icons.ICON_WARN));
    }

    record.getValues().get(0).setValue(res);
    record.getValues().get(1).setValue(zaakRecord.getNr());
    record.getValues().get(2).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(3).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(4).setValue(zaak.getDatumTijdAfhandeling().getFormatDate());
    record.getValues().get(5).setValue(zaak.getReacties().size());
    record.getValues().get(6).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(7).setValue(zaak.getAfgehandeldDoor().getDescription());
    record.getValues().get(8).setValue(zaak.getTerugmelding());
  }

  public class Page3TmvTabPage extends ZakenOverzichtPage<TerugmeldingAanvraag> {

    private Page3TmvTabPage(TerugmeldingAanvraag tmv) {
      super(tmv, "Terugmelding: overzicht");
    }

    @Override
    protected void addOptieButtons() {
      addOptieButton(buttonDoc);
    }

    @Override
    protected void addTabs() {

      addTab(new Page4Tmv(getZaak()), "Terugmelding");
      addTab(new Page5Tmv(getZaak()), "Afhandeling");
      addTab(new Page6Tmv(getZaak()), "Landelijke registratie");

      TmvReactieLayout tmvReactieLayout = new TmvReactieLayout(getZaak());
      tmvReactieLayout.setTab(addTab(tmvReactieLayout, tmvReactieLayout.getHeader()));
    }

    @Override
    protected void goToDocument() {
      getNavigation().goToPage(new Page10Tmv(getZaak()));
    }
  }
}
