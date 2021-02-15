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

import java.util.List;
import java.util.Optional;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RegistrationDeclarant;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

class DeclarantContainer extends IndexedContainer {

  private static final int DECLARATION_AGE_LIMIT = 16;

  DeclarantContainer(List<DossierPersoon> people) {
    for (DossierPersoon person : people) {
      if (person.getLeeftijd() >= DECLARATION_AGE_LIMIT) {
        RegistrationDeclarant declarant = RegistrationDeclarant.person(person);
        addItem(new FieldValue(declarant, person.getNaam().getNaam_naamgebruik_eerste_voornaam()));
      }
    }
    addItem(new FieldValue(RegistrationDeclarant.byCivilServant(), "Ambtshalve / verzorger"));
    addItem(new FieldValue(RegistrationDeclarant.institutionHead(), AangifteSoort.HOOFDINSTELLING.getOms()));
  }

  /**
   * Get item object for given declarant which can be used to select the item in a select field.
   */
  Optional<FieldValue> getItem(RegistrationDeclarant declarant) {
    return getItemIds().stream()
        .map(i -> (FieldValue) i)
        .filter(i -> i.getValue().equals(declarant))
        .findFirst();
  }
}
