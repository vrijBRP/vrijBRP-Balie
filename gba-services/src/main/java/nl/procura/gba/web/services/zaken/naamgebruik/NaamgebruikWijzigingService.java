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

package nl.procura.gba.web.services.zaken.naamgebruik;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.NaamgebruikDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;

public class NaamgebruikWijzigingService extends AbstractZaakContactService<NaamgebruikAanvraag>
    implements ZaakService<NaamgebruikAanvraag> {

  public NaamgebruikWijzigingService() {
    super("Naamgebruik", ZaakType.NAAMGEBRUIK);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de naamgebruikaanvragen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return NaamgebruikDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(NaamgebruikAanvraag zaak) {

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
  @Timer
  @ThrowException("Fout bij het zoeken van de naamgebruikaanvragen")
  public List<NaamgebruikAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(
        copyList(NaamgebruikDao.find(getArgumentenToMap(zaakArgumenten)), NaamgebruikAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new NaamgebruikAanvraag());
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return NaamgebruikDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  /**
   * Opslaan van de naamgebruikaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van het record")
  public void save(NaamgebruikAanvraag zaak) {

    ZaakStatusService zaakStatus = getZaakStatussen();

    if (getServices().getPersonenWsService().getHuidige().getDatasource() == PLEDatasource.GBAV) {
      zaakStatus.setInitieleStatus(zaak, ZaakStatusType.WACHTKAMER, " Niet-inwoner");
    } else {
      zaakStatus.setInitieleStatus(zaak);
    }

    opslaanStandaardZaak(zaak);
    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijderen van de naamgebruikaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(NaamgebruikAanvraag zaak) {

    if (fil(zaak.getZaakId())) {

      ConditionalMap map = new ConditionalMap();
      map.put(NaamgebruikDao.ZAAK_ID, zaak.getZaakId());
      map.put(NaamgebruikDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(NaamgebruikDao.find(map));
      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
