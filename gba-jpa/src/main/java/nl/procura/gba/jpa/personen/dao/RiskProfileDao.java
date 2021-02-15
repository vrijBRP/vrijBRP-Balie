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

import nl.procura.gba.jpa.personen.db.RiskProfile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RiskProfileDao extends GenericDao {

  public static List<RiskProfile> findByName(String name) {
    TypedQuery<RiskProfile> query = createQuery("select s from RiskProfile s where s.name = :name", RiskProfile.class);
    query.setParameter("name", name);
    return query.getResultList();
  }
}
