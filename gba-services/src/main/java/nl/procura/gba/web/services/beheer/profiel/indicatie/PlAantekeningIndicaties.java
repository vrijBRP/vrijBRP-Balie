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

package nl.procura.gba.web.services.beheer.profiel.indicatie;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;

public class PlAantekeningIndicaties {

  private List<PlAantekeningIndicatie> all = new ArrayList<>();

  public PlAantekeningIndicaties() {
  }

  public PlAantekeningIndicaties(List<PlAantekeningIndicatie> all) {
    setAll(all);
  }

  public List<PlAantekeningIndicatie> getAlle() {
    return all;
  }

  public PlAantekeningIndicatie getIndicatie(PlAantekeningIndicatie plIndicatie) {
    for (PlAantekeningIndicatie indicatie : getAlle()) {
      if (indicatie.equals(plIndicatie)) {
        return indicatie;
      }
    }

    return null;
  }

  public void setAll(List<PlAantekeningIndicatie> all) {
    this.all = all;
  }

  @Override
  public String toString() {
    return "PlAantekeningIndicatiesImpl [all=" + all + "]";
  }
}
