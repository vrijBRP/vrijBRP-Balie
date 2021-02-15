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

package nl.procura.gba.web.modules.bs.registration.person.modules.module4;

import static com.google.common.util.concurrent.Runnables.doNothing;
import static java.lang.String.format;
import static nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock.DUTCH_TRAVEL_AUTH_BU0518_CODE;
import static nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock.UNKNOWN_DUTCH_TRAVEL_DOC_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.vaadin.ui.CssLayout;

import nl.procura.gba.jpa.personen.db.DossTravelDoc;
import nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer;
import nl.procura.gba.web.components.containers.DutchTravelDocumentContainer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class PersonDutchTravelDocumentFormTest {

  private static final String           TEST_DOC_NUMBER = "A_NUMBER";
  private static final Date             TEST_D_END_DATE = new ProcuraDate("20190101").getDateFormat();
  private static final String           TEST_DOC_DESCR  = "ABC";
  private final DutchTravelDocumentForm form;
  private final DossTravelDoc           travelDoc;

  public PersonDutchTravelDocumentFormTest() {
    TabellenServiceMock.init();
    final DossierPersoon person = new DossierPersoon();
    travelDoc = new DossTravelDoc(person);
    form = new DutchTravelDocumentForm(person, travelDoc);
    // form must be added to a container otherwise commit doesn't validate
    final CssLayout layout = new CssLayout();
    layout.addComponent(form);
  }

  @Test
  public void typeIsRequired() {
    assertExceptionMessageEquals("Veld \"Soort\" is verplicht.", form::commit);
  }

  @Test
  public void numberIsRequired() {
    final TabelFieldValue tabelFieldValue = new DutchTravelDocumentContainer().get(UNKNOWN_DUTCH_TRAVEL_DOC_CODE);
    form.getField(DutchTravelDocumentBean.F_TYPE).setValue(tabelFieldValue);
    assertExceptionMessageEquals("Veld \"Nummer\" is verplicht.", form::commit);
  }

  @Test
  public void authorityIsRequired() {
    final TabelFieldValue tabelFieldValue = new DutchTravelDocumentContainer().get(UNKNOWN_DUTCH_TRAVEL_DOC_CODE);
    form.getField(DutchTravelDocumentBean.F_TYPE).setValue(tabelFieldValue);
    form.getField(DutchTravelDocumentBean.F_NUMBER).setValue(TEST_DOC_NUMBER);
    form.getField(DutchTravelDocumentBean.F_ISSUE_DATE).setValue(TEST_D_END_DATE);
    form.getField(DutchTravelDocumentBean.F_END_DATE).setValue(TEST_D_END_DATE);

    assertExceptionMessageEquals("Veld \"Autoriteit\" is verplicht.", form::commit);
  }

  @Test
  public void throwNoExceptionWhenAllRequiredFieldsAreEntered() {
    // given
    final TabelFieldValue unknownType = new DutchTravelDocumentContainer().get(UNKNOWN_DUTCH_TRAVEL_DOC_CODE);
    form.getField(DutchTravelDocumentBean.F_TYPE).setValue(unknownType);
    form.getField(DutchTravelDocumentBean.F_NUMBER).setValue(TEST_DOC_NUMBER);
    form.getField(DutchTravelDocumentBean.F_ISSUE_DATE).setValue(TEST_D_END_DATE);
    form.getField(DutchTravelDocumentBean.F_END_DATE).setValue(TEST_D_END_DATE);
    final TabelFieldValue autoritHhw = new DutchTravelDocumentAuthorityContainer().get(DUTCH_TRAVEL_AUTH_BU0518_CODE);
    form.getField(DutchTravelDocumentBean.F_AUTHORITY).setValue(autoritHhw);
    form.getField(DutchTravelDocumentBean.F_DOSSIER_DESCRIPTION).setValue(TEST_DOC_DESCR);

    // when
    form.commit(doNothing());

    // then
    assertEquals(travelDoc.getNedReisdoc(), UNKNOWN_DUTCH_TRAVEL_DOC_CODE);
    assertEquals(travelDoc.getDocNr(), TEST_DOC_NUMBER);
    assertEquals(travelDoc.getAutVerstrek(), DUTCH_TRAVEL_AUTH_BU0518_CODE);
  }

  // suppress Exception handlers should preserve the original exceptions as we deliberate throw Exceptions
  @SuppressWarnings("squid:S1166")
  private void assertExceptionMessageEquals(String expected, Runnable runnable) {
    try {
      runnable.run();
      fail(format("Expected: message '%s' thrown, actual: no exception", expected));
    } catch (final Exception e) {
      assertEquals(expected, e.getMessage());
    }
  }
}
