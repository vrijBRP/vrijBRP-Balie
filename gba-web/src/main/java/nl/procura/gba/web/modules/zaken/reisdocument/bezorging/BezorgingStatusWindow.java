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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controle;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.theme.twee.Icons;

public class BezorgingStatusWindow extends GbaModalWindow {

  private final Controles controles;

  public BezorgingStatusWindow(Controles controles) {
    this.controles = controles;

    setWidth("800px");
    setCaption("Controleer status thuisbezorgingen");

    Table table = new Table();
    addComponent(new VLayout(table)
        .sizeFull()
        .margin(true));
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {
      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Referentie", 150);
      addColumn("Melding");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      if (controles.isEmpty()) {
        Record record = addRecord(null);
        record.addValue(new TableImage(Icons.getIcon(Icons.ICON_INFO)));
        record.addValue("N.v.t");
        record.addValue("Geen wijzigingen als gevolg van berichtenuitwisseling met AMP");
      }

      for (Controle controle : controles) {
        Record record = addRecord(controle);
        record.addValue(new TableImage(Icons.getIcon(controle.isGewijzigd() ? Icons.ICON_OK : Icons.ICON_ERROR)));
        record.addValue(controle.getId());
        record.addValue(controle.getOmschrijving());
      }
      super.setRecords();
    }
  }
}
