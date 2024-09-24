/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.beheer.vrs;

import java.time.OffsetDateTime;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import nl.procura.burgerzaken.vrsclient.model.InvalidParam;

import lombok.Data;

@Data
public class Error400Response {

  private String         titel;
  private String         status;
  private String         code;
  private OffsetDateTime tijdstip;
  private String         instantie;

  @JsonProperty("ongeldige-parameters")
  private List<InvalidParam> ongeldigeParameters = null;
}
