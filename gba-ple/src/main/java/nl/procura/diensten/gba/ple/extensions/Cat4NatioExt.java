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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BIJZ_NL_SCHAP;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_EINDE_NATIO;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.UnknownGBAElementException;

public class Cat4NatioExt extends BasePLCatExt {

  private static final int STAATLOOS = 499;
  private static final int NEDERLAND = 1;

  public Cat4NatioExt(BasePLExt ext) {
    super(ext);
  }

  public BasePLElem getNationaliteit() {

    List<BasePLElem> natios = new ArrayList<>();

    int[] ok_natios = { 1, 499, 52, 308, 54, 55, 45, 56, 57, 59, 61, 62, 64, 44, 46, 67, 68, 71, 72, 73, 42, 27, 77, 28,
        60, 83, 80, 66, 70, 63, 81 };

    for (BasePLSet set : getExt().getCat(GBACat.NATIO).getSets()) {
      BasePLRec natioRecord = set.getLatestRec();
      BasePLElem element = natioRecord.getElem(GBAElem.NATIONALITEIT);
      String byznl = natioRecord.getElemVal(AAND_BIJZ_NL_SCHAP).getVal();
      String redenVerlies = natioRecord.getElemVal(REDEN_EINDE_NATIO).getVal();
      boolean byzNl = byznl.equalsIgnoreCase("B");

      if (emp(redenVerlies)) {
        if (byzNl) { // Bijzonder nederlanderschap
          BasePLElem byzElement = new BasePLElem();
          byzElement.setCatCode(GBACat.NATIO.getCode());
          byzElement.setElemCode(GBAElem.NATIONALITEIT.getCode());
          byzElement.getValue().setCode("0001");
          byzElement.getValue().setVal("0001");
          byzElement.getValue().setDescr("Nederlandse");
          natios.add(byzElement);
        } else {
          natios.add(element);
        }
      }
    }

    // De waarde die als eerst komt.
    for (int i : ok_natios) {
      for (BasePLElem e : natios) {
        if (aval(e.getValue().getVal()) == i) {
          return e;
        }
      }
    }

    if (natios.size() > 0) {
      return natios.iterator().next();
    }

    // Onbekende nationaliteit

    BasePLElem onbekend = new BasePLElem();
    onbekend.setCatCode(GBACat.NATIO.getCode());
    onbekend.setElemCode(GBAElem.NATIONALITEIT.getCode());
    onbekend.getValue().setCode("0000");
    onbekend.getValue().setVal("0000");
    onbekend.getValue().setDescr("Onbekend");

    return onbekend;
  }

  public boolean isStaatloos() {
    try {
      for (BasePLSet set : getExt().getCat(GBACat.NATIO).getSets()) {
        int natioCode = aval(set.getLatestRec().getElemVal(GBAElem.NATIONALITEIT).getVal());
        if (natioCode == STAATLOOS) {
          return true;
        }
      }
    } catch (UnknownGBAElementException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean isNietNederlander() {
    return !isNederlander() && !isBehandeldAlsNederlander();
  }

  public boolean isNederlander() {
    return getExt().getCat(GBACat.NATIO)
        .getSets()
        .stream()
        .mapToInt(set -> aval(set.getLatestRec().getElemVal(GBAElem.NATIONALITEIT).getVal()))
        .anyMatch(code -> code == NEDERLAND);
  }

  public boolean isBehandeldAlsNederlander() {
    return getExt().getCat(GBACat.NATIO)
        .getSets()
        .stream()
        .map(set -> set.getLatestRec().getElemVal(AAND_BIJZ_NL_SCHAP).getVal())
        .anyMatch(val -> val.equalsIgnoreCase("B"));
  }

  public String getNationaliteiten() {
    StringBuilder natio = new StringBuilder();
    for (BasePLSet set : getExt().getCat(GBACat.NATIO).getSets()) {
      natio.append(set.getLatestRec().getElemVal(GBAElem.NATIONALITEIT).getDescr());
      natio.append(", ");
    }

    return trim(natio.toString());
  }

  public boolean isBestendig() {
    for (BasePLSet v : getExt().getCat(GBACat.VBTITEL).getSets()) {
      int vbt = aval(v.getLatestRec().getElemVal(GBAElem.AAND_VBT).getVal());
      if (Arrays.asList(21, 22, 23, 24, 25, 26, 27, 35).contains(vbt)) {
        return true;
      }
    }

    for (BasePLSet v : getExt().getCat(GBACat.NATIO).getSets()) {
      int natioCode = aval(v.getLatestRec().getElemVal(GBAElem.NATIONALITEIT).getVal());
      String bijzNL = astr(v.getLatestRec().getElemVal(AAND_BIJZ_NL_SCHAP).getVal());

      if (eq(bijzNL, "B") || Arrays.asList(1, 52, 308, 54, 55, 45, 56, 57, 59, 61,
          62, 64, 44, 46, 67, 68, 71, 72, 73, 42, 27, 77,
          28, 60, 83, 80, 66, 70, 63, 81).contains(natioCode)) {
        return true;
      }
    }

    return false;
  }
}
