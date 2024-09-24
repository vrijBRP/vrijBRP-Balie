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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_BRONNEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_LEVERANCIERS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_ZAAKTYPES;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.FOUT_BIJ_VERWERKING;

import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.Services;

public class ZaakArgumentenBuilder {

  public static ZaakArgumenten favorieteZaken(Services services) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.setCodeGebruikerFavoriet(services.getGebruiker().getCUsr());
    return zaakArgumenten;
  }

  public static ZaakArgumenten mijnZaken(Services services) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.getNegeerStatussen().addAll(ZaakStatusType.getMetEindStatus());
    zaakArgumenten.setCodeBehandelaar(services.getGebruiker().getCUsr());
    return zaakArgumenten;
  }

  public static ZaakArgumenten probleemZaken() {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.addAttributen(FOUT_BIJ_VERWERKING.getCode());
    zaakArgumenten.getNegeerStatussen().addAll(ZaakStatusType.getMetEindStatus());
    return zaakArgumenten;
  }

  public static ZaakArgumenten nieuweZaken(Services services) {
    ZakenService zakenService = services.getZakenService();
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.getNegeerStatussen().addAll(ZaakStatusType.getMetEindStatus());
    zaakArgumenten.setZonderBehandelaar(true);
    zaakArgumenten.getTypen().addAll(ZaakType.getByCodes(zakenService.getParm(ZAKEN_NIEUW_ZAAKTYPES)));
    parmList(zakenService.getParm(ZAKEN_NIEUW_BRONNEN), val -> zaakArgumenten.getBronnen().add(val));
    parmList(zakenService.getParm(ZAKEN_NIEUW_LEVERANCIERS), val -> zaakArgumenten.getLeveranciers().add(val));
    return zaakArgumenten;
  }

  private static void parmList(String value, Consumer<String> consumer) {
    Arrays.stream(StringUtils.split(value, ","))
        .filter(StringUtils::isNotBlank)
        .forEach(consumer);
  }
}
