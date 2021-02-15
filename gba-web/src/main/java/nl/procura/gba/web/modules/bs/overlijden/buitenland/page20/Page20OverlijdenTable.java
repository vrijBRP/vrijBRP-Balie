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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page20;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;

public class Page20OverlijdenTable extends GbaTable {

  private final DossierOverlijdenBuitenland dossier;

  public Page20OverlijdenTable(DossierOverlijdenBuitenland dossier) {
    this.dossier = dossier;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Naam", 300).setUseHTML(true);
    addColumn("Relatie", 100);
    addColumn("Adres");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    DossierPersoon persoon = dossier.getAfgever();

    Record r = addRecord(persoon);

    if (persoon != null && persoon.isVolledig()) {
      r.addValue(persoon.getNaam().getPred_adel_voorv_gesl_voorn());
      r.addValue(persoon.getDossierPersoonType().getDescr());
      r.addValue(persoon.getAdres().getAdres());
    } else {
      r.addValue(setClass("grey", "Klik om de gegevens in te voeren"));
      r.addValue("");
      r.addValue("");
    }

    super.setRecords();
  }
}
