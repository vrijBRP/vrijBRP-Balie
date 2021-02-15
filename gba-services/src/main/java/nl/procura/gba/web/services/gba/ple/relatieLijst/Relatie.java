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

package nl.procura.gba.web.services.gba.ple.relatieLijst;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class Relatie {

  private BasePLExt   pl          = null;
  private RelatieType relatieType = RelatieType.ONBEKEND;
  private boolean     huisgenoot  = false;

  public BasePLExt getPl() {
    return pl;
  }

  public void setPl(BasePLExt pl) {
    this.pl = pl;
  }

  public RelatieType getRelatieType() {
    return relatieType;
  }

  public void setRelatieType(RelatieType relatieType) {
    this.relatieType = relatieType;
  }

  public boolean isHuisgenoot() {
    return huisgenoot;
  }

  public void setHuisgenoot(boolean huisgenoot) {
    this.huisgenoot = huisgenoot;
  }

  public boolean isRelatieType(RelatieType... types) {
    for (RelatieType type : types) {
      if (type == getRelatieType()) {
        return true;
      }
    }

    return false;
  }
}
