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

package nl.procura.gba.web.modules.zaken.personmutations.page2.containers;

import static java.text.MessageFormat.format;

import java.text.MessageFormat;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;

public class SetContainer extends MutationFieldContainer {

  public SetContainer(GBACat category, BasePLCat soort) {

    // List all sets
    int setNr = 0;
    for (BasePLSet set : soort.getSets()) {
      addSet(set, format("Set {0} {1}", ++setNr, getLabel(soort, set)), false);
    }

    // Add a new set
    if (soort.getSets().isEmpty() || category.isMultiSet()) {
      int maxExtIndex = getMaxExtIndex(soort) + 1;
      int maxIntIndex = getMaxIntIndex(soort) + 1;
      addSet(getEmptySet(category, maxIntIndex),
          MessageFormat.format("Set {0} (Nieuw)", ++setNr), true);
    }
  }

  private String getLabel(BasePLCat soort, BasePLSet set) {
    String label = "";
    switch (soort.getCatType()) {
      case NATIO:
        label = getNationality(set);
        break;
      case HUW_GPS:
      case KINDEREN:
        label = getName(set);
        break;
      case REISDOC:
        label = getTravelDocumentLabel(set);
      default:
        break;
    }
    return label;
  }

  private String getNationality(BasePLSet set) {
    return "(" + getValue(set, GBAElem.NATIONALITEIT) + ")";
  }

  private String getName(BasePLSet set) {
    return "(" + getValue(set, GBAElem.VOORNAMEN) + ")";
  }

  private String getTravelDocumentLabel(BasePLSet set) {
    String type = getValue(set, GBAElem.SOORT_NL_REISDOC);
    String nr = getValue(set, GBAElem.NR_NL_REISDOC);
    return "(" + type + " - " + nr + ")";
  }

  private String getValue(BasePLSet set, GBAElem voornamen) {
    return set.getLatestRec().getElemVal(voornamen).getDescr();
  }

  private int getMaxExtIndex(BasePLCat soort) {
    return soort.getSets().stream()
        .mapToInt(BasePLSet::getExtIndex)
        .max().orElse(0);
  }

  private int getMaxIntIndex(BasePLCat soort) {
    return soort.getSets().stream()
        .mapToInt(BasePLSet::getIntIndex)
        .max().orElse(0);
  }

  private void addSet(BasePLSet set, String label, boolean newSet) {
    ContainerItem<BasePLSet> item = new ContainerItem<>(set, label);
    item.setNewSet(newSet);
    super.addItem(item);
  }

  private BasePLSet getEmptySet(GBACat category, int volgCode) {
    BasePLSet set = new BasePLSet(category, volgCode);
    BasePLRec record = new BasePLRec(category, set, GBARecStatus.MUTATION);
    record.setIndex(1);
    for (GBAGroupElements.GBAGroupElem type : GBAGroupElements.getByCat(category.getCode())) {
      BasePLElem gbaElement = new BasePLElem(type.getCat().getCode(), type.getElem().getCode(), "");
      record.getElems().add(gbaElement);
    }
    set.getRecs().add(record);
    return set;
  }
}
