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

package nl.procura.gba.web.modules.bs.registration.person.modules.module1;

import java.util.function.Consumer;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class RelativeNotInBrpPage extends ButtonPageTemplate {

  private final DossierPersoon           person;
  private final Consumer<DossierPersoon> savePerson;
  private RelativesNotInBrpDetailsForm   relativeForm;
  private BirthDetailsForm               birthForm;

  RelativeNotInBrpPage(DossierPersoon person, Consumer<DossierPersoon> savePerson) {
    this.savePerson = savePerson;
    this.person = person;

    addButton(buttonSave, 1F);
    addButton(buttonClose);
  }

  @Override
  public void event(final PageEvent event) {
    super.event(event);
    if (event.isEvent(InitPage.class)) {

      relativeForm = new RelativesNotInBrpDetailsForm();
      birthForm = new BirthDetailsForm();
      relativeForm.update(person);
      birthForm.update(person);

      addComponent(relativeForm);
      addComponent(new Fieldset("Geboorte"));
      setInfo("Als één van de onderstaande velden is ingevuld, dan moeten alledrie worden ingevuld.");
      addComponent(birthForm);

    } else if (event.isEvent(LoadPage.class)) {
      // always focus on family name to override ButtonPageTemplate behavior otherwise birth country will get focus
      relativeForm.getField(PersonBean.F_FAMILY_NAME).focus();
    }
  }

  @Override
  public void onSave() {
    relativeForm.commit();
    birthForm.commit();
    updateRelative();
    onClose();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  private void updateRelative() {
    final PersonBean relative = relativeForm.getBean();
    final PersonBean relativeBirth = birthForm.getBean();
    if (relative.getPrefix() != null) {
      person.setVoorvoegsel(relative.getPrefix().getStringValue());
    }
    person.setGeslachtsnaam(relative.getFamilyName());
    person.setVoornaam(relative.getFirstNames());
    person.setTitel(relative.getTitle());
    person.setGeslacht(relative.getGender());

    person.setDatumGeboorte(relativeBirth.getDateOfBirth());
    if (relativeBirth.getCountry() != null) {
      final boolean isGebNl = Landelijk.isNederland(relativeBirth.getCountry());
      person.setGeboorteplaats(
          isGebNl ? relativeBirth.getMunicipality() : new FieldValue(-1, relativeBirth.getForeignMunicipality()));
      person.setGeboorteland(relativeBirth.getCountry());
    } else {
      person.setGeboorteland(new FieldValue(-1, ""));
      person.setGeboorteplaats(new FieldValue(-1, relativeBirth.getForeignMunicipality()));

    }
    savePerson.accept(person);
  }
}
