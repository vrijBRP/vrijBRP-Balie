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

package nl.procura.gba.web.modules.bs.common.layouts.namenrecht;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.HashSet;
import java.util.Optional;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.GbaFieldsetLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.broerzuspage.BsBroerZusWindow;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenPage.NaamsKeuze;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenWindow;
import nl.procura.gba.web.modules.bs.geboorte.ModuleGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.checks.DeclarationCheckButton;
import nl.procura.gba.web.modules.bs.geboorte.checks.NameSelectionCheckWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrechtVerzoek;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorteVerzoek;
import nl.procura.vaadin.functies.VaadinUtils;

public class BsNamenrechtLayout extends GbaFieldsetLayout implements ClickListener {

  private final Button buttonNamen = new Button("Naamselectie");
  private final Button buttonBroerZus = new Button("Broer / zus");

  private final Services          services;
  private final DossierNamenrecht dossier;
  private final BsNamenrechtForm  form;
  private DeclarationCheckButton  declarationButton;

  public BsNamenrechtLayout(Services services, DossierNamenrecht dossier) {
    super("Conclusie namenrecht");
    setSpacing(true);

    this.services = services;
    this.dossier = dossier;

    buttonNamen.setDescription("Toon de namen van de ouders.");
    buttonBroerZus.setDescription("Toon de broers / zussen van het kind");

    form = new BsNamenrechtForm(services, dossier);
    OptieLayout namenrechtOptieLayout = new OptieLayout(form, "150px");
    namenrechtOptieLayout.getRight().addButton(buttonNamen, this);
    namenrechtOptieLayout.getRight().addButton(buttonBroerZus, this);

    if (dossier instanceof DossierGeboorteVerzoek) {
      declarationButton = new DeclarationCheckButton();
      namenrechtOptieLayout.getRight().addComponent(declarationButton);
      form.setUpdateListener(() -> checkVerzoek(false));
    }

    addComponent(namenrechtOptieLayout);
    form.initBean();
    checkVerzoek(true);
  }

  public void checkVerzoek(boolean showPopup) {
    if (declarationButton != null) {
      declarationButton.setPopupState(
          () -> getGeboorteModule().map(ModuleGeboorte::getVerzoekPopupStates)
              .orElse(new HashSet<>()));

      DossierNamenrechtVerzoek dossierNamenrechtVerzoek = (DossierNamenrechtVerzoek) this.dossier;
      declarationButton.check(new NameSelectionCheckWindow(
          dossierNamenrechtVerzoek,
          form.getGeslachtsnaam(),
          form.getVoorvoegsel(),
          form.getTitel()),
          dossierNamenrechtVerzoek.isVerzoekInd() && showPopup);
    }
  }

  private Optional<ModuleGeboorte> getGeboorteModule() {
    return Optional.ofNullable(VaadinUtils.getParent(BsNamenrechtLayout.this, ModuleGeboorte.class));
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonNamen) {
      openNamenWindow();
    }

    if (event.getButton() == buttonBroerZus) {
      openBroerZusWindow();
    }
  }

  public void checkRecht() {
    form.checkRecht();
  }

  public BsNamenrechtForm getForm() {
    return form;
  }

  public void resetNaamsPersoonType() {
    form.setDefaultNaamskeuzePersoon(NaamsPersoonType.ONBEKEND);
  }

  @Override
  public void setToelichting(String oms) {
    if (!getForm().isReadOnlyAllFields()) {
      super.setToelichting(oms);
    }
  }

  private void openBroerZusWindow() {

    BasePLExt moeder = services.getPersonenWsService().getPersoonslijst(
        astr(dossier.getMoeder().getBurgerServiceNummer().getValue()));

    getParentWindow().addWindow(new BsBroerZusWindow(moeder));
  }

  private void openNamenWindow() {
    getParentWindow().addWindow(new BsNamenWindow(dossier) {

      @Override
      public void setNaam(NaamsKeuze naamsKeuze) {
        form.setNewNaamskeuzePersoon(naamsKeuze.getType());
        form.setNaam(naamsKeuze.getGeslachtsnaam(), naamsKeuze.getVoorvoegsel(), naamsKeuze.getTitel());
      }
    });
  }
}
