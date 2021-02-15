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

package nl.procura.gba.web.application;

import com.vaadin.ui.Window;

import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.theme.twee.window.LoginWindow3;

/**
 * Onderschept het wijzigen van de pagina
 */
public class ProcessChangeInterceptor {

  private ProcessInterceptor   process       = null;
  private Window               currentwindow = null;
  private AccordionLink        link          = null;
  private WindowChangeListener wcl           = null;

  public ProcessChangeInterceptor(Window currentwindow, AccordionLink link) {
    this.currentwindow = currentwindow;
    this.link = link;
  }

  public ProcessChangeInterceptor(WindowChangeListener wcl) {
    this.wcl = wcl;
  }

  public AccordionLink getLink() {
    return link;
  }

  public void setLink(AccordionLink link) {
    this.link = link;
  }

  public ProcessInterceptor getProcess() {
    return process;
  }

  public void setProcess(ProcessInterceptor process) {
    this.process = process;
  }

  public void intercept() {

    if (wcl != null) {
      interceptWindow();
    } else {
      interceptPage();
    }
  }

  @SuppressWarnings("unused")
  protected void proceed(AccordionLink link) {
  } // Override

  @SuppressWarnings("unused")
  protected void proceed(WindowChangeListener wcl) {
  } // Override

  private void interceptPage() {

    boolean isCurrentWindow = (currentwindow != null);

    if (process.isProcess() && isCurrentWindow) {
      currentwindow.addWindow(new Dialog());
    } else {
      proceed(link);
    }
  }

  private void interceptWindow() {

    boolean isCurrentWindow = (wcl.getCurrentWindow() != null);
    boolean isNewWindow = (wcl.getNewWindow() != null);
    boolean isLoginWindow = (wcl.getCurrentWindow() instanceof LoginWindow3);

    if (process.isProcess() && isCurrentWindow && isNewWindow && !isLoginWindow) {
      wcl.getCurrentWindow().addWindow(new Dialog());
    } else {
      proceed(wcl);
    }
  }

  public class Dialog extends ConfirmDialog {

    public Dialog() {
      super("Het (aanvraag)proces is nog niet voltooid.<br/>Wilt u de pagina verlaten?", 350);
    }

    @Override
    public void buttonYes() {

      process.endProcess();
      proceed(wcl);
      proceed(link);

      super.buttonYes();
    }
  }
}
