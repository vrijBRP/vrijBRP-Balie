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

package nl.procura.bvbsn.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.ictu.bsn.*;
import nl.procura.bvbsn.BvBsnActionInterface;
import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.misc.CODES;

public class ActionOpvrBsnIdenGeg extends BvBsnActionTemplate implements BvBsnActionInterface {

  private final static Logger LOGGER     = LoggerFactory.getLogger(ActionOpvrBsnIdenGeg.class);
  private String              bsn        = "";
  private IDGegevens          idGegevens = new IDGegevens();

  public ActionOpvrBsnIdenGeg(AfzenderDE afzender, String indicatieEindgebruiker) {
    setIndicatieEindgebruiker(indicatieEindgebruiker);
    setAfzender(afzender);
  }

  public ActionOpvrBsnIdenGeg(AfzenderDE afzender, String indicatieEindgebruiker, IDGegevens idGegevens) {
    setIndicatieEindgebruiker(indicatieEindgebruiker);
    setAfzender(afzender);

    this.idGegevens = idGegevens;
  }

  public String getBsn() {
    return bsn;
  }

  public IDGegevens getIdGegevens() {
    return idGegevens;
  }

  public void setIdGegevens(IDGegevens iDGegevens) {
    idGegevens = iDGegevens;
  }

  @Override
  public String getOutputMessage() {
    OpvrBsnIdenGegBU a = (OpvrBsnIdenGegBU) getResponseObject();
    StringBuilder sb = new StringBuilder();

    sb.append(super.getOutputMessage());
    try {
      append(sb, "Actie|code|", a.getResultaat().getOpvrBsnIdenGegResultaatDE().get(0).getResultaatCode());
      append(sb, "Actie|vraagnummer|", a.getResultaat().getOpvrBsnIdenGegResultaatDE().get(0).getVraagnummer());
      append(sb, "Actie|omschrijving|",
          a.getResultaat().getOpvrBsnIdenGegResultaatDE().get(0).getResultaatOmschrijving());
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    if (a.getResultaat() != null) {

      GebruikerIdenGegAntwoordDE g = a.getResultaat().getOpvrBsnIdenGegResultaatDE().get(0).getIdenGegAntwoord();

      if (g != null) {

        this.bsn = g.getBsn();

        append(sb, "gegevens|bsn|", g.getBsn());
        append(sb, "gegevens|voornamen|", g.getVoornamen());
        append(sb, "gegevens|adellijke titel-predikaat|", g.getAdellijkeTitelPredikaat());
        append(sb, "gegevens|voorvoegsel geslachtsnaam|", g.getVoorvoegselGeslachtsnaam());
        append(sb, "gegevens|geslachtsnaam|", g.getGeslachtsnaam());
        append(sb, "gegevens|geboortedatum|", g.getGeboortedatum());
        append(sb, "gegevens|geboorteplaats|", g.getGeboorteplaats());
        append(sb, "gegevens|geboorteland|", g.getGeboorteland());
        append(sb, "gegevens|geslachtsaanduiding|", g.getGeslachtsaanduiding());
        append(sb, "gegevens|aanduiding gegevens in onderzoek persoon|",
            g.getAanduidingGegevensInOnderzoekPersoon());
        append(sb, "gegevens|datum ingang onderzoek persoon|", g.getDatumIngangOnderzoekPersoon());
        append(sb, "gegevens|datum overlijden|", g.getDatumOverlijden());
        append(sb, "gegevens|aanduiding gegevens in onderzoek overlijden|",
            g.getAanduidingGegevensInOnderzoekOverlijden());
        append(sb, "gegevens|datum ingang onderzoek overlijden|", g.getDatumIngangOnderzoekOverlijden());
        append(sb, "gegevens|omschrijving reden opschorting|", g.getOmschrijvingRedenOpschorting());
        append(sb, "gegevens|indicatie geheim|", g.getIndicatieGeheim());
        append(sb, "gegevens|gemeente van inschrijving|", g.getGemeenteVanInschrijving());
        append(sb, "gegevens|straatnaam|", g.getStraatnaam());
        append(sb, "gegevens|huisnummer|", g.getHuisnummer());
        append(sb, "gegevens|huisletter|", g.getHuisletter());
        append(sb, "gegevens|huisnummertoevoeging|", g.getHuisnummertoevoeging());
        append(sb, "gegevens|aanduiding bij huisnummer|", g.getAanduidingBijHuisnummer());
        append(sb, "gegevens|postcode|", g.getPostcode());
        append(sb, "gegevens|locatiebeschrijving|", g.getLocatiebeschrijving());
        append(sb, "gegevens|land van waar ingeschreven|", g.getLandVanwaarIngeschreven());
        append(sb, "gegevens|functie adres|", g.getFunctieAdres());
        append(sb, "gegevens|gemeentedeel|", g.getGemeentedeel());
        append(sb, "gegevens|aanduiding gegevens in oderzoek adres|",
            g.getAanduidingGegevensInOnderzoekAdres());
      }
    }

    return sb.toString();
  }

  @Override
  public BerichtInBase getRequestObject() {
    OpvrBsnIdenGegBI berichtIn = new OpvrBsnIdenGegBI();

    berichtIn.setAfzender(getAfzender());
    berichtIn.setIndicatieEindgebruiker(getIndicatieEindgebruiker());
    GebruikerIdenGegVraagDE newIdGegevens = new GebruikerIdenGegVraagDE();

    newIdGegevens.setAanduidingBijHuisnummer(idGegevens.getAanduidingBijHuisnummer());
    newIdGegevens.setGeboortedatum(idGegevens.getGeboortedatum());
    newIdGegevens.setGeboorteland(idGegevens.getGeboorteland());
    newIdGegevens.setGeboorteplaats(idGegevens.getGeboorteplaats());
    newIdGegevens.setGemeenteVanInschrijving(idGegevens.getGemeenteVanInschrijving());
    newIdGegevens.setGeslachtsaanduiding(idGegevens.getGeslachtsaanduiding());
    newIdGegevens.setGeslachtsnaam(idGegevens.getGeslachtsnaam());
    newIdGegevens.setHuisletter(idGegevens.getHuisletter());
    newIdGegevens.setHuisnummer(idGegevens.getHuisnummer());
    newIdGegevens.setHuisnummertoevoeging(idGegevens.getHuisnummertoevoeging());
    newIdGegevens.setLandVanwaarIngeschreven(idGegevens.getLandVanwaarIngeschreven());
    newIdGegevens.setLocatiebeschrijving(idGegevens.getLocatiebeschrijving());
    newIdGegevens.setPostcode(idGegevens.getPostcode());
    newIdGegevens.setStraatnaam(idGegevens.getStraatnaam());
    newIdGegevens.setVoornamen(idGegevens.getVoornamen());
    newIdGegevens.setVoorvoegselGeslachtsnaam(idGegevens.getVoorvoegselGeslachtsnaam());
    OpvrBsnIdenGegVraagDE vraag = new OpvrBsnIdenGegVraagDE();

    vraag.setVraagnummer(1);
    vraag.setIdenGegVraag(newIdGegevens);
    ArrayOfOpvrBsnIdenGegVraagDE vragen = new ArrayOfOpvrBsnIdenGegVraagDE();

    vragen.getOpvrBsnIdenGegVraagDE().add(vraag);
    berichtIn.setIdenGegVraag(vragen);
    return berichtIn;
  }

  @Override
  public int getVerwerkingSuccesCode() {
    return CODES.OpvrBsnIdenGeg.VERWERKING_SUCCESS;
  }

  public boolean isGegevensGevonden() {
    try {
      return ((OpvrBsnIdenGegBU) getResponseObject()).getResultaat().getOpvrBsnIdenGegResultaatDE().get(
          0).getResultaatCode() == CODES.OpvrBsnIdenGeg.SUCCESS;
    } catch (Exception e) {
      LOGGER.debug(e.toString());
      return false;
    }
  }
}
