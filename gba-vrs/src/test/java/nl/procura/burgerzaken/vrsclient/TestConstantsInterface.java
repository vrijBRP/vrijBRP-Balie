/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.burgerzaken.vrsclient;

public interface TestConstantsInterface {

  String AANVRAAGNUMMER = "100000147";
  String DOCUMENTNUMMER = "IP8799D18";
  String BSN            = "999995947";
  String PSEUDONIEM     = "user123";
  String INSTANTIECODE  = "inst456";

  String NEEM_CONTACT_OP              = "Neem contact op met de Rijksdienst voor Identiteitsgegevens van het ministerie van Binnenlandse Zaken en Koninkrijksrelaties via het nummer 088-9001000.";
  String V2_SIGNALERING_ENDPOINT      = "/signaleringcontroles/v2/bsn";
  String V2_AANVRAGEN_ENDPOINT        = "/controleaanvragen/v2/personalisatiegegevens";
  String V2_AANVRAAG_ENDPOINT         = "/controleaanvragen/v2/aanvraag";
  String V2_AANVRAAG_DETAILS_ENDPOINT = "/controleaanvragen/v2/aanvraag/details";
  String V2_DOCUMENTEN_ENDPOINT       = "/reisdocumentinformatie/uitgevendeinstanties/v2/personalisatiegegevens";
  String V2_DOCUMENT_DETAILS_ENDPOINT = "/reisdocumentinformatie/uitgevendeinstanties/v2/documentnummer";
  String IDP_CONFIG_ENDPOINT          = "/idpconfiguratie/v1";
  String TOKEN_ENDPOINT               = "/token";
}
