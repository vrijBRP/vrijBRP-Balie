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

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.zaken.common.SourceDocumentBean.*;
import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.*;
import static nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock.TEST_MUNICIPALITY_CODE;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class SourceDocumentFormTest {

  private static final List<ValidityDateType>   DEFAULT_DATE_TYPES     = asList(
      ValidityDateType.DATE_OF_BIRTH,
      ValidityDateType.UNKNOWN, ValidityDateType.CUSTOM);
  private static final List<SourceDocumentType> DEFAULT_DOCUMENT_TYPES = asList(SourceDocumentType.NONE,
      SourceDocumentType.DUTCH, SourceDocumentType.CUSTOM);

  public SourceDocumentFormTest() {
    TabellenServiceMock.init();
  }

  @Test
  public void showFormWithNotSetDateMustOnlyShowType() {
    // when
    final SourceDocumentForm form = new SourceDocumentForm(DossSourceDoc.newNotSetSourceDocument(),
        DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES, null);
    // then
    assertTrue(form.getField(F_DATE_TYPE).isVisible());
    assertFalse(form.getField(F_DATE).isVisible());
  }

  @Test
  public void showFormWithDateOfBirthMustOnlyShowType() {
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.DATE_OF_BIRTH.getCode());
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_DATE_TYPE).isVisible());
    assertFalse(form.getField(F_DATE).isVisible());
  }

  @Test
  public void showFormWithUnknownDateMustOnlyShowType() {
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.UNKNOWN.getCode());
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_DATE_TYPE).isVisible());
    assertFalse(form.getField(F_DATE).isVisible());
  }

  @Test
  public void showFormWithCustomDateMustShowTypeAndDate() {
    // given
    final BigDecimal givenDate = new BigDecimal("20190301");
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
    dossSourceDoc.setValidityDate(givenDate);
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_DATE_TYPE).isVisible());
    assertTrue(form.getField(F_DATE).isVisible());
    assertEquals(givenDate, ((GbaDateFieldValue) form.getField(F_DATE).getValue()).toBigDecimal());
  }

  @Test
  public void showFormWithNotSetMustOnlyShowSourceType() {
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_TYPE).isVisible());
    assertFalse(form.getField(F_CERTIFICATE_NO).isVisible());
    assertFalse(form.getField(F_MUNICIPALITY).isVisible());
    assertFalse(form.getField(F_DESCRIPTION).isVisible());
  }

  @Test
  public void showFormWithNoneMustOnlyShowSourceType() {
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
    dossSourceDoc.setDocType(NONE.getCode());
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_TYPE).isVisible());
    assertFalse(form.getField(F_CERTIFICATE_NO).isVisible());
    assertFalse(form.getField(F_MUNICIPALITY).isVisible());
    assertFalse(form.getField(F_DESCRIPTION).isVisible());
  }

  @Test
  public void showFormWithDutchMustShowIdAndMunicipality() {
    // given
    final String givenId = "id";
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
    dossSourceDoc.setDocType(DUTCH.getCode());
    dossSourceDoc.setDocNumber(givenId);
    dossSourceDoc.setDocMun(TEST_MUNICIPALITY_CODE);
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_TYPE).isVisible());
    assertTrue(form.getField(F_CERTIFICATE_NO).isVisible());
    assertEquals(givenId, form.getField(F_CERTIFICATE_NO).getValue());
    assertTrue(form.getField(F_MUNICIPALITY).isVisible());
    assertEquals(TEST_MUNICIPALITY_CODE, ((FieldValue) form.getField(F_MUNICIPALITY).getValue()).getStringValue());
    assertFalse(form.getField(F_DESCRIPTION).isVisible());
  }

  @Test
  public void showFormWithCustomMustShowDescription() {
    final String givenDescription = "description";
    // when
    final DossSourceDoc dossSourceDoc = DossSourceDoc.newNotSetSourceDocument();
    dossSourceDoc.setValidityDateType(ValidityDateType.CUSTOM.getCode());
    dossSourceDoc.setDocType(CUSTOM.getCode());
    dossSourceDoc.setDocDescr(givenDescription);
    final SourceDocumentForm form = new SourceDocumentForm(dossSourceDoc, DEFAULT_DOCUMENT_TYPES, DEFAULT_DATE_TYPES,
        null);
    // then
    assertTrue(form.getField(F_TYPE).isVisible());
    assertFalse(form.getField(F_CERTIFICATE_NO).isVisible());
    assertFalse(form.getField(F_MUNICIPALITY).isVisible());
    assertTrue(form.getField(F_DESCRIPTION).isVisible());
    assertEquals(givenDescription, form.getField(F_DESCRIPTION).getValue());
  }
}
