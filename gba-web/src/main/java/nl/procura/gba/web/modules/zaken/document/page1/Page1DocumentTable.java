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

package nl.procura.gba.web.modules.zaken.document.page1;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;

public class Page1DocumentTable extends GbaTable {

  public void addRelatie(Relatie relatie) {

    Record r = addRecord(relatie);
    r.addValue(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());

    switch (relatie.getRelatieType()) {
      case NIET_GERELATEERD:
      case ONBEKEND:
        r.addValue(setClass(false, relatie.getRelatieType().getOms()));
        break;

      default:
        r.addValue(relatie.getRelatieType().getOms());
    }

    r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
    r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
  }

  @Override
  public void refreshRecords() {
    super.refreshRecords();
    if (getContainerDataSource().getItemIds().size() > 0) {
      select(getContainerDataSource().getItemIds().iterator().next());
    }
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Persoon").setUseHTML(true);
    addColumn("Relatie", 100).setUseHTML(true);
    addColumn("Geslacht", 100);
    addColumn("Geboren", 100);
  }
}
