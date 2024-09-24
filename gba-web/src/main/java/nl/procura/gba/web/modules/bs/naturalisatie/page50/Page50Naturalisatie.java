/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page50;

import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.INCOMPLEET;

import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page50Naturalisatie extends BsPageNaturalisatie {

  private Page50NaturalisatieForm form;

  public Page50Naturalisatie() {
    super("Nationaliteit - behandeling");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonNext);

      form = new Page50NaturalisatieForm(getZaakDossier());

      setInfo("Leg vast wat van toepassing is en druk waar nodig aanvullende documenten af. "
          + "Druk op Volgende (F2) om verder te gaan.");

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      form.commit();

      Page50NaturalisatieBean bean = form.getBean();
      getZaakDossier().setBehBotOpgevraagd(bean.getBerichtOmtrentToelating());
      getZaakDossier().setKinderen12AkkoordType(bean.getMinderjarigeKinderen12());
      getZaakDossier().setBehMinderjKind2(bean.getMinderjarigeKinderen16());
      getZaakDossier().setBehAndereVertToel(bean.getToelichting());
      getZaakDossier().setBehOpgevrJustis(getZaakDossier().isOptie() ? bean.getInformatieJustis() : Boolean.FALSE);
      getZaakDossier().setBehDAanvr(bean.getDatumAanvraag());
      getZaakDossier().setBehTermDEnd(bean.getEindeTermijn());
      getZaakDossier().setBehTermToel(bean.getToelichting2());

      getServices().getNaturalisatieService().save(getDossier());

      if (bean.getDatumAanvraag() != null && getDossier().getStatus().is(INCOMPLEET)) {
        getParentWindow().addWindow(new OkDialog("Ter informatie: status wijziging",
            "Omdat de datum aanvraag is gevuld is de status van de zaak gewijzigd van "
                + "<b><u>incompleet</u></b> naar <b><u>in behandeling</u></b>",
            "700px"));
        getServices().getDossierService().updateStatus(getDossier(), INCOMPLEET, INBEHANDELING, "");
      }

      return true;
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }
}
