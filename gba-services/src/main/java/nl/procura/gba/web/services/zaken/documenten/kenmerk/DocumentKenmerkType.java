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

package nl.procura.gba.web.services.zaken.documenten.kenmerk;

import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Documentkenmerk type
 */
public enum DocumentKenmerkType {

  MIJN_OVERHEID("mijnoverheid", "Mijn overheid", ""),
  POST("post", "Post", ""),
  VERVOLGBLAD("vervolgblad", "Vervolgblad", ""),
  OPHALEN_BEWONERS("bewoners", "Ophalen bewoners", ""),
  ERKENNING_1("erkenning_1", "Autom. selectie document (erkenning 1)",
      "Sprake van erkenning bestaand kind en toestemminggever is rechtbank, kind en rechtbank (tot 16 jr.) of moeder uit wie het kind is geboren en rechtbank",
      DocumentType.ERKENNING),
  ERKENNING_2("erkenning_2", "Autom. selectie document (erkenning 2)",
      "Sprake van erkenning bestaand kind en gezag is op ja gezet",
      DocumentType.ERKENNING),
  GEBOORTE_1("geboorte_1", "Autom. selectie document (geboorte 1)",
      "Sprake van erkenning ongeboren vrucht en gezag is op ja gezet",
      DocumentType.GEBOORTE),
  GEBOORTE_2("geboorte_2", "Autom. selectie document (geboorte 2)",
      "De erkenning vóór geboorte is gedaan in 2022",
      DocumentType.GEBOORTE),
  ONBEKEND("", "Onbekend", "");

  private final String       code;
  private final String       oms;
  private final DocumentType documentType;
  private final String       toel;

  DocumentKenmerkType(String code, String oms, String toel) {
    this(code, oms, toel, null);
  }

  DocumentKenmerkType(String code, String oms, String toel, DocumentType documentType) {
    this.code = code;
    this.oms = oms;
    this.toel = toel;
    this.documentType = documentType;
  }

  public static DocumentKenmerkType get(String code) {

    for (DocumentKenmerkType type : DocumentKenmerkType.values()) {
      if (code.equals(type.getCode())) {
        return type;
      }
    }

    return ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public String getToel() {
    return toel;
  }

  public DocumentType getDocumentType() {
    return documentType;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
