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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GbaWsPersonListSet {

  private Integer                  index;
  private Integer                  internalIndex;
  private Boolean                  mostRecentMarriage;
  private List<GbaWsPersonListRec> records = new ArrayList<>();

  @Transient
  public Optional<GbaWsPersonListRec> getByStatus(GBARecStatus status) {
    return records.stream().filter(r -> r.getStatus().getCode() == status.getCode()).findFirst();
  }

  @Transient
  public Optional<GbaWsPersonListRec> getCurrentRec() {
    return getByStatus(GBARecStatus.CURRENT);
  }
}
