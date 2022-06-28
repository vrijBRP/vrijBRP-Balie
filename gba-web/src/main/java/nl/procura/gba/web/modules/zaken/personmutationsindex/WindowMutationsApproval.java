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

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.modules.zaken.personmutationsindex.page1.Page1MutationsIndex.ApprovalElement;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class WindowMutationsApproval extends GbaModalWindow {

  private final List<ApprovalElement> elements;

  public WindowMutationsApproval(List<ApprovalElement> elements) {
    super("Resultaat mutatie goedkeuring (Escape om te sluiten)", "900px");
    this.elements = elements;
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
      addComponent(new Table());
    }
  }

  public class Table extends GbaTable {

    public Table() {
      setClickable(true);
    }

    @Override
    public void setColumns() {
      addColumn("Nr", 20);
      addColumn("Naam", 200);
      addColumn("Resultaat");
      super.setColumns();
    }

    @Override
    public void onClick(Record record) {
      ApprovalElement approvalElement = record.getObject(ApprovalElement.class);
      WindowMutationsApprovalLog window = new WindowMutationsApprovalLog(approvalElement);
      getApplication().getParentWindow().addWindow(window);
      super.onClick(record);
    }

    @Override
    public void setRecords() {
      int nr = 0;
      for (ApprovalElement element : elements) {
        Record record = addRecord(element);
        record.addValue(++nr);
        record.addValue(element.getMutationRestElement().getName().getWaarde());
        List<String> output = Arrays.asList(element.getResponseRestElement().getOutput().getWaarde().split("\n"));
        record.addValue(output.get(output.size() - 1));
      }
    }
  }
}
