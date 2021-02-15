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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;

public class RecordContainer extends MutationFieldContainer {

  public RecordContainer(ContainerItem<BasePLSet> set) {
    long size = set.getItem().getRecs().stream()
        .filter(r -> !r.isAdmHistory()).count();
    set.getItem().getRecs()
        .stream()
        .filter(r -> !isIgnoreRecord(r))
        .forEach(r -> addRecord(set, r, size));
  }

  private boolean isIgnoreRecord(BasePLRec r) {
    return r.isAdmHistory() || (r.isStatus(HIST) && r.getCatType().is(INSCHR, REISDOC, KIESR));
  }

  private void addRecord(ContainerItem<BasePLSet> set, BasePLRec record, long recordCount) {
    super.addItem(new ContainerItem<>(record, getStatus(set, record, recordCount)));
  }

  private String getStatus(ContainerItem<BasePLSet> set, BasePLRec record, long recordCount) {
    StringBuilder value = new StringBuilder();

    if (record.isCat(OUDER_1, OUDER_2)) {
      String geslachtsnaam = record.getElemVal(GBAElem.GESLACHTSNAAM).getVal();

      if (StringUtils.isEmpty(geslachtsnaam)) {
        value.append("Juridisch geen ouder");
        value.append(" - ");

      } else if (".".equals(geslachtsnaam)) {
        value.append("Ouder onbekend");
        value.append(" - ");
      }
    }

    if (record.isBagChange()) {
      value.append("Dubbel ivm bag wijziging");
      value.append(" - ");
    }

    if (record.isAdmHistory()) {
      value.append("Admin. historie");
      value.append(" - ");

    } else {

      if (record.isConflicting()) {
        value.append("Strijdig");
        value.append(" - ");

      } else if (record.isIncorrect()) {
        value.append("Onjuist");
        value.append(" - ");

      } else if (record.isCat(KINDEREN)
          && "L".equals(record.getElemVal(GBAElem.REG_BETREKK).getVal())) {
        value.append("Levenloos geboren");
        value.append(" - ");
      }

      if (set.isNewSet()) {
        value.append("Nieuw");
      } else {
        value.append(record.getStatus());
      }

      value.append(" - ");
    }

    return String.format("Record %d van %d (%s)", recordCount - getItemIds().size(), recordCount,
        value.toString().trim().replaceAll(" -$", ""));
  }
}
