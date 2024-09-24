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

package nl.procura.gba.web.modules.bs.registration.person.modules.module1;

import java.util.function.Supplier;

import com.vaadin.data.validator.AbstractStringValidator;

import nl.procura.gba.web.services.Services;

public class ExistingIdNumberValidator extends AbstractStringValidator {

  private final Supplier<Services> servicesSupplier;

  public enum TYPE {
    BSN,
    ANR
  }

  public ExistingIdNumberValidator(TYPE type, Supplier<Services> servicesSupplier) {
    super(type == TYPE.BSN
        ? "Burgerservicenummer komt al voor in de BRP"
        : "A-nummer komt al voor in de BRP");
    this.servicesSupplier = servicesSupplier;
  }

  @Override
  protected boolean isValidString(String s) {
    return servicesSupplier.get().getPersonenWsService().getPersoonslijst(s).getCats().isEmpty();
  }
}
