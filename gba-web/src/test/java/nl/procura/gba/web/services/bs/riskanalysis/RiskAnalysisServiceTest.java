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

package nl.procura.gba.web.services.bs.riskanalysis;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.commons.core.exceptions.ProException;

public class RiskAnalysisServiceTest extends ZaakServiceTest {

  private static final String       TEST_ZAAK_ID = "test-zaak-id";
  private final RiskAnalysisService riskAnalysisService;

  public RiskAnalysisServiceTest() {
    riskAnalysisService = services.getRiskAnalysisService();
  }

  @After
  public void tearDown() throws Exception {
    riskAnalysisService.getRiskProfiles()
        .forEach(riskAnalysisService::delete);
  }

  @Test
  public void getNewZaakWithRelocationDossierWithOnePersionMustReturnDossierWithOneSubject() {
    RiskProfile riskProfile = new RiskProfile();
    VerhuisAanvraag relocationDossier = givenRelocationDossier();

    // when
    Dossier newDossier = riskAnalysisService.getNewZaak(riskProfile, new RiskAnalysisRelatedCase(relocationDossier));

    // then
    DossierRiskAnalysis riskAnalysis = (DossierRiskAnalysis) newDossier.getZaakDossier();
    assertEquals(riskProfile, riskAnalysis.getRiskProfile());
    assertEquals(TEST_ZAAK_ID, riskAnalysis.getRefCaseId());
    assertEquals(ZaakType.VERHUIZING.getCode(), riskAnalysis.getRefCaseType().longValue());
    List<DossRiskAnalysisSubject> subjects = riskAnalysis.getSubjects();
    assertEquals(1, subjects.size());
  }

  @Test(expected = ProException.class)
  public void getNewZaakWithInvalidDossierMustThrowException() {
    RiskProfile riskProfile = new RiskProfile();
    Dossier marriageDossier = new DossierHuwelijk().getDossier();
    marriageDossier.setZaakId(TEST_ZAAK_ID);

    // when
    riskAnalysisService.getNewZaak(riskProfile, new RiskAnalysisRelatedCase(marriageDossier));

    // then expect ProException
  }

  @Test
  public void saveMustCreateCorrectDatabaseRecords() {
    // given
    VerhuisAanvraag relocationDossier = givenRelocationDossier();
    services.getVerhuizingService().save(relocationDossier);
    Dossier newDossier = riskAnalysisService.getNewZaak(new RiskProfile(),
        new RiskAnalysisRelatedCase(relocationDossier));

    // when
    riskAnalysisService.save(newDossier);

    // then
    List<DossierRiskAnalysis> riskAnalyses = GenericDao
        .createQuery("SELECT d FROM DossRiskAnalysis d", DossierRiskAnalysis.class).getResultList();
    assertEquals(1, riskAnalyses.size());
    List<ZaakRel> relations = GenericDao.createQuery("SELECT d FROM ZaakRel d", ZaakRel.class).getResultList();
    assertEquals(1, relations.size());
    List<DossPer> people = GenericDao.createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(1, people.size());
    List<DossRiskAnalysisSubject> subjects = GenericDao
        .createQuery("SELECT d FROM DossRiskAnalysisSubject d", DossRiskAnalysisSubject.class).getResultList();
    assertEquals(1, subjects.size());
  }

  @Test
  public void saveMustSaveProfileOnce() {
    // given
    RiskProfile profile = newProfile();
    // when
    riskAnalysisService.save(profile);
    // then
    List<RiskProfile> profiles = getRiskProfiles(profile);
    assertEquals(1, profiles.size());
  }

