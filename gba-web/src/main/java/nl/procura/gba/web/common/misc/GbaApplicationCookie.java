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

package nl.procura.gba.web.common.misc;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.Properties;

import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.vaadin.component.panel.ApplicationCookie;
import nl.procura.vaadin.theme.ProcuraApplication;

public class GbaApplicationCookie extends ApplicationCookie {

  private static final String LOCATIE = "locatie";

  public GbaApplicationCookie(ProcuraApplication a) {
    super(a);
  }

  public long getLocation() {
    return along(load().getProperty(LOCATIE));
  }

  public void setDefaultLocation(Locatie locatie) {

    Properties properties = load();
    properties.put(LOCATIE, astr(locatie.getCLocation()));

    store(properties);
  }
}
