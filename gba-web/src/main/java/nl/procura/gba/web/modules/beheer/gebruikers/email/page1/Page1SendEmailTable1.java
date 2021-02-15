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

package nl.procura.gba.web.modules.beheer.gebruikers.email.page1;

import static nl.procura.gba.web.services.beheer.email.EmailType.NIEUWE_GEBRUIKER;
import static nl.procura.gba.web.services.beheer.email.EmailType.WACHTWOORD_VERGETEN;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.email.EmailType;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1SendEmailTable1 extends GbaTable {

  public Page1SendEmailTable1() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    setHeight("200px");
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("Type", 150);
    addColumn("Onderwerp");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    EmailType[] emailTypes = { WACHTWOORD_VERGETEN, NIEUWE_GEBRUIKER };
    List<EmailTemplate> templates = getApplication().getServices().getEmailService().getTemplates(emailTypes);

    int nr = 0;

    for (EmailTemplate template : templates) {

      nr++;

      Record r = addRecord(template);

      r.addValue(nr);
      r.addValue(template.getType());
      r.addValue(template.getOnderwerp());
    }

    super.setRecords();
  }
}
