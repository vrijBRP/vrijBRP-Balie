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

package nl.procura.gba.web.services.zaken.algemeen;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.ZaakType.ONBEKEND;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.PROGRAMMING;
import static org.apache.commons.collections.CollectionUtils.containsAny;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.gba.common.MaxList;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.standard.exceptions.ProException;

public class ZakenService extends AbstractService {

  public ZakenService() {
    super("Zaken");
  }

  /**
   * Geeft aantal zaken terug op basis van zaakargumenten
   */
  @Timer
  public int getAantalZaken(ZaakArgumenten zaakArgumenten) {
    getAlternativeZaakIds(zaakArgumenten);

    return getServices(zaakArgumenten.getTypen())
        .stream()
        .mapToInt(db -> db.getZakenCount(zaakArgumenten))
        .sum();
  }

  /**
   * Geeft zaken terug op basis van zaakargumenten
   */
  @Timer
  public List<Zaak> getMinimaleZaken(ZaakArgumenten zaakArgumenten) {

    MaxList<Zaak> totaalLijst = new MaxList<>(zaakArgumenten.getMax());

    getAlternativeZaakIds(zaakArgumenten);

    for (ZaakService db : getServices(getZaakTypen(zaakArgumenten))) {

      // Zaakargumenten met alleen de types van de service
      ZaakArgumenten zaakArgumentenByType = getZaakargumentenByType(zaakArgumenten, db.getZaakType());

      // Zoek de minimale zaken
      totaalLijst.addAll(new MaxList(db.getMinimalZaken(zaakArgumentenByType), zaakArgumenten.getMax()));

      if (totaalLijst.isMaxBereikt()) {
        break;
      }
    }

    totaalLijst.sort(ZaakSorterBuilder.getZaakSorter(zaakArgumenten.getSortering()));
    return totaalLijst;
  }

  /**
   * Geeft zaakservice op basis van een zaak
   */
  public ZaakService<Zaak> getService(Zaak zaak) {
    return getServices(zaak.getType()).get(0);
  }

  /**
   * Geeft services terug op basis van de types
   */
  public List<ZaakService> getServices(Collection<ZaakType> zaakTypes) {

    List<ZaakService> services = new ArrayList<>();

    for (ZaakService zdb : getServices().getServices(ZaakService.class)) {
      // Alleen service selecteren die betrekking heeft op het zaakType
      if (isEmpty(zaakTypes) || containsAny(zaakTypes, asList(zdb.getZaakType(), ONBEKEND))) {
        services.add(zdb);
      }
    }

    if (services.size() == 0) {
      throw new ProException(PROGRAMMING, ERROR, "Geen service gevonden");
    }

    return services;
  }

  /**
   * Geeft services terug op basis van de types
   */
  public List<ZaakService> getServices(ZaakType... zaakTypes) {
    return getServices(zaakTypes == null ? new ArrayList<>() : asList(zaakTypes));
  }

  @Timer
  public <T extends Zaak> List<T> getStandaardZaken(ZaakArgumenten zaakArgumenten) {
    return (List<T>) getStandaardZaken(getMinimaleZaken(zaakArgumenten));
  }

  /**
   * Laad alleen de databasegegevens van de zaak
   */
  @Timer
  public <T extends Zaak> List<T> getStandaardZaken(List<T> zaken) {
    zaken.stream().filter(DatabaseTable::isStored).forEach(zaak -> getService(zaak).getStandardZaak(zaak));
    return zaken;
  }

  /**
   * Laad de zaak in volledig (inclusief afgeleide gegevens)
   */
  public <T extends Zaak> T getVolledigeZaak(T zaak) {
    return (zaak != null) ? (zaak.isStored() ? (T) getService(zaak).getCompleteZaak(zaak) : zaak) : null;
  }

  @Timer
  public <T extends Zaak> List<T> getVolledigeZaken(ZaakArgumenten zaakArgumenten) {
    return (List<T>) getVolledigeZaken(getMinimaleZaken(zaakArgumenten));
  }

  /**
   * Geeft de volledige zaak terug
   */
  @Timer
  public <T extends Zaak> List<T> getVolledigeZaken(List<T> zaken) {

    for (T zaak : zaken) {
      if (zaak.isStored()) {
        getService(zaak).getCompleteZaak(zaak);
      }
    }

    return zaken;
  }

  /**
   * Geeft zaken terug op basis van zaakargumenten
   */
  @Timer(newLine = true)
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {

    MaxList<ZaakKey> totaalLijst = new MaxList<>(zaakArgumenten.getMax());

    // Zoek de andere zaakIds erbij
    getAlternativeZaakIds(zaakArgumenten);

    // Zoek in de desbetreffende services
    Set<ZaakType> zaakTypen = getZaakTypen(zaakArgumenten);
    for (ZaakService db : getServices(zaakTypen)) {
      totaalLijst.addAll(new MaxList(db.getZaakKeys(zaakArgumenten), zaakArgumenten.getMax()));
      if (totaalLijst.isMaxBereikt()) {
        break;
      }
    }

    totaalLijst.sort(ZaakSorterBuilder.getZaakKeySorter(zaakArgumenten.getSortering()));

    return totaalLijst;
  }

  /**
   * Verwijder de zaak
   */
  public void delete(ZaakKey zaakKey) {
    for (Zaak zaak : getMinimaleZaken(new ZaakArgumenten(zaakKey))) {
      getService(zaak).delete(zaak);
    }
  }

  /**
   * Zoek alternatieve zaakidentificaties
   */
  private void getAlternativeZaakIds(ZaakArgumenten zaakArgumenten) {
    Set<ZaakKey> zaakKeys = zaakArgumenten.getZaakKeys();
    Set<ZaakKey> altZaakKey = getServices().getZaakIdentificatieService().getIdentificatie(zaakKeys);
    altZaakKey.forEach(zk -> zaakArgumenten.addZaakKey(zk));
  }

  /**
   * filtert de zaakKeys die niet horen bij een bepaald type
   */
  private ZaakArgumenten getZaakargumentenByType(ZaakArgumenten zaakArgumenten, ZaakType zaakType) {
    ZaakArgumenten za = new ZaakArgumenten(zaakArgumenten);
    za.getZaakKeys().clear();
    for (ZaakKey zk : zaakArgumenten.getZaakKeys()) {
      if (zk.getZaakType().is(zaakType, ONBEKEND)) {
        za.addZaakKey(zk);
      }
    }
    return za;
  }

  /**
   * Een set met zaakTypes of basis van de ZaakKeys
   * Op deze manier wordt er alleen gezocht in de services die nodig zijn.
   */
  private Set<ZaakType> getZaakTypen(ZaakArgumenten zaakArgumenten) {
    Set<ZaakType> typen = new HashSet<>();
    if (zaakArgumenten.getTypen().isEmpty()) {
      for (ZaakKey zaakKey : zaakArgumenten.getZaakKeys()) {
        typen.add(zaakKey.getZaakType());
      }
    } else {
      typen.addAll(zaakArgumenten.getTypen());
    }

    return typen;
  }
}
