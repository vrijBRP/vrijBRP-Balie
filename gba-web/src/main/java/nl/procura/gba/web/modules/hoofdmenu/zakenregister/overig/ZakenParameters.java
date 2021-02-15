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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.application.GbaApplication;

public class ZakenParameters {

  public static final String PERIODE_INVOER = "zaken.periode.invoer";
  public static final String ZAAK           = "zaken.id";
  public static final String BSN            = "zaken.bsn";

  private final GbaApplication application;

  public ZakenParameters(GbaApplication application) {
    this.application = application;
  }

  public void storeParameters() {
    setParameter(PERIODE_INVOER);
    setParameter(ZAAK);
    setParameter(BSN);
  }

  private void setParameter(String id) {
    application.getServices().getMemoryService().setObject(id, astr(application.getParameterMap().get(id)));
  }
}
