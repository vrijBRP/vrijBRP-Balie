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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Zaakrelatie
 */
public abstract class Page2ZaakRelatie extends NormalPageTemplate {

  private final Zaak           zaak;
  private final ZaakRelatie    relatie;
  private Page2ZaakRelatieForm form = null;

  public Page2ZaakRelatie(final Zaak zaak, final ZaakRelatie relatie) {

    this.zaak = zaak;
    this.relatie = relatie;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      form = new Page2ZaakRelatieForm(zaak);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public abstract void onReload();

  @Override
  public void onSave() {

    form.commit();

    relatie.setZaakId(form.getBean().getZaakId());
    relatie.setGerelateerdZaakId(form.getBean().getZaakIdRel());

    getServices().getZaakRelatieService().save(relatie);
    getServices().getZaakRelatieService().updateZaakRelaties(zaak);

    onReload();

    onPreviousPage();
  }
}
