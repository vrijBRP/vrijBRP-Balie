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

package nl.procura.gba.web.modules.zaken.reisdocument;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;

public class ReisdocumentAanvraagPage extends ZakenPage {

  private ReisdocumentAanvraag aanvraag = null;

  public ReisdocumentAanvraagPage(String msg, ReisdocumentAanvraag aanvraag) {

    super(msg);

    setMargin(true);

    setAanvraag(aanvraag);
  }

  public ReisdocumentAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(ReisdocumentAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }
}
