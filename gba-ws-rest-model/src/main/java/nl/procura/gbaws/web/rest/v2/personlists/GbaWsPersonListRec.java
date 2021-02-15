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

package nl.procura.gbaws.web.rest.v2.personlists;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GbaWsPersonListRec {

  private Integer                   index;
  private GbaWsPersonListRecStatus  status;
  private boolean                   incorrect;
  private boolean                   bagChange;
  private boolean                   adminHist;
  private boolean                   conflicting;
  private List<GbaWsPersonListElem> elems = new ArrayList<>();

  @Transient
  public Optional<GbaWsPersonListElem> getElem(GBAElem elem) {
    return elems.stream()
        .filter(e -> e.getCode() == elem.getCode())
        .filter(e -> StringUtils.isNotBlank(e.getValue().getVal()))
        .findFirst();
  }

  @Transient
  public List<GbaWsPersonListElem> getElems(GBAElem elem, GBAElem... moreElements) {
    List<GBAElem> list = new ArrayList<>();
    list.add(elem);
    list.addAll(Arrays.asList(moreElements));
    return list.stream()
        .filter(Objects::nonNull)
        .map(this::getElem)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  @Transient
  public String getElemValue(GBAElem elem) {
    return getElem(elem).map(e -> e.getValue().getVal()).orElse(null);
  }

  @Transient
  public String getElemDescr(GBAElem elem) {
    return getElem(elem).map(e -> e.getValue().getDescr()).orElse(null);
  }

  @Transient
  public boolean hasElems() {
    return !elems.isEmpty();
  }
}
