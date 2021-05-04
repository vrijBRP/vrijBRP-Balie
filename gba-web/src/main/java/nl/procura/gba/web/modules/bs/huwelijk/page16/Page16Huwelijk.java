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

package nl.procura.gba.web.modules.bs.huwelijk.page16;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getOntbrekendePersonenMelding;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER1;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER2;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdeLayout;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdePersoon;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page16Huwelijk extends BsPage<DossierHuwelijk> {

  private RelatiesLayout relatiesLayout1 = null;
  private RelatiesLayout relatiesLayout2 = null;

  public Page16Huwelijk() {
    super("Huwelijk/GPS - gerelateerden");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    getProcessen().updateStatus();

    getApplication().getServices().getHuwelijkService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {

        setInfo("De ouders, partners en de minderjarige kinderen van de partners.");

        relatiesLayout1 = new RelatiesLayout(getZaakDossier().getPartner1());
        relatiesLayout1.setMargin(true, false, false, false);
        relatiesLayout2 = new RelatiesLayout(getZaakDossier().getPartner2());
        relatiesLayout2.setMargin(true, false, false, false);

        GbaTabsheet personenTabsheet = new GbaTabsheet();
        personenTabsheet.addStyleName("zoektab");
        personenTabsheet.addTab(relatiesLayout1, "Partner 1");
        personenTabsheet.addTab(relatiesLayout2, "Partner 2");

        addInfo(getOntbrekendePersonenMelding(getServices(),
            getDossier().getPersonen(PARTNER1, PARTNER2))
                .orElse(""));

        addExpandComponent(personenTabsheet);

      } else if (event.isEvent(AfterReturn.class)) {
        relatiesLayout1.init();
        relatiesLayout2.init();
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

  public class RelatiesLayout extends PageBsGerelateerdeLayout {

    private final DossierPersoon dossierPersoon;

    public RelatiesLayout(DossierPersoon dossierPersoon) {
      super(Page16Huwelijk.this.getApplication(), getDossier(), dossierPersoon);
      this.dossierPersoon = dossierPersoon;
    }

    @Override
    public void onDossierPersoon(DossierPersoon dossierPersoon) {
      getNavigation().goToPage(
          new PageBsGerelateerdePersoon(getZaakDossier().getDossier().getType(), dossierPersoon));
    }

    @Override
    public void onToevoegen(DossierPersoonType type) {

      DossierPersoon nieuwePersoon = dossierPersoon.toevoegenPersoon(type);
      getNavigation().goToPage(
          new PageBsGerelateerdePersoon(getZaakDossier().getDossier().getType(), nieuwePersoon));
    }

    @Override
    public void onVerwijderen(DossierPersoon persoon) {
      getServices().getDossierService().deletePersonen(getDossier(), asList(persoon));
    }

    @Override
    protected void afterLaadPersonen() {
      Page16Huwelijk.this.getApplication().getServices().getHuwelijkService().save(getDossier());
      super.afterLaadPersonen();
    }
  }
}
