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

package nl.procura.gba.web.components.fields;

import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search.favorieten.FavorietenWindow;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.BsnField;

public class GbaBsnField extends BsnField {

  public GbaBsnField() {
    final UpArrowAction upAction = new UpArrowAction(this);
    addListener((FocusListener) event -> GbaBsnField.this.addShortcutListener(upAction));
    addListener((BlurListener) event -> GbaBsnField.this.removeShortcutListener(upAction));
  }

  public GbaBsnField(String caption) {
    super(caption);
  }

  protected static class UpArrowAction extends ShortcutListener {

    private final AbstractTextField field;

    public UpArrowAction(AbstractTextField field) {
      super(Globalfunctions.astr(field.hashCode()), 38, new int[0]);
      this.field = field;
    }

    public void handleAction(Object sender, Object target) {
      GbaApplication app = (GbaApplication) this.field.getWindow().getApplication();
      if (app.getServices().isTestomgeving()) {
        app.getParentWindow().addWindow(new FavorietenWindow(nr -> {
          field.setValue(nr);
          field.focus();
        }));
      }
    }
  }
}
