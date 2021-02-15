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

package nl.procura.gba.web.modules.bs.registration.person.modules.module2;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ANDERS;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ERKENNINGS_DATUM;

import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;

class NationalityTable extends GbaTable {

  private final NationalityPage                nationalityPage;
  private final Consumer<DossierNationaliteit> onDoubleClick;

  NationalityTable(NationalityPage nationalityPage, Consumer<DossierNationaliteit> onDoubleClick) {
    this.nationalityPage = nationalityPage;
    this.onDoubleClick = onDoubleClick;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr.", 30);
    addColumn("Nationaliteit");
    addColumn("Sinds", 200);
    addColumn("Reden");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int nr = 1;
    for (final DossierNationaliteit n : nationalityPage.getPerson().getNationaliteiten()) {
      final Record record = addRecord(n);
      record.addValue(nr);
      record.addValue(n.getNatio());
      DossierNationaliteitDatumVanafType sinceType = n.getVerkrijgingType();
      record.addValue(isCustomDate(sinceType)
          ? sinceType.getDescr() + " (" + n.getDatumVerkrijging().toString() + ")"
          : sinceType.getDescr());
      record.addValue(n.getRedenverkrijgingNederlanderschap());
      nr++;
    }
  }

  private boolean isCustomDate(DossierNationaliteitDatumVanafType sinceType) {
    return sinceType.is(ERKENNINGS_DATUM, ANDERS);
  }

  @Override
  public void onDoubleClick(Record record) {
    super.onDoubleClick(record);
    onDoubleClick.accept(record.getObject(DossierNationaliteit.class));
  }
}
