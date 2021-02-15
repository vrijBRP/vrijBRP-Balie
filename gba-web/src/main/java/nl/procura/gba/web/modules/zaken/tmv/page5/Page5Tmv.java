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

package nl.procura.gba.web.modules.zaken.tmv.page5;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvTabPage;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TmvWaarschuwing;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page5Tmv extends TmvTabPage {

  private Page5TmvForm1 form       = null;
  private InfoLayout    infoLayout = new InfoLayout();

  public Page5Tmv(TerugmeldingAanvraag tmv) {

    super(tmv);
    setMargin(false);

    addButton(buttonSave);

    setInfoLayout();

    form = new Page5TmvForm1(getTmv());
    addComponent(form);
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(LoadPage.class)) {
        setVerantwVeld();
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    form.commit();

    Page5TmvBean1 bean = form.getBean();

    getTmv().setVerantwoordelijke(bean.getVerantwoordelijke());
    getTmv().setDatumRappel(new DateTime(along(bean.getRappeldatum().getValue())));
    getTmv().setResultaat(bean.getResultaat());

    getServices().getTerugmeldingService().updateStatus(getTmv(), getTmv().getStatus(), bean.getStatus(), "");
    getServices().getTerugmeldingService().updateTerugmelding(getTmv());

    new Message(getWindow(), "De gegevens zijn opgeslagen.", Message.TYPE_SUCCESS);

    super.onSave();
  }

  private void setInfoLayout() {

    infoLayout = new InfoLayout("Melding", "");

    TmvWaarschuwing waarschuwing = getTmv().getWaarschuwing();
    if (fil(waarschuwing.getMsg())) {
      infoLayout.append(waarschuwing.getMsg());
      addComponent(infoLayout);
    }
  }

  private void setVerantwVeld() {

    List<Gebruiker> behandelaars = getServices().getParameterService().getParameterGebruikers(
        ParameterConstant.TERUGMBEHEER, GeldigheidStatus.ACTUEEL);

    if (behandelaars.isEmpty()) {

      StringBuilder msg = new StringBuilder();

      if (infoLayout.getParent() != null) { // Ruler toevoegen als de infoLayout al is toegevoegd
        msg.append("<hr/>");
      }

      msg.append("Er zijn nog geen gebruikers die als terugmeldingbeheerder zijn aangemerkt.");

      infoLayout.append(setClass(false, msg.toString()));

      if (infoLayout.getParent() == null) {
        addComponent(infoLayout, 1);
      }
    }

    GebruikerContainer container = new GebruikerContainer(behandelaars);
    GbaNativeSelect veld = form.getField(Page5TmvBean1.VERANTWOORDELIJKE, GbaNativeSelect.class);
    veld.setContainerDataSource(container);
    veld.setValue(getTmv().getVerantwoordelijke());
  }
}
