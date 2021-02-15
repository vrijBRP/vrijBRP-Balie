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
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindow;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAccount;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;

public class Tab1RdwTable extends GbaTable {

  @Override
  public void onClick(Record record) {
    getWindow().addWindow(new RdwWindow(this::init));
    super.onClick(record);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Account-id", 80);
    addColumn("Verloopdatum", 100).setUseHTML(true);
    addColumn("Dagen geldig", 100);
    addColumn("Geblokkeerd", 100).setUseHTML(true);
    addColumn("Status").setUseHTML(true);
  }

  @Override
  public void setRecords() {

    try {
      RijbewijsService rijb = getApplication().getServices().getRijbewijsService();
      RijbewijsAccount account = rijb.getAccount();

      Record record = addRecord(account);
      record.addValue(account.getGebruikersnaam());
      record.addValue(getDatumVerloopStatus(account.getDatumVerloop()));
      record.addValue(getDagenGeldigStatus(account.getDatumVerloop(), account.getDagenGeldig()));
      record.addValue(getGeblokkeerdStatus(account.isGeblokkeerd()));
      record.addValue(getStatus(account.getDatumVerloop(), account.getDagenGeldig()));
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }

  private String getDatumVerloopStatus(String datumVerloop) {
    if (fil(datumVerloop)) {
      return date2str(datumVerloop);
    }
    return "Onbekend";
  }

  private String getDagenGeldigStatus(String datumVerloop, int dagenGeldig) {
    if (fil(datumVerloop)) {
      return astr(dagenGeldig);
    }
    return "Onbekend";
  }

  private String getGeblokkeerdStatus(boolean geblokkeerd) {
    if (geblokkeerd) {
      return setClass(false, "Ja");
    }
    return setClass(true, "Nee");
  }

  private String getStatus(String datumVerloop, int dagenGeldig) {
    if (fil(datumVerloop)) {
      if (dagenGeldig <= 0) {
        return setClass(false, "Wachtwoord is verlopen");
      }

      return setClass(true, "Wachtwoord is nog geldig");
    }

    return setClass(false, "Het is niet bekend op welke datum het wachtwoord verloopt");
  }
}
