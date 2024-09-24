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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.functies.VaadinUtils;

import ch.lambdaj.collection.LambdaList;

public class Page8ZakenTabTemplate<T extends Zaak> extends ZakenregisterPage<T> {

  protected final Button  buttonStatus  = new Button("Status wijzigen");
  protected final Button  buttonRefresh = new Button("Herladen (F5)");
  private Page8ZakenTable table         = null;

  public Page8ZakenTabTemplate(String caption) {
    super(null, caption);
  }

  public Page8ZakenTable getTable() {
    return table;
  }

  public void setTable(Page8ZakenTable table) {
    this.table = table;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonStatus) {
      wijzigStatus();
    } else if (button == buttonRefresh || keyCode == KeyCode.F5) {
      reload();
    }

    super.handleEvent(button, keyCode);
  }

  public void reload() {

    getParentTab().reload();

    table.init();
  }

  protected Page8ZakenTab getParentTab() {
    return VaadinUtils.getParent(this, Page8ZakenTab.class);
  }

  @SuppressWarnings("unused")
  protected List<PrintRecord> getPrintRecords(boolean isPreview) {

    List<PrintRecord> records = new ArrayList<>();

    records.addAll(getTable().getSelectedValues(PrintRecord.class));

    if (records.isEmpty()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Geen documenten geselecteerd.");
    }

    return records;
  }

  private void reloadTree() {
    VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class).reloadTree();
  }

  private void wijzigStatus() {

    LambdaList<Zaak> zaken = with(table.getSelectedValues(PrintRecord.class)).extract(
        on(PrintRecord.class).getZaak());

    new ZaakStatusUpdater(getWindow(), zaken) {

      @Override
      protected void reload() {
        table.init();
        reloadTree();
      }
    };
  }
}
