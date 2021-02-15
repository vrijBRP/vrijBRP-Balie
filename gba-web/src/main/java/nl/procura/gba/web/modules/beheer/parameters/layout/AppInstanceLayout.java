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

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.modules.beheer.parameters.form.ConfigParameterForm;
import nl.procura.gba.web.modules.beheer.parameters.layout.instances.InstanceTable;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;

public class AppInstanceLayout extends ParameterLayout<ConfigParameterForm> {

  private final InstanceTable table;

  public AppInstanceLayout(GbaApplication application, String naam, String category) {

    super(application, fil(naam) ? (naam + " - parameters - " + category) : ("Parameters - " + category));
    setMargin(false);
    setForm(new ConfigParameterForm());

    addButton(buttonNew);
    addButton(buttonDel);

    table = new InstanceTable();
    addComponent(table);
  }

  @Override
  public void onNew() {
    table.showApp(new Application());
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<Application>(table, true) {

      @Override
      public void deleteValue(Application app) {
        getServices().getOnderhoudService().delete(app);
      }

      @Override
      protected void afterDelete() {
        table.init();
      }
    };
  }
}
