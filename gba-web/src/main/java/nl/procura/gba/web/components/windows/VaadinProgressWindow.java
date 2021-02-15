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

package nl.procura.gba.web.components.windows;

import java.util.List;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;

public abstract class VaadinProgressWindow<T> extends GbaModalWindow {

  private final Refresher refresher  = new Refresher();
  private final Label     label      = new Label("", Label.CONTENT_XHTML);
  private final List<T>   list;
  private double          percentage = 0;
  private int             nr         = 0;

  public VaadinProgressWindow(List<T> list) {

    super("De zaken worden geladen ...", "220px");

    setBorder(Window.BORDER_NONE);

    this.list = list;
    refresher.addListener(new ActionRefreshListener());
    refresher.setRefreshInterval(1000);

    VerticalLayout v = new VerticalLayout();
    v.setSizeFull();
    v.setWidth("100%");
    v.setSpacing(true);

    label.setHeight("50px");
    label.setWidth("200px");

    v.addComponent(label);
    v.addComponent(refresher);
    v.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    v.setExpandRatio(label, 1f);

    setContent(v);
  }

  public abstract void process(T object);

  private class ActionRefreshListener implements RefreshListener {

    @Override
    public void refresh(Refresher source) {

      if ((nr + 1) < list.size()) {

        System.out.println("Process: " + nr);

        process(list.get(nr));

        nr++;
      } else {

        closeWindow();
      }

      percentage = (100.00 / list.size()) * (nr + 1);

      System.out.println(percentage);

      label.setValue((int) percentage + " %");
    }
  }
}
