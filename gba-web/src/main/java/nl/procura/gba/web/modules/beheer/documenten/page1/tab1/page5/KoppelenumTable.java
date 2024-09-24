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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page5;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieSoortType;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.commons.core.exceptions.ProException;

public class KoppelenumTable extends GbaTable {

  private static final int                  INDEX_STATUS = 0;
  private final KoppelEnumeratieSoortType[] soortTypes;
  private final DocumentRecord              documentRecord;

  public KoppelenumTable(DocumentRecord documentRecord, KoppelEnumeratieSoortType... soortTypes) {
    this.documentRecord = documentRecord;
    this.soortTypes = soortTypes;
  }

  public DocumentService getDocumentRecordExtras() {
    return getApplication().getServices().getDocumentService();
  }

  @Override
  public void onDoubleClick(Record record) {

    KoppelEnumeratie koppelenum = (KoppelEnumeratie) record.getObject();
    boolean isGekoppeld = documentRecord.isGekoppeld(koppelenum);

    getApplication().getServices().getDocumentService().koppelActie(asList(koppelenum), asList(documentRecord),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, geefStatus(!isGekoppeld));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Gekoppeld", 120).setUseHTML(true);
    addColumn("Type", 140);
    addColumn("Gegeven");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      for (KoppelEnumeratieSoortType soortType : soortTypes) {

        for (KoppelEnumeratieType gvKoppelenumType : KoppelEnumeratieType.get(soortType)) {

          if (gvKoppelenumType.getCode() > 0) {

            KoppelEnumeratie gvKoppelenum = new KoppelEnumeratie(gvKoppelenumType);
            boolean isGekoppeld = documentRecord.isGekoppeld(gvKoppelenum);

            Record r = addRecord(gvKoppelenum);
            String status = geefStatus(isGekoppeld);
            r.addValue(status);
            r.addValue(gvKoppelenum.getType().getSoortType());
            r.addValue(gvKoppelenum.getType());
          }
        }
      }
    } catch (Exception e) {

      e.printStackTrace();
      throw new ProException(WARNING, "Fout bij tonen categorieen", e);
    }
  }

  private String geefStatus(boolean b) {
    return b ? setClass("green", "Gekoppeld") : setClass("red", "Niet-gekoppeld");
  }
}
