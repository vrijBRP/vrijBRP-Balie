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

import java.io.Serializable;

import nl.procura.diensten.gba.ple.base.BasePLList;

public class WkSearchResult implements Serializable {

  private static final long serialVersionUID = -3690781277780069797L;

  private BasePLList<BaseWK>        baseWKs  = new BasePLList<>();
  private BasePLList<BaseWKMessage> messages = new BasePLList<>();

  public BasePLList<BaseWK> getBaseWKs() {
    return baseWKs;
  }

  public BasePLList<BaseWKMessage> getMessages() {
    return messages;
  }
}
