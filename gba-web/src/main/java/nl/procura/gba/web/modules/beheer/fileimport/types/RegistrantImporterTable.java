/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.types;

import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.ACHTERNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.STRAATNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.TOEVOEGING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.VOORNAMEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.WOONPLAATS;
import static nl.procura.standard.Globalfunctions.date2str;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

import java.util.ArrayList;
import java.util.List;

import nl.procura.commons.misc.formats.adres.Adres;
import nl.procura.commons.misc.formats.naam.NaamBuilder;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;

public class RegistrantImporterTable extends FileImportTable {

  private final FileImportTableListener onClickHandler;
  private final List<FileImportRecord>  records = new ArrayList<>();

  public RegistrantImporterTable(FileImportTableListener onClickHandler) {
    this.onClickHandler = onClickHandler;
  }

  @Override
  public void setColumns() {
    addColumn("Geslachtsnaam");
    addColumn("Voornamen");
    addColumn("Geboortedatum", 110);
    addColumn("Adres");
    addColumn("Fouten", 250).setUseHTML(true);
    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {
    onClickHandler.onSelect(record.getObject(FileImportRecord.class));
  }

  @Override
  public void setRecords() {
    for (FileImportRecord line : records) {
      Record record = addRecord(line);
      record.addValue(new NaamBuilder()
          .setVoornamen(line.getValue(VOORNAMEN))
          .setGeslachtsnaam(line.getValue(ACHTERNAAM))
          .createNaam()
          .getNaamNaarNaamgebruik());
      record.addValue(line.getValue(VOORNAMEN));
      record.addValue(date2str(line.getValue(GEBOORTEDATUM)));
      record.addValue(new Adres()
          .setStraat(line.getValue(STRAATNAAM))
          .setHuisnummer(toLong(line.getValue(HUISNUMMER), -1L))
          .setHuisletter(line.getValue(HUISLETTER))
          .setToevoeging(line.getValue(TOEVOEGING))
          .setPostcode(line.getValue(POSTCODE))
          .setWoonplaats(line.getValue(WOONPLAATS))
          .getAdresPcWpl());
      record.addValue(MiscUtils.setClass(false, line.getRemarks()));
    }

    super.setRecords();
  }

  @Override
  public void update(List<FileImportRecord> records) {
    this.records.clear();
    this.records.addAll(records);
    init();
  }
}
