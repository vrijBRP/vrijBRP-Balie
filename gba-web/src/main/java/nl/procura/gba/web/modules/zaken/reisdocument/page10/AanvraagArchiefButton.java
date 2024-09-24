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

import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.standard.exceptions.ProException;

public class AanvraagArchiefButton extends Button {

  private final ControleAanvragenResponse response;
  private final boolean                   autoOpen;
  private       boolean                   opened = false;

  public AanvraagArchiefButton(Aanvraagnummer aanvraagnummer, ControleAanvragenResponse response, boolean autoOpen) {
    super("");
    this.response = response;
    this.autoOpen = autoOpen;
    setCaption(String.format("Aanvraagarchief (%d)", count(response)));
    addListener((ClickListener) event -> {
      if (count(response) == 0) {
        throw new ProException(INFO, "Geen aanvragen gevonden");
      }
      getWindow().addWindow(new VrsAanvragenWindow(aanvraagnummer, response));
    });
  }

  @Override
  public void attach() {
    if (autoOpen && !opened && count(response) > 0) {
      opened = true;
      click();
    }
    super.attach();
  }

  private static int count(ControleAanvragenResponse response) {
    return response != null && response.getAanvragen() != null ? response.getAanvragen().size() : 0;
  }
}
