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

package nl.procura.gba.web.modules.bs.naturalisatie.valuechoice;

import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "create")
public class ValueChoiceConfig<T> {

  private String                                        title;
  private Getter<DossierNaturalisatieVerzoeker, Object> component;
  private Getter<DossierNaturalisatieVerzoeker, T>      getter;
  private Setter<T>                                     setter;
  private boolean                                       required;

  public interface Getter<T, R> {

    R apply(T t);
  }

  public interface Setter<VALUE> {

    void accept(DossierNaturalisatieVerzoeker t, VALUE u);
  }
}
