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

package nl.procura.gba.web.modules.zaken.woningkaart.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;

public class Page1WoningkaartTable extends GbaTable {

  private int geheimAantal = 0;

  public void addToTable(List<BaseWKPerson> personen, List<BasePLExt> plLijst) {

    int i = 0;

    Profielen profielen = getApplication().getServices().getGebruiker().getProfielen();
    boolean isGeheimToegestaan = profielen.isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);

    for (BaseWKPerson wkPersoon : personen) {

      String dIn = wkPersoon.getDatum_ingang().getDescr();
      String dEnd = wkPersoon.getDatum_vertrek().getDescr();

      String naam = setClass(false,
          "Persoon met a-nummer: " + wkPersoon.getAnummer().getDescr() + " niet gevonden");

      if (pos(wkPersoon.getBsn().getValue())) {
        naam = setClass(false, "Persoon met bsn: " + wkPersoon.getBsn().getDescr() + " niet gevonden");
      }

      String geslacht = "";

      boolean skip = false;

      for (BasePLExt pl : plLijst) {

        if (pl.getPersoon().isNr(wkPersoon.getAnummer().getCode(), wkPersoon.getBsn().getCode())) {

          if (!isGeheimToegestaan && pl.getPersoon().getStatus().isGeheim()) {
            geheimAantal++;
            skip = true;
          }

          naam = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
          geslacht = pl.getPersoon().getGeslacht().getDescr();
          break;
        }
      }

      if (skip) {
        continue;
      }

      i++;

      Record r = addRecord(wkPersoon);
      r.addValue(i);
      r.addValue(dIn);
      r.addValue(dEnd);
      r.addValue(naam);
      r.addValue(geslacht);
      r.addValue(wkPersoon.getDatum_geboren().getDescr());
    }

    reloadRecords();
  }

  public String getGeheimMelding() {

    StringBuilder melding = new StringBuilder();

    if (geheimAantal > 0) {

      melding.append("<br/>");

      if (geheimAantal > 1) {

        melding.append(
            "<b>" + geheimAantal + "</b> personen worden echter niet getoond vanwege een verstrekkingsbeperking.");
      } else if (geheimAantal > 0) {

        melding.append("De persoon wordt echter niet getoond vanwege een verstrekkingsbeperking");
      }
    }

    return melding.toString();
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nr.", 50);
    addColumn("Datum ingang", 90);
    addColumn("Datum einde", 90);
    addColumn("Persoon").setUseHTML(true);
    addColumn("Geslacht", 90);
    addColumn("Geboren", 90);

    super.setColumns();
  }
}
