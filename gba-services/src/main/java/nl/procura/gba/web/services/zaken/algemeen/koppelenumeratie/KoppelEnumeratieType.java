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

package nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie;

import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieSoortType.*;

import java.util.ArrayList;
import java.util.List;

public enum KoppelEnumeratieType {

  // Grondslagen
  ONBEKEND(0, "", null, false),
  // Grondslag
  GS_NVT(100, "Niet van toepassing", GRONDSLAG, true),
  GS_OVERHEID(101, "Overheidsorgaan, art. 3.5", GRONDSLAG, false),
  GS_LANDELIJK(102, "(Landelijk aangewezen) derde, art. 3.6", GRONDSLAG, false),
  GS_GEMEENTELIJK(103, "(Gemeentelijk aangewezen) derde, art. 3.9", GRONDSLAG, false),
  // ProcesActies
  PA_NVT(200, "Niet van toepassing", PROCESACTIE, true),
  PA_KENBAAR_MAKEN(201, "Verzoek kenbaar maken belang", PROCESACTIE, false),
  PA_NIET_VERSTREKKEN(202, "Niet verstrekken", PROCESACTIE, false),
  PA_NA_TERMIJN_VERSTREKKEN(203, "Verstrekken na termijn", PROCESACTIE, false),
  PA_NU_VERSTREKKEN(204, "Nu verstrekken", PROCESACTIE, false),
  // Reacties
  REACTIE_NVT(300, "Niet van toepassing", REACTIE, true),
  REACTIE_GEEN(301, "Geen reactie", REACTIE, false),
  REACTIE_WEL_BEZWAAR(302, "Wel bezwaar", REACTIE, false),
  REACTIE_GEEN_BEZWAAR(303, "Geen bezwaar", REACTIE, false),
  // Toekenning
  TK_NVT(500, "Niet van toepassing", TOEKENNING, true),
  TK_JA(501, "Ja", TOEKENNING, false),
  TK_NEE(502, "Nee", TOEKENNING, false),
  TK_JA_VOORWAARDELIJK(503, "Misschien, na belangenafweging", TOEKENNING, false),
  // Verstrekkingsbeperking
  VP_NVT(600, "Niet van toepassing", VERSTREK_BEP, true),
  VP_JA(601, "Ja", VERSTREK_BEP, false),
  VP_NEE(602, "Nee", VERSTREK_BEP, false),
  // Eigen gemeente
  BG_NVT(700, "Niet van toepassing", BINNENGEM, true),
  BG_JA(701, "Ja", BINNENGEM, false),
  BG_NEE(702, "Nee", BINNENGEM, false);

  private long                      code = 0;
  private String                    oms  = "";
  private boolean                   nvt  = false;
  private KoppelEnumeratieSoortType soortType;

  KoppelEnumeratieType(long code, String oms, KoppelEnumeratieSoortType soortType, boolean nvt) {

    setCode(code);
    setOms(oms);
    setSoortType(soortType);
    setNvt(nvt);
  }

  public static List<KoppelEnumeratieType> get(KoppelEnumeratieSoortType soortType) {

    List<KoppelEnumeratieType> types = new ArrayList<>();

    for (KoppelEnumeratieType type : values()) {
      if (type.getSoortType() == soortType) {
        types.add(type);
      }
    }

    return types;
  }

  public static KoppelEnumeratieType get(long code) {
    for (KoppelEnumeratieType var : values()) {
      if (var.getCode() == code) {
        return var;
      }
    }

    return ONBEKEND;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public KoppelEnumeratieSoortType getSoortType() {
    return soortType;
  }

  public void setSoortType(KoppelEnumeratieSoortType soortType) {
    this.soortType = soortType;
  }

  public boolean is(KoppelEnumeratieType... types) {
    for (KoppelEnumeratieType type : types) {
      KoppelEnumeratieType eType = type != null ? type : ONBEKEND;
      if (eType.getCode() == code) {
        return true;
      }
    }

    return false;
  }

  public boolean isNvt() {
    return nvt;
  }

  public void setNvt(boolean nvt) {
    this.nvt = nvt;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
