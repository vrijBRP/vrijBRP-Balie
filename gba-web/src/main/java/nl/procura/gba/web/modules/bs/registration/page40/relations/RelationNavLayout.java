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

import java.util.Arrays;
import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.label.H3;
import nl.procura.vaadin.component.layout.HLayout;

public class RelationNavLayout extends HLayout {

  private AbstractRelationPage page;

  public RelationNavLayout(AbstractRelationPage page, Relation currRelation) {
    this.page = page;

    setWidth("100%");
    setHeight("40px");

    boolean stored = currRelation.getRelation().isStored();
    DossPersRelation revRelation = getReversedRelation(currRelation);
    boolean isReversed = isReversed(currRelation, revRelation);

    String relationDesc = currRelation.getRelationType().getOms();
    StringBuilder infoText = new StringBuilder();

    if (currRelation.getRelationType().isRelated()) {
      infoText.append(isReversed ? "Scherm 2 / 2" : "Scherm 1 / 2");
    } else {
      infoText.append("Scherm 1 / 1");
    }

    infoText.append(": De '");
    infoText.append(relationDesc.toLowerCase());
    infoText.append("' relatie");
    H3 info = new H3(infoText.toString());

    add(info);
    setExpandRatio(info, 1f);
    setComponentAlignment(info, Alignment.MIDDLE_LEFT);

    if (stored && currRelation.getRelationType().isRelated()) {
      Button prevB = new Button("");
      prevB.setDescription("Ga naar scherm 1");
      prevB.addListener((Button.ClickListener) event -> goToRelation(currRelation, revRelation));

      Button nextB = new Button("");
      nextB.setDescription("Ga naar scherm 2");
      nextB.addListener((Button.ClickListener) event -> goToRelation(currRelation, revRelation));

      prevB.setEnabled(isReversed);
      nextB.setEnabled(!isReversed);
      prevB.setWidth("46px");
      prevB.setIcon(new ThemeResource("../gba-web/buttons/img/back.png"));
      nextB.setWidth("46px");
      nextB.setIcon(new ThemeResource("../gba-web/buttons/img/forward.png"));

      add(prevB);
      add(nextB);
      setComponentAlignment(prevB, Alignment.MIDDLE_RIGHT);
      setComponentAlignment(nextB, Alignment.MIDDLE_RIGHT);
    }
  }

  /**
   * Is this the reversed relation
   */
  private boolean isReversed(Relation currRelation, DossPersRelation revRelation) {
    if (revRelation == null) {
      return false;
    }
    if (!currRelation.getRelation().isStored()) {
      return true;
    }
    long codeCurrentRelation = currRelation.getRelation().getCDossRelative().longValue();
    long codeReversedRelation = revRelation.getCDossRelative().longValue();
    return revRelation != null && codeReversedRelation < codeCurrentRelation;
  }

  /**
   * Find the 'reversed' relatie
   */
  private DossPersRelation getReversedRelation(Relation currRelation) {
    return Services.getInstance().getRelationService()
        .getInOtherRelation(currRelation.getRelatedPerson(),
            RelationType.UNKNOWN, currRelation.getPerson());
  }

  private void goToRelation(Relation currRelation, DossPersRelation relation) {
    if (relation != null) {
      PersonenWsService wsService = page.getServices().getPersonenWsService();
      RelationDetailsPageFactory factory = new RelationDetailsPageFactory(wsService);
      List<DossierPersoon> people = Arrays.asList(currRelation.getPerson(), currRelation.getRelatedPerson());
      AbstractRelationPage newPage = factory.create(Relation.fromDossPersRelation(relation, people));

      page.getNavigation().removeOtherPages();
      page.getNavigation().addPage(newPage);
    } else {
      throw new ProException(ProExceptionSeverity.INFO, "Geen relatie gevonden.");
    }
  }
}
