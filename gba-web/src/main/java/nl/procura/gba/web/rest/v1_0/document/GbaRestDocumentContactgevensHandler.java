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

package nl.procura.gba.web.rest.v1_0.document;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocument;
import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocumentType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsZaakArgumenten;

class GbaRestDocumentContactgevensHandler extends GbaRestHandler {

  GbaRestDocumentContactgevensHandler(Services services) {
    super(services);
  }

  /**
   * Zoek reisdocument in het zakenregister (PROWEB)
   */
  GbaRestDocument getReisdocumentBackoffice(ReisdocumentZaakArgumenten zaakArgumenten) {

    String aanvraagnummer = getServices().getReisdocumentService()
        .getBackOfficeAanvraag(zaakArgumenten.getDocumentnummer());

    if (fil(aanvraagnummer)) {

      return getReisdocumentProweb("", aanvraagnummer);
    }

    return null;
  }

  /**
   * Zoek reisdocument in het zakenregister (PROWEB)
   */
  GbaRestDocument getReisdocumentProweb(String documentNummer, String aanvraagnummer) {

    ReisdocumentZaakArgumenten zaakArgumenten = new ReisdocumentZaakArgumenten(documentNummer);
    zaakArgumenten.setZaakKey(new ZaakKey(aanvraagnummer));
    List<ReisdocumentAanvraag> zaken = getServices().getReisdocumentService().getMinimalZaken(zaakArgumenten);

    if (zaken.size() > 0) {

      for (ReisdocumentAanvraag zaak : zaken) {
        ReisdocumentAanvraag aanvraag = getServices().getReisdocumentService().getCompleteZaak(zaak);

        GbaRestDocument document = new GbaRestDocument();
        document.setDocumentType(GbaRestDocumentType.REISDOCUMENT);
        document.setOmschrijving(aanvraag.getReisdocumentType().toString());
        document.setDatumAanvraag(aanvraag.getDatumTijdInvoer().getFormatDate());
        document.setAanvraagNummer(aanvraag.getAanvraagnummer().getNummer());
        document.setDocumentNummer(aanvraag.getReisdocumentStatus().getNrNederlandsDocument());

        addPersoon(document, aanvraag.getBasisPersoon());

        return document;
      }
    }

    return null;
  }

  /**
   * Zoek rijbewijs in de backoffice (RDW)
   */
  GbaRestDocument getRijbewijsBackoffice(RijbewijsZaakArgumenten zaakArgumenten) {

    String aanvraagnummer = NrdServices.getBackOfficeAanvraag(getServices().getRijbewijsService(),
        zaakArgumenten.getDocumentnummer());

    if (fil(aanvraagnummer)) {
      return getRijbewijsProweb("", aanvraagnummer);
    }

    return null;
  }

  /**
   * Zoek rijbewijs in Proweb
   */
  GbaRestDocument getRijbewijsProweb(String documentNummer, String aanvraagnummer) {

    RijbewijsZaakArgumenten zaakArgumenten = new RijbewijsZaakArgumenten(documentNummer);
    zaakArgumenten.setZaakKey(new ZaakKey(aanvraagnummer));
    List<RijbewijsAanvraag> zaken = getServices().getRijbewijsService().getMinimalZaken(zaakArgumenten);

    if (zaken.size() > 0) {

      for (RijbewijsAanvraag zaak : zaken) {
        RijbewijsAanvraag aanvraag = getServices().getRijbewijsService().getCompleteZaak(zaak);

        GbaRestDocument document = new GbaRestDocument();
        document.setDocumentType(GbaRestDocumentType.RIJBEWIJS);
        document.setOmschrijving(""); // Geen toepassing
        document.setDatumAanvraag(aanvraag.getDatumTijdInvoer().getFormatDate());
        document.setAanvraagNummer(aanvraag.getAanvraagNummer());
        document.setDocumentNummer(aanvraag.getRijbewijsnummer());

        addPersoon(document, aanvraag.getBasisPersoon());

        return document;
      }
    }

    return null;
  }

  /**
   * Voeg persoonsgegevens toe aan het antwoord
   */
  private void addPersoon(GbaRestDocument document, BasePLExt persoonslijst) {

    Naam naam = persoonslijst.getPersoon().getNaam();

    document.getPersoon().setGeslachtsnaam(naam.getGeslachtsnaam().getValue().getVal());
    document.getPersoon().setVoornamen(naam.getVoornamen().getValue().getVal());
    document.getPersoon().setVoorvoegsel(naam.getVoorvoegsel().getValue().getVal());
    document.getPersoon().setNaamgebruik(naam.getNaamgebruik().getValue().getVal());

    if (naam.getPartner() != null) {
      document.getPersoon().setVoorvoegselPartner(naam.getPartner().getVoorvoegsel().getValue().getVal());
      document.getPersoon().setGeslachtsnaamPartner(naam.getPartner().getGeslachtsnaam().getValue().getVal());
    }

    for (PlContactgegeven aant : getServices().getContactgegevensService().getContactgegevens(persoonslijst)) {

      String gegeven = aant.getContactgegeven().getGegeven();
      String waarde = aant.getAant();

      if (ContactgegevensService.TEL_MOBIEL.equalsIgnoreCase(gegeven)) {
        document.getPersoon().getContactgegevens().setTelefoonMobiel(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.TEL_THUIS.equalsIgnoreCase(gegeven)) {
        document.getPersoon().getContactgegevens().setTelefoonThuis(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.TEL_WERK.equalsIgnoreCase(gegeven)) {
        document.getPersoon().getContactgegevens().setTelefoonWerk(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.EMAIL.equalsIgnoreCase(gegeven)) {
        document.getPersoon().getContactgegevens().setEmail(waarde);
      }
    }
  }
}
