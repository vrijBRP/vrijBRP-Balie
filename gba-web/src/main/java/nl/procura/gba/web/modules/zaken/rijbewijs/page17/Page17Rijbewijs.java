/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.rijbewijs.page17;

import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.EMAIL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_MOBIEL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_THUIS;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page8.Page8Rijbewijs;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page17Rijbewijs extends RijbewijsPage {

  private final RijbewijsAanvraagAntwoord documentAanvraag;
  private final RijbewijsAanvraag         aanvraag;
  private Page17RijbewijsForm             form = null;

  public Page17Rijbewijs(RijbewijsAanvraagAntwoord documentAanvraag, RijbewijsAanvraag aanvraag) {
    super("Nieuwe aanvraag");
    this.documentAanvraag = documentAanvraag;
    this.aanvraag = aanvraag;
    setMargin(true);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Page17RijbewijsForm();
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    form.commit();
    aanvraag.setIndBezorgen(form.getBean().getIndBezorgen());
    aanvraag.setOpmBezorgen(form.getBean().getOpmBezorgen());
    documentAanvraag.setThuisbezorgingGewenst(form.getBean().getIndBezorgen());

    if (aanvraag.isThuisbezorgingGewenst()) {
      ContactgegevensService service = getServices().getContactgegevensService();
      String email = service.getContactWaarde(aanvraag.getBasisPersoon(), EMAIL);
      String mobiel = service.getContactWaarde(aanvraag.getBasisPersoon(), TEL_MOBIEL);
      String thuis = service.getContactWaarde(aanvraag.getBasisPersoon(), TEL_THUIS);

      if (isAllBlank(email, mobiel, thuis)) {
        getApplication().getParentWindow().addWindow(new ContactWindow(
            "Thuisbezorging vereist minimaal een geldig "
                + "e-mailadres, mobiel- of thuisnummer"));
        return;
      }
    }

    getNavigation().goToPage(new Page8Rijbewijs(documentAanvraag, aanvraag));
  }
}
