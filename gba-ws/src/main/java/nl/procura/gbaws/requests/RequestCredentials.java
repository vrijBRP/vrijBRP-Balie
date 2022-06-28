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

package nl.procura.gbaws.requests;

import nl.procura.gbaws.db.wrappers.UsrWrapper;

import lombok.Data;

@Data
public final class RequestCredentials {

  private UsrWrapper user;
  private String     password = "";
  private String     username = "";

  public RequestCredentials() {
  }

  public RequestCredentials(UsrWrapper user) {
    this.user = user;
  }

  public RequestCredentials(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
