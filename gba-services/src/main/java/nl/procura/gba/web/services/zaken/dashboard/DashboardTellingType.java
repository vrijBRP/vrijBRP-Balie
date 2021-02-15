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

package nl.procura.gba.web.services.zaken.dashboard;

import java.util.ArrayList;
import java.util.List;

import ch.lambdaj.group.Group;

public class DashboardTellingType<K, T> {

  private K                          key       = null;
  private String                     oms       = "";
  private List<T>                    zaken     = new ArrayList<>();
  private List<DashboardTellingType> tellingen = new ArrayList<>();

  public DashboardTellingType() {
  }

  public DashboardTellingType(K key) {
    this(key, key.toString(), new ArrayList<>());
  }

  public DashboardTellingType(K key, Group group) {
    this(key, group.findAll());
  }

  public DashboardTellingType(K key, List<T> zaken) {
    this(key, key.toString(), zaken);
  }

  public DashboardTellingType(K key, String oms, Group group) {
    this(key, oms, group.findAll());
  }

  public DashboardTellingType(K key, String oms, List<T> zaken) {
    this();
    this.key = key;
    this.oms = oms;
    this.zaken = zaken;
  }

  public DashboardTellingType addTelling(Object key) {
    DashboardTellingType type = new DashboardTellingType(key);
    tellingen.add(type);
    return type;
  }

  public void addZaken(List<T> zaken) {
    this.zaken.addAll(zaken);
  }

  public int getAantal() {
    return zaken.size();
  }

  public K getKey() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public DashboardTellingType getTelling(Object key) {
    for (DashboardTellingType telling : tellingen) {
      if (key.equals(telling.getKey())) {
        return telling;
      }
    }

    return addTelling(key);
  }

  public List<DashboardTellingType> getTellingen() {
    return tellingen;
  }

  public void setTellingen(List<DashboardTellingType> tellingen) {
    this.tellingen = tellingen;
  }

  public List<T> getZaken() {
    return zaken;
  }

  public void setZaken(List<T> zaken) {
    this.zaken = zaken;
  }
}
