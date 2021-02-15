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

package nl.procura.gba.web.components.layouts.page;

import static nl.procura.standard.Globalfunctions.fil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.window.Message;

public class MainButtons extends GbaHorizontalLayout implements ClickListener {

  private final HorizontalLayout headerLayout = new HorizontalLayout();
  private final H2               h2Title;
  private String                 title        = "";

  public MainButtons(String title) {

    setTitle(title);
    setStyleName("mainbuttons");
    setWidth("100%");

    h2Title = new H2(getTitle());
    headerLayout.addComponent(h2Title);
    headerLayout.setSpacing(true);
    headerLayout.setComponentAlignment(h2Title, Alignment.MIDDLE_LEFT);

    addComponent(headerLayout);
    setExpandRatio(headerLayout, 1);
  }

  public void addSubTitel(final String subTitel) {

    if (fil(getTitle()) && fil(subTitel)) {
      Button classNameButton = new Button(" ( " + subTitel + " )");
      classNameButton.setStyleName(GbaWebTheme.BUTTON_LINK);
      classNameButton.addStyleName("title-file");
      classNameButton.addListener((ClickListener) event -> onClipBtn(subTitel));

      headerLayout.addComponent(classNameButton, headerLayout.getComponentIndex(h2Title) + 1);
      headerLayout.setComponentAlignment(classNameButton, Alignment.MIDDLE_LEFT);
    }
  }

  @Override
  public void buttonClick(ClickEvent event) {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  private void onClipBtn(String subTitel) {
    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable tText = new StringSelection(subTitel);
    clip.setContents(tText, null);
    new Message(getWindow(), "'" + subTitel + "' gekopieërd naar clipboard", Message.TYPE_INFO);
  }
}
