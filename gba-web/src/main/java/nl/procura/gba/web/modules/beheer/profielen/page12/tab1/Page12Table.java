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

package nl.procura.gba.web.modules.beheer.profielen.page12.tab1;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratieService;

public class Page12Table extends GbaTable {

  private static final int INDEX_STATUS = 0;
  private final Profiel    profiel;

  public Page12Table(Profiel profiel) {
    this.profiel = profiel;
  }

  public ProfielExtrasService getProfielBehandelaars() {
    return getApplication().getServices().getProfielExtrasService();
  }

  @Override
  public void onDoubleClick(Record record) {

    ZaakConfiguratie configuratie = (ZaakConfiguratie) record.getObject();
    boolean isGekoppeld = profiel.isGekoppeld(configuratie);

    getApplication().getServices().getProfielService().koppelActie(asList(configuratie), asList(profiel),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, KoppelActie.get(!isGekoppeld).getStatus());
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Status", 120).setUseHTML(true);
    addColumn("Omschrijving");
    addColumn("Bron / leverancier", 350);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    ZaakConfiguratieService service = getApplication().getServices().getZaakConfiguratieService();
    for (ZaakConfiguratie configuratie : service.getConfiguraties()) {
      if (aval(configuratie.getCZaakConf()) >= 0) {
        boolean isGekoppeld = profiel.isGekoppeld(configuratie);
        Record record = addRecord(configuratie);
        record.addValue(KoppelActie.get(isGekoppeld).getStatus());
        record.addValue(configuratie.getZaakConf());
        record.addValue(configuratie.getBron() + " / " + configuratie.getLeverancier());
      }
    }
  }
}
