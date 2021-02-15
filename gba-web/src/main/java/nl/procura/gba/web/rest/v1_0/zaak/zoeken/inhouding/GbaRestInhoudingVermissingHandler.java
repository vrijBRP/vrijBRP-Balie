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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.inhouding;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

public class GbaRestInhoudingVermissingHandler extends GbaRestElementHandler {

  public GbaRestInhoudingVermissingHandler(Services services) {
    super(services);
  }

  private static void addDeelzaken(GbaRestElement gbaZaak, DocumentInhouding zaak) {

    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

    add(deelZaak, BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, ANR, zaak.getAnummer());
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    DocumentInhouding zaak = (DocumentInhouding) zaakParm;

    GbaRestElement inhouding = gbaZaak.add(GbaRestElementType.INHOUDING_VERMISSING);

    InhoudingType inhoudingType = zaak.getInhoudingType();
    ReisdocumentType documentType = zaak.getDocumentType();

    add(inhouding, INHOUDING_TYPE, inhoudingType.getCode(), inhoudingType.getOms());
    add(inhouding, DOCUMENT_TYPE, documentType.getCode(), documentType.getOms());
    add(inhouding, NUMMER, zaak.getNummerDocument());
    add(inhouding, RIJBEWIJS, zaak.isSprakeVanRijbewijs());

    GbaRestElement pv = inhouding.add(GbaRestElementType.PROCES_VERBAAL);
    add(pv, NUMMER, zaak.getProcesVerbaalNummer());
    add(pv, OMSCHRIJVING, zaak.getProcesVerbaalOms());

    addDeelzaken(gbaZaak, zaak);
  }
}
