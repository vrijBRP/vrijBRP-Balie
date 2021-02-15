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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Kassa;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class KassaDao extends GenericDao {

  public static final List<Kassa> findKassaProducten() {
    return createQuery("select obj from Kassa obj order by obj.cKassa", Kassa.class).getResultList();
  }

  public static final List<Kassa> findKassaByTypeAndDocument(BigDecimal type, long cDocument) {
    TypedQuery<Kassa> q = GbaJpa.getManager().createNamedQuery("Kassa.findByTypeAndDocument", Kassa.class);
    q.setParameter("type", type);
    q.setParameter("cDocument", cDocument);
    return q.getResultList();
  }

  public static List<Kassa> findKassaByTypeAndReisdocument(BigDecimal type, Long cReisdoc) {
    TypedQuery<Kassa> q = GbaJpa.getManager().createNamedQuery("Kassa.findByTypeAndReisdocument", Kassa.class);
    q.setParameter("type", type);
    q.setParameter("cReisdoc", cReisdoc);
    return q.getResultList();
  }

  public static List<Kassa> findKassaByTypeAndRijbewijs(BigDecimal type, Long cRijb) {
    TypedQuery<Kassa> q = GbaJpa.getManager().createNamedQuery("Kassa.findByTypeAndRijbewijs", Kassa.class);
    q.setParameter("type", type);
    q.setParameter("cRijb", cRijb);
    return q.getResultList();
  }

  public static List<Kassa> findKassaByTypeAndAnders(BigDecimal type, String anders, String productgroep) {
    TypedQuery<Kassa> q = GbaJpa.getManager().createNamedQuery("Kassa.findByTypeAndAnders", Kassa.class);
    q.setParameter("type", type);
    q.setParameter("anders", anders);
    q.setParameter("productgroep", productgroep);
    return q.getResultList();
  }

  public static List<Kassa> findKassaByType(BigDecimal type) {
    TypedQuery<Kassa> q = GbaJpa.getManager().createNamedQuery("Kassa.findByType", Kassa.class);
    q.setParameter("type", type);
    return q.getResultList();
  }
}
