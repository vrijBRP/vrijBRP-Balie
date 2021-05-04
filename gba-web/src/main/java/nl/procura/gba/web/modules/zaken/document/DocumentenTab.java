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

package nl.procura.gba.web.modules.zaken.document;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.*;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.zaken.document.page1.Page1Document;
import nl.procura.gba.web.modules.zaken.document.page2.ModulePage2Document;
import nl.procura.gba.web.modules.zaken.document.page3.Page3Document;
import nl.procura.gba.web.modules.zaken.document.page4.Page4Document;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakArgumenten;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZakenService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;

public class DocumentenTab extends GbaPageTemplate {

  private final LazyTabsheet tabs = new LazyTabsheet();

  public DocumentenTab() {

    setMargin(false);

    tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
    tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
    tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

    tabs.addTab(new Tab1Module(), "Nieuw");
    tabs.addTab(ModulePage2Document.class, "Aangevraagde documenten");
    tabs.addTab(Page3Document.class, "Archief");
    tabs.addTab(Page4Document.class, "Uploaden");

    addExpandComponent(tabs);
  }

  @Override
  public void attach() {

    reload();

    super.attach();
  }

  public int getAantal() {

    DocumentZakenService service = getServices().getDocumentZakenService();
    DocumentZaakArgumenten zaakArgumenten = new DocumentZaakArgumenten(getPl());
    zaakArgumenten.setDocumentTypes(PL_FORMULIER, PL_NATURALISATIE, PL_OPTIE, PL_ADRESONDERZOEK);
    return service.getZakenCount(zaakArgumenten);
  }

  public int getAantalArchief() {
    return getServices().getDmsService().countDocumentsByPL(getPl());
  }

  public void reload() {

    try {
      tabs.getTab(1).setCaption("Afgedrukte documenten - " + getAantal());
      tabs.getTab(2).setCaption("Archief - " + getAantalArchief());

      tabs.getTab(1).setVisible(getApplication().isProfielActie(ProfielActie.SELECT_ZAAK_DOCUMENT));
      tabs.getTab(2).setVisible(getApplication().isProfielActie(ProfielActie.SELECT_ZAAK_DOCUMENT_ARCHIEF));
      tabs.getTab(3).setVisible(getApplication().isProfielActie(ProfielActie.SELECT_ZAAK_DOCUMENT_ARCHIEF));

    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  public class Tab1Module extends ModuleTemplate {

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(Page1Document.class);
      }
    }
  }
}
