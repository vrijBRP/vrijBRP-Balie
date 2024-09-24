/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.bs.lv;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_1;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_10;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_11;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_12;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_13;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_14;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_15;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_16;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_17;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_18;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_19;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_2;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_20;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_3;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_4;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_5;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_6;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_7;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_8;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.LATERE_VERMELDING_AFST_9;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.Arrays;

import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

import lombok.Getter;

@Getter
public enum LvType implements EnumWithCode<Integer> {

  ADOPTIE(1, "Adoptie", "Q",
      LATERE_VERMELDING_AFST_1),
  AMBTSHALVE_VERBETERING_AKTE(20, "Ambtshalve verbetering akte", "",
      LATERE_VERMELDING_AFST_2),
  DOORHALING_AKTE(30, "Doorhaling akte", "K",
      LATERE_VERMELDING_AFST_3),
  GERECHTELIJKE_VASTSTELLING_OUDERSCHAP(40, "Gerechtelijke vaststelling ouderschap", "V",
      LATERE_VERMELDING_AFST_4),
  HERROEPING_ADOPTIE(50, "Herroeping adoptie", "R",
      LATERE_VERMELDING_AFST_5),
  ONTKENNING_OUDERSCHAP(60, "Ontkenning ouderschap", "E",
      LATERE_VERMELDING_AFST_6),
  VASTSTELLING_NAMEN_BIJ_KB(70, "Vaststelling namen bij KB", "G",
      LATERE_VERMELDING_AFST_7),
  VASTSTELLING_NAMEN_BIJ_OPTIE(80, "Vaststelling namen bij optie", "",
      LATERE_VERMELDING_AFST_8),
  VERBETERING_AKTE(90, "Verbetering akte", "K",
      LATERE_VERMELDING_AFST_9),
  VERNIETIGING_ERKENNING(100, "Vernietiging erkenning", "N",
      LATERE_VERMELDING_AFST_10),
  WETTIGING(110, "Wettiging", "",
      LATERE_VERMELDING_AFST_11),
  WIJZIGING_GESLACHT(120, "Wijziging geslacht", "S",
      LATERE_VERMELDING_AFST_12),
  WIJZIGING_GESLACHTSNAAM_BIJ_KB(130, "Wijziging geslachtsnaam bij KB", "H",
      LATERE_VERMELDING_AFST_13),
  WIJZIGING_GESLACHTSNAAM_DOOR_RECHTERLIJKE_UITSPRAAK(140, "Wijziging geslachtsnaam door rechterlijke uitspraak",
      "X",
      LATERE_VERMELDING_AFST_14),
  WIJZIGING_GESLACHTSNAAM_TGV_HUWELIJK_GPS(150, "Wijziging geslachtsnaam t.g.v. huwelijk/GPS", "",
      LATERE_VERMELDING_AFST_15),
  WIJZIGING_GESLACHTSNAAM_TGV_ECHTSCHEIDING(160, "Wijziging geslachtsnaam t.g.v. echtscheiding", "",
      LATERE_VERMELDING_AFST_16),
  WIJZIGING_GESLACHTSNAAM_ERK_OUDER(170, "Wijziging geslachtsnaam door erkenning ouder", "",
      LATERE_VERMELDING_AFST_17),
  WIJZIGING_VOORNAMEN(180, "Wijziging voornamen", "M",
      LATERE_VERMELDING_AFST_18),
  NAAMSKEUZE(190, "Naamskeuze", "W",
      LATERE_VERMELDING_AFST_19),
  ERKENNING(200, "Erkenning", "C",
      LATERE_VERMELDING_AFST_20),
  ONBEKEND(-1, "Onbekend", "", null);

  private final Integer      code;
  private final String       oms;
  private final String       akteCode;
  private final DocumentType documentType;

  LvType(Integer code, String oms, String akteCode, DocumentType documentType) {
    this.code = code;
    this.oms = oms;
    this.akteCode = akteCode;
    this.documentType = documentType;
  }

  public boolean is(LvType... types) {
    return types != null && Arrays.stream(types).anyMatch(type -> type == this);
  }

  public static LvType get(Integer code) {
    return Arrays.stream(values())
        .filter(value -> value.getCode().equals(code))
        .findFirst().orElse(ONBEKEND);
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String toString() {
    return oms + (fil(akteCode) ? " (" + akteCode + ")" : " (-)");
  }
}
