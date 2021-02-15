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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import static nl.procura.standard.Globalfunctions.date2str;

import java.util.List;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.aanvraag.DocHistorieDto;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Tab1RaasPage2Table extends GbaTable {

  private final DocAanvraagDto aanvraag;

  Tab1RaasPage2Table(DocAanvraagDto aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumns() {

    setSelectable(false);

    addColumn("Nr", 50);
    addColumn("Soort");
    addColumn("Nummer", 150);
    addColumn("Datum einde", 100);
    addColumn("Autoriteit", 200);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    List<DocHistorieDto> historie = aanvraag.getHistorie().getAll();
    for (DocHistorieDto hr : historie) {
      Record record = addRecord(aanvraag);
      record.addValue(hr.getVnr());
      record.addValue(hr.getDocSoort());
      record.addValue(hr.getDocNr());
      record.addValue(date2str(hr.getDatumEindeGeldigheid().getValue()));
      record.addValue(hr.getAutoriteit());
    }

    super.setRecords();
  }
}
