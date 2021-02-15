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

package nl.procura.gba.web.services.gba.ple;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gbaws.testdata.Testdata;

public class VerblijfstitelTest extends ZaakServiceTest {

  BasePLExt pl = null;

  @Before
  public void init() {
    pl = services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString());
  }

  @Test
  public void verblijfstitelCode() {
    assertTrue(pl.getVerblijfstitel().isVerblijfstitelCode(21, 22, 23, 24, 25, 35));
  }

  @Test
  public void nietNederlander() {
    assertTrue(pl.getNatio().isNietNederlander());
  }
}
