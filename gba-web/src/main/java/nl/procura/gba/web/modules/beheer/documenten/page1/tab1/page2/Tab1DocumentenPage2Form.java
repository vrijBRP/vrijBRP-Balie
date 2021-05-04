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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.Tab1DocumentenPage2Bean.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.DocumentVertrouwelijkheidContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Tab1DocumentenPage2Form extends GbaForm<Tab1DocumentenPage2Bean> {

  private final List<String> opgeslagenSjablonen = new ArrayList<>();
  private DocumentRecord     document;

  public Tab1DocumentenPage2Form(DocumentRecord document) {

    setCaption("Document");
    setOrder(CODE, VOLGNR, ALIAS, NAAM, SJABLOON, TYPE, MAP, VERVALDATUM, AANTAL,
        DOCUMENT_DMS_TYPE, VERTROUWELIJKHEID, OMSCHRIJVING);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    this.document = document;
    initFields(document);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(AANTAL)) {
      column.addComponent(new Label("(Max. 5)"));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void attach() {
    updateFields();
    super.attach();
  }

  public DocumentRecord getDocument() {
    return document;
  }

  @Override
  public void reset() {
    super.reset();
    document = new DocumentRecord();
    initFields(document);
    updateFields();
  }

  public void setDoc(DocumentRecord document) {
    this.document = document;
  }

  /**
   * Werkt de code bij
   */
  public void updateCode(long code) {
    getField(CODE).setReadOnly(false);
    getField(CODE).setValue(code);
    getField(CODE).setReadOnly(true);
  }

  protected void setDocumentmapContainer() {
    ComboBox map = getField(Tab1DocumentenPage2Bean.MAP, ComboBox.class);
    map.setContainerDataSource(new DocumentmapContainer());
    map.setNewItemsAllowed(true);
    map.setValue(cleanPath(getBean().getMap()));
  }

  protected void setVertrouwelijkheidContainer() {
    GbaNativeSelect field = getField(Tab1DocumentenPage2Bean.VERTROUWELIJKHEID, GbaNativeSelect.class);
    field.setContainerDataSource(new DocumentVertrouwelijkheidContainer());
    field.setValue(getDefaultVertrouwelijkheid(getBean().getVertrouwelijkheid()));
  }

  private DocumentVertrouwelijkheid getDefaultVertrouwelijkheid(DocumentVertrouwelijkheid vertrouwelijkheid) {
    return Services.getInstance().getDocumentService().getStandaardVertrouwelijkheid(vertrouwelijkheid, null);
  }

  protected void setDocumenttypeOmschrijvingContainer() {
    GbaComboBox field = getField(DOCUMENT_DMS_TYPE, GbaComboBox.class);
    field.setContainerDataSource(new DocumentTypeOmschrijvingContainer(getBean().getDocumentDmsType()));
    field.setNewItemsAllowed(true);
    field.setValue(getBean().getDocumentDmsType());
  }

  protected class DocumentTypeOmschrijvingContainer extends ArrayListContainer {

    public DocumentTypeOmschrijvingContainer(String documentDmsType) {
      addItem(documentDmsType);
      getApplication().getServices().getDocumentService().getDmsDocumentTypes().stream()
          .map(DmsDocumentType::toString)
          .forEach(this::addItem);
    }
  }

  private void initFields(DocumentRecord doc) {

    Tab1DocumentenPage2Bean bean = new Tab1DocumentenPage2Bean();

    if (doc.isStored()) {
      bean.setCode(astr(doc.getCDocument()));
      bean.setVolgnr(astr(doc.getVDocument()));
      bean.setNaam(doc.getDocument());
      bean.setSjabloon(doc.getBestand());
      bean.setType(doc.getDocumentType());
      bean.setMap(cleanPath(doc.getPad()));
      bean.setVervaldatum(new DateFieldValue(doc.getDatumVerval().getLongDate()));
      bean.setOmschrijving(doc.getOmschrijving());
      int aantal = doc.getAantal();
      bean.setAantal(astr(aantal > 0 && aantal <= 5 ? aantal : 1));
      bean.setAlias(doc.getAlias());
      bean.setVertrouwelijkheid(doc.getVertrouwelijkheid());
      bean.setDocumentDmsType(doc.getDocumentDmsType());
    }

    setBean(bean);
    getField(AANTAL).addValidator(new MaxAantalValidator(1, 5));
    repaint();
  }

  private void setContainers() {

    setTemplateContainer();
    setDocumentTypeContainer();
    setDocumentmapContainer();
    setVertrouwelijkheidContainer();
    setDocumenttypeOmschrijvingContainer();

    repaint();
  }

  private void setTemplateContainer() {
    GbaComboBox sj = getField(SJABLOON, GbaComboBox.class);
    sj.setDataSource(new TemplateContainer());
    sj.setValue(getBean().getSjabloon());
  }

  private void setDocumentTypeContainer() {
    GbaNativeSelect t = getField(TYPE, GbaNativeSelect.class);
    t.setDataSource(new DocumentTypeContainer());
    t.setValue(getBean().getType());
  }

  private void updateFields() {

    updateOpgeslagenSjablonen();

    if (document.isStored()) {
      verwijderSjabloon(document);
    }

    setContainers();
  }

  private void updateOpgeslagenSjablonen() {

    opgeslagenSjablonen.clear();

    List<DocumentRecord> documenten = getApplication().getServices().getDocumentService().getDocumenten(false);

    for (DocumentRecord document : documenten) {
      if (document.isStored() && !opgeslagenSjablonen.contains(document.getBestand())) {
        opgeslagenSjablonen.add(document.getBestand());
      }
    }
  }

  /**
   * Deze functie zorgt ervoor dat het sjabloon van een aangeklikt document altijd getoond wordt.
   */
  private void verwijderSjabloon(DocumentRecord document) {
    String sjabloon = document.getBestand();
    opgeslagenSjablonen.remove(sjabloon);
  }

  private final class DocumentmapContainer extends ArrayListContainer {

    private DocumentmapContainer() {

      List<DocumentRecord> documentList = getApplication().getServices()
          .getDocumentService()
          .getDocumenten(false);
      List<String> pathList = new ArrayList<>();

      for (DocumentRecord doc : documentList) {

        String cleanedPath = cleanPath(doc.getPad());
        if (!pathList.contains(cleanedPath)) {
          pathList.add(cleanedPath);
        }
      }

      Collections.sort(pathList);

      for (String map : pathList) {
        addItem(map);
      }
    }
  }

  private final class TemplateContainer extends ArrayListContainer {

    private TemplateContainer() {

      DocumentService dbDocumenten = getApplication().getServices().getDocumentService();
      List<File> sjablonen = dbDocumenten.getSjabloonBestanden();
      File sjablonenMap = dbDocumenten.getSjablonenMap();

      // verwijder alle opgeslagen sjablonen
      for (String sjabloon : opgeslagenSjablonen) {
        File sjabloonBestand = new File(sjablonenMap, sjabloon);
        if (sjabloonBestand.exists()) {
          sjablonen.remove(sjabloonBestand);
        }
      }

      for (File sjabloonBestand : sjablonen) {
        addItem(MiscUtils.getRelatiefPad(sjablonenMap, sjabloonBestand));
      }
    }
  }
}
