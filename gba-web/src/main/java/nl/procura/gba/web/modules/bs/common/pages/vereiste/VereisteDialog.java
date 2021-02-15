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

package nl.procura.gba.web.modules.bs.common.pages.vereiste;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class VereisteDialog extends ModalWindow implements Button.ClickListener {

  private final Button             yes     = new Button("Ja", this);
  private final Button             no      = new Button("Nee", this);
  private final Button             unknown = new Button("Onbekend (Esc)", this);
  private final TextArea           textArea;
  private BurgerlijkeStandVereiste vereiste;
  private GbaTable                 table;

  public VereisteDialog(String msg, BurgerlijkeStandVereiste vereiste, GbaTable table) {

    this("Weet u het zeker?", msg, "500px");

    this.vereiste = vereiste;
    this.table = table;

    textArea.setValue(vereiste.getOverruleReason());
  }

  private VereisteDialog(String caption, String msg, String width) {

    setCaption(caption);
    setWidth(width);

    HorizontalLayout h = new HorizontalLayout();
    h.setSizeFull();
    h.setSpacing(true);

    Embedded em = new Embedded(null, new ThemeResource(ProcuraTheme.ICOON_24.QUESTION));
    h.addComponent(em);

    Label helpText = new Label(msg, Label.CONTENT_XHTML);
    helpText.setSizeFull();
    h.addComponent(helpText);

    h.setExpandRatio(helpText, 1f);
    addComponent(h);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSizeFull();
    buttons.setSpacing(true);

    yes.setSizeFull();
    no.setSizeFull();
    unknown.setSizeFull();

    buttons.addComponent(yes);
    buttons.addComponent(no);
    buttons.addComponent(unknown);

    addComponent(buttons);

    textArea = new TextArea();
    textArea.focus();
    textArea.setSizeFull();
    textArea.setHeight("50px");

    addComponent(new Ruler());
    addComponent(new Fieldset("Waarom wordt deze waarde eigenlijk overruled? / Toelichting", textArea));

    VerticalLayout v = (VerticalLayout) getContent();
    v.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    v.setComponentAlignment(h, Alignment.MIDDLE_CENTER);
    v.setSpacing(true);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == yes) {
      buttonYes();
    } else if (event.getButton() == no) {
      buttonNo();
    } else if (event.getButton() == unknown) {
      buttonUnknown();
    }
  }

  private void buttonNo() {

    set("N");

    close();
  }

  private void buttonUnknown() {

    set("");

    close();
  }

  private void buttonYes() {

    set("J");

    close();
  }

  private void set(String value) {

    vereiste.setVoldoet(value);

    String taValue = astr(textArea.getValue());

    if (!vereiste.getVoldoet().equals(vereiste.getDefaultVoldoet())) {
      if (emp(taValue)) {
        if ("J".equalsIgnoreCase(vereiste.getVoldoet())) {
          throw new ProException(ProExceptionType.ENTRY, ProExceptionSeverity.INFO,
              "Geef in de toelichting aan waarom er wél is voldaan aan deze voorwaarde.");
        } else if ("N".equalsIgnoreCase(vereiste.getVoldoet())) {
          throw new ProException(ProExceptionType.ENTRY, ProExceptionSeverity.INFO,
              "Geef in de toelichting aan waarom er niet is voldaan aan deze voorwaarde.");
        } else {
          throw new ProException(ProExceptionType.ENTRY, ProExceptionSeverity.INFO,
              "Geef in de toelichting aan waarom niet bekend is waarom er aan deze voorwaarde is voldaan.");
        }
      }
    }

    vereiste.setOverruleReason(taValue);

    table.init();
  }
}
