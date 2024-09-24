/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page20;

import static nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieDossierNummer.ofGemeente;
import static nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieDossierNummer.ofValue;

import com.vaadin.data.validator.AbstractStringValidator;

public class NaturalisatieDossierNummerValidator extends AbstractStringValidator {

  public NaturalisatieDossierNummerValidator(long gemeenteCode) {
    super(String.format("Dossiernummer voldoet niet aan het formaat, zoals %s",
        ofGemeente(gemeenteCode).getValue()));
  }

  @Override
  protected boolean isValidString(String value) {
    return ofValue(value).isValid();
  }
}
