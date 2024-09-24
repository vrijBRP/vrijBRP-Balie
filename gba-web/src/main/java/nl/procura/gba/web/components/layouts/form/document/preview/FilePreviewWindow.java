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

package nl.procura.gba.web.components.layouts.form.document.preview;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getDownloadLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getImageLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getJsonLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getPdfLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getPropertiesLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getTxtLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getXmlLayout;
import static nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewLayouts.getnoBytesLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;

import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

import lombok.Data;

public class FilePreviewWindow extends ModalWindow {

  private final TabSheet          tabSheet = new TabSheet();
  private final List<PreviewFile> files;

  private FilePreviewWindow(List<PreviewFile> files) {
    this.files = files;

    setCaption("Bestanden");
    setWidth("900px");
    setHeight("80%");
    setStyleName("download-window");

    tabSheet.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
    tabSheet.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
    tabSheet.addStyleName(GbaWebTheme.TABSHEET_TOP);
    tabSheet.setSizeFull();

    setContent(new VLayout(tabSheet).sizeFull());
  }

  private FilePreviewWindow(PreviewFile file) {
    this(Collections.singletonList(file));
  }

  public static void preview(Window window, PreviewFile file) {
    window.addWindow(new FilePreviewWindow(file));
  }

  @Override
  public void attach() {
    files.forEach(file -> addDownload(getWindow(), file));
    super.attach();
  }

  private void addDownload(Window window, final PreviewFile file) {
    final byte[] bytes = file.getBytes();
    final String fileName = file.getFileName();

    Button downloadButton = new Button("Downloaden");
    downloadButton.setEnabled(bytes != null);
    downloadButton.addListener((ClickListener) event -> {
      ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
      new DownloadHandlerImpl(getWindow().getParent()).download(stream, fileName, true);
    });

    Button sluitButton = new Button("Sluiten (Esc)");
    sluitButton.addListener((ClickListener) event -> closePrintWindow());

    // Focus op sluitbutton in eerste tab
    if (tabSheet.getComponentCount() == 0) {
      sluitButton.focus();
    }

    HLayout h = new HLayout(downloadButton, sluitButton).widthFull();
    h.setExpandRatio(downloadButton, 1f);

    VLayout contentLayout = new VLayout(h).sizeFull().margin(true);
    contentLayout.setStyleName("download-layout");

    TabSheet contentTabSheet = new TabSheet();
    contentTabSheet.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
    contentTabSheet.addStyleName(GbaWebTheme.TABSHEET_TOP);
    contentTabSheet.setSizeFull();
    contentLayout.add(contentTabSheet);

    Component content;
    if (bytes == null) {
      content = getnoBytesLayout();
    } else {
      switch (file.getFileType()) {
        case PDF:
          content = getPdfLayout(window, file);
          break;
        case PNG:
        case BMP:
        case TIF:
        case TIFF:
        case JPG:
        case JPEG:
          content = getImageLayout(window, file);
          break;
        case TXT:
          content = getTxtLayout(file);
          break;
        case JSON:
          content = getJsonLayout(file);
          break;
        case PROPERTIES:
          content = getPropertiesLayout(file);
          break;
        case XML:
          content = getXmlLayout(file);
          break;
        default:
          content = getDownloadLayout(file);
          break;
      }
    }

    content.addStyleName("content-layout");
    contentTabSheet.addTab(content, "Bestand");
    contentTabSheet.addTab(FilePreviewLayouts.getPropertiesLayout(file.getProperties()),
        "Informatie (" + file.getProperties().size() + ")");
    contentLayout.setExpandRatio(contentTabSheet, 1f);
    tabSheet.addTab(contentLayout, file.getFileName(), new ThemeResource(file.getFileType().getPath()));
  }

  private void closePrintWindow() {
    ((ModalWindow) getWindow()).closeWindow();
  }

  public enum TYPE {
    NUMBER,
    TEXT
  }

  @Data
  public static class PreviewFile {

    private byte[]              bytes;
    private String              title;
    private String              fileName;
    private BestandType         fileType;
    private Map<String, String> properties = new LinkedHashMap<>();

    public PreviewFile(byte[] bytes, String title, String fileName, BestandType fileType) {
      super();
      this.bytes = bytes;
      this.title = title;
      this.fileName = fileName;
      this.fileType = fileType;
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
      byte[] bytes = IOUtils.toByteArray(inputStream);
      IOUtils.closeQuietly(inputStream);
      return bytes;
    }

    public void setProperty(String key, String value) {
      properties.put(key, value);
    }
  }
}
