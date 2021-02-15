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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results;

import static nl.procura.standard.Globalfunctions.aval;

import nl.bprbzk.bcgba.v14.MatchIdenGegAntwoordDE;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;

public class PresentievraagResultsTable extends GbaTable {

  private final Presentievraag presentievraag;

  public PresentievraagResultsTable(Presentievraag presentievraag) {
    this.presentievraag = presentievraag;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Score", 100);
    addColumn("Registratie", 100);
    addColumn("Persoon");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (PresentievraagMatch match : presentievraag.getPresentievraagAntwoord().getMatches()) {
      Record record = addRecord(match);
      record.addValue(match.getScore());
      record.addValue(getRegistratie(match.getMatch().getRegistratie()));
      record.addValue(getNaam(match.getMatch()));
    }

    super.setRecords();
  }

  private String getNaam(MatchIdenGegAntwoordDE a) {
    Naamformats nf = new Naamformats(a.getVoornamen(), a.getGeslachtsnaam(), a.getVoorvoegselGeslachtsnaam(), "",
        "", null);
    return nf.getNaam_naamgebruik_nen_eerste_voornaam();
  }

  private String getRegistratie(String registratie) {
    if (aval(registratie) == Landelijk.RNI) {
      return "RNI";
    }
    return registratie;
  }
}
