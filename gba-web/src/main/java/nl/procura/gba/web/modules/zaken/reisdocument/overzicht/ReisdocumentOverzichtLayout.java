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

import java.util.Optional;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.RdmRaasComparison;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.vaadin.component.layout.Fieldset;

public class ReisdocumentOverzichtLayout extends GbaVerticalLayout implements ZaakTabLayout {

  private ReisdocumentOverzichtLayoutForm3 form3;

  public ReisdocumentOverzichtLayout(ReisdocumentAanvraag aanvraag) {
    init(aanvraag);
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    init((ReisdocumentAanvraag) zaak);
  }

  private void init(final ReisdocumentAanvraag aanvraag) {
    removeAllComponents();
    setSpacing(true);

    ReisdocumentService rdmService = Services.getInstance().getReisdocumentService();
    Optional<RdmRaasComparison> raasComparison = rdmService.getRaasComparison(aanvraag, false);

    if (raasComparison.isPresent()) {
      addComponent(new ReisdocumentComparisonLayout(raasComparison.get()));

    } else {
      addComponent(new Fieldset("Huidige status reisdocument en aanvraag"));
      form3 = new ReisdocumentOverzichtLayoutForm3(aanvraag);
      form3.setCaption(null);
      addComponent(form3);
    }

    addComponent(new ReisdocumentOverzichtLayoutForm1(aanvraag));
    if (!aanvraag.getToestemmingen().getGegevenToestemmingen().isEmpty()) {
      addComponent(new Fieldset("Toestemmingen", new ReisdocumentOverzichtTable(aanvraag)));
    }

    addComponent(new ReisdocumentOverzichtLayoutForm2(aanvraag));
  }
}
