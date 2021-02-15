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

package nl.procura.gba.web.services.beheer.personmutations;

import static nl.procura.gba.common.MiscUtils.copyList;

import java.math.BigDecimal;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.PlMutDao;
import nl.procura.gba.jpa.personen.dao.UsrDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.standard.Globalfunctions;

public class PersonListMutationsService extends AbstractZaakService<PersonListMutation>
    implements ZaakService<PersonListMutation> {

  public PersonListMutationsService() {
    super("Persoonslijstmutaties", ZaakType.PL_MUTATION);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van GPK records")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return PlMutDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van persoonslijst mutatie records")
  public List<PersonListMutation> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(PlMutDao.find(getArgumentenToMap(zaakArgumenten)), PersonListMutation.class));
  }

  @Override
  public PersonListMutation getNewZaak() {
    return aanvullenZaak(new PersonListMutation());
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return PlMutDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van de aanvraag")
  public void save(PersonListMutation zaak, List<PlMutRec> mutationRecords) {
    save(zaak);
    for (PlMutRec mutRec : mutationRecords) {
      mutRec.getId().setcPlMutRec(zaak.getCPlMut());
      saveEntity(mutRec);
    }
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de aanvraag")
  public void save(PersonListMutation zaak) {
    if (!zaak.isStored()) {
      DateTime currentDate = new DateTime();
      BasePLExt pl = getServices().getPersonenWsService().getHuidige();
      zaak.setUsr(UsrDao.find(Usr.class, getServices().getGebruiker().getCUsr()));
      zaak.setLocation(UsrDao.find(Location.class, getServices().getGebruiker().getLocatie().getCLocation()));
      zaak.setDIn(BigDecimal.valueOf(currentDate.getLongDate()));
      zaak.setTIn(BigDecimal.valueOf(currentDate.getLongTime()));
      zaak.setAnr(BigDecimal.valueOf(pl.getPersoon().getAnr().toLong()));
      zaak.setBsn(BigDecimal.valueOf(pl.getPersoon().getBsn().toLong()));
    }

    getZaakStatussen().setInitieleStatus(zaak);
    opslaanStandaardZaak(zaak);
    callListeners(ServiceEvent.CHANGE);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(PersonListMutation zaak) {
    if (Globalfunctions.fil(zaak.getZaakId())) {
      ConditionalMap map = new ConditionalMap();
      map.put(PlMutDao.ZAAK_ID, zaak.getZaakId());
      map.put(PlMutDao.MAX_CORRECT_RESULTS, 1);
      removeEntities(PlMutDao.find(map));
      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
