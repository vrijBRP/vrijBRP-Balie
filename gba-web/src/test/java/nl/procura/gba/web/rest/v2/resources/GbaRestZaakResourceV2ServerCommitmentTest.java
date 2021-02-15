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

package nl.procura.gba.web.rest.v2.resources;

import static nl.procura.gba.web.rest.v2.resources.GbaRestZaakResourceV2TestUtils.getResourceV2;
import static nl.procura.gba.web.rest.v2.resources.GbaRestZaakResourceV2TestUtils.jsonResourceToObject;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;

public class GbaRestZaakResourceV2ServerCommitmentTest {

  private static final String ADD_COMMITMENT_JSON = "addCommitment.json";

  @Test
  public void addZaakWithExistingZaakIdMustNotReturnSuccess() {
    // given added zaak
    GbaRestZaakResourceV2Server resource = getResourceV2(GbaRestZaakResourceV2Server.class);
    GbaRestZaakToevoegenVraag vraag = jsonResourceToObject(getClass(), ADD_COMMITMENT_JSON,
        GbaRestZaakToevoegenVraag.class);
    resource.addZaak(vraag);
    // when add again
    GbaRestAntwoord<GbaRestZaak> response = resource.addZaak(vraag);
    assertFalse(response.isSucces());
  }

}
