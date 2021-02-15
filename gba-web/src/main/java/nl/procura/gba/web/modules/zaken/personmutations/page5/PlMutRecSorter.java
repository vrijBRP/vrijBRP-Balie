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

package nl.procura.gba.web.modules.zaken.personmutations.page5;

import java.util.Comparator;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.jpa.personen.db.PlMutRec;

public class PlMutRecSorter implements Comparator<PlMutRec> {

  @Override
  public int compare(PlMutRec o1, PlMutRec o2) {

    GBAGroupElements.GBAGroupElem elem1 = GBAGroupElements.getByCat(o1.getPlMut().getCat().intValue(),
        o1.getId().getElem());
    GBAGroupElements.GBAGroupElem elem2 = GBAGroupElements.getByCat(o2.getPlMut().getCat().intValue(),
        o2.getId().getElem());

    int cat1 = elem1.getCat().getCode();
    int cat2 = elem2.getCat().getCode();

    int grp1 = elem1.getGroup().getCode();
    int grp2 = elem2.getGroup().getCode();

    if (cat1 > cat2) {
      return 1;
    }

    if (cat1 < cat2) {
      return -1;
    }

    if (grp1 > grp2) {
      return 1;
    }

    if (grp1 < grp2) {
      return -1;
    }

    return (o1.getId().getElem() > o2.getId().getElem()) ? 1 : -1;
  }
}
