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

package nl.procura.gba.jpa.personen.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

import nl.procura.gba.jpa.personen.db.VersionEntity;
import nl.procura.gba.jpa.personen.session.UniqueJpaList;
import nl.procura.java.reflection.ReflectionCache;
import nl.procura.java.reflection.ReflectionCache.ClassStorage;
import nl.procura.java.reflection.ReflectionCache.FieldStorage;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

public class GbaDaoUtils {

  /**
   * Converteert de entiteit naar een eventuele extensie van de entity
   */
  public static <T> T getEntity(T resultaat, Object o) {

    try {
      return ReflectionUtil.deepCopyBean(resultaat, o);
    } catch (Exception e) {
      throw new RuntimeException("Fout bij converteren entiteit: " + e.getClass(), e);
    }
  }

  /**
   * Converteert een eventuele extensie van de entiteit naar de database entiteit.
   */
  public static Object getEntity(Object o) {
    return ReflectionUtil.deepCopyBean(getEntityClass(o.getClass(), false).getClazz(), o);
  }

  /**
   * Geeft entity class terug
   */
  public static ClassStorage getEntityClass(Class<?> cl, boolean withId) {

    if (cl == Object.class) {
      throw new RuntimeException("Class " + cl + " is geen database entiteit.");
    }

    ClassStorage classStorage = ReflectionCache.get(cl);
    if (classStorage.is(Entity.class)) {
      if (withId) {
        for (FieldStorage fs : classStorage.getFields()) {
          if (fs.isOr(Id.class, EmbeddedId.class)) {
            return classStorage;
          }
        }
      } else {
        return classStorage;
      }
    }

    return getEntityClass(cl.getSuperclass(), withId);
  }

  /**
   * Zet sleutel
   */
  public static void setId(Object o, Long id) {
    for (FieldStorage fs : getEntityClass(o.getClass(), true).getFields()) {
      if (fs.isAnd(Id.class, Column.class)) {
        try {
          fs.getField().set(o, id);
        } catch (Exception e) {
          throw new RuntimeException(
              "Kan geen nieuwe sleutel vinden voor tabel: " + o.getClass().getSimpleName());
        }
      }
    }
  }

  public static boolean isNumberId(Object o) {
    for (FieldStorage fs : getEntityClass(o.getClass(), true).getFields()) {
      if (fs.isOr(Id.class)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Geef sleutel terug
   */
  public static Object getId(Object o) {

    for (FieldStorage fs : getEntityClass(o.getClass(), true).getFields()) {
      if (fs.isOr(Id.class, EmbeddedId.class)) {
        try {
          return fs.getField().get(o);
        } catch (Exception e) {
          throw new RuntimeException(
              "Kan geen nieuwe sleutel vinden voor tabel: " + o.getClass().getSimpleName());
        }
      }
    }

    throw new RuntimeException("Kan geen sleutel vinden voor tabel: " + o.getClass().getSimpleName());
  }

  /**
   * Alle Strings en BigDecimals op default zetten.
   */
  public static void checkNull(Object object) {

    try {
      for (FieldStorage fs : getEntityClass(object.getClass(), false).getFields()) {

        Object returnV = fs.getField().get(object);
        Type returnType = fs.getField().getGenericType();

        if (returnV == null) {
          if (returnType == BigDecimal.class) {
            fs.getField().set(object, BigDecimal.valueOf(-1L));
          } else if (returnType == String.class) {
            fs.getField().set(object, "");
          } else if (returnType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) returnType;
            if (pt.getRawType() == List.class) {
              fs.getField().set(object, new UniqueJpaList());
            }
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Fout bij aanroep van methode.", e);
    }
  }

  /**
   * Alle Strings en BigDecimals op default zetten.
   */
  public static void setLists(Object object) {

    try {
      for (FieldStorage fs : getEntityClass(object.getClass(), false).getFields()) {

        Object returnV = fs.getField().get(object);
        Type returnType = fs.getField().getGenericType();

        if (returnV == null && returnType instanceof ParameterizedType) {
          ParameterizedType pt = (ParameterizedType) returnType;
          if (pt.getRawType() == List.class) {
            fs.getField().set(object, new UniqueJpaList());
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Fout bij aanroep van methode.", e);
    }
  }

  /**
   * Check if the version timestamp (version_ts) is not newer than the version in
   * this instance.
   */
  public static void checkVersion(EntityManager em, Object object) {
    if (object instanceof VersionEntity) {
      VersionEntity versionEntity = (VersionEntity) object;
      Validate.notBlank(versionEntity.getZaakId());
      Long instanceVersionTs = versionEntity.getVersionTs();
      String tableName = getEntityClass(object.getClass(), false).getClazz().getName();
      String query = "select t.versionTs from " + tableName + " t where t.zaakId = :zaakId";
      TypedQuery<Long> tq = em.createQuery(query, Long.class);
      tq.setParameter("zaakId", versionEntity.getZaakId());
      List<Long> results = tq.getResultList();
      if (!results.isEmpty() && instanceVersionTs != null) {
        Long dbVersion = results.get(0);
        if (instanceVersionTs < dbVersion) {
          String message = "Deze zaak is door een ander proces of gebruiker gewijzigd. <br>Verlaat de zaak en controleer de status van de zaak.";
          throw new ProException(ProExceptionSeverity.WARNING, message);
        }
      }
      versionEntity.setVersionTs(System.currentTimeMillis());
    }
  }
}
