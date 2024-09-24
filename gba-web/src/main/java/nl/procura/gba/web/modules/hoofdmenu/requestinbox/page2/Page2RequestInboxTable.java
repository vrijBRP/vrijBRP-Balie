/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page2;

import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getBsnFromUrl;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import java.util.Optional;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemCustomer;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemCustomerRole;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.validation.Bsn;

public class Page2RequestInboxTable extends GbaTable {

  private final InboxItem inboxItem;

  public Page2RequestInboxTable(InboxItem inboxItem) {
    this.inboxItem = inboxItem;
    setSelectable(true);
  }

  @Override
  public void setColumns() {
    addColumn("Persoon", 130);
    addColumn("Bsn");

    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {
    try {
      InboxItemCustomer customer = record.getObject(InboxItemCustomer.class);
      Bsn bsn = getBsnFromUrl(customer.getCustomer());
      if (bsn.isCorrect()) {
        getApplication().goToPl(getApplication().getParentWindow(),
            "#pl", STANDAARD, bsn.getDefaultBsn());
      } else {
        throw new IllegalArgumentException("Geen geldig BSN");
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }
  }

  @Override
  public void setRecords() {
    try {
      for (InboxItemCustomer customer : inboxItem.getCustomers()) {
        Record record = addRecord(customer);
        record.addValue(InboxItemCustomerRole.getByName(customer.getRole()));
        record.addValue(Optional.of(getBsnFromUrl(customer.getCustomer()))
            .filter(Bsn::isCorrect)
            .map(Bsn::getFormatBsn)
            .orElse("Onbekend"));
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }
}
