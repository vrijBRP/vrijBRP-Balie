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

package nl.procura.gba.jpa.personen.types;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;

public class RiskProfileRuleMap {

  private Set<RuleVariable> variables = new LinkedHashSet<>();

  public RiskProfileRuleMap add(RiskProfileRuleVar variable, String label, Type type) {
    return add(variable, label, type, true);
  }

  public RiskProfileRuleMap add(RiskProfileRuleVar variable, String label, Type type, boolean required) {
    RuleVariable var = new RuleVariable();
    var.setVariable(variable.name().toLowerCase());
    var.setLabel(label);
    var.setType(type);
    var.setRequired(required);
    variables.add(var);
    return this;
  }

  public Set<RuleVariable> getVariables() {
    return variables;
  }

  public enum Type {
    DATE,
    NUMBER,
    POSNUMBER,
    TEXT,
    COUNTRY,
    DISTRICT1,
    DISTRICT2,
    DISTRICT3,
    STREET,
    POSTALCODE,
  }

  @Data
  public class RuleVariable {

    private String  variable = "";
    private String  label    = "";
    private Type    type     = null;
    private String  value    = "";
    private boolean required = true;
  }
}
