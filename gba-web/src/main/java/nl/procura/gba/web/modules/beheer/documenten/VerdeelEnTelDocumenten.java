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

package nl.procura.gba.web.modules.beheer.documenten;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

/**
 * Maak van een documentenlijst twee aparte lijsten van documenten:
 * een lijst van documenten waarvoor 'all-allowed' = 1 en een lijst
 * waarvoor 'all-allowed' = 0. Het aantal documenten met 'all-allowed = 1'
 * wordt bijgehouden. Wordt toegepast bij het koppelen/ontkoppelen van gebruikers
 * en documenten.
 *

 * <p>
 * 2012
 */
public class VerdeelEnTelDocumenten {

  private final List<DocumentRecord> nietAlleToegestaan = new ArrayList<>();
  private final List<DocumentRecord> welAlleToegestaan  = new ArrayList<>();

  public VerdeelEnTelDocumenten(List<DocumentRecord> documenten) {

    for (DocumentRecord doc : documenten) {
      if (doc.isIedereenToegang()) {
        welAlleToegestaan.add(doc);
      } else {
        nietAlleToegestaan.add(doc);
      }
    }
  }

  public int getAantalAllesToegestaan() {
    return welAlleToegestaan.size();
  }

  public List<DocumentRecord> getAlleToegestaan() {
    return welAlleToegestaan;
  }

  public List<DocumentRecord> getNietAlleToegestaan() {
    return nietAlleToegestaan;
  }
}
