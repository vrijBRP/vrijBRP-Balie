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

package nl.procura.bcgba.v14.actions;

import static nl.procura.standard.Globalfunctions.*;

import nl.bprbzk.bcgba.v14.*;
import nl.procura.bcgba.v14.BcGbaActionInterface;
import nl.procura.bcgba.v14.BcGbaActionTemplate;
import nl.procura.bcgba.v14.misc.BcGbaCode;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;

public class ActionMatchIdenGeg extends BcGbaActionTemplate implements BcGbaActionInterface {

  private String     instantie  = "";
  private IDGegevens IDGegevens = new IDGegevens();

  public ActionMatchIdenGeg(BcGbaVraagbericht vraagbericht, String instantie, AfzenderDE afzender,
      IDGegevens idGegevens) {
    setVraagbericht(vraagbericht);
    setAfzender(afzender);
    setInstantie(instantie);
    setIDGegevens(idGegevens);
  }

  @Override
  public BerichtInBase getRequestObject() {

    MatchIdenGegBI berichtIn = new MatchIdenGegBI();
    MatchIdenGegVraagDE vraag = new MatchIdenGegVraagDE();

    String geboortedatum = getIDGegevens().getGeboortedatum();
    String geboorteland = getIDGegevens().getGeboorteland();
    String geboorteplaats = getIDGegevens().getGeboorteplaats();
    String gemeenteVanInschrijving = getIDGegevens().getGemeenteVanInschrijving();
    String geslachtsaanduiding = getIDGegevens().getGeslachtsaanduiding();
    String geslachtsnaam = getIDGegevens().getGeslachtsnaam();
    String nationaliteit = getIDGegevens().getNationaliteit();
    String voornamen = getIDGegevens().getVoornamen();
    String voorvoegselGeslachtsnaam = getIDGegevens().getVoorvoegselGeslachtsnaam();
    String datumVertrekUitNederland = getIDGegevens().getDatumVertrekUitNederland();
    String buitenlandnr = getIDGegevens().getBuitenlandsPersoonsnummer();

    BeheerIdenGegVraagDE id = new BeheerIdenGegVraagDE();

    if (pos(datumVertrekUitNederland)) {
      id.setDatumAanvangAdresBuitenland(datumVertrekUitNederland); // Voormalig datumVertrekUitNederland
    }

    if (aval(geboortedatum) >= 0) {
      id.setGeboortedatum(geboortedatum);
    }

    if (fil(geboorteland)) {
      id.setGeboorteland(geboorteland);
    }

    if (fil(geboorteplaats)) {
      id.setGeboorteplaats(geboorteplaats);
    }

    if (fil(gemeenteVanInschrijving)) {
      id.setGemeenteVanInschrijving(gemeenteVanInschrijving);
    }

    if (fil(geslachtsaanduiding)) {
      id.setGeslachtsaanduiding(geslachtsaanduiding);
    }

    if (fil(geslachtsnaam)) {
      id.setGeslachtsnaam(geslachtsnaam);
    }

    if (fil(nationaliteit)) {
      id.setNationaliteit(nationaliteit);
    }

    if (fil(buitenlandnr)) {
      id.setBuitenlandsPersoonsnummer(buitenlandnr);
    }

    if (fil(voornamen)) {
      id.setVoornamen(voornamen);
    }

    if (fil(voorvoegselGeslachtsnaam)) {
      id.setVoorvoegselGeslachtsnaam(voorvoegselGeslachtsnaam);
    }

    vraag.setVraagnummer(getVraagbericht().getCode());
    vraag.setIdenGegVraag(id);

    ArrayOfMatchIdenGegVraagDE vragen = new ArrayOfMatchIdenGegVraagDE();
    vragen.getMatchIdenGegVraagDE().add(vraag);

    berichtIn.setIdenGegVraag(vragen);
    berichtIn.setAfzender(getAfzender());
    berichtIn.setInstantie(getInstantie());

    return berichtIn;
  }

  @Override
  public BcGbaCode getVerwerkingSuccesCode() {
    return BcGbaCode.VERWERKING_SUCCESS;
  }

  @Override
  public boolean isResultaat(BcGbaCode code) {

    try {
      int returnCode = ((MatchIdenGegBU) getResponseObject()).getResultaat().getMatchIdenGegResultaatDE().get(
          0).getResultaatCode();
      return BcGbaCode.get(returnCode) == code;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isResultaat() {
    MatchIdenGegBU a = (MatchIdenGegBU) getResponseObject();
    return a.getResultaat() != null;
  }

  public MatchIdenGegResultaatDE getResultaat() {
    MatchIdenGegBU a = (MatchIdenGegBU) getResponseObject();
    return a.getResultaat().getMatchIdenGegResultaatDE().get(0);
  }

  public String getInstantie() {
    return instantie;
  }

  public void setInstantie(String instantie) {
    this.instantie = instantie;
  }

  public IDGegevens getIDGegevens() {
    return IDGegevens;
  }

  public void setIDGegevens(IDGegevens iDGegevens) {
    IDGegevens = iDGegevens;
  }

  @Override
  public String getOutputMessage() {

    MatchIdenGegBU a = (MatchIdenGegBU) getResponseObject();

    StringBuilder sb = new StringBuilder();

    try {
      int code = a.getBcGBABericht().getBerichtResultaatCode();
      String oms = a.getBcGBABericht().getBerichtResultaatOmschrijving();

      sb.append(
          "Bericht|Vraagbericht|" + getVraagbericht().getOms() + " (" + getVraagbericht().getCode() + ")\n");
      sb.append("Bericht|Melding|" + oms + " (" + code + ")\n");

    } catch (Exception ignored) {}

    try {
      int code = a.getResultaat().getMatchIdenGegResultaatDE().get(0).getResultaatCode();
      String oms = a.getResultaat().getMatchIdenGegResultaatDE().get(0).getResultaatOmschrijving();

      sb.append("Resultaat|Melding|" + oms + " (" + code + ")\n");

    } catch (Exception ignored) {}

    return sb.toString();
  }
}
