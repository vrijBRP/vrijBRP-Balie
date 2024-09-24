/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.document.page4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.Icons;

public class DocUploader extends CustomComponent
    implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

  private final GridLayout fileLayout = new GridLayout(2, 1);
  private File             file;
  private String           fileName   = "";
  private Upload           upload     = new Upload(null, this);

  public DocUploader() {
    VerticalLayout root = new VerticalLayout();
    root.setSpacing(true);
    root.setWidth("100%");
    setCompositionRoot(root);

    getUpload().setButtonCaption("Uploaden");
    getUpload().addListener((Upload.SucceededListener) this);
    getUpload().addListener((Upload.FailedListener) this);

    fileLayout.setWidth("100%");
    fileLayout.setSpacing(true);

    root.addComponent(getUpload());
    root.addComponent(fileLayout);
  }

  public boolean isValidFileSize() {
    DMSService dms = getApplication().getServices().getDmsService();
    long maxSizeInMb = dms.getMaxFileSizeUploadsInMB();
    long fileSize = FileUtils.sizeOf(file) / 1024 / 1024;
    if (maxSizeInMb > 0 && fileSize > maxSizeInMb) {
      addMessage(String.format("Het bestand is %d MB groot. Maximaal %d MB toegestaan.", fileSize,
          dms.getMaxFileSizeUploadsInMB()), Icons.ICON_WARN);
      VaadinUtils.resetHeight(getWindow());
      return false;
    }
    return true;
  }

  public void addMessage(String msg, int icon) {
    clearMessage();
    fileLayout.addComponent(new Embedded(null, new ThemeResource(Icons.getIcon(icon))));
    fileLayout.addComponent(new Label(msg, Label.CONTENT_XHTML));
    fileLayout.setColumnExpandRatio(1, 1f);
  }

  public void clearMessage() {
    fileLayout.removeAllComponents();
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Upload getUpload() {
    return upload;
  }

  public void setUpload(Upload upload) {
    this.upload = upload;
  }

  @Override
  public OutputStream receiveUpload(String filename, String MIMEType) {
    FileOutputStream fos;
    try {
      setFileName(filename);
      setFile(DocumentenPrintenService.newTijdelijkBestand(filename));
      fos = new FileOutputStream(getFile());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return fos;
  }

  @Override
  public void uploadFailed(Upload.FailedEvent event) {
    addMessage("Bestand " + event.getFilename() + " niet succesvol geupload.", Icons.ICON_ERROR);
    getWindow().setHeight(null);
  }

  @Override
  public void uploadSucceeded(Upload.SucceededEvent event) {
    addMessage("Bestand " + event.getFilename() + " succesvol geupload.", Icons.ICON_OK);
    getWindow().setHeight(null);
  }
}
