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

package nl.procura.gba.web.services.bs.registration.document;

import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.*;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.bs.registration.relations.RelationDateStartUtils;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.standard.Globalfunctions;

import lombok.Data;

public class RegistrationTemplateData extends DocumentTemplateData {

  private static final int MAX_PARENTS = 2;

  private final Services services;

  public RegistrationTemplateData(Services services, DossierRegistration firstRegistrationDossier,
      DossierPersoon person) {
    this.services = services;
    put("items", Stream.of(firstRegistrationDossier)
        .map(DossierRegistrationData::new)
        .collect(Collectors.toList()));
    put("aanschrijfpersoon", person);
  }

  private Adresformats getAddress(DossierRegistration r) {

    String straat = r.getStreet();
    String hnr = r.getHouseNumber().toString();
    String hnrL = r.getHouseNumberL();
    String hnrT = r.getHouseNumberT();
    String hnrA = r.getHouseNumberA();
    String pc = r.getPostalCode();
    String wpl = WOONPLAATS.get(r.getResidence()).toString();

    return new Adresformats().setValues(straat, hnr, hnrL, hnrT, hnrA, "", pc, "", wpl, "", "", "", "", "", "", "");
  }

  private class DossierRegistrationData extends DocumentTemplateData {

    DossierRegistrationData(DossierRegistration r) {
      put("zaak", new CaseData(r));
      put("anr", r.getAnummer());
      put("bsn", r.getBurgerServiceNummer());
      put("landHerkomst", LAND.get(r.getDepartureCountry().getStringValue()));
      put("adres", getAddress(r));
      put("functieAdres", FunctieAdres.get(r.getAddressFunction()));
      put("duur", StaydurationType.valueOfCode(r.getDuration()));
      put("toestemming", new ConsentData(r));
      put("aantalPersonen", r.getNoOfPeople());
      put("aangever", new DeclarantData(r));
      put("tolk", new InterpreterData(r));
      put("personen", new PersonsData(r));
    }
  }

  private class PersonsData extends DocumentTemplateData {

    public PersonsData(DossierRegistration r) {
      List<DossierPersoon> people = r.getDossier().getPersonen();
      put("inschrijvers", r.getDossier()
          .getPersonen(DossierPersoonType.INSCHRIJVER)
          .stream()
          .map((DossierPersoon p) -> new ApplicantData(p, people))
          .collect(Collectors.toList()));
    }
  }

  private class ApplicantData extends DocumentTemplateData {

    public ApplicantData(DossierPersoon p, List<DossierPersoon> allPeople) {
      put("persoon", toDocumentDossierPersoon(p));
      put("brondocument", new SourceDocumentData(p.getDossSourceDoc().get(), p));
      put("natOpsomming", p.getNationaliteiten()
          .stream()
          .map(n -> n.getNationaliteit().toString())
          .collect(Collectors.joining(", ")));
      put("buitenlandsNrOpsomming", p.getNationaliteiten()
          .stream()
          .filter(n -> isNotBlank(n.getSourceForeignId()))
          .map(n -> n.getSourceForeignId())
          .collect(Collectors.joining(", ")));
      put("nationaliteiten", p.getNationaliteiten()
          .stream()
          .map((DossierNationaliteit nat) -> new NationalityData(nat, p))
          .collect(Collectors.toList()));
      put("aangifte", new DeclarationData(p));
      put("naamgebruik", GbaTables.NAAMGEBRUIK.get(p.getNaamgebruik()));
      put("eerderGewoond", p.getPrevType().equalsIgnoreCase("Y") ? "Ja" : "Nee");
      put("eerderGewoondOmschrijving", StringUtils.uncapitalize(p.getPrevDescription()));
      put("contact", new ContactData(p));
      put("id", new IdData(p));

      RelationService relationService = services.getRelationService();
      List<DossPersRelation> dossRelations = relationService.findByPerson(p);
      List<RelationData> relations = Relation.fromDossPersRelations(dossRelations, allPeople)
          .stream()
          .map(RelationData::new)
          .collect(Collectors.toList());

      put("relaties", relations);
      setPartners(relations);
      setParents(relations);
      setChildren(relations);
    }

    private void setParents(List<RelationData> relations) {
      // Existing parents
      List<RelationData> parents = relations.stream().filter(RelationData::isChildOf).collect(Collectors.toList());
      put("ouders", parents);

      // No legal parents
      List<PersonData> noParents = new ArrayList<>();
      relations.stream().filter(RelationData::isOnlyOnParent)
          .forEach(relation -> noParents.add(new PersonData("Juridisch geen ouder")));

      relations.stream()
          .filter(RelationData::isNoLegalParents)
          .forEach(relation -> {
            noParents.add(new PersonData("Juridisch geen ouder"));
            noParents.add(new PersonData("Juridisch geen ouder"));
          });

      // Missing parents
      int currentParents = parents.size() + noParents.size();
      if (currentParents < MAX_PARENTS) {
        for (int i = currentParents; i < MAX_PARENTS; i++) {
          noParents.add(new PersonData("Onbekende ouder"));
        }
      }

      put("geen_ouders", noParents);
    }

