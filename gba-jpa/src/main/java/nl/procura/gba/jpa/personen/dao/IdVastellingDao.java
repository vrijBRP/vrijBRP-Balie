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
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Idvaststelling;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class IdVastellingDao extends GenericDao {

  private static final String USR   = "usr";
  private static final String D_IN  = "dIn";
  private static final String T_IN  = "tIn";

  /**
   * geeft een vaststelling terug
   */
  public static Idvaststelling getVaststelling(long bsn, long cUsr, long dIn, long tIn) {

    EntityManager em = GbaJpa.getManager();

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Idvaststelling> query = builder.createQuery(Idvaststelling.class);
    Root<Idvaststelling> table = query.from(Idvaststelling.class);

    List<Predicate> where = new ArrayList<>();

    where.add(builder.equal(table.<BigDecimal>get(BSN), bsn));
    where.add(builder.equal(table.<BigDecimal>get(D_IN), dIn));

    if (tIn <= 0) {
      where.add(builder.ge(table.<BigDecimal>get(T_IN), toBigDecimal(-1L)));
    }

    if (pos(cUsr)) {
      where.add(builder.equal(table.get(USR).get(extracted()), cUsr));
    }

    query.where(where.toArray(new Predicate[0]));
    query.orderBy(builder.desc(table.<BigDecimal>get(T_IN)));

    for (Idvaststelling i : em.createQuery(query).getResultList()) {
      long idTime = along(i.getTIn());
      if (idTime <= 0 || idTime <= tIn) { // Geen tijd in zaak of wel tijd in zaak
        return i;
      }
    }

    return null;
  }

  private static String extracted() {
    return "cUsr";
  }

  /**
   * Voegt een vaststelling toe
   */
  public static void addVaststelling(long bsn, long cUsr, String soort,
      String documentnr, boolean verificatie, String verificatieOms) {

    DateTime dt = new DateTime();
    Idvaststelling id = new Idvaststelling();
    id.setUsr(find(Usr.class, cUsr));
    id.setBsn(BigDecimal.valueOf(bsn));
    id.setDIn(BigDecimal.valueOf(along(dt.getLongDate())));
    id.setTIn(BigDecimal.valueOf(along(dt.getLongTime())));
    id.setSoort(soort);
    id.setDocumentnr(documentnr);
    id.setVerificatie(BigDecimal.valueOf(verificatie ? 1 : 0));
    id.setSVerificatie(verificatieOms);

    saveEntity(id);
  }
}
