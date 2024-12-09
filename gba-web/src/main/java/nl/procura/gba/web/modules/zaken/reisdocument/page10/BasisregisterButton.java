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
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.buttons.GbaButton;
import nl.procura.gba.web.modules.zaken.reisdocument.basisregister.VrsBasisregisterWindow;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.vaadin.component.window.Message;

@Slf4j
public class BasisregisterButton extends GbaButton {

  private final Supplier<BasePLExt>                                     pl;
  private final Supplier<Aanvraagnummer>                                aanvraagnummer;
  private final Runnable                                                updateListener;
  private       ReisdocumentInformatiePersoonsGegevensInstantieResponse response;

  public BasisregisterButton(Supplier<BasePLExt> pl, Supplier<Aanvraagnummer> aanvraagnummer, Runnable updateListener) {
    super("Basisregister");
    this.pl = pl;
    this.aanvraagnummer = aanvraagnummer;
    this.updateListener = updateListener;
  }

  @Override
  public void attach() {
    if (response == null) {
      updateCaption();
      addListener((Button.ClickListener) event -> {
        if (response != null) {
          getWindow().addWindow(new VrsBasisregisterWindow(pl.get(), aanvraagnummer.get()) {
            @Override
            public void closeWindow() {
              updateCaption();
              if (updateListener != null) {
                updateListener.run();
              }
              super.closeWindow();
            }
          });
        }
      });
    }

    super.attach();
  }

  private void updateCaption() {
    response = null;
    setCaption(String.format("Basisregister (%d)", count()));
  }

  private Integer count() {
    return getResponse()
        .map(ReisdocumentInformatiePersoonsGegevensInstantieResponse::getReisdocumentenLijst)
        .map(List::size)
        .orElse(0);
  }

  private Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> getResponse() {
    if (response == null) {
      VrsService vrs = getApplication().getServices().getReisdocumentService().getVrsService();
      try {
        this.response = vrs.getReisdocumenten(pl.get(), aanvraagnummer.get()).orElse(null);
      } catch (RuntimeException e) {
        new Message(getWindow(), "Fout bij het raadplegen van het basisregister", TYPE_ERROR_MESSAGE);
      }
    }
    return Optional.ofNullable(response);
  }
}
