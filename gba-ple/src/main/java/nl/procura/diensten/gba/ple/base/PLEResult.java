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

package nl.procura.diensten.gba.ple.base;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class PLEResult implements Serializable {

  private static final long serialVersionUID = -5912694569454493813L;

  private BasePLList<BasePL>     basePLs    = new BasePLList<>();
  private BasePLList<PLEMessage> messages   = new BasePLList<>();
  private PLEDatasource          datasource = PLEDatasource.STANDAARD;

  public BasePLList<BasePL> getBasePLs() {
    return basePLs;
  }

  public void setBasePLs(BasePLList<BasePL> basePLs) {
    this.basePLs = basePLs;
  }

  public BasePLList<PLEMessage> getMessages() {
    return messages;
  }

  public void setMessages(BasePLList<PLEMessage> messages) {
    this.messages = messages;
  }

  public PLEDatasource getDatasource() {
    return datasource;
  }

  public void setDatasource(PLEDatasource datasource) {
    this.datasource = datasource;
  }
}
