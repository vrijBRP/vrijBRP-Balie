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

package nl.procura.gba.web.services.beheer.parameter;

import static nl.procura.standard.Globalfunctions.emp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

public class Parameters implements Serializable {

  private static final long serialVersionUID = 5424663635426940929L;

  private List<Parameter> all = new ArrayList<>();

  public Parameters() {
  }

  public Parameters(List<Parameter> all) {
    this.all = all;
  }

  public Parameter get(ParameterType parameterType) {
    return get(parameterType, false);
  }

  public Parameter get(ParameterType parameterType, boolean required) {
    Parameter pa = new Parameter();
    if (parameterType != null) {
      for (Parameter p : getAlle()) {
        if (parameterType.isKey(p.getParm())) {
          pa = p;
          break;
        }
      }
    }

    if (required && emp(pa.getValue())) {
      throw new ProException(ProExceptionSeverity.WARNING,
          "Parameter {0} is niet gevuld", parameterType.toString());
    }

    return pa;
  }

  public List<Parameter> getAlle() {
    return all;
  }

  @Override
  public String toString() {
    return "ParametersImpl [all=" + all + "]";
  }
}
