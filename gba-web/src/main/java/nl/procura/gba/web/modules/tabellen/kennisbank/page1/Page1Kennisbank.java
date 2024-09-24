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

package nl.procura.gba.web.modules.tabellen.kennisbank.page1;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.tabellen.kennisbank.page2.Page2Kennisbank;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankRecord;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankService;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Page1Kennisbank extends NormalPageTemplate {

  private final Uploader      uploader = new Uploader();
  private Page1KennisbankForm form1    = null;
  private Table1              table1   = null;

  public Page1Kennisbank() {
    super("Overzicht van kennisbankgegevens");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page1KennisbankForm() {

        @Override
        public void onDelete() {
          getServices().getKennisbankService().getBestandInConfig().delete();
          check();
        }
      };

      table1 = new Table1();
      check();

      addComponent(new Fieldset("Uploaden van bestand", uploader));
      addComponent(form1);
      addComponent(new Fieldset("Gegevens"));
      addExpandComponent(table1);

      getMainbuttons().addComponent(new GbaIndexedTableFilterLayout(table1));
    } else if (event.isEvent(AfterReturn.class)) {
      table1.init();
    }

    super.event(event);
  }

  public void check() {
    KennisbankService service = getServices().getKennisbankService();
    if (service.isBestandInConfig()) {
      form1.set("Geüpload bestand", service.getUitgave(), true);
    } else {
      form1.set("Meegeleverd bestand", service.getUitgave(), false);
    }

    table1.init();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Kennisbank((KennisbankRecord) record.getObject()));
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nationaliteit", 350);
      addColumn("Land");
    }

    @Override
    public void setRecords() {

      for (KennisbankRecord kb : getServices().getKennisbankService().getRecords()) {

        Record r = addRecord(kb);
        r.addValue(kb.getNatio());
        r.addValue(kb.getLand());
      }

      super.setRecords();
    }
  }

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(final SucceededEvent event) {

      if (!getFileName().toLowerCase().endsWith("csv")) {
        addMessage("Het is geen .csv bestand", Icons.ICON_ERROR);
      } else {

        try {
          KennisbankService service = getServices().getKennisbankService();
          FileUtils.copyFile(getFile(), service.getBestandInConfig());
          check();
          super.uploadSucceeded(event);
        } catch (IOException e) {
          throw new ProException(ProExceptionSeverity.ERROR, "Bestand kan niet worden opgeslagen.", e);
        }
      }
    }
  }
}
