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

package nl.procura.gbaws.web.vaadin.module.tables.page1.upload;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.theme.twee.Icons;

public class DocUploader extends CustomComponent
    implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

  private static final long    serialVersionUID = -1597090884303540655L;
  private final GridLayout     fileLayout       = new GridLayout(2, 1);
  private final VerticalLayout root;
  private File                 file;
  private String               fileName         = "";
  private Upload               upload           = new Upload("", this);

  public DocUploader() {

    root = new VerticalLayout();
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

  public static File createTempFile(String fileName) {

    try {
      String e = FilenameUtils.getExtension(fileName);
      String n = "upload-" + FilenameUtils.getBaseName(fileName) + "-";
      n = n.replaceAll("\\W+", "-"); // Niet tekst-karakters vervangen met underscore
      return File.createTempFile(n, ("." + e));
    } catch (IOException exception) {
      throw new ProException(ERROR, "Fout bij aanmaken tijdelijk bestand", exception);
    }
  }

  @Override
  public OutputStream receiveUpload(String filename, String MIMEType) {

    FileOutputStream fos;
    try {
      setFileName(filename);
      setFile(createTempFile(filename));
      fos = new FileOutputStream(getFile());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return fos;
  }

  @Override
  public void uploadSucceeded(Upload.SucceededEvent event) {
    addMessage("Bestand " + event.getFilename() + " succesvol geupload.", Icons.ICON_OK);
  }

  @Override
  public void uploadFailed(Upload.FailedEvent event) {
    addMessage("Bestand " + event.getFilename() + " niet succesvol geupload.", Icons.ICON_ERROR);
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

  public Upload getUpload() {
    return upload;
  }

  public void setUpload(Upload upload) {
    this.upload = upload;
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
}
