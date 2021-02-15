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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page2;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page1.Tab6DocumentenPage1;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempelType;
import nl.procura.gba.web.services.zaken.documenten.stempel.PositieType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Tab6DocumentenPage2 extends DocumentenTabPage {

  private DocumentStempel         documentStempel = null;
  private Tab6DocumentenPage2Form form            = null;
  private UploaderLayout          uploaderLayout  = null;

  public Tab6DocumentenPage2(DocumentStempel documentStempel) {

    super("Toevoegen / muteren documentstempel");

    setDocumentStempel(documentStempel);

    addButton(buttonPrev, buttonNew, buttonSave);

    setMargin(true);

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Tab6DocumentenPage2Form(documentStempel) {

        @Override
        protected void setOnChangePositie(PositieType type) {
        }

        @Override
        protected void setOnChangeType(DocumentStempelType type) {

          onChangeType(type);
        }
      };

      addComponent(form);

      uploaderLayout = new UploaderLayout();

      addComponent(uploaderLayout);

      onChangeType(documentStempel.getStempelType());
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form.reset();

    setDocumentStempel(new DocumentStempel());

    uploaderLayout.updateAfbeelding();

    onChangeType(documentStempel.getStempelType());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab6DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {

    form.commit();

    Tab6DocumentenPage2Bean b = form.getBean();

    documentStempel.setDocumentStempel(b.getStempel());
    documentStempel.setStempelType(b.getType());
    documentStempel.setWoord(b.getWoord());
    documentStempel.setXcoordinaat(along(b.getX()));
    documentStempel.setYcoordinaat(along(b.getY()));
    documentStempel.setPositie(b.getPositie());
    documentStempel.setBreedte(along(b.getBreedte()));
    documentStempel.setHoogte(along(b.getHoogte()));
    documentStempel.setVolgorde(along(b.getVolgorde()));
    documentStempel.setPaginas(b.getPaginas());
    documentStempel.setFontType(b.getFont());
    documentStempel.setFontSize(along(b.getFontsize()));

    if (!documentStempel.isAfbeelding()) {
      documentStempel.setData(null);
      documentStempel.setDataType("");
    }

    if (PositieType.WOORD != documentStempel.getPositie()) {
      documentStempel.setWoord("");
    }

    getServices().getDocumentService().save(documentStempel);

    successMessage("Documentstempel is opgeslagen.");

    uploaderLayout.updateAfbeelding();
  }

  public void setDocumentStempel(DocumentStempel documentStempel) {
    this.documentStempel = documentStempel;
  }

  private void onChangeType(DocumentStempelType type) {

    documentStempel.setStempelType(type);

    uploaderLayout.updateAfbeelding();

    form.repaint();
  }

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(SucceededEvent event) {

      try {

        documentStempel.setData(IOUtils.toByteArray(new FileInputStream(getFile())));
        documentStempel.setDataType(FilenameUtils.getExtension(getFile().getName()));

        addMessage("Bestand " + event.getFilename() + " is succesvol geupload.", Icons.ICON_OK);

        onSave();
      } catch (Exception e) {

        e.printStackTrace();
        addMessage("Bestand " + event.getFilename() + " is niet succesvol geupload.", Icons.ICON_ERROR);
      }
    }
  }

  private class UploaderLayout extends VerticalLayout {

    private Embedded afbeelding = null;

    public UploaderLayout() {

      setVisible(false);
      addComponent(new Fieldset("Uploaden van de afbeelding"));
      addComponent(new Uploader());

      updateAfbeelding();
    }

    public void updateAfbeelding() {

      // Alleen tonen als type afbeelding is
      setVisible(documentStempel.isAfbeelding());

      final byte[] data = documentStempel.getData();
      String dataType = documentStempel.getDataType();
      GbaApplication app = Tab6DocumentenPage2.this.getApplication();

      if (afbeelding != null) {
        removeComponent(afbeelding);
      }

      if (data != null && app != null) {

        afbeelding = new Embedded("", new StreamResource((StreamSource) () -> new ByteArrayInputStream(data),
            new Date().getTime() + "." + dataType, app));

        afbeelding.setType(Embedded.TYPE_IMAGE);
        afbeelding.setWidth(astr(documentStempel.getBreedte()));
        afbeelding.setHeight(astr(documentStempel.getHoogte()));

        addComponent(afbeelding);
      }
    }
  }
}
