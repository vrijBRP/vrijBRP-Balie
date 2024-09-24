/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.DVBDOC;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.GELDIGVOORREIZENNAAR;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.NATIOGBA;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.NRVBDOC;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.PSEUDONIEM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.STAATLOOS;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.TERVERVANGINGVAN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.UITGEZONDERDELANDEN;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.NEDERLANDSE_IDENTITEITSKAART;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page25.Page25Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26Reisdocument;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.StaatloosType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuw reisdocument
 */
public class Page11Reisdocument extends ReisdocumentAanvraagPage {

  private Page11ReisdocumentForm1 form1 = null;

  public Page11Reisdocument(ReisdocumentAanvraag aanvraag) {

    super("Reisdocument: nieuw", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page11ReisdocumentForm1(getServices().getReisdocumentService(), getAanvraag());

      addComponent(form1);

      if (getAanvraag().getReisdocumentType().isDocument(NEDERLANDSE_IDENTITEITSKAART)) {

        form1.getField(PSEUDONIEM).setVisible(false);
      }

      if (getPl().getNatio().isNederlander()) {

        form1.getField(UITGEZONDERDELANDEN).setVisible(false);
        form1.getField(STAATLOOS).setVisible(false);
        form1.getField(NATIOGBA).setVisible(false);

      } else if (getPl().getNatio().isBehandeldAlsNederlander()) {

        form1.getField(UITGEZONDERDELANDEN).setVisible(false);
        form1.getField(STAATLOOS).setVisible(false);
        form1.getField(NATIOGBA).setVisible(false);
        form1.getField(TERVERVANGINGVAN).setVisible(false);
      }

      if (!getPl().getNatio().isNietNederlander()) {
        form1.getField(DVBDOC).setVisible(false);
        form1.getField(NRVBDOC).setVisible(false);
      }

      boolean teJong = getPl().getPersoon().getGeboorte().getLeeftijd() < 16;
      boolean heeftSignalering = getPl().getReisdoc().heeftSignalering();

      form1.getField(GELDIGVOORREIZENNAAR).setVisible(teJong || heeftSignalering);

      if (!getPl().getNatio().isStaatloos()) {
        form1.getField(STAATLOOS).setVisible(false);
      }

      form1.getBean().setStaatloos(
          getPl().getNatio().isStaatloos() ? StaatloosType.STAATLOZE : StaatloosType.NIET_VAN_TOEPASSING);

      form1.repaint();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page11ReisdocumentBean1 b = form1.getBean();

    getAanvraag().setGeldigheidsTermijn(b.getTermijnGeld().getBigDecimalValue());
    getAanvraag().setDatumEindeGeldigheid(new DateTime(along(b.getdGeldEnd().getValue())));
    getAanvraag().setVermeldingTitel(b.getVermeldingTitel());
    getAanvraag().setVermeldingLand(b.getVermeldingLand());

    // Als de nieuwe regels van toepassing zijn en gekozen wordt voor de standaard termijn dan
    // niet de datum opslaan.
    if (ReisdocumentUtils.isNieuweReisdocumentenRegelsVanToepassing(getServices().getReisdocumentService()) && pos(
        getAanvraag().getGeldigheidsTermijn())) {
      getAanvraag().setDatumEindeGeldigheid(null);
    }

    // Clausules
    getAanvraag().getClausules().setGeldigVoorReizen(b.getGeldigVoorReizenNaar());
    getAanvraag().getClausules().setOndertekening(b.getOndertekening());
    getAanvraag().getClausules().setPseudoniem(b.getPseudoniem());
    getAanvraag().getClausules().setStaatloos(b.getStaatloos());
    getAanvraag().getClausules().setTvv(b.getTerVervangingVan());
    getAanvraag().getClausules().setUitzonderingLanden(b.getUitgezonderdeLanden());
    getAanvraag().getClausules().setVermeldingPartner(b.getVermeldingPartner());
    getAanvraag().getClausules().setInBezigBuitenlandsDocument(b.getBuitenlDoc());

    // Verblijfsdocument
    getAanvraag().setNrVbDocument(b.getNrVbDoc());
    getAanvraag().setDatumEindeGeldigheidVb(new DateTime(along(b.getdVbDoc().getValue())));

    ContactgegevensService dbcg = getServices().getContactgegevensService();

    getAanvraag().getContactgegevens().setTelThuis(
        dbcg.getContactWaarde(getPl(), ContactgegevensService.TEL_THUIS));
    getAanvraag().getContactgegevens().setTelMobiel(
        dbcg.getContactWaarde(getPl(), ContactgegevensService.TEL_MOBIEL));
    getAanvraag().getContactgegevens().setTelWerk(dbcg.getContactWaarde(getPl(), ContactgegevensService.TEL_WERK));
    getAanvraag().getContactgegevens().setEmail(dbcg.getContactWaarde(getPl(), ContactgegevensService.EMAIL));

    ReisdocumentService service = getApplication().getServices().getReisdocumentService();
    service.save(getAanvraag());

    if (getServices().getReisdocumentBezorgingService().isEnabled()) {
      getNavigation().goToPage(new Page26Reisdocument(getAanvraag()));
    } else {
      getNavigation().goToPage(new Page25Reisdocument(getAanvraag()));
    }

    super.onNextPage();
  }
}