    private void setPartners(List<RelationData> relations) {
      List<RelationData> partners = relations.stream().filter(RelationData::isPartnerOf).collect(Collectors.toList());
      put("partners", partners);
      List<PersonData> noPartners = new ArrayList<>();
      if (partners.isEmpty()) {
        noPartners.add(new PersonData(""));
      }
      put("geen_partners", noPartners);
    }

    private void setChildren(List<RelationData> relations) {
      List<RelationData> children = relations.stream().filter(RelationData::isParentOf).collect(Collectors.toList());
      put("kinderen", children);
      List<PersonData> noChildren = new ArrayList<>();
      if (children.isEmpty()) {
        noChildren.add(new PersonData(""));
      }
      put("geen_kinderen", noChildren);
    }
  }

  @Data
  private class PersonData extends DocumentTemplateData {

    public PersonData(String naam) {
      put("naam", naam);
    }
  }

  private class IdData extends DocumentTemplateData {

    public IdData(DossierPersoon p) {
      put("type", IdentificatieType.get(p.getSoort()));
      put("land", GbaTables.LAND.get(p.getIssueingCountry().getStringValue()));
      put("nummer", p.getIdDocNr());
    }
  }

  private class ContactData extends DocumentTemplateData {

    public ContactData(DossierPersoon p) {
      String email = "";
      String tel = "";
      for (PlContactgegeven contact : services.getContactgegevensService().getContactgegevens(p)) {
        if (contact.getContactgegeven().isGegeven(EMAIL)) {
          email = contact.getAant();
        }
        // Mobile first, home second
        if (contact.getContactgegeven().isGegeven(TEL_MOBIEL, TEL_THUIS)) {
          if (StringUtils.isBlank(tel)) {
            tel = contact.getAant();
          }
        }
      }
      put("email", email);
      put("tel", tel);
    }
  }

  private class CaseData extends DocumentTemplateData {

    public CaseData(DossierRegistration r) {
      put("zaakId", r.getDossier().getZaakId());
      put("datumIngang", r.getDossier().getDatumIngang());
      put("datumTijdInvoer", r.getDossier().getDatumTijdInvoer());
      put("ingevoerdDoor", r.getDossier().getIngevoerdDoor());
      put("locatieInvoer", r.getDossier().getLocatieInvoer());
    }
  }

  private class RelationData extends DocumentTemplateData {

    private Relation relation;

    public RelationData(Relation r) {
      this.relation = r;
      put("type", getRelationType());
      RelationshipDateType dateType = RelationshipDateType.valueOfCode(r.getRelation().getStartDateType());
      put("datumType", dateType);
      put("sinds", RelationDateStartUtils.getRelationDate(r.getRelation(), getRelationType()));
      put("isSprakeVanVerbintenis", isPartnerOf());
      put("gerelateerde", toDocumentDossierPersoon(r.getRelatedPerson()));
      put("verbintenis", new CommitmentData(r));
      put("brondocument", new RelationSourceDocumentData(r.getSourceDoc(), r.getRelation(), getRelationType()));
    }

    public boolean isParentOf() {
      return RelationType.PARENT_OF == getRelationType();
    }

    public boolean isChildOf() {
      return RelationType.CHILD_OF == getRelationType();
    }

    public boolean isOnlyOnParent() {
      return RelationType.ONLY_1_LEGAL_PARENT == getRelationType();
    }

    public boolean isNoLegalParents() {
      return RelationType.NO_LEGAL_PARENTS == getRelationType();
    }

    public boolean isPartnerOf() {
      return RelationType.PARTNER_OF == getRelationType();
    }

    public RelationType getRelationType() {
      return RelationType.valueOfCode(relation.getRelation().getRelationShipType());
    }
  }

  private class CommitmentData extends DocumentTemplateData {

    public CommitmentData(Relation r) {
      CommitmentType commitmentType = CommitmentType.valueOfCode(r.getRelation().getCommitmentType());
      put("isSprakeVanHuwelijk", Boolean.valueOf(CommitmentType.MARRIAGE == commitmentType));
      put("type", commitmentType);
      put("start", new StartCommitmentData(r));
      put("einde", new EndCommitmentData(r));
    }
  }

  private class NationalityData extends DocumentTemplateData {

