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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page270;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBuilder;
import nl.procura.gba.web.modules.zaken.personmutations.page3.Page3PersonListMutations;
import nl.procura.gba.web.modules.zaken.personmutations.page5.Page5PersonListMutations.WindowPersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;

/**
 * Tonen persoonsmutaties
 */
public class Page270Zaken extends ZakenregisterOptiePage<PersonListMutation> {

  public Page270Zaken(PersonListMutation zaak) {
    super(zaak, "Zakenregister - persoonsmutaties");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonAanpassen);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("pl", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  @Override
  protected void goToZaak() {
    Page3PersonListMutations page = new Page3PersonListMutations(getZaak());
    getApplication().getParentWindow().addWindow(new WindowPersonListMutations(page, this::reloadTabs));
  }

  @Override
  protected void addTabs(ZaakTabsheet<PersonListMutation> tabsheet) {
    PersonMutationOverviewBuilder.addTab(tabsheet, getZaak());
  }
}
