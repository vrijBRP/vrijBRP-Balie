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

package nl.procura.gba.web.services.beheer.bsm;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.bsm.taken.BsmTaak;
import nl.procura.bsm.taken.GbaBsmTaak;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.commons.core.exceptions.ProException;

public class BsmZaakTypes {

  public static BsmTaak getBsmTaak(Zaak zaak) {

    String zaakOms = zaak.getType().getOms().toLowerCase();

    switch (zaak.getType()) {

      case VERHUIZING:

        VerhuisAanvraag verhuizing = (VerhuisAanvraag) zaak;

        switch (verhuizing.getTypeVerhuizing()) {

          case BINNENGEMEENTELIJK:
            return GbaBsmTaak.PROBEV_ZAAK_BINNENVERHUIZING_1_0;

          case EMIGRATIE:
            return GbaBsmTaak.PROBEV_ZAAK_EMIGRATIE_1_0;

          case HERVESTIGING:
            return GbaBsmTaak.PROBEV_ZAAK_HERVESTIGING_1_0;

          case INTERGEMEENTELIJK:
            return GbaBsmTaak.PROBEV_ZAAK_BUITENVERHUIZING_1_0;

          default:
            throw new ProException(ERROR,
                "Type verhuizing is niet bekend: " + verhuizing.getTypeVerhuizing());
        }

      case VERSTREKKINGSBEPERKING:
        return GbaBsmTaak.PROBEV_ZAAK_GEHEIMHOUDING_1_0;

      case INDICATIE:
        return GbaBsmTaak.PROBEV_ZAAK_INDICATIE_1_0;

      case NAAMGEBRUIK:
        return GbaBsmTaak.PROBEV_ZAAK_NAAMGEBRUIK_1_0;

      case OVERLIJDEN_IN_GEMEENTE:
        return GbaBsmTaak.PROBEV_ZAAK_OVERLIJDEN_GEMEENTE_1_0;

      case LIJKVINDING:
        return GbaBsmTaak.PROBEV_ZAAK_LIJKVINDING_1_0;

      case LEVENLOOS:
        return GbaBsmTaak.PROBEV_ZAAK_LEVENLOOS_1_0;

      case GEBOORTE:
        return GbaBsmTaak.PROBEV_ZAAK_GEBOORTE_1_0;

      case RISK_ANALYSIS:
        return GbaBsmTaak.PERSONEN_ZAAK_RISK_ANALYSIS_1_0;

      case REGISTRATION:
        return GbaBsmTaak.PROBEV_ZAAK_FIRST_REG_1_0;

      case ERKENNING:
        return GbaBsmTaak.PROBEV_ZAAK_ERKENNING_1_0;

      case HUWELIJK_GPS_GEMEENTE:
        return GbaBsmTaak.PROBEV_ZAAK_HUW_GPS_1_0;

      case OMZETTING_GPS:
        return GbaBsmTaak.PROBEV_ZAAK_OMZET_GPS_1_0;

      case ONTBINDING_GEMEENTE:
        return GbaBsmTaak.PROBEV_ZAAK_ONTB_1_0;

      case INBOX:
        return GbaBsmTaak.PERSONEN_ZAAK_INBOX_1_0;

      case INHOUD_VERMIS:
      case REISDOCUMENT:
        return GbaBsmTaak.PROBEV_ZAAK_REISDOCUMENT_1_0;

      case PL_MUTATION:
        return GbaBsmTaak.PROBEV_ZAAK_PERSON_MUTATIONS_1_0;

      case NAAMSKEUZE:
        return GbaBsmTaak.PROBEV_ZAAK_NAAMSKEUZE_1_0;

      case CORRESPONDENTIE:
      case COVOG:
      case GEGEVENSVERSTREKKING:
      case GPK:
      case RIJBEWIJS:
      case TERUGMELDING:
      case UITTREKSEL:
      case OVERLIJDEN_IN_BUITENLAND:
        throw new ProException(ERROR, "Deze zaak ({0}) hoeft niet te worden verwerkt.", zaakOms);

      default:
        break;
    }

    throw new ProException(ERROR,
        "Het is niet duidelijk welke BSM taak aangeroepen moet worden voor deze zaak ({0})",
        zaakOms);
  }
}
