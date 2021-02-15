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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page80;

import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.overlijden.correspondentie.CorrespondentieBean;
import nl.procura.gba.web.modules.bs.overlijden.correspondentie.CorrespondentieForm;
import nl.procura.gba.web.modules.bs.overlijden.correspondentie.UittrekselTable;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.BsPageOverlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page20.Page20Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page30.Page30Overlijden;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page80Overlijden extends BsPageOverlijden {

  private CorrespondentieForm form = null;

  public Page80Overlijden() {
    super("Overlijden - correspondentie");
  }

  @Override
  public boolean checkPage() {
    form.commit();

    CorrespondentieBean bean = form.getBean();
    DossCorrDest corr = getZaakDossier().getVerzoek().getCorrespondentie();
    corr.setCommunicatieType(bean.getCommunicatieType().getCode());
    corr.setOrganisatie(bean.getOrganisatie());
    corr.setAfdeling(bean.getAfdeling());
    corr.setNaam(bean.getNaam());
    corr.setEmail(bean.getEmail());
    corr.setTelefoon(bean.getTelefoon());
    corr.setStraat(bean.getStraat());
    corr.setHnr(NumberUtils.toInt(bean.getHnr(), -1));
    corr.setHnrL(bean.getHnrL());
    corr.setHnrT(bean.getHnrT());
    corr.setPostcode(bean.getPostcode().getStringValue());
    corr.setPlaats(bean.getPlaats());

    getServices().getOverlijdenGemeenteService().save(getDossier());
    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      form = new CorrespondentieForm(getZaakDossier().getVerzoek().getCorrespondentie());
      UittrekselTable table = new UittrekselTable(getZaakDossier().getVerzoek().getUittreksels());

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));
      setInfo("Vul de gegevens in en druk op Volgende (F2) om verder te gaan.");
      addComponent(form);
      addComponent(new Fieldset("Gevraagde documenten", table));
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    getNavigation().goToPage(Page30Overlijden.class);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page20Overlijden.class);
  }
}
