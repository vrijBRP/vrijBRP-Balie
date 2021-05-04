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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;

public class PersonListMutElems extends ArrayList<PersonListMutElem> {

  public boolean isAllBlank(GBAElem... elems) {
    return Arrays.stream(elems)
        .allMatch(elem -> stream()
            .filter(e -> e.getElemType() == elem)
            .allMatch(PersonListMutElem::isBlank));
  }

  public boolean isApplicable(Predicate<PersonListMutElem> predicate, GBAElem... elems) {
    return Arrays.stream(elems)
        .allMatch(elem -> stream()
            .filter(e -> e.getElemType() == elem)
            .allMatch(predicate::test));
  }

  public boolean isAllBlank(GBAGroup... groups) {
    return Arrays.stream(groups)
        .allMatch(group -> stream()
            .filter(e -> e.getElemType().isNational())
            .filter(e -> e.getGroup() == group)
            .allMatch(PersonListMutElem::isBlank));
  }

  public Optional<PersonListMutElem> getElem(GBAElem elem) {
    return this.stream()
        .filter(e -> e.getElemType().isNational())
        .filter(e -> e.getElemType().getCode() == elem.getCode())
        .findFirst();
  }

  public void validateAll() {
    this.forEach(mutElem -> mutElem.getField().validate());
  }
}