  @Test
  public void saveTypesMustSaveProfileAndTypes() {
    // given
    RiskProfile profile = newProfile();
    // when
    riskAnalysisService.save(profile, singleton(RiskProfileRelatedCaseType.BINNENGEMEENTELIJK));
    // then
    List<RiskProfile> profiles = getRiskProfiles(profile);
    assertEquals(1, profiles.size());
    List<RiskProfileType> types = getTypes(profiles.get(0));
    assertEquals(1, types.size());
    assertEquals(RiskProfileZaakType.BINNENGEMEENTELIJK, types.get(0).getType());
  }

  @Test
  public void updateTypesMustSaveProfileAndTypes() {
    // given
    RiskProfile profile = newProfile();
    riskAnalysisService.save(profile, singleton(RiskProfileRelatedCaseType.BINNENGEMEENTELIJK));
    // when
    riskAnalysisService.save(profile,
        new HashSet<>(asList(RiskProfileRelatedCaseType.INTERGEMEENTELIJK, RiskProfileRelatedCaseType.HERVESTIGING)));
    // then
    List<RiskProfile> profiles = getRiskProfiles(profile);
    assertEquals(1, profiles.size());
    List<RiskProfileType> types = getTypes(profiles.get(0));
    assertEquals(2, types.size());
    assertTrue(types.stream().anyMatch(t -> t.getType() == RiskProfileZaakType.INTERGEMEENTELIJK));
    assertTrue(types.stream().anyMatch(t -> t.getType() == RiskProfileZaakType.HERVESTIGING));
  }

  @Test
  public void getApplicableRiskProfileMustReturnGivenProfile() {
    // given
    RiskProfile givenProfile = newProfile();
    riskAnalysisService.save(givenProfile, singleton(RiskProfileRelatedCaseType.BINNENGEMEENTELIJK));
    VerhuisAanvraag zaak = givenRelocationDossier();
    // when
    RiskProfile profile = riskAnalysisService.getApplicableRiskProfile(zaak).orElseThrow(AssertionError::new);
    // then
    assertEquals(givenProfile.getcRp(), profile.getcRp());
  }

  @Test
  public void getApplicableRiskProfileMustNotReturnWrongProfile() {
    // given
    RiskProfile givenProfile = newProfile();
    riskAnalysisService.save(givenProfile, singleton(RiskProfileRelatedCaseType.INTERGEMEENTELIJK));
    VerhuisAanvraag zaak = givenRelocationDossier();
    // when
    Optional<RiskProfile> profile = riskAnalysisService.getApplicableRiskProfile(zaak);
    // then
    assertFalse(profile.isPresent());
  }

  private static RiskProfile newProfile() {
    RiskProfile profile = new RiskProfile();
    profile.setName("Test-" + UUID.randomUUID());
    profile.setThreshold(BigDecimal.ZERO);
    return profile;
  }

  private List<RiskProfile> getRiskProfiles(RiskProfile profile) {
    List<RiskProfile> profiles = GenericDao.createQuery("SELECT r FROM RiskProfile r" +
        " WHERE r.name = :name", RiskProfile.class)
        .setParameter("name", profile.getName())
        .getResultList();
    return profiles;
  }

  private List<RiskProfileType> getTypes(RiskProfile profile) {
    List<RiskProfileType> types = GenericDao.createQuery("SELECT r FROM RiskProfileType r" +
        " WHERE r.cRp = :name", RiskProfileType.class)
        .setParameter("name", profile.getcRp())
        .getResultList();
    return types;
  }

  public static VerhuisAanvraag givenRelocationDossier() {
    VerhuisAanvraag relocationDossier = new VerhuisAanvraag();
    relocationDossier.setZaakId(TEST_ZAAK_ID);
    relocationDossier.setVerhuisType(BigDecimal.valueOf(VerhuisType.BINNENGEMEENTELIJK.getCode()));
    relocationDossier.setFuncAdr(FunctieAdres.WOONADRES.getCode());
    VerhuisPersoon person = new VerhuisPersoon();
    person.setPersoon(new DocumentPL(new BasePLExt()));
    relocationDossier.getPersonen().add(person);
    return relocationDossier;
  }
}
