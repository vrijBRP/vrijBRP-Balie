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

public class ActionVerificatieIdentiteitsDocument extends BvBsnActionTemplate implements BvBsnActionInterface {

  public static final String  TYPE_REISDOCUMENT          = "Reisdocument";
  public static final String  TYPE_RIJBEWIJS             = "Rijbewijs";
  public static final String  TYPE_VREEMDELINGENDOCUMENT = "Vreemdelingendocument";
  private final static Logger LOGGER                     = LoggerFactory.getLogger(
      ActionVerificatieIdentiteitsDocument.class);
  private String              documentNummer             = "";
  private String              documentType               = "";

  public ActionVerificatieIdentiteitsDocument(AfzenderDE afzender, String indicatieEindgebruiker, String documentType,
      String documentNummer) {
    setIndicatieEindgebruiker(indicatieEindgebruiker);
    setAfzender(afzender);
    setDocumentType(documentType);
    setDocumentNummer(documentNummer);
  }

  public String getDocumentNummer() {
    return documentNummer;
  }

  public void setDocumentNummer(String documentNummer) {
    this.documentNummer = documentNummer;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  @Override
  public String getOutputMessage() {
    VerificatieIdenDocumentenBU a = (VerificatieIdenDocumentenBU) getResponseObject();
    StringBuilder sb = new StringBuilder();

    sb.append(super.getOutputMessage());

    try {

      VerificatieIdenDocumentenResultaatDE res = a.getIdenDocumentenResultaat()
          .getVerificatieIdenDocumentenResultaatDE().get(
              0);

      append(sb, "Actie|code|", res.getResultaatCode());
      append(sb, "Actie|vraagnummer|", res.getVraagnummer());
      append(sb, "Actie|omschrijving|", res.getResultaatOmschrijving());
    } catch (Exception e) {
      LOGGER.debug(e.toString());
    }

    return sb.toString();
  }

  @Override
  public BerichtInBase getRequestObject() {
    VerificatieIdenDocumentenBI berichtIn = new VerificatieIdenDocumentenBI();

    berichtIn.setAfzender(getAfzender());
    berichtIn.setIndicatieEindgebruiker(getIndicatieEindgebruiker());
    VerificatieIdenDocumentenVraagDE vraag = new VerificatieIdenDocumentenVraagDE();

    vraag.setVraagnummer(1);
    vraag.setDocumentNummer(getDocumentNummer());
    vraag.setDocumentType(getDocumentType());
    ArrayOfVerificatieIdenDocumentenVraagDE vragen = new ArrayOfVerificatieIdenDocumentenVraagDE();

    vragen.getVerificatieIdenDocumentenVraagDE().add(vraag);
    berichtIn.setIdenDocumentenVraag(vragen);
    return berichtIn;
  }

  @Override
  public int getVerwerkingSuccesCode() {
    return CODES.VerificatieIdentiteitsDocument.VERWERKING_SUCCESS;
  }

  public boolean isIdentiteitsDocument() {
    try {
      return ((ZoekNrBU) getResponseObject()).getResultaat().getZoekNrAntwoordDE().get(
          0).getResultaatCode() == CODES.VerificatieIdentiteitsDocument.SUCCESS;
    } catch (Exception e) {
      LOGGER.debug(e.toString());
      return false;
    }
  }
}
