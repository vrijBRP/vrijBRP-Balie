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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.resultpl.Tab3PlResultPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.resultwk.Tab3AdresResultPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.search.Tab3Form;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab3.search.Tab3SearchPage;
import nl.procura.vaadin.component.window.Message;

public class Tab3Page extends ZoekTabPage {

  private final Tab3Page prevPage;
  private Tab3Form       form = new Tab3Form();

  public Tab3Page(Tab3Page prevPage, String title) {

    super(title);

    this.prevPage = prevPage;

    if (prevPage != null) {
      form.setBean(prevPage.getForm().getBean());
    }
  }

  public Tab3Form getForm() {
    return form;
  }

  public void setForm(Tab3Form form) {
    this.form = form;
  }

  public Tab3Page getPrevPage() {
    return prevPage;
  }

  @Override
  public void onNew() {
    form.reset();
    getNavigation().removeOtherPages();
    getNavigation().goToPage(Tab3SearchPage.class);
  }

  @Override
  public void onSearch() {

    getForm().commit();

    ZoekBean b = getForm().getBean();

    ZoekArgumenten z = new ZoekArgumenten();

    if (b.getStraat() != null) {
      z.setStraatnaam(b.getStraat().getDescription());
    }

    z.setHuisnummer(b.getHnr());
    z.setHuisletter(b.getHnrl());
    z.setHuisnummertoevoeging(b.getHnrt());
    z.setHuisnummeraanduiding(astr(b.getHnra()));
    z.setPostcode(b.getPostcode());

    if (b.getGemeentedeel() != null) {
      z.setGemeentedeel(b.getGemeentedeel().getDescription());
    }

    if (b.getLocatie() != null) {
      z.setLokatie(b.getLocatie().getDescription());
    }

    WKResultWrapper result = getApplication().getServices().getPersonenWsService().getAdres(z, false);

    switch (result.getBasisWkWrappers().size()) {
      case 0:
        new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
        break;

      case 1:
        selectRow(result.getBasisWkWrappers().get(0));
        break;

      default:
        getNavigation().goToPage(new Tab3AdresResultPage(this, result));
    }
  }

  protected void addForm() {

    addButton(buttonSearch);
    addButton(buttonReset);

    buttonSearch.setCaption("Zoeken (Enter)");

    addComponent(getForm());

    getForm().getField(ZoekBean.STRAAT).focus();
  }

  private void selectRow(BaseWKExt wk) {

    try {

      if (wk != null) {

        WKResultWrapper result = getApplication().getServices().getPersonenWsService().getAdres(wk);

        if (result.getBasisWkWrappers().size() > 0) {
          getNavigation().goToPage(new Tab3PlResultPage(this, result));
        } else {
          new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
