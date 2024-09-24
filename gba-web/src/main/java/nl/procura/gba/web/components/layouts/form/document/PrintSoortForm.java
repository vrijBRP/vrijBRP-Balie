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

package nl.procura.gba.web.components.layouts.form.document;

import static nl.procura.gba.web.components.layouts.form.document.PrintDocumentBean.SOORT;
import static nl.procura.gba.web.components.layouts.form.document.PrintDocumentBean.SOORT_LEEG;
import static nl.procura.gba.web.components.layouts.form.document.PrintDocumentBean.VERVOLG_BLAD;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PrintSoortForm extends GbaForm<PrintDocumentBean> {

  private List<DocumentSoort>                 documentSoorten;
  private final Consumer<List<DocumentSoort>> soortChangeListener;
  private final DocumentType[]                documentTypes;

  public PrintSoortForm(List<DocumentSoort> documentSoorten,
      DocumentType[] documentTypes,
      Consumer<List<DocumentSoort>> soortChangeListener) {

    this.soortChangeListener = soortChangeListener;
    setCaption("Soort");
    setOrder(SOORT, SOORT_LEEG, VERVOLG_BLAD);
    setColumnWidths(WIDTH_130, "");
    this.documentSoorten = documentSoorten;
    this.documentTypes = documentTypes;
    setBean(new PrintDocumentBean());
  }

  @Override
  public void attach() {
    getSoortField().addListener((ValueChangeListener) this);

    if (documentSoorten == null || documentSoorten.isEmpty()) {
      Services services = getApplication().getServices();
      documentSoorten = services.getDocumentService().getDocumentSoorten(services.getGebruiker(), documentTypes);
    }

    DocumentSoortContainer container = new DocumentSoortContainer(getApplication(), documentSoorten);
    setContainer(getSoortField(), container);

    getField(SOORT).setVisible(!container.getItemIds().isEmpty());
    getField(SOORT_LEEG).setVisible(container.getItemIds().isEmpty());
    repaint();

    super.attach();
  }

  public List<DocumentSoort> getSelectedSoorten() {
    if (getSoortField().getValue() != null) {
      return (List<DocumentSoort>) ((FieldValue) getSoortField().getValue()).getValue();
    }

    return new ArrayList<>();
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
    if (event.getProperty() == getField(SOORT)) {
      List<DocumentSoort> soorten = getSelectedSoorten();
      if (soorten != null) {
        // Set vervolgblad veld
        Field vervolgblad = getField(PrintDocumentBean.VERVOLG_BLAD);
        if (isKenmerk(soorten, DocumentKenmerkType.VERVOLGBLAD) && vervolgblad != null) {
          vervolgblad.setVisible(true);
        }
        if (soortChangeListener != null) {
          soortChangeListener.accept(soorten);
        }
      }
    }

    repaint();
  }

  @SuppressWarnings("unused")
  private GbaNativeSelect getSoortField() {
    return ((GbaNativeSelect) getField(SOORT));
  }

  /**
   * Heeft één of meerdere van de documenten een bepaald kenmerk
   */
  private boolean isKenmerk(List<DocumentSoort> soorten, DocumentKenmerkType kenmerk) {
    for (DocumentSoort soort : soorten) {
      for (DocumentRecord doc : soort.getDocumenten()) {
        if (doc.getDocumentKenmerken().is(kenmerk)) {
          return true;
        }
      }
    }

    return false;
  }

  private void setContainer(GbaNativeSelect field, IndexedContainer container) {
    field.setReadOnly(false);
    field.setDataSource(container);

    if (field.getContainerDataSource().size() > 0) {
      field.setValue(field.getContainerDataSource().getItemIds().iterator().next());
    }

    field.setReadOnly(field.getContainerDataSource().getItemIds().size() == 1);
  }
}
