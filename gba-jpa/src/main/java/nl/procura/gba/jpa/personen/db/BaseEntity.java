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

package nl.procura.gba.jpa.personen.db;

import java.io.Serializable;

import javax.persistence.*;

import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;

@MappedSuperclass
public class BaseEntity<ID> implements Serializable {

  private static final long serialVersionUID = 1L;

  public static Long NULL    = null;
  public static Long EMPTY   = -1L;
  public static Long DEFAULT = 0L;

  public BaseEntity() {
    GbaDaoUtils.setLists(this);
  }

  public ID getId() {
    return (ID) GbaDaoUtils.getId(this);
  }

  public boolean isStored() {
    return getId() != null;
  }

  @Override
  public int hashCode() {

    final Object uniqueKey = getUniqueKey();
    final Object key = uniqueKey != null ? uniqueKey : getId();

    if (key == null) {
      return super.hashCode();
    }

    if (key instanceof Number) {
      final Number number = (Number) key;
      final int intValue = number.intValue();
      return intValue <= 0 ? super.hashCode() : intValue;
    }

    return 31 + key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof BaseEntity)) {
      return false;
    }

    final BaseEntity other = (BaseEntity) obj;
    final int key = hashCode();
    final int otherKey = other.hashCode();
    return key == otherKey;
  }

  @Override
  public String toString() {
    return getClass() + ": " + getId();
  }

  public ID getUniqueKey() {
    return null;
  }

  @PreRemove
  protected void preRemove() {
  }

  @PostRemove
  protected void postRemove() {
  }

  @PrePersist
  protected void prePersist() {
    preSave();
  }

  @PostPersist
  protected void postPersist() {
  }

  @PreUpdate
  protected void preUpdate() {
    preSave();
  }

  protected void preSave() {
    GbaDaoUtils.checkNull(this);
  }

  @PostUpdate
  protected void postUpdate() {
  }

  @PostLoad
  protected void postLoad() {
  }

  public void beforeSave() {
  }

  public void afterSave() {
  }
}
