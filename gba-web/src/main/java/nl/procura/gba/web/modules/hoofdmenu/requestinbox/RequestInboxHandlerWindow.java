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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;

public class RequestInboxHandlerWindow extends GbaModalWindow {

  private boolean                   loaded = false;
  private final GbaNativeSelect     select = new GbaNativeSelect();
  private final Consumer<Gebruiker> gebruikerConsumer;

  public RequestInboxHandlerWindow(Consumer<Gebruiker> gebruikerConsumer) {
    super("Behandelaar", "340px");
    this.gebruikerConsumer = gebruikerConsumer;
  }

  @Override
  public void attach() {
    if (!loaded) {
      loaded = true;
      HLayout hLayout = new HLayout().spacing(true).widthFull().height(null).margin(false);
      select.setContainerDataSource(new ArrayListContainer(getGebruikers()));
      select.setImmediate(true);
      select.setWidth("100%");
      select.setNullSelectionAllowed(true);

      VLayout vLayout = new VLayout().margin(true);
      hLayout.addComponent(select);
      hLayout.setExpandRatio(select, 1F);
      hLayout.addComponent(new Button("Opslaan", event -> {
        try {
          select.validate();
          Gebruiker gebruiker = (Gebruiker) select.getValue();
          if (gebruiker == null) {
            getGbaApplication().getParentWindow().addWindow(new ConfirmDialog("Geen behandelaar geselecteerd",
                "Weet u zeker dat u geen behandelaar wilt selecteren?",
                "400px") {

              @Override
              public void buttonYes() {
                gebruikerConsumer.accept(null);
                closeWindow();
                RequestInboxHandlerWindow.this.closeWindow();
              }
            });
          } else {
            gebruikerConsumer.accept(gebruiker);
            closeWindow();
          }
        } catch (RuntimeException e) {
          throw new ProException(ProExceptionSeverity.ERROR, e.getMessage());
        }
      }));

      Fieldset fieldset = new Fieldset("Selecteer de nieuwe behandelaar");
      fieldset.setWidth("100%");
      vLayout.addComponent(fieldset);
      vLayout.addComponent(hLayout);
      addComponent(vLayout);
    }

    super.attach();
  }

  private List<Gebruiker> getGebruikers() {
    return getGbaApplication().getServices().getGebruikerService().getGebruikers(false).stream()
        .filter(gebruiker -> isNotBlank(gebruiker.getNaam()))
        .collect(toList());
  }
}
