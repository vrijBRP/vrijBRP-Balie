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

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;

import com.vaadin.ui.Button;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.buttons.GbaButton;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.vaadin.component.window.Message;

@Slf4j
public class AanvraagArchiefButton extends GbaButton {

  private final BasePLExt                 pl;
  private final Aanvraagnummer            aanvraagnummer;
  private final boolean                   autoOpen;
  private       boolean                   opened   = false;
  private       ControleAanvragenResponse response;

  public AanvraagArchiefButton(BasePLExt pl, Aanvraagnummer aanvraagnummer, boolean autoOpen) {
    super("Aanvraagarchief");
    this.pl = pl;
    this.aanvraagnummer = aanvraagnummer;
    this.autoOpen = autoOpen;
  }

  @Override
  public void attach() {
    if (response == null) {
      updateCaption();
      addListener((Button.ClickListener) event -> {
        if (response != null) {
          getWindow().addWindow(new VrsAanvragenWindow(response));
        }
      });
      if (autoOpen && !opened && count() > 0) {
        opened = true;
        click();
      }
    }
    super.attach();
  }

  private void updateCaption() {
    response = null;
    setCaption(String.format("Aanvraagarchief (%d)", count()));
  }

  private Integer count() {
    return getResponse()
        .map(ControleAanvragenResponse::getAanvragen)
        .map(List::size)
        .orElse(0);

  }

  private Optional<ControleAanvragenResponse> getResponse() {
    if (response == null) {
      try {
        VrsService vrs = getApplication().getServices().getReisdocumentService().getVrsService();
        this.response = vrs.getAanvragen(pl, aanvraagnummer).orElse(null);
      } catch (RuntimeException e) {
        new Message(getWindow(), "Fout bij het raadplegen van de aanvragen in het basisregister", TYPE_ERROR_MESSAGE);
      }
    }
    return Optional.ofNullable(response);
  }
}
