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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.util.List;

import nl.procura.gba.web.services.beheer.kassa.transports.KassaConnect;
import nl.procura.gba.web.services.beheer.kassa.transports.KassaFtp;
import nl.procura.gba.web.services.beheer.kassa.transports.KassaLocal;
import nl.procura.standard.exceptions.ProException;

public class KassaSender {

  private KassaParameters parameters;

  public KassaSender(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public KassaParameters getParameters() {
    return parameters;
  }

  public void setParameters(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public void send(List<KassaFile> bestanden) {
    if (bestanden.size() == 0) {
      throw new ProException(CONFIG, WARNING, "Geen uitvoer voor de kassakoppeling gedefinieërd.");
    }

    switch (getParameters().getType()) {
      case LOKAAL:
      default:
        new KassaLocal().send(parameters, bestanden);
        break;
      case FTP:
        new KassaFtp().send(parameters, bestanden);
        break;
      case CONNECT:
        new KassaConnect().send(parameters, bestanden);
    }
  }
}
