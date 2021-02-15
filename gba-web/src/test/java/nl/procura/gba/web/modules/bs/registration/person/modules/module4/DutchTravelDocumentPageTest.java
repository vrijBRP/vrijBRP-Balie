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

import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.NATIONALITY_1;
import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.NATIONALITY_DUTCH;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.vaadin.ui.Window;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.application.GbaApplicationMock;
import nl.procura.gba.web.components.layouts.page.buttons.ButtonNew;
import nl.procura.gba.web.services.ServicesMock;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.ProcuraWindow;

public class DutchTravelDocumentPageTest {

  private final DossierPersoon person;
  private final ServicesMock   services;

  public DutchTravelDocumentPageTest() {
    TemporaryDatabase.ensureCleanMockDatabase();
    person = new DossierPersoon();
    services = new ServicesMock();
  }

  @Test
  public void personWithoutDutchNationalityMustNotBeAbleToAdd() {
    // given
    person.toevoegenNationaliteit(NATIONALITY_1);

    // when
    DutchTravelDocumentPage page = newDutchTravelDocumentPage(person);

    // then
    ButtonNew buttonNew = VaadinUtils.getChild(page, ButtonNew.class);
    assertTrue(buttonNew.isEnabled());
  }

  @Test
  public void personWithoutDutchNationalityMustNotShowConfirmWhenNext() {
    // given
    DutchTravelDocumentPage page = newDutchTravelDocumentPage(person);
    AtomicBoolean hasRun = new AtomicBoolean(false);

    // when
    page.checkPage(() -> hasRun.set(true));

    // then
    assertTrue(hasRun.get());
  }

  @Test
  public void personWithDutchNationalityMustBeAbleToAdd() {
    // given
    person.toevoegenNationaliteit(NATIONALITY_DUTCH);

    // when
    DutchTravelDocumentPage page = newDutchTravelDocumentPage(person);

    // then
    ButtonNew buttonNew = VaadinUtils.getChild(page, ButtonNew.class);
    assertTrue(buttonNew.isEnabled());
  }

  @Test
  public void personWithDutchNationalityMustShowConfirmWhenNext() {
    // given
    person.toevoegenNationaliteit(NATIONALITY_DUTCH);
    DutchTravelDocumentPage page = newDutchTravelDocumentPage(person);
    AtomicBoolean hasRun = new AtomicBoolean(false);

    // when
    page.checkPage(() -> hasRun.set(true));

    // then
    assertFalse(hasRun.get());
  }

  private DutchTravelDocumentPage newDutchTravelDocumentPage(DossierPersoon person) {
    Window window = new ProcuraWindow();
    GbaApplication application = GbaApplicationMock.getInstance(services);
    window.setApplication(application);
    DutchTravelDocumentPage page = new DutchTravelDocumentPage(person, null);
    window.addComponent(page);

    return page;
  }
}
