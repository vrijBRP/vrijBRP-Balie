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

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.gba.jpa.personen.utils.GbaDaoUtils.getEntity;
import static nl.procura.gba.jpa.personen.utils.GbaDaoUtils.getId;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class GenericDao {

  public static final String  ANR                 = "anr";
  public static final String  BSN                 = "bsn";
  public static final String  ZAAK_ID             = "zaakId";
  public static final String  MAX_CORRECT_RESULTS = "maxCorrectResults";
  public static final String  MAX_RESULTS         = "maxResults";
  private static final Logger LOGGER              = LoggerFactory.getLogger(GenericDao.class);

  /**
   * Ververs de entiteit
   */
  public static final <T> T refreshEntity(T o) {
    GbaJpa.getManager().refresh(o);
    return o;
  }

  /**
   * Ververs de entiteiten in de collectie
   */
  public static <T> T refreshEntities(T collection) {

    if (!(collection instanceof Collection)) {
      throw new IllegalArgumentException("Object " + collection.getClass() + " is niet van het type Collection.");
    }

    for (Object o : (Collection) collection) {
      refreshEntity(o);
    }

    return collection;
  }

  /**
   * Sla de entiteit op
   */
  public static final <T> T saveEntity(T o) {

    EntityManager em = GbaJpa.getManager();

    GbaDaoUtils.checkVersion(em, o);

    // Executing beforeSave methode
    if (o instanceof BaseEntity) {
      ((BaseEntity) o).beforeSave();
    }

    Object dbO = getEntity(checkId(o));

    dbO = em.merge(dbO);

    LOGGER.debug("Entiteit opgeslagen: " + dbO);

    getEntity(o, dbO);

    // Executing beforeSave methode
    if (o instanceof BaseEntity) {
      ((BaseEntity) o).afterSave();
    }

    return o;
  }

  /**
   * Verwijder de entiteit
   */
  public static final void removeEntity(Object o) {

    if (GbaDaoUtils.getId(o) == null) {
      return;
    }

    EntityManager em = GbaJpa.getManager();
    Object fo = find(getEntity(o));

    if (fo != null) {
      em.remove(fo);
      LOGGER.debug("Entiteit verwijderd: " + o);
    }
  }

  /**
   * Zoek alle op basis van een voorbeeld
   */
  public static final <T> List<T> findByExample(T example) {

    EntityManager em = GbaJpa.getManager();
    if (em instanceof EntityManagerImpl) {
      return ((EntityManagerImpl) em).createQueryByExample(example).getResultList();
    }

    return new ArrayList<>();
  }

  /**
   * Zoek de eerste op basis van een voorbeeld
   */
  public static final <T> T findFirstByExample(T example) {

    EntityManager em = GbaJpa.getManager();
    if (em instanceof EntityManagerImpl) {
      Query queryByExample = ((EntityManagerImpl) em).createQueryByExample(example);
      queryByExample.setMaxResults(1);
      List results = queryByExample.getResultList();
      return (T) (results.isEmpty() ? null : results.get(0));
    }

    return null;
  }

  /**
   * Is dit object al opgeslagen
   */
  public static final boolean isSaved(Object object) {
    Object entity = GbaDaoUtils.getEntity(object);
    Object id = GbaDaoUtils.getId(object);
    return (id != null) && find(entity.getClass(), id) != null;
  }

  /**
   * Zoek op basis van een SQL statement
   */
  public static Query createQuery(String criteriaQuery) {
    return GbaJpa.getManager().createQuery(criteriaQuery);
  }

  /**
   * Zoek op basis van een query en resultaat klasse
   */
  public static <T> TypedQuery<T> createQuery(String criteriaQuery, Class<T> resultClass) {
    return GbaJpa.getManager().createQuery(criteriaQuery, resultClass);
  }

  /**
   * zoek op basis van een klasse en sleutel
   */
  public static final <T> T find(Class<T> entityClass, Object primaryKey) {
    if (primaryKey != null) {
      return GbaJpa.getManager().find(entityClass, primaryKey);
    }
    return null;
  }

  /**
   * Als de key 0 is dan null van maken. Extra veiligheidsfeature
   */
  private static <T> T checkId(T o) {
    Object id = GbaDaoUtils.getId(o);
    if (id != null && (id.equals(0) || id.equals(0L))) {
      LOGGER.error("Object " + o + " of class " + o.getClass() + " has id 0.");
      GbaDaoUtils.setId(o, null);
    }

    return o;
  }

  /**
   * Zoek op basis van een voorbeeld
   */
  private static <T> T find(T example) {
    EntityManager em = GbaJpa.getManager();
    Object id = getId(example);
    if (id != null) {
      return (T) em.find(example.getClass(), id);
    }
    return null;
  }

  /**
   * Controleer of het aantal resultaat overeenkomt met het gewenste max. aantal
   */
  protected static <T> List<T> get(TypedQuery query, ConditionalMap map) {

    int maxAantal = aval(map.get(GenericDao.MAX_RESULTS));

    if (maxAantal > 0) {
      query.setMaxResults(maxAantal);
    }

    List resultList = query.getResultList();
    long maxCorrectAantal = along(map.get(GenericDao.MAX_CORRECT_RESULTS));
    if (maxCorrectAantal >= 0 && resultList.size() > maxCorrectAantal) {
      String maxMsg = maxCorrectAantal == 1 ? "1 record" : maxCorrectAantal + " records";
      throw new RuntimeException("Deze actie zou betrekking moeten hebben op maximaal " +
          maxMsg + ". Er voldoen echter " + resultList.size()
          + " record(s) aan de zoekopdracht. De actie wordt afgebroken");
    }

    return resultList;
  }

  protected static void ge(List<Predicate> where, CriteriaBuilder builder, ConditionalMap map, Path path,
      String... mapIds) {
    for (String mapId : mapIds) {
      if (map.containsKey(mapId)) {
        where.add(builder.and(builder.ge(path, along(map.get(mapId)))));
      }
    }
  }

  protected static void le(List<Predicate> where, CriteriaBuilder builder, ConditionalMap map, Path path,
      String... mapIds) {
    for (String mapId : mapIds) {
      if (map.containsKey(mapId)) {
        where.add(builder.and(builder.le(path, along(map.get(mapId)))));
      }
    }
  }
}
