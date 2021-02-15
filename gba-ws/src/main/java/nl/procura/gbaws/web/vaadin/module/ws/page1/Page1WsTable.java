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

package nl.procura.gbaws.web.vaadin.module.ws.page1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;

@SuppressWarnings("serial")
public class Page1WsTable extends PersonenWsTable {

  public Page1WsTable() {
  }

  @Override
  public void setColumns() {

    addColumn("Naam", 150);
    addColumn("Omschrijving");

    setClickable(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<Webservice> webservices = new ArrayList<>();

    Webservice ws1 = addWebservice(getLocalURL() + "services/ZoekpersoonWS");
    ws1.setName("ZoekpersoonWS");
    ws1.setDescription("Webservice voor het opvragen van persoonsgegevens uit de GBA");
    ws1.addError(0, "Webservice verkeerd aangeroepen");
    ws1.addError(1, "Geen gebruikersnaam/wachtwoord");
    ws1.addError(2, "Gebruikersnaam/wachtwoord incorrect");
    ws1.addError(3, "Gebruiker heeft alleen toegang tot de site/documentatie");

    ws1.addError(100, "Geen zoekargumenten ingegeven");
    ws1.addError(101, "A-nummer is incorrect");

    ws1.addError(900, "Webservice error");
    ws1.addError(901, "Webservice verkeerd geconfigureerd");
    ws1.addError(902, "Webservice configuratiefout, parameter 'xx' niet gezet");

    Webservice ws2 = addWebservice(getLocalURL() + "services/WoningkaartWS");
    ws2.setName("WoningkaartWS");
    ws2.setDescription("Webservice voor het opvragen van adressen uit de GBA");

    ws2.addError(0, "Webservice verkeerd aangeroepen");
    ws2.addError(1, "Geen gebruikersnaam/wachtwoord");
    ws2.addError(2, "Gebruikersnaam/wachtwoord incorrect");
    ws2.addError(3, "Gebruiker heeft alleen toegang tot de site/documentatie");

    ws2.addError(100, "Geen zoekargumenten ingegeven");
    ws2.addError(101, "A-nummer is incorrect");

    ws2.addError(900, "Webservice error");
    ws2.addError(901, "Webservice verkeerd geconfigureerd");
    ws2.addError(902, "Webservice configuratiefout, parameter 'xx' niet gezet");

    webservices.add(ws1);
    webservices.add(ws2);

    for (Webservice ws : webservices) {
      Record record = addRecord(ws);
      record.addValue(ws.getName());
      record.addValue(ws.getDescription());
    }

    super.setRecords();
  }

  private String getLocalURL() {
    return getApplication().getURL().toString();
  }

  public Webservice addWebservice(String url) {
    Webservice ws = new Webservice();
    ws.setWsdlUrl(url + "?wsdl");
    ws.setUrl(url);
    return ws;
  }

  public class Webservice implements Serializable {

    private List<Action> actions     = new ArrayList<>();
    private List<Error>  errors      = new ArrayList<>();
    private String       name        = "";
    private String       description = "";
    private String       url         = "";
    private String       wsdlUrl     = "";

    public void addError(int code, String description) {
      getErrors().add(new Error(code, description));
    }

    public void addAction(String name) {
      getActions().add(new Action(name));
    }

    public List<Action> getActions() {
      return actions;
    }

    public void setActions(ArrayList<Action> actions) {
      this.actions = actions;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getWsdlUrl() {
      return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
      this.wsdlUrl = wsdlUrl;
    }

    public List<Error> getErrors() {
      return errors;
    }

    public void setErrors(List<Error> errors) {
      this.errors = errors;
    }
  }

  public class Action implements Serializable {

    private String name = "";

    public Action(String name) {
      setName(name);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public class Error {

    private int    code;
    private String description;

    public Error(int code, String description) {
      this.code = code;
      this.description = description;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }
}
