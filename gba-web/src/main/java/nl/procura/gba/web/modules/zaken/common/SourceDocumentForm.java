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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.gba.web.modules.zaken.common.SourceDocumentBean.*;

import java.util.List;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.GbaDateIsBeforeValidator;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class SourceDocumentForm extends GbaForm<SourceDocumentBean> {

  private final DossSourceDoc                sourceDocument;
  private final IndexedContainer             dateTypeContainer;
  private final IndexedContainer             docTypeContainer;
  private final SourceDocumentChangeListener sourceDocumentChangeListener;

  public SourceDocumentForm(DossSourceDoc sourceDocument, List<SourceDocumentType> docTypes,
      List<ValidityDateType> dateTypes,
      SourceDocumentChangeListener sourceDocumentChangeListener) {

    setCaption("Geldigheid / document");
    this.sourceDocument = sourceDocument;
    this.sourceDocumentChangeListener = sourceDocumentChangeListener;

    dateTypeContainer = new IndexedContainer(dateTypes);
    docTypeContainer = new IndexedContainer(docTypes);

    SourceDocumentBean sourceDocumentBean = new SourceDocumentBean();
    sourceDocumentBean.setCertificateNo(sourceDocument.getDocNumber());
    sourceDocumentBean.setDescription(sourceDocument.getDocDescr());

    setBean(sourceDocumentBean, F_DATE_TYPE, F_DATE, F_TYPE, F_CERTIFICATE_NO, F_MUNICIPALITY, F_DESCRIPTION);
  }

  public void setIsBeforeValidation(GbaDateField field, String description) {
    getField(F_DATE).addValidator(new GbaDateIsBeforeValidator(
        "\"Datum geldigheid\" mag niet vóór \"" + description + "\" liggen", field));
  }

  @Override
  public void afterSetBean() {

    if (getField(F_DATE_TYPE_LABEL) == null) {

      GbaNativeSelect dateType = (GbaNativeSelect) getField(F_DATE_TYPE);
      dateType.setContainerDataSource(dateTypeContainer);

      GbaNativeSelect source = (GbaNativeSelect) getField(F_TYPE);
      source.setContainerDataSource(docTypeContainer);

      ValidityDateType dateTypeValue = ValidityDateType.valueOfCode(sourceDocument.getValidityDateType());
      dateType.setValue(dateTypeValue);
      onDateTypeChange(dateTypeValue, new GbaDateFieldValue(sourceDocument.getValidityDate()));
      dateType.addListener((ValueChangeListener) e -> {
        onDateTypeChange((ValidityDateType) e.getProperty().getValue(), null);
      });

      SourceDocumentType sourceTypeValue = SourceDocumentType.valueOfCode(sourceDocument.getDocType());
      source.setValue(sourceTypeValue);
      onSourceChange(sourceTypeValue, Container.PLAATS.getOrUnknown(sourceDocument.getDocMun()));
      source.addListener((ValueChangeListener) e -> {
        onSourceChange((SourceDocumentType) e.getProperty().getValue(), null);
      });
    }
    super.afterSetBean();
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_DATE, F_CERTIFICATE_NO, F_MUNICIPALITY, F_DESCRIPTION)) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  public DossSourceDoc getValidatedValue() {
    commit();
    final SourceDocumentBean enteredSourceDocument = getBean();
    return new DossSourceDoc(sourceDocument.getCDossSourceDoc(), enteredSourceDocument.getDateType().getCode(),
        enteredSourceDocument.getDate().toBigDecimal(), enteredSourceDocument.getType().getCode(),
        enteredSourceDocument.getCertificateNo(), enteredSourceDocument.getMunicipality().getStringValue(),
        enteredSourceDocument.getDescription());
  }

  private void onDateTypeChange(ValidityDateType type, GbaDateFieldValue customDate) {
    if (type == ValidityDateType.CUSTOM) {
      getField(F_DATE).setVisible(true);
      getField(F_DATE).setValue(customDate);
    } else {
      getField(F_DATE).setVisible(false);
      getField(F_DATE).setValue(null);
    }
    repaint();
  }

  private void onSourceChange(SourceDocumentType value, TabelFieldValue municipality) {
    if (value == SourceDocumentType.DUTCH) {
      getField(F_CERTIFICATE_NO).setVisible(true);
      getField(F_MUNICIPALITY).setVisible(true);
      getField(F_MUNICIPALITY).setValue(municipality);
      getField(F_DESCRIPTION).setValue("");
      getField(F_DESCRIPTION).setVisible(false);

    } else if (value == SourceDocumentType.CUSTOM) {
      getField(F_CERTIFICATE_NO).setValue("");
      getField(F_CERTIFICATE_NO).setVisible(false);
      getField(F_MUNICIPALITY).setValue(Container.PLAATS.unknown());
      getField(F_MUNICIPALITY).setVisible(false);
      getField(F_DESCRIPTION).setVisible(true);
      getField(F_DESCRIPTION).setValue(sourceDocument.getDocDescr());

    } else {
      getField(F_CERTIFICATE_NO).setValue("");
      getField(F_CERTIFICATE_NO).setVisible(false);
      getField(F_MUNICIPALITY).setValue(Container.PLAATS.unknown());
      getField(F_MUNICIPALITY).setVisible(false);
      getField(F_DESCRIPTION).setValue("");
      getField(F_DESCRIPTION).setVisible(false);
    }
    if (sourceDocumentChangeListener != null) {
      sourceDocumentChangeListener.onChangeSourceDocument(value);
    }
    repaint();
  }

  public void setReadOnly(boolean readOnly, boolean changeFields) {
    if (changeFields) {
      setOrder(F_DATE_TYPE_LABEL, F_TYPE_LABEL);
      setBean(new SourceDocumentBean());
    }
    setReadOnly(readOnly);
  }

  public boolean isNoDocument() {
    return SourceDocumentType.NONE == getField(F_TYPE).getValue();
  }

  public interface SourceDocumentChangeListener {

    void onChangeSourceDocument(SourceDocumentType value);
  }
}
