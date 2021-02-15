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

package nl.procura.gba.web.services.zaken.curatele;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.CURATELE_URL;

import java.util.List;

import nl.procura.curatele.soap.CurateleSoapHandler;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.rechtspraak.namespaces.ccr.CCRWS;

public class CurateleService extends AbstractService {

  public CurateleService() {
    super("Curatele");
  }

  public List<CCRWS> verstuur(String voorv, String achternaam, String geboorteDatum) {

    String curEndpoint = getProxyUrl(CURATELE_URL, true);
    String curGebruiker = getParm(ParameterConstant.CURATELE_GEBRUIKERSNAAM, true);
    String curWachtwoord = getParm(ParameterConstant.CURATELE_WACHTWOORD, true);

    List<CCRWS> list;

    try {
      list = new CurateleSoapHandler(curEndpoint, curGebruiker, curWachtwoord).send(voorv, achternaam,
          geboorteDatum);
    } catch (RuntimeException e) {
      throw new ProException(ProExceptionSeverity.ERROR, "Fout bij het zoeken van curatele gegevens", e);
    }

    for (CCRWS persoon : list) {
      if (persoon.getExceptie() != null) {
        if (persoon.getExceptie().getErrorcode().equals("1")) {
          throw new ProException(ProExceptionSeverity.INFO, "Geen personen gevonden");
        }

        throw new RuntimeException(persoon.getExceptie().getValue());
      }
    }

    return list;
  }
}
