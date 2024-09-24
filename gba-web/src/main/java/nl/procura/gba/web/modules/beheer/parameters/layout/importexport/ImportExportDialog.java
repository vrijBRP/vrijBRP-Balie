/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.parameters.layout.importexport;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.AES256TextEncryptor;

import com.google.gson.Gson;
import com.vaadin.ui.Button;
import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;
import nl.procura.vaadin.theme.twee.Icons;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportExportDialog extends GbaModalWindow {

  private final boolean doImport;

  public ImportExportDialog(boolean doImport) {
    super(doImport ? "Importeren (Escape om te sluiten)" : "Exporteren (Escape om te sluiten)", "500px");
    this.doImport = doImport;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public class Page1 extends ButtonPageTemplate {

    private final Uploader docUploader  = new Uploader();
    private final Button   buttonExport = new Button("Exporteren", this);
    private       Form     form;

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        form = new Form();
        if (doImport) {
          setInfo("", "Overschrijft de algemene parameters met de geïmporteerde parameters");
          addComponent(form);
          addComponent(docUploader);
        } else {
          setInfo("", "Exporteer de algemene parameters naar een bestand");
          buttonExport.setWidth("100px");
          addComponent(form);
          addComponent(buttonExport);
        }
      }

      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {
      if (button == buttonExport) {
        exportParameters();
      }
      super.handleEvent(button, keyCode);
    }

    private void exportParameters() {
      form.commit();
      ParameterExport parameterExport = new ParameterExport();
      getServices().getParameterService().getAllParameters().getAlle()
          .stream()
          .filter(parameter -> parameter.getProfile().getCProfile() == 0L)
          .filter(parameter -> parameter.getUsr().getCUsr() == 0L)
          .map(parm -> new ParameterExportRecord(parm.getParm(), parm.getValue()))
          .collect(Collectors.toList())
          .forEach(parameterExport.getParms()::add);

      String data = new Gson().toJson(parameterExport);
      AES256TextEncryptor encryptor = new AES256TextEncryptor();
      encryptor.setPassword(form.getBean().getWachtwoord());
      String encryptedData = encryptor.encrypt(data);
      ByteArrayInputStream stream = new ByteArrayInputStream(encryptedData.getBytes());
      new DownloadHandlerImpl(getApplication().getParentWindow())
          .download(stream, "parameters.json", true);
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    @Data
    @FormFieldFactoryBean(
        accessType = ElementType.FIELD,
        defaultWidth = "310px")
    public class Bean implements Serializable {

      @Field(customTypeClass = ProTextField.class,
          caption = "Wachtwoord",
          required = true)
      @TextField(nullRepresentation = "")
      private String wachtwoord;
    }

    public class Form extends GbaForm<Bean> {

      public Form() {
        setCaption("Opties");
        setColumnWidths("90px", "");
        setOrder("wachtwoord");
        setBean(new Bean());
      }
    }

    private class Uploader extends DocUploader {

      @Override
      public void uploadSucceeded(SucceededEvent event) {
        try {
          form.commit();
          // Decrypt
          String data = IOUtils.toString(Files.newInputStream(getFile().toPath()), UTF_8);
          AES256TextEncryptor encryptor = new AES256TextEncryptor();
          encryptor.setPassword(form.getBean().getWachtwoord());
          String decryptedData = encryptor.decrypt(data);

          // Import
          ParameterExport parameterExport = new Gson().fromJson(decryptedData, ParameterExport.class);
          parameterExport.getParms().forEach(this::getSaveParameter);
          addMessage("Bestand " + event.getFilename() + " is succesvol ingelezen.", Icons.ICON_OK);

        } catch (EncryptionOperationNotPossibleException e) {
          log.error("Fout bij inlezen bestand", e);
          addMessage("Foutief wachtwoord", Icons.ICON_ERROR);

        } catch (Exception e) {
          log.error("Fout bij inlezen bestand", e);
          addMessage("Bestand " + event.getFilename() + " is niet succesvol ingelezen.", Icons.ICON_ERROR);

        } finally {
          VaadinUtils.resetHeight(getWindow());
        }
      }

      private void getSaveParameter(ParameterExportRecord record) {
        getServices().getParameterService().saveParameter(record.getParm(), record.getValue(), 0L, 0L);
      }
    }
  }
}
