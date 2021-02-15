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

package nl.procura.gba.web.modules.hoofdmenu.sms.page2;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.sms.rest.domain.Message;
import nl.procura.sms.rest.domain.MessageStatus;
import nl.procura.sms.rest.domain.SmsStatus;
import nl.procura.sms.rest.domain.StatusType;
import nl.procura.standard.ProcuraDate;

public class Page2SmsTable extends GbaTable {

  private final Message message;

  public Page2SmsTable(Message message) {
    this.message = message;
  }

  @Override
  public void setColumns() {

    setSelectable(false);

    addColumn("Nr", 30);
    addColumn("Ingevoerd op", 150);
    addColumn("Status", 320).setUseHTML(true);
    addColumn("Eindstatus", 70);
    addColumn("SMS Status", 350);
    addColumn("(Fout)melding");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<MessageStatus> statuses = new ArrayList(message.getStatuses());
    Collections.reverse(statuses);
    int nr = statuses.size();
    for (MessageStatus status : statuses) {
      Record record = addRecord(status);
      record.addValue(nr);
      record.addValue(new ProcuraDate(status.getTimestamp()).getFormatDate("MM-dd-yyyy HH:mm:ss"));
      record.addValue(setStatus(status));
      record.addValue(status.isStatusFinal() ? "Ja" : "Nee");
      record.addValue(getSmsStatus(status));
      record.addValue(status.getMessage());
      nr--;
    }

    super.setRecords();
  }

  private String setStatus(MessageStatus status) {
    StatusType code = StatusType.getByCode(aval(status.getCode()));
    return MiscUtils.setClass(code.isPositive(), status.getName());
  }

  private String getSmsStatus(MessageStatus status) {
    SmsStatus smsStatus = status.getSmsStatus();
    StringBuilder out = new StringBuilder();
    if (smsStatus != null) {
      if (smsStatus.isSuccess()) {
        out.append("Succes");
      } else {
        out.append("Fout");
      }
      if (fil(smsStatus.getName())) {
        out.append(": ");
        out.append(smsStatus.getName());
      }
      return out.toString();
    }

    return out.toString();
  }
}
