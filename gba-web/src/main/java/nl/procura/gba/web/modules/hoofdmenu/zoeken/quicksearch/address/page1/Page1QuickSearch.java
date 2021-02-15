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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page1;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.CheckListener;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.SelectListener;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page2.Page2QuickSearch;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.standard.exceptions.ProException;

public class Page1QuickSearch extends NormalPageTemplate {

  private final int INT_ZERO = 0;
  private final int INT_ONE  = 1;

  private final SelectListener selectListener;
  private CheckListener        checkListener;
  private Page1QuickSearchForm form;

  public Page1QuickSearch(SelectListener selectListener,
      CheckListener checkListener) {

    this.selectListener = selectListener;
    this.checkListener = checkListener;
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
    if (b.getSource().is(AddressSourceType.BRP)) {
      findBrpAddresses(b);
    }

    if (b.getSource().is(AddressSourceType.BAG)) {
      if (checkListener.isMarked(b.getBagAddress())) {
        throw new ProException(INFO, "Dit adres is al toegevoegd");
      } else {
        selectListener.select(b.getBagAddress());
      }
    }
  }

  @Override
  protected void initPage() {

    form = new Page1QuickSearchForm(getServices());
    buttonSearch.setCaption("Toevoegen (F7)");
    addButton(buttonSearch);
    addButton(buttonReset, 1f);
    addButton(buttonClose);
    addComponent(form);

    super.initPage();
  }

  private void findBrpAddresses(Page1QuickSearchBean bean) {
    ZoekArgumenten z = new ZoekArgumenten();
    z.setStraatnaam(astr(bean.getStreet()));
    z.setPostcode(astr(bean.getPostalCode().getValue()));
    z.setHuisnummer(bean.getHouseNumber());
    z.setHuisletter(bean.getHouseNumberL());
    z.setHuisnummertoevoeging(bean.getHouseNumberT());
    z.setHuisnummeraanduiding(astr(bean.getHouseNumberA()));
    z.setDatum_einde("-1");

    if (!z.isGevuld()) {
      throw new ProException(ENTRY, INFO, "Geen adres ingegeven.");
    }

    List<Address> addresses = getServices().getPersonenWsService()
        .getAdres(z, false)
        .getBasisWkWrappers()
        .stream()
        .map(ProcuraInhabitantsAddress::new)
        .collect(Collectors.toList());

    if (addresses.isEmpty()) {
      throw new ProException(WARNING, "Het ingegeven adres kan niet worden gevonden in de gemeentelijke BRP");
    }

    if (addresses.size() == INT_ONE) {
      if (checkListener.isMarked(addresses.get(INT_ZERO))) {
        throw new ProException(INFO, "Dit adres is al toegevoegd");
      }
      selectListener.select(addresses.get(INT_ZERO));
      onClose();
    } else {
      getNavigation().goToPage(new Page2QuickSearch(addresses, selectListener, checkListener));
    }
  }
}
