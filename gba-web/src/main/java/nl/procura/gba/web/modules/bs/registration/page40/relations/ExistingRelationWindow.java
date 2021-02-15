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

import java.util.Optional;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ExistingRelationWindow extends GbaModalWindow {

  private final Relation           relation;
  private final Consumer<Relation> saveRelation;

  public ExistingRelationWindow(Relation relation, Consumer<Relation> saveRelation) {
    super("Relatie bewerken", "900px");
    this.relation = relation;
    this.saveRelation = saveRelation;
  }

  @Override
  public void attach() {
    super.attach();
    RelationDetailsPageFactory relationDetailsPageFactory = new RelationDetailsPageFactory(
        getGbaApplication().getServices().getPersonenWsService());
    addComponent(new MainModuleContainer(false,
        relationDetailsPageFactory.create(relation)
            .withSaveListener(this::saveAndClose)));
  }

  private void saveAndClose(Optional<Relation> relation) {
    relation.ifPresent(saveRelation);
    closeWindow();
  }
}
