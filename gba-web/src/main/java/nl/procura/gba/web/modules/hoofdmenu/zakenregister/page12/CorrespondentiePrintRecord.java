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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page12;

import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

public class CorrespondentiePrintRecord {

  private PrintRecord  printRecord      = null;
  private DocumentZaak uittrekselZaak   = null;
  private Zaak         gerelateerdeZaak = null;

  public Zaak getGerelateerdeZaak() {
    return gerelateerdeZaak;
  }

  public void setGerelateerdeZaak(Zaak gerelateerdeZaak) {
    this.gerelateerdeZaak = gerelateerdeZaak;
  }

  public PrintRecord getPrintRecord() {
    return printRecord;
  }

  public void setPrintRecord(PrintRecord printRecord) {
    this.printRecord = printRecord;
  }

  public DocumentZaak getUittrekselZaak() {
    return uittrekselZaak;
  }

  public void setUittrekselZaak(DocumentZaak correspondentieZaak) {
    this.uittrekselZaak = correspondentieZaak;
  }
}
