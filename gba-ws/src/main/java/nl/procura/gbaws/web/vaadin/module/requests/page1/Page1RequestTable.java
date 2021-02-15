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

package nl.procura.gbaws.web.vaadin.module.requests.page1;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.jpa.personenws.db.Request;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.handlers.RequestDao;
import nl.procura.gbaws.db.wrappers.RequestWrapper;
import nl.procura.gbaws.web.vaadin.layouts.tables.PageableLogTable;
import nl.procura.gbaws.web.vaadin.module.requests.ModuleRequestWindow;
import nl.procura.gbaws.web.vaadin.module.requests.page2.RequestContent;

@SuppressWarnings("serial")
public class Page1RequestTable extends PageableLogTable<Integer> {

  public Page1RequestTable() {
  }

  @Override
  public void setColumns() {

    addColumn("Nr", 70);
    addColumn("Datum", 90);
    addColumn("Tijd", 60);
    addColumn("Gebruiker");
    addColumn("Gezocht in");
    addColumn("Gevonden in");
    addColumn("Resultaat");
    addColumn("Duur");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    long count = getReversePageStartIndex();

    for (int cRequest : getPageRecords()) {

      RequestWrapper request = new RequestWrapper(GbaWsJpa.getManager().find(Request.class, cRequest));
      Record record = addRecord(cRequest);

      RequestContent rc = new RequestContent(request.getInhoud());
      String username = request.getGebruiker().getVolledigeNaam();

      String aantalWoningkaarten = rc.getItemValue(RequestContent.AANTAL_WONINGKAARTEN);
      String aantalPersoonslijsten = rc.getItemValue(RequestContent.AANTAL_PERSOONSLIJSTEN);
      String databron = rc.getItemValue(RequestContent.DATABRON);
      String duurProcura = rc.getItemValue(RequestContent.DUUR_PROCURA);
      String duurGbav = rc.getItemValue(RequestContent.DUUR_GBAV);
      String bron = rc.getItemValue(RequestContent.BRON);

      StringBuilder resultaat = new StringBuilder();

      if (fil(aantalWoningkaarten)) {

        resultaat.append("Woningkaarten: ");
        resultaat.append(aantalWoningkaarten);
        databron = "Gemeentelijk";
      } else if (fil(aantalPersoonslijsten)) {

        resultaat.append("Persoonslijsten: ");
        resultaat.append(aantalPersoonslijsten);
      }

      StringBuilder duur = new StringBuilder();

      if (fil(duurProcura)) {
        duur.append("Procura: ");
        duur.append(duurProcura);
      }

      if (fil(duurGbav)) {

        if (fil(duurProcura)) {
          duur.append(", ");
        }

        duur.append("GBA-V: ");
        duur.append(duurGbav);
      }

      record.addValue(count);
      record.addValue(date2str(request.getDatumIngang()));
      record.addValue(time2str(astr(request.getTijdIngang())));
      record.addValue(fil(username) ? username : "Onbekend");
      record.addValue(databron);
      record.addValue(bron);
      record.addValue(resultaat.toString());
      record.addValue(duur.toString());

      count--;
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {
    getWindow().addWindow(new ModuleRequestWindow(RequestDao.get(record.getObject(Integer.class))));
  }
}
