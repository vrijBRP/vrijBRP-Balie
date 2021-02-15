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

package nl.procura.gba.web.modules.zaken.vog.page15;

import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page16.Page16Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogsService;

public class Page15Vog extends VogAanvraagPage {

  private Page15VogForm1 form1 = null;

  public Page15Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    form1 = new Page15VogForm1(aanvraag);

    addComponent(new VogHeaderForm(aanvraag));
    addComponent(form1);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page15VogBean1 b = form1.getBean();

    getAanvraag().getOpmerkingen().setByzonderhedenTekst(b.isBijzonder() ? b.getBijzonderToel() : "");
    getAanvraag().getOpmerkingen().setPersisterenTekst(b.isPersist() ? b.getPersistToel() : "");
    getAanvraag().getOpmerkingen().setCovogAdviesTekst(b.isCovogAdvies() ? b.getCovogAdviesToel() : "");
    getAanvraag().getOpmerkingen().setToelichtingTekst(b.isAlgToelichting() ? b.getAlgToelichtingToel() : "");
    getAanvraag().getOpmerkingen().setBurgemeesterAdvies(b.getAdvies());

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getAanvraag());

    VogsService d = getApplication().getServices().getVogService();
    d.save(getAanvraag());

    getNavigation().goToPage(new Page16Vog(getAanvraag()));

    super.onNextPage();
  }
}
