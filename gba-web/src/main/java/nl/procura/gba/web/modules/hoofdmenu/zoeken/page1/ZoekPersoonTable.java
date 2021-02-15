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

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.fields.DatumLeeftijdWaarde;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekResultPage.Adres;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.gba.web.theme.GbaWebTheme;

public class ZoekPersoonTable extends GbaTable {

  private final List<Adres> adressen;
  private int               geheimAantal = 0;

  public ZoekPersoonTable(List<Adres> adressen) {
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
    addColumn("Naam", 200);
    addColumn("Geslacht", 60);
    addColumn("Geboortedatum", 100);
    addColumn("Adres", 300);
    addColumn("Status").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    Profielen profielen = getApplication().getServices().getGebruiker().getProfielen();
    boolean isGeheimToegestaan = profielen.isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);

    int i = 0;
    geheimAantal = 0;

    for (Adres adres : adressen) {

      for (BasePLExt pl : adres.getPls()) {

        if (!isGeheimToegestaan && pl.getPersoon().getStatus().isGeheim()) {
          geheimAantal++;
          continue;
        }

        i++;
        Record row = addRecord(pl);
        row.addValue(astr(i));
        row.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        row.addValue(pl.getPersoon().getGeslacht().getDescr());
        row.addValue(new DatumLeeftijdWaarde(pl.getPersoon().getGeboorte()));
        row.addValue(pl.getVerblijfplaats().getAdres().getAdresPcWplGem());
        row.addValue(setClass(GbaWebTheme.TEXT.RED, pl.getPersoon().getStatus().getOpsomming()));
      }
    }
  }
}
