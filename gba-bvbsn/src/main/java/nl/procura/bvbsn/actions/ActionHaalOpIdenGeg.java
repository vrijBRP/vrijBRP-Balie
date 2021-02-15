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

import static nl.procura.standard.Globalfunctions.emp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.ictu.bsn.*;
import nl.procura.bvbsn.BvBsnActionInterface;
import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.misc.CODES;
import nl.procura.validation.Bsn;

public class ActionHaalOpIdenGeg extends BvBsnActionTemplate implements BvBsnActionInterface {

  private final static Logger LOGGER = LoggerFactory.getLogger(ActionHaalOpIdenGeg.class);
  private String              bsn    = "";

  public ActionHaalOpIdenGeg(AfzenderDE afzender, String indicatieEindgebruiker, String bsn) {
    setIndicatieEindgebruiker(indicatieEindgebruiker);
    setAfzender(afzender);
    setBsn(bsn);
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  @Override
  public String getOutputMessage() {
    HaalOpIdenGegBU a = (HaalOpIdenGegBU) getResponseObject();
    StringBuilder sb = new StringBuilder();

    sb.append(super.getOutputMessage());
    try {
      String newLine = "\n";

      sb.append("Actie|code|");
      sb.append(a.getResultaat().getHaalOpIdenGegAntwoordDE().get(0).getResultaatCode());
      sb.append(newLine);
      sb.append("Actie|vraagnummer|");
      sb.append(a.getResultaat().getHaalOpIdenGegAntwoordDE().get(0).getVraagnummer());
      sb.append(newLine);
      sb.append("Actie|omschrijving|");
      sb.append(a.getResultaat().getHaalOpIdenGegAntwoordDE().get(0).getResultaatOmschrijving());
      sb.append(newLine);
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    if (a.getResultaat() != null) {

      GebruikerIdenGegAntwoordDE g = a.getResultaat().getHaalOpIdenGegAntwoordDE().get(0).getIdenGeg();

      if (g != null) {

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

    if (emp(getBsn())) {
      throw new IllegalArgumentException("Geen BSN ingegeven.");
    }

    if (!new Bsn(getBsn()).isCorrect()) {
      throw new IllegalArgumentException("Incorrecte BSN ingegeven.");
    }

    HaalOpIdenGegBI berichtIn = new HaalOpIdenGegBI();

    berichtIn.setAfzender(getAfzender());
    berichtIn.setIndicatieEindgebruiker(getIndicatieEindgebruiker());
    HaalOpIdenGegVraagDE vraag = new HaalOpIdenGegVraagDE();

    vraag.setVraagnummer(1);
    vraag.setBurgerServiceNr(Long.valueOf(getBsn()));
    ArrayOfHaalOpIdenGegVraagDE vragen = new ArrayOfHaalOpIdenGegVraagDE();

    vragen.getHaalOpIdenGegVraagDE().add(vraag);
    berichtIn.setVraag(vragen);
    return berichtIn;
  }

  @Override
  public int getVerwerkingSuccesCode() {
    return CODES.HaalOpIdenGeg.VERWERKING_SUCCESS;
  }

  public boolean isGegevensGevonden() {
    try {
      return ((HaalOpIdenGegBU) getResponseObject()).getResultaat().getHaalOpIdenGegAntwoordDE().get(
          0).getResultaatCode() == CODES.HaalOpIdenGeg.SUCCESS;
    } catch (Exception e) {
      LOGGER.debug(e.toString());
      return false;
    }
  }
}
