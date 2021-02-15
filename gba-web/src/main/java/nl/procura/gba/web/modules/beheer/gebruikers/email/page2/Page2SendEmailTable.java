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

package nl.procura.gba.web.modules.beheer.gebruikers.email.page2;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.theme.twee.Icons;

public class Page2SendEmailTable extends GbaTable {

  private final List<Verzending> verzendingen;

  public Page2SendEmailTable(List<Verzending> verzendingen) {
    this.verzendingen = verzendingen;
  }

  @Override
  public void init() {

    super.init();

    selectAll(true);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Melding", 300).setUseHTML(true);
    addColumn("Gebruiker");
    addColumn("E-mailadres");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    int nr = 0;

    for (Verzending verzending : verzendingen) {

      nr++;

      Record r = addRecord(verzending);

      r.addValue(nr);

      switch (verzending.getStatus()) {

        case VERSTUURD:
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
          r.addValue(setClass(true, "Verstuurd"));
          r.addValue(verzending.getGebruiker().getNaam());
          break;

        case FOUT:
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
          r.addValue(setClass(false, "Fout: " + verzending.getFout()));
          r.addValue(verzending.getGebruiker().getNaam());
          break;

        default:
          r.addValue(null);
          r.addValue("Geen");
          r.addValue(verzending.getGebruiker().getNaam());
      }

      r.addValue(verzending.getEmailAdres());
    }

    super.setRecords();
  }
}
