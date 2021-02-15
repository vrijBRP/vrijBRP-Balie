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

package nl.procura.gba.web.modules.zaken.vog.page16;

import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.overzicht.VogOverzichtLayout;
import nl.procura.gba.web.modules.zaken.vog.page17.Page17Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

/**
 * Tonen aanvraag
 */
public class Page16Vog extends VogAanvraagPage {

  public Page16Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    setAanvraag(aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo("Controleer de gegevens.");

    addComponent(new VogOverzichtLayout(aanvraag));
  }

  @Override
  public void onNextPage() {

    // Toevoegen aan de kassa
    getServices().getKassaService().addToWinkelwagen(getAanvraag(), false);

    getNavigation().goToPage(new Page17Vog(getAanvraag()));

    super.onNextPage();
  }
}
