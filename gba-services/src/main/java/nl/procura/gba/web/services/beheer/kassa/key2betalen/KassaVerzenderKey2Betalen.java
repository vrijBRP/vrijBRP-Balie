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

package nl.procura.gba.web.services.beheer.kassa.key2betalen;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.util.List;

import nl.procura.gba.web.services.beheer.kassa.KassaLeverancierType;
import nl.procura.gba.web.services.beheer.kassa.KassaLokaal;
import nl.procura.gba.web.services.beheer.kassa.gkas.KassaParameters;
import nl.procura.standard.exceptions.ProException;

public class KassaVerzenderKey2Betalen {

  private KassaParameters parameters;

  public KassaVerzenderKey2Betalen(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public KassaParameters getParameters() {
    return parameters;
  }

  public void setParameters(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public boolean verstuur(List<String> bestanden) {

    if (emp(getParameters().getFilename())) {
      throw new ProException(CONFIG, WARNING, "Geen uitvoerbestand voor de kassakoppeling gedefinieërd.");
    }

    if (bestanden.size() == 0) {
      throw new ProException(CONFIG, WARNING, "Geen uitvoer voor de kassakoppeling gedefinieërd.");
    }

    switch (getParameters().getType()) {
      case LOKAAL:
      default:
        return new KassaLokaal().verstuur(KassaLeverancierType.CENTRIC, parameters, bestanden);

      case FTP:
        throw new ProException(WARNING,
            "Versturen van bestand naar de kassa is niet mogelijk via FTP bij deze kassasoort.");
    }
  }
}
