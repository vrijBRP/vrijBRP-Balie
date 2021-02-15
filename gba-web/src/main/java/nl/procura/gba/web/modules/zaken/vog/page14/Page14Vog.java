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

package nl.procura.gba.web.modules.zaken.vog.page14;

import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page15.Page15Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogsService;

public class Page14Vog extends VogAanvraagPage {

  private Page14VogForm1 form1 = null;

  public Page14Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    form1 = new Page14VogForm1(aanvraag);

    setInfo(
        "Bijzondere omstandigheden m.b.t. de aanvraag, gelet op het risico voor de samenleving. De locatie waar de werkzaamheden worden verricht kan van belang zijn. Ook andere bijzonderheden graag vermelden.");
    addComponent(new VogHeaderForm(aanvraag));
    addComponent(form1);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page14VogBean1 b = form1.getBean();

    getAanvraag().getScreening().setOmstandighedenTekst(
        b.isByzOmstandigheden() ? b.getByzOmstandighedenToel() : "");

    VogsService d = getApplication().getServices().getVogService();
    d.save(getAanvraag());

    getNavigation().goToPage(new Page15Vog(getAanvraag()));

    super.onNextPage();
  }
}
