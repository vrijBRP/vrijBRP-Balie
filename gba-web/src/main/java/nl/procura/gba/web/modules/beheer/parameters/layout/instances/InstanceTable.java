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

package nl.procura.gba.web.modules.beheer.parameters.layout.instances;

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.vaadin.theme.twee.Icons.ICON_ERROR;
import static nl.procura.vaadin.theme.twee.Icons.ICON_OK;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.App;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.vaadin.theme.twee.Icons;

public class InstanceTable extends GbaTable {

  @Override
  public void onDoubleClick(Record record) {
    showApp(record.getObject(Application.class));
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Actief", 50);
    addColumn("Omschrijving", 250);
    addColumn("Url", 250);
    addColumn("Gebruiker", 400).setUseHTML(true);
    addColumn("Synchroniseren");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (Application app : getApplication().getServices().getOnderhoudService().getApps(true)) {

      App sync = app.getEntity();
      Record r = addRecord(app);

      // Problems info
      StringBuilder problemInfo = new StringBuilder();
      if (!app.isLoginOk()) {
        problemInfo.append(MiscUtils.setClass(false, " (" + app.getProblem() + ")"));
      }

      // Synchronisation info
      StringBuilder syncInfo = new StringBuilder();
      if (app.getAttributes().isSyncUsers()) {
        syncInfo.append(", gebruikers");
      }

      r.addValue(new TableImage(Icons.getIcon(app.isLoginOk() ? ICON_OK : ICON_ERROR)));
      r.addValue(pos(sync.getActive()) ? "Ja" : "Nee");
      r.addValue(sync.getName());
      r.addValue(sync.getUrl());
      r.addValue(sync.getUsername() + problemInfo.toString());
      r.addValue(emp(syncInfo.toString()) ? "Niets" : trim(StringUtils.capitalize(syncInfo.toString())));
    }

    super.setRecords();
  }

  public void showApp(Application app) {

    InstanceWindow instanceWindow = new InstanceWindow(app) {

      @Override
      public void closeWindow() {
        init();
        super.closeWindow();
      }
    };

    getApplication().getParentWindow().addWindow(instanceWindow);
  }
}
