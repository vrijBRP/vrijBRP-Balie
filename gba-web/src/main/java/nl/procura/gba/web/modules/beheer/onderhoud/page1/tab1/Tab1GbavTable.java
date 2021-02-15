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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindow;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;

public class Tab1GbavTable extends GbaTable {

  @Override
  public void onClick(Record record) {
    getWindow().addWindow(new GbavWindow(record.getObject(GbaWsRestGbavAccount.class), this::init));
    super.onClick(record);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Account-id", 80);
    addColumn("Naam", 215);
    addColumn("Type", 100);
    addColumn("Verloopdatum", 100).setUseHTML(true);
    addColumn("Dagen geldig", 100);
    addColumn("Geblokkeerd", 100).setUseHTML(true);
    addColumn("Status").setUseHTML(true);
  }

  @Override
  public void setRecords() {

    try {
      PersonenWsService gbaWs = getApplication().getServices().getPersonenWsService();

      for (GbaWsRestGbavAccount account : gbaWs.getGbavAccounts(PROFIEL_STANDAARD)) {

        Record record = addRecord(account);
        record.addValue(account.getGebruikersnaam());
        record.addValue(account.getNaam() + " (" + account.getOmschrijving() + ")");
        record.addValue(account.getType());
        record.addValue(date2str(account.getDatumVerloop()));
        record.addValue(account.getDagenGeldig());
        record.addValue(getStatus(account.isGeblokkeerd()));
        record.addValue(getStatus(account.getDagenGeldig()));
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }

  private Object getStatus(boolean geblokkeerd) {
    if (geblokkeerd) {
      return setClass(false, "Ja");
    }
    return setClass(true, "Nee");
  }

  private Object getStatus(int dagenGeldig) {
    if (dagenGeldig <= 0) {
      return setClass(false, "Wachtwoord is verlopen");
    }

    return setClass(true, "Wachtwoord is nog geldig");
  }
}
