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

import java.util.ArrayList;
import java.util.List;

public class RelatieLijst {

  private List<Relatie> relaties = new ArrayList<>();

  public List<Relatie> getRelaties() {
    return relaties;
  }

  public void setRelaties(List<Relatie> relaties) {
    this.relaties = relaties;
  }

  public List<Relatie> getSortedRelaties() {

    List<Relatie> l = new ArrayList<>();

    l.addAll(getRelaties(RelatieType.AANGEVER));
    l.addAll(getRelaties(RelatieType.PARTNER));
    l.addAll(getRelaties(RelatieType.OUDER));
    l.addAll(getRelaties(RelatieType.MEERDERJARIG_KIND));
    l.addAll(getRelaties(RelatieType.KIND));
    l.addAll(getRelaties(RelatieType.GEZAGHEBBENDE));
    l.addAll(getRelaties(RelatieType.NIET_GERELATEERD));
    l.addAll(getRelaties(RelatieType.ONBEKEND));

    return l;
  }

  private List<Relatie> getRelaties(List<Relatie> relaties, RelatieType... types) {

    List<Relatie> l = new ArrayList<>();

    for (Relatie r : relaties) {

      for (RelatieType t : types) {

        if (r.getRelatieType() == t) {

          l.add(r);
        }
      }
    }

    return l;
  }

  private List<Relatie> getRelaties(RelatieType... types) {
    return getRelaties(relaties, types);
  }
}
