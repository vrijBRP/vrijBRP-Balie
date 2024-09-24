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
  DELETE_MUT(40, "Mutatie verwijderen", false, true),
  ADD_HISTORIC(50, "Historie toevoegen", false),
  CORRECT_CURRENT_GENERAL(60, "Correctie van actuele algemene gegevens", false),
  CORRECT_HISTORIC_GENERAL(70, "Correctie van historische algemene gegevens", false),
  CORRECT_CURRENT_ADMIN(80, "Correctie van actuele administratieve gegevens", false),
  CORRECT_HISTORIC_ADMIN(90, "Correctie van historische administratieve gegevens", false),
  CORRECT_CATEGORY(100, "Correctie van onterecht opgenomen categorie", false),
  SUPER_OVERWRITE_CURRENT(110, "Superuser: Overschrijven van actuele gegevens", true),
  SUPER_OVERWRITE_HISTORIC(111, "Superuser: Overschrijven van historische gegevens", true),
  SUPER_DELETE_CURRENT(120, "Superuser: Verwijderen actueel record", true, true),
  SUPER_DELETE_HISTORIC(130, "Superuser: Verwijderen historisch record", true, true),
  REMOVE_BLOCK(150, "Blokkering verwijderen", false, true),
  OVERWRITE_CURRENT(160, "Overschrijven van actuele gegevens (datum opneming blijft hetzelfde)", false),
  OVERWRITE_HISTORIC(161, "Overschrijven van historische gegevens (datum opneming blijft hetzelfde)", false),
  ONBEKEND(-1, "Onbekend", false);

  private final int     code;
  private final String  description;
  private final boolean superuser;
  private boolean       skipElements;

  PersonListActionType(int code, String description, boolean superuser) {
    this.code = code;
    this.description = description;
    this.superuser = superuser;
  }

  PersonListActionType(int code, String description, boolean superuser, boolean skipElements) {
    this.code = code;
    this.description = description;
    this.superuser = superuser;
    this.skipElements = skipElements;
  }

  public static PersonListActionType get(int code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode() == code)
        .findFirst()
        .orElse(ONBEKEND);

  }

  public boolean is(PersonListActionType... types) {
    return Arrays.asList(types).contains(this);
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

  public boolean isSkipElements() {
    return skipElements;
  }
}
