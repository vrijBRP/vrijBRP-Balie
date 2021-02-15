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

package nl.procura.gba.web.services.zaken.rijbewijs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RijbewijsAanvraagStatussen implements Serializable {

  private static final long serialVersionUID = 7699681347779069042L;

  private List<RijbewijsAanvraagStatus> statussen = new ArrayList<>();

  public RijbewijsAanvraagStatussen() {
  }

  /**
   * Controle of status al bestaat
   */
  public void addStatus(RijbewijsAanvraagStatus status) {

    for (RijbewijsAanvraagStatus s : getStatussen()) {

      boolean eqStatus = s.getStatus().equals(status.getStatus());
      boolean eqDate = s.getDatumTijdRdw().getLongDate() == status.getDatumTijdRdw().getLongDate();
      boolean eqTime = s.getDatumTijdRdw().getLongTime() == status.getDatumTijdRdw().getLongTime();

      if (eqStatus && eqDate && eqTime) {
        return;
      }
    }

    getStatussen().add(status);
  }

  public RijbewijsAanvraagStatus getStatus() {
    return getStatussen().size() > 0 ? getStatussen().get(0) : new RijbewijsAanvraagStatus();
  }

  public RijbewijsAanvraagStatus getStatus(RijbewijsStatusType status) {
    for (RijbewijsAanvraagStatus rijbewijsStatus : getStatussen()) {
      if (rijbewijsStatus.getStatus() == status) {
        return rijbewijsStatus;
      }
    }
    return null;
  }

  public List<RijbewijsAanvraagStatus> getStatussen() {
    Collections.sort(statussen);
    return statussen;
  }

  public void setStatussen(List<RijbewijsAanvraagStatus> statussen) {
    this.statussen = statussen;
  }
}
