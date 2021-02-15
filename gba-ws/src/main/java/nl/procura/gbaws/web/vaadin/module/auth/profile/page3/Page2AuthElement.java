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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gbaws.db.handlers.ProfileDao;
import nl.procura.gbaws.db.handlers.ProfileDao.ElementWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.gbaws.export.elements.ElementsExportUtils;
import nl.procura.gbaws.web.vaadin.module.auth.ModuleAuthPage;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.ElementsProfile;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page3.upload.ElementsUploadWindow;
import nl.procura.gbaws.web.vaadin.module.auth.profile.tasks.ElementsImportTask;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.StreamDownloader;
import nl.procura.vaadin.functies.task.VaadinTaskWindow;

public class Page2AuthElement extends ModuleAuthPage {

  protected Button buttonKoppel    = new Button("Koppel");
  protected Button buttonOntkoppel = new Button("Ontkoppel");
  protected Button buttonExport    = new Button("Exporteren");
  protected Button buttonImport    = new Button("Importeren");

  private final ElementsProfile ep;
  private Page2AuthElementForm  form;
  private Page2AuthElementTable table;

  public Page2AuthElement(ElementsProfile button) {
    this.ep = button;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    List<ElementWrapper> elements = table.getSelectedValues(ElementWrapper.class);

    if (button == buttonKoppel) {

      ProfileDao.koppeling(ep, elements);
    } else if (button == buttonOntkoppel) {

      ProfileDao.ontKoppeling(ep, elements);
    } else if (button == buttonExport) {

      onExport();
    } else if (button == buttonImport) {

      onImport();
    }

    table.reload(table.getSelectedRecords());

    super.handleEvent(button, keyCode);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonKoppel);
      addButton(buttonOntkoppel);
      addButton(buttonExport);
      addButton(buttonImport);

      form = new Page2AuthElementForm(ep);

      table = new Page2AuthElementTable(ep) {

        @Override
        protected void koppel(ElementWrapper elem) {
          ProfileDao.switchKoppeling(ep, asList(elem));
        }
      };

      Page2AuthElementFilter filter = new Page2AuthElementFilter(table);

      addComponent(form);
      addComponent(new Fieldset("BRP elementen", filter));
      addExpandComponent(table);
    }

    super.event(event);
  }

  private void onExport() {

    String filename = ep.getProfile().getNaam().replaceAll("\\s+", "").toLowerCase() + ".json";
    List<ProfielElement> elementen = ep.getProfile().getElementen(ep.getDatabaseType().getCode(),
        ep.getRefDatabase());

    byte[] bytes = ElementsExportUtils.export(elementen).toByteArray();
    StreamDownloader.download(new ByteArrayInputStream(bytes), getWindow(), filename, true);
  }

  private void onImport() {
    getParentWindow().addWindow(new ElementsUploadWindow() {

      @Override
      public void onUpload(File file, SucceededEvent event) {
        getParentWindow().addWindow(new ElementsImportWindow(file));
        closeWindow();
      }
    });
  }

  public class ElementsImportWindow extends VaadinTaskWindow {

    public ElementsImportWindow(File file) {
      super(new ElementsImportTask(ep, file));
      setCaption("Elementen importeren");
    }

    @Override
    public void closeWindow() {
      table.init();
      super.closeWindow();
    }
  }
}
