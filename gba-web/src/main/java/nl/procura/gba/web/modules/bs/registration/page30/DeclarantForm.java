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

import static java.util.Collections.unmodifiableList;

import java.util.List;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.RegistrationDeclarant;

class DeclarantForm extends GbaForm<DeclarantBean> {

  private final DossierRegistration  registration;
  private final List<DossierPersoon> people;

  DeclarantForm(DossierRegistration registration, List<DossierPersoon> people) {
    this.registration = registration;
    this.people = unmodifiableList(people);
    setOrder(DeclarantBean.F_PERSON, DeclarantBean.F_COMMENT);
    DeclarantBean bean = new DeclarantBean();
    bean.setComment(registration.getDeclarantComment());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    GbaNativeSelect peopleField = getField(DeclarantBean.F_PERSON, GbaNativeSelect.class);
    DeclarantContainer container = new DeclarantContainer(people);
    peopleField.setContainerDataSource(container);
    container.getItem(registration.getDeclarant()).ifPresent(peopleField::setValue);
    super.afterSetBean();
  }

  void saveDeclarant(DossierRegistration registration) {
    commit();
    DeclarantBean bean = getBean();
    registration.setDeclarant((RegistrationDeclarant) bean.getPerson().getValue());
    registration.setDeclarantComment(bean.getComment());
  }
}
