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

package nl.procura.gba.web.modules.bs.common.pages.documentpage.page1;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.bs.common.pages.documentpage.page2.Page2BsDocument;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1BsDocument extends ButtonPageTemplate {

  private final Dossier        dossier;
  private Table                table;
  private final ChangeListener listener;

  public Page1BsDocument(Dossier dossier, ChangeListener listener) {
    this.dossier = dossier;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew, buttonDel, buttonClose);
      getButtonLayout().setExpandRatio(buttonDel, 1f);
      getButtonLayout().setWidth("100%");

      setInfo("Dubbelklik op een regel van de tabel om het document te bekijken.");
      setSpacing(true);

      this.table = new Table();
      addComponent(table);
      table.focus();

    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<DMSDocument>(table) {

      @Override
      public void deleteValue(DMSDocument record) {
        getServices().getDmsService().delete(record);
      }

      @Override
      protected void afterDelete() {
        listener.onChange();
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2BsDocument(dossier, listener));
    super.onNew();
  }

  public class Table extends DocumentenTabel {

    @Override
    public DMSResult getOpgeslagenBestanden() {
      return getServices().getDmsService().getDocumentsByZaak(dossier);
    }
  }
}
