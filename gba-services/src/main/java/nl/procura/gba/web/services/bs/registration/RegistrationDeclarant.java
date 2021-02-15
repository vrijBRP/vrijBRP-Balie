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

package nl.procura.gba.web.services.bs.registration;

import java.util.Optional;

import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.java.reflection.ReflectionUtil;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class RegistrationDeclarant {

  private final DeclarantType type;

  private final DossierPersoon person;

  private RegistrationDeclarant(DeclarantType type, DossierPersoon person) {
    this.type = type;
    this.person = person;
  }

  public static RegistrationDeclarant person(DossierPersoon person) {
    return new RegistrationDeclarant(DeclarantType.PERSON, person);
  }

  public static RegistrationDeclarant institutionHead() {
    return new RegistrationDeclarant(DeclarantType.INSTITUTION_HEAD, null);
  }

  public static RegistrationDeclarant byCivilServant() {
    return new RegistrationDeclarant(DeclarantType.BY_CIVIL_SERVANT, null);
  }

  public static RegistrationDeclarant unknown() {
    return new RegistrationDeclarant(DeclarantType.UNKNOWN, null);
  }

  public static RegistrationDeclarant fromCodeAndPerson(String code, DossPer person) {
    return new RegistrationDeclarant(DeclarantType.valueOfCode(code),
        ReflectionUtil.deepCopyBean(DossierPersoon.class, person));
  }

  public Optional<DossierPersoon> getPerson() {
    return Optional.ofNullable(person);
  }

  public String getCode() {
    return type.getCode();
  }
}
