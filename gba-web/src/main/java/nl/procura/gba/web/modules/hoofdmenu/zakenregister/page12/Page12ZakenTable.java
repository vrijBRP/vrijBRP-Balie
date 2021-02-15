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

import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentTypeUtils;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.theme.twee.Icons;

public class Page12ZakenTable extends GbaTable {

  public Page12ZakenTable() {
  }

  public TableImage getIcon(Zaak zaak) {
    return zaak == null ? new TableImage(Icons.getIcon(Icons.ICON_WARN)) : null;
  }

  @Override
  public void init() {

    super.init();

    selectAll(true);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("&nbsp;", 20).setClassType(Embedded.class);
    addColumn("Nr", 30);
    addColumn("Soort");
    addColumn("Aantekening");
    addColumn("Status", 120).setUseHTML(true);
    addColumn("Uitvoer naar", 250).setClassType(Component.class);

    super.setColumns();
  }

  protected void addPrintRecord(List<DocumentSoort> soorten, Zaak zaak) {

    DocumentZaak uittreksel = (DocumentZaak) zaak;

    for (DocumentSoort soort : soorten) {

      for (DocumentRecord doc : soort.getDocumenten()) {

        DocumentRecord uittDoc = uittreksel.getDoc();

        if (doc.getCDocument().equals(uittDoc.getCDocument())) {

          ZaakType zaakType = DocumentTypeUtils.getZaakType(uittDoc.getDocumentType());
          Zaak gerelateerdeZaak = getGerelateerdeZaak(uittreksel, uittDoc);

          CorrespondentiePrintRecord correspondentiePrintRecord = new CorrespondentiePrintRecord();
          correspondentiePrintRecord.setUittrekselZaak(uittreksel);

          Record r = addRecord(correspondentiePrintRecord);
          r.addValue(getIcon(gerelateerdeZaak));
          r.addValue(getRecords().size());

          if (gerelateerdeZaak != null) {

            String omschrijving = uittreksel.getSoort();
            PrintRecord printRecord = new PrintRecord();
            printRecord.setSoort(soort);
            printRecord.setDocument(doc);
            printRecord.setUitvoer(new UitvoerField(doc));
            printRecord.setZaak(uittreksel);
            printRecord.setModel(PrintModelUtils.getModel(gerelateerdeZaak, zaak, doc));

            correspondentiePrintRecord.setGerelateerdeZaak(gerelateerdeZaak);
            correspondentiePrintRecord.setPrintRecord(printRecord);

            r.addValue(omschrijving);
            r.addValue(
                uittreksel.getZaakHistorie().getAantekeningHistorie().getAantekeningenSamenvatting());
            r.addValue(ZaakUtils.getStatus(uittreksel.getStatus()));
            r.addValue(printRecord.getUitvoer());
          } else {

            r.addValue(
                uittreksel.getZaakId() + " (Geen gerelateerde zaak gevonden van het type " + zaakType + ")");
            r.addValue(
                uittreksel.getZaakHistorie().getAantekeningHistorie().getAantekeningenSamenvatting());
            r.addValue(ZaakUtils.getStatus(uittreksel.getStatus()));
            r.addValue(correspondentiePrintRecord);
          }
        }
      }
    }
  }

  /**
   * Zoek een gerelateerde zaak van een bepaald zaaktype
   */
  private Zaak getGerelateerdeZaak(DocumentZaak zaak, DocumentRecord document) {
    Services services = getApplication().getServices();
    return services.getZaakRelatieService().getGerelateerdeDocumentZaak(zaak, document);
  }

  private class UitvoerContainer extends ArrayListContainer {

    private UitvoerContainer(DocumentRecord document) {

      try {
        addItems(document.getPrintOpties());
      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }
  }

  private class UitvoerField extends GbaNativeSelect {

    private UitvoerField(DocumentRecord document) {

      setWidth("100%");
      setNullSelectionAllowed(false);
      setContainerDataSource(new UitvoerContainer(document));
      setValue(document.getStandaardPrintOptie());
    }
  }
}
