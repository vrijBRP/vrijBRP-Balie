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

package nl.procura.gba.web.services.gba.functies;

import static nl.procura.gba.common.ZaakStatusType.*;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.opslag.PersonenWsOpslag;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonIndicatieOpslagEntry;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonStatusOpslagEntry;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;

/**
 * Geeft een status opsomming van de persoonslijst
 */
public class PersoonslijstStatus {

  /**
   * Zoek de status in de cache of anders in de database
   */
  public static String getStatus(Services services, BasePLExt pl) {
    PersonenWsOpslag opslag = getOpslag(services);
    PersoonStatusOpslagEntry entry = getStoredStatus(services, pl);

    if (entry != null) {
      return entry.getStatus();
    }

    Cat1PersoonExt persoon = pl.getPersoon();
    StringBuilder zakenMsg = new StringBuilder();

    int aantalZaken = getAantalLopendeZaken(services, pl);
    int aantalTmv = getAantalLopendeTmv(services, pl);

    if (aantalZaken > 0) {
      if (aantalZaken == 1) {
        zakenMsg.append("1 lopende zaak");
      } else {

        zakenMsg.append(aantalZaken);
        zakenMsg.append(" lopende zaken");
      }
    }

    if (aantalTmv > 0) {
      zakenMsg.append(", terugmelding");
    }

    zakenMsg.append(",");
    zakenMsg.append(persoon.getStatus().getOpsomming());

    String status = trim(zakenMsg.toString());
    return opslag.set(new PersoonStatusOpslagEntry(pl, status)).getStatus();
  }

  /**
   * Zoek de status in de cache
   */
  public static PersoonStatusOpslagEntry getStoredStatus(Services services, BasePLExt pl) {
    return getOpslag(services).get(new PersoonStatusOpslagEntry(pl));
  }

  public static boolean isGetoond(Services services, BasePLExt pl, String indicatie) {
    return getOpslag(services).get(new PersoonIndicatieOpslagEntry(pl, indicatie)) != null;
  }

  public static void setGetoond(Services services, BasePLExt pl, String indicatie) {
    getOpslag(services).set(new PersoonIndicatieOpslagEntry(pl, indicatie));
  }

  private static int getAantalLopendeTmv(Services services, BasePLExt pl) {
    ZaakService tmv = services.getTerugmeldingService();
    return tmv.getZakenCount(new ZaakArgumenten(pl, INBEHANDELING, OPGENOMEN, INCOMPLEET));
  }

  private static int getAantalLopendeZaken(Services services, BasePLExt pl) {
    ZaakArgumenten z = new ZaakArgumenten(pl, INBEHANDELING, OPGENOMEN, INCOMPLEET, DOCUMENT_ONTVANGEN);
    return services.getZakenService().getAantalZaken(z);
  }

  private static PersonenWsOpslag getOpslag(Services services) {
    return services.getPersonenWsService().getOpslag();
  }
}
