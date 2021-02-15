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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page160;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page160Zaken extends ZakenregisterPage<Zaak> {

  private Page160ZakenForm1 form1 = null;

  public Page160Zaken() {

    super(null, "Filter");

    setSpacing(true);

    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      Page160ZakenBean bean = new Page160ZakenBean();

      getStatusses(bean);

      addComponent(new InfoLayout("Statussen", "De volgende status(sen) <b>niet</b> tellen en/of tonen."));

      form1 = new Page160ZakenForm1(bean);

      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSave() {

    form1.commit();

    setStatusses();

    successMessage("De gegevens zijn opgeslagen");

    reloadTree();

    super.onSave();
  }

  private void addStatus(List<ZaakStatusType> list, ZaakStatusType zaakStatus, boolean isChecked) {
    if (isChecked) {
      list.add(zaakStatus);
    }
  }

  private void getStatusses(Page160ZakenBean bean) {

    Collection<ZaakStatusType> statusses = getServices().getZakenregisterService().getNegeerStatussen();

    bean.setIncompleet(statusses.contains(ZaakStatusType.INCOMPLEET));
    bean.setWachtkamer(statusses.contains(ZaakStatusType.WACHTKAMER));
    bean.setOpgenomen(statusses.contains(ZaakStatusType.OPGENOMEN));
    bean.setInbehandeling(statusses.contains(ZaakStatusType.INBEHANDELING));
    bean.setGeprevalideerd(statusses.contains(ZaakStatusType.GEPREVALIDEERD));
    bean.setDocumentOntvangen(statusses.contains(ZaakStatusType.DOCUMENT_ONTVANGEN));
    bean.setGeweigerd(statusses.contains(ZaakStatusType.GEWEIGERD));
    bean.setVerwerkt(statusses.contains(ZaakStatusType.VERWERKT));
    bean.setGeannuleerd(statusses.contains(ZaakStatusType.GEANNULEERD));
  }

  private void reloadTree() {

    ZakenregisterAccordionTab tab = VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class);

    tab.reloadTree();
  }

  private void setStatusses() {
    List<ZaakStatusType> list = new ArrayList<>();
    addStatus(list, ZaakStatusType.INCOMPLEET, form1.getBean().isIncompleet());
    addStatus(list, ZaakStatusType.WACHTKAMER, form1.getBean().isWachtkamer());
    addStatus(list, ZaakStatusType.OPGENOMEN, form1.getBean().isOpgenomen());
    addStatus(list, ZaakStatusType.INBEHANDELING, form1.getBean().isInbehandeling());
    addStatus(list, ZaakStatusType.GEPREVALIDEERD, form1.getBean().isGeprevalideerd());
    addStatus(list, ZaakStatusType.DOCUMENT_ONTVANGEN, form1.getBean().isDocumentOntvangen());
    addStatus(list, ZaakStatusType.GEWEIGERD, form1.getBean().isGeweigerd());
    addStatus(list, ZaakStatusType.VERWERKT, form1.getBean().isVerwerkt());
    addStatus(list, ZaakStatusType.GEANNULEERD, form1.getBean().isGeannuleerd());
    getServices().getZakenregisterService().setNegeerStatusses(list);
  }
}
