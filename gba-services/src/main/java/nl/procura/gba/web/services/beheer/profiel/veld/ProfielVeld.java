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

package nl.procura.gba.web.services.beheer.profiel.veld;

import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

public enum ProfielVeld {

  ONBEKEND(new Veld(ProfielVeldType.ONBEKEND, VeldDescr.ONBEKEND)),
  PL_VERSTREKKINGSBEPERKING(new Veld(ProfielVeldType.PERSOONSLIJST, VeldDescr.VERSTREKKINGSBEPERKING));

  private Veld veld;

  ProfielVeld() {
  }

  ProfielVeld(Veld veld) {
    this.veld = veld;
  }

  public static ProfielVeld getVeld(ProfielVeldType type, ProfielVeld veld) {
    for (ProfielVeld p : values()) {
      if (p.getVeld().getType() == type && eq(p.getVeld().getField(), veld.getVeld().getField())) {
        return p;
      }
    }

    return null;
  }

  public static List<ProfielVeld> getVelden() {

    List<ProfielVeld> list = new ArrayList<>();

    for (ProfielVeld veld : values()) {

      if (fil(veld.getVeld().getField())) {

        list.add(veld);
      }
    }
    return list;
  }

  public Veld getVeld() {
    return veld;
  }
}
