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

package nl.procura.gba.web.services.zaken.algemeen;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakStatusType;

public class ZaakStatusHistorie {

  private List<ZaakStatus> statussen = new ArrayList<>();

  public ZaakStatus getHuidigeStatus() {
    return statussen.size() > 0 ? statussen.get(0) : new ZaakStatus();
  }

  public List<ZaakStatus> getStatussen() {
    return statussen;
  }

  public void setStatussen(List<ZaakStatus> statussen) {
    this.statussen = statussen;
  }

  public boolean isEindStatus() {
    return (getHuidigeStatus().getStatus().isEindStatus());
  }

  public boolean isHuidigeStatus(ZaakStatusType status) {
    return (getHuidigeStatus().getStatus() == status);
  }

  public boolean isInHistorieIngevoerd(ZaakStatusType status) {
    return getHuidigeStatus().isCorrectIngevoerd() && isHuidigeStatus(status);
  }

  public int size() {
    return statussen.size();
  }
}
