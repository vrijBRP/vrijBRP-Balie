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

package nl.procura.gba.web.services.bs.algemeen;

import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.NATIONALITY_1;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

import nl.procura.gba.web.services.zaken.ZaakServiceTest;

public class DossierServiceTest extends ZaakServiceTest {

  private final DossierService dossierService;

  public DossierServiceTest() {
    this.dossierService = services.getDossierService();
  }

  @Test
  public void addNationaliteitMustAddNonExistingNationality() {
    DossierPersoon dossierPersoon = new DossierPersoon();

    // when
    dossierService.addNationaliteit(dossierPersoon, NATIONALITY_1);
    // then
    assertEquals(1, dossierPersoon.getNationaliteiten().size());
  }

  @Test
  public void addNationaliteitMustNotAddExistingNationality() {
    DossierPersoon dossierPersoon = new DossierPersoon();
    dossierService.addNationaliteit(dossierPersoon, NATIONALITY_1);

    // when add it again
    dossierService.addNationaliteit(dossierPersoon, NATIONALITY_1);

    // then
    assertEquals(1, dossierPersoon.getNationaliteiten().size());
  }
}
