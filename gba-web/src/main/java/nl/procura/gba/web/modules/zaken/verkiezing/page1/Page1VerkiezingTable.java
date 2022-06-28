/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.verkiezing.page1;

import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;

public class Page1VerkiezingTable extends GbaTable {

  private List<Stempas> stempassen = new ArrayList<>();

  public Page1VerkiezingTable() {
    setClickable(true);
  }

  @Override
  public void setColumns() {
    addColumn("Stempas", 200);
    addColumn("A-nummer", 150);
    addColumn("Naam");
    super.setColumns();
  }

  @Override
  public int getPageLength() {
    return 3;
  }

  @Override
  public void setRecords() {
    for (Stempas stempas : stempassen) {
      Naam naam = stempas.getStemgerechtigde().getPersoon().getNaam();
      Record record = addRecord(stempas);
      record.addValue(stempas.toString());
      record.addValue(stempas.getAnr().getFormatAnummer());
      record.addValue(naam.getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf());
    }

    super.setRecords();
  }

  public void setStempassen(List<Stempas> stempassen) {
    this.stempassen = stempassen;
    init();
  }

  @Override
  public void onDoubleClick(Record record) {
    getApplication().goToPl(getWindow(), "zaken.kiezersregister", STANDAARD,
        record.getObject(Stempas.class).getAnr().getAnummer());
    super.onClick(record);
  }
}
