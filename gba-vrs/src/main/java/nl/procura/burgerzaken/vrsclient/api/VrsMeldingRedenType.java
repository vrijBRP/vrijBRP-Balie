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

import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.DEF_ONTREKK;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_OVERIG;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_VERM;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingType.RDO;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingType.RRV;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VrsMeldingRedenType {

  // RRV
  RENV("RENV", "Reisdocument Nederlanderschap verloren", RRV, RECHTSW_OVERIG),
  REVV("REVV", "Reisdocument de redenen tot verstrekking zijn vervallen", VrsMeldingType.ONBEKEND, VrsActieType.ONBEKEND),
  REGV("REGV", "Reisdocument gegevens onjuist na verstrekking", RRV, RECHTSW_OVERIG),
  REVS("REVS", "Reisdocument verlies status/verblijfstitel, verkrijgen Nederlanderschap of "
      + "nationaliteit/document ander land", RRV, RECHTSW_OVERIG),
  REHG("REHG", "Reisdocument houdergegevens gewijzigd", RRV, RECHTSW_OVERIG),
  REHO("REHO", "Reisdocument houder overleden", VrsMeldingType.ONBEKEND, VrsActieType.ONBEKEND),
  REVG("REVG", "Reisdocument vermist/gestolen", RRV, RECHTSW_VERM),
  REVF("REVF", "Reisdocument vermoeden fraude", RRV, RECHTSW_OVERIG),
  REFU("REFU", "Reisdocument FTF Uitreisverbod", VrsMeldingType.ONBEKEND, VrsActieType.ONBEKEND),
  REIT("REIT", "Reisdocument intrekking toestemming", RRV, RECHTSW_OVERIG),
  // RDO
  REDO("REDO", "Reisdocument is ingeleverd", RDO, DEF_ONTREKK),
  REBO("REBO", "Reisdocument is onleesbaar, beschadigd en/of incompleet", RDO, DEF_ONTREKK),
  REOW("REOW", "Reisdocument er zijn onbevoegde wijzigingen aangebracht", RDO, DEF_ONTREKK),
  REGO("REGO", "Reisdocument gezichtsopname is onvoldoende gelijkend", RDO, DEF_ONTREKK),
  ONBEKEND("", "Onbekend", VrsMeldingType.ONBEKEND, VrsActieType.ONBEKEND);


  private final String         code;
  private final String         description;
  private final VrsMeldingType meldingType;
  private final VrsActieType   actieType;

  public static List<VrsMeldingRedenType> getByActieType(VrsActieType actieType) {
    return Arrays.stream(values())
        .filter(meldingType -> meldingType.getActieType() == actieType)
        .collect(Collectors.toList());
  }

  public static VrsMeldingRedenType getByCode(String code) {
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
