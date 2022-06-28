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

package nl.procura.gba.web.modules.zaken.reisdocument.page18;

import static nl.procura.gba.web.modules.zaken.reisdocument.page18.Page18ReisdocumentBean1.AANVRAAGNR;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.Page10Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * In te voeren aanvraagnummer
 */
public class Page18Reisdocument extends ReisdocumentAanvraagPage {

  private Form1 form1;

  public Page18Reisdocument(ReisdocumentAanvraag aanvraag) {

    super("Reisdocument: nieuw - aanvraagnummers", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Form1(getAanvraag());

      ReisdocumentService service = getApplication().getServices().getReisdocumentService();

      form1.getField(AANVRAAGNR).addValidator(new DubbelNummerValidator(service));

      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    getAanvraag().setAanvraagnummer(new Aanvraagnummer(form1.getBean().getAanvraagnr()));
    getAanvraag().setAnummer(new AnrFieldValue(getPl().getPersoon().getAnr().getVal()));
    getAanvraag().setBurgerServiceNummer(new BsnFieldValue(getPl().getPersoon().getBsn().getVal()));
    getAanvraag().setDatumTijdInvoer(new DateTime());
    getAanvraag().setIngevoerdDoor(new UsrFieldValue(getApplication().getServices().getGebruiker()));
    getAanvraag().setCodeRaas(getServices().getRaasService().getRaasCode());

    ReisdocumentService service = getApplication().getServices().getReisdocumentService();
    service.save(getAanvraag());

    getNavigation().goToPage(new Page10Reisdocument(getAanvraag()));
  }

  public class Form1 extends Page18ReisdocumentForm1 {

    private Form1(ReisdocumentAanvraag aanvraag) {

      super(aanvraag);
    }
  }
}
