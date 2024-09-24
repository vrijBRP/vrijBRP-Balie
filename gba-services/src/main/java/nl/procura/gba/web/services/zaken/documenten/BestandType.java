/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.File;

public enum BestandType {

  PDF("pdf", "Portable Document Format (PDF)", "pdf.png", true),
  DOC("doc", "MS Word", "doc.png", false),
  DOCX("docx", "MS Word (2007)", "doc.png", false),
  ODT("odt", "OpenOffice", "odt.png", false),
  XLS("xls", "Excel", "csv.png", false),
  CSV("csv", "Comma separated values", "csv.png", true),
  MAP("dir", "Map", "dir.png", false),
  VORIGE_MAP("rdir", "Vorige Map", "rdir.png", false),
  TXT("txt", "Tekstbestand", "file.png", true),
  JSON("json", "JSON", "file.json", true),
  PROPERTIES("properties", "Eigenschappen", "file.png", true),
  XML("xml", "XML bestand", "file.png", true),
  PNG("png", "Afbeelding (PNG)", "img.png", true),
  BMP("bmp", "Afbeelding (BMP)", "img.png", true),
  TIF("tif", "Afbeelding (TIF)", "img.png", true),
  TIFF("tiff", "Afbeelding (TIFF)", "img.png", true),
  JPG("jpg", "Afbeelding (JPG)", "img.png", true),
  JPEG("jpeg", "Afbeelding (JPG)", "img.png", true),
  ONBEKEND("onb", "Onbekend", "file.png", false);

  private final String  type;
  private final String  oms;
  private final String  path;
  private final boolean toonBestand;

  BestandType(String type, String oms, String path, boolean toonBestand) {

    this.type = type;
    this.oms = oms;
    this.path = path;
    this.toonBestand = toonBestand;
  }

  public static BestandType getType(File file) {
    return getType(file.getName());
  }

  public static BestandType getType(String fileName) {
    if (fil(fileName)) {
      for (BestandType var : BestandType.values()) {
        if (fileName.toLowerCase().endsWith(var.getType())) {
          return var;
        }
      }
    }

    return ONBEKEND;
  }

  public String getOms() {
    return oms;
  }

  public String getPath() {
    return "buttons/img/doc/" + path;
  }

  public String getType() {
    return type;
  }

  public boolean isToonBestand() {
    return toonBestand;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
