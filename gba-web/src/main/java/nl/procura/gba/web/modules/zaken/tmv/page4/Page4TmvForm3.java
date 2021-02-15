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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;

public class Page4TmvForm3 extends ReadOnlyForm {

  private static final String MELDING       = "melding";
  private static final String VERSTUURDDOOR = "verstuurddoor";

  public Page4TmvForm3(TerugmeldingAanvraag tmv) {

    setCaption("Externe registratie");
    setOrder(MELDING, VERSTUURDDOOR);
    setColumnWidths("200px", "");

    Page4TmvBean3 b = new Page4TmvBean3();

    b.setMelding(tmv.getRegistratieTmv().getToelichtingOmschrijving());
    b.setVerstuurddoor(tmv.getRegistratieTmv().getUsr().toString());

    setBean(b);
  }
}
