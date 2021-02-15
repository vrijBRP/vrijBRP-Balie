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

package nl.procura.gba.web.modules.bs.erkenning.page1;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsContactpersoonPage;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**

 * <p>
 * 3 nov. 2011 15:00:17
 */

public class Page1Erkenning extends BsContactpersoonPage<DossierErkenning> {

  public Page1Erkenning() {
    super("Erkenning - moeder uit wie het kind wordt / is geboren");
  }

  @Override
  public void addButtons() {

    super.addButtons();

    getOptieLayout().getRight().addComponent(new Ruler());
    getOptieLayout().getRight().addButton(buttonIden, this);
    getOptieLayout().getRight().addButton(buttonContact, this);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      if (getDossierPersoon().getGeslacht() != Geslacht.VROUW) {
        throw new ProException(WARNING, "De moeder dient een vrouw te zijn.");
      }

      getServices().getErkenningService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonPrev.setEnabled(false);

      setDossierPersoon(getZaakDossier().getMoeder());

      getDossierPersoon().setDossierPersoonType(MOEDER);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }
}
