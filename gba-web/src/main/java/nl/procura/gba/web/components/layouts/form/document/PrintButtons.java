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

package nl.procura.gba.web.components.layouts.form.document;

import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.document.sign.DocSignParameters;
import nl.procura.gba.web.components.layouts.form.document.sign.DocSignWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

import lombok.Getter;

@Getter
public class PrintButtons {

  private final Button     buttonPreview = new Button("Voorbeeld / e-mailen");
  private final Button     buttonPrint   = new Button("Afdrukken (F3)");
  private final ButtonSign buttonSign    = new ButtonSign();

  public PrintButtons() {
  }

  public Button[] getButtons(Zaak zaak) {
    if (zaak != null && Services.getInstance().getZynyoService().isZynyoEnabled()) {
      return new Button[]{ buttonPreview, buttonPrint, buttonSign };
    } else {
      return new Button[]{ buttonPreview, buttonPrint };
    }
  }

  public static class ButtonSign extends Button {

    private final DocSignParameters     parameters = new DocSignParameters();
    private Consumer<DocSignParameters> consumer;

    public ButtonSign() {
      super("Ondertekenen");
      updateCaption();
      addListener((ClickListener) event -> sign());
    }

    public void setParameterConsumer(Consumer<DocSignParameters> consumer) {
      this.consumer = consumer;
    }

    private void sign() {
      consumer.accept(parameters);
      if (parameters.getPrintRecords().isEmpty()) {
        throw new ProException(ProExceptionSeverity.WARNING, "Geen documenten geselecteerd.");
      }

      DocSignWindow window = new DocSignWindow(parameters);
      window.addListener((CloseListener) closeEvent -> updateCaption());
      ((GbaApplication) getApplication()).getParentWindow().addWindow(window);
    }

    private void updateCaption() {
      setCaption("Ondertekenen (" + parameters.getSignedDocuments().size() + ")");
    }
  }
}
