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

package nl.procura.gba.web.services.beheer.bsm;

import static java.lang.System.currentTimeMillis;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementAntwoord;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementVraag;
import nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.GenereerZaakIdAntwoordRestElement;

public class BsmServiceMock extends BsmService {

  private static final String ID_PREFIX = "Mock-";

  @Override
  public void bsmQuery(String query, BsmRestElementVraag vraag, BsmRestElementAntwoord antwoord) {
    if (!"zkn0310.genereerzaakid".equals(query)) {
      super.bsmQuery(query, vraag, antwoord);
      return;
    }
    if (!(antwoord instanceof GenereerZaakIdAntwoordRestElement)) {
      throw new IllegalArgumentException();
    }
    ((GenereerZaakIdAntwoordRestElement) antwoord).setZaakId(ID_PREFIX + currentTimeMillis());
  }

  public static boolean isMockId(String id) {
    return id.startsWith(ID_PREFIX);
  }
}
