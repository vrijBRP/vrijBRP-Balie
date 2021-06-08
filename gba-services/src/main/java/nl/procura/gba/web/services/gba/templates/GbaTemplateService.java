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

package nl.procura.gba.web.services.gba.templates;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_GBAV_PLUS;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import nl.procura.gba.web.services.AbstractService;
import nl.procura.vaadin.theme.Credentials;

public class GbaTemplateService extends AbstractService {

  public GbaTemplateService(String name) {
    super(name);
  }

  public String getReden(Throwable t) {

    if (t instanceof ConnectException || t instanceof SocketTimeoutException) {
      return "geen verbinding";
    }

    return t.getMessage();
  }

  public boolean heeftGbavPlus() {
    return (ZoekProfielType.is(PROFIEL_GBAV_PLUS, getParm(ZOEK_PROFIEL)));
  }

  protected Credentials getCredentials(ZoekProfielType type) {
    return new Credentials(getUsername(type), getPassword(type));
  }

  protected String getPassword(ZoekProfielType type) {
    if (ZoekProfielType.PROFIEL_GBAV_PLUS == type && heeftGbavPlus()) {
      return getParm(ZOEK_PERSONEN_PROFIEL2_PW);
    }

    return getParm(ZOEK_PLE_JAVA_SERVER_PW);
  }

  protected String getURL() {
    return getSysteemParm(ZOEK_PLE_JAVA_SERVER_URL, false);
  }

  protected String getUsername(ZoekProfielType type) {
    if (ZoekProfielType.PROFIEL_GBAV_PLUS == type && heeftGbavPlus()) {
      return getParm(ZOEK_PERSONEN_PROFIEL2_USERNAME);
    }

    return getParm(ZOEK_PLE_JAVA_SERVER_USERNAME);
  }
}
