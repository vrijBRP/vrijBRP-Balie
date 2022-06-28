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

package nl.procura.gba.web.services.zaken.algemeen.dms.filesystem;

import static nl.procura.standard.Globalfunctions.along;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSStorageType;
import nl.procura.standard.ProcuraDate;

import lombok.Data;

@Data
public class IndexLine {

  private String name                    = "";
  private String alias                   = "";
  private String extension               = "";
  private String title                   = "";
  private String user                    = "";
  private String date                    = "";
  private String time                    = "";
  private String dataType                = "";
  private String zaakId                  = "";
  private String documentTypeDescription = "";
  private String confidentiality         = "";

  public IndexLine() {
    this(new ProcuraDate());
  }

  public IndexLine(ProcuraDate date) {
    setDate(date.getSystemDate());
    setTime(date.getSystemTime());
  }

  public String getFileName() {
    return name + "." + extension;
  }

  @Override
  public String toString() {
    return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s", name, extension, title,
        user, date, time, dataType, zaakId, documentTypeDescription, confidentiality, alias);
  }

  public DMSDocument toDmsDocument(File file) {
    return DMSDocument.builder()
        .storage(DMSStorageType.DEFAULT)
        .content(DMSFileContent.from(file))
        .title(getTitle())
        .user(getUser())
        .datatype(getDataType())
        .zaakId(getZaakId())
        .date(along(getDate()))
        .time(along(getTime()))
        .documentTypeDescription(getDocumentTypeDescription())
        .confidentiality(getConfidentiality())
        .alias(getAlias())
        .build();
  }

  public static IndexLine of(DMSDocument dmsDocument) {
    String naam = FilenameUtils.getBaseName(dmsDocument.getContent().getFilename());
    IndexLine indexLine = new IndexLine();
    indexLine.setName(naam);
    indexLine.setExtension(dmsDocument.getContent().getExtension());
    indexLine.setTitle(StringUtils.defaultIfBlank(dmsDocument.getTitle(), naam));
    indexLine.setUser(dmsDocument.getUser());
    indexLine.setDataType(dmsDocument.getDatatype());
    indexLine.setZaakId(dmsDocument.getZaakId());
    indexLine.setDocumentTypeDescription(dmsDocument.getDocumentTypeDescription());
    indexLine.setConfidentiality(dmsDocument.getConfidentiality());
    indexLine.setAlias(dmsDocument.getAlias());

    return indexLine;
  }
}
