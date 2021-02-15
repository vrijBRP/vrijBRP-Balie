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

package nl.procura.gba.web.modules.zaken.aantekening.page1;

import static nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningHistorie;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1AantekeningTable extends GbaTable {

  private Zaak    zaak              = null;
  private boolean toonAantekeningen = false;

  public Page1AantekeningTable() {
  }

  public Page1AantekeningTable(Zaak zaak) {
    this.zaak = zaak;
  }

  public boolean isToonGeslotenAantekeningen() {
    return toonAantekeningen;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);
    addStyleName(GbaWebTheme.TABLE.ALIGN_TOP);

    addColumn("Nr", 30);
    addColumn("Status", 65);
    addColumn("Ingevoerd op", 130);
    addColumn("Aantekening", 180).setCollapsible(true);
    addColumn("Betreft");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    // Aantekening
    getColumns().get(1).setCollapsed(zaak != null);

    List<PlAantekening> zaakAantekeningen = new ArrayList<>();
    PlAantekeningStatus[] statussen = new PlAantekeningStatus[]{ OPEN };

    if (isToonGeslotenAantekeningen()) {
      statussen = new PlAantekeningStatus[]{ AFGESLOTEN, ONBEKEND };
    }

    if (zaak != null) {
      zaakAantekeningen = getAantekeningenService().getZaakAantekeningen(zaak).getAantekeningen();
    } else {
      zaakAantekeningen = getAantekeningenService().getPersoonAantekeningen(statussen).getAantekeningen();
    }

    Collections.sort(zaakAantekeningen);

    int size = zaakAantekeningen.size();

    for (PlAantekening aant : zaakAantekeningen) {

      PlAantekeningHistorie laatste = aant.getLaatsteHistorie();

      if (laatste != null) {

        String onderwerp = laatste.getOnderwerp();
        String inhoud = (fil(onderwerp) ? ("Onderwerp: " + onderwerp + "\n") : "") + laatste.getInhoud();
        Record r = addRecord(aant);

        r.addValue(size);
        r.addValue(aant.getLaatsteHistorie().getHistorieStatus());
        r.addValue(aant.getLaatsteHistorie().getTijdstip());
        r.addValue(fil(aant.getZaakId()) ? "Aantekening bij de zaak" : laatste.getIndicatie());
        r.addValue(MiscUtils.summarize(inhoud, 2));

        size--;
      }
    }

    super.setRecords();
  }

  public void setToonAantekeningen(boolean toonAantekeningen) {
    this.toonAantekeningen = toonAantekeningen;
  }

  private AantekeningService getAantekeningenService() {
    return getApplication().getServices().getAantekeningService();
  }
}
