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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.ArrayList;
import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.standard.ProcuraDate;

public class Page1ZknDmsStatusTable extends GbaTable {

  private List<BsmRestElement> statussen = new ArrayList<>();

  public Page1ZknDmsStatusTable() {
  }

  @Override
  public void setColumns() {

    setClickable(true);

    addColumn("Status", 300).setUseHTML(true);
    addColumn("Datum / tijd", 300);
    addColumn("Is gezet door");
    addColumn("Toelichting");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (BsmRestElement status : statussen) {

      Record r = addRecord(status);

      BsmRestElement zaakStatusType = status.get(ZAAKSTATUS_TYPE);

      // Volgnummer
      String volgnummer = zaakStatusType.getElementWaarde(VOLGNUMMER);

      // Status
      boolean actueleStatus = isTru(status.getElementWaarde(INDICATIE_LAATSTE_STATUS));
      String code = zaakStatusType.getElementWaarde(CODE);
      String statusOmschrijving = zaakStatusType.getElementWaarde(OMSCHRIJVING);

      if (actueleStatus) {
        statusOmschrijving += " <b>(actuele status)</b>";
      }

      // Datum / tijd
      String tijdstip = "Onbekend";
      String datumZaakStatusGezet = status.getElementWaarde(DATUM_ZAAKSTATUS_GEZET);
      ProcuraDate datum = MiscUtils.convertMsToProcuraDate(datumZaakStatusGezet);
      if (datum != null) {
        String sDatum = datum.getFormatDate();
        String sTijd = datum.getFormatTime();
        tijdstip = (sDatum + " om " + sTijd);
      }

      // Is gezet door
      String isGezetDoor = "";
      if (status.isAdded(IS_GEZET_DOOR)) {
        BsmRestElement isGezetDoorElement = status.get(IS_GEZET_DOOR);
        List<BsmRestElement> betrokkenenElement = isGezetDoorElement.get(BETROKKENEN).getAll(BETROKKENE);
        for (BsmRestElement betrokkeneElement : betrokkenenElement) {
          if (betrokkeneElement.isAdded(MEDEWERKER)) {
            BsmRestElement medewerker = betrokkeneElement.get(MEDEWERKER);
            isGezetDoor = medewerker.getElementWaarde(ACHTERNAAM);
          }
        }
      }

      // Toelichting
      String toelichting = status.getElementWaarde(ZAAKSTATUS_TOELICHTING);

      r.addValue(statusOmschrijving);
      r.addValue(tijdstip);
      r.addValue(isGezetDoor);
      r.addValue(toelichting);
    }

    super.setRecords();
  }

  public void setStatussen(List<BsmRestElement> statussen) {
    this.statussen = statussen;
  }
}
