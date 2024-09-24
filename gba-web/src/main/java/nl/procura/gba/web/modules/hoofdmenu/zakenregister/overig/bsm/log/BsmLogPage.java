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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.log;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.MELDING;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.RESULTAAT;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.STATUS;

import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLog;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenForm;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsmLogPage extends NormalPageTemplate {

  protected static final int     DEFAULT_SET_SIZE = 25;
  private final List<BsmRestLog> logs;
  private final BsmUitvoerenBean progressBean;

  public BsmLogPage(List<BsmRestLog> logs, BsmUitvoerenBean progressBean) {
    this.logs = logs;
    this.progressBean = progressBean;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      if (getNavigation().getPreviousPage() == null) {
        addButton(buttonClose);

      } else {
        addButton(buttonPrev);
      }

      if (!logs.isEmpty()) {
        if (logs.get(0).getSubLogs() != null) {
          addComponent(getProgressForm());
          addComponent(new InfoLayout("Ter informatie", "Klik op een regel voor meer informatie"));

        } else {
          addComponent(getProgressForm());
        }
      }

      BsmLogTable table = new BsmLogTable(logs) {

        @Override
        public int getPageLength() {
          int count = getRecords().size();
          return (count < 2) ? 2 : (count < DEFAULT_SET_SIZE) ? count + 1 : DEFAULT_SET_SIZE;
        }

        @Override
        protected void goToPage(List<BsmRestLog> logs) {
          getNavigation().goToPage(new BsmLogPage(logs, progressBean));
        }
      };

      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private BsmUitvoerenForm getProgressForm() {
    BsmUitvoerenForm progressForm = new BsmUitvoerenForm("Verwerking door de taakplanner", false,
        STATUS, RESULTAAT, MELDING);
    progressForm.setBean(progressBean);
    return progressForm;
  }
}
