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

package nl.procura.gba.web.modules.beheer.parameters.form;

import static nl.procura.gba.web.modules.beheer.parameters.bean.ConfiguratieBean.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.util.List;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.parameters.bean.ConfiguratieBean;
import nl.procura.gba.web.services.applicatie.DatabaseConfig;
import nl.procura.standard.exceptions.ProException;

public class ConfigParameterForm extends GbaForm<ConfiguratieBean> {

  public ConfigParameterForm() {

    setColumnWidths("250px", "");

    Object bean = getNewBean();

    setOrder(SNELKEUZE, TYPE, SERVER, PORT, SCHEMA, SID, USERNAME, PW);

    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    getSnelkeuze().addListener((ValueChangeListener) event -> {
      onChangeDatabase((DatabaseConfig) event.getProperty().getValue());
    });
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
  public ConfiguratieBean getNewBean() {
    return new ConfiguratieBean(GbaConfig.getProperties());
  }

  public void save() {

    commit();

    checkConnection();

    getBean().commitToConfig(GbaConfig.getProperties());

    GbaConfig.storeProperties();
  }

  public void setDatabases(List<DatabaseConfig> databases) {
    getSnelkeuze().setContainerDataSource(new DatabaseContainer(databases));
  }

  private GbaNativeSelect getSnelkeuze() {
    return getField(SNELKEUZE, GbaNativeSelect.class);
  }

  private void onChangeDatabase(DatabaseConfig config) {
    if (config != null) {
      getField(TYPE).setValue(config.getType());
      getField(SERVER).setValue(config.getServer());
      getField(PORT).setValue(config.getPort());
      getField(SCHEMA).setValue(config.getSchema());
      getField(SID).setValue(config.getSid());
      getField(USERNAME).setValue(config.getUsername());
      getField(PW).setValue(config.getPassword());
    }
  }
}
