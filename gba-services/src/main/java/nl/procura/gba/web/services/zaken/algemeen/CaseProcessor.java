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

package nl.procura.gba.web.services.zaken.algemeen;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.StreamUtils;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

import lombok.Getter;

@Getter
public abstract class CaseProcessor<T> {

  private StringBuilder              log    = new StringBuilder();
  private final CaseProcessingResult result = new CaseProcessingResult();
  private final Services             services;

  public CaseProcessor(Services services) {
    this.services = services;
  }

  public abstract CaseProcessingResult process(T zaak);

  public void log(ProExceptionSeverity severity, String message, Object... args) {
    String line = MessageFormat.format(message, args);
    result.getInfo().add(new ProException(severity, line));
    log.append(severity.getCode() + ": " + line);
    log.append("\n");
  }

  protected void clearLog() {
    log = new StringBuilder();
  }

  protected void addZaakRelatie(String zaakId, GemeenteInboxRecord inboxRecord, ZaakType zaakType) {
    ZaakRelatieService relaties = getServices().getZaakRelatieService();
    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(zaakId);
    relatie.setZaakType(zaakType);
    relatie.setGerelateerdZaakId(inboxRecord.getZaakId());
    relatie.setGerelateerdZaakType(ZaakType.INBOX);

    relaties.save(relatie);
  }

  protected <T> T fromStream(GemeenteInboxRecord inboxRecord, Class<T> cl) {
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
