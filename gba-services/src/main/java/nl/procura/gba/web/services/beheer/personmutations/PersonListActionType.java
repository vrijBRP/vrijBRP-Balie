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

package nl.procura.gba.web.services.beheer.personmutations;

import java.util.Arrays;

/**
 * Keep in-sync with same class in Personen-BSM
 */
public enum PersonListActionType {

  UPDATE_SET(10, "Actualiseren (toevoegen gegevens)", false),
  ADD_SET(20, "Actualiseren (toevoegen gegevens)", false),
  UPDATE_MUT(30, "Mutatie bewerken", false),
  DELETE_MUT(40, "Mutatie verwijderen", false),
  ADD_HISTORIC(50, "Historie toevoegen", false),
  CORRECT_CURRENT_GENERAL(60, "Correctie van actuele algemene gegevens", false),
  CORRECT_HISTORIC_GENERAL(70, "Correctie van historische algemene gegevens", false),
  CORRECT_CURRENT_ADMIN(80, "Correctie van actuele administratieve gegevens", false),
  CORRECT_HISTORIC_ADMIN(90, "Correctie van historische administratieve gegevens", false),
  CORRECT_CATEGORY(100, "Correctie van onterecht opgenomen categorie", false),
  SUPER_CHANGE(110, "Superuser: Overschrijven van gegevens", true),
  SUPER_DEL_ACT(120, "Superuser: Verwijderen actueel record", true),
  SUPER_DEL_HIST(130, "Superuser: Verwijderen historisch record", true),
  SUPER_DEL_CAT(140, "Superuser: Verwijderen hele categorie", true),
  NO_ACTION_INCORRECT_HIST(400, "Geen actie mogelijk op onjuiste historie", false),
  ONBEKEND(-1, "Onbekend", false);

  private int     code;
  private String  description;
  private boolean superuser;

  PersonListActionType(int code, String description, boolean superuser) {
    this.code = code;
    this.description = description;
    this.superuser = superuser;
  }

  public static PersonListActionType get(int code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode() == code)
        .findFirst()
        .orElse(ONBEKEND);

  }

  public boolean is(PersonListActionType... types) {
    return Arrays.stream(types).anyMatch(type -> this == type);
  }

  @Override
  public String toString() {
    return getDescription();
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public boolean isSuperuser() {
    return superuser;
  }
}
