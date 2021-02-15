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

package nl.procura.gba.web.components.layouts.proces;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.PageNavigation;

import lombok.Data;
import lombok.Getter;

public class ProcessLayout {

  private final List<ProcessButton> buttons = new ArrayList<>();
  private final HLayout             hLayout = new HLayout();
  private final MainModuleContainer moduleContainer;

  @Getter
  private final VLayout outerLayout;

  @Getter
  private final VLayout innerLayout;

  public ProcessLayout() {
    hLayout.setMargin(false);
    hLayout.setWidth("100%");

    outerLayout = new VLayout();
    outerLayout.setSpacing(true);
    outerLayout.setMargin(false);
    outerLayout.setWidth("210px");

    innerLayout = new VLayout();
    innerLayout.setStyleName("bslayout");
    innerLayout.setWidth("100%");
    innerLayout.setMargin(false);

    moduleContainer = new MainModuleContainer();

    outerLayout.addComponent(innerLayout);
    VLayout contentLayout = new VLayout();
    contentLayout.addComponent(outerLayout);
    contentLayout.addExpandComponent(moduleContainer);

    hLayout.addComponent(outerLayout);
    hLayout.addExpandComponent(contentLayout);
  }

  public PageNavigation getNavigation() {
    return moduleContainer.getNavigation();
  }

  public ProcessButton addButton(String caption, ButtonListener listener) {
    ProcessButton processButton = new ProcessButton(caption, listener);
    buttons.add(processButton);
    innerLayout.add(processButton.getButton());
    return processButton;
  }

  public List<ProcessButton> getButtons() {
    return buttons;
  }

  public HLayout getLayout() {
    return hLayout;
  }

  public void setWidths(String outerWidth, String innerWidth) {
    outerLayout.setWidth(outerWidth);
    innerLayout.setWidth(innerWidth);
  }

  public interface ButtonListener {

    void onClick(ProcessButton button);
  }

  @Data
  public class ProcessButton {

    private Button         button;
    private String         caption;
    private ButtonListener listener;

    ProcessButton(String caption, ButtonListener listener) {
      this.caption = caption;
      this.listener = listener;
      this.button = new Button();

      button.setCaption(caption);
      button.setWidth("100%");
      button.setStyleName(buttons.isEmpty() ? "current" : "");
      button.addListener((Button.ClickListener) event -> onClick());
    }

    public void onClick() {
      listener.onClick(this);
      enable();
    }

    public void enable() {
      buttons.forEach(b -> b.getButton().setStyleName(""));
      button.setStyleName("current");
    }
  }
}
