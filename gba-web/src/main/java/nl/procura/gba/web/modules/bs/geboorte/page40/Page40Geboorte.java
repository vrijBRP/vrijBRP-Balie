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

package nl.procura.gba.web.modules.bs.geboorte.page40;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.VADER_DUO_MOEDER;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonPage;
import nl.procura.gba.web.modules.bs.geboorte.ModuleGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.checks.BirthFatherBsnCheckWindow;
import nl.procura.gba.web.modules.bs.geboorte.checks.DeclarationCheckButton;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

/**
 * Moeder
 */

public class Page40Geboorte<T extends DossierGeboorte> extends BsPersoonPage<T> {

  public Page40Geboorte() {
    this("Geboorte - vader / duo-moeder");
  }

  public Page40Geboorte(String title) {
    super(title);
  }

  private DeclarationCheckButton declarationButton;

  @Override
  public void addButtons() {

    getOptieLayout().getRight().addButton(buttonReset, this);
    getOptieLayout().getRight().addButton(buttonGba, this);
    getOptieLayout().getRight().addComponent(new Ruler());
    getOptieLayout().getRight().addButton(buttonNat, this);
    getOptieLayout().getRight().addButton(buttonZoek, this);

    declarationButton = new DeclarationCheckButton();
    getOptieLayout().getRight().addComponent(declarationButton);
    checkDeclaration();
  }

  @Override
  public void update() {
    super.update();
    checkDeclaration();
  }

  private void checkDeclaration() {
    declarationButton.setPopupState(() -> getGeboorteModule().getVerzoekPopupStates());
    declarationButton.check(new BirthFatherBsnCheckWindow(
        getServices(),
        getZaakDossier(),
        getDossierPersoon()),
        getZaakDossier().isVerzoekInd());
  }

  private ModuleGeboorte getGeboorteModule() {
    return VaadinUtils.getParent(Page40Geboorte.this, ModuleGeboorte.class);
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      getApplication().getServices().getGeboorteService().save(getDossier());
      return true;
    }
    return false;
  }

  @Override
  public void event(PageEvent event) {
    try {
      if (event.isEvent(InitPage.class)) {
        DossierPersoon vader = getZaakDossier().getVader();

        try {
          if (!vader.isVolledig()) {
            GezinssituatieType gezin = getZaakDossier().getGezinssituatie();
            if (gezin != null) {
              if (gezin.is(BINNEN_HETERO_HUWELIJK)) {
                BasePLRec partnerRecord = getPartnerRecord(getZaakDossier().getMoeder());
                BasePLExt partner = getPartner(partnerRecord);

                if (partnerRecord != null) {
                  if (partner != null) {
                    BsPersoonUtils.kopieDossierPersoon(partner, vader);
                  } else {
                    BsPersoonUtils.kopieDossierPersoon(partnerRecord, vader);
                  }
                }
              } else if (gezin.is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK)) {
                if (getZaakDossier().getVragen().heeftErkenningVoorGeboorte()) {
                  DossierErkenning erkenning = getZaakDossier().getErkenningVoorGeboorte();
                  BsPersoonUtils.kopieDossierPersoonFromDatabase(erkenning.getErkenner(), vader);
                }
              }
            }
          }
        } catch (Exception e) {
          throw new ProException(SELECT, WARNING, "De persoonslijst kan niet worden geladen", e);
        } finally {
          setDossierPersoon(vader);
          getDossierPersoon().setDossierPersoonType(VADER_DUO_MOEDER);
        }
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
