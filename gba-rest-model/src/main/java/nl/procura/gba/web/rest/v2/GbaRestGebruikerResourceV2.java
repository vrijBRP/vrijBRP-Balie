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

package nl.procura.gba.web.rest.v2;

import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerVraag;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerZoekenAntwoord;

public interface GbaRestGebruikerResourceV2 {

  String BASE_GEBRUIKER_URI = "/v2/gebruiker";

  GbaRestAntwoord<GbaRestGebruikerZoekenAntwoord> getGebruiker();

  GbaRestAntwoord<GbaRestGebruikerZoekenAntwoord> getGebruiker(GbaRestGebruikerVraag vraag);
}