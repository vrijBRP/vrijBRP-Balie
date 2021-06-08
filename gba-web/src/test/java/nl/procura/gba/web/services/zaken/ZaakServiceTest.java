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

package nl.procura.gba.web.services.zaken;

import java.util.UUID;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.ServicesMock;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.*;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;

public class ZaakServiceTest {

  protected final Services services;

  public ZaakServiceTest() {
    this(true);
  }

  public ZaakServiceTest(boolean clean) {
    if (clean) {
      TemporaryDatabase.ensureCleanMockDatabase();
    }
    services = new ServicesMock();
  }

  protected String getNewZaakId(ZaakType zaakType) {
    return "mock-" + zaakType.getCode() + "-" + UUID.randomUUID();
  }

  protected Zaak refresh(Zaak zaak) {
    ZaakArgumenten args = new ZaakArgumenten(zaak.getZaakId());
    return services.getZakenService().getStandaardZaken(args).get(0);
  }

  protected boolean verwijder(Zaak zaak) {
    services.getZakenService().delete(new ZaakKey(zaak.getZaakId()));
    return getAantal(zaak) == 0;
  }

  protected boolean opslaan(Zaak zaak) {
    ZaakService<Zaak> zaakService = services.getZakenService().getService(zaak);
    zaakService.save(zaak);
    return getAantal(zaak) == 1;
  }

  protected boolean status(Zaak zaak) {
    ZaakStatusService statusService = services.getZaakStatusService();
    statusService.updateStatus(zaak, ZaakStatusType.VERWERKT, "Mockstatus!");
    return ZaakStatusType.VERWERKT.is(refresh(zaak).getStatus());
  }

  protected boolean aantekening(Zaak zaak) {
    AantekeningService service = services.getAantekeningService();

    PlAantekening aantekening = new PlAantekening();
    aantekening.setZaakId(zaak.getZaakId());

    PlAantekeningHistorie historie = new PlAantekeningHistorie();
    historie.setTijdstip(new DateTime());
    historie.setGebruiker(new UsrFieldValue(services.getGebruiker()));
    historie.setIndicatie(new PlAantekeningIndicatie(PlAantekeningIndicatie.VRIJE_AANTEKENING));
    historie.setOnderwerp("Mockonderwerp");
    historie.setInhoud("Mockinhoud");
    historie.setHistorieStatus(PlAantekeningStatus.AFGESLOTEN);

    service.save(aantekening, historie);
    return ZaakStatusType.VERWERKT.is(refresh(zaak).getStatus());
  }

  protected int getAantal(Zaak zaak) {
    return services.getZakenService().getAantalZaken(new ZaakArgumenten(zaak.getZaakId()));
  }
}
