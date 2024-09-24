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

package nl.procura.gba.web.modules.bs.overlijden;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdeLayout;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdePersoon;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class PageBsOverlijdenGerelateerde<T extends ZaakDossier> extends BsPage<T> {

  private RelatiesLayout relatiesLayout = null;

  private DossierPersoon dossierPersoon;

  public PageBsOverlijdenGerelateerde(String caption) {

    super(caption);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    for (DossierPersoon persoon : relatiesLayout.getPersonen()) {
      if (DossierPersoonType.KIND.is(persoon.getDossierPersoonType()) && isOverleden(persoon)) {
        throw new ProException(WARNING, "Eén of meerdere kinderen is overleden");
      }
    }

    super.checkPage();

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));
        setInfo("De ouders, partners en de minderjarige kinderen van de overledene." +
            "<hr/><b>Het is belangrijk dat de meest actuele (ex-)partner van de overledene bovenaan staat.</b>");

        relatiesLayout = new RelatiesLayout();
        addExpandComponent(relatiesLayout);
      } else if (event.isEvent(AfterReturn.class)) {
        relatiesLayout.init();
      }
    } finally {
      super.event(event);
    }
  }

  public boolean isMeerderjarig(DossierPersoon persoon) {
    return (persoon.isVolledig() && (aval(persoon.getGeboorte().getLeeftijd()) >= 18));
  }

  public boolean isOverleden(DossierPersoon persoon) {
    if (persoon.isVolledig() && persoon.getBurgerServiceNummer().isCorrect()) {
      BasePLExt persoonPl = getServices().getPersonenWsService().getPersoonslijst(
          persoon.getBurgerServiceNummer().getStringValue());
      return persoonPl.getOverlijding().isOverleden();
    }
    return false;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  protected List<DossierPersoon> getPersonen() {
    return relatiesLayout.getPersonen();
  }

  protected List<DossierPersoon> getPersonen(DossierPersoonType... types) {
    return relatiesLayout.getPersonen(types);
  }

  protected void init(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  public class RelatiesLayout extends PageBsGerelateerdeLayout {

    public RelatiesLayout() {
      super(PageBsOverlijdenGerelateerde.this.getApplication(), getDossier(), dossierPersoon);
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
      getServices().getDossierService().deletePersonen(getDossier(), Collections.singletonList(persoon));
    }
  }
}
