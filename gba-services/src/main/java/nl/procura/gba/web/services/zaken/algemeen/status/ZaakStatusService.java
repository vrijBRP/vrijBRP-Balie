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

package nl.procura.gba.web.services.zaken.algemeen.status;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.IndicatieVerwerktDao;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.standard.exceptions.ProException;

public class ZaakStatusService extends AbstractService {

  public ZaakStatusService() {
    super("Zaakstatus");
  }

  /**
   * Geeft de initiele status van de zaak
   */
  public ZaakStatusType getInitieleStatus(Zaak zaak) {
    return ZaakStatusUtils.getInitieleStatus(this, zaak);
  }

  public ZaakStatusHistorie getStatusHistorie(Zaak zaak) {

    ZaakStatusHistorie historie = new ZaakStatusHistorie();
    historie.setStatussen(
        copyList(IndicatieVerwerktDao.getByZaakId(zaak.getZaakId()), ZaakStatus.class));
    updateStatusDuur(zaak, historie);

    return historie;
  }

  /**
   * Zet de initiele status van de zaak
   */
  public <T extends Zaak> T setInitieleStatus(T zaak) {
    return setInitieleStatus(zaak, getInitieleStatus(zaak));
  }

  public <T extends Zaak> T setInitieleStatus(T zaak, ZaakStatusType statusType) {
    ZaakStatusType st = ZaakStatusType.ONBEKEND.is(statusType) ? getInitieleStatus(zaak) : statusType;
    return setInitieleStatus(zaak, st, "");
  }

  public <T extends Zaak> T setInitieleStatus(T zaak, ZaakStatusType statusType, String aantekening) {
    if (!isSaved(zaak)) {
      zaak.setStatus(statusType);
      toevoegenStatusHistorie(zaak, "Initiële status. " + aantekening);
    }

    return zaak;
  }

  @Transactional
  @ThrowException("Fout bij het updaten van de status")
  public void updateStatus(Zaak zaak, ZaakStatusType newStatus, String opmerking) {
    updateStatus(zaak, zaak.getStatus(), newStatus, opmerking);
  }

  /**
   * Algemene functie voor update van de status.
   * In principe is het gewoon opslaan van de zaak zonder nieuwe primaire sleutel
   */
  @Transactional
  @ThrowException("Fout bij het updaten van de status")
  public void updateStatus(Zaak zaak, ZaakStatusType huidigeStatus, ZaakStatusType newStatus, String opmerking) {

    if (fil(zaak.getZaakId()) && isUpdateStatusMogelijk(huidigeStatus, newStatus)) {
      ZaakStatusUtils.check(zaak, newStatus);

      if (zaak.getZaakHistorie().getStatusHistorie().size() == 0) {
        zaak.getZaakHistorie().setStatusHistorie(getStatusHistorie(zaak));
      }

      ZaakHistorie zh = zaak.getZaakHistorie();
      if (zh.getStatusHistorie().isHuidigeStatus(newStatus)) {
        // Geen dubbelen
        return;
      }

      if (huidigeStatus != null && !zh.getStatusHistorie().isInHistorieIngevoerd(huidigeStatus)) {
        // Voeg huidige status als historie toe
        saveEntity(new ZaakStatus(zaak.getZaakId(), huidigeStatus));
      }

      updateZaakStatus(zaak, newStatus);
      toevoegenStatusHistorie(zaak, opmerking);
      callListeners(ServiceEvent.CHANGE);

      // Document ontvangen
      getServices().getSmsService().addSms(zaak, newStatus);
    }
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    removeEntities(IndicatieVerwerktDao.getByZaakId(zaak.getZaakId()));
  }

  /**
   * Mag de gebruiker de eindstatus nog wijzigen?
   */
  private boolean isUpdateStatusMogelijk(ZaakStatusType huidigeStatus, ZaakStatusType nieuweStatusType) {

    boolean magEindStatusWijzigen = isTru(getParm(ParameterConstant.ZAKEN_EINDSTATUS));

    if ((huidigeStatus.isEindStatus() && nieuweStatusType.isEindStatus()) && !magEindStatusWijzigen) {
      return false;
    }

    if (huidigeStatus.isEindStatus() && !magEindStatusWijzigen) {
      throw new ProException(ENTRY, WARNING, "Zaken met een eindstatus kunnen niet meer worden gewijzigd");
    }

    return true;
  }

  /**
   * Create new status
   */
  private void toevoegenStatusHistorie(Zaak zaak, String opmerking) {

    if (!IndicatieVerwerktDao.isZaakId(zaak.getZaakId(), zaak.getStatus().getCode())) {

      ZaakStatus newStatus = new ZaakStatus();
      newStatus.setZaakId(zaak.getZaakId());
      newStatus.setIngevoerdDoor(new UsrFieldValue(getServices().getGebruiker()));
      newStatus.setDatumTijdInvoer(new DateTime());
      newStatus.setStatus(zaak.getStatus());
      newStatus.setOpmerking(opmerking);

      saveEntity(newStatus);

      // Laad de historie opnieuw
      zaak.getZaakHistorie().setStatusHistorie(getStatusHistorie(zaak));
    }
  }

  private void updateStatusDuur(Zaak zaak, ZaakStatusHistorie statusHistorie) {

    boolean hasStatus = statusHistorie.size() > 0;
    boolean isLastStatus = hasStatus && (statusHistorie.getHuidigeStatus().getStatus() == zaak.getStatus());

    // Als huidige status bij zaak hetzelfde is als status in historie dan niet tonen

    if (!isLastStatus) {
      statusHistorie.getStatussen().add(0, new ZaakStatus(zaak.getStatus()));
    }

    for (int i = 0; i < statusHistorie.size(); i++) {

      ZaakStatus vorigeStatus = statusHistorie.getStatussen().get(i);
      boolean heeftEerdereStatus = (i + 1) < statusHistorie.size();

      if (heeftEerdereStatus) {

        ZaakStatus volgendeStatus = statusHistorie.getStatussen().get(i + 1);

        long vorigeDatumTijd = vorigeStatus.getDatumTijdInvoer().getDate().getTime();
        long volgendeDatumTijd = volgendeStatus.getDatumTijdInvoer().getDate().getTime();
        long duur = (vorigeDatumTijd - volgendeDatumTijd);

        vorigeStatus.setDuur(duur);
      }
    }
  }

  /**
   * Update zaak status
   */
  private void updateZaakStatus(Zaak zaak, ZaakStatusType nieuweStatus) {
    zaak.getZaakHistorie().addWijziging("Status van de zaak gewijzigd in " + nieuweStatus.getOms());
    zaak.setStatus(nieuweStatus);

    saveEntity(zaak);
  }
}
