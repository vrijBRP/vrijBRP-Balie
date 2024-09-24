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

package nl.procura.gba.web.modules.zaken.document;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;

public abstract class DocumentenTabel extends GbaTable {

  public abstract DMSResult getOpgeslagenBestanden();

  @Override
  public void onDoubleClick(Record record) {
    openBestand(record);
  }

  public void openBestand(Record record) {

    try {
      DMSDocument dmsDocument = (DMSDocument) record.getObject();
      DMSContent content = dmsDocument.getContent();

      BestandType type = BestandType.getType(dmsDocument.getContent().getExtension());
      PreviewFile previewFile = new PreviewFile(content.getBytes(),
          dmsDocument.getTitle(),
          dmsDocument.getContent().getFilename(),
          type);

      previewFile.setProperty("Titel", dmsDocument.getTitle());
      previewFile.setProperty("Bestandsnaam", dmsDocument.getContent().getFilename());
      previewFile.setProperty("Alias", dmsDocument.getAlias());
      previewFile.setProperty("Aangemaakt door", dmsDocument.getUser());
      previewFile.setProperty("Aangemaakt op", new DateTime(dmsDocument.getDate(), dmsDocument.getTime()).toString());
      previewFile.setProperty("Zaak-id", dmsDocument.getZaakId());
      previewFile.setProperty("Opgeslagen op", dmsDocument.getContent().getLocation());
      previewFile.setProperty("Documenttype", dmsDocument.getDocumentTypeDescription());
      previewFile.setProperty("Vertrouwelijkheid", DocumentVertrouwelijkheid.get(dmsDocument
          .getConfidentiality())
          .getOmschrijving());
      previewFile.setProperty("Grootte", FileUtils.byteCountToDisplaySize(dmsDocument.getContent().getSize()));
      previewFile.setProperty("Opslaglocatie", dmsDocument.getStorage().toString());
      previewFile.setProperty("Verzonden naar DMS",
          BooleanUtils.toBoolean(dmsDocument.getOtherProperties().get("dms")) ? "Ja" : "Nee");

      FilePreviewWindow.preview(getApplication().getParentWindow(), previewFile);

    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 50);
    addColumn("Type", 30).setClassType(Embedded.class);
    addColumn("Datum/tijd", 140);
    addColumn("Document");
    addColumn("DMS omschrijving", 200);
    addColumn("Vertrouwelijkheid", 130);
    addColumn("Grootte", 120);
    addColumn("Zaak-id", 120);
    addColumn("Gebruiker", 150);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    DMSResult dmsResult = getOpgeslagenBestanden();
    List<DMSDocument> records = dmsResult.getDocuments();
    int index = records.size();

    for (DMSDocument record : records) {
      Record r = addRecord(record);
      r.addValue(index);
      r.addValue(TableImage.getByBestandType(BestandType.getType(record.getContent().getExtension())));
      r.addValue(new DateTime(record.getDate(), record.getTime()));
      r.addValue(record.getTitle());
      r.addValue(record.getDocumentTypeDescription());
      r.addValue(DocumentVertrouwelijkheid.get(record.getConfidentiality()).getOmschrijving());
      r.addValue(FileUtils.byteCountToDisplaySize(record.getContent().getSize()));
      r.addValue(record.getZaakId());
      r.addValue(record.getUser());

      index--;
    }
  }
}
