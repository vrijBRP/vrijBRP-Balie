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

package nl.procura.gba.web.services.zaken.gv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GvAanvraagProcessen implements Serializable {

  private List<GvAanvraagProces> processen = new ArrayList<>();

  public GvAanvraagProcessen() {
  }

  public GvAanvraagProces getProces() {
    return processen.size() > 0 ? processen.get(0) : new GvAanvraagProces();
  }

  public void setProces(GvAanvraagProces proces) {
    if (proces.equals(getProces())) {
      getProces().setDatumEindeTermijn(proces.getDatumEindeTermijn());
      getProces().setProcesActieType(proces.getProcesActieType());
      getProces().setReactieType(proces.getReactieType());
      getProces().setMotivering(proces.getMotivering());
    } else {
      processen.add(0, proces);
    }
  }

  public List<GvAanvraagProces> getProcessen() {
    Collections.sort(processen);
    return processen;
  }

  public void setStatussen(List<GvAanvraagProces> processen) {
    this.processen = processen;
  }
}
