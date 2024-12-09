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

package nl.procura.gba.web.services.zaken.identiteit;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.stream.Collectors;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.IdVastellingDao;
import nl.procura.gba.jpa.personen.db.Idvaststelling;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.reisdocumenten.IdentificatieBijUitreikingService;
import nl.procura.gba.web.services.zaken.reisdocumenten.IdentificatieUitreikingZaak;

public class IdentificatieService extends AbstractService {

  public IdentificatieService() {
    super("Identificatie");
  }

  @Transactional
  public void addIdentificatie(Identificatie id) {
    long bsn = along(id.getBurgerServiceNummer().getValue());
    long cUsr = getServices().getGebruiker().getCUsr();
    String nr = id.getDocumentnr();
    boolean ver = id.isExternGeverificeerd();
    String verOms = id.getExternVerificatieAntwoord();

    String sb = id.getIdentificatieTypes().stream().map(type -> type.getCode() + ", ").collect(Collectors.joining());
    IdVastellingDao.addVaststelling(bsn, cUsr, trim(sb), nr, ver, verOms);
    callListeners(ServiceEvent.CHANGE);
  }

  public Identificatie getIdentificatie(BasePLExt pl) {
    long bsn = along(pl.getPersoon().getBsn().getVal());
    long cUsr = along(getServices().getGebruiker().getCUsr());
    return getIdentificatie(bsn, cUsr, new DateTime());
  }

  public Identificatie getIdentificatie(Zaak zaak, DateTime dateTime) {
    long bsn = along(zaak.getBurgerServiceNummer().getValue());
    long cUsr = along(zaak.getIngevoerdDoor().getValue());
    return getIdentificatie(bsn, cUsr, dateTime);
  }

  public Identificatie getIdentificatie(Zaak zaak) {
    if (zaak != null) {
      long bsn = along(zaak.getBurgerServiceNummer().getValue());
      long cUsr = along(zaak.getIngevoerdDoor().getValue());
      return getIdentificatie(bsn, cUsr, zaak.getDatumTijdInvoer());
    }

    return new Identificatie();
  }

  public <T extends Zaak> void setIdentificatieByUitreiking(T zaak) {
    if (zaak instanceof IdentificatieUitreikingZaak) {
      ZaakService<Zaak> service = getServices().getZakenService().getService(zaak);
      if (service instanceof IdentificatieBijUitreikingService) {
        IdentificatieBijUitreikingService identificatieBijUitreikingServiceservice = (IdentificatieBijUitreikingService) service;
        identificatieBijUitreikingServiceservice.getIdentificatieBijUitreiking((IdentificatieUitreikingZaak) zaak);
        IdentificatieUitreikingZaak identificatieUitreikingZaak = (IdentificatieUitreikingZaak) zaak;
        identificatieUitreikingZaak.setIdentificatieBijUitreiking(
            identificatieBijUitreikingServiceservice.getIdentificatieBijUitreiking(identificatieUitreikingZaak));
      }
    }
  }

  public IdVerplichtMate getMateVerplicht() {
    return IdVerplichtMate.get(along(getServices().getParameterService().getParm(ParameterConstant.ID_VERPLICHT)));
  }

  public boolean isVastGesteld(BasePLExt pl) {
    Identificatie id = getIdentificatie(pl);
    return id != null && pos(id.getBurgerServiceNummer().getValue());
  }

  private Identificatie getIdentificatie(long bsn, long cUsr, DateTime dateTime) {
    Idvaststelling id = IdVastellingDao.getVaststelling(bsn, cUsr, dateTime.getLongDate(), dateTime.getLongTime());
    return id != null ? copy(id, Identificatie.class) : new Identificatie();
  }
}
