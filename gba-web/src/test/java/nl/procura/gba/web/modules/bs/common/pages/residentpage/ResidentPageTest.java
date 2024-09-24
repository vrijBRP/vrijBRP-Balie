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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.web.application.GbaApplicationMock;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.ServicesMock;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.theme.ProcuraWindow;

public class ResidentPageTest {

  private ServicesMock servicesMock;

  @Before
  public void setUp() {
    TemporaryDatabase.ensureCleanMockDatabase();
    servicesMock = new ServicesMock();
  }

  @Test
  public void searchResidentsFunctionMustBeCalled() {
    // given form with BSN entered
    ConsentProviderListener listener = new ConsentProviderListener();
    ResidentPageMock residentPage = newResidentPage(newDestinationAddress(), ConsentProvider.notDeclared(), listener,
        this::oneResident);
    residentPage.residentForm().setValue(ResidentPageBean.F_BSN, Testdata.TEST_BSN_1.toString());

    // when search residents on address
    residentPage.handleEvent(null, ShortcutAction.KeyCode.ENTER);

    // then
    List<IndexedTable.Record> records = residentPage.residentTable().getRecords();
    assertEquals(1, records.size());
    List<IndexedTable.Value> values = records.get(0).getValues();
    assertEquals(1, values.get(0).getValue());
    assertEquals("x, x x", values.get(1).getValue());
    assertEquals("Onbekend", values.get(4).getValue());

    // when select record and click consent provider button
    residentPage.residentTable().select(records.get(0).getItemId());
    residentPage.handleEvent(null, ShortcutAction.KeyCode.F4);

    // then listener must have been called
    assertTrue(listener.isSelected());
  }

  @Test
  public void moreThan50ResidentsMustShowConfirmation() {
    // given
    ConsentProviderListener listener = new ConsentProviderListener();
    VerhuisAdres destinationAddress = newDestinationAddressWithMoreThan50People();
    ResidentPageMock residentPage = newResidentPage(destinationAddress, ConsentProvider.notDeclared(), listener,
        this::moreThan50Residents);

    // when search residents on address
    residentPage.handleEvent(null, ShortcutAction.KeyCode.ENTER);

    // then there must be one subwindow with a confirmation dialog
    assertConfirmationDialog(residentPage.getWindow());
  }

  @Test
  public void deleteFeedbackRequiresSelection() {
    // given
    ConsentProviderListener listener = new ConsentProviderListener();
    VerhuisAdres destinationAddress = newDestinationAddressWithMoreThan50People();
    ResidentPageMock residentPage = newResidentPage(destinationAddress, ConsentProvider.notDeclared(), listener,
        this::oneResident);

    // when delete feedback of residents
    try {
      residentPage.handleEvent(null, ShortcutAction.KeyCode.F8);
      fail("Expected: ProException; actual: no exception");
    } catch (ProException e) {
      assertEquals("Geen personen geselecteerd", e.getMessage());
    }
  }

  @Test
  public void deleteFeedbackWithSelectionMustShowConfirmation() {
    // given
    ConsentProviderListener listener = new ConsentProviderListener();
    VerhuisAdres destinationAddress = newDestinationAddress();
    ResidentPageMock residentPage = newResidentPage(destinationAddress, ConsentProvider.notDeclared(), listener,
        this::oneResident);
    // given search and select resident
    residentPage.handleEvent(null, ShortcutAction.KeyCode.ENTER);
    residentPage.residentTable().select(residentPage.residentTable().getRecords().get(0).getItemId());

    // when delete feedback of selected residents
    residentPage.handleEvent(null, ShortcutAction.KeyCode.F8);

    // then
    assertConfirmationDialog(residentPage.getWindow());
  }

  private void assertConfirmationDialog(Window window) {
    Object subwindows;
    try {
      subwindows = FieldUtils.readField(window, "subwindows", true);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    assertTrue(subwindows instanceof Set);
    assertEquals(1, ((Set) subwindows).size());
    assertTrue(((Set) subwindows).iterator().next() instanceof ConfirmDialog);
  }

  private List<Relatie> oneResident(@SuppressWarnings("unused") PLEArgs args) {
    Relatie resident = new Relatie();
    BasePLExt persoonslijst = servicesMock.getPersonenWsService()
        .getPersoonslijst(Testdata.TEST_BSN_1.toString());
    resident.setPl(persoonslijst);

    return singletonList(resident);
  }

  private List<Relatie> moreThan50Residents(@SuppressWarnings("unused") PLEArgs args) {
    fail("moreThan50Residents should not be called");
    return null;
  }

  static class ConsentProviderListener implements SelectListener<ConsentProvider> {

    private ConsentProvider selectedConsentProvider;

    @Override
    public void select(ConsentProvider consentProvider) {
      selectedConsentProvider = consentProvider;
    }

    boolean isSelected() {
      return selectedConsentProvider != null;
    }
  }

  private VerhuisAdres newDestinationAddressWithMoreThan50People() {
    BaseWK basisWK = newBasisWK();
    int numberOfPeople = 51;
    ArrayList<BaseWKPerson> people = new ArrayList<>(numberOfPeople);
    for (int i = 0; i < numberOfPeople; i++) {
      BaseWKPerson person = new BaseWKPerson();
      person.setBsn(new BaseWKValue().setValue(Testdata.TEST_BSN_1.toString()));
      people.add(person);
    }
    basisWK.setPersonen(people);
    return new VerhuisAdres(new BaseWKExt(basisWK));
  }

  private VerhuisAdres newDestinationAddress() {
    BaseWK basisWK = newBasisWK();
    return new VerhuisAdres(new BaseWKExt(basisWK));
  }

  private BaseWK newBasisWK() {
    BaseWK basisWK = new BaseWK();
    BaseWKValue postcode = new BaseWKValue();
    postcode.setValue("1705TM");
    basisWK.setPostcode(postcode);
    return basisWK;
  }

  private ResidentPageMock newResidentPage(VerhuisAdres destinationAddress, ConsentProvider relationship,
      SelectListener<ConsentProvider> listener, Function<PLEArgs, List<Relatie>> searchResidents) {
    Window window = new ProcuraWindow();
    window.setApplication(GbaApplicationMock.getInstance());
    ResidentPageMock residentPage = new ResidentPageMock(destinationAddress, relationship, listener, searchResidents);
    residentPage.mockServices(servicesMock);
    window.addComponent(residentPage);
    residentPage.event(new InitPage());

    return residentPage;
  }

}
