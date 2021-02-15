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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page1;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1BewonersTable extends GbaTable {

  private int geheimAantal;
  private int aantal;

  public int getAantal() {
    return aantal;
  }

  public void setAantal(int aantal) {
    this.aantal = aantal;
  }

  public int getGeheimAantal() {
    return geheimAantal;
  }

  public void setGeheimAantal(int geheimAantal) {
    this.geheimAantal = geheimAantal;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr.", 50);
    addColumn("Persoon").setUseHTML(true);
    addColumn("Geslacht", 90);
    addColumn("Geboren", 90);

    super.setColumns();
  }

  public void update(GbaApplication application, List<BasePLExt> persoonslijsten) {

    getRecords().clear();
    aantal = 0;
    geheimAantal = 0;

    Profielen profielen = application.getServices().getGebruiker().getProfielen();
    boolean isGeheimToegestaan = profielen.isProfielVeld(ProfielVeld.PL_VERSTREKKINGSBEPERKING);

    int nr = 0;
    for (BasePLExt pl : persoonslijsten) {

      aantal++;

      if (!isGeheimToegestaan && pl.getPersoon().getStatus().isGeheim()) {
        geheimAantal++;
        continue;
      }

      nr++;

      String status = pl.getPersoon().getStatus().getOpsomming();
      String naam = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      String geslacht = pl.getPersoon().getGeslacht().getDescr();
      String geboorte = pl.getPersoon().getGeboorte().getDatumLeeftijd();

      if (fil(status)) {
        naam += MiscUtils.setClass(GbaWebTheme.TEXT.RED, " (" + trim(status) + ")");
      }

      Record record = addRecord(pl);
      record.addValue(nr);
      record.addValue(naam);
      record.addValue(geslacht);
      record.addValue(geboorte);
    }

    reloadRecords();
  }
}
