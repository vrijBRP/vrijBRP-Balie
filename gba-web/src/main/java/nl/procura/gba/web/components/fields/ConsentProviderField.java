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

package nl.procura.gba.web.components.fields;

import com.vaadin.ui.TextField;

import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;

public class ConsentProviderField extends TextField {

  private static final long serialVersionUID = -8325746835387082441L;

  @Override
  public Class<?> getType() {
    return ConsentProvider.class;
  }

  @Override
  protected void setInternalValue(Object newValue) {
    // set value is called with "" in TextField constructor
    if ("".equals(newValue)) {
      super.setInternalValue(ConsentProvider.notDeclared());
    } else {
      super.setInternalValue(newValue);
    }
  }
}
