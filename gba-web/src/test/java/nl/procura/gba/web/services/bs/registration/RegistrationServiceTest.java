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

package nl.procura.gba.web.services.bs.registration;

import static nl.procura.gba.web.services.beheer.bsm.BsmServiceMock.isMockId;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificaties;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class RegistrationServiceTest extends ZaakServiceTest {

  private static final Long HOUSE_NUMBER = 1L;

  private static final GbaDateFieldValue BIRTH_DATE            = new GbaDateFieldValue(BigDecimal.valueOf(2018_02_02));
  private static final String            FAMILY_NAME           = "Register Person1";
  private static final Geslacht          MALE                  = Geslacht.MAN;
  private static final String            COUNTRY               = "Nederland";
  private static final String            MUNICIPALITY          = "Bedum";
  private static final int               NUMBER_OF_PEOPLE      = 1;
  private static final String            NATIONALITY           = "TestNationality";
  private static final int               NUMBER_OF_NATIONALITY = 1;

  private final DossierService      dossierService;
  private final RegistrationService registrationService;

  public RegistrationServiceTest() {
    dossierService = services.getDossierService();
    registrationService = services.getRegistrationService();
    // make sure an external DMS ID is requested during save
    Parameter parameter = services.getGebruiker().getParameters().get(ParameterConstant.ZAKEN_DMS_REGISTRATION);
    parameter.setValue("1");
  }

  @Test
  public void getNewZaakMustReturnDossierWithDefaultValues() {
    // when
    final Zaak newZaak = registrationService.getNewZaak();
    // then
    assertEquals("PROWEB Personen", newZaak.getBron());
    assertEquals("PROCURA", newZaak.getLeverancier());
    assertTrue(isNotBlank(newZaak.getZaakId()));
    assertTrue(newZaak.getDatumTijdInvoer().getLongDate() > -1);
    assertTrue(newZaak.getDatumTijdInvoer().getLongTime() > -1);
    assertTrue(newZaak.getIngevoerdDoor().getLongValue() > 0);
  }

  @Test
  public void newRegistrationDossierMustNotBeStoredWhenCreated() {
    // when
    final DossierRegistration dossierRegistration = newDossierRegistration();

    // then
    final ZaakArgumenten searchById = new ZaakArgumenten(dossierRegistration.getDossier().getZaakId());
    final List<Dossier> minimalZaken = dossierService.getMinimalZaken(searchById);
    assertEquals(0, minimalZaken.size());
  }

  /**
   * Test requires parameters bsm_zaken_dms and zaken_dms_registration to be true (1).
   */
  @Test
  public void saveRegistrationMustSaveGenericDossierAndSpecificDossier() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();
    String consentProvider = "not natural person";
    dossierRegistration.setOtherConsentProvider(consentProvider);
    final CalledServiceListener listener = new CalledServiceListener();
    registrationService.setListener(listener);

    // when
    registrationService.saveRegistration(dossierRegistration);

    // then
    assertDossCount(1L, dossierRegistration);
    assertDossRegistrationCount(1L, dossierRegistration);
    assertTrue(listener.isCalled());
    // then DMS ID must be saved
    final List<ZaakId> zaakIds = getZaakIdsByInternId(dossierRegistration.getDossier().getZaakId());
    assertEquals(1, zaakIds.size());
    assertEquals(dossierRegistration.getDossier().getZaakId(), zaakIds.get(0).getId().getInternId());
    assertTrue(isMockId(zaakIds.get(0).getExternId()));
    // then check registration dossier values
    final Dossier standardZaak = registrationService.getStandardZaak(getMinimalDossier(dossierRegistration));
    final DossierRegistration savedRegistration = (DossierRegistration) standardZaak.getZaakDossier();
    assertEquals(HOUSE_NUMBER, savedRegistration.getHouseNumber());
    assertEquals(consentProvider, savedRegistration.getOtherConsentProvider());
  }

  @Test
  public void saveDossierMustCreateNationalityEntities() {
    final DossierRegistration dossierRegistration = newDossierRegistration();
    final DossierNationaliteit dossNatio = new DossierNationaliteit();
    dossNatio.setNationaliteit(new FieldValue(NATIONALITY));
    dossNatio.setDatumVerkrijging(new DateTime());
    dossNatio.setTypeVerkrijging("");

    final DossierPersoon person = new DossierPersoon();
    person.toevoegenNationaliteit(dossNatio);
    dossierRegistration.getDossier().toevoegenPersoon(person);
    saveAndRetrieveMinimalDossier(dossierRegistration);

    final List<DossPer> dossierPers = GenericDao.createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(NUMBER_OF_PEOPLE, dossierPers.size());
    final List<DossNatio> dossNats = dossierPers.get(0).getDossNats();
    assertEquals(NUMBER_OF_NATIONALITY, dossNats.size());
    assertEquals(NATIONALITY, dossNats.get(0).getNatio());
  }

  /**
   * Test requires parameters bsm_zaken_dms and zaken_dms_registration to be true (1). Parameter bsm_zaken_dms_zaaktype
   * must be set to "ZAAKSYSTEEM".
   */
  @Test
  public void getStandardZaakMustSetExternalIdentifications() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();
    registrationService.saveRegistration(dossierRegistration);

    // when
    final String zaakId = dossierRegistration.getDossier().getZaakId();
    final ZaakArgumenten searchById = new ZaakArgumenten(zaakId);
    final Dossier dossier = dossierService.getMinimalZaken(searchById).get(0);
    registrationService.getStandardZaak(dossier);

    // then
    final ZaakIdentificaties identificaties = dossier.getZaakHistorie().getIdentificaties();
    assertTrue(isMockId(identificaties.getNummer(ZaakIdType.ZAAKSYSTEEM.getCode())));
    assertEquals(zaakId, identificaties.getNummer(ZaakIdType.PROWEB_PERSONEN.getCode()));
  }

  @Test
  public void getStandardZaakMustRetrieveGenericAndSpecificDossier() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();
    dossierRegistration.getDossier().toevoegenPersoon(newRegistrant());
    final Dossier dossier = saveAndRetrieveMinimalDossier(dossierRegistration);

    // when
    final Dossier standardDossier = registrationService.getStandardZaak(dossier);

    // then
    assertEquals(dossierRegistration.getCode(), standardDossier.getCode());
    assertTrue(standardDossier.getZaakDossier() instanceof DossierRegistration);
    final DossierRegistration specificDossier = (DossierRegistration) standardDossier.getZaakDossier();
    assertEquals(dossierRegistration.getCode(), specificDossier.getCode());
    // then people must be loaded
    List<DossierPersoon> people = standardDossier.getPersonen();
    assertEquals(1, people.size());
    DossierPersoon loadedPerson = people.get(0);
    assertEquals(FAMILY_NAME, loadedPerson.getGeslachtsnaam());
    assertEquals(MALE, loadedPerson.getGeslacht());
    assertEquals(BIRTH_DATE, loadedPerson.getDatumGeboorte());
  }

  @Test
  public void deleteMustDeleteGenericAndSpecificDossier() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();
    final Dossier dossier = saveAndRetrieveMinimalDossier(dossierRegistration);
    final CalledServiceListener listener = new CalledServiceListener();
    registrationService.setListener(listener);

    // when
    registrationService.delete(dossier);

    // then
    assertDossCount(0L, dossierRegistration);
    assertDossRegistrationCount(0L, dossierRegistration);
    assertTrue(listener.isCalled());
  }

  @Test
  public void zakenServiceDeleteMustDeleteGenericAndSpecificDossier() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();
    registrationService.saveRegistration(dossierRegistration);

    final ZaakKey zaakKey = new ZaakKey(dossierRegistration.getDossier().getZaakId(), ZaakType.REGISTRATION);
    services.getZakenService().delete(zaakKey);

    // then
    assertDossCount(0L, dossierRegistration);
    assertDossRegistrationCount(0L, dossierRegistration);
  }

  @Test
  public void saveDossierMustCreatePersonEntities() {
    // given
    final DossierRegistration dossierRegistration = newDossierRegistration();

    final DossierPersoon person1 = newRegistrant();
    person1.setGeboorteland(new FieldValue(COUNTRY));
    person1.setGeboorteplaats(new FieldValue(MUNICIPALITY));
    dossierRegistration.getDossier().toevoegenPersoon(person1);

    // when
    saveAndRetrieveMinimalDossier(dossierRegistration);

    // then
    final List<DossPer> dossierPers = GenericDao.createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(NUMBER_OF_PEOPLE, dossierPers.size());
    assertEquals(FAMILY_NAME, dossierPers.get(0).getGeslachtsnaam());
    assertEquals(MALE, Geslacht.get(dossierPers.get(0).getGesl()));
  }

  @Test
  public void saveRegistrationMustAddChangeAndDeleteTravelDocs() {
    // given registration with travel doc
    DossierRegistration dossierRegistration = newDossierRegistration();
    DossierPersoon person = new DossierPersoon();
    services.getDossierService().savePersoon(person);
    DossTravelDoc dossTravelDoc = new DossTravelDoc(person);
    String initialDocNr = "N1";
    dossTravelDoc.setDocNr(initialDocNr);
    person.addDossTravelDoc(dossTravelDoc);
    dossierRegistration.getDossier().toevoegenPersoon(person);

    // when
    registrationService.saveRegistration(dossierRegistration);

    // then
    List<DossTravelDoc> travelDocs = GenericDao.createQuery("SELECT d FROM DossTravelDoc d", DossTravelDoc.class)
        .getResultList();
    assertEquals(1, travelDocs.size());
    DossTravelDoc createdTravelDoc = travelDocs.get(0);
    assertTrue(createdTravelDoc.getCDossTravelDoc() > 0);
    assertEquals(person.getCDossPers(), createdTravelDoc.getDossPers().getCDossPers());
    assertEquals(initialDocNr, createdTravelDoc.getDocNr());

    // when update travel doc and save registration
    String updatedDocNr = "N2";
    dossierRegistration.getDossier().getPersonen().get(0).getDossTravelDocs().get(0).setDocNr(updatedDocNr);
    registrationService.saveRegistration(dossierRegistration);

    // then travel doc should have been updated
    travelDocs = GenericDao.createQuery("SELECT d FROM DossTravelDoc d", DossTravelDoc.class).getResultList();
    assertEquals(1, travelDocs.size());
    createdTravelDoc = travelDocs.get(0);
    assertEquals(updatedDocNr, createdTravelDoc.getDocNr());

    // when remove travel doc and save registration
    person.removeDossTravelDoc(travelDocs.get(0));
    registrationService.saveRegistration(dossierRegistration);

    // then
    travelDocs = GenericDao.createQuery("SELECT d FROM DossTravelDoc d", DossTravelDoc.class)
        .getResultList();
    assertEquals(0, travelDocs.size());
  }

  @Test
  public void saveRegistrationMustAddAndDeleteSourceDocs() {
    // given registration with source doc
    DossierRegistration dossierRegistration = newDossierRegistration();
    DossierPersoon person = new DossierPersoon();
    services.getDossierService().savePersoon(person);
    DossSourceDoc sourceDoc = DossSourceDoc.newNotSetSourceDocument();
    person.setDossSourceDoc(sourceDoc);
    dossierRegistration.getDossier().toevoegenPersoon(person);

    // when
    registrationService.saveRegistration(dossierRegistration);

    // then
    List<DossSourceDoc> sourceDocs = GenericDao.createQuery("SELECT d FROM DossSourceDoc d", DossSourceDoc.class)
        .getResultList();
    assertEquals(1, sourceDocs.size());
    DossSourceDoc createdSourceDoc = sourceDocs.get(0);
    assertTrue(createdSourceDoc.getCDossSourceDoc().longValue() > 0);
    Object cDossSourceDoc = GbaJpa.getManager().createNativeQuery("SELECT c_doss_source_doc FROM doss_pers")
        .getResultList().get(0);
    assertEquals(createdSourceDoc.getCDossSourceDoc(), cDossSourceDoc);

    // when remove travel doc and save registration
    person.removeDossSourceDoc();
    registrationService.saveRegistration(dossierRegistration);

    // then
    sourceDocs = GenericDao.createQuery("SELECT d FROM DossSourceDoc d", DossSourceDoc.class)
        .getResultList();
    assertEquals(0, sourceDocs.size());
  }

  private DossierRegistration newDossierRegistration() {
    final DossierRegistration dossierRegistration = ZaakUtils.newZaakDossier(registrationService.newDossier(),
        services);
    dossierRegistration.setHouseNumber(HOUSE_NUMBER);
    registrationService.getServices().getZaakDmsService().genereerZaakId(dossierRegistration.getDossier());
    return dossierRegistration;
  }

  private Dossier saveAndRetrieveMinimalDossier(DossierRegistration dossierRegistration) {
    registrationService.saveRegistration(dossierRegistration);
    return getMinimalDossier(dossierRegistration);
  }

  private Dossier getMinimalDossier(DossierRegistration dossierRegistration) {
    final ZaakArgumenten searchById = new ZaakArgumenten(dossierRegistration.getDossier().getZaakId());
    return dossierService.getMinimalZaken(searchById).get(0);
  }

  private void assertDossRegistrationCount(long expected, DossierRegistration dossierRegistration) {
    Query query;
    query = GenericDao.createQuery("SELECT COUNT(d) FROM DossRegistration d WHERE d.cDossRegistration = :id");
    query.setParameter("id", dossierRegistration.getCode());
    assertEquals(expected, query.getResultList().get(0));
  }

  private void assertDossCount(long expected, DossierRegistration dossierRegistration) {
    final Query query = GenericDao.createQuery("SELECT COUNT(d) FROM Doss d WHERE d.cDoss = :id");
    query.setParameter("id", dossierRegistration.getCode());
    assertEquals(expected, query.getResultList().get(0));
  }

  private List<ZaakId> getZaakIdsByInternId(String internId) {
    final TypedQuery<ZaakId> query = GenericDao.createQuery("SELECT z FROM ZaakId z WHERE z.id.internId = :id",
        ZaakId.class);
    query.setParameter("id", internId);
    return query.getResultList();
  }

  private static DossierPersoon newRegistrant() {
    DossierPersoon person = new DossierPersoon(DossierPersoonType.INSCHRIJVER);
    person.setGeslachtsnaam(RegistrationServiceTest.FAMILY_NAME);
    person.setGeslacht(RegistrationServiceTest.MALE);
    person.setDatumGeboorte(RegistrationServiceTest.BIRTH_DATE);

    return person;
  }

  private static class CalledServiceListener implements ServiceListener {

    private boolean called;

    CalledServiceListener() {
      called = false;
    }

    @Override
    public void action(ServiceEvent event) {
      called = true;
    }

    @Override
    public String getId() {
      return "TEST_LISTENER";
    }

    boolean isCalled() {
      return called;
    }
  }
}
