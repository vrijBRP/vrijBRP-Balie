/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page1;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaarHistorie;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1ZaakBehandelaarTable extends GbaTable {

  private final Zaak zaak;

  public Page1ZaakBehandelaarTable(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("Datum / tijd", 120);
    addColumn("Behandelaar", 180);
    addColumn("Opmerking");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    ZaakBehandelaarHistorie historie = getApplication()
        .getServices()
        .getZaakAttribuutService()
        .getZaakBehandelaarHistorie(zaak);
    if (historie.exists()) {
      int nr = historie.size();
      for (ZaakBehandelaar behandelaar : historie.getBehandelaars()) {
        String name = behandelaar.getUsrToek().getUsrfullname();
        name += nr == historie.size() ? " (huidige)" : "";
        Record r = addRecord(behandelaar);
        r.addValue(nr--);
        r.addValue(behandelaar.getDatumTijdInvoer());
        r.addValue(name);
        r.addValue(behandelaar.getOpm());
      }
    }

    super.setRecords();
  }
}
