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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_ERK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_GEB;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_LV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_NK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_CORRES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_COVOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_GPK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_GV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_HUW_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INBOX;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INDICATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INH_VERMIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INH_VERMIS_RYB;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_NAAMGEBRUIK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_NATURALISATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OMZET_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_ONDERZOEK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_ONTBINDING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_GEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_LEVENLOOS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_LIJKV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_PL_MUTATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REGISTRATION_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REGISTRATION_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REISDOC;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_RISK_ANALYSIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_TMV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_UITTREKSEL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BINNEN_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BINNEN_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BUITEN_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BUITEN_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_EMIGR_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_EMIGR_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_HERV_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_HERV_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERSTREK;
import static nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres.BRIEFADRES;
import static nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres.WOONADRES;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;

public enum ZaakStatusParameterType {

  GEBOORTE(ZAKEN_INIT_STATUS_AFSTAM_GEB, ZaakType.GEBOORTE),
  ERKENNING(ZAKEN_INIT_STATUS_AFSTAM_ERK, ZaakType.ERKENNING),
  NAAMSKEUZE(ZAKEN_INIT_STATUS_AFSTAM_NK, ZaakType.NAAMSKEUZE),
  LV(ZAKEN_INIT_STATUS_AFSTAM_LV, ZaakType.LV),
  CORRESPONDENTIE(ZAKEN_INIT_STATUS_CORRES, ZaakType.CORRESPONDENTIE),
  COVOG(ZAKEN_INIT_STATUS_COVOG, ZaakType.COVOG),
  VERSTREKKINGSBEPERKING(ZAKEN_INIT_STATUS_VERSTREK, ZaakType.VERSTREKKINGSBEPERKING),
  GPK(ZAKEN_INIT_STATUS_GPK, ZaakType.GPK),
  INBOX(ZAKEN_INIT_STATUS_INBOX, ZaakType.INBOX),
  INDICATIE(ZAKEN_INIT_STATUS_INDICATIE, ZaakType.INDICATIE),
  HUWELIJK_GPS_GEMEENTE(ZAKEN_INIT_STATUS_HUW_GPS, ZaakType.HUWELIJK_GPS_GEMEENTE),
  OMZETTING_GPS(ZAKEN_INIT_STATUS_OMZET_GPS, ZaakType.OMZETTING_GPS),
  ONDERZOEK(ZAKEN_INIT_STATUS_ONDERZOEK, ZaakType.ONDERZOEK),
  RISK_ANALYSIS(ZAKEN_INIT_STATUS_RISK_ANALYSIS, ZaakType.RISK_ANALYSIS),
  ONTBINDING_GEMEENTE(ZAKEN_INIT_STATUS_ONTBINDING, ZaakType.ONTBINDING_GEMEENTE),
  INHOUD_VERMIS(ZAKEN_INIT_STATUS_INH_VERMIS, ZaakType.INHOUD_VERMIS),
  INHOUD_VERMIS_RYB(ZAKEN_INIT_STATUS_INH_VERMIS_RYB, ZaakType.INHOUD_VERMIS, true),
  OVERLIJDEN_IN_GEMEENTE(ZAKEN_INIT_STATUS_OVERL_GEM, ZaakType.OVERLIJDEN_IN_GEMEENTE),
  LIJKVINDING(ZAKEN_INIT_STATUS_OVERL_LIJKV, ZaakType.LIJKVINDING),
  LEVENLOOS(ZAKEN_INIT_STATUS_OVERL_LEVENLOOS, ZaakType.LEVENLOOS),
  GEGEVENSVERSTREKKING(ZAKEN_INIT_STATUS_GV, ZaakType.GEGEVENSVERSTREKKING),
  REISDOCUMENT(ZAKEN_INIT_STATUS_REISDOC, ZaakType.REISDOCUMENT),
  RIJBEWIJS(ZAKEN_INIT_STATUS_RIJBEWIJS, ZaakType.RIJBEWIJS),
  TERUGMELDING(ZAKEN_INIT_STATUS_TMV, ZaakType.TERUGMELDING),
  UITTREKSEL(ZAKEN_INIT_STATUS_UITTREKSEL, ZaakType.UITTREKSEL),
  VERH_EMIGRATIE_WA(ZAKEN_INIT_STATUS_VERH_EMIGR_WA, ZaakType.VERHUIZING, WOONADRES, VerhuisType.EMIGRATIE),
  VERH_EMIGRATIE_BA(ZAKEN_INIT_STATUS_VERH_EMIGR_BA, ZaakType.VERHUIZING, BRIEFADRES, VerhuisType.EMIGRATIE),
  VERH_BINNEN_WA(ZAKEN_INIT_STATUS_VERH_BINNEN_WA, ZaakType.VERHUIZING, WOONADRES, VerhuisType.BINNENGEMEENTELIJK),
  VERH_BINNEN_BA(ZAKEN_INIT_STATUS_VERH_BINNEN_BA, ZaakType.VERHUIZING, BRIEFADRES, VerhuisType.BINNENGEMEENTELIJK),
  VERH_BUITEN_WA(ZAKEN_INIT_STATUS_VERH_BUITEN_WA, ZaakType.VERHUIZING, WOONADRES, VerhuisType.INTERGEMEENTELIJK),
  VERH_BUITEN_BA(ZAKEN_INIT_STATUS_VERH_BUITEN_BA, ZaakType.VERHUIZING, BRIEFADRES, VerhuisType.INTERGEMEENTELIJK),
  VERH_HERV_WA(ZAKEN_INIT_STATUS_VERH_HERV_WA, ZaakType.VERHUIZING, WOONADRES, VerhuisType.HERVESTIGING),
  VERH_HERV_BA(ZAKEN_INIT_STATUS_VERH_HERV_BA, ZaakType.VERHUIZING, BRIEFADRES, VerhuisType.HERVESTIGING),
  REGISTRATION_WA(ZAKEN_INIT_STATUS_REGISTRATION_WA, ZaakType.REGISTRATION, WOONADRES, null),
  REGISTRATION_BA(ZAKEN_INIT_STATUS_REGISTRATION_BA, ZaakType.REGISTRATION, BRIEFADRES, null),
  NAAMGEBRUIK(ZAKEN_INIT_STATUS_NAAMGEBRUIK, ZaakType.NAAMGEBRUIK),
  NATURALISATIE(ZAKEN_INIT_STATUS_NATURALISATIE, ZaakType.NATURALISATIE),
  PL_MUTATIE(ZAKEN_INIT_STATUS_PL_MUTATIE, ZaakType.PL_MUTATION);

