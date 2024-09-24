/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.standard.exceptions.ProException;

public class BasisregisterButton extends Button {

  public BasisregisterButton(Aanvraagnummer aanvraagnummer,
      ReisdocumentInformatiePersoonsGegevensInstantieResponse response) {
    super("");
    setCaption(String.format("Basisregister (%d)", count(response)));
    addListener((Button.ClickListener) event -> {
      if (count(response) == 0) {
        throw new ProException(INFO, "Geen documenten gevonden");
      }
      getWindow().addWindow(new VrsDocumentenWindow(aanvraagnummer, response));
    });
  }

  private static int count(ReisdocumentInformatiePersoonsGegevensInstantieResponse response) {
    return response != null && response.getReisdocumentenLijst() != null ? response.getReisdocumentenLijst().size() : 0;
  }
}
