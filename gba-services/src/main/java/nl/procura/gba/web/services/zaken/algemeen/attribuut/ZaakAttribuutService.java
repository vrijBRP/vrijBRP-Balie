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
import static nl.procura.gba.jpa.personen.dao.GenericDao.ZAAK_ID;
import static nl.procura.gba.jpa.personen.dao.ZaakAttrDao.C_USR;
import static nl.procura.gba.jpa.personen.dao.ZaakAttrDao.ZAAK_ATTR;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.FAVORIET;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.procura.gba.jpa.personen.dao.*;
import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.standard.exceptions.ProException;

public class ZaakAttribuutService extends AbstractService {

  public ZaakAttribuutService() {
    super("Zaakattributen");
  }

  public AttribuutHistorie getZaakAttributen(String zaakId, String attribuut) {

    if (emp(zaakId)) {
      throw new ProException(ERROR, "ZaakId is leeg");
    }

    if (emp(attribuut)) {
      throw new ProException(ERROR, "Attribuut is leeg");
    }

    AttribuutHistorie historie = new AttribuutHistorie();
    ConditionalMap map = new ConditionalMap();
    ZaakIdentificatieService identificaties = getServices().getZaakIdentificatieService();
    map.putString(ZAAK_ID, identificaties.getIdentificatie(new ZaakKey(zaakId)).getZaakId());
    map.putString(ZAAK_ATTR, attribuut);

    if (ZaakAttribuutType.get(attribuut).is(FAVORIET)) {
      map.putLong(C_USR, getServices().getGebruiker().getCUsr());
    }

    historie.setAttributen(copyList(ZaakAttrDao.find(map), ZaakAttribuut.class));
    return historie;
  }

  public AttribuutHistorie getZaakAttribuutHistorie(Zaak zaak) {
    AttribuutHistorie historie = new AttribuutHistorie();
    ConditionalMap map = new ConditionalMap();
    map.putString(ZAAK_ID, zaak.getZaakId());
    historie.setAttributen(copyList(ZaakAttrDao.find(map), ZaakAttribuut.class));
    return historie;
  }

  public boolean isFavoriet(String zaakId) {
    ZaakIdentificatieService identificaties = getServices().getZaakIdentificatieService();
    return !ZaakAttrDao.find(new ConditionalMap()
        .putString(ZAAK_ID, identificaties.getIdentificatie(new ZaakKey(zaakId)).getZaakId())
        .putString(ZAAK_ATTR, FAVORIET.getCode())
        .putLong(C_USR, getServices().getGebruiker().getCUsr())).isEmpty();
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void setFavoriet(String zaakId, boolean favoriet) {
    if (favoriet) {
      ZaakAttribuut attribuut = new ZaakAttribuut();
      attribuut.setZaakId(zaakId);
      attribuut.setAttribuut(FAVORIET.getCode());
      attribuut.setCodeGebruiker(getServices().getGebruiker().getCUsr());
      save(attribuut);

    } else {
      ZaakIdentificatieService identificaties = getServices().getZaakIdentificatieService();
      ZaakAttrDao.find(new ConditionalMap()
          .putString(ZAAK_ID, identificaties.getIdentificatie(new ZaakKey(zaakId)).getZaakId())
          .putString(ZAAK_ATTR, FAVORIET.getCode())
          .putLong(C_USR, getServices().getGebruiker().getCUsr()))
          .forEach(this::removeEntity);
    }
  }

  public boolean isZakenBehandelenEnable() {
    return StringUtils.isNotBlank(Stream.of(
        ZAKEN_NIEUW_ZAAKTYPES,
        ZAKEN_NIEUW_BRONNEN,
        ZAKEN_NIEUW_LEVERANCIERS)
        .map(parm -> getServices().getParameterService().getParm(parm))
        .collect(Collectors.joining()));
  }

  public boolean isBehandelbareZaak(Zaak zaak) {
    if (isZakenBehandelenEnable()) {
      ZakenService zakenService = getServices().getZakenService();
      String parmBronnen = zakenService.getParm(ZAKEN_NIEUW_BRONNEN);
      String parmLeveranciers = zakenService.getParm(ZAKEN_NIEUW_LEVERANCIERS);
      boolean isMatchBron = emp(parmBronnen) || zaak.getBron().matches(parmBronnen);
      boolean isMatchLeveranciers = emp(parmLeveranciers) || zaak.getLeverancier().matches(parmLeveranciers);
      List<ZaakType> zaakTypes = ZaakType.getByCodes(zakenService.getParm(ZAKEN_NIEUW_ZAAKTYPES));
      return isMatchBron
          && isMatchLeveranciers
          && (zaakTypes.isEmpty() || zaakTypes.contains(zaak.getType()))
          && !zaak.getStatus().isEindStatus();
    }
    return false;
  }

  public ZaakBehandelaarHistorie getZaakBehandelaarHistorie(Zaak zaak) {
    ZaakBehandelaarHistorie historie = new ZaakBehandelaarHistorie();
    ConditionalMap map = new ConditionalMap();
    map.putString(ZAAK_ID, zaak.getZaakId());
    historie.setZaakBehandelaars(copyList(ZaakUsrDao.find(map), ZaakBehandelaar.class));
    return historie;
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(ZaakAttribuut zaakAttribuut) {
    saveEntity(zaakAttribuut);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void koppelenAlsBehandelaar(Zaak zaak) {
    ZaakBehandelaar zaakBehandelaar = new ZaakBehandelaar();
    zaakBehandelaar.setZaakId(zaak.getZaakId());
    Usr usr = UsrDao.find(Usr.class, getServices().getGebruiker().getCUsr());
    zaakBehandelaar.setUsr(usr);
    zaakBehandelaar.setUsrToek(usr);
    saveEntity(zaakBehandelaar);
    zaak.getZaakHistorie().setBehandelaarHistorie(getZaakBehandelaarHistorie(zaak));
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(ZaakBehandelaar zaakBehandelaar) {
    zaakBehandelaar.setUsr(UsrDao.find(Usr.class, getServices().getGebruiker().getCUsr()));
    saveEntity(zaakBehandelaar);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    removeEntities(getZaakAttribuutHistorie(zaak).getAttributen());
    removeEntities(getZaakBehandelaarHistorie(zaak).getBehandelaars());
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(ZaakAttribuut id) {
    removeEntity(id);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(ZaakBehandelaar id) {
    removeEntity(id);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(String zaakId, ZaakAttribuutType type) {
    removeEntities(getZaakAttributen(zaakId, type.getCode()).getAttributen());
  }
}