  private final ParameterType parameterType;
  private final ZaakType      zaakType;

  private FunctieAdres functieAdres       = FunctieAdres.ONBEKEND;
  private VerhuisType  verhuisType;
  private boolean      inhoudingRijbewijs = false;

  ZaakStatusParameterType(ParameterType parameterType, ZaakType zaakType) {
    this.parameterType = parameterType;
    this.zaakType = zaakType;
  }

  ZaakStatusParameterType(ParameterType parameterType,
      ZaakType zaakType,
      FunctieAdres functieAdres,
      VerhuisType verhuisType) {
    this(parameterType, zaakType);
    this.functieAdres = functieAdres;
    this.verhuisType = verhuisType;
  }

  ZaakStatusParameterType(ParameterType parameterType, ZaakType zaakType,
      boolean inhoudingRijbewijs) {
    this(parameterType, zaakType);
    this.inhoudingRijbewijs = inhoudingRijbewijs;
  }

  public static List<ZaakStatusParameterType> getByZaakType(ZaakType zaakType) {
    return Arrays.stream(values()).filter(type -> type.getZaakType() == zaakType).collect(Collectors.toList());
  }

  public static List<ZaakStatusParameterType> getByParameterType(ParameterType parameterType) {
    return Arrays.stream(values()).filter(type -> type.getParameterType() == parameterType)
        .collect(Collectors.toList());
  }

  public ParameterType getParameterType() {
    return parameterType;
  }

  public ZaakType getZaakType() {
    return zaakType;
  }

  public FunctieAdres getFunctieAdres() {
    return functieAdres;
  }

  public boolean isInhoudingRijbewijs() {
    return inhoudingRijbewijs;
  }

  public VerhuisType getVerhuisType() {
    return verhuisType;
  }
}
