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

package nl.procura.gba.web.services.zaken.zakenregister;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class ZaakItem {

  private String         caption = "";
  private ZaakStatusType status;
  private ZaakType[]     types;
  private ZaakSortering  sortering;

  public void setType(ZaakType type) {
    setTypes(new ZaakType[]{ type });
  }

  @Override
  public String toString() {
    if (StringUtils.isNotBlank(caption)) {
      return caption;
    }
    if (status != null) {
      return status.toString();
    }
    if (types != null) {
      return types[0].toString();
    }
    return "";
  }
}
