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
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.*;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.*;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;

public class ActionContainer extends MutationFieldContainer {

  public ActionContainer(
      GbaApplication application,
      BasePLExt pl,
      ContainerItem<GBACat> cat,
      ContainerItem<BasePLSet> set,
      ContainerItem<BasePLRec> record) {

    List<BasePLSet> sets = pl.getCat(cat.getItem()).getSets();
    int size = sets.size();

    if (set.isNewSet()) {
      if (cat.getItem().isMultiSet()) {
        addOperationType(ADD_SET, "Actualiseren (nieuwe gegevensset: " + (size + 1) + "e)");

      } else {
        addOperationType(ADD_SET);
      }

    } else {
      if (record.getItem().isIncorrect()) {
        addOperationType(NO_ACTION_INCORRECT_HIST);

      } else if (record.getItem().getStatus().is(MUTATION)) {
        // Add mutation actions
        addOperationType(DELETE_MUT);

      } else {

        // Add other misc. actions
        if (record.getItem().getStatus().is(CURRENT)) {

          // Category already exists
          addOperationType(UPDATE_SET);

          // Cat 7 has no history
          if (!cat.getItem().is(INSCHR, REISDOC, KIESR)) {
            addOperationType(ADD_HISTORIC);
            addOperationType(CORRECT_CURRENT_GENERAL);
            addOperationType(CORRECT_CURRENT_ADMIN);
          }

        } else if (record.getItem().getStatus().is(HIST)
            && !cat.getItem().is(INSCHR, REISDOC, KIESR)) {
          addOperationType(ADD_HISTORIC);
          addOperationType(CORRECT_HISTORIC_GENERAL);
          addOperationType(CORRECT_HISTORIC_ADMIN);
        }

        if (!cat.getItem().isRequired()
            && !cat.getItem().is(REISDOC, KIESR)) {
          addOperationType(CORRECT_CATEGORY);
        }

        boolean profielActie = application.isProfielActie(ProfielActie.SELECT_PL_SUPERUSER_MUTATIONS);
        if (profielActie) {
          addSuperUserActions(record);
        }
      }
    }
  }

  @SuppressWarnings("unused")
  private void addSuperUserActions(ContainerItem<BasePLRec> record) {
    addOperationType(SUPER_CHANGE);

    if (record.getItem().isStatus(GBARecStatus.CURRENT)) {
      addOperationType(SUPER_DEL_ACT);
    }

    if (record.getItem().isStatus(GBARecStatus.HIST)) {
      addOperationType(SUPER_DEL_HIST);
    }

    addOperationType(SUPER_DEL_CAT);
  }

  private void addOperationType(PersonListActionType type) {
    addOperationType(type, type.getDescription());
  }

  private void addOperationType(PersonListActionType type, String label) {
    addItem(new ContainerItem<>(type, label));
  }
}
