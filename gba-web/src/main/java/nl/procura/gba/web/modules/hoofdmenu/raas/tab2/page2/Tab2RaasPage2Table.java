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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2;

import nl.procura.dto.raas.bestand.DescriptionDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.dto.raas.bestand.RaasBestandElementDto;
import nl.procura.dto.raas.bestand.RaasBestandRequestDto;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Tab2RaasPage2Table extends GbaTable {

  private final RaasBestandDto bestand;

  public Tab2RaasPage2Table(RaasBestandDto bestand) {
    this.bestand = bestand;
  }

  @Override
  public void setColumns() {

    setSelectable(false);
    addColumn("Element id", 100);
    addColumn("Omschrijving", 400);
    addColumn("Waarde");
    addColumn("Toelichting waarde");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    getRecords().clear();
    for (RaasBestandRequestDto req : bestand.getRequests().getAll()) {
      for (RaasBestandElementDto elem : req.getElements()) {
        Record r = addRecord(elem);
        r.addValue(elem.getElementId());
        r.addValue(elem.getDescription());
        r.addValue(elem.getElementValue());
        DescriptionDto ev = elem.getElementValueDescription();
        r.addValue(ev.isNotBlank() ? ev.getValue() : "");
      }
    }

    super.setRecords();
  }
}
