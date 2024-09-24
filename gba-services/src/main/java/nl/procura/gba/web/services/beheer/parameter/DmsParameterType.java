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

package nl.procura.gba.web.services.beheer.parameter;

import java.util.Arrays;
import java.util.Optional;

import nl.procura.gba.common.ZaakType;

import lombok.Getter;

@Getter
public enum DmsParameterType {

  GEBOORTE(ParameterConstant.ZAKEN_DMS_AFSTAM_GEB, ZaakType.GEBOORTE),
  ERKENNING(ParameterConstant.ZAKEN_DMS_AFSTAM_ERK, ZaakType.ERKENNING),
  NAAMSKEUZE(ParameterConstant.ZAKEN_DMS_AFSTAM_NK, ZaakType.NAAMSKEUZE),
  LV(ParameterConstant.ZAKEN_DMS_AFSTAM_LV, ZaakType.LV),
  COVOG(ParameterConstant.ZAKEN_DMS_COVOG, ZaakType.COVOG),
  VERSTREKKINGSBEPERKING(ParameterConstant.ZAKEN_DMS_VERSTREK, ZaakType.VERSTREKKINGSBEPERKING),
  HUWELIJK_GPS_GEMEENTE(ParameterConstant.ZAKEN_DMS_HUW_GPS, ZaakType.HUWELIJK_GPS_GEMEENTE),
  OMZET_GPS(ParameterConstant.ZAKEN_DMS_OMZET_GPS, ZaakType.OMZETTING_GPS),
  ONDERZOEK(ParameterConstant.ZAKEN_DMS_ONDERZOEK, ZaakType.ONDERZOEK),
  GEGEVENSVERSTREKKING(ParameterConstant.ZAKEN_DMS_GV, ZaakType.GEGEVENSVERSTREKKING),
  RISK_ANALYSIS(ParameterConstant.ZAKEN_DMS_RISK_ANALYSIS, ZaakType.RISK_ANALYSIS),
  REGISTRATION(ParameterConstant.ZAKEN_DMS_REGISTRATION, ZaakType.REGISTRATION),
  ONTBINDING_GEMEENTE(ParameterConstant.ZAKEN_DMS_ONTB_HUW_GPS, ZaakType.ONTBINDING_GEMEENTE),
  INHOUD_VERMIS(ParameterConstant.ZAKEN_DMS_INH_VERMIS, ZaakType.INHOUD_VERMIS),
  OVERLIJDEN_IN_GEMEENTE(ParameterConstant.ZAKEN_DMS_OVERL_GEM, ZaakType.OVERLIJDEN_IN_GEMEENTE),
  LIJKVINDING(ParameterConstant.ZAKEN_DMS_OVERL_LIJKV, ZaakType.LIJKVINDING),
  LEVENLOOS(ParameterConstant.ZAKEN_DMS_OVERL_LEVENLOOS, ZaakType.LEVENLOOS),
  REISDOCUMENT(ParameterConstant.ZAKEN_DMS_REISDOC, ZaakType.REISDOCUMENT),
  RIJBEWIJS(ParameterConstant.ZAKEN_DMS_RIJBEWIJS, ZaakType.RIJBEWIJS),
  UITTREKSEL(ParameterConstant.ZAKEN_DMS_UITTREKSEL, ZaakType.UITTREKSEL),
  VERHUIZING(ParameterConstant.ZAKEN_DMS_VERHUIZING, ZaakType.VERHUIZING),
  NAAMGEBRUIK(ParameterConstant.ZAKEN_DMS_NAAMGEBRUIK, ZaakType.NAAMGEBRUIK),
  NATURALISATIE(ParameterConstant.ZAKEN_DMS_NATURALISATIE, ZaakType.NATURALISATIE);

  private final ParameterType parameterType;
  private final ZaakType      zaakType;

  DmsParameterType(ParameterType parameterType, ZaakType zaakType) {
    this.parameterType = parameterType;
    this.zaakType = zaakType;
  }

  public static Optional<DmsParameterType> getByZaakType(ZaakType zaakType) {
    return Arrays.stream(values()).filter(type -> type.getZaakType() == zaakType).findFirst();
  }
}
