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

package nl.procura.gba.web.modules.beheer.parameters.layout.database.page2;

import static nl.procura.gba.web.modules.beheer.parameters.layout.database.page2.Page2DatabaseBean.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.applicatie.DatabaseConfig;
import nl.procura.standard.exceptions.ProException;

public class Page2DatabaseForm extends GbaForm<Page2DatabaseBean> {

  public Page2DatabaseForm(Page2DatabaseBean bean) {

    setColumnWidths(WIDTH_130, "");
    setOrder(TYPE, SERVER, PORT, SCHEMA, SID, USERNAME, PW);
    setBean(bean);
  }

  public void checkConnection() {

    commit();

    try {

      String type = getBean().getType();
      String server = getBean().getServer();
      String port = getBean().getPort();
      String schema = getBean().getSchema();
      String sid = getBean().getSid();
      String username = getBean().getUsername();
      String pw = getBean().getPw();

      DatabaseConfig config = new DatabaseConfig(type, server, port, schema, sid, username, pw);
      getApplication().getServices().getOnderhoudService().checkConnection(config);
    } catch (Exception e) {
      throw new ProException(CONFIG, ERROR, "Kan geen verbinding maken", e);
    }
  }

  @Override
  public Page2DatabaseBean getNewBean() {
    return new Page2DatabaseBean();
  }
}
