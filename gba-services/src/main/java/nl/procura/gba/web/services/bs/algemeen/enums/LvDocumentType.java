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

package nl.procura.gba.web.services.bs.algemeen.enums;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum LvDocumentType {

  AANG_WIJZ_GESL("aangifteformulier van wijziging van het geslacht"),
  AANG_WIJZ_GESL_VN("aangifteformulier van wijziging van het geslacht en voornamen"),
  AKTE_ERK("akte van erkenning"),
  AKTE_NAAMSKEUZE("akte van naamskeuze"),
  AKTE_PARTNER("akte van partnerschapsregistratie"),
  BEV_VERK_NL("bevestiging verkrijging Nederlanderschap door optie"),
  HUWELIJKSAKTE("huwelijksakte"),
  HUWELIJKSREGISTRATIE("huwelijksregistratie"),
  KONINKLIJK_BESLUIT("koninklijk besluit"),
  VERKL_RECHTSKEUZE("verklaring rechtskeuze"),
  ANDERS("anders, nl"),
  ONBEKEND("Onbekend");

  private final String oms;

  LvDocumentType(String oms) {
    this.oms = oms;
  }

  public static LvDocumentType get(String oms) {
    return Arrays.stream(values())
        .filter(locatie -> equalsIgnoreCase(locatie.getOms(), oms))
        .findFirst()
        .orElse(ONBEKEND);
  }

  @Override
  public String toString() {
    return getOms();
  }
}
