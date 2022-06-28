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

package nl.procura.gba.web.services.zaken.algemeen.dms.objectstore;

import java.util.Arrays;

public enum ObjectStoreFieldName {

  ALIAS("alias"),
  TITLE("title"),
  USER("user"),
  DATA_TYPE("dataType"),
  CUSTOMER_ID("customerId"),
  DOSSIER_ID("dossierId"),
  DOCUMENT_TYPE_DESCRIPTION("documentTypeDescription"),
  CONFIDENTIALITY("confidentiality");

  private final String code;

  ObjectStoreFieldName(String code) {
    this.code = code;
  }

  public static ObjectStoreFieldName valueOfCode(String code) {
    return Arrays.stream(ObjectStoreFieldName.values())
        .filter(value -> value.code.equals(code))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Onbekende veld: " + code));
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return code;
  }
}
