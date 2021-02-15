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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekResultPage.Adres;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;

public class ZoekAdresTable extends GbaTable {

  private final List<Adres> adressen;
  private int               geheimAantal = 0;

  public ZoekAdresTable(List<Adres> adressen) {
    this.adressen = adressen;
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
    setSelectFirst(true);

    addColumn("Nr", 30);
    addColumn("Adres");
    addColumn("Aantal personen", 150);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (adressen != null) {

      Profielen profielen = getApplication().getServices().getGebruiker().getProfielen();
      boolean isGeheimToegestaan = profielen.isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);

      int i = 0;
      for (Adres adres : adressen) {
        for (BasePLExt pl : adres.getPls()) {
          if (!isGeheimToegestaan && pl.getPersoon().getStatus().isGeheim()) {
            geheimAantal++;
          }
        }

        i++;
        Record row = addRecord(adres);
        row.addValue(i);
        row.addValue(adres.getOmschrijving());

        int aantal = adres.getPls().size();
        String aantalMelding = aantal + (aantal == 1 ? " persoon" : " personen");
        row.addValue(aantalMelding);
      }
    }
  }
}
