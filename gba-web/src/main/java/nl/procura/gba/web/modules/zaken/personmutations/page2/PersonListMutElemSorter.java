/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_VERBINTENIS;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class PersonListMutElemSorter implements Comparator<PersonListMutElem> {

  @Override
  public int compare(PersonListMutElem o1, PersonListMutElem o2) {

    int cat1 = o1.getTypes().getCat().getCode();
    int cat2 = o2.getTypes().getCat().getCode();

    int grp1 = o1.getTypes().getGroup().getCode();
    int grp2 = o2.getTypes().getGroup().getCode();

    int set1 = o1.getSet().getExtIndex();
    int set2 = o2.getSet().getExtIndex();

    int elem1 = o1.getElem().getElemCode();
    int elem2 = o2.getElem().getElemCode();

    // Uitzondering voor land en plaats. Hier wil je eerst land hebben
    if (o1.getElemType().is(GEBOORTELAND, LAND_VERBINTENIS, LAND_ONTBINDING, LAND_OVERL)
        && o2.getElemType().is(GEBOORTEPLAATS, PLAATS_VERBINTENIS, PLAATS_ONTBINDING, PLAATS_OVERL)) {
      return -1;
    }

    return new CompareToBuilder()
        .append(cat1, cat2)
        .append(grp1, grp2)
        .append(set1, set2)
        .append(elem1, elem2)
        .toComparison();
  }
}
