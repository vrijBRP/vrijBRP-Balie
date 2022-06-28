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

import static nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakDocumentVertrouwelijkheid.ZEER_GEHEIM;
import static nl.procura.gba.web.rest.v2.resources.GbaRestZaakResourceV2TestUtils.getResourceV2;
import static nl.procura.gba.web.rest.v2.resources.GbaRestZaakResourceV2TestUtils.jsonResourceToObject;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.BvhPark;
import nl.procura.gba.web.application.GbaApplicationMock;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakDocumentToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.*;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.*;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GbaRestZaakResourceV2ServerRelocationTest {

  private static final long   BSN_INGESCHREVENE   = Testdata.TEST_BSN_1;
  private static final long   BSN_ECHTGENOOT      = Testdata.TEST_BSN_2;
  private static final String ADD_VERHUIZING_JSON = "addVerhuizing.json";

  private GbaRestZaakResourceV2Server resource;

  @Before
  public void setUp() {
    resource = getResourceV2(GbaRestZaakResourceV2Server.class);
    // set application path for LokaleDmsService
    GbaApplicationMock.getInstance();
  }

  @Test
  public void addVerhuizingMustReturnValidResponseAndSaveInDatabase() {
    // given
    GbaRestZaakToevoegenVraag vraag = jsonResourceToObject(getClass(), ADD_VERHUIZING_JSON,
        GbaRestZaakToevoegenVraag.class);
    // when
    GbaRestAntwoord<GbaRestZaak> response = resource.addZaak(vraag);
    // then validate response
    assertTrue(response.isSucces());
    GbaRestZaakAlgemeen algemeen = response.getInhoud().getAlgemeen();
    assertFalse(algemeen.getOmschrijving().isEmpty());
    assertFalse(algemeen.getIds().isEmpty());
    assertEquals(GbaRestZaakStatusType.WACHTKAMER, algemeen.getStatus());
    assertFalse(algemeen.getStatussen().isEmpty());

    GbaRestVerhuizing verhuizing = response.getInhoud().getVerhuizing();
    GbaRestInwoning inwoning = verhuizing.getInwoning();
    assertEquals(GbaRestAangifteStatus.GEACCEPTEERD, inwoning.getAangifteStatus());
    assertEquals(GbaRestToestemmingStatus.JA, inwoning.getToestemmingStatus());
    // then validate database
    List<BvhPark> zaken = GenericDao.createQuery("SELECT d FROM BvhPark d ORDER BY d.bsn", BvhPark.class)
        .getResultList();
    assertEquals("There must be a change of address case for each person", 2, zaken.size());

    BvhPark ingeschrevene = zaken.get(0);
    assertEquals(BigDecimal.valueOf(BSN_INGESCHREVENE), ingeschrevene.getBsn());
    assertEquals(BigDecimal.valueOf(BSN_INGESCHREVENE), ingeschrevene.getAangifteBsn());
    assertEquals(AangifteSoort.INGESCHREVENE.getCode(), ingeschrevene.getAangifte());
    assertEquals(FunctieAdres.WOONADRES.getCode(), ingeschrevene.getFuncAdr());
    assertEquals(BigDecimal.valueOf(ZaakStatusType.WACHTKAMER.getCode()), ingeschrevene.getIndVerwerkt());
    assertEquals(vraag.getZaak().getAlgemeen().getZaakId(), ingeschrevene.getZaakId());
    assertEquals(BigDecimal.valueOf(VerhuisType.BINNENGEMEENTELIJK.getCode()), ingeschrevene.getVerhuisType());
    assertEquals(BigDecimal.valueOf(1), ingeschrevene.getAantPers());

    BvhPark echtgenoot = zaken.get(1);
    assertEquals(BigDecimal.valueOf(BSN_ECHTGENOOT), echtgenoot.getBsn());
    assertEquals(BigDecimal.valueOf(BSN_INGESCHREVENE), echtgenoot.getAangifteBsn());
    assertEquals(AangifteSoort.ECHTGENOOT_GEREGISTREERD_PARTNER.getCode(), echtgenoot.getAangifte());
    assertEquals(FunctieAdres.WOONADRES.getCode(), echtgenoot.getFuncAdr());
    assertEquals(BigDecimal.valueOf(ZaakStatusType.WACHTKAMER.getCode()), echtgenoot.getIndVerwerkt());
    assertEquals(vraag.getZaak().getAlgemeen().getZaakId(), echtgenoot.getZaakId());
    assertEquals(BigDecimal.valueOf(VerhuisType.BINNENGEMEENTELIJK.getCode()), echtgenoot.getVerhuisType());
    assertEquals(BigDecimal.valueOf(1), echtgenoot.getAantPers());
  }

  @Test
  public void addVerhuizingWithoutZaakIdMustGenerateZaakId() {
    // given
    GbaRestZaakToevoegenVraag vraag = jsonResourceToObject(getClass(), ADD_VERHUIZING_JSON,
        GbaRestZaakToevoegenVraag.class);
    vraag.getZaak().getAlgemeen().setZaakId(null);
    // when
    GbaRestAntwoord<GbaRestZaak> response = resource.addZaak(vraag);
    // then generated zaak id must be returned
    String zaakId = response.getInhoud().getAlgemeen().getZaakId();
    assertFalse(zaakId.isEmpty());

    // then validate database
    List<BvhPark> zaken = GenericDao.createQuery("SELECT d FROM BvhPark d ORDER BY d.bsn", BvhPark.class)
        .getResultList();
    assertEquals("There must be a change of address case for each person", 2, zaken.size());
    for (BvhPark zaak : zaken) {
      assertEquals(zaakId, zaak.getZaakId());
    }
  }

  @Test
  public void getVerhuizingByZaakIdMustReturnValidResponse() {
    // given added zaak
    String zaakId = givenVerhuizingZaak();
    // when
    GbaRestAntwoord<GbaRestZaak> response = resource.getZaakByZaakId(zaakId);
    // then
    assertTrue(response.isSucces());
    assertEquals(GbaRestZaakStatusType.WACHTKAMER, response.getInhoud().getAlgemeen().getStatus());
    List<GbaRestVerhuizer> verhuizers = response.getInhoud().getVerhuizing().getVerhuizers();
    assertEquals(2, verhuizers.size());
    GbaRestVerhuizer ingeschrevene = verhuizers.stream()
        .filter(v -> v.getBsn() == BSN_INGESCHREVENE)
        .findFirst().orElseThrow((AssertionError::new));
    assertEquals(GbaRestAangifteSoort.INGESCHREVENE, ingeschrevene.getAangifte());
    GbaRestVerhuizer echtgenoot = verhuizers.stream()
        .filter(v -> v.getBsn() == BSN_ECHTGENOOT)
        .findFirst().orElseThrow((AssertionError::new));
    assertEquals(GbaRestAangifteSoort.ECHTGENOOT_GEREGISTREERD_PARTNER, echtgenoot.getAangifte());
  }

  @Test
  public void addZaakWithExistingZaakIdMustNotReturnSuccess() {
    // given added zaak
    GbaRestZaakToevoegenVraag vraag = jsonResourceToObject(getClass(), ADD_VERHUIZING_JSON,
        GbaRestZaakToevoegenVraag.class);
    resource.addZaak(vraag);
    // when add again
    GbaRestAntwoord<GbaRestZaak> response = resource.addZaak(vraag);
    assertFalse(response.isSucces());
  }

  @Test
  public void addDocumentMustSetVertrouwelijkheidToOnbekendWhenNotGiven() {
    // given added zaak
    String zaakId = givenVerhuizingZaak();
    // when
    String created = resource.addDocument(zaakId, givenZaakDocumentToevoegen(null)).getInhoud().getId();
    // then
    GbaRestZaakDocument createdDocument = getCreatedDocument(zaakId, created);
    assertEquals(GbaRestZaakDocumentVertrouwelijkheid.ONBEKEND, createdDocument.getVertrouwelijkheid());
  }

  @Test
  public void addDocumentMustSetVertrouwelijkheidToGivenValue() {
    // given
    String zaakId = givenVerhuizingZaak();
    // when
    String created = resource.addDocument(zaakId, givenZaakDocumentToevoegen(ZEER_GEHEIM)).getInhoud().getId();
    // then
    GbaRestZaakDocument createdDocument = getCreatedDocument(zaakId, created);
    assertEquals(ZEER_GEHEIM, createdDocument.getVertrouwelijkheid());
  }

  @Test
  public void addDocumentMustSetTypeToOnbekend() {
    // given
    String zaakId = givenVerhuizingZaak();
    // when
    String created = resource.addDocument(zaakId, givenZaakDocumentToevoegen(ZEER_GEHEIM)).getInhoud()
        .getBestandsnaam();
    // then
    VerhuisAanvraag verhuizing = new VerhuisAanvraag();
    verhuizing.setBurgerServiceNummer(new BsnFieldValue(BSN_INGESCHREVENE));
    verhuizing.setZaakId(zaakId);

    DMSDocument createdDocument = Services.getInstance().getDmsService().getDocumentsByZaak(verhuizing).getDocuments()
        .stream()
        .filter(d -> d.getContent().getFilename().equals(created))
        .findFirst()
        .orElseThrow(AssertionError::new);
    assertEquals(DocumentType.ONBEKEND.getType(), createdDocument.getDatatype());
  }

  @Test
  public void addDocumentMustFailWhenBestandsnaamIsNull() {
    // given
    String zaakId = givenVerhuizingZaak();
    GbaRestZaakDocumentToevoegenVraag request = givenZaakDocumentToevoegen(ZEER_GEHEIM);
    request.getDocument().setBestandsnaam(null);
    // when
    GbaRestAntwoord<GbaRestZaakDocument> antwoord = resource.addDocument(zaakId, request);
    // then
    assertFalse(antwoord.isSucces());
    assertEquals("document bestandsnaam is verplicht", antwoord.getFoutmelding());
  }

  private String givenVerhuizingZaak() {
    GbaRestZaakToevoegenVraag vraag = jsonResourceToObject(getClass(), ADD_VERHUIZING_JSON,
        GbaRestZaakToevoegenVraag.class);
    return resource.addZaak(vraag).getInhoud().getAlgemeen().getZaakId();
  }

  private static GbaRestZaakDocumentToevoegenVraag givenZaakDocumentToevoegen(
      GbaRestZaakDocumentVertrouwelijkheid vertrouwelijkheid) {
    GbaRestZaakDocument document = new GbaRestZaakDocument();
    document.setBestandsnaam("test.txt");
    document.setVertrouwelijkheid(vertrouwelijkheid);
    GbaRestZaakDocumentToevoegenVraag request = new GbaRestZaakDocumentToevoegenVraag();
    request.setDocument(document);

    return request;
  }

  private GbaRestZaakDocument getCreatedDocument(String zaakId, String documentId) {
    return resource.getDocumentsByZaakId(zaakId).getInhoud().getDocumenten().stream()
        .filter(d -> d.getId().equals(documentId))
        .findFirst()
        .orElseThrow(AssertionError::new);
  }
}
