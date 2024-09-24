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

package nl.procura.gba.web.modules.zaken.reisdocument.page25;

import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtLayout;
import nl.procura.gba.web.modules.zaken.reisdocument.page19.Page19Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;

/**
 * Overzicht niet-opgeslagen reisdocument
 */
public class Page25Reisdocument extends ReisdocumentAanvraagPage {

  public Page25Reisdocument(ReisdocumentAanvraag aanvraag) {
    super("Reisdocument: overzicht", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    addComponent(new ReisdocumentOverzichtLayout(aanvraag));
  }

  @Override
  public void onNextPage() {
    getServices().getKassaService().addToWinkelwagen(getAanvraag());
    getNavigation().goToPage(new Page19Reisdocument(getAanvraag()));
  }
}
