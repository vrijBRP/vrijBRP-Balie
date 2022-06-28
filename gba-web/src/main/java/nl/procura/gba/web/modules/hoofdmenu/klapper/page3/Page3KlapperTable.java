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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;

public class Page3KlapperTable extends GbaTable {

  private final List<DossierAkte> aktes;

  public Page3KlapperTable(List<DossierAkte> aktes) {
    this.aktes = aktes;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Datum akte", 130);
    addColumn("Akte", 100);
    addColumn("Invoertype", 150);
    addColumn("Soort", 200);
    addColumn("Persoon");
    addColumn("Opmerking", 200);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    for (DossierAkte akte : aktes) {
      Record r = addRecord(akte);
      r.addValue(akte.getDatumIngang());
      r.addValue(akte.getAkte());
      r.addValue(akte.getInvoerType());
      r.addValue(akte.getAkteRegistersoort());
      r.addValue(akte.getAktePersoon().getNaam().getNaam_naamgebruik_eerste_voornaam());
      r.addValue(akte.getOpm());
    }
  }
}
