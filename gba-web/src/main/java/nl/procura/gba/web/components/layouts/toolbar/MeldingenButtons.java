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

package nl.procura.gba.web.components.layouts.toolbar;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.SYSTEM;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.TASK;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.WORK;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;
import java.util.stream.Stream;

import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Window;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.gba.web.modules.account.meldingen.MeldingenWindow;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.applicatie.meldingen.MeldingService;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory;
import nl.procura.gba.web.windows.home.HomeWindow;

public class MeldingenButtons extends GbaHorizontalLayout {

  private final Button         meldingenButton0;
  private final Button         meldingenButton1;
  private final Button         meldingenButton2;
  private final Button         meldingenButton3;
  private final Button         taskButton;
  private final GbaApplication application;

  public MeldingenButtons(GbaApplication application, Window window) {

    this.application = application;
    setStyleName("melding-layout");

    meldingenButton0 = new NativeButton("Geen meldingen", (Button.ClickListener) event -> {
      application.getMainWindow().addWindow(new MeldingenWindow(FAULT));
    });

    meldingenButton1 = new NativeButton("", (Button.ClickListener) event -> {
      application.getMainWindow().addWindow(new MeldingenWindow(FAULT));
    });

    meldingenButton2 = new NativeButton("", (Button.ClickListener) event -> {
      application.getMainWindow().addWindow(new MeldingenWindow(SYSTEM));
    });

    meldingenButton3 = new NativeButton("", (Button.ClickListener) event -> {
      application.getMainWindow().addWindow(new MeldingenWindow(WORK));
    });

    taskButton = new NativeButton("", (Button.ClickListener) event -> {
      application.getMainWindow().addWindow(new MeldingenWindow(TASK));
    });

    if (window.getName().equals(HomeWindow.NAME)) {
      application.getServices().getServices().forEach(AbstractService::check);
    }

    meldingenButton0.setHtmlContentAllowed(true);
    meldingenButton0.setCaption("<span class='melding-ok'>Geen meldingen</span>");
    meldingenButton0.setStyleName("melding-button");
    meldingenButton1.setStyleName("melding-button");
    meldingenButton2.setStyleName("melding-button");
    meldingenButton3.setStyleName("melding-button");
    taskButton.setStyleName("melding-button");
    taskButton.setHtmlContentAllowed(true);

    addComponent(taskButton);
    addComponent(meldingenButton0);
    addComponent(meldingenButton1);
    addComponent(meldingenButton2);
    addComponent(meldingenButton3);

    update();
    showMessages();
  }

  private void showMessages() {
    if (HomeWindow.NAME.equalsIgnoreCase(application.getMainWindow().getName())) {
      MeldingService meldingService = application.getServices().getMeldingService();
      if (!meldingService.isMessagePopupShown()) {
        if (meldingService.isShowMessagesPopup()) {
          meldingService.disableMessagePopupShown();
          Stream.of(meldingenButton1, meldingenButton2, meldingenButton3)
              .filter(component -> component.getParent() != null)
              .findFirst()
              .ifPresent(Button::click);
        }
      }
    }
  }

  public void update() {
    if (application != null) {
      removeAllComponents();
      updateMeldingButton();
      updateTaakButton();
    }
  }

  private void updateMeldingButton() {
    boolean b1 = updateButton("Fouten", meldingenButton1, FAULT);
    boolean b2 = updateButton("Systeem", meldingenButton2, SYSTEM);
    boolean b3 = updateButton("Herinnering", meldingenButton3, WORK);
    if (!(b1 || b2 || b3)) {
      add(meldingenButton0);
    }
  }

  private boolean updateButton(String type, Button button, ServiceMeldingCategory category) {
    MeldingService service = application.getServices().getMeldingService();
    List<ServiceMelding> meldingen = service.getMeldingen(category);

    long errors = meldingen.stream().filter(m -> ERROR.equals(m.getSeverity())).count();
    long warnings = meldingen.stream().filter(m -> WARNING.equals(m.getSeverity())).count();

    String bCaption = "";
    if (errors > 0) {
      bCaption = "<span class='melding-error'>" + type + "</span>";
    } else if (warnings > 0) {
      bCaption = "<span class='melding-warning'>" + type + "</span>";
    }

    button.setHtmlContentAllowed(true);
    button.setCaption(bCaption);

    if (errors + warnings > 0) {
      addComponent(button);
    }

    return errors + warnings > 0;
  }

  private void updateTaakButton() {
    taskButton.setCaption("<span class='melding-ok'>Geen taken</span>");
    Services services = application.getServices();
    int countT = services.getTaskService().getOpenUserTasks().size();
    if (countT > 0) {
      addComponent(taskButton);
    }

    if (countT == 1) {
      taskButton.setCaption("<span class='melding-warning'>1 taak</span>");

    } else if (countT > 1) {
      taskButton.setCaption("<span class='melding-warning'>" + countT + " taken</span>");
    }
  }
}
