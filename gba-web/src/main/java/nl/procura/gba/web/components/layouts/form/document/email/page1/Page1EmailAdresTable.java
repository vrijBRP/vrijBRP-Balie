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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import java.util.Collections;

import nl.procura.gba.web.common.misc.email.EmailAddress;
import nl.procura.gba.web.components.layouts.form.document.email.page1.keuze.PrintEmailKeuzeWindow;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Page1EmailAdresTable extends GbaTable {

  private final EmailPreviewContainer container;

  public Page1EmailAdresTable(EmailPreviewContainer container) {
    this.container = container;

    setWidth("100%");
    setHeight("100%");
  }

  @Override
  public void onDoubleClick(Record record) {
    getWindow().getParent().addWindow(new PrintEmailKeuzeWindow(container, record.getObject(EmailAddress.class)));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Soort", 90);
    addColumn("Functie");
    addColumn("Naam");
    addColumn("E-mail");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    Collections.sort(container.getAdressen());

    for (EmailAddress adres : container.getAdressen()) {

      Record record = addRecord(adres);
      record.addValue(adres.getType());
      record.addValue(adres.getFunction());
      record.addValue(adres.getName());
      record.addValue(adres.getEmail());
    }

    super.setRecords();
  }
}
