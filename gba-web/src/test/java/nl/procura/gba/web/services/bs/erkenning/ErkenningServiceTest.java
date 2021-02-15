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

package nl.procura.gba.web.services.bs.erkenning;

import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossErk;
import nl.procura.gba.jpa.personen.db.DossNatio;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;

public class ErkenningServiceTest extends ZaakServiceTest {

  private static final int        START_DATE              = 2018_01_01;
  private static final BigDecimal EXPECTED_START_DATE     = new BigDecimal("20180101");
  private static final String     MOTHER_FIRST_NAME       = "Mother";
  private static final String     ACKNOWLEDGER_FIRST_NAME = "Acknowledger";
  private static final int        NUMBER_OF_PEOPLE        = 2;
  private static final int        NUMBER_OF_NATIONALITIES = 2;

  private final ErkenningService erkenningService;

  public ErkenningServiceTest() {
    erkenningService = services.getErkenningService();
  }

  @Test
  public void saveMustCreateValidDossierEntities() {
    // given an acknowledge dossier with two nationalities
    Dossier dossier = (Dossier) erkenningService.getNewZaak();
    dossier.setDatumIngang(new DateTime(START_DATE));
    DossierErkenning erkenning = (DossierErkenning) dossier.getZaakDossier();
    DossierPersoon mother = erkenning.getMoeder();
    mother.setVoornaam(MOTHER_FIRST_NAME);
    mother.toevoegenNationaliteit(NATIONALITY_1);
    DossierPersoon acknowledger = erkenning.getErkenner();
    acknowledger.setVoornaam(ACKNOWLEDGER_FIRST_NAME);
    acknowledger.toevoegenNationaliteit(NATIONALITY_2);

    // when
    erkenningService.save(dossier);

    // then generic dossier must be created
    List<Doss> dossiers = GenericDao.createQuery("SELECT d FROM Doss d", Doss.class).getResultList();
    assertEquals(1, dossiers.size());
    assertEquals(EXPECTED_START_DATE, dossiers.get(0).getDIn());

    // then acknowledge dossier must be created
    List<DossErk> acknowledgeDossiers = GenericDao.createQuery("SELECT d FROM DossErk d", DossErk.class)
        .getResultList();
    assertEquals(1, acknowledgeDossiers.size());

    // then both persons must be linked
    List<DossPer> dossierPers = GenericDao.createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(NUMBER_OF_PEOPLE, dossierPers.size());
    DossPer actualMother = dossierPers.stream()
        .filter(p -> MOTHER_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(EXPECTED_START_DATE, actualMother.getDoss().getDIn());
    DossPer actualAcknowledger = dossierPers.stream()
        .filter(p -> ACKNOWLEDGER_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(EXPECTED_START_DATE, actualAcknowledger.getDoss().getDIn());

    // then nationalities must be linked to persons and not to dossiers
    List<DossNatio> nationalityDossiers = GenericDao.createQuery("SELECT d FROM DossNatio d", DossNatio.class)
        .getResultList();
    assertEquals(NUMBER_OF_NATIONALITIES, nationalityDossiers.size());
    DossNatio natio1 = nationalityWithCode(nationalityDossiers, NATIONALITY_1.getCNatio());
    assertNull(natio1.getDoss());
    assertEquals(MOTHER_FIRST_NAME, natio1.getDossPer().getVoorn());
    DossNatio natio2 = nationalityWithCode(nationalityDossiers, NATIONALITY_2.getCNatio());
    assertNull(natio2.getDoss());
    assertEquals(ACKNOWLEDGER_FIRST_NAME, natio2.getDossPer().getVoorn());
  }
}
