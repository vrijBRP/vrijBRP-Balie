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

package nl.procura.gba.web.modules.bs.common.pages.vereiste;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereisten;
import nl.procura.commons.core.exceptions.NotSupportedException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsVereistePage<T extends ZaakDossier> extends BsPage<T> {

  private final OptieLayout optieLayout    = new OptieLayout();
  private final Button      buttonCuratele = new Button("Curateleregister (F3)");
  private final Button      buttonInfo     = new Button("Info (F6)");

  private final Set<BurgerlijkeStandVereiste> vereisten = new HashSet<>();

  public BsVereistePage(String title) {
    super(title);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    DossierVereisten dossier = getDossier();
    dossier.getVereisten().clear();

    for (BurgerlijkeStandVereiste bs : vereisten) {
      dossier.addVereiste(bs.getId(), bs.getDossierVereiste(), bs.getNamen(), bs.getOverruleReason(),
          bs.getVoldoet(), bs.isOverruled());
    }

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        if (getDossier().getType().is(ZaakType.HUWELIJK_GPS_GEMEENTE)) {
          setInfo("Controleer of aan alle vereisten wordt voldaan. "
              + "Met de optie Curateleregister (F3) kan dit register worden bevraagd. Druk op Volgende (F2) om verder te gaan.");
        } else {
          setInfo("Controleer of aan alle vereisten wordt voldaan. Druk op Volgende (F2) om verder te gaan.");
        }

        addOptieComponenten();
        addVereisten();
        updateVereisten();

        if (getDossier().getType().is(ZaakType.HUWELIJK_GPS_GEMEENTE)) {
          optieLayout.getRight().addButton(buttonCuratele, this);
          optieLayout.getRight().addButton(buttonInfo, this);
        }

        addOptieButtons();

        optieLayout.getRight().setWidth("150px");
        optieLayout.getRight().setCaption("Opties");

        addComponent(optieLayout);
      }
    } finally {
      super.event(event);
    }
  }

  public OptieLayout getOptieLayout() {
    return optieLayout;
  }

  public Set<BurgerlijkeStandVereiste> getVereisten() {
    return vereisten;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonCuratele || keyCode == KeyCode.F3) {
      getParentWindow().addWindow(getCuraleWindow());
    } else if (button == buttonInfo) {
      getParentWindow().addWindow(getInfoWindow());
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  protected void addOptieButtons() {
  }

  protected void addOptieComponenten() {
  }

  protected void addOptieComponenten(BsVereisteTable c) {
    addOptieComponenten(c, c.getDossierVereiste());
  }

  protected void addOptieComponenten(Component c, String h) {

    if (fil(h)) {
      optieLayout.getLeft().addComponent(new Fieldset(h));
    }

    optieLayout.getLeft().addComponent(c);
  }

  protected void addVereisten() {
  }

  protected Window getCuraleWindow() {
    throw new NotSupportedException();
  }

  protected Window getInfoWindow() {
    throw new NotSupportedException();
  }

  protected BasePLExt getPl(DossierPersoon p) {
    return getPersoonslijst(p.getBurgerServiceNummer());
  }

  protected BurgerlijkeStandVereiste getVereiste(String id) {
    for (BurgerlijkeStandVereiste v : vereisten) {
      if (v.getId().equals(id)) {
        return v;
      }
    }

    return new BurgerlijkeStandVereiste(id, null, null);
  }

  protected void updateVereisten() {
  }
}
