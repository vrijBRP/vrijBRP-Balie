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

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.email.PrintEmailWindow;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin6.addons.embed.Embed;

public class PrintPreviewWindow extends ModalWindow {

  private final TabSheet          tabSheet = new TabSheet();
  private final boolean           withEmail;
  private final List<PrintRecord> records;

  public PrintPreviewWindow(List<PrintRecord> records) {
    this(records, true);
  }

  public PrintPreviewWindow(List<PrintRecord> records, boolean withEmail) {

    this.records = records;
    this.withEmail = withEmail;

    setCaption("Afdrukvoorbeelden");
    setWidth("900px");
    setHeight("80%");
    setStyleName("download-window");

    tabSheet.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
    tabSheet.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
    tabSheet.addStyleName(GbaWebTheme.TABSHEET_TOP);
    tabSheet.setSizeFull();

    setContent(new VLayout().sizeFull().add(tabSheet));
  }

  @Override
  public void attach() {
    for (PrintRecord record : records) {
      addDownload(record.getPreviewArray(), getWindow(), record.getDocument().getDocument());
    }

    super.attach();
  }

  private void addDownload(final byte[] bytes, Window window, String downloadNaam) {

    if (bytes == null) { // Moeten wel bytes zijn
      return;
    }

    StreamResource streamResource = new StreamResource((StreamSource) () -> new ByteArrayInputStream(bytes),
        downloadNaam + new Date().getTime() + ".pdf", window.getApplication());
    Embed pdf = new Embed(streamResource);
    pdf.setSizeFull();

    VerticalLayout pdfLayout = new VerticalLayout();
    pdfLayout.setStyleName("download-layout");
    pdfLayout.setMargin(true);
    pdfLayout.setSizeFull();
    pdfLayout.setSpacing(true);

    Button sluitButton = new Button("Sluiten (Esc)");
    sluitButton.addListener((ClickListener) event -> closePrintWindow());

    Button emailButton = new Button("E-mailen");
    emailButton.addListener((ClickListener) event -> {
      getParent().addWindow(new PrintEmailWindow(records));
      closeWindow();
    });

    // Focus op sluitbutton in eerste tab
    if (tabSheet.getComponentCount() == 0) {
      sluitButton.focus();
    }

    HorizontalLayout h = new HorizontalLayout();

    h.setWidth("100%");

    if (withEmail) {
      h.addComponent(emailButton);
      h.setExpandRatio(emailButton, 1f);
    }

    h.addComponent(sluitButton);

    pdfLayout.addComponent(h);
    pdfLayout.addComponent(pdf);
    pdfLayout.setExpandRatio(pdf, 1f);

    tabSheet.addTab(pdfLayout, downloadNaam, new ThemeResource(BestandType.PDF.getPath()));
  }

  private void closePrintWindow() {
    ((ModalWindow) getWindow()).closeWindow();
  }
}
