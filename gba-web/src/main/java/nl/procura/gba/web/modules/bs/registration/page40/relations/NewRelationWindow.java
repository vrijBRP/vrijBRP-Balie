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
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.PageNavigation;

public class NewRelationWindow extends GbaModalWindow {

  private final Dossier              dossier;
  private final Consumer<Relation>   saveRelation;
  private RelationDetailsPageFactory relationDetailsPageFactory;
  private PageNavigation             navigation;

  public NewRelationWindow(Dossier dossier, Consumer<Relation> saveRelation) {
    super("Relatie tussen personen toevoegen", "800px");
    this.dossier = dossier;
    this.saveRelation = saveRelation;
  }

  @Override
  public void attach() {
    super.attach();
    relationDetailsPageFactory = new RelationDetailsPageFactory(
        getGbaApplication().getServices().getPersonenWsService());
    MainModuleContainer mainModuleContainer = new MainModuleContainer(false,
        new RelationPage(dossier.getPersonen(), this::showFirstDetailPage));
    navigation = mainModuleContainer.getNavigation();
    addComponent(mainModuleContainer);
  }

  private void showFirstDetailPage(Relation relation) {
    AbstractRelationPage page = relationDetailsPageFactory.create(relation);
    page.withSaveListener(r -> {
      Relation reverseRelation = page.getReverseRelation();
      if (reverseRelation != null) {
        saveAndShowReverseRelation(r, reverseRelation);
      } else {
        saveAndClose(Optional.of(relation));
      }
    });
    navigation.addPage(page);
  }

  /**
   * Show second page of the relation which shows the relation the other way around.
   */
  private void saveAndShowReverseRelation(Optional<Relation> firstRelation, Relation reverseRelation) {
    firstRelation.ifPresent(saveRelation);
    AbstractRelationPage page = relationDetailsPageFactory.create(reverseRelation);
    page.withSaveListener(this::saveAndClose);
    navigation.addPage(page);
  }

  private void saveAndClose(Optional<Relation> relation) {
    relation.ifPresent(saveRelation);
    closeWindow();
  }
}
