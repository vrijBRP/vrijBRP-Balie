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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.registration;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.registration.relations.RelationDateStartUtils.getValidityDate;
import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.*;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.bs.registration.relations.RelationDateStartUtils;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GbaRestRegistrationHandler extends GbaRestDossierHandler {

  public GbaRestRegistrationHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {
    if (dossier.getZaakDossier() instanceof DossierRegistration) {
      addPersonen(parent, dossier);
    }
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier dossier) {
    GbaRestElement registrationElement = dossierElement.add(INSCHRIJVING);
    DossierRegistration registration = (DossierRegistration) dossier.getZaakDossier();
    addAlgemeneElementen(registrationElement, dossier);
    setDeclaration(registrationElement, registration);
    setRelations(registrationElement, dossier);
  }

  private void setDeclaration(GbaRestElement registrationElement, DossierRegistration registration) {
    GbaRestElement declarationElement = registrationElement.add(AANGIFTE);

    DateTime datumAangifte = registration.getDossier().getDatumIngang();
    add(declarationElement, DATUM_INGANG, datumAangifte);
    add(declarationElement, HERKOMST_SITUATIE,
        OriginSituationType.valueOfCode(registration.getOriginSituation()).getCode());
    add(declarationElement, LAND_VERTREK, Container.LAND.get(registration.getDepartureCountry().getStringValue()));

    GbaRestElement interpreter = declarationElement.add(TOLK);
    InterpreterType interpreterType = InterpreterType.valueOfCode(registration.getInterpreter());
    add(interpreter, TYPE, interpreterType.getCode(), interpreterType.getDescription());
    add(interpreter, NAAM, registration.getInterpreterName());
    add(interpreter, TAAL, registration.getInterpreterLanguage());

    GbaRestElement addressElement = declarationElement.add(ADRES);
    add(addressElement, STRAAT, registration.getStreet());
    add(addressElement, HNR, registration.getHouseNumber());
    add(addressElement, HNR_L, registration.getHouseNumberL());
    add(addressElement, HNR_T, registration.getHouseNumberT());
    add(addressElement, HNR_A, registration.getHouseNumberA());
    add(addressElement, POSTCODE, registration.getPostalCode());
    add(addressElement, WOONPLAATS, registration.getResidence());
    add(addressElement, FUNCTIEADRES, registration.getAddressFunction());
    add(addressElement, AANTAL_PERSONEN, registration.getNoOfPeople());
    add(addressElement, DUUR, registration.getDuration());

    ConsentProvider consentProvider = registration.getConsent();
    GbaRestElement consentElement = declarationElement.add(TOESTEMMINGEVER);
    // TOESTEMMING_STATUS: -1 = not declared, 0 = denied, 1 = consent
    add(consentElement, TOESTEMMING_STATUS, astr(consentProvider.getConsentCode()));
    add(consentElement, BSN, new BsnFieldValue(consentProvider.getBsn()));
    add(consentElement, ANDERS, consentProvider.getOtherConsentProvider());

    RegistrationDeclarant declarant = registration.getDeclarant();
    GbaRestElement declarantElement = registrationElement.add(AANGEVER);
    add(declarantElement, TYPE, astr(declarant.getCode()));
    if (declarant.getPerson().isPresent()) {
      add(declarantElement, BSN, declarant.getPerson().get().getBurgerServiceNummer());
    }
  }

  private void setRelations(GbaRestElement registrationElement, Dossier dossier) {
    // Get relations
    RelationService service = getServices().getRelationService();
    List<DossierPersoon> people = dossier.getPersonen();
    List<Relation> relations = Relation.fromDossPersRelations(service.findByPeople(people), people);
    GbaRestElement relaties = registrationElement.add(RELATIES);

    for (Relation relation : relations) {
      GbaRestElement relElement = relaties.add(RELATIE);
      DossPersRelation rel = relation.getRelation();

      // Date
      RelationshipDateType dateType = RelationshipDateType.valueOfCode(rel.getStartDateType());
      RelationType relationType = RelationType.valueOfCode(rel.getRelationShipType());
      RelationMatchType matchType = RelationMatchType.valueOfCode(rel.getMatchType());

      add(relElement, DATUM_TYPE, dateType.getCode(), dateType.getDescription());
      add(relElement, DATUM_INGANG, RelationDateStartUtils.getRelationDate(rel, relationType));
      add(relElement, RELATIE_TYPE, relationType.getCode(), relationType.getOms());
      add(relElement, MATCH_TYPE, matchType.getCode(), matchType.getDescription());
      add(relElement, VOORNAMEN, rel.getVoorn());
      add(relElement, GESLACHTSNAAM, rel.getGeslachtsnaam());
      add(relElement, VOORVOEGSEL, rel.getVoorv());
      add(relElement, TITEL_PREDIKAAT, rel.getTp());

      addCommitment(relElement, rel, relationType);
      addRelationSourceDocument(relElement, relationType, rel);

      // Persons
      DossierPersoon person1 = dossier.getPersoon(deepCopyBean(DossierPersoon.class, rel.getPerson()));
      DossierPersoon person2 = dossier.getPersoon(deepCopyBean(DossierPersoon.class, rel.getRelatedPerson()));

      GbaRestElement personen = relElement.add(GbaRestElementType.PERSONEN);
      addPersoon(personen, getSoort(person1.getDossierPersoonType()), person1);
      addPersoon(personen, getSoort(person2.getDossierPersoonType()), person2);
    }
  }

  private void addCommitment(GbaRestElement relElement, DossPersRelation rel, RelationType relationType) {
    CommitmentType commitmentType = CommitmentType.valueOfCode(rel.getCommitmentType());
    if (RelationType.PARTNER_OF == relationType) {

      //Start commitment
      GbaRestElement startElement = relElement.add(VERBINTENIS);
      add(startElement, SOORT, commitmentType.getCode(), commitmentType.getDescription());
      add(startElement, DATUM_INGANG, new DateTime(rel.getCustomStartDate()));
      if (Globalfunctions.pos(rel.getStartDateMunicipality())) {
        add(startElement, PLAATS, GbaTables.PLAATS.get(rel.getStartDateMunicipality()));
      } else {
        add(startElement, PLAATS, rel.getStartDateMunicipality());
      }
      add(startElement, LAND, GbaTables.LAND.get(rel.getStartDateCountry()));

      // End commitment
      if (pos(rel.getCustomEndDate())) {
        GbaRestElement endElement = relElement.add(ONTBINDING);
        add(endElement, DATUM_INGANG, new DateTime(rel.getCustomEndDate()));
        if (Globalfunctions.pos(rel.getEndDateMunicipality())) {
          add(endElement, PLAATS, GbaTables.PLAATS.get(rel.getEndDateMunicipality()));
        } else {
          add(endElement, PLAATS, rel.getEndDateMunicipality());
        }
        add(endElement, LAND, GbaTables.LAND.get(rel.getEndDateCountry()));
        add(endElement, ONTBINDING_REDEN, GbaTables.REDEN_HUW_ONTB.get(rel.getEndReason()));
      }
    }
  }

  private void addRelationSourceDocument(GbaRestElement relElement, RelationType relationType, DossPersRelation rel) {
    DossSourceDoc sourceDoc = rel.getDossSourceDoc();
    SourceDocumentType sourceDocType = SourceDocumentType.valueOfCode(sourceDoc.getDocType());
    ValidityDateType dateType = ValidityDateType.valueOfCode(sourceDoc.getValidityDateType());

    GbaRestElement sourceDocElement = relElement.add(BRONDOCUMENT);
    add(sourceDocElement, DATUM_TYPE, dateType.getCode(), dateType.getDescription());
    add(sourceDocElement, DATUM_INGANG, getValidityDate(sourceDoc, rel, relationType));
    add(sourceDocElement, TYPE, sourceDocType.getCode(), sourceDocType.getDescription());
    add(sourceDocElement, NUMMER, sourceDoc.getDocNumber());
    add(sourceDocElement, PLAATS, Container.PLAATS.get(sourceDoc.getDocMun()));
    add(sourceDocElement, OMSCHRIJVING, sourceDoc.getDocDescr());
  }
}
