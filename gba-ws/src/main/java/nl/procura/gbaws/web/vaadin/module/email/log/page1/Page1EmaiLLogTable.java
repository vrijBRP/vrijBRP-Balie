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

package nl.procura.gbaws.web.vaadin.module.email.log.page1;

import nl.procura.gba.jpa.personenws.db.EmailLog;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.web.vaadin.layouts.tables.PageableLogTable;

@SuppressWarnings("serial")
public class Page1EmaiLLogTable extends PageableLogTable<Integer> {

  public Page1EmaiLLogTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 70);
    addColumn("Datum / tijd", 130);
    addColumn("Status", 150).setUseHTML(true);
    addColumn("Onderwerp");
    addColumn("Foutmelding").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    long count = getReversePageStartIndex();

    for (int cEmailLog : getPageRecords()) {

      EmailLogWrapper log = new EmailLogWrapper(GbaWsJpa.getManager().find(EmailLog.class, cEmailLog));

      Record r = addRecord(cEmailLog);
      r.addValue(count);
      r.addValue(log.getDateTime());
      r.addValue(log.getSentMessage());
      r.addValue(log.getTable().getSubject());
      r.addValue(log.getError());

      count--;
    }

    super.setRecords();
  }
}
