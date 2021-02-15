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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab2.search;

import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab2SearchPage extends ZoekTabPage {

  private Tab2Form form = new Tab2Form();

  public Tab2SearchPage() {

    super("Zoeken in de landelijke database");

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInlogOpmerking();

      addButton(buttonSearch);
      addButton(buttonReset);

      buttonSearch.setCaption("Zoeken (Enter)");

      addComponent(getForm());
      setExpandRatio(getForm(), 1);

      getForm().getField(ZoekBean.BSN).focus();
    }

    super.event(event);
  }

  public Tab2Form getForm() {
    return form;
  }

  public void setForm(Tab2Form form) {
    this.form = form;
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {
    getForm().reset();
  }

  @Override
  public void onSearch() {

    getForm().commit();

    ZoekBean b = getForm().getBean();

    zoekLandelijk(b);
  }
}
