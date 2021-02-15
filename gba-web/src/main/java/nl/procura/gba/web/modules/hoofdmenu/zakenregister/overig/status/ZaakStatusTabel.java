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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakStatus;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.theme.GbaWebTheme;

public class ZaakStatusTabel extends GbaTable {

  private Zaak zaak;

  public ZaakStatusTabel(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void attach() {
    getRecords().clear();
    super.attach();
  }

  @Override
  public void setColumns() {

    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr.", 30);
    addColumn("Status", 130).setUseHTML(true);
    addColumn("Datum / tijd", 140);
    addColumn("Verschil met vorige status", 160);
    addColumn("Ingevoerd door", 200);
    addColumn("Opmerking");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      if (zaak != null) {

        List<ZaakStatus> zaken = zaak.getZaakHistorie().getStatusHistorie().getStatussen();
        int nr = zaken.size();

        for (ZaakStatus status : zaken) {

          String ingevoerdDoor = astr(status.getIngevoerdDoor());
          Record r = addRecord(status);
          r.addValue(astr(nr));
          r.addValue(ZaakUtils.getStatus(status.getStatus()));
          r.addValue(astr(status.isCorrectIngevoerd() ? status.getDatumTijdInvoer() : "Onbekend"));
          r.addValue(getDuration(status.getDuur()));
          r.addValue(fil(ingevoerdDoor) ? ingevoerdDoor : "Onbekend");
          r.addValue(astr(status.getOpmerking()));

          nr--;
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  private String getDuration(long millis) {
    return (millis <= 0 ? "-" : GbaDatumUtils.getDuration(millis));
  }
}
