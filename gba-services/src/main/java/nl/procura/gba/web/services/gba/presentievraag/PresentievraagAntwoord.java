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

package nl.procura.gba.web.services.gba.presentievraag;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.math.NumberUtils;

import nl.bprbzk.bcgba.v14.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "vraagBericht", "bsnRelatie", "request", "response" })
public class PresentievraagAntwoord {

  private int            vraagBericht = 0;
  private String         bsnRelatie   = "";
  private MatchIdenGegBI request;
  private MatchIdenGegBU response;

  // Wordt niet meer gebruikt
  @SuppressWarnings("unused")
  private MatchIdenGegAntwoordDE match;

  public String getBsnRelatie() {
    return bsnRelatie;
  }

  public void setBsnRelatie(String bsnRelatie) {
    this.bsnRelatie = bsnRelatie;
  }

  @XmlTransient
  public BeheerIdenGegVraagDE getGegevensVraag() {
    if (request != null && request.getIdenGegVraag() != null) {
      List<MatchIdenGegVraagDE> vragen = request.getIdenGegVraag().getMatchIdenGegVraagDE();
      if (vragen != null && !vragen.isEmpty()) {
        return vragen.get(0).getIdenGegVraag();
      }
    }
    return null;
  }

  @XmlTransient
  public List<PresentievraagMatch> getMatches() {
    List<PresentievraagMatch> matches = new ArrayList<>();
    ArrayOfMatchIdenGegResultaatDE resultaat = response.getResultaat();
    if (resultaat != null) {
      List<MatchIdenGegResultaatDE> matchIdenGegResultaatDE = resultaat.getMatchIdenGegResultaatDE();
      if (matchIdenGegResultaatDE != null && !matchIdenGegResultaatDE.isEmpty()) {
        ArrayOfMatchIdenGegAntwoordDE idenGegAntwoord = matchIdenGegResultaatDE.get(0).getIdenGegAntwoord();
        if (idenGegAntwoord != null) {
          for (MatchIdenGegAntwoordDE matc : idenGegAntwoord.getMatchIdenGegAntwoordDE()) {
            matches.add(new PresentievraagMatch(matc));
          }
        }
      }
    }

    return matches;
  }

  public MatchIdenGegBI getRequest() {
    return request;
  }

  public void setRequest(MatchIdenGegBI request) {
    this.request = request;
  }

  public MatchIdenGegBU getResponse() {
    return response;
  }

  public void setResponse(MatchIdenGegBU response) {
    this.response = response;
  }

  @XmlTransient
  public PresentieVraagResultaatType getResultaatType() {
    ArrayOfMatchIdenGegResultaatDE resultaat = getResponse().getResultaat();
    if (resultaat != null) {
      MatchIdenGegResultaatDE info = resultaat.getMatchIdenGegResultaatDE().get(0);
      return PresentieVraagResultaatType.get(info.getResultaatCode());
    }

    return PresentieVraagResultaatType.ONBEKEND;
  }

  public int getVraagBericht() {
    return vraagBericht;
  }

  public void setVraagBericht(int vraagBericht) {
    this.vraagBericht = vraagBericht;
  }

  @XmlTransient
  public boolean isMatch() {
    return !getMatches().isEmpty();
  }

  @XmlTransient
  public boolean isOneMatch() {
    return getMatches().size() == NumberUtils.INTEGER_ONE;
  }

  @XmlTransient
  public String toResultaatPn(boolean html) {

    StringBuilder out = new StringBuilder();
    ArrayOfMatchIdenGegResultaatDE resultaat = getResponse().getResultaat();

    if (resultaat != null) {

      MatchIdenGegResultaatDE info = resultaat.getMatchIdenGegResultaatDE().get(0);
      String code = info.getResultaatCodePersoonsnummer();
      String oms = info.getResultaatOmschrijvingPersoonsnummer();

      if (pos(code)) {
        out.append(oms);
        out.append(" (");
        out.append(code);
        out.append(")");
        return (html ? "<b>" + out.toString() + "</b>" : out.toString());
      }
    }

    return "";
  }

  @XmlTransient
  public String toResultaatString(boolean html) {

    StringBuilder out = new StringBuilder();
    ArrayOfMatchIdenGegResultaatDE resultaat = getResponse().getResultaat();

    if (resultaat != null) {

      MatchIdenGegResultaatDE info = resultaat.getMatchIdenGegResultaatDE().get(0);
      int code = info.getResultaatCode();
      String oms = info.getResultaatOmschrijving();

      if (pos(code)) {
        out.append(oms);
        out.append(" (code: ");
        out.append(code);
        out.append(")");
        return html ? "<b>" + out.toString() + "</b>" : out.toString();
      }
    }

    return "";
  }

  @XmlTransient
  public String toVerwerkingString() {

    BerichtInfoDE info1 = getResponse().getBcGBABericht();
    StringBuilder info = new StringBuilder();
    info.append(info1.getBerichtResultaatOmschrijving());
    info.append(" (code: ");
    info.append(info1.getBerichtResultaatCode());
    info.append(")");
    return info.toString();
  }
}
