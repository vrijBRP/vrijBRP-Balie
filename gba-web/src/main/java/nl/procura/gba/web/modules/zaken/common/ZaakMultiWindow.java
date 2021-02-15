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

package nl.procura.gba.web.modules.zaken.common;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.common.misc.Matrix;
import nl.procura.gba.web.common.misc.Matrix.Element;
import nl.procura.gba.web.common.misc.Matrix.MOVES;
import nl.procura.gba.web.components.layouts.page.ArrowKeylistenerHandler;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.HomeWindow;

public abstract class ZaakMultiWindow extends GbaModalWindow {

  private final Matrix<Keuze> matrix = new Matrix<>(2);

  public ZaakMultiWindow(String caption) {
    super(caption + " (Escape om te sluiten)", "400px");
  }

  @Override
  public void attach() {

    VerticalLayout v = new VerticalLayout();
    v.setMargin(true);

    GridLayout gl = new GridLayout(1, 1);
    gl.setSizeFull();
    gl.setSpacing(true);

    setKeuzes();

    if (matrix.getElements().size() > 0) {

      for (Element<Keuze> e : matrix.getElements()) {
        gl.addComponent(e.getObject());
      }

      v.addComponent(gl);

      setContent(v);

      ArrowKeylistenerHandler keylistenerHandler = new ArrowKeylistenerHandler() {

        @Override
        public void handleKey(int keyCode) {

          if (keyCode == KeyCodes.KEY_UP) {
            matrix.move(MOVES.UP);
          } else if (keyCode == KeyCodes.KEY_RIGHT) {
            matrix.move(MOVES.RIGHT);
          } else if (keyCode == KeyCodes.KEY_DOWN) {
            matrix.move(MOVES.DOWN);
          } else if (keyCode == KeyCodes.KEY_LEFT) {
            matrix.move(MOVES.LEFT);
          }

          focusKeuze(matrix.getCurrentElement().getObject());
        }
      };

      addActionHandler(keylistenerHandler);

      focusKeuze(matrix.getCurrentElement().getObject());
    }

    super.attach();
  }

  protected void addKeuze(Keuze keuze) {
    matrix.add(keuze);
  }

  protected abstract void setKeuzes();

  private void focusKeuze(Keuze keuze) {

    for (Element<Keuze> k : matrix.getElements()) {

      if (k.getObject() == keuze) {

        k.getObject().focus();
      } else {

        k.getObject().setStyleName("");
      }
    }
  }

  @SuppressWarnings("unchecked")
  public abstract class FragmentKeuze extends Keuze {

    private final String fragment;

    public FragmentKeuze(String label, String fragment) {

      super(label);

      this.fragment = fragment;

      setSizeFull();

      addListener((Button.ClickListener) this);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      onClick();

      getGbaApplication().openWindow(getWindow().getParent(), new HomeWindow(), fragment);
    }

    @Override
    public void onClick() {

      getGbaApplication().openWindow(getWindow().getParent(), new HomeWindow(), fragment);
    }
  }

  @SuppressWarnings("unchecked")
  public abstract class Keuze extends Button implements ClickListener {

    public Keuze(String label) {

      super(label);
      setSizeFull();
      addListener((Button.ClickListener) this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      onClick();
    }

    public abstract void onClick();
  }
}
