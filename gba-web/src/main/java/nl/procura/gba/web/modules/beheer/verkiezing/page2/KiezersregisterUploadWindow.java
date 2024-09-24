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

package nl.procura.gba.web.modules.beheer.verkiezing.page2;

import static nl.procura.gba.common.MiscUtils.to;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.io.IOUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Upload;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezingsbestand;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.task.VaadinTaskOptions;
import nl.procura.vaadin.functies.task.VaadinTaskWindow;

import au.com.bytecode.opencsv.CSVReader;
import lombok.Data;

public class KiezersregisterUploadWindow extends GbaModalWindow {

  private static final String F_VERKIEZING = "verkiezing";
  private static final String F_NIEUWE     = "nieuwe";
  private static final String F_BESTAANDE  = "bestaande";

  private Verkiezingsbestand verkiezingsbestand;
  private final KiesrVerk    verkiezing;

  public KiezersregisterUploadWindow(KiesrVerk verkiezing) {
    super("Uploaden (Escape om te sluiten)", "500px");
    this.verkiezing = verkiezing;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new UploadPage()));
  }

  public class UploadPage extends NormalPageTemplate {

    private final Uploader docUploader  = new Uploader();
    private final Form     form         = new Form();
    private final Button   importButton = new Button("Importeren");

    public UploadPage() {
      super("");
      setInfo("", "Selecteer het bestand om te importeren");
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        addComponent(docUploader);
        addComponent(form);
        addComponent(importButton);
        importButton.addListener(this);
        importButton.setWidth("100%");
        importButton.setEnabled(false);
      }
      super.event(event);
      buttonClose.focus();
    }

    @Override
    public void handleEvent(Button button, int keyCode) {
      if (button == importButton) {
        importBestand();
      }
      super.handleEvent(button, keyCode);
    }

    private void importBestand() {
      getGbaApplication().getParentWindow().addWindow(new ImportWindow(verkiezingsbestand) {

        @Override
        public void closeWindow() {
          KiezersregisterUploadWindow.this.closeWindow();
          super.closeWindow();
        }
      });
    }

    @Override
    public void onClose() {
      to(getWindow(), GbaModalWindow.class).closeWindow();
      super.onClose();
    }

    public class ImportWindow extends VaadinTaskWindow {

      public ImportWindow(Verkiezingsbestand verkiezingsbestand) {
        super(new VerkiezingsbestandImportTask(getGbaApplication(), verkiezingsbestand),
            new VaadinTaskOptions()
                .setShowLog(true));
        setCaption("Bestand verwerken");
      }

      @Override
      public void closeWindow() {
        super.closeWindow();
      }
    }

    public class Uploader extends DocUploader {

      @Override
      public void uploadSucceeded(Upload.SucceededEvent event) {

        try {
          importButton.setEnabled(false);
          verkiezingsbestand = new Verkiezingsbestand(verkiezing);
          CSVReader reader = null;
          try {
            reader = new CSVReader(new FileReader(getFile()), ';');
            int i = 1;
            for (String[] line : reader.readAll()) {
              verkiezingsbestand.add(i, line);
              i++;
            }
          } catch (IOException e) {
            throw new ProException(ProExceptionSeverity.ERROR, "Fout bij inlezen CSV");
          } finally {
            IOUtils.closeQuietly(reader);
          }

          verkiezingsbestand.checkDuplicates(getServices().getKiezersregisterService());
          form.setVerkiezingsbestand(verkiezingsbestand);
          importButton.setEnabled(true);
          super.uploadSucceeded(event);
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form() {
      setOrder(F_VERKIEZING, F_NIEUWE, F_BESTAANDE);
      setColumnWidths(WIDTH_130, "");
      setBean(new Bean());
    }

    public void setVerkiezingsbestand(Verkiezingsbestand bestand) {
      Bean bean = new Bean();
      bean.setVerkiezing(bestand.getVerkiezingInfo());
      bean.setNieuwe(bestand.getNieuweRegels().size());
      bean.setBestaande(bestand.getBestaandeRegels().size());
      setBean(bean);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(caption = "Verkiezing", readOnly = true)
    private String verkiezing = "";

    @Field(caption = "Nieuwe regels", readOnly = true)
    private Integer nieuwe = 0;

    @Field(caption = "Bestaande regels", readOnly = true)
    private Integer bestaande = 0;
  }
}
