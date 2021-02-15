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

package nl.procura.gba.web.components.fields.values;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

public class MultiFieldValue<T> {

  private List<T> values = new ArrayList<>();

  public MultiFieldValue() {
  }

  public MultiFieldValue(List<T> values) {

    setValues(values);
  }

  public List<T> getValues() {
    return values;
  }

  public void setValues(List<T> values) {
    this.values = values;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    if (values.size() > 0) {

      for (Object value : values) {

        sb.append(astr(value));
        sb.append(", ");
      }
    }

    return trim(sb.toString());
  }
}
