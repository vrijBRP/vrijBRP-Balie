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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.File;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.modules.beheer.parameters.form.ConfigParameterForm;
import nl.procura.gba.web.modules.beheer.parameters.layout.database.DatabaseTable;
import nl.procura.gba.web.modules.beheer.parameters.layout.database.DatabaseWindow;

public class AvailableDatabasesLayout extends ParameterLayout<ConfigParameterForm> {

  private final DatabaseTable table;

  public AvailableDatabasesLayout(GbaApplication application, String naam, String category) {

    super(application, fil(naam) ? (naam + " - parameters - " + category) : ("Parameters - " + category));
    setMargin(false);
    setForm(new ConfigParameterForm());

    addButton(buttonNew);
    addButton(buttonDel);

    table = new DatabaseTable();
    addComponent(table);
  }

  @Override
  public void onNew() {
    table.showPropertiesFile(DatabaseWindow.getNewFile());
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<File>(table, true) {

      @Override
      public void deleteValue(File file) {
        file.delete();
      }

      @Override
      protected void afterDelete() {
        table.init();
      }
    };
  }
}
