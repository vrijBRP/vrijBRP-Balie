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

package nl.procura.gba.web.services.zaken.algemeen.zaakrelaties;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.*;
import java.util.Map.Entry;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.dao.ZaakRelDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentTypeUtils;
import nl.procura.commons.core.exceptions.ProException;

public class ZaakRelatieService extends AbstractService {

  public ZaakRelatieService() {
    super("Zaakrelaties");
  }

  /**
   * Zoek een gerelateerde zaak op basis van een documenttype
   */
  public Zaak getGerelateerdeDocumentZaak(Zaak zaak, DocumentRecord document) {

    ZaakType zaakType = DocumentTypeUtils.getZaakType(document.getDocumentType());
    Services services = getServices();
    return services.getZaakRelatieService().getGerelateerdeZaakByType(zaak, zaakType, true);
  }

  /**
   * Zoek een gerelateerde zaak van een specifiek type
   */
  public Zaak getGerelateerdeZaakByType(Zaak zaak, ZaakType zaakType, boolean recursief) {

    Set<String> zaakIds = new HashSet<>();
    return getGerelateerdeZaakByZaakType(zaakIds, zaak, zaakType, recursief);
  }

  /**
   * Zoek de zaakrelaties
   */
  public ZaakRelaties getGerelateerdeZaakRelaties(Zaak zaak) {

    ZaakRelaties relaties = new ZaakRelaties();
    List<ZaakRelatie> zaakRelaties = relaties.getRelaties();

    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakRelDao.ZAAK_ID, zaak.getZaakId());
    zaakRelaties.addAll(copyList(ZaakRelDao.find(map), ZaakRelatie.class));

    return relaties;
  }

  public Map<ZaakRelatie, Zaak> getGerelateerdeZaken(ZaakRelaties relaties) {

    Map<ZaakRelatie, Zaak> map = new LinkedHashMap<>();

    if (relaties.exists()) {

      ZaakArgumenten zaakArgumenten = new ZaakArgumenten();

      for (ZaakRelatie relatie : relaties.getRelaties()) {

        // Als er een zaaktype is ingevuld dan specifiek daarop zoeken
        if (ZaakType.ONBEKEND != relatie.getGerelateerdZaakType()) {
          zaakArgumenten.addTypen(relatie.getGerelateerdZaakType());
        }

        // Zoeken op gerelateerd zaakid
        zaakArgumenten.addZaakKey(new ZaakKey(relatie.getGerelateerdZaakId()));
        map.put(relatie, null);
      }

      List<Zaak> zaken = getServices().getZakenService().getMinimaleZaken(zaakArgumenten);

      // Zoek zaak bij zaakRelatie
      for (Entry<ZaakRelatie, Zaak> entry : map.entrySet()) {
        for (Zaak zaak : zaken) {
          if (entry.getKey().isVanToepassingOp(zaak)) {
            entry.setValue(zaak);
          }
        }
      }
    }

    return map;
  }

  @Transactional
  public void save(ZaakRelatie relatie) {

    ZaakArgumenten args = new ZaakArgumenten();
    args.addZaakKey(new ZaakKey(relatie.getGerelateerdZaakId()));
    List<Zaak> zaken = getServices().getZakenService().getMinimaleZaken(args);

    for (ZaakKey zaakKey : args.getZaakKeys()) {
      boolean gevonden = false;
      for (Zaak zaak : zaken) {
        if (zaak.getZaakId().equals(zaakKey.getZaakId())) {
          relatie.setGerelateerdZaakType(zaak.getType());
          gevonden = true;
        }
      }

      if (!gevonden) {
        throw new ProException(WARNING, "Geen zaak gevonden met zaak-id {0}", zaakKey);
      }
    }

    saveEntity(relatie);
  }

  public void updateZaakRelaties(Zaak zaak) {

    ZaakHistorie historie = zaak.getZaakHistorie();
    historie.setRelaties(getGerelateerdeZaakRelaties(zaak));
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {

    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakRelDao.ZAAK_ID, zaak.getZaakId());
    removeEntities(ZaakRelDao.findBoth(map));
  }

  @ThrowException("Fout bij het ontkoppelen")
  @Transactional
  public void delete(ZaakRelatie relatie) {

    ConditionalMap map = new ConditionalMap();
    map.putString(ZaakRelDao.ZAAK_ID, relatie.getZaakId());
    map.putString(ZaakRelDao.ZAAK_ID_REL, relatie.getGerelateerdZaakId());
    removeEntities(ZaakRelDao.findBoth(map));
  }

  private Zaak getGerelateerdeZaakByZaakType(Set<String> zaakIds, Zaak zaak, ZaakType zaakType, boolean recursief) {

    if (!zaak.isStored() || (zaak.getType() == zaakType) || (ZaakType.ONBEKEND == zaakType)) {
      return zaak;
    }

    zaakIds.add(zaak.getZaakId());

    ZaakRelaties zaakRelaties = getGerelateerdeZaakRelaties(zaak);

    for (Entry<ZaakRelatie, Zaak> entry : getGerelateerdeZaken(zaakRelaties).entrySet()) {

      if (entry.getValue() != null) {

        Zaak relatieZaak = entry.getValue();
        String relatieZaakId = relatieZaak.getZaakId();

        // Als gerelateerde zaak van het juiste type is dan deze nemen
        if (relatieZaak.getType() == zaakType) {
          return relatieZaak;
        }

        // Zaken die al zijn gezocht overslaan
        if (zaakIds.contains(entry.getKey().getGerelateerdZaakId())) {
          continue;
        }

        zaakIds.add(relatieZaakId);

        if (recursief) {
          return getGerelateerdeZaakByZaakType(zaakIds, relatieZaak, zaakType, recursief);
        }
      }
    }

    return null;
  }
}
