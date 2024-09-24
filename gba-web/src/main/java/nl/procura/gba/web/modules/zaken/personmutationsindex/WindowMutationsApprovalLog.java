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

package nl.procura.gba.web.modules.zaken.personmutationsindex;

import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.CustomLayout;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.modules.zaken.personmutationsindex.page1.Page1MutationsIndex;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;

public class WindowMutationsApprovalLog extends GbaModalWindow {

  private final Page1MutationsIndex.ApprovalElement element;

  public WindowMutationsApprovalLog(Page1MutationsIndex.ApprovalElement element) {
    super("Log goedkeuring mutatie (Escape om te sluiten)", "500px");
    this.element = element;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new ModuleMutationsIndex()));
  }

  public class ModuleMutationsIndex extends ZakenModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new Page1());
      }
    }
  }

  public class Page1 extends NormalPageTemplate {

    public Page1() {
      try {
        setHeight("400px");
        CustomLayout layout = new CustomLayout(new ByteArrayInputStream(getContent().getBytes()));
        addComponent(new ScrollLayout(layout));
      } catch (IOException e) {
        throw new ProException(CONFIG, "Fout bij tonen log", e);
      }
    }

    private String getContent() {
      return buildFile(Arrays.asList(element.getResponseRestElement()
          .getOutput().getWaarde()
          .split("\n"))).toString();
    }

    private StringBuilder buildFile(List<String> lines) {
      StringBuilder log = new StringBuilder();
      log.append("<table style=\"width:100%\" class=\"logbestand\">");

      int index = 0;
      for (String line : lines) {
        log.append("<tr>");
        log.append("<td class=\"nr\">" + ++index + "</td>");
        log.append("<td class=\"line\">" + line + "</td>");
        log.append("</tr>");
      }

      log.append("</table>");
      return log;
    }
  }
}
