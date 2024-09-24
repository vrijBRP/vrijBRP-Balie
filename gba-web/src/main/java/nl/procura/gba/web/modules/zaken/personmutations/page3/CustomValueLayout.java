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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import java.util.function.Function;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class CustomValueLayout extends GbaHorizontalLayout {

  private PersonListMutElem              mutElem;
  private Function<Services, FieldValue> valueSupplier;

  public CustomValueLayout() {
    width("285px");
  }

  public CustomValueLayout(PersonListMutElem mutElem) {
    this();
    this.mutElem = mutElem;
    mutElem.getField().setWidth("275px");
    addComponent(mutElem.getField());
  }

  public CustomValueLayout(PersonListMutElem mutElem, Function<Services, FieldValue> valueSupplier) {
    this(mutElem);
    this.valueSupplier = valueSupplier;
    NativeButton button = new NativeButton();
    button.setHeight("23px");
    button.setWidth("20px");
    button.setStyleName(GbaWebTheme.BUTTON_LINK);
    button.addStyleName("pl-url-button");
    button.setIcon(new ThemeResource("../gba-web/buttons/img/pl-default.png"));
    button.setDescription("Vul de voorkeurswaarde in");
    add(button);

    button.addListener((ClickListener) event -> {
      setDefault();
    });

    height("24px");
    margin(false);
    spacing(true);
    widthFull();
  }

  public void setDefault() {
    if (valueSupplier != null) {
      mutElem.getField().setValue(valueSupplier.apply(getApplication().getServices()));
    }
    mutElem.getField().focus();
  }
}
