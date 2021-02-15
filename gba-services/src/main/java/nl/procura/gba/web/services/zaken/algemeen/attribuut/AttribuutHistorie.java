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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

public class AttribuutHistorie {

  private List<ZaakAttribuut> attributen = new ArrayList<>();

  public boolean exists() {
    return pos(size());
  }

  public List<ZaakAttribuut> getAttributen() {
    return attributen;
  }

  public void setAttributen(List<ZaakAttribuut> attributen) {
    this.attributen = attributen;
  }

  public boolean is(ZaakAttribuutType type) {
    for (ZaakAttribuut z : attributen) {
      if (z.getAttribuut().equals(type.getCode())) {
        return true;
      }
    }
    return false;
  }

  public boolean isNot(ZaakAttribuutType type) {
    for (ZaakAttribuut z : attributen) {
      if (z.getAttribuut().equals(type.getCode())) {
        return false;
      }
    }
    return true;
  }

  public int size() {
    return getAttributen().size();
  }
}
