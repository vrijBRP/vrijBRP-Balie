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

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import static nl.procura.gba.web.services.bs.registration.RelationType.*;

import java.text.MessageFormat;
import java.util.stream.Stream;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class RelationTypeContainer extends GbaContainer {

  public RelationTypeContainer(DossierPersoon person, RelationService relationService) {
    Stream.of(CHILD_OF, PARTNER_OF, PARENT_OF, ONLY_1_LEGAL_PARENT, NO_LEGAL_PARENTS)
        .filter(type -> type.getCode().longValue() > 0)
        .forEach(type -> {
          int count = relationService.getRelations(person, type).size();
          if (count > 0) {
            if (type.isRelated()) {
              addItem(
                  new FieldValue(type, MessageFormat.format("{0} ({1} relatie{2} toegevoegd)",
                      type.getOms(), count, count > 1 ? "s" : "")));
            } else {
              addItem(new FieldValue(type, type.getOms() + " (toegevoegd)"));
            }
          } else {
            RelationType relationType = getConflictingRelationType(relationService, person, type);
            if (relationType == null) {
              addItem(new FieldValue(type, type.getOms()));
            } else {
              addItem(new FieldValue(type, MessageFormat.format("{0} (NIET MOGELIJK. Relatie ''{1}'' al toegevoegd)",
                  type.getOms(), relationType.getOms().toLowerCase())));
            }
          }
        });
  }

  private RelationType getConflictingRelationType(RelationService relationService, DossierPersoon person,
      RelationType type) {
    if (!type.isRelated()) {
      DossPersRelation otherRelation = relationService.getInOtherRelation(person, type, person);
      if (otherRelation != null) {
        RelationType relationType = valueOfCode(otherRelation.getRelationShipType());
        if (relationType.is(ONLY_1_LEGAL_PARENT, NO_LEGAL_PARENTS)) {
          return relationType;
        }
      }
    }
    return null;
  }
}
