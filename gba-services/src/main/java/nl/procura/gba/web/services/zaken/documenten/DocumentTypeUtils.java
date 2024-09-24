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

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.gba.common.ZaakType;
import nl.procura.commons.core.exceptions.ProException;

public class DocumentTypeUtils {

  /**
   * Bepaal het zaaktype op basis van het documentType
   */
  public static ZaakType getZaakType(DocumentType documentType) {

    switch (documentType) {

      case CORRESPONDENTIE_ZAAK:
        return ZaakType.CORRESPONDENTIE;

      case COVOG_AANVRAAG:
        return ZaakType.COVOG;

      case ERKENNING:
        return ZaakType.ERKENNING;

      case NAAMSKEUZE:
        return ZaakType.NAAMSKEUZE;

      case GEBOORTE:
        return ZaakType.GEBOORTE;

      case ONDERZOEK:
        return ZaakType.ONDERZOEK;

      case RISK_ANALYSE:
        return ZaakType.RISK_ANALYSIS;

      case REGISTRATION:
        return ZaakType.REGISTRATION;

      case GPK_AANVRAAG:
        return ZaakType.GPK;

      case GV_AANVRAAG:
        return ZaakType.GEGEVENSVERSTREKKING;

      case HUWELIJK:
        return ZaakType.HUWELIJK_GPS_GEMEENTE;

      case GPS_OMZETTING:
        return ZaakType.OMZETTING_GPS;

      case ONTBINDING_GEMEENTE:
        return ZaakType.ONTBINDING_GEMEENTE;

      case INDICATIE_AANVRAAG:
        return ZaakType.INDICATIE;

      case LEVENLOOS:
        return ZaakType.LEVENLOOS;

      case LIJKVINDING:
        return ZaakType.LIJKVINDING;

      case NAAMGEBRUIK_AANVRAAG:
        return ZaakType.NAAMGEBRUIK;

      case OVERLIJDEN:
        return ZaakType.OVERLIJDEN_IN_GEMEENTE;

      case PL_UITTREKSEL:
      case PL_BEGELEIDENDE_BRIEF:
      case PL_BEWONER_BRIEF:
      case PL_FORMULIER:
      case PL_NATURALISATIE:
      case PL_OPTIE:
      case PL_ADRESONDERZOEK:
        return ZaakType.UITTREKSEL;

      case REISDOCUMENT_AANVRAAG:
      case REISDOCUMENT_C1:
        return ZaakType.REISDOCUMENT;

      case DOCUMENT_VERMISSING:
        return ZaakType.INHOUD_VERMIS;

      case RIJBEWIJS_AANVRAAG:
        return ZaakType.RIJBEWIJS;

      case TMV:
        return ZaakType.TERUGMELDING;

      case VERHUIZING_AANGIFTE:
        return ZaakType.VERHUIZING;

      case VERSTREKKINGSBEPERKING_AANVRAAG:
        return ZaakType.VERSTREKKINGSBEPERKING;

      case ZAAK:
        return ZaakType.ONBEKEND;

      case TELLING:
      case LATERE_VERMELDING_ERK:
      case LATERE_VERMELDING_NK:
      case KLAPPER:
      case ONBEKEND:
      default:
        throw new ProException(ERROR, "Geen zaak op basis van documenttype: " + documentType);
    }
  }
}
