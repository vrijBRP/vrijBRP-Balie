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

package nl.procura.gba.web.modules.beheer.verkiezing.page3;

import static nl.procura.gba.web.modules.beheer.verkiezing.page3.Page3VerkiezingBean.F_AANTAL;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Date;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2Verkiezing;
import nl.procura.gba.web.modules.beheer.verkiezing.page3.exporting.ROSExportWindow;
import nl.procura.gba.web.modules.beheer.verkiezing.page4.Page4Verkiezing;
import nl.procura.gba.web.modules.beheer.verkiezing.page7.Page7Verkiezing;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.StempasQuery;
import nl.procura.gba.web.services.beheer.verkiezing.StempasResult;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class Page3Verkiezing extends NormalPageTemplate {

  private final Button    buttonRosPrint  = new Button("ROS afdrukken");
  private final Button    buttonRosExport = new Button("ROS exporteren");
  private final KiesrVerk verkiezing;

  private Page3VerkiezingForm  form;
  private Page3VerkiezingTable table;

  public Page3Verkiezing(KiesrVerk verkiezing) {
    super("Kiezersregister");
    this.verkiezing = verkiezing;
    addButton(buttonPrev, buttonReset, buttonSearch);
    addButton(buttonRosPrint);
    addButton(buttonRosExport, 1f);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      form = new Page3VerkiezingForm();
      table = new Page3VerkiezingTable();
      addComponent(form);
      addComponent(new InfoLayout("De eerste 1000 records worden getoond."));
      addExpandComponent(table);
      refreshTable();

    } else if (event.isEvent(AfterReturn.class)) {
      refreshTable();
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(Page2Verkiezing.class);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonRosPrint) {
      printKiezersregister();

    } else if (button == buttonRosExport) {
      getApplication().getParentWindow().addWindow(new ROSExportWindow(verkiezing));
    }
    super.handleEvent(button, keyCode);
  }

  private void printKiezersregister() {
    getNavigation().goToPage(new Page7Verkiezing(verkiezing));
  }

  @Override
  public void onNew() {
    form.reset();
    onSearch();
  }

  @Override
  public void onSearch() {
    form.commit();
    Page3VerkiezingBean bean = form.getBean();
    StempasResult result = getApplication().getServices().getKiezersregisterService().getStempassen(StempasQuery
        .builder(verkiezing)
        .vnr(isNotBlank(bean.getVnr()) ? Long.valueOf(bean.getVnr()) : null)
        .anrKiesgerechtigde(form.getAnrKiesgerechtigde())
        .anrGemachtigde(form.getAnrGemachtigde())
        .opgenomenInROS(bean.getRos())
        .aanduidingType(bean.getAanduiding())
        .datumAanduidingVan(dateToLong(bean.getDatumVan()))
        .datumAanduidingTm(dateToLong(bean.getDatumTm()))
        .max(1000)
        .build());
    table.setResult(result);
    form.getField(F_AANTAL).setValue(result.getAantal() + " / " + result.getTotaal());
    form.repaint();
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<KiesrStem>(table, false) {

      @Override
      public void deleteValue(KiesrStem kiesrStem) {
        getServices().getKiezersregisterService().delete(kiesrStem);
      }

      @Override
      protected void afterDelete() {
        onSearch();
      }
    };
  }

  private Long dateToLong(Date date) {
    if (date != null) {
      return Long.parseLong(new ProcuraDate(date).getSystemDate());
    }
    return null;
  }

  private void refreshTable() {
    onSearch();
    table.init();
  }

  class Page3VerkiezingTable extends GbaTable {

    private StempasResult result;

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Pasnr.", 110);
      addColumn("A-nummer", 110);
      addColumn("Toegevoegd", 90);
      addColumn("Aanduiding");
      addColumn("Aanduiding tijdstip", 150);
      addColumn("Naam");
      addColumn("Adres");
      addColumn("Postcode / Woonplaats");
      super.setColumns();
    }

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page4Verkiezing(verkiezing, record.getObject(KiesrStem.class)));
    }

    @Override
    public void setRecords() {
      if (result != null) {
        for (KiesrStem stem : result.getStempassen()) {
          IndexedTable.Record r = addRecord(stem);
          Stempas stempas = new Stempas(stem);
          r.addValue(stempas.getPasnummer());
          r.addValue(stempas.getAnr().getFormatAnummer());
          r.addValue(stempas.isToegevoegd() ? "Ja" : "Nee");
          r.addValue(stempas.getAanduidingType().getOms());
          r.addValue(stempas.getAanduidingTijdstip());
          r.addValue(stempas.getNaam());
          r.addValue(stempas.getAdres());
          r.addValue(stempas.getPostcodeWoonplaats());
        }
      }
      super.setRecords();
    }

    public void setResult(StempasResult result) {
      this.result = result;
      init();
    }
  }
}
