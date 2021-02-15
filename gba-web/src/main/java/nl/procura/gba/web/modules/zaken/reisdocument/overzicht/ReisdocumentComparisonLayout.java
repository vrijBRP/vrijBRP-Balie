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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import static nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtBean3.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_GOED;
import static nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType.AANVRAAG_NIET_AFGESLOTEN;

import com.vaadin.ui.Button;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.RdmRaasComparison;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class ReisdocumentComparisonLayout extends GbaVerticalLayout {

  private RdmRaasComparison comparison;

  public ReisdocumentComparisonLayout(RdmRaasComparison comparison) {
    this.comparison = comparison;
    setSpacing(true);
    setLayout();
  }

  public void setLayout() {

    removeAllComponents();

    DocAanvraagDto raasAanvraag = comparison.getRaasCase();
    ReisdocumentAanvraag aanvraag = comparison.getCase();

    ReisdocumentOverzichtLayoutForm4 form1 = new ReisdocumentOverzichtLayoutForm4(aanvraag, raasAanvraag,
        AFLEVERING, AFSLUITING);

    ReisdocumentOverzichtLayoutForm4 form2 = new ReisdocumentOverzichtLayoutForm4(aanvraag, raasAanvraag,
        AFLEVERING2, AFSLUITING2);

    addComponent(new Fieldset("Huidige status reisdocument en aanvraag"));

    if (comparison.isDifferent()) {
      StringBuilder msg = new StringBuilder();
      msg.append("<b>Er zijn verschillen tussen het RAAS en de BRP applicatie.</b>");

      if (aanvraag.getStatus().isEindStatus()) {
        msg.append("</br>Deze zijn niet automatisch overgenomen omdat de zaak al een eindstatus heeft.");
      }

      msg.append("</br>Met onderstaande knop kunnen de gegevens van het RAAS worden overgenomen.");
      addComponent(new InfoLayout("", ProcuraTheme.ICOON_24.WARNING, msg.toString()));

      Button copyRaasButton = new Button("Waardes van de RAAS service overnemen in BRP applicatie");
      copyRaasButton.addListener((Button.ClickListener) clickEvent -> {
        sync(form1);
      });

      addComponent(copyRaasButton);

    } else {
      addComponent(new InfoLayout("", GbaWebTheme.ICOON_24.OK,
          "Het RAAS en de BRP applicatie hebben dezelfde statussen"));
    }

    addComponent(form1);
    form2.setCaption("Statussen in RAAS");
    addComponent(form2);
  }

  private void sync(ReisdocumentOverzichtLayoutForm4 form1) {

    ReisdocumentAanvraag zaak = comparison.getCase();
    zaak.setReisdocumentStatus(comparison.getUpdatedStatus());

    ReisdocumentService reisdocumentService = Services.getInstance().getReisdocumentService();
    reisdocumentService.save(zaak);

    form1.updateBean();
    setLayout();

    boolean raasDocumentOntvangen = comparison.isRaasDeliveryStatus(DOCUMENT_GOED);
    boolean raasNietAfgesloten = comparison.isRaasClosingStatus(AANVRAAG_NIET_AFGESLOTEN);

    if (raasDocumentOntvangen &&
        raasNietAfgesloten &&
        zaak.getStatus().isEindStatus()) {
      getParentWindow().addWindow(new ConfirmDialog("De gegevens zijn overgenomen." +
          "<hr/>Wilt u ook de status van de zaak wijzigen naar <br/>'<b>document ontvangen</b>'?") {

        @Override
        public void buttonYes() {
          reisdocumentService.updateStatus(zaak, zaak.getStatus(),
              ZaakStatusType.DOCUMENT_ONTVANGEN, "");
          super.buttonYes();
        }
      });
    }
  }
}
