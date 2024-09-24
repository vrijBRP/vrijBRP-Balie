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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6;

import java.util.Collections;
import java.util.List;

import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultWindow;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

import lombok.Setter;

public class PresentievragenTable extends GbaTable {

  @Setter
  private List<Presentievraag> presentievragen;

  public PresentievragenTable() {
    setSelectable(true);
    setMultiSelect(true);
  }

  public PresentievragenTable(List<Presentievraag> presentievragen) {
    this();
    setPresentievragen(presentievragen);
  }

  @Override
  public void setColumns() {
    addColumn("Nr", 30);
    addColumn("Tijdstip", 150);
    addColumn("Bericht");
    addColumn("Resultaat", 300).setUseHTML(true);
    addColumn("Betrekking op zaak");
  }

  @Override
  public void setRecords() {

    int nr = presentievragen.size();
    Collections.reverse(presentievragen);

    for (Presentievraag p : presentievragen) {
      switch (p.getPresentievraagVersie()) {

        case VERSIE_1_4:
          PresentievraagAntwoord antwoord = p.getPresentievraagAntwoord();

          if (antwoord != null) {
            Record record = addRecord(p);
            record.addValue(nr);
            record.addValue(p.getDatumTijdInvoer());
            record.addValue(BcGbaVraagbericht.get(antwoord.getVraagBericht()).getOms());
            record.addValue(antwoord.toResultaatString(true));

            if (getColumns().size() >= 5) {
              record.addValue(p.getZaakOmschrijving());
            }
          } else {
            Record record = addRecord(p);
            record.addValue(nr);
            record.addValue(p.getDatumTijdInvoer());
            record.addValue("Onbekend");
            record.addValue("Kan presentievraag niet tonen");

            if (getColumns().size() >= 5) {
              record.addValue(p.getZaakOmschrijving());
            }
          }

          break;

        case ONBEKEND:
        default:
          continue;
      }

      nr--;
    }
  }

  @Override
  public void onDoubleClick(Record record) {
    selectRecord(record);
    super.onDoubleClick(record);
  }

  private void selectRecord(Record record) {
    Presentievraag presentievraag = record.getObject(Presentievraag.class);
    if (presentievraag.getPresentievraagAntwoord() != null) {
      getApplication().getParentWindow().addWindow(new PresentievraagResultWindow(presentievraag));
    } else {
      throw new ProException(ProExceptionSeverity.WARNING, "De presentievraag kan niet worden getoond.");
    }
  }
}
