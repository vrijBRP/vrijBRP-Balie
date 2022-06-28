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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.standard.exceptions.ProException;

import lombok.Getter;

public class KassaParameters {

  @Getter
  private final String        gebruikerId;
  @Getter
  private final String        locatie;
  @Getter
  private final String        kassaId;
  @Getter
  private final String        ftpPassword;
  @Getter
  private final String        ftpUrl;
  @Getter
  private final String        ftpUsername;
  @Getter
  private final KassaSendType type;

  private final Services services;
  private final String   kassaLocatieId;
  private final String   fileName;

  public KassaParameters(Services services) {
    this.services = services;
    locatie = services.getGebruiker().getLocatie().getLocatie();
    gebruikerId = astr(services.getGebruiker().getCUsr());
    kassaLocatieId = services.getGebruiker().getLocatie().getGkasId();
    kassaId = getParm(KASSA_ID, false);
    type = KassaSendType.get(getParm(KASSA_SEND_TYPE, true));
    ftpUrl = getParm(KASSA_FTP_URL, false);
    ftpUsername = getParm(KASSA_FTP_USERNAME, false);
    ftpPassword = getParm(KASSA_FTP_PW, false);
    fileName = getParm(KASSA_FILENAME, false);
  }

  private String getParm(ParameterConstant parameter, boolean required) {
    return services.getParameterService().getParm(parameter, required);
  }

  public String getFilename() {
    if (emp(fileName)) {
      throw new ProException(CONFIG, WARNING, "Geen uitvoerbestand voor de kassakoppeling gedefinieërd.");
    }

    Map<String, String> ids = new HashMap<>();
    ids.put("${g-kas}", "\\$\\{g-kas\\}");
    ids.put("${kassa-id}", "\\$\\{kassa-id\\}");

    String name = fileName;
    for (Entry<String, String> entry : ids.entrySet()) {
      name = name.replaceAll(entry.getValue(), getKassaLocatieId());
    }
    return name;
  }

  public String getKassaLocatieId() {
    if (emp(kassaLocatieId)) {
      throw new ProException(CONFIG, WARNING,
          String.format("De configuratie voor de kassakoppeling is nog niet compleet. <hr/>" +
              "<p>Bij deze locatie (%s) is de kassa-locatie-identificatie niet opgenomen</p>",
              locatie));
    }
    return kassaLocatieId;
  }

  public ParameterService getParameterService() {
    return services.getParameterService();
  }
}
