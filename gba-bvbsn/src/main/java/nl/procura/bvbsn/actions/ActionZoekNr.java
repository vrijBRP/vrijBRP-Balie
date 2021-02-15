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
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.ictu.bsn.*;
import nl.procura.bvbsn.BvBsnActionInterface;
import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.misc.CODES;
import nl.procura.standard.exceptions.ProException;
import nl.procura.validation.Bsn;

public class ActionZoekNr extends BvBsnActionTemplate implements BvBsnActionInterface {

  private final static Logger LOGGER = LoggerFactory.getLogger(ActionZoekNr.class);
  private String              bsn    = "";

  public ActionZoekNr(AfzenderDE afzender, String indicatieEindgebruiker, String bsn) {
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

  public String getBsnAntwoordOmschrijving() {
    try {
      return ((ZoekNrBU) getResponseObject()).getResultaat().getZoekNrAntwoordDE().get(
          0).getResultaatOmschrijving();
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    return "";
  }

  @Override
  public String getOutputMessage() {

    ZoekNrBU a = (ZoekNrBU) getResponseObject();

    StringBuilder sb = new StringBuilder();

    sb.append(super.getOutputMessage());

    try {
      String newLine = "\n";

      sb.append("Actie|code|");
      sb.append(a.getResultaat().getZoekNrAntwoordDE().get(0).getResultaatCode());
      sb.append(newLine);
      sb.append("Actie|vraagnummer|");
      sb.append(a.getResultaat().getZoekNrAntwoordDE().get(0).getVraagnummer());
      sb.append(newLine);
      sb.append("Actie|omschrijving|");
      sb.append(a.getResultaat().getZoekNrAntwoordDE().get(0).getResultaatOmschrijving());
      sb.append(newLine);
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    return sb.toString();
  }

  @Override
  public BerichtInBase getRequestObject() {
    if (emp(getBsn())) {
      throw new ProException(WARNING, "Geen BSN ingegeven.");
    }
    if (!new Bsn(getBsn()).isCorrect()) {
      throw new ProException(WARNING, "Incorrecte BSN ingegeven.");
    }
    ZoekNrBI berichtIn = new ZoekNrBI();

    berichtIn.setAfzender(getAfzender());
    berichtIn.setIndicatieEindgebruiker(getIndicatieEindgebruiker());
    ZoekNrVraagDE vraag = new ZoekNrVraagDE();

    vraag.setVraagnummer(1);
    vraag.setBurgerServiceNr(Long.valueOf(getBsn()));
    ArrayOfZoekNrVraagDE vragen = new ArrayOfZoekNrVraagDE();

    vragen.getZoekNrVraagDE().add(vraag);
    berichtIn.setVraag(vragen);
    return berichtIn;
  }

  @Override
  public int getVerwerkingSuccesCode() {
    return CODES.ToetsNr.VERWERKING_SUCCESS;
  }

  public boolean isCorrectBsn() {
    try {
      return ((ZoekNrBU) getResponseObject()).getResultaat().getZoekNrAntwoordDE().get(
          0).getResultaatCode() == CODES.ToetsNr.SUCCESS;
    } catch (Exception e) {
      LOGGER.debug(e.toString());
      return false;
    }
  }
}
