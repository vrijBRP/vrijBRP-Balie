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

package nl.procura.gba.web.modules.beheer.parameters.layout.database;

import static nl.procura.vaadin.theme.twee.Icons.ICON_ERROR;
import static nl.procura.vaadin.theme.twee.Icons.ICON_OK;

import java.io.File;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.applicatie.DatabaseConfig;
import nl.procura.vaadin.theme.twee.Icons;

public class DatabaseTable extends GbaTable {

  public boolean isConnectionOK(DatabaseConfig db) {

    String type = db.getType();
    String server = db.getServer();
    String port = db.getPort();
    String schema = db.getSchema();
    String sid = db.getSid();
    String username = db.getUsername();
    String pw = db.getPassword();

    try {
      DatabaseConfig config = new DatabaseConfig(type, server, port, schema, sid, username, pw);
      getApplication().getServices().getOnderhoudService().checkConnection(config);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void onDoubleClick(Record record) {
    showPropertiesFile(record.getObject(File.class));
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Type");
    addColumn("Server");
    addColumn("Poort");
    addColumn("Schema");
    addColumn("ID");
    addColumn("Gebruikersnaam");
    addColumn("Wachtwoord");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (DatabaseConfig bean : getApplication().getServices().getOnderhoudService().getDatabases()) {

      Record r = addRecord(bean.getFile());

      r.addValue(new TableImage(Icons.getIcon(isConnectionOK(bean) ? ICON_OK : ICON_ERROR)));
      r.addValue(bean.getType());
      r.addValue(bean.getServer());
      r.addValue(bean.getPort());
      r.addValue(bean.getSchema());
      r.addValue(bean.getSid());
      r.addValue(bean.getUsername());
      r.addValue(bean.getPassword());
    }

    super.setRecords();
  }

  public void showPropertiesFile(File propertiesFile) {

    getApplication().getParentWindow().addWindow(new DatabaseWindow(propertiesFile) {

      @Override
      public void closeWindow() {

        init();

        super.closeWindow();
      }
    });

  }
}
