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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.standard.Globalfunctions.aval;

import com.vaadin.data.validator.AbstractStringValidator;

public class MaxAantalValidator extends AbstractStringValidator {

  private final int max;
  private final int min;

  public MaxAantalValidator(int min, int max) {
    super("Geef een aantal in tussen " + min + " en " + max + ".");
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean isValidString(String value) {
    int aantal = aval(value);
    return aantal >= min && aantal <= max;
  }
}
