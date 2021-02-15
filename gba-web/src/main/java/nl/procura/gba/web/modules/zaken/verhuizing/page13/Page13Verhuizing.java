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

package nl.procura.gba.web.modules.zaken.verhuizing.page13;

import static nl.procura.gba.web.components.containers.Container.PLAATS;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.AddressLayout;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAanvraagPage;
import nl.procura.gba.web.modules.zaken.verhuizing.page17.Page17Verhuizing;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuw adres bij verhuisaanvraag
 */

public class Page13Verhuizing extends VerhuisAanvraagPage {

  private Page13VerhuizingForm1 form1 = null;
  private AddressLayout         addressLayout;

  public Page13Verhuizing(VerhuisAanvraag verhuisAanvraag) {
    super("Verhuizing: adres", verhuisAanvraag);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      getAanvraag().getNieuwAdres().setFunctieAdres(FunctieAdres.WOONADRES);
      form1 = new Page13VerhuizingForm1();
      addressLayout = new AddressLayout(new MigrationAddress(getAanvraag()), getServices());
      addComponent(form1);
      addComponent(addressLayout);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    form1.commit();
    checkAanvang(form1.getBean().getAanvang());
    if (addressLayout.isSaved()) {
      fillOutRelocation();
      getNavigation().goToPage(new Page17Verhuizing(getAanvraag()));
      super.onNextPage();
    }
  }

  private void fillOutRelocation() {
    String gemeenteCode = getApplication().getServices().getGebruiker().getGemeenteCode();
    getAanvraag().getNieuwAdres().setGemeente(PLAATS.get(gemeenteCode));
    getAanvraag().setDatumIngang(new DateTime(form1.getBean().getAanvang()));
    if (getAanvraag().getTypeVerhuizing() == VerhuisType.HERVESTIGING) {
      getAanvraag().getHervestiging().setDatumHervestiging(new DateTime(form1.getBean().getAanvang()));
    }
  }
}
