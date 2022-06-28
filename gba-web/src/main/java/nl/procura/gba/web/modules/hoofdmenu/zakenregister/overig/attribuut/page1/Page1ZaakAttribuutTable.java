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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page1;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1ZaakAttribuutTable extends GbaTable {

  private Zaak zaak = null;

  public Page1ZaakAttribuutTable() {
  }

  public Page1ZaakAttribuutTable(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("Attribuut", 200);
    addColumn("Waarde");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    AttribuutHistorie lijst = getApplication().getServices().getZaakAttribuutService().getZaakAttribuutHistorie(zaak);

    if (lijst.exists()) {
      for (ZaakAttribuut attribuut : lijst.getAttributen()) {
        Record r = addRecord(attribuut);
        r.addValue(getRecords().size());
        r.addValue(ZaakAttribuutType.getOms(attribuut.getAttribuut()));
        r.addValue(attribuut.getWaarde());
      }
    }

    super.setRecords();
  }
}
