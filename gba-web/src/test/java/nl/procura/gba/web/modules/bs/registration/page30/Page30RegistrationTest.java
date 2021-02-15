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

package nl.procura.gba.web.modules.bs.registration.page30;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.procura.gba.web.application.WindowMock;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.common.modules.BsModuleMock;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonMock;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.RegistrationDeclarant;
import nl.procura.gba.web.services.bs.registration.RegistrationService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page30RegistrationTest {

  private final WindowMock          window;
  private final DossierPersoon      firstAdult;
  private final DossierPersoon      child;
  private final BsModule            bsModule;
  private final DossierPersoon      secondAdult;
  private final DossierRegistration dossierRegistration;

  public Page30RegistrationTest() {
    window = new WindowMock();
    RegistrationService registrationService = window.getServices().getRegistrationService();
    // given registration dossier with two adults and one child
    dossierRegistration = ZaakUtils.newZaakDossier(registrationService.newDossier(),
        window.getServices());
    dossierRegistration.setHouseNumber(1L);
    child = DossierPersoonMock.childBelow16();
    child.setDossierPersoonType(DossierPersoonType.INSCHRIJVER);
    dossierRegistration.getDossier().toevoegenPersoon(child);
    firstAdult = DossierPersoonMock.adult("First");
    // set unique database ID otherwise FieldValue thinks they're the same and won't be added to select list
    firstAdult.setCode(1L);
    firstAdult.setDossierPersoonType(DossierPersoonType.GERELATEERDE_BRP);
    dossierRegistration.getDossier().toevoegenPersoon(firstAdult);
    secondAdult = DossierPersoonMock.adult("Second");
    // set unique database ID otherwise FieldValue thinks they're the same and won't be added to select list
    secondAdult.setCode(2L);
    secondAdult.setDossierPersoonType(DossierPersoonType.GERELATEERDE_BRP);
    dossierRegistration.getDossier().toevoegenPersoon(secondAdult);

    bsModule = BsModuleMock.processes(dossierRegistration.getDossier(), Page30Registration.class);
  }

  @Test
  public void onlyPeople16YearsAndOlderShouldBeShown() {
    // when
    window.addComponent(bsModule);

    // then
    DeclarantContainer container = getDeclarantContainer();

    assertTrue(contains(container, firstAdult));
    assertTrue(contains(container, secondAdult));
    assertFalse(contains(container, child));
  }

  @Test
  public void correctPersonMustBeSelected() {
    // given second adult as declarant
    RegistrationDeclarant declarant = RegistrationDeclarant.person(secondAdult);
    dossierRegistration.setDeclarant(declarant);

    // when
    window.addComponent(bsModule);

    // then second adult must be selected
    GbaNativeSelect field = getPersonField();
    assertEquals(declarant, ((FieldValue) field.getValue()).getValue());
  }

  private DeclarantContainer getDeclarantContainer() {
    GbaNativeSelect field = getPersonField();
    return (DeclarantContainer) field.getContainerDataSource();
  }

  private GbaNativeSelect getPersonField() {
    DeclarantForm form = VaadinUtils.getChild(bsModule, DeclarantForm.class);
    return form.getField(DeclarantBean.F_PERSON, GbaNativeSelect.class);
  }

  private boolean contains(DeclarantContainer container, DossierPersoon person) {
    RegistrationDeclarant declarant = RegistrationDeclarant.person(person);
    return container.getItemIds().stream()
        .map(i -> (FieldValue) i)
        .map(fv -> (RegistrationDeclarant) fv.getValue())
        .anyMatch(d -> d.equals(declarant));
  }

}
