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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8;

import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.MIJN_OVERHEID_NIET;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.MIJN_OVERHEID_WEL;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab1.Page8ZakenTab1;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab2.Page8ZakenTab2;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3.Page8ZakenTab3;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab4.Page8ZakenTab4;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakArgumenten;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;

public class Page8ZakenTab extends GbaPageTemplate {

  private final LazyTabsheet tabs  = new LazyTabsheet();
  private List<DocumentZaak> zaken = new ArrayList<>();

  public Page8ZakenTab() {
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (isMijnOverheid()) {

        tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
        tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
        tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

        tabs.addTab(new Page8ZakenTab1(), "", null);
        tabs.addTab(new Page8ZakenTab2(), "", null);
        tabs.addTab(new Page8ZakenTab3(), "", null);

        reload();
        addExpandComponent(tabs);

      } else {
        reload();
        addExpandComponent(new Page8ZakenTab4());
      }
    }

    super.event(event);
  }

  public List<Zaak> getAlle() {
    return new ArrayList<>(zaken);
  }

  public List<Zaak> getGeen() {

    List<Zaak> filteredZaken = new ArrayList<>();
    for (Zaak zaak : zaken) {
      if (zaak.getZaakHistorie().getAttribuutHistorie().is(MIJN_OVERHEID_NIET)) {
        filteredZaken.add(zaak);
      }
    }

    return filteredZaken;
  }

  public List<Zaak> getOnBepaald() {

    List<Zaak> filteredZaken = new ArrayList<>();
    for (Zaak zaak : zaken) {
      AttribuutHistorie historie = zaak.getZaakHistorie().getAttribuutHistorie();
      if (historie.isNot(MIJN_OVERHEID_WEL) && historie.isNot(MIJN_OVERHEID_NIET)) {
        filteredZaken.add(zaak);
      }
    }

    return filteredZaken;
  }

  public List<Zaak> getWel() {

    List<Zaak> filteredZaken = new ArrayList<>();
    for (Zaak zaak : zaken) {
      if (zaak.getZaakHistorie().getAttribuutHistorie().is(MIJN_OVERHEID_WEL)) {
        filteredZaken.add(zaak);
      }
    }

    return filteredZaken;
  }

  public void reload() {

    DocumentZaakArgumenten args = new DocumentZaakArgumenten();
    args.setStatussen(OPGENOMEN, INBEHANDELING);
    args.setDocumentTypes(PL_UITTREKSEL);

    ZakenService dbZaken = getServices().getZakenService();
    zaken = dbZaken.getVolledigeZaken(getServices().getDocumentZakenService().getMinimalZaken(args));

    reloadCaptions();
  }

  private void reloadCaptions() {
    if (isMijnOverheid()) {
      tabs.getTab(0).setCaption(caption("Abonnee niet bepaald", getOnBepaald()));
      tabs.getTab(1).setCaption(caption("Geen abonnee", getGeen()));
      tabs.getTab(2).setCaption(caption("Wel abonnee", getWel()));
    }
  }

  private String caption(String caption, List<Zaak> list) {
    return caption + " (" + list.size() + ")";
  }

  private boolean isMijnOverheid() {
    return isTru(getApplication().getParmValue(ParameterConstant.MIJN_OVERHEID_BULK_UITTREKSELS));
  }
}
