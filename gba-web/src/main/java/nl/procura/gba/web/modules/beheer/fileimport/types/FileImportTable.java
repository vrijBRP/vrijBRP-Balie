package nl.procura.gba.web.modules.beheer.fileimport.types;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;

public abstract class FileImportTable extends GbaTable {

  public abstract void update(List<FileImportRecord> records);

  public abstract void sort(Object value);
}
