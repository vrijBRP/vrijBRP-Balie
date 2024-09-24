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

package nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.common.EnumWithCode;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class EnumContainer<T extends EnumWithCode<?>> extends ArrayListContainer {

  public EnumContainer(T[] enums) {
    this(Arrays.asList(enums));
  }

  public EnumContainer(List<T> enums) {
    for (EnumWithCode<?> en : enums) {
      if (en.getCode() instanceof Number) {
        if (Long.parseLong(en.getCode().toString()) >= 0L) {
          addItem(en);
        }
      } else if (isNotBlank(en.getCode().toString())) {
        addItem(en);
      }
    }
  }
}
