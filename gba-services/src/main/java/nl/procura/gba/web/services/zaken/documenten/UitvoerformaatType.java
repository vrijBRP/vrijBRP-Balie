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

package nl.procura.gba.web.services.zaken.documenten;

import java.util.Arrays;

import nl.procura.openoffice.OODocumentFormats;

public enum UitvoerformaatType {

  PDF_A1(OODocumentFormats.PDF_A1, "pdf", "Archief PDF (PDF/A-1)"),
  PDF(OODocumentFormats.PDF, "pdf", "PDF"),
  ODT(OODocumentFormats.ODT, "odt", "OpenOffice"),
  DOC(OODocumentFormats.DOC, "doc", "MS Word"),
  CSV(OODocumentFormats.CSV, "csv", "Normale CSV", ','),
  CSV_SEMICOLON(OODocumentFormats.CSV, "csv", "Excel CSV", ';'),
  DEF("def", "def", "Default");

  private final String id;
  private final String type;
  private final String oms;
  private final char   separator;

  UitvoerformaatType(String id, String type, String oms) {
    this(id, type, oms, ',');
  }

  UitvoerformaatType(String id, String type, String oms, char separator) {
    this.id = id;
    this.type = type;
    this.separator = separator;
    this.oms = oms;
  }

  public static UitvoerformaatType getById(String id) {
    return Arrays.stream(UitvoerformaatType.values())
        .filter(var -> var.getId().equalsIgnoreCase(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Onbekend document type: " + id));
  }

  public String getId() {
    return id;
  }

  public String getExt() {
    return type;
  }

  public String getOms() {
    return oms;
  }

  public char getSeparator() {
    return separator;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
