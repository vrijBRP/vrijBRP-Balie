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

package nl.procura.burgerzaken.keesy.api;

import java.util.function.Function;

import nl.procura.burgerzaken.keesy.api.model.InwonerAppError;

import lombok.Data;

@Data
public class ApiResponse<T> {

  private boolean         successful;
  private int             httpCode;
  private T               entity;
  private InwonerAppError error;

  public ApiResponse<T> onError(Function<String, RuntimeException> f) {
    if (!isSuccessful()) {
      if (error != null && error.getException() != null) {
        throw f.apply(error.getException().message());
      } else {
        throw f.apply("Onbekende fout met HTTP code: " + httpCode);
      }
    }
    return this;
  }
}
