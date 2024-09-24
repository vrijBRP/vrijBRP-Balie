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

package nl.procura.gba.web.services.beheer.inwonerapp;

import java.util.Arrays;
import java.util.Optional;

import nl.procura.gba.common.ZaakType;

import lombok.Getter;

@Getter
public enum InwonerSignaalType {

  //  SIGN_UITTREKSEL(UITTREKSEL, "Uittreksel");
  //  SIGN_VERSTREKKINGSBEPERKING(VERSTREKKINGSBEPERKING, "Verstrekkingsbeperking"),
  //  SIGN_NAAMGEBRUIK(NAAMGEBRUIK, "Naamgebruik"),
  //  SIGN_VERHUIZING(VERHUIZING, "Verhuizing"),
  //  SIGN_COVOG(ZaakType.COVOG, "COVOG"),
  //  SIGN_GPK(ZaakType.GPK, "GPK"),
  //  SIGN_REISDOCUMENT(ZaakType.REISDOCUMENT, "Reisdocument"),
  //  SIGN_INHOUD_VERMIS(ZaakType.INHOUD_VERMIS, "Inhoud vermist"),
  //  SIGN_RIJBEWIJS(ZaakType.RIJBEWIJS, "Rijbewijs"),
  //  SIGN_TERUGMELDING(ZaakType.TERUGMELDING, "Terugmelding"),
  //  SIGN_GEBOORTE(ZaakType.GEBOORTE, "Geboorte"),
  //  SIGN_ERKENNING(ZaakType.ERKENNING, "Erkenning"),
  SIGN_HUWELIJK_GPS_GEMEENTE(ZaakType.HUWELIJK_GPS_GEMEENTE, "Huwelijk"),
  //  SIGN_NAAMSKEUZE(ZaakType.NAAMSKEUZE, "Naamskeuze"),
  //  SIGN_OVERLIJDEN_IN_GEMEENTE(ZaakType.OVERLIJDEN_IN_GEMEENTE, "Overlijden"),
  //  SIGN_LIJKVINDING(ZaakType.LIJKVINDING, "Lijkvinding"),
  //  SIGN_OVERLIJDEN_IN_BUITENLAND(ZaakType.OVERLIJDEN_IN_BUITENLAND, "Overlijden"),
  //  SIGN_LEVENLOOS(ZaakType.LEVENLOOS, "Levenloos geboren kind"),
  //  SIGN_INDICATIE(ZaakType.INDICATIE, "Indicatie"),
  //  SIGN_GEGEVENSVERSTREKKING(ZaakType.GEGEVENSVERSTREKKING, "Gegevensverstrekking"),
  //  SIGN_OMZETTING_GPS(ZaakType.OMZETTING_GPS, "Omzetting GPS in Huwelijk"),
  //  SIGN_ONTBINDING_GEMEENTE(ZaakType.ONTBINDING_GEMEENTE, "Ontbinding huwelijk/GPS"),
  //  SIGN_ONDERZOEK(ZaakType.ONDERZOEK, "Onderzoek"),
  //  SIGN_REGISTRATION(ZaakType.REGISTRATION, "Eerste inschrijving"),
  //  SIGN_NATURALISATIE(ZaakType.NATURALISATIE, "Naturalisatie"),
  //  SIGN_LV(ZaakType.LV, "Latere vermelding")
  ;

  private final ZaakType zaakType;
  private final String   naam;

  InwonerSignaalType(ZaakType zaakType, String naam) {
    this.zaakType = zaakType;
    this.naam = naam;
  }

  public static Optional<InwonerSignaalType> getByZaakType(ZaakType zaakType) {
    return Arrays.stream(values())
        .filter(type -> type.getZaakType().equals(zaakType))
        .findFirst();
  }
}
