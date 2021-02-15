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

package nl.procura.gba.web.services.zaken.gpk;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GpkDao;
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

public class GpkService extends AbstractZaakContactService<GpkAanvraag>
    implements ZaakService<GpkAanvraag> {

  public GpkService() {
    super("GPK", ZaakType.GPK);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van GPK records")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return GpkDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(GpkAanvraag zaak) {

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
  @ThrowException("Fout bij het zoeken van GPK records")
  public List<GpkAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(GpkDao.find(getArgumentenToMap(zaakArgumenten)), GpkAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new GpkAanvraag());
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return GpkDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de aanvraag")
  public void save(GpkAanvraag zaak) {

    getZaakStatussen().setInitieleStatus(zaak);

    if (isSaved(zaak)) {
      opslaanStandaardZaak(zaak);
    } else {

      opslaanStandaardZaak(zaak);
      getServices().getKassaService().addToWinkelwagen(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(GpkAanvraag zaak) {

    if (fil(zaak.getZaakId())) {

      ConditionalMap map = new ConditionalMap();
      map.put(GpkDao.ZAAK_ID, zaak.getZaakId());
      map.put(GpkDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(GpkDao.find(map));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
