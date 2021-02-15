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

package nl.procura.gba.web.components.layouts.form.document.preview;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.*;

import nl.procura.gba.common.StreamUtils;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class FilePreviewLayouts {

  /**
   * Component voor als bericht geen bytes bevat
   */
  public static Component getDownloadLayout(final PreviewFile file) {
    String content = newLineToBreaks("Dit bestand (." + FilenameUtils.getExtension(
        file.getFileName()) + " formaat) kan niet worden getoond in de browser, druk op de downloadknop.");
    return new LucidaConsoleLabel(content, Label.CONTENT_XHTML, true);
  }

  /**
   * Component voor tonen van PNG afbeeldingen
   */
  public static Component getImageLayout(Window window, final PreviewFile file) {

    StreamResource streamResource = getStreamResource(file.getBytes(), file.getFileName(), window.getApplication());
    Embedded embedded = new Embedded(null, streamResource);

    embedded.setType(Embedded.TYPE_IMAGE);
    VLayout layout = new VLayout(embedded).sizeFull();
    layout.setStyleName("text");
    layout.setExpandRatio(embedded, 1f);

    return layout;
  }

  /**
   * Component voor als bericht geen bytes bevat
   */
  public static Component getnoBytesLayout() {
    String content = newLineToBreaks("Dit bestand is leeg (geen bytes).");
    return new LucidaConsoleLabel(content, Label.CONTENT_XHTML, true);
  }

  /**
   * Component voor tonen van PDF bestanden
   */
  public static Component getPdfLayout(Window window, final PreviewFile file) {

    StreamResource streamResource = getStreamResource(file.getBytes(), file.getFileName(), window.getApplication());
    Embedded embedded = new Embedded(null, streamResource);

    embedded.setType(Embedded.TYPE_BROWSER);
    embedded.setSizeFull();

    VLayout layout = new VLayout(embedded).sizeFull();
    layout.setExpandRatio(embedded, 1f);

    return layout;
  }

  /**
   * @param properties
   * @return
   */
  public static Component getPropertiesLayout(Map<String, String> properties) {
    PropertiesLabel propertiesLabel = new PropertiesLabel(new PropertiesLayout(properties));
    propertiesLabel.setMargin(true);
    return propertiesLabel;
  }

  /**
   * Component voor tonen van PROPERTIES bestanden
   */
  public static Component getPropertiesLayout(final PreviewFile file) {
    return new PropertiesLabel(new PropertiesLayout(file.getBytes()));
  }

  /**
   * Component voor tonen van TXT bestanden
   */
  public static Component getTxtLayout(final PreviewFile file) {
    String content = newLineToBreaks(new String(file.getBytes()));
    return new LucidaConsoleLabel(content, Label.CONTENT_XHTML, true);
  }

  /**
   * Component voor tonen van XML bestanden
   */
  public static Component getXmlLayout(final PreviewFile file) {
    String content = StreamUtils.toPrettyString(new String(file.getBytes()), true);
    return new LucidaConsoleLabel(content, Label.CONTENT_XHTML, false);
  }

  private static StreamResource getStreamResource(final byte[] bytes, String fileName, Application application) {

    StreamSource streamSource = (StreamSource) () -> new ByteArrayInputStream(bytes);

    StreamResource streamResource = new StreamResource(streamSource, fileName, application);
    streamResource.setCacheTime(0);
    return streamResource;
  }

  private static String newLineToBreaks(String content) {
    return content.replaceAll("\n", "<br/>").replaceAll("\\s", "&nbsp;");
  }

  /**
   * Label met pre voor text (Textarea), maar niet voor XML
   */
  public static class LucidaConsoleLabel extends CssLayout {

    public LucidaConsoleLabel(String value, int contentType, boolean usePre) {
      setStyleName(ProcuraTheme.AUTOSCROLL);
      addStyleName("text");
      setMargin(false);
      setSizeUndefined();
      Label label = usePre ? new Label("<pre>" + value + "</pre>", contentType) : new Label(value, contentType);
      label.setSizeUndefined();
      addComponent(label);
    }
  }

  /**
   * Layout voor properties
   */
  public static class PropertiesLabel extends CssLayout {

    public PropertiesLabel(PropertiesLayout layout) {
      setStyleName(ProcuraTheme.AUTOSCROLL);
      setMargin(false);
      setSizeFull();
      addComponent(layout);
    }
  }

}
