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

package nl.procura.gba.web.services.zaken.reisdocumenten.bezorging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BezorgingBlokkeerType {

  PL_IS_GEBLOKKEERD("BLOK_PL",
      "Persoonslijst van de aanvrager is geblokkeerd"),

  DOC_NIET_GOED("DOC_NIET_GOED",
      "Document is niet goed"),

  DOC_NIET_GELEVERD("DOC_NIET_GELEVERD",
      "Document is niet geleverd"),

  DOC_VERDWENEN("DOC_VERDWENEN",
      "Document is verdwenen"),

  DOC_UITGEREIKT("DOC_UITGEREIKT_BALIE",
      "Document al uitgereikt aan de balie"),

  PL_IS_OPGESCHORT_O("OPSCHORT_PL",
      "Persoonslijst van de aanvrager wordt opgeschort met reden Overlijden"),

  PL_IS_OPGESCHORT_E("OPSCHORT_PL",
      "Persoonslijst van de aanvrager wordt opgeschort met reden Emigratie"),

  PL_IS_OPGESCHORT_F("OPSCHORT_PL_F",
      "Persoonslijst van de aanvrager wordt opgeschort met reden Foute PL"),

  PL_IS_OPGESCHORT_M("OPSCHORT_PL_M",
      "Persoonslijst van de aanvrager wordt opgeschort met reden Ministerieel besluit"),

  PL_ANDERE_GEMEENTE("ANDERE_GEMEENTE",
      "Inschrijving van de aanvrager in een andere gemeente");

  private final String code;
  private final String oms;
}
