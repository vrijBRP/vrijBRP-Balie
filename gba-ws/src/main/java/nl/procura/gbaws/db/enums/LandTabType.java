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

package nl.procura.gbaws.db.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;

public enum LandTabType {

  ONBEKEND(GBATable.ONBEKEND),
  NATIONALITEIT(GBATable.NATIO),
  PLAATS(GBATable.PLAATS),
  LAND(GBATable.LAND),
  VOORVOEGSEL(GBATable.VOORVOEGSEL),
  RDN_VERKR_NED(GBATable.REDEN_NATIO),
  TITEL_PRED(GBATable.TITEL_PREDIKAAT),
  AKTE_AAND(GBATable.AKTE_AANDUIDING),
  RDN_HUW_ONTB(GBATable.REDEN_HUW_ONTB),
  NED_REISD(GBATable.NED_REISDOC),
  AUT_VERSTR_NED_REISD(GBATable.AUT_VERSTREK_NED_REISD),
  VERBLIJFSTITEL(GBATable.VERBLIJFSTITEL),
  GBA_DEELNEMER(GBATable.GBA_DEELNEMER),
  RNI_DEELNEMER(GBATable.RNI_DEELNEMER),
  GEZAG(GBATable.IND_GEZAG_MINDERJ);

  private final GBATable gbaTabel;

  LandTabType(GBATable gbaTabel) {
    this.gbaTabel = gbaTabel;
  }

  public static LandTabType get(int code) {
    for (LandTabType so : values()) {
      if (so.getCode() == code) {
        return so;
      }
    }

    return ONBEKEND;
  }

  public static LandTabType getByDescription(String description) {
    return Arrays.stream(values())
        .filter(so -> so.getDescr().equalsIgnoreCase(description))
        .findFirst().orElse(ONBEKEND);

  }

  public List<GBAElem> getElements() {
    List<GBAElem> elements = new ArrayList<>();
    for (GBAGroupElements.GBAGroupElem element : GBAGroupElements.getAll()) {
      if (gbaTabel.equals(element.getElem().getTable())) {
        if (!elements.contains(element.getElem())) {
          elements.add(element.getElem());
        }
      }
    }

    return elements;
  }

  public int getCode() {
    return gbaTabel.getTableCode();
  }

  public String getDescr() {
    return gbaTabel.getDescr();
  }

  @Override
  public String toString() {
    return getDescr();
  }

  public GBATable getGbaTabel() {
    return gbaTabel;
  }
}
