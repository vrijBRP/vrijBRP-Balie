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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import java.util.List;

import nl.bprbzk.bcgba.v14.ArrayOfMatchIdenGegResultaatDE;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class Page2VerhuizingTable2 extends GbaTable {

  private final List<Presentievraag> presentievragen;
  private VerhuisAanvraag            aanvraag = null;

  public Page2VerhuizingTable2(List<Presentievraag> presentievragen) {
    this.presentievragen = presentievragen;
    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Bericht");
    addColumn("Resultaat", 405).setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (Presentievraag p : presentievragen) {

      switch (p.getPresentievraagVersie()) {

        case VERSIE_1_4:

          PresentievraagAntwoord antwoord = p.getPresentievraagAntwoord();

          if (antwoord != null) {

            ArrayOfMatchIdenGegResultaatDE resultaat = antwoord.getResponse().getResultaat();

            if (resultaat != null) {
              Record record = addRecord(p);
              record.addValue(BcGbaVraagbericht.get(antwoord.getVraagBericht()).getOms());
              record.addValue(antwoord.toResultaatString(true));
            }
          } else {
            Record record = addRecord(p);
            record.addValue("Onbekend");
            record.addValue("Kan presentievraag niet tonen");
          }

          break;

        case ONBEKEND:
        default:
      }
    }
  }
}
