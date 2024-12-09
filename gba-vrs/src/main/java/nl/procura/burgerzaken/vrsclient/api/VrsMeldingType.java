/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.vrsclient.api;

import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_OVERIG;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_VERM;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VrsMeldingType {

  RGP("RGP", "Reisdocument is gepersonaliseerd", new ArrayList<>()),
  RVU("RVU", "Reisdocument is vervallen vóór uitreiking", new ArrayList<>()),
  RRV("RRV", "Reisdocument is van rechtswege vervallen", Arrays.asList(RECHTSW_VERM, RECHTSW_OVERIG)),
  RRU("RRU", "Reisdocument is uitgereikt", new ArrayList<>()),
  RDE("RDE", "Reisdocument geldig tot verstreken", new ArrayList<>()),
  RDO("RDO", "Reisdocument is definitief onttrokken", singletonList(VrsActieType.DEF_ONTREKK)),
  ONBEKEND("", "Onbekend", new ArrayList<>());

  private final String             code;
  private final String             description;
  private final List<VrsActieType> actieTypes;

  public static VrsMeldingType getByActieType(VrsActieType actieType) {
    return Arrays.stream(values())
        .filter(a -> a.getActieTypes().contains(actieType))
        .findFirst()
        .orElse(ONBEKEND);
  }

  public static VrsMeldingType getByCode(String code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode().equals(code))
        .findFirst()
        .orElse(ONBEKEND);
  }

  @Override
  public String toString() {
    return description + (isBlank(code) ? "" : " (" + code + ")");
  }
}
