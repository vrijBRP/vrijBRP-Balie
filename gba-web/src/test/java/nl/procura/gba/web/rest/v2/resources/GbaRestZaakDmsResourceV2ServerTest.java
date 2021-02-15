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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.web.application.GbaApplicationMock;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakId;

public class GbaRestZaakDmsResourceV2ServerTest {

  private GbaRestZaakDmsResourceV2Server resource;

  @Before
  public void setUp() {
    resource = getResourceV2(GbaRestZaakDmsResourceV2Server.class);
    // set application path for LokaleDmsService
    GbaApplicationMock.getInstance();
  }

  @Test
  public void mustReturnGeneratedZaakId() {

    GbaRestAntwoord<GbaRestZaakId> response = resource.genereerZaakId();
    // then validate response
    assertTrue(response.isSucces());
    assertTrue(StringUtils.isNotBlank(response.getInhoud().getId()));
    assertEquals("ZAAKSYSTEEM", response.getInhoud().getSysteem());
  }
}
