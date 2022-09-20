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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Optional;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DeclarantType;
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
        .filter(i -> equals((RegistrationDeclarant) i.getValue(), declarant))
        .findFirst();
  }

  /**
   * Match based on type and name, not id which could be different
   */
  private boolean equals(RegistrationDeclarant listValue, RegistrationDeclarant value) {
    if (listValue.getPerson().isPresent() && value.getPerson().isPresent()) {
      DossierPersoon person1 = listValue.getPerson().get();
      DossierPersoon person2 = value.getPerson().get();
      String name1 = person1.getNaam().getNaam_naamgebruik_eerste_voornaam();
      String name2 = person2.getNaam().getNaam_naamgebruik_eerste_voornaam();
      DossierPersoonType type1 = person1.getDossierPersoonType();
      DossierPersoonType type2 = person2.getDossierPersoonType();
      return type1.equals(type2) && name1.equals(name2);

    } else if (listValue.getPerson().isPresent() && isBlank(value.getCode())) {
      return DeclarantType.PERSON.getCode().equals(listValue.getCode());
    }
    return listValue.equals(value);
  }
}
