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

package nl.procura.gbaws.db.handlers;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.interfaces.TableWrapper;

public class GenericDao implements Serializable {

  public static void removeAndCommit(List<Integer> codes, Class tableClass) {
    EntityManager em = GbaWsJpa.getManager();
    em.getTransaction().begin();
    remove(codes, tableClass);
    em.getTransaction().commit();
  }

  public static void removeAndCommit(List<? extends TableWrapper> entities) {

    EntityManager em = GbaWsJpa.getManager();
    em.getTransaction().begin();
    remove(entities);
    em.getTransaction().commit();
  }

  public static void remove(List<? extends TableWrapper> entities) {
    EntityManager em = GbaWsJpa.getManager();
    for (TableWrapper entity : entities) {
      em.remove(em.find(entity.getTable().getClass(), entity.getPk()));
    }
  }

  public static <T> T find(Class<T> clazz, Object primaryKey) {
    EntityManager em = GbaWsJpa.getManager();
    return em.find(clazz, primaryKey);
  }

  public static void remove(List<Integer> codes, Class tableClass) {
    EntityManager em = GbaWsJpa.getManager();
    for (int code : codes) {
      em.remove(em.find(tableClass, code));
    }
  }
}
