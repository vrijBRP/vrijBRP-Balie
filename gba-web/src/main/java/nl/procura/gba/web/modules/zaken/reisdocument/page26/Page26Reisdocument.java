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

package nl.procura.gba.web.modules.zaken.reisdocument.page26;

import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.INGEVOERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.ONBEKEND;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.jpa.personen.db.RdmAmpDoc;
import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page25.Page25Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.ReisdocumentBezorgingService;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page26Reisdocument extends ReisdocumentAanvraagPage {

  private Page26ReisdocumentForm1  form1;
  private Bezorging                bezorging = new Bezorging();
  private Page26ReisdocumentTable1 inhoudingTabel;

  public Page26Reisdocument(ReisdocumentAanvraag aanvraag) {
    super("Reisdocument: thuisbezorging", aanvraag);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  protected void initPage() {
    super.initPage();

    Page26ReisdocumentBean1 bean = new Page26ReisdocumentBean1();

    if (getAanvraag().getThuisbezorging().isMelding()) {
      bezorging = getAanvraag().getThuisbezorging();
      bean.setBezorgingGewenst(bezorging.getMelding().isBezorgingGewenst());

    } else {
      bezorging.setMelding(getBezorgingService().createNewVoormelding(getAanvraag()));

      // Prefill homedelivery
      getServices().getReisdocumentService().getInboxRequestData(getAanvraag())
          .ifPresent(data -> {
            bean.setBezorgingGewenst(data.getHomeDelivery());
            bezorging.getMelding().setBezorgingGewenst(data.getHomeDelivery());
          });
    }

    bean.setOpmerkingen(bezorging.getMelding().getOpmerkingen());
    bean.setAdres(bezorging.getAdres());

    VLayout tabelLayout = new VLayout().spacing(true);
    form1 = new Page26ReisdocumentForm1(bean, bezorging, getBezorgingService(), tabelLayout::setVisible);

    if (bezorging.isAangemeld()) {
      form1.setReadOnly(true);
    }
    addComponent(form1);

    inhoudingTabel = new Page26ReisdocumentTable1(bezorging, getAanvraag().getBasisPersoon());
    tabelLayout.addComponent(new Fieldset("In te houden reisdocumenten"));
    tabelLayout.addComponent(new InfoLayout("Geef aan welke documenten moeten worden ingenomen "
        + "bij de uitreiking door de bezorger."));

    tabelLayout.addExpandComponent(inhoudingTabel);
    addExpandComponent(tabelLayout);
  }

  @Override
  public void onNextPage() {
    form1.commit();

    if (bezorging.getStatus().getType().is(ONBEKEND, INGEVOERD)) {
      bezorging.getMelding().setEmail("");
      bezorging.getMelding().setTel1("");
      bezorging.getMelding().setTel2("");
      getBezorgingService().updateVoormelding(bezorging.getMelding(), getAanvraag().getBurgerServiceNummer());

      RdmAmp melding = bezorging.getMelding();
      melding.setOpmerkingen(form1.getBean().getOpmerkingen());
      melding.setBezorgingGewenst(form1.getBean().getBezorgingGewenst());
      melding.setLocatiecode(getServices().getGebruiker().getLocatie().getCodeRaas().longValue());
      melding.setBsn(getAanvraag().getBurgerServiceNummer().getLongValue());
      melding.setBundelRefNr(melding.getAanvrNr());

      if (melding.isBezorgingGewenst()) {
        if (getBezorgingService().isBundelingMogelijk()) {
          melding.setBundelRefNr(form1.getBean().getBundeling().getBundelRefNr());
        }

        if (isAllBlank(melding.getEmail(), melding.getTel1())) {
          getApplication().getParentWindow().addWindow(new ContactWindow(
              "Thuisbezorging vereist minimaal een geldig "
                  + "e-mailadres, mobiel- of thuisnummer"));
          return;
        }
      }
      melding.setHoofdorder(melding.getBundelRefNr().equals(melding.getAanvrNr()));
      getBezorgingService().saveMelding(melding);

      List<BezorgingInhouding> inhoudingen = getGekozenInhoudingen();
      for (RdmAmpDoc doc : new ArrayList<>(melding.getRdmAmpDocs())) {
        if (!melding.isBezorgingGewenst() || inhoudingen.stream()
            .noneMatch(inhouding -> inhouding.isDocumentNr(doc.getDocNr()))) {
          getBezorgingService().deleteInhouding(doc);
          melding.getRdmAmpDocs().remove(doc);
        }
      }

      if (melding.isBezorgingGewenst()) {
        getGekozenInhoudingen()
            .stream()
            .map(inhouding -> toRdmAmpDoc(melding, inhouding))
            .filter(Objects::nonNull)
            .filter(rdmAmpDoc -> !melding.getRdmAmpDocs().contains(rdmAmpDoc))
            .forEach(e -> melding.getRdmAmpDocs().add(e));

        for (RdmAmpDoc doc : melding.getRdmAmpDocs()) {
          getBezorgingService().saveInhouding(doc);
        }
      }
    }

    getAanvraag().setThuisbezorging(bezorging);
    getNavigation().goToPage(new Page25Reisdocument(getAanvraag()));
  }

  private List<BezorgingInhouding> getGekozenInhoudingen() {
    return inhoudingTabel.getReisdocumenten().stream()
        .filter(BezorgingInhouding::isInhoudenKeuze)
        .collect(Collectors.toList());
  }

  private RdmAmpDoc toRdmAmpDoc(RdmAmp rdmAmp, BezorgingInhouding inhouding) {
    for (RdmAmpDoc doc : rdmAmp.getRdmAmpDocs()) {
      if (inhouding.isDocumentNr(doc.getDocNr())) {
        return null;
      }
    }
    RdmAmpDoc rdmAmpDoc = new RdmAmpDoc();
    rdmAmpDoc.setDocType(inhouding.getDocumentType().getCode());
    rdmAmpDoc.setDocNr(inhouding.getDocumentNr());
    rdmAmpDoc.setDEndGeld(inhouding.getDatumEindeGeldigheid());
    rdmAmpDoc.setRdmAmp(rdmAmp);
    return rdmAmpDoc;
  }

  private ReisdocumentBezorgingService getBezorgingService() {
    return getApplication().getServices().getReisdocumentBezorgingService();
  }
}
