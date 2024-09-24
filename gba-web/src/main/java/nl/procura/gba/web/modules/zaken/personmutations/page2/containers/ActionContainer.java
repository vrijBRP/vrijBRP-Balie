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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.AKTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.DOCUMENT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.DOCUMENTINDICATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.EMIGRATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.GESLACHT;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.IMMIGRATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.NAAMGEBRUIK;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.CURRENT;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.MUTATION;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CATEGORY;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_GENERAL;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_GENERAL;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.DELETE_MUT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.REMOVE_BLOCK;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_DELETE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_DELETE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.UPDATE_SET;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.SELECT_PL_SUPERUSER_MUTATIONS;

import java.util.EnumSet;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;

public class ActionContainer extends MutationFieldContainer {

  public ActionContainer(
      GbaApplication application,
      BasePLExt pl,
      ContainerItem<GBACat> cat,
      ContainerItem<BasePLSet> set,
      ContainerItem<BasePLRec> record) {

    List<BasePLSet> sets = pl.getCat(cat.getItem()).getSets();
    int size = sets.size();
    boolean isCurrent = record.getItem().isStatus(CURRENT);

    if (set.isNewSet()) {
      if (cat.getItem().isMultiSet()) {
        addOperationType(ADD_SET, "Actualiseren (nieuwe gegevensset: " + (size + 1) + "e)");

      } else {
        addOperationType(ADD_SET);
      }

    } else {
      if (record.getItem().getStatus().is(MUTATION)) {
        // Add mutation actions
        addOperationType(DELETE_MUT);

      } else {

        // Add other misc. actions
        if (record.getItem().getStatus().is(CURRENT)) {

          // Remove block
          if (cat.getItem().is(INSCHR)
              && record.getItem().getSet().getLatestRec()
                  .getElemVal(GBAElem.DATUM_INGANG_BLOK_PL)
                  .isNotBlank()) {
            addOperationType(REMOVE_BLOCK);
          }

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

        // Overwrite actions        
        if (isOverwriteCategory(cat.getItem())) {
          addOperationType(isCurrent ? OVERWRITE_CURRENT : OVERWRITE_HISTORIC);
        }

        // Superuser only
        if (application.isProfielActie(SELECT_PL_SUPERUSER_MUTATIONS)) {
          addOperationType(isCurrent ? SUPER_OVERWRITE_CURRENT : SUPER_OVERWRITE_HISTORIC);

          // Cannot delete current cat 07 because it has no format history
          if (!cat.getItem().is(INSCHR)) {
            if (record.getItem().isStatus(GBARecStatus.CURRENT)) {
              // Cannot delete current cat 1,2,3,8 if no hist
              if (!(cat.getItem().is(PERSOON, OUDER_1, OUDER_2, VB)
                  && set.getItem().getOfficialHistRecs().isEmpty())) {
                addOperationType(SUPER_DELETE_CURRENT);
              }
            }

            // Cat 7,12,13 have no formal hist
            if (!cat.getItem().is(REISDOC, KIESR)
                && record.getItem().isStatus(GBARecStatus.HIST)) {
              addOperationType(SUPER_DELETE_HISTORIC);
            }
          }
        }
      }
    }
  }

  private boolean isOverwriteCategory(GBACat cat) {
    EnumSet<GBAGroup> groups = EnumSet.of(NAAM, GESLACHT, EMIGRATIE, IMMIGRATIE,
        NAAMGEBRUIK, DOCUMENTINDICATIE, AKTE, DOCUMENT, ONJUIST);
    return GBAGroupElements.getByCat(cat.getCode())
        .stream()
        .anyMatch(groupElem -> groups.contains(groupElem.getGroup()));
  }

  private void addOperationType(PersonListActionType type) {
    addOperationType(type, type.getDescription());
  }

  private void addOperationType(PersonListActionType type, String label) {
    addItem(new ContainerItem<>(type, label));
  }

  public Object getFirstItem() {
    return getAllItemIds().stream().findFirst().orElse(null);
  }
}
