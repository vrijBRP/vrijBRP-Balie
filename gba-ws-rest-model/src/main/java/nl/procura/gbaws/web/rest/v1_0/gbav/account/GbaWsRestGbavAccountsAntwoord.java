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

package nl.procura.gbaws.web.rest.v1_0.gbav.account;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import nl.procura.proweb.rest.v1_0.ProRestAntwoordImpl;

@XmlRootElement(name = "antwoord")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "accounts" })
public class GbaWsRestGbavAccountsAntwoord extends ProRestAntwoordImpl {

  @XmlElement(name = "accounts")
  private List<GbaWsRestGbavAccount> accounts = new ArrayList<>();

  public GbaWsRestGbavAccountsAntwoord() {
  }

  public List<GbaWsRestGbavAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<GbaWsRestGbavAccount> accounts) {
    this.accounts = accounts;
  }
}
