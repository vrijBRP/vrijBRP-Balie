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

package nl.procura.gba.web.services.zaken.documenten.printen;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakPersoon;

public class PrintModelUtils {

  /**
   * Zoek het model op basis van de zaak en het document
   */
  public static Object getModel(Object model, Zaak zaak, DocumentRecord documentRecord) {

    if (documentRecord != null && zaak != null && documentRecord.getDocumentType() == DocumentType.ZAAK) {
      // Maak een ZaakSamenvatting als het documentType zaak is
      return new ZaakSamenvatting(zaak);
    } else if (model instanceof Zaak) {
      Zaak modelZaak = (Zaak) model;
      if (ZaakType.UITTREKSEL.is(modelZaak.getType())) {
        return toDocumentPersoonslijsten((DocumentZaak) modelZaak);
      }
    }

    return model;
  }

  private static List<DocumentPL> toDocumentPersoonslijsten(DocumentZaak aanvraag) {

    List<DocumentZaakPersoon> personen = aanvraag.getPersonen();
    List<DocumentPL> persoonslijsten = new ArrayList<>();

    for (DocumentZaakPersoon persoon : personen) {
      persoonslijsten.add(persoon.getPersoon());
    }

    return persoonslijsten;
  }
}
