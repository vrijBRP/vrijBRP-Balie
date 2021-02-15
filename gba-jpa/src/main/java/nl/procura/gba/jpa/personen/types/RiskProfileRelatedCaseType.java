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

package nl.procura.gba.jpa.personen.types;

import static java.lang.String.format;

import java.util.NoSuchElementException;

import nl.procura.gba.jpa.personen.db.RiskProfileType;
import nl.procura.gba.jpa.personen.db.RiskProfileZaakType;

public enum RiskProfileRelatedCaseType {

  BINNENGEMEENTELIJK(RiskProfileZaakType.BINNENGEMEENTELIJK, "Binnengemeentelijk"),
  INTERGEMEENTELIJK(RiskProfileZaakType.INTERGEMEENTELIJK, "Intergemeentelijk"),
  HERVESTIGING(RiskProfileZaakType.HERVESTIGING, "Hervestiging"),
  INSCHRIJVING(RiskProfileZaakType.INSCHRIJVING, "Eerste inschrijving");

  private final RiskProfileZaakType type;
  private final String              descr;

  RiskProfileRelatedCaseType(RiskProfileZaakType type, String descr) {
    this.type = type;
    this.descr = descr;
  }

  public RiskProfileZaakType type() {
    return type;
  }

  public String descr() {
    return descr;
  }

  @Override
  public String toString() {
    return descr;
  }

  public RiskProfileType toRiskProfileType(Long riskProfileId) {
    return new RiskProfileType(riskProfileId, type);
  }

  public static RiskProfileRelatedCaseType of(RiskProfileType type) {
    for (RiskProfileRelatedCaseType value : values()) {
      if (value.type == type.getType()) {
        return value;
      }
    }
    throw new NoSuchElementException(format("RiskProfileType %s not found", type.getType()));
  }
}
