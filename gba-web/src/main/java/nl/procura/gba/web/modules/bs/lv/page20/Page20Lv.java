/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.lv.page20;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ADOPTIEFOUDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.OUDER;

import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdePersoon;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdeTableLayout;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Lv extends BsPage<DossierLv> {

  private RelatiesLayout relatiesLayout = null;

  public Page20Lv() {
    super("Latere vermelding - ouder(s)");
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  protected void initPage() {
    super.initPage();
  }

  @Override
  public boolean checkPage() {
    super.checkPage();
    getServices().getLvService().save(getDossier());
    return true;
  }

  @Override
  public void event(PageEvent event) {
    try {
      if (event.isEvent(InitPage.class)) {
        addComponent(new BsStatusForm(getDossier()));
        setInfo("De ouders van het kind.");

        relatiesLayout = new RelatiesLayout(getZaakDossier().getKind());
        addExpandComponent(relatiesLayout);

      } else if (event.isEvent(AfterReturn.class)) {
        relatiesLayout.init();
      }
    } finally {
      super.event(event);
    }
  }

  protected List<DossierPersoon> getPersonen() {
    return relatiesLayout.getPersonen();
  }

  public class RelatiesLayout extends PageBsGerelateerdeTableLayout {

    private final DossierPersoon dossierPersoon;

    public RelatiesLayout(DossierPersoon dossierPersoon) {
      super(Page20Lv.this.getApplication(), getDossier());
      this.dossierPersoon = dossierPersoon;

      // Laad alleen de personen als deze nog niet zijn gevuld
      if (isGeenPersonen(OUDER)) {
        laadPersonen(getDossier().getPersonen(OUDER), OUDER);
      }

      RelatiesTableLayout layout1 = addLayout(new RelatiesTableLayout("Ouder(s)", OUDER));
      RelatiesTableLayout layout2 = addLayout(new RelatiesTableLayout("Adoptiefouder(s)", ADOPTIEFOUDER));
      addExpandComponent(layout1, 0.50f);
      addExpandComponent(layout2, 0.50f);
    }

    public boolean isGeenPersonen(DossierPersoonType... types) {
      return getDossier().getPersonen(types).isEmpty();
    }

    @Override
    public void laadRelaties(DossierPersoonType... types) {
      Services services = Page20Lv.this.getApplication().getServices();
      if (isType(asList(types), OUDER)) {
        getTypePersonen(services, dossierPersoon, OUDER_1, OUDER_2)
            .forEach(getDossier()::toevoegenPersoon);
      }
    }

    @Override
    public void onHerladen(DossierPersoonType[] types) {
      for (DossierPersoonType type : types) {
        laadPersonen(dossierPersoon.getPersonen(type), type);
        afterLaadPersonen();
      }
    }

    @Override
    public List<DossierPersoon> getPersonen(DossierPersoonType... types) {
      return getDossier().getPersonen(types);
    }

    @Override
    public List<DossierPersoon> getPersonen() {
      return getDossier().getPersonen();
    }

    @Override
    public void onDossierPersoon(DossierPersoon dossierPersoon) {
      getNavigation().goToPage(new PageBsGerelateerdePersoon(getZaakDossier()
          .getDossier().getType(), dossierPersoon));
    }

    @Override
    public void toevoegenPersonen(List<DossierPersoon> personen) {
      getDossier().toevoegenPersonen(personen);
    }

    @Override
    public void verwijderPersoon(DossierPersoon persoon) {
      getDossier().verwijderPersoon(persoon);
    }

    @Override
    public void onToevoegen(DossierPersoonType type) {
      DossierPersoon nieuwePersoon = getDossier().toevoegenPersoon(type);
      ZaakType zaakType = getZaakDossier().getDossier().getType();
      getNavigation().goToPage(new PageBsGerelateerdePersoon(zaakType, nieuwePersoon));
    }

    @Override
    public void onVerwijderen(DossierPersoon persoon) {
      getServices().getDossierService().deletePersonen(getDossier(), singletonList(persoon));
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
