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

package nl.procura.gba.web.services.zaken.correspondentie;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.CorrespondentieDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Correspondentie;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;

public class CorrespondentieService extends AbstractZaakContactService<CorrespondentieZaak>
    implements ZaakService<CorrespondentieZaak>, ControleerbareService {

  public CorrespondentieService() {
    super("Correspondenties", ZaakType.CORRESPONDENTIE);
  }

  @Override
  @ThrowException("Fout bij het zoeken van correspondentie records")
  @Timer
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return CorrespondentieDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(CorrespondentieZaak zaak) {

    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    return zaakContact;
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new CorrespondentieZaakControles(this).getControles(listener);
  }

  @Override
  @Timer
  public List<CorrespondentieZaak> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList<>(
        copyList(CorrespondentieDao.find(getArgumentenToMap(zaakArgumenten)), CorrespondentieZaak.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new CorrespondentieZaak());
  }

  @Override
  @ThrowException("Fout bij het zoeken van zaak-ids")
  @Timer
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return CorrespondentieDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public void save(CorrespondentieZaak zaak) {
    save(zaak, getZaakStatussen().getInitieleStatus(zaak));
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van de aanvraag")
  public void save(CorrespondentieZaak zaak, ZaakStatusType initieleStatus) {
    getZaakStatussen().setInitieleStatus(zaak, initieleStatus);
    opslaanStandaardZaak(zaak);
    callListeners(ServiceEvent.CHANGE);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(CorrespondentieZaak zaak) {

    if (fil(zaak.getZaakId())) {

      ConditionalMap map = new ConditionalMap();
      map.put(CorrespondentieDao.ZAAK_ID, zaak.getZaakId());
      map.put(CorrespondentieDao.MAX_CORRECT_RESULTS, 1);

      for (Correspondentie e : CorrespondentieDao.find(map)) {
        removeEntity(e);
      }

      // Verwijder gerelateerde uittreksels
      Zaak gerelateerdeZaak = getServices().getZaakRelatieService().getGerelateerdeZaakByType(zaak,
          ZaakType.UITTREKSEL,
          false);
      if (gerelateerdeZaak != null && gerelateerdeZaak.getType() == ZaakType.UITTREKSEL) {
        getServices().getZakenService().getService(gerelateerdeZaak).delete(gerelateerdeZaak);
      }

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
