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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Zaakidentificatie
 */
public abstract class Page2ZaakIdentificatie extends NormalPageTemplate {

  private final Zaak                 zaak;
  private final ZaakIdentificatie    id;
  private Page2ZaakIdentificatieForm form = null;

  public Page2ZaakIdentificatie(final Zaak zaak, final ZaakIdentificatie id) {

    this.zaak = zaak;
    this.id = id;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      form = new Page2ZaakIdentificatieForm(id);

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

    id.setType(form.getBean().getType().getCode());
    id.setExternId(form.getBean().getExternId());
    id.setZaakType(zaak.getType());

    getServices().getZaakIdentificatieService().save(id);
    getServices().getZaakIdentificatieService().updateZaakIdentificaties(zaak);

    onReload();

    onPreviousPage();
  }
}
