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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page1;

import static java.util.Comparator.comparingInt;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificaties;

public class Page1ZaakIdentificatieTable extends GbaTable {

  private Zaak zaak = null;

  public Page1ZaakIdentificatieTable() {
  }

  public Page1ZaakIdentificatieTable(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr", 30);
    addColumn("Type", 200);
    addColumn("Zaak-id");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int nr = 0;
    ZaakIdentificaties lijst = getApplication().getServices().getZaakIdentificatieService().getIdentificaties(zaak);

    if (lijst.exists()) {
      List<ZaakIdentificatie> nummers = lijst.getNummers();
      nummers.sort(comparingInt(z -> z.getZaakIdType().getOrder()));

      for (ZaakIdentificatie id : nummers) {
        nr++;
        Record record = addRecord(id);
        record.addValue(nr);
        record.addValue(id.getType());
        record.addValue(id.getExternId());
      }
    }

    super.setRecords();
  }
}
