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

package nl.procura.gba.web.services.zaken.algemeen.identificatie;

import static nl.procura.gba.common.MiscUtils.copyList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakIdDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;

public class ZaakIdentificatieService extends AbstractService {

  public ZaakIdentificatieService() {
    super("Zaakidentificaties");
  }

  /**
   * Alternatief zaak-id opvragen bij de BSM
   */
  @ThrowException("Het is niet mogelijk om een zaak-id op te vragen")
  public String getDmsZaakId(Zaak zaak) {
    return getServices().getZaakDmsService().genereerZaakId(zaak);
  }

  /**
   * Vertaald eventuele extern zaakId naar een intern zaakId
   */
  public Set<ZaakKey> getIdentificatie(Set<ZaakKey> zaakKeys) {
    ConditionalMap map = new ConditionalMap();
    Set<ZaakKey> altZaakKeys = new HashSet<>();
    map.putList(ZaakIdDao.EXTERN_ID, zaakKeys.stream()
        .map(ZaakKey::getZaakId)
        .collect(Collectors.toList()));
    for (ZaakId id : ZaakIdDao.find(map)) {
      altZaakKeys.add(new ZaakKey(id.getId().getInternId(), ZaakType.get(id.getZaakTypeCode())));
    }

    return altZaakKeys;
  }

  /**
   * Vertaald eventuele extern zaakId naar een intern zaakId
   */
  public ZaakKey getIdentificatie(ZaakKey zaakKey) {
    for (ZaakKey zk : Sets.newHashSet(zaakKey)) {
      return zk;
    }
    return zaakKey;
  }

  /**
    Zoek op basis van een extern zaakId in de database    
   */
  public boolean isExternId(String id) {
    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakIdDao.EXTERN_ID, id);
    return !ZaakIdDao.find(map).isEmpty();
  }

  public ZaakIdentificaties getIdentificaties(Zaak zaak) {

    ZaakIdentificaties identificaties = new ZaakIdentificaties();
    List<ZaakIdentificatie> nummers = identificaties.getNummers();

    ZaakIdentificatie prowebId = new ZaakIdentificatie();
    prowebId.setInternId(zaak.getZaakId());
    prowebId.setExternId(zaak.getZaakId());
    prowebId.setType(ZaakIdType.PROWEB_PERSONEN.getCode());
    prowebId.setZaakType(zaak.getType());
    nummers.add(prowebId);

    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakIdDao.INTERN_ID, zaak.getZaakId());
    nummers.addAll(copyList(ZaakIdDao.find(map), ZaakIdentificatie.class));

    return identificaties;
  }

  public boolean isProwebPersonen(ZaakIdentificatie id) {
    return ZaakIdType.PROWEB_PERSONEN == id.getZaakIdType();
  }

  public void save(Zaak zaak) {
    for (ZaakIdentificatie id : zaak.getZaakHistorie().getIdentificaties().getNummers()) {
      if (!isProwebPersonen(id)) {
        getServices().getZaakIdentificatieService().save(id);
      }
    }
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(ZaakIdentificatie id) {
    saveEntity(id);
  }

  public void updateZaakIdentificaties(Zaak zaak) {
    ZaakHistorie historie = zaak.getZaakHistorie();
    historie.setIdentificaties(getIdentificaties(zaak));
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    removeEntities(getIdentificaties(zaak).getNummers());
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(ZaakIdentificatie id) {
    removeEntity(id);
  }
}
