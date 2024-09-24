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

import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.TaskEntity;

public class TaskDao extends GenericDao {

  public static List<TaskEntity> getByUsrAndStatus(Long cUsr, Integer status) {
    TypedQuery<TaskEntity> query = createQuery(
        "select v from TaskEntity v "
            + "where v.cUsr = :cUsr and v.status = :status "
            + "order by v.cTask asc",
        TaskEntity.class);
    query.setParameter("cUsr", cUsr);
    query.setParameter("status", status);
    return query.getResultList();
  }

  public static List<TaskEntity> getByZaakId(String zaakId) {
    TypedQuery<TaskEntity> query = createQuery(
        "select v from TaskEntity v "
            + "where v.zaakId = :zaakId "
            + "order by v.cTask asc",
        TaskEntity.class);
    query.setParameter("zaakId", zaakId);
    return query.getResultList();
  }
}
