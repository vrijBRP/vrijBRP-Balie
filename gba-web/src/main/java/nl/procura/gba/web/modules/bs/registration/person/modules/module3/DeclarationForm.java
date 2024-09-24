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

package nl.procura.gba.web.modules.bs.registration.person.modules.module3;

import static nl.procura.gba.web.modules.bs.registration.person.modules.module3.DeclarationBean.F_DECLARATION_DONE_BY;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module3.DeclarationBean.F_EXPLANATION;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DeclarationType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

public class DeclarationForm extends GbaForm<DeclarationBean> {

  private static final int     REGISTERED_PERSON_AGE_LIMIT = 16;
  private final DossierPersoon person;

  DeclarationForm(DossierPersoon person) {
    this.person = person;
    setColumnWidths("200px", "");
    setOrder(F_DECLARATION_DONE_BY,
        F_EXPLANATION);

    final DeclarationBean declarationBean = new DeclarationBean();
    declarationBean.setDeclarationDoneBy(DeclarationType.valueOfCode(person.getBron()));
    declarationBean.setExplanation(person.getToelichting());
    setBean(declarationBean);
  }

  void save() {
    commit();
    validateDeclarationDoneBy();
    final DeclarationBean declarationBean = getBean();
    person.setBron(declarationBean.getDeclarationDoneBy().getCode());
    person.setToelichting(declarationBean.getExplanation());
  }

  private void validateDeclarationDoneBy() {
    final int personaAge = person.getLeeftijd();
    final DeclarationType declarationDoneBy = (DeclarationType) getField(
        DeclarationBean.F_DECLARATION_DONE_BY).getValue();
    if (declarationDoneBy == DeclarationType.REGISTERED) {
      if (personaAge < REGISTERED_PERSON_AGE_LIMIT) {
        throw new ProException(ProExceptionSeverity.WARNING, DeclarationType.REGISTERED.toString()
            + " kan alleen gekozen worden voor personen >= " + REGISTERED_PERSON_AGE_LIMIT);
      }
    }
  }
}
