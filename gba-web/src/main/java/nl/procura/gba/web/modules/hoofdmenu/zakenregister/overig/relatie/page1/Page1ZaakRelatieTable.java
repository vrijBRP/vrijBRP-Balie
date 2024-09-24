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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page1;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelaties;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1ZaakRelatieTable extends GbaTable {

  private final Zaak zaak;

  public Page1ZaakRelatieTable(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void onDoubleClick(Record record) {

    ZaakRelatie zaakRelatie = record.getObject(ZaakRelatie.class);
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(zaakRelatie.getGerelateerdZaakId());
    List<Zaak> zaken = getApplication().getServices().getZakenService().getMinimaleZaken(zaakArgumenten);

    if (zaken.size() > 0) {

      GbaPageTemplate page = VaadinUtils.getParent(this, GbaPageTemplate.class);
      ZaakregisterNavigator.navigatoTo(zaken.get(0), page, false);
    } else {
      throw new ProException(INFO, "Geen zaak gevonden met zaak-id: {0}", zaakRelatie.getGerelateerdZaakId());
    }
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30);
    addColumn("Zaaktype");
    addColumn("Gebruiker");
    addColumn("Datum ingang");
    addColumn("Status").setUseHTML(true);
  }

  @Override
  public void setRecords() {

    ZaakRelaties relaties = getApplication().getServices().getZaakRelatieService().getGerelateerdeZaakRelaties(
        zaak);
    Map<ZaakRelatie, Zaak> map = getApplication().getServices().getZaakRelatieService().getGerelateerdeZaken(
        relaties);

    int nr = 0;
    for (Entry<ZaakRelatie, Zaak> entry : map.entrySet()) {

      Zaak zaak = entry.getValue();

      nr++;
      Record r = addRecord(entry.getKey());
      r.addValue(nr);

      if (zaak != null) {

        r.addValue(ZaakUtils.getTypeEnOmschrijving(zaak));
        r.addValue(ZaakUtils.getIngevoerdDoorGebruiker(zaak));
        r.addValue(astr(zaak.getDatumIngang()));
        r.addValue(ZaakUtils.getStatus(zaak.getStatus()));
      } else {
        r.addValue("Onbekend zaak-id: " + entry.getKey().getGerelateerdZaakId());
        r.addValue("");
        r.addValue("");
        r.addValue("");
      }
    }

    super.setRecords();
  }
}
