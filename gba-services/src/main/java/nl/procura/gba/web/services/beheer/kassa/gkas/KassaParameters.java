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

package nl.procura.gba.web.services.beheer.kassa.gkas;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.procura.gba.web.services.beheer.kassa.KassaVerstuurType;
import nl.procura.standard.exceptions.ProException;

public class KassaParameters {

  private String            locatie        = "";
  private String            kassaId        = "";
  private String            filename       = "";
  private String            ftpPassword    = "";
  private String            ftpUrl         = "";
  private String            ftpUsername    = "";
  private String            kassaLocatieId = "";
  private KassaVerstuurType type           = KassaVerstuurType.LOKAAL;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFtpPassword() {
    return ftpPassword;
  }

  public void setFtpPassword(String ftp_password) {
    this.ftpPassword = ftp_password;
  }

  public String getFtpUrl() {
    return ftpUrl;
  }

  public void setFtpUrl(String ftpUrl) {
    this.ftpUrl = ftpUrl;
  }

  public String getFtpUsername() {
    return ftpUsername;
  }

  public void setFtpUsername(String ftpUsername) {
    this.ftpUsername = ftpUsername;
  }

  public String getKassaId() {
    return kassaId;
  }

  public void setKassaId(String kassaId) {
    this.kassaId = kassaId;
  }

  public String getKassaLocatieId() {
    return kassaLocatieId;
  }

  public void setKassaLocatieId(String kassaLocatieId) {
    this.kassaLocatieId = kassaLocatieId;
  }

  public String getLocatie() {
    return locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getParameterFileName() {

    String filename = getFilename();

    Map<String, String> ids = new HashMap<>();
    ids.put("${g-kas}", "\\$\\{g-kas\\}");
    ids.put("${kassa-id}", "\\$\\{kassa-id\\}");

    for (Entry<String, String> entry : ids.entrySet()) {

      if (filename.contains(entry.getKey()) && emp(getKassaLocatieId())) {

        throw new ProException(CONFIG, WARNING,
            "De configuratie voor de kassakoppeling is nog niet compleet. <hr/><p>" + "Bij deze locatie (" + locatie
                + ") is de Kassa-locatie-identificatie niet opgenomen</p>");
      }

      filename = filename.replaceAll(entry.getValue(), getKassaLocatieId());
    }

    return filename;
  }

  public KassaVerstuurType getType() {
    return type;
  }

  public void setType(KassaVerstuurType type) {
    this.type = type;
  }
}
