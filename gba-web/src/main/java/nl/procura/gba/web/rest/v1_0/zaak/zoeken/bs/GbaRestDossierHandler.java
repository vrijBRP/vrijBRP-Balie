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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort.*;
import static nl.procura.gba.web.services.bs.registration.relations.RelationDateStartUtils.getValidityDate;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossSourceDoc;
import nl.procura.gba.jpa.personen.db.DossTravelDoc;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.components.containers.DutchTravelDocumentAuthorityContainer;
import nl.procura.gba.web.components.containers.DutchTravelDocumentContainer;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersonen;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DeclarationType;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.standard.Globalfunctions;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class GbaRestDossierHandler extends GbaRestElementHandler {

  public GbaRestDossierHandler(Services services) {
    super(services);
  }

  public void addAktes(GbaRestElement dossierElement, Dossier zaak) {

    GbaRestElement aktesElement = dossierElement.add(GbaRestElementType.AKTES);

    for (DossierAkte akte : zaak.getAktes()) {

      GbaRestElement akteElement = aktesElement.add(GbaRestElementType.AKTE);
      add(akteElement, AKTE_NUMMER, akte.getAkte());
      add(akteElement, AKTE_BRP_NUMMER, akte.getBrpAkte());
      add(akteElement, AKTE_JAAR, akte.getJaar().longValue());
      add(akteElement, AKTE_PLAATS, "");
      add(akteElement, DATUM_AKTE, akte.getDatumIngang());
      add(akteElement, DATUM_FEIT, akte.getDatumFeit());
      add(akteElement, REGISTERDEEL, akte.getRegisterdeel());

      DossierAkteRegistersoort soort = DossierAkteRegistersoort.get(akte.getRegistersoort());
      add(akteElement, REGISTERSOORT, soort.getCode(), soort.getOms());
      add(akteElement, VOLGNUMMER, akte.getFormatVnr());

      GbaRestElement personenElement = akteElement.add(GbaRestElementType.PERSONEN);

      if (akte.getAkteRegistersoort().is(AKTE_ERKENNING_NAAMSKEUZE, AKTE_GEBOORTE)) {
        addAktePersoon(personenElement, KIND, akte.getAktePersoon());

      } else if (akte.getAkteRegistersoort().is(AKTE_HUWELIJK, AKTE_GPS)) {
        addAktePersoon(personenElement, PARTNER, akte.getAktePersoon());
        addAktePersoon(personenElement, PARTNER, akte.getAktePartner());

      } else if (akte.getAkteRegistersoort().is(AKTE_OVERLIJDEN)) {
        addAktePersoon(personenElement, OVERLEDENE, akte.getAktePersoon());

      } else {
        throw new ProException(ERROR, "Onbekende akteregistersoort: " + akte.getAkteRegistersoort());
      }
    }
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    Dossier zaak = (Dossier) zaakParm;
    GbaRestElement dossierElement = gbaZaak.add(DOSSIER);

    addAktes(dossierElement, zaak);
    addDossierElementen(dossierElement, zaak);

    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);
    for (DossierAkte akte : zaak.getAktes()) {
      GbaRestElement deelZaak = deelZaken.add(DEELZAAK);
      add(deelZaak, BSN, akte.getAktePersoon().getBurgerServiceNummer());
      add(deelZaak, ANR, "");
    }
  }

  @SuppressWarnings("unused")
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
  }

  protected void addNationaliteiten(GbaRestElement parent,
      DossierNationaliteiten dn,
      DateTime datumVerkrijging) {

    if (dn.getNationaliteiten().size() > 0) {
      GbaRestElement natElementen = parent.add(GbaRestElementType.NATIONALITEITEN);

      for (DossierNationaliteit n : dn.getNationaliteiten()) {
        DateTime ontlening = n.getDatumTijdOntlening();
        GbaRestElement natElement = natElementen.add(GbaRestElementType.NATIONALITEIT);
        add(natElement, OMSCHRIJVING, n.getNationaliteit());
        add(natElement, DATUM_ONTLENING, ontlening.getLongDate(), ontlening.getFormatDate());
        add(natElement, TIJD_ONTLENING, ontlening.getLongTime(), ontlening.getFormatTime());
        add(natElement, DATUM_VERLIES, n.getDatumVerlies());
        add(natElement, REDEN_VERKRIJGING_NL, n.getRedenverkrijgingNederlanderschap());
        add(natElement, SINDS, n.getSinds());
        add(natElement, AFGELEID_VAN, n.getAfgeleidVan().getCode(), n.getAfgeleidVan().getDescr());
        add(natElement, TYPE_VERKRIJGING, n.getVerkrijgingType().getCode(), n.getVerkrijgingType().getDescr());
        add(natElement, BRONDOCUMENT, n.getSourceDescription());
        add(natElement, BUITENLANDS_ID_NR, n.getSourceForeignId());

        if (datumVerkrijging != null) {
          add(natElement, DATUM_VERKRIJGING, datumVerkrijging);
        } else if (n.getDatumVerkrijging().getLongDate() >= 0) {
          add(natElement, DATUM_VERKRIJGING, n.getDatumVerkrijging());
        } else {
          if (DossierNationaliteitDatumVanafType.GEBOORTE_DATUM == n.getVerkrijgingType()) {
            if (dn instanceof DossierPersoon) {
              add(natElement, DATUM_VERKRIJGING, ((DossierPersoon) dn).getDatumGeboorte());
            } else {
              add(natElement, DATUM_VERKRIJGING, n.getDatumVerkrijging());
            }
          } else if (DossierNationaliteitDatumVanafType.ONBEKEND == n.getVerkrijgingType()) {
            add(natElement, DATUM_VERKRIJGING, 0L);
          } else {
            add(natElement, DATUM_VERKRIJGING, n.getDatumVerkrijging());
          }
        }
      }
    }
  }

  protected void addPersonen(GbaRestElement parent, DossierPersonen dn) {

    if (dn.getPersonen().size() > 0) {
      GbaRestElement personenElement = parent.add(GbaRestElementType.PERSONEN);
      for (DossierPersoon persoon : dn.getPersonen()) {
        addPersoon(personenElement, getSoort(persoon.getDossierPersoonType()), persoon);
      }
    }
  }

  protected GbaRestElement addPersoon(GbaRestElement personenElement, GbaRestElement soort, DossierPersoon p) {

    GbaRestElement persoonElement = null;

    if (p.isVolledig()) {

      persoonElement = personenElement.add(GbaRestElementType.PERSOON);

      add(persoonElement, SOORT, soort.getWaarde(), soort.getOmschrijving());
      add(persoonElement, BSN, p.getBurgerServiceNummer());
      add(persoonElement, ANR, p.getAnummer());
      add(persoonElement, GESLACHTSNAAM, p.getGeslachtsnaam());
      add(persoonElement, VOORVOEGSEL, p.getVoorvoegsel());
      add(persoonElement, VOORNAMEN, p.getVoornaam());
      add(persoonElement, INITIALEN, p.getNaam().getInit());
      add(persoonElement, GESLACHTSAANDUIDING, p.getGeslacht());
      add(persoonElement, TITEL_PREDIKAAT, p.getTitel());
      add(persoonElement, NAAMGEBRUIK, p.getNaamgebruik());

      addGeboorte(persoonElement, p);
      addSourceDocument(persoonElement, p, p.getDossSourceDoc());
      addVerblijfplaats(persoonElement, p);
      addNationaliteiten(persoonElement, p, null);
      addOverige(persoonElement, p);
      addReisdocumenten(persoonElement, p);
    }

    return persoonElement;
  }

  protected GbaRestElement getSoort(DossierPersoonType dossierPersoonType) {

    String value = "";
    switch (dossierPersoonType) {
      case AANGEVER:
        value = AANGEVER;
        break;
      case AFGEVER:
        value = AFGEVER;
        break;
      case AMBTENAAR:
        value = AMBTENAAR;
        break;
      case BETROKKENE:
        value = BETROKKENE;
        break;
      case ERKENNER:
        value = ERKENNER;
        break;
      case EXPARTNER:
        value = EX_PARTNER;
        break;
      case GETUIGE:
        value = GETUIGE;
        break;
      case KIND:
        value = KIND;
        break;
      case MOEDER:
        value = MOEDER;
        break;
      case OUDER:
        value = OUDER;
        break;
      case OVERLEDENE:
        value = OVERLEDENE;
        break;
      case PARTNER:
      case PARTNER1:
      case PARTNER2:
        value = PARTNER;
        break;
      case VADER_DUO_MOEDER:
        value = VADER_DUO_MOEDER;
        break;
      case INSCHRIJVER:
        value = INSCHRIJVER;
        break;
      case GERELATEERDE_BRP:
        value = GERELATEERDE_BRP;
        break;
      case GERELATEERDE_NIET_BRP:
        value = GERELATEERDE_NIET_BRP;
        break;
      case ONBEKEND:
        value = ONBEKEND;
        break;
      default:
        throw new ProException(ERROR, "Onbekend type persoon: " + dossierPersoonType);
    }

    return new GbaRestElement().set(value, dossierPersoonType.getDescr());
  }

  private void addAktePersoon(GbaRestElement personenElement, String soort, DossierAktePersoon p) {

    GbaRestElement persoonElement = personenElement.add(PERSOON);
    add(persoonElement, SOORT, soort);
    add(persoonElement, BSN, p.getBurgerServiceNummer());
    add(persoonElement, GESLACHTSNAAM, p.getGeslachtsnaam());
    add(persoonElement, VOORVOEGSEL, p.getVoorvoegsel());
    add(persoonElement, VOORNAMEN, p.getVoornaam());
    add(persoonElement, GESLACHTSAANDUIDING, p.getGeslacht());
    add(persoonElement.add(GEBOORTE), DATUM_GEBOORTE, p.getGeboortedatum());
  }

  private void addGeboorte(GbaRestElement persoon, DossierPersoon p) {

    GbaRestElement gebElement = persoon.add(GbaRestElementType.GEBOORTE);
    add(gebElement, DATUM_GEBOORTE, p.getDatumGeboorte());
    add(gebElement, PLAATS, p.getGeboorteplaats());
    add(gebElement, LAND, p.getGeboorteland());

    if (fil(p.getGeboorteAkteNummer())) {
      add(gebElement, AKTE_NUMMER, p.getGeboorteAkteNummer());
      add(gebElement, AKTE_BRP_NUMMER, p.getGeboorteAkteBrpNummer());
      add(gebElement, AKTE_JAAR, along(p.getGeboorteAkteJaar()));
      add(gebElement, AKTE_PLAATS, p.getGeboorteAktePlaats());
    }
  }

  private void addSourceDocument(GbaRestElement persoon, DossierPersoon dossierPersoon,
      Optional<DossSourceDoc> dossSourceDoc) {

    if (dossSourceDoc.isPresent()) {
      DossSourceDoc sourceDoc = dossSourceDoc.get();
      GbaRestElement sourceDocElement = persoon.add(BRONDOCUMENT);

      SourceDocumentType sourceDocType = SourceDocumentType.valueOfCode(sourceDoc.getDocType());
      ValidityDateType dateType = ValidityDateType.valueOfCode(sourceDoc.getValidityDateType());

      add(sourceDocElement, DATUM_TYPE, dateType.getCode(), dateType.getDescription());
      add(sourceDocElement, DATUM_INGANG, getValidityDate(dossSourceDoc.get(), dossierPersoon));
      add(sourceDocElement, TYPE, sourceDocType.getCode(), sourceDocType.getDescription());
      add(sourceDocElement, NUMMER, sourceDoc.getDocNumber());
      add(sourceDocElement, PLAATS, Container.PLAATS.get(sourceDoc.getDocMun()));
      add(sourceDocElement, OMSCHRIJVING, sourceDoc.getDocDescr());
    }
  }

  private void addOverige(GbaRestElement persoon, DossierPersoon p) {

    GbaRestElement overigeElement = persoon.add(GbaRestElementType.OVERIGE);

    add(overigeElement, BURGERLIJKE_STAAT, p.getBurgerlijkeStaat().getCode(), p.getBurgerlijkeStaat().getOms());
    add(overigeElement, DATUM_BURGERLIJKE_STAAT, p.getDatumBurgerlijkeStaat());
    add(overigeElement, VERSTREKKINGSBEPERKING, p.isVerstrekkingsbeperking());
    add(overigeElement, EERDER_IN_NL_GEWOOND, Globalfunctions.isTru(p.getPrevType()));
    add(overigeElement, TOELICHTING, p.getPrevDescription());

    DeclarationType bronType = DeclarationType.valueOfCode(p.getBron());
    if (bronType != null) {
      GbaRestElement aangifteElement = overigeElement.add(AANGIFTE);
      add(aangifteElement, SOORT, bronType.getCode(), bronType.getDescription());
      add(aangifteElement, TOELICHTING, p.getToelichting());
    }
  }

  private void addReisdocumenten(GbaRestElement persoon, DossierPersoon p) {

    if (p.getDossTravelDocs() != null) {

      GbaRestElement reisdocumentenElement = persoon.add(REISDOCUMENTEN);
      for (DossTravelDoc traveldoc : p.getDossTravelDocs()) {
        GbaRestElement reisdocumentElement = reisdocumentenElement.add(REISDOCUMENT);
        add(reisdocumentElement, DOCUMENT_TYPE, new DutchTravelDocumentContainer().get(traveldoc.getNedReisdoc()));
        add(reisdocumentElement, NUMMER, traveldoc.getDocNr());
        add(reisdocumentElement, DATUM_VERKRIJGING, new DateFieldValue(traveldoc.getDVerkrijging()));
        add(reisdocumentElement, DATUM_EINDE, new DateFieldValue(traveldoc.getDEnd()));

        // Autority
        GbaRestElement aut = reisdocumentElement.add(AUTORITEIT);
        add(aut, SOORT, new DutchTravelDocumentAuthorityContainer().get(traveldoc.getAutVerstrek()));
        add(aut, PLAATS, Container.PLAATS.get(traveldoc.getAutVerstrekGem()));
        add(aut, LAND, Container.LAND.get(traveldoc.getAutVerstrekLand()));

        // Dossier
        GbaRestElement dossier = reisdocumentElement.add(DOSSIER);
        add(dossier, DATUM_INGANG, "0");

        if (Globalfunctions.pos(traveldoc.getDossGem())) {
          add(dossier, PLAATS, Container.PLAATS.get(traveldoc.getDossGem()));
        } else {
          add(dossier, PLAATS, Container.PLAATS.unknown());
        }

        if (StringUtils.isBlank(traveldoc.getDossOms())) {
          add(dossier, OMSCHRIJVING, ".");
        } else {
          add(dossier, OMSCHRIJVING, traveldoc.getDossOms());
        }
      }
    }
  }

  private void addVerblijfplaats(GbaRestElement persoon, DossierPersoon p) {

    GbaRestElement element = persoon.add(GbaRestElementType.VERBLIJFSADRES);
    add(element, STRAAT, p.getStraat());
    add(element, HNR, p.getHuisnummer());
    add(element, HNR_L, p.getHuisnummerLetter());
    add(element, HNR_A, p.getHuisnummerAand());
    add(element, HNR_T, p.getHuisnummerToev());
    add(element, POSTCODE, p.getPostcode());
    add(element, WOONPLAATS, p.getWoonplaats());
    add(element, GEMEENTE, p.getWoongemeente());
    add(element, LAND, p.getLand());
  }
}
