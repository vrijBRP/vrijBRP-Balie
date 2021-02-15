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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;

public class BsNatioTable extends GbaTable {

  private final DossierNationaliteiten dossier;

  public BsNatioTable(DossierNationaliteiten dossier) {
    this.dossier = dossier;
  }

  @Override
  public void setColumns() {

    addColumn("Nationaliteit");
    addColumn("Sinds", 232);
    addColumn("Reden", 400);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (DossierNationaliteit nat : dossier.getNationaliteiten()) {

      Record r = addRecord(nat);

      r.addValue(nat.getNationaliteitOmschrijving());
      r.addValue(nat.getSinds());
      r.addValue(nat.getRedenverkrijgingNederlanderschap());
    }

    super.setRecords();
  }
}
