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

package nl.procura.gbaws.web.vaadin.module.auth.usr.page1;

import java.util.List;

import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;

@SuppressWarnings("serial")
public class Page1AuthUsrTable extends PersonenWsTable {

  public Page1AuthUsrTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Gebruikersnaam");
    addColumn("Volledige naam");
    addColumn("Beheerder");
    addColumn("Profiel");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<UsrWrapper> users = UsrDao.getUsers();

    for (UsrWrapper usr : users) {

      Record r = addRecord(usr);
      r.addValue(usr.getGebruikersNaam());
      r.addValue(usr.getVolledigeNaam());
      r.addValue(usr.isAdmin() ? "Ja" : "Nee");
      r.addValue(usr.getProfiel());
    }

    super.setRecords();
  }
}
