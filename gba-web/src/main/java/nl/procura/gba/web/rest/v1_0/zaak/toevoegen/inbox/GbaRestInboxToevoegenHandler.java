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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import com.sun.jersey.core.util.Base64;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakAlgemeenToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxService;

public class GbaRestInboxToevoegenHandler extends GbaRestZaakAlgemeenToevoegenHandler {

  public GbaRestInboxToevoegenHandler(Services services) {
    super(services);
  }

  public List<GbaRestElement> toevoegen(GbaRestZaakToevoegenVraag vraagParm) {

    GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag(vraagParm);
    GemeenteInboxService service = getServices().getInboxService();
    GemeenteInboxRecord inboxRecord = (GemeenteInboxRecord) service.getNewZaak();

    inboxRecord.setNieuw(vraag.isNieuweZaak());
    inboxRecord.setZaakIdExtern(vraag.getZaakId());
    inboxRecord.setBestandsnaam(vraag.getBestandsnaam());
    inboxRecord.setOmschrijving(vraag.getOmschrijving());
    inboxRecord.setBron(vraag.getBron());
    inboxRecord.setLeverancier(vraag.getLeverancier());
    inboxRecord.setBestandsbytes(Base64.decode(vraag.getBestand()));
    inboxRecord.setVerwerkingId(vraag.getVerwerkingId());

    // Als datumingang leeg is dan
    // deze vullen met datum invoer
    if (pos(vraag.getDatumIngang())) {
      inboxRecord.setDatumIngang(new DateTime(vraag.getDatumIngang()));
    } else {
      inboxRecord.setDatumIngang(new DateTime(inboxRecord.getDatumTijdInvoer().getLongDate()));
    }

    service.save(inboxRecord);

    addZaakRelatie(inboxRecord);

    return getAntwoord(inboxRecord.getZaakId(), GbaRestZaakType.INBOX);
  }

  protected void addZaakRelatie(GemeenteInboxRecord inboxRecord) {

    String externZaakId = inboxRecord.getZaakIdExtern();
    ZaakKey zaakKey = getInternalZaakKey(externZaakId);

    if (zaakKey != null) {

      ZaakRelatieService relaties = getServices().getZaakRelatieService();
      ZaakRelatie relatie = new ZaakRelatie();
      relatie.setZaakId(zaakKey.getZaakId());
      relatie.setZaakType(zaakKey.getZaakType());
      relatie.setGerelateerdZaakId(inboxRecord.getZaakId());
      relatie.setGerelateerdZaakType(ZaakType.INBOX);

      relaties.save(relatie);
    }
  }

  /**
   * Zoek een gerelateerde zaak
   */
  private ZaakKey getInternalZaakKey(String externZaakId) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(externZaakId);
    zaakArgumenten.setTypen(
        ZaakType.HUWELIJK_GPS_GEMEENTE); // Zoeken in relevante zaakTypes (aanvullen indien nodig)
    for (ZaakKey zaakKey : getServices().getZakenService().getZaakKeys(zaakArgumenten)) {
      if (zaakKey.getZaakType() != ZaakType.INBOX) {
        return zaakKey;
      }
    }

    return null;
  }
}
