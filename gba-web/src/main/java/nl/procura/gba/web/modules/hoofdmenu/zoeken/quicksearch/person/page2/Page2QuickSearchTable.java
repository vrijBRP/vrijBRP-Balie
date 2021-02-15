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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page2;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Page2QuickSearchTable extends GbaTable {

  private final PLResultComposite result;

  public Page2QuickSearchTable(PLResultComposite result) {
    this.result = result;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setSelectFirst(true);

    addColumn("Nr", 30);
    addColumn("Naam", 200);
    addColumn("Geslacht", 60);
    addColumn("Geboortedatum", 100);
    addColumn("Adres");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    int nr = 0;
    if (result != null) {
      for (BasePLExt pl : result.getBasisPLWrappers()) {
        nr++;
        Record row = addRecord(pl);
        row.addValue(astr(nr));
        row.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        row.addValue(pl.getPersoon().getGeslacht().getDescr());
        row.addValue(pl.getPersoon().getGeboorte().getDatumLeeftijd());
        row.addValue(pl.getVerblijfplaats().getAdres().getAdresPcWplGem());
      }
    }
  }
}
