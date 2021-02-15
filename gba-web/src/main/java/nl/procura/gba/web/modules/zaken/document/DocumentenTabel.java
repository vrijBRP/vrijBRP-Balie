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

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsResultaat;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsStream;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;

public abstract class DocumentenTabel extends GbaTable {

  public abstract DmsResultaat getOpgeslagenBestanden();

  @Override
  public void onDoubleClick(Record record) {
    openBestand(record);
  }

  public void openBestand(Record record) {

    try {
      DmsDocument file = (DmsDocument) record.getObject();
      DmsStream stream = getApplication().getServices().getDmsService().getBestand(file);

      BestandType type = BestandType.getType(file.getExtensie());
      PreviewFile previewFile = new PreviewFile(stream.getInputStream(), file.getTitel(), file.getBestandsnaam(),
          type);
      previewFile.setProperty("Titel", file.getTitel());
      previewFile.setProperty("Bestandsnaam", file.getBestandsnaam());
      previewFile.setProperty("Aangemaakt door", file.getAangemaaktDoor());
      previewFile.setProperty("Aangemaakt op", new DateTime(file.getDatum(), file.getTijd()).toString());
      previewFile.setProperty("Zaak-id", file.getZaakId());
      previewFile.setProperty("Opgeslagen op", parsePath(file.getPad()));
      previewFile.setProperty("Dms-naam", file.getDmsNaam());
      previewFile.setProperty("Vertrouwelijkheid",
          DocumentVertrouwelijkheid.get(file.getVertrouwelijkheid()).getOmschrijving());

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
    addColumn("Vertrouwelijkheid", 130);
    addColumn("Zaak-id", 120);
    addColumn("Gebruiker", 150);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    DmsResultaat dmsResult = getOpgeslagenBestanden();

    List<DmsDocument> records = dmsResult.getDocumenten();

    int i = records.size();

    for (DmsDocument record : records) {

      Record r = addRecord(record);
      r.addValue(i);
      r.addValue(TableImage.getByBestandType(BestandType.getType(record.getExtensie())));
      r.addValue(new DateTime(record.getDatum(), record.getTijd()));
      r.addValue(getTitel(record));
      r.addValue(DocumentVertrouwelijkheid.get(record.getVertrouwelijkheid()).getOmschrijving());
      r.addValue(record.getZaakId());
      r.addValue(record.getAangemaaktDoor());

      i--;
    }
  }

  private String getTitel(DmsDocument record) {

    StringBuilder out = new StringBuilder();
    out.append(record.getTitel());

    if (fil(record.getDmsNaam())) {
      out.append(" (");
      out.append(record.getDmsNaam());
      out.append(")");
    }

    return out.toString();
  }

  private String parsePath(String pad) {
    String[] splits = pad.split("documenten");
    if (splits.length > 1) {
      return FilenameUtils.separatorsToUnix(splits[1]);
    }
    return pad;
  }
}
