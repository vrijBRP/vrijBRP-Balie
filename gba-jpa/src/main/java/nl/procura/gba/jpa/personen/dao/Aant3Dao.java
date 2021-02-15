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

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.getSystemDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Aant3;
import nl.procura.gba.jpa.personen.db.Aant3PK;
import nl.procura.gba.jpa.personen.db.Aantek3;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class Aant3Dao extends GenericDao {

  public static final List<Aant3> findAant(long anr, long bsn) {
    return findAant(anr, bsn, null);
  }

  @SuppressWarnings("unchecked")
  public static final List<Aant3> findAant(long anr, long bsn, Aantek3 aantek3) {

    StringBuilder sql = new StringBuilder();
    sql.append("select a from Aant3 a ");

    if (anr > 0 && bsn > 0) {
      sql.append("where (a.id.bsn = :bsn or a.id.anr = :anr) ");
    } else if (bsn > 0) {
      sql.append("where a.id.bsn = :bsn ");
    } else if (anr > 0) {
      sql.append("where a.id.anr = :anr ");
    } else {
      return new ArrayList();
    }

    if (aantek3 != null) {
      sql.append("and a.aantek3 = :aantek3 ");
    }

    sql.append("order by a.dIn desc");
    EntityManager em = GbaJpa.getManager();
    TypedQuery<Aant3> q = em.createQuery(sql.toString(), Aant3.class);

    if (anr > 0) {
      q.setParameter("anr", anr);
    }

    if (bsn > 0) {
      q.setParameter("bsn", bsn);
    }

    if (aantek3 != null) {
      q.setParameter("aantek3", aantek3);
    }

    Set<String> resultTypes = new HashSet();
    List<Aant3> results = new ArrayList();
    for (Aant3 result : q.getResultList()) {
      String type = result.getAantek3().getAantek3();
      if (!resultTypes.contains(type)) {
        resultTypes.add(type);
        results.add(result);
      }
    }

    return results;
  }

  public static final void saveAant(long anr, long bsn, String aantek3, String value, long country) {

    Aantek3 aantType = findAantType(aantek3);
    Aant3PK id = getAant(anr, bsn, aantType).getId();
    id.setCAantek3(aantType.getCAantek3());

    Aant3 a = new Aant3();
    a.setId(id);
    a.setDIn(BigDecimal.valueOf(along(getSystemDate())));
    a.setAant(value);
    a.setAantek3(aantType);
    a.setCountry(BigDecimal.valueOf(country));

    saveEntity(a);
  }

  private static Aant3 getAant(long anr, long bsn, Aantek3 aantek3) {
    for (Aant3 aant3 : findAant(anr, bsn, aantek3)) {
      return aant3;
    }

    Aant3 aant3 = new Aant3();
    aant3.setId(new Aant3PK());
    aant3.getId().setBsn(bsn);
    aant3.getId().setAnr(anr);

    return aant3;
  }

  private static Aantek3 findAantType(String aantek3) {

    Aantek3 example = new Aantek3();
    example.setAantek3(aantek3);

    for (Aantek3 aant : findByExample(example)) {
      return aant;
    }

    example.setOms("-- geen omschrijving --");

    return saveEntity(example);
  }
}
