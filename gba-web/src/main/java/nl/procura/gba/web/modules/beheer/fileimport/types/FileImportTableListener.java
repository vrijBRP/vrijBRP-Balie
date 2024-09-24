package nl.procura.gba.web.modules.beheer.fileimport.types;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileImportTableListener {

  private FileImportListener listener;
  private boolean            newRegistration;

  public static FileImportTableListener newRegistration(FileImportListener listener) {
    return new FileImportTableListener(true, listener);
  }

  public static FileImportTableListener existingRegistration(FileImportListener listener) {
    return new FileImportTableListener(false, listener);
  }

  private FileImportTableListener(boolean newRegistration, FileImportListener listener) {
    this.newRegistration = newRegistration;
    this.listener = listener;
  }
}
