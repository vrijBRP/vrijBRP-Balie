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

package nl.procura.gbaws.web.vaadin.module.sources.gbav.page1;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personenws.db.GbavProfileType;
import nl.procura.gbaws.db.handlers.GbavProfileDao;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;

@SuppressWarnings("serial")
public class Page1DbGbavTable extends PersonenWsTable {

  public Page1DbGbavTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Type", 150);
    addColumn("GBA-V verbinding");
    addColumn("Geblokkeerd", 90).setUseHTML(true);
    addColumn("Wachtwoord verlopen", 150).setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<GbavProfileWrapper> profiles = GbavProfileDao.getProfiles();

    for (GbavProfileWrapper profile : profiles) {

      Record r = addRecord(profile);
      r.addValue(GbavProfileType.get(profile.getType()));
      r.addValue(profile.toString());
      r.addValue(MiscUtils.setClass(!profile.isGeblokkeerd(), profile.isGeblokkeerd() ? "Ja" : "Nee"));
      r.addValue(MiscUtils.setClass(!profile.isVerlopen(), profile.isVerlopen() ? "Ja" : "Nee"));
    }

    super.setRecords();
  }
}
