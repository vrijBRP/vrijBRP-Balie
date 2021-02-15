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

package nl.procura.gba.web.services.bs.geboorte;

import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.NATIONALITY_1;
import static nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteitTest.nationalityWithCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonMock;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;

public class GeboorteServiceTest extends ZaakServiceTest {

  private static final String MOTHER_FIRST_NAME  = "Mother";
  private static final String FATHER_FIRST_NAME  = "Father";
  private static final String APPLIER_FIRST_NAME = "Applier";
  private static final String CHILD_FIRST_NAME   = "Child";
  private static final int    DOSSIER_START_DATE = 2018_01_01;
  private static final int    NUMBER_OF_PEOPLE   = 4;

  private final GeboorteService geboorteService;

  public GeboorteServiceTest() {
    this.geboorteService = services.getGeboorteService();
  }

  @Test
  public void saveMustCreateValidDossierEntities() {
    // given birth dossier with mother, father, applier, and child
    Dossier dossier = (Dossier) geboorteService.getNewZaak();
    dossier.setDatumIngang(new DateTime(DOSSIER_START_DATE));
    DossierGeboorte birthDossier = (DossierGeboorte) dossier.getZaakDossier();
    DossierPersoon mother = birthDossier.getMoeder();
    mother.setVoornaam(MOTHER_FIRST_NAME);
    mother.toevoegenNationaliteit(NATIONALITY_1);
    DossierPersoon father = birthDossier.getVader();
    father.setVoornaam(FATHER_FIRST_NAME);
    DossierPersoon applier = birthDossier.getAangever();
    applier.setVoornaam(APPLIER_FIRST_NAME);
    DossierPersoon kind = DossierPersoonMock.childBelow16();
    services.getDossierService().addKind(birthDossier, kind);

    // when
    geboorteService.save(dossier);

    // then generic dossier must be created with birth date as start date
    List<Doss> dossiers = GenericDao.createQuery("SELECT d FROM Doss d", Doss.class).getResultList();
    assertEquals(1, dossiers.size());
    Doss createdDoss = dossiers.get(0);
    assertEquals(kind.getDGeb(), createdDoss.getDIn());

    // then birth dossier must be created
    List<DossGeb> birthDossiers = GenericDao.createQuery("SELECT d FROM DossGeb d", DossGeb.class)
        .getResultList();
    assertEquals(1, birthDossiers.size());

    // then all people must be linked
    List<DossPer> dossierPers = GenericDao.createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(NUMBER_OF_PEOPLE, dossierPers.size());
    DossPer actualMother = dossierPers.stream()
        .filter(p -> MOTHER_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(kind.getDGeb(), actualMother.getDoss().getDIn());
    DossPer actualFather = dossierPers.stream()
        .filter(p -> FATHER_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(kind.getDGeb(), actualFather.getDoss().getDIn());
    DossPer actualApplier = dossierPers.stream()
        .filter(p -> APPLIER_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(kind.getDGeb(), actualApplier.getDoss().getDIn());
    DossPer actualChild = dossierPers.stream()
        .filter(p -> CHILD_FIRST_NAME.equals(p.getVoorn()))
        .findFirst().orElseThrow(AssertionError::new);
    assertEquals(kind.getDGeb(), actualChild.getDoss().getDIn());

    // then nationalities must be linked to mother and not to dossiers
    List<DossNatio> nationalityDossiers = GenericDao.createQuery("SELECT d FROM DossNatio d", DossNatio.class)
        .getResultList();
    assertEquals(1, nationalityDossiers.size());
    DossNatio natio1 = nationalityWithCode(nationalityDossiers, NATIONALITY_1.getCNatio());
    assertNull(natio1.getDoss());
    assertEquals(MOTHER_FIRST_NAME, natio1.getDossPer().getVoorn());

    // one linked document (akte) should be created
    List<DossAkte> dossierDocuments = GenericDao.createQuery("SELECT d FROM DossAkte d", DossAkte.class)
        .getResultList();
    assertEquals(1, dossierDocuments.size());
    assertEquals(kind.getDGeb(), dossierDocuments.get(0).getDoss().getDIn());
  }
}
