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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class TitelvermeldContainer extends ArrayListContainer {

  private VermeldTitelType defaultValue = null;

  public TitelvermeldContainer(BasePLExt pl) {
    BasePLElem tp = pl.getPersoon().getTitel();
    if (StringUtils.isBlank(tp.getValue().getVal())) {
      addItem(VermeldTitelType.NVT);
      defaultValue = VermeldTitelType.NVT;
    } else {
      addItem(VermeldTitelType.JA);
      addItem(VermeldTitelType.NEE);
    }
  }

  public VermeldTitelType getDefaultValue() {
    return defaultValue;
  }
}
