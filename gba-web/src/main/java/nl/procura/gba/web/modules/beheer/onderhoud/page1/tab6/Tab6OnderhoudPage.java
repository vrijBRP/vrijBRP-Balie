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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab6;

import java.util.Date;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.gba.web.services.gba.tabellen.TabelResultaat;
import nl.procura.gba.web.services.gba.tabellen.TabellenService;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab6OnderhoudPage extends OnderhoudTabPage {

  private final Table  table         = new Table();
  private final Button buttonRefresh = new Button("Herlaad de gegevens (F3)");

  public Tab6OnderhoudPage() {

    super("Geladen gegevens");

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonRefresh);

      addComponent(new Fieldset("Gegevens"));
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F3, buttonRefresh)) {
      reload();
    }

    super.handleEvent(button, keyCode);
  }

  private String getDag(long time) {
    return GbaDatumUtils.getDuration(new Date().getTime() - time) + " geleden";
  }

  private void reload() {

    TabellenService tabellen = getServices().getTabellenService();
    tabellen.clearTabellen();
    table.init();
  }

  private class Table extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Laatst geladen", 130);
      addColumn("Naam");
    }

    @Override
    public void setRecords() {

      try {

        for (TabelResultaat tabel : getServices().getTabellenService().getTabellen()) {

          Record record = addRecord(tabel);
          record.addValue(getDag(tabel.getTijdstip()));
          record.addValue(tabel.getOmschrijving());
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
