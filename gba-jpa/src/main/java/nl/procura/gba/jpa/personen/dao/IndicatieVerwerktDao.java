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

import nl.procura.gba.jpa.personen.db.IndVerwerkt;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class IndicatieVerwerktDao extends ZaakDao {

  public static final String D_IN = "dIn";
  public static final String T_IN = "tIn";

  public static final List<IndVerwerkt> getByZaakId(String zaakId) {

    TypedQuery<IndVerwerkt> q = GbaJpa.getManager().createNamedQuery("IndVerwerkt.findByZaakId", IndVerwerkt.class);
    q.setParameter("zaakid", zaakId);

    return q.getResultList();
  }

  public static final boolean isZaakId(String zaakId, long indVerwerkt) {

    TypedQuery<IndVerwerkt> q = GbaJpa.getManager().createNamedQuery("IndVerwerkt.findByZaakId", IndVerwerkt.class);
    q.setParameter("zaakid", zaakId);
    q.setMaxResults(1);

    for (IndVerwerkt ind : q.getResultList()) {
      return (ind.getIndVerwerkt() == indVerwerkt);
    }

    return false;
  }
}
