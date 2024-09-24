/*
 * Copyright 2024 - 2025 Procura B.V.
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

import static nl.procura.gba.common.ZaakType.INBOX;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.Inbox;
import nl.procura.gba.jpa.personen.db.InboxBestand;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class InboxDao extends ZaakDao {

  public static final String C_INBOX        = "cInbox";
  public static final String D_INVOER       = "dInvoer";
  public static final String T_INVOER       = "tInvoer";
  public static final String ZAAK_ID_EXTERN = "zaakIdExtern";

  public static List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<Inbox, ZaakKey> w = new CriteriaWrapper<>(Inbox.class, ZaakKey.class, map);

    Path<Object> code = w.getTable().get(C_INBOX);
    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dIngang = w.getTable().get(D_INVOER);
    Path<Object> dInvoer = w.getTable().get(D_INVOER);
    Path<Object> tInvoer = w.getTable().get(T_INVOER);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, code, zaakId, dIngang, dInvoer, tInvoer)).orderBy(
        getOrder(w, D_INVOER, T_INVOER, C_INBOX));

    find(w);

    return addZaakType(w.getResultList(), INBOX);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<Inbox, Long> w = new CriteriaWrapper<>(Inbox.class, Long.class, map);

    w.getCq().select(w.getBuilder().count(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static List<Inbox> find(ConditionalMap map) {

    CriteriaWrapper<Inbox, Inbox> w = new CriteriaWrapper<>(Inbox.class, Inbox.class, map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_INVOER, T_INVOER, C_INBOX));

    TypedQuery<Inbox> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<Inbox, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<Inbox> table = w.getTable();
    CriteriaQuery<?> query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(C_INBOX), 0));

    if (map.containsKey(ZAAK_ID)) {
      Expression<Boolean> predicate1 = getZaakIds(builder, table, map);
      Expression<Boolean> predicate2 = getZaakIds(builder, table, map, ZAAK_ID_EXTERN);
      where.add(builder.and(builder.or(predicate1, predicate2)));
    }

    if (map.containsKey(IND_VERWERKT)) {
      where.add(getStatussen(table, map));
    }

    if (map.containsKey(NIET_IND_VERWERKT)) {
      where.add(builder.not(getNegeerStatussen(table, map)));
    }

    ge(where, builder, map, table.<Long> get(D_INVOER), D_INVOER_VANAF, D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_INVOER), D_INVOER_TM, D_INGANG_TM);

    // Niet mogelijk voor deze zaak
    if (map.containsKeys(ANR, BSN, D_END_TERMIJN, D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(builder.lessThan(table.get(C_INBOX), 0));
    }

    getAttributes(USR, query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  public static InboxBestand findInboxBestandByInboxCode(long cInbox) {
    EntityManager em = GbaJpa.getManager();
    TypedQuery<InboxBestand> query = em.createNamedQuery("InboxBestand.findByInboxId", InboxBestand.class);
    query.setParameter("c_inbox", cInbox);
    for (InboxBestand result : query.getResultList()) {
      return result;
    }

    return null;
  }
}
