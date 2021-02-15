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

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static nl.procura.gba.web.services.bs.registration.RelationMatchType.INAPPLICABLE;
import static nl.procura.gba.web.services.bs.registration.RelationType.PARENT_OF;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;

import lombok.Value;

public class RelationServiceTest extends ZaakServiceTest {

  private final RegistrationService registrationService;
  private final RelationService     relationService;

  public RelationServiceTest() {
    registrationService = services.getRegistrationService();
    relationService = services.getRelationService();
  }

  @Test
  public void saveMustCreateNewRecord() {
    // given
    RegistrationResult registration = givenRegistration();
    DossPersRelation relation = new DossPersRelation(null,
        registration.person,
        PARENT_OF.getCode(),
        registration.relatedPerson,
        INAPPLICABLE.getCode());

    // when
    relationService.save(relation);

    // then relation in database must be the same
    List<DossPersRelation> relations = getRelations();
    assertEquals(1, relations.size());
    DossPersRelation createdRelation = relations.get(0);
    assertEquals(registration.person.getCDossPers(), createdRelation.getPerson().getCDossPers());
    assertEquals(registration.relatedPerson.getCDossPers(), createdRelation.getRelatedPerson().getCDossPers());
    assertEquals(BigDecimal.valueOf(1), createdRelation.getRelationShipType());
  }

  @Test
  public void removeRelationMustNotDeletePeople() {
    // given
    RegistrationResult registration = givenRegistration();
    DossPersRelation relation = new DossPersRelation(null,
        registration.person,
        PARENT_OF.getCode(),
        registration.relatedPerson,
        INAPPLICABLE.getCode());

    relationService.save(relation);

    // when
    relationService.remove(relation);

    // then relation in database must not exist
    List<DossPersRelation> relations = getRelations();
    assertEquals(0, relations.size());

    // then people must still exist
    List<DossPer> people = GenericDao
        .createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(2, people.size());
  }

  @Test
  public void removePersonMustRemoveRelation() {
    // given
    RegistrationResult registration = givenRegistration();
    DossPersRelation relation = new DossPersRelation(null,
        registration.person,
        PARENT_OF.getCode(),
        registration.relatedPerson,
        INAPPLICABLE.getCode());

    relationService.save(relation);

    // when
    services.getDossierService().deletePersonen(registration.registration.getDossier(), singleton(registration.person));

    // then relation in database must not exist
    List<DossPersRelation> relations = getRelations();
    assertEquals(0, relations.size());

    // then related person must still exist
    List<DossPer> people = GenericDao
        .createQuery("SELECT d FROM DossPer d", DossPer.class).getResultList();
    assertEquals(1, people.size());
    assertEquals(relation.getRelatedPerson().getCDossPers(), people.get(0).getCDossPers());
  }

  @Test
  public void findByPeopleMustReturnExpectedResults() {
    // given 2 registrations with relations
    RegistrationResult registration1 = givenRegistration();
    DossPersRelation relation1 = new DossPersRelation(null,
        registration1.person,
        PARENT_OF.getCode(),
        registration1.relatedPerson,
        INAPPLICABLE.getCode());

    relationService.save(relation1);
    RegistrationResult registration2 = givenRegistration();
    DossPersRelation relation2 = new DossPersRelation(null,
        registration2.person,
        PARENT_OF.getCode(),
        registration2.relatedPerson,
        INAPPLICABLE.getCode());
    relationService.save(relation2);

    // when search all relations for person of registration1
    List<DossPersRelation> relations = relationService
        .findByPeople(singletonList(registration1.person));
    // then expect only first relation
    assertEquals(1, relations.size());
    assertRelationPerson(registration1, relations.get(0));

    // when search all relations for related person of registration1
    relations = relationService.findByPeople(singletonList(registration1.relatedPerson));
    // then expect only first relation
    assertEquals(1, relations.size());
    assertRelationPerson(registration1, relations.get(0));

    // when search all relations for person of registration1 and person of registation2
    relations = relationService.findByPeople(asList(registration1.person, registration2.person));
    // then expect all relations
    assertEquals(2, relations.size());
  }

  private void assertRelationPerson(RegistrationResult expected, DossPersRelation actual) {
    assertEquals(expected.person.getCDossPers(), actual.getPerson().getCDossPers());
    assertEquals(expected.relatedPerson.getCDossPers(), actual.getRelatedPerson().getCDossPers());
  }

  private List<DossPersRelation> getRelations() {
    return GenericDao.createQuery("SELECT d FROM DossPersRelation d", DossPersRelation.class).getResultList();
  }

  private RegistrationResult givenRegistration() {
    DossierRegistration dossierRegistration = ZaakUtils.newZaakDossier(registrationService.newDossier(), services);
    dossierRegistration.setHouseNumber(1L);
    DossierPersoon person = new DossierPersoon();
    dossierRegistration.getDossier().toevoegenPersoon(person);
    DossierPersoon relatedPerson = new DossierPersoon();
    dossierRegistration.getDossier().toevoegenPersoon(relatedPerson);
    registrationService.saveRegistration(dossierRegistration);
    return new RegistrationResult(dossierRegistration, person, relatedPerson);
  }

  @Value
  private class RegistrationResult {

    public final DossierRegistration registration;
    public final DossierPersoon      person;
    public final DossierPersoon      relatedPerson;
  }
}