    public NationalityData(DossierNationaliteit nat, DossierPersoon p) {
      put("nationaliteit", nat.getNationaliteit());

      put("datumType", nat.getVerkrijgingType());
      if (nat.getDatumVerkrijging().getLongDate() >= 0) {
        put("sinds", nat.getDatumVerkrijging().toString());
      } else {
        if (DossierNationaliteitDatumVanafType.GEBOORTE_DATUM == nat.getVerkrijgingType()) {
          put("sinds", p.getDatumGeboorte());
        } else if (DossierNationaliteitDatumVanafType.ONBEKEND == nat.getVerkrijgingType()) {
          put("sinds", nat.getDatumVerkrijging().getFormatDate());
        }
      }

      put("brondocument", nat.getSourceDescription());
      put("buitenlandsNr", nat.getSourceForeignId());
    }
  }

  private class SourceDocumentData extends DocumentTemplateData {

    public SourceDocumentData(DossSourceDoc d, DossierPersoon p) {
      put("type", SourceDocumentType.valueOfCode(d.getDocType()));
      put("nummer", d.getDocNumber());
      put("omschrijving", d.getDocDescr());
      put("plaats", PLAATS.get(d.getDocMun()).toString());
      put("sinds", RelationDateStartUtils.getValidityDate(d, p));
      put("datumType", ValidityDateType.valueOfCode(d.getValidityDateType()));
    }
  }

  private class RelationSourceDocumentData extends DocumentTemplateData {

    public RelationSourceDocumentData(DossSourceDoc d, DossPersRelation rel, RelationType relationType) {
      put("type", SourceDocumentType.valueOfCode(d.getDocType()));
      put("nummer", d.getDocNumber());
      put("omschrijving", d.getDocDescr());
      put("plaats", PLAATS.get(d.getDocMun()).toString());
      put("sinds", RelationDateStartUtils.getValidityDate(d, rel, relationType));
      put("datumType", ValidityDateType.valueOfCode(d.getValidityDateType()));
    }
  }

  private class StartCommitmentData extends DocumentTemplateData {

    public StartCommitmentData(Relation r) {
      put("datum", new DateTime(r.getRelation().getCustomStartDate()));
      String startDateMunicipality = r.getRelation().getStartDateMunicipality();
      if (Globalfunctions.pos(startDateMunicipality)) {
        put("plaats", PLAATS.get(startDateMunicipality));
      } else {
        put("plaats", startDateMunicipality);
      }
      put("land", GbaTables.LAND.get(r.getRelation().getStartDateCountry()));
    }
  }

  private class EndCommitmentData extends DocumentTemplateData {

    public EndCommitmentData(Relation r) {
      put("datum", new DateTime(r.getRelation().getCustomEndDate()));
      put("isOntbonden", Globalfunctions.pos(r.getRelation().getCustomEndDate()));
      String endDateMunicipality = r.getRelation().getEndDateMunicipality();
      if (Globalfunctions.pos(endDateMunicipality)) {
        put("plaats", PLAATS.get(endDateMunicipality));
      } else {
        put("plaats", endDateMunicipality);
      }
      put("land", GbaTables.LAND.get(r.getRelation().getEndDateCountry()));
      put("reden", GbaTables.REDEN_HUW_ONTB.get(r.getRelation().getEndReason()));
    }
  }

  private class ConsentData extends DocumentTemplateData {

    public ConsentData(DossierRegistration r) {
      String out = "Niet aangegeven";
      ConsentProvider consent = r.getConsent();
      Relatie relation = consent.getBrpConsentProvider();
      if (relation != null) {
        out = relation.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      } else if (isNotBlank(consent.getOtherConsentProvider())) {
        out = consent.getOtherConsentProvider();
      }
      put("betreft", out);
    }
  }

  private class DeclarationData extends DocumentTemplateData {

    public DeclarationData(DossierPersoon r) {
      put("bron", DeclarationType.valueOfCode(r.getBron()));
      put("toelichting", r.getToelichting());
    }
  }

  private class DeclarantData extends DocumentTemplateData {

    public DeclarantData(DossierRegistration r) {
      put("type", DeclarantType.valueOfCode(r.getDeclarant().getCode()));
      r.getDeclarant().getPerson().ifPresent(dossierPersoon -> put("persoon", dossierPersoon));
      put("toelichting", r.getDeclarantComment());
    }
  }

  private class InterpreterData extends DocumentTemplateData {

    public InterpreterData(DossierRegistration r) {
      put("type", InterpreterType.valueOfCode(r.getInterpreter()));
      put("naam", r.getInterpreterName());
      put("taal", r.getInterpreterLanguage());
    }
  }
}
