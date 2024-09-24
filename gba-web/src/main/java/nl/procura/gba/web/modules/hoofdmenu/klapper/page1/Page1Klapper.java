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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page1;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.KlapperGatenLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.PageKlapperTemplate;
import nl.procura.gba.web.modules.hoofdmenu.klapper.page2.Page2Klapper;
import nl.procura.gba.web.modules.hoofdmenu.klapper.page3.Page3Klapper;
import nl.procura.gba.web.modules.hoofdmenu.klapper.page4.Page4Klapper;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperUtils;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

public class Page1Klapper extends PageKlapperTemplate {

  private final Button buttonGaten     = new Button("Toon gaten");
  private final Button buttonResetForm = new Button("Reset");
  private final Button buttonPrint     = new Button("Afdrukken");

  private final List<DossierAkte> aanvragen      = new ArrayList<>();
  private Table                   table          = null;
  private Page1KlapperForm        form           = null;
  private KlapperGatenLayout      gatenLayout    = null;
  private KlapperZoekargumenten   zoekargumenten = null;

  public Page1Klapper() {
    super("Klapper - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonSearch);
      addButton(buttonNew);
      addButton(buttonDel);
      addButton(buttonGaten);
      addButton(buttonResetForm);
      addButton(buttonPrint);

      form = new Page1KlapperForm(getApplication()) {

        @Override
        public void update(KlapperZoekargumenten za) {
          zoekargumenten = za;
          if (table != null) {
            table.init();
          }
        }
      };

      // Alle componenten
      table = new Table();
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      addComponent(form);
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGaten) {
      getNavigation().goToPage(new Page2Klapper(zoekargumenten));
    } else if (button == buttonResetForm) {
      form.reset();
    } else if (button == buttonPrint) {
      if (zoekargumenten.getVolgorde().isAfdrukbaar()) {
        List<DossierAkte> aktes = KlapperUtils.appenderen(aanvragen, zoekargumenten.getVolgorde());
        Page4Klapper page = new Page4Klapper(aktes, zoekargumenten);
        getNavigation().goToPage(page);
      } else {
        throw new ProException(INFO, "Er kan alleen worden afgedrukt met een <b>oplopende</b> volgorde.");
      }
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<DossierAkte>(table) {

      @Override
      public void deleteValue(DossierAkte akte) {
        getServices().getAkteService().deleteAkte(null, akte);
      }

      @Override
      protected void afterDelete() {
        table.init();
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page3Klapper(new DossierAkte(new DateTime()), true));
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {
    table.init();
  }

  private void selectRecord(Record record) {
    DossierAkte dossierAkte = record.getObject(DossierAkte.class);
    getNavigation().goToPage(new Page3Klapper(dossierAkte, false));
  }

  public class Table extends GbaTable {

    public TableImage getIcon(DossierAkte akte) {

      boolean isCorrect = !akte.isDossierVerwerkt() || !akte.isCorrect();
      return isCorrect ? new TableImage(Icons.getIcon(Icons.ICON_WARN)) : null;
    }

    @Override
    public void onDoubleClick(Record record) {
      selectRecord(record);
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Nr", 40);
      addColumn("Datum akte", 130);
      addColumn("Akte", 100);
      addColumn("Datum feit", 130);
      addColumn("Invoertype", 110);
      addColumn("Soort", 190);
      addColumn("Persoon");
      addColumn("Eventuele 2e persoon");
      addColumn("Opmerking", 180);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      aanvragen.clear();
      aanvragen.addAll(getServices().getAkteService().getAktes(zoekargumenten));

      if (gatenLayout != null) {
        removeComponent(gatenLayout);
      }

      gatenLayout = new KlapperGatenLayout(aanvragen, zoekargumenten, true, true);
      addComponent(gatenLayout, getComponentIndex(table));

      boolean isOp = form.isOplopend();
      int nr = isOp ? 1 : aanvragen.size();

      for (DossierAkte akte : aanvragen) {
        Record r = addRecord(akte);
        String styleName = gatenLayout.isVerkeerdeVolgorde(akte) ? "red" : "";

        r.addValue(getIcon(akte));
        r.addValue(nr).setStyleName(styleName);
        r.addValue(akte.getDatumIngang()).setStyleName(styleName);
        r.addValue(akte.getAkte()).setStyleName(styleName);
        r.addValue(akte.getDatumFeit()).setStyleName(styleName);
        r.addValue(akte.getInvoerType()).setStyleName(styleName);
        r.addValue(akte.getAkteRegistersoort()).setStyleName(styleName);
        r.addValue(akte.getAktePersoon().getNaam().getNaam_naamgebruik_eerste_voornaam())
            .setStyleName(styleName);
        r.addValue(akte.getAktePartner().getNaam().getNaam_naamgebruik_eerste_voornaam())
            .setStyleName(styleName);
        r.addValue(akte.getOpm()).setStyleName(styleName);

        nr = isOp ? (nr + 1) : (nr - 1);
      }
    }
  }
}
