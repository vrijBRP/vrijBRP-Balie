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

package nl.procura.gba.web.services.applicatie.onderhoud;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.jpa.personen.db.App;
import nl.procura.gba.web.rest.client.GbaRestClient;
import nl.procura.gba.web.services.interfaces.EntityComposity;

public class Application implements EntityComposity<App> {

  private boolean        userOk;
  private boolean        loginOk;
  private GbaRestClient  client;
  private App            app;
  private String         problem;
  private SyncAttributes attributes = new SyncAttributes();

  public Application() {
    this(new App());
  }

  public Application(App app) {
    setEntity(app);
  }

  public boolean isUserOk() {
    return userOk;
  }

  public void setUserOk(boolean userOk) {
    this.userOk = userOk;
  }

  public boolean isLoginOk() {
    return loginOk;
  }

  public void setLoginOk(boolean loginOk) {
    this.loginOk = loginOk;
  }

  public GbaRestClient getClient() {
    return client;
  }

  public void setClient(GbaRestClient client) {
    this.client = client;
  }

  @Override
  public App getEntity() {
    return app;
  }

  public void setEntity(App app) {
    this.app = app;
    getAttributes().load(app.getAttr());
  }

  public String getProblem() {
    return problem;
  }

  public void setProblem(String problem) {
    this.problem = problem;
  }

  public SyncAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(SyncAttributes attributes) {
    this.attributes = attributes;
  }

  public boolean isActive() {
    return pos(app.getActive());
  }
}
