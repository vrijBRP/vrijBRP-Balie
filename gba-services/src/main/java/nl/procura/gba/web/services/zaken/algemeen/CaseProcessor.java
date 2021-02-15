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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.StreamUtils;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;

public class CaseProcessor {

  private StringBuilder              log    = new StringBuilder();
  private final CaseProcessingResult result = new CaseProcessingResult();
  private final Services             services;

  public CaseProcessor(Services services) {
    this.services = services;
  }

  public void clearLog() {
    log = new StringBuilder();
  }

  public void log(ProExceptionSeverity severity, String message, Object... args) {
    String line = MessageFormat.format(message, args);
    result.getInfo().add(new ProException(severity, line));
    log.append(severity.getCode() + ": " + line);
    log.append("\n");
  }

  public StringBuilder getLog() {
    return log;
  }

  public CaseProcessingResult getResult() {
    return result;
  }

  public Services getServices() {
    return services;
  }

  protected void addZaakRelatie(String zaakId, InboxRecord inboxRecord, ZaakType zaakType) {
    ZaakRelatieService relaties = getServices().getZaakRelatieService();
    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(zaakId);
    relatie.setZaakType(zaakType);
    relatie.setGerelateerdZaakId(inboxRecord.getZaakId());
    relatie.setGerelateerdZaakType(ZaakType.INBOX);

    relaties.save(relatie);
  }

  protected <T> T fromStream(InboxRecord inboxRecord, Class<T> cl) {
    return StreamUtils.fromStream(cl, new ByteArrayInputStream(inboxRecord.getBestandsbytes()));
  }

  /**
   * Zaak
   */
  protected Zaak getZaak(String zaakNummer, ZaakType... zaakTypes) {

    ZakenService service = getServices().getZakenService();
    List<ZaakKey> zaakKeys = getZaakKeys(zaakNummer, zaakTypes);

    List<Zaak> zaken = new ArrayList<>();
    for (Zaak zaak : service.getMinimaleZaken(new ZaakArgumenten(zaakKeys))) {
      zaken.add(service.getVolledigeZaak(zaak));
    }

    return zaken.get(0);
  }

  /**
   * Zaakkeys
   */
  protected List<ZaakKey> getZaakKeys(String zaakNummer, ZaakType... zaakTypes) {

    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(zaakNummer);
    zaakArgumenten.setTypen(zaakTypes);

    ZakenService service = getServices().getZakenService();
    List<ZaakKey> zaakKeys = new ArrayList<>();
    for (ZaakKey zaakKey : service.getZaakKeys(zaakArgumenten)) {
      if (ZaakType.INBOX != zaakKey.getZaakType()) {
        zaakKeys.add(zaakKey);
      }
    }

    if (zaakKeys.isEmpty()) {
      throw new ProException(ERROR, "Geen zaak gevonden met zaak-id: " + zaakNummer);
    }

    if (zaakKeys.size() > 1) {
      throw new ProException(ERROR, "meerdere ({0}) zaken gevonden met zaak-id {1}", zaakKeys.size(), zaakNummer);
    }

    return zaakKeys;
  }

  /**
   * Zet pagina op 'reset'
   */
  protected void resetPagina(ZaakMetPagina zaak) {
    zaak.reset();
  }
}
