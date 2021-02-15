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

package nl.procura.gba.web.services.zaken.documenten.stempel.stempels;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;

public class OsaraQrZaakStempel {

  public static String getTekst(Zaak zaak) {

    StringBuilder qrTekst = new StringBuilder();
    qrTekst.append("Zaak-id: ");
    qrTekst.append(ZaakUtils.getRelevantZaakId(zaak));
    qrTekst.append(";Product-Id: ");
    qrTekst.append(zaak.getType().getCode());
    qrTekst.append(";Hoofdbewoner: " + false);
    qrTekst.append(";");

    return qrTekst.toString();
  }
}
