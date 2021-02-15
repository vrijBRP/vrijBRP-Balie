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

package nl.procura.gba.web.modules.beheer.profielen.page12.tab2.page2;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2ZaakConfiguraties extends NormalPageTemplate {

  private Page2ZaakConfiguratiesForm1 form = null;
  private ZaakConfiguratie            configuratie;

  public Page2ZaakConfiguraties(ZaakConfiguratie configuratie) {
    super("Toevoegen / muteren configuratie");
    this.configuratie = configuratie;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Page2ZaakConfiguratiesForm1();
      form.setConfiguratie(configuratie);
      addComponent(new Fieldset("Configuratie",
          new InfoLayout("Laat bron en leverancier leeg om de standaardwaarden te gebruiken.")));
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    configuratie = new ZaakConfiguratie();
    form.setConfiguratie(new ZaakConfiguratie());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page2ZaakConfiguratiesBean bean1 = form.getBean();
    configuratie.setZaakConf(bean1.getTitel());
    configuratie.setBron(bean1.getBron());
    configuratie.setLeverancier(bean1.getLeverancier());
    configuratie.setIndZaaksysteemId(isTrue(bean1.getIndZaaksysteemId()));
    configuratie.setZaakTypes(bean1.getZaakTypesAsList());

    getServices().getZaakConfiguratieService().save(configuratie);
    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }
}
