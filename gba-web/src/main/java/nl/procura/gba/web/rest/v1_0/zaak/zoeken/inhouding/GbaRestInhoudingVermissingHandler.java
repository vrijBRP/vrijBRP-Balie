/*
 * Copyright 2024 - 2025 Procura B.V.
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

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ALLEEN_BASISREGISTER;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ANR;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.BSN;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.DATUM_INVOER;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.DEELZAAK;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.DEELZAKEN;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.DOCUMENT_TYPE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.INHOUDING_TYPE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.MELDING_TYPE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.NUMMER;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.OMSCHRIJVING;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.REDEN_TYPE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.REGISTRATIE_MELDING;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.RIJBEWIJS;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.TIJD_INVOER;

import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.gba.common.DateTime;
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

    /*
     * Registratie melding is enabled in the VRS service configuration
     */
    if (getServices().getReisdocumentService().getVrsService().isRegistratieMeldingEnabled()) {
      GbaRestElement regMelding = inhouding.add(REGISTRATIE_MELDING);
      DateTime vrsDatumTijd = zaak.getVrsDatumTijd();
      VrsMeldingType meldingType = zaak.getVrsMelding();
      VrsMeldingRedenType redenType = zaak.getVrsReden();
      add(regMelding, DATUM_INVOER, vrsDatumTijd);
      add(regMelding, TIJD_INVOER, vrsDatumTijd.getLongTime(), vrsDatumTijd.getFormatTime());
      add(regMelding, MELDING_TYPE, meldingType.getCode(), meldingType.getDescription());
      add(regMelding, REDEN_TYPE, redenType.getCode(), redenType.getDescription());
      add(regMelding, ALLEEN_BASISREGISTER, zaak.isVrsOnlyBasisregister());
    }

    addDeelzaken(gbaZaak, zaak);
  }
}
