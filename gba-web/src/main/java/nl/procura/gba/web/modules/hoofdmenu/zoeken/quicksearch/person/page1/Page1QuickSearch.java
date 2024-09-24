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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page2.Page2QuickSearch;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1QuickSearch extends NormalPageTemplate {

  private final Page1QuickSearchForm form = new Page1QuickSearchForm();

  private final SelectListener quickSearchListener;

  public Page1QuickSearch(SelectListener quickSearchListener) {
    this.quickSearchListener = quickSearchListener;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonSearch);
      addButton(buttonReset, 1f);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void attach() {
    super.attach();
    VaadinUtils.resetHeight(getWindow());
    getWindow().setWidth("700px");
    getWindow().center();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {
    form.reset();
  }

  @Override
  public void onSearch() {

    form.commit();

    Page1QuickSearchBean b = form.getBean();

    PLEArgs args = new PLEArgs();
    args.setMaxFindCount(10);
    args.addNummer(b.getBsn().getStringValue());
    args.addNummer(b.getAnr().getStringValue());
    args.setGeboortedatum(b.getGeboortedatum().getStringValue());
    args.setGeslachtsnaam(astr(b.getGeslachtsnaam()));
    args.setPostcode(astr(b.getPostcode()));
    args.setHuisnummer(b.getHnr());

    Address adres = b.getAdres();
    if (adres != null) {
      args.setPostcode(adres.getPostalCode());
      args.setHuisnummer(adres.getHnr());
      args.setHuisletter(adres.getHnrL());
      args.setHuisnummertoevoeging(adres.getHnrT());
    }

    args.setShowHistory(false);
    args.setShowArchives(false);
    args.addCat(PERSOON, VB, INSCHR);
    args.setCat(HUW_GPS, isTru(getApplication().getServices().getGebruiker().getParameters()
        .get(ParameterConstant.ZOEK_PLE_NAAMGEBRUIK).getValue()));

    int maxFindCount = aval(getApplication().getServices().getGebruiker().getParameters()
        .get(ParameterConstant.ZOEK_MAX_FOUND_COUNT).getValue());
    args.setMaxFindCount(maxFindCount);

    PLResultComposite result = getApplication().getServices().getPersonenWsService().getPersoonslijsten(args,
        false);

    if (result.getBasisPLWrappers().size() > 0) {
      getNavigation().goToPage(new Page2QuickSearch(result, quickSearchListener));
    } else {
      new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
    }
  }
}
