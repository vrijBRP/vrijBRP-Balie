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

package nl.procura.gba.web.components.layouts.form.document.sign;

import nl.procura.gba.web.common.validators.GbaEmailValidator;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.VaadinValidator;

public class InitiateDocumentSignForm extends GbaForm<InitiateDocumentSignBean> {
  public InitiateDocumentSignForm(String personName, String document, String defaultEmail) {
    setOrder(InitiateDocumentSignBean.PERSON_NAME, InitiateDocumentSignBean.DOCUMENT, InitiateDocumentSignBean.EMAIL, InitiateDocumentSignBean.LANGUAGE);
    setColumnWidths("170px", "");

    InitiateDocumentSignBean bean = new InitiateDocumentSignBean();
    bean.setPersonName(personName);
    bean.setDocument(document);
    bean.setEmail(defaultEmail);
    bean.setLanguage(SignInterfaceLanguage.NL);

    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    getField(InitiateDocumentSignBean.EMAIL).addValidator(VaadinValidator.of(new GbaEmailValidator()));

    super.afterSetBean();
  }
}
