/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.applicatie.meldingen.types;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.CITIZEN;

import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;

public class InwonerMelding extends ServiceMelding {

  public InwonerMelding(int count) {
    setId("Signalen Inwoner.app");
    setAdminOnly(false);
    setCategory(CITIZEN);
    setSeverity(WARNING);
    setMelding(String.format("Inwoner: %s signalen", count));
  }
}
