/*
 * Copyright 2022 - 2023 Procura B.V.
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

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class DossNatDao extends GenericDao {

  /**
   * Returns the maximum dossier number.
   * Remove the non-digits in the order to sort numerically
   */
  public static String getMaxDossierNummer(long gemeenteCode, long yearDigit) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select dossiernr from doss_natur where dossiernr like ?1 "
        + "order by regexp_replace (dossiernr, '\\D', '', 'g')::int desc";
    List<?> resultList = em.createNativeQuery(sql)
        .setParameter(1, "%" + gemeenteCode + "." + yearDigit + "%")
        .setMaxResults(1)
        .getResultList();
    return resultList.stream()
        .findFirst()
        .map(Object::toString)
        .orElse(null);
  }
}
