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

package nl.procura.diensten.gba.wk.baseWK;

import java.beans.Transient;
import java.io.Serializable;

import nl.procura.burgerzaken.gba.NumberUtils;

import lombok.Data;

@Data
public class BaseWKPerson implements Serializable {

  private static final long serialVersionUID = 2962748643597331037L;

  private BaseWKValue anummer       = new BaseWKValue();
  private BaseWKValue bsn           = new BaseWKValue();
  private BaseWKValue datum_ingang  = new BaseWKValue();
  private BaseWKValue datum_vertrek = new BaseWKValue();
  private BaseWKValue gezin_code    = new BaseWKValue();
  private BaseWKValue volg_code     = new BaseWKValue();
  private BaseWKValue datum_geboren = new BaseWKValue();

  @Transient
  public boolean isCurrentResident() {
    return NumberUtils.toInt(getDatum_vertrek().getValue()) < 0;
  }
}
