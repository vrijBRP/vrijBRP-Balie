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

import static nl.procura.standard.Globalfunctions.aval;

import com.vaadin.data.Property;

import nl.procura.vaadin.component.field.NumberField;

public class HuisnummerVeld extends NumberField {

  public HuisnummerVeld() {
  }

  @Override
  public void setPropertyDataSource(Property newDataSource) {

    if ((aval(newDataSource) < 0)) {
      if (Number.class.isAssignableFrom(newDataSource.getType())) {
        newDataSource.setValue(0);
      } else if ((aval(newDataSource) < 0) && (newDataSource.getType() == String.class)) {
        newDataSource.setValue("");
      }

    }

    super.setPropertyDataSource(newDataSource);
  }
}
