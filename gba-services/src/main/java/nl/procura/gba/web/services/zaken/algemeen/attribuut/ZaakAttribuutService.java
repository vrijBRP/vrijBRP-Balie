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

package nl.procura.gba.web.services.zaken.algemeen.attribuut;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import javax.inject.Singleton;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.dao.ZaakAttrDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.standard.exceptions.ProException;

@Singleton
public class ZaakAttribuutService extends AbstractService {

  public ZaakAttribuutService() {
    super("Zaakattributen");
  }

  public AttribuutHistorie getAttributen(String zaakId, String attribuut) {

    if (emp(zaakId)) {
      throw new ProException(ERROR, "ZaakId is leeg");
    }

    if (emp(attribuut)) {
      throw new ProException(ERROR, "Attribuut is leeg");
    }

    ZaakIdentificatieService identificaties = getServices().getZaakIdentificatieService();

    AttribuutHistorie historie = new AttribuutHistorie();
    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakAttrDao.ZAAK_ID, identificaties.getIdentificatie(new ZaakKey(zaakId)).getZaakId());
    map.putString(ZaakAttrDao.ZAAK_ATTR, attribuut);
    historie.setAttributen(copyList(ZaakAttrDao.find(map), ZaakAttribuut.class));

    return historie;
  }

  public AttribuutHistorie getAttributen(Zaak zaak) {

    AttribuutHistorie historie = new AttribuutHistorie();
    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakAttrDao.ZAAK_ID, zaak.getZaakId());
    historie.setAttributen(copyList(ZaakAttrDao.find(map), ZaakAttribuut.class));

    return historie;
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(ZaakAttribuut id) {
    saveEntity(id);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    removeEntities(getAttributen(zaak).getAttributen());
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(ZaakAttribuut id) {
    removeEntity(id);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(String zaakId, ZaakAttribuutType type) {
    removeEntities(getAttributen(zaakId, type.getCode()).getAttributen());
  }
}
