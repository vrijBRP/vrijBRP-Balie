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

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.jpa.personen.db.RdmAmpDoc;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class RdmAmpDao extends GenericDao {

  public static List<RdmAmp> findNieuwe() {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.bezorgingGewenst = true "
        + "and a.indVoormelding = false "
        + "and a.dEnd <= 0 ";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    return query.getResultList()
        .stream()
        .sorted((o1, o2) -> o2.getHoofdorder().compareTo(o1.getHoofdorder()))
        .collect(toList());
  }

  public static boolean isIngehoudenDezeAanvraag(RdmAmp rdmAmp, String docNr) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmpDoc a where a.rdmAmp = :rdmAmp and a.docNr = :docNr";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    query.setParameter("rdmAmp", rdmAmp);
    query.setParameter("docNr", docNr);
    return query.getResultList().stream().findFirst().isPresent();
  }

  public static boolean isIngehoudenAndereAanvraag(RdmAmp rdmAmp, String docNr) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmpDoc a where a.docNr = :docNr";
    TypedQuery<RdmAmpDoc> query = em.createQuery(sql, RdmAmpDoc.class);
    query.setParameter("docNr", docNr);
    RdmAmpDoc foundRdmAmpDoc = query.getResultList().stream().findFirst().orElse(null);
    return foundRdmAmpDoc != null && !foundRdmAmpDoc.getRdmAmp().equals(rdmAmp);
  }

  public static List<RdmAmp> findNietIngeklaarde() {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.indVoormelding = true "
        + "  and a.orderRefNr   != '' "
        + "  and a.indKoppeling  = true "
        + "  and a.indInklaring  = false "
        + "  and a.indBlokkering = false "
        + "  and a.indAnnulering = false "
        + "  and a.indUitreiking = false "
        + "  and a.dEnd        <= 0 ";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    return query.getResultList();
  }

  public static List<RdmAmp> findGekoppelde() {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.indVoormelding = true "
        + "  and a.orderRefNr   != '' "
        + "  and a.indKoppeling  = true "
        + "  and a.indBlokkering = false "
        + "  and a.indAnnulering = false "
        + "  and a.indUitreiking = false "
        + "  and a.dEnd        <= 0 ";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    return query.getResultList();
  }

  public static List<RdmAmp> findUitgereikte() {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.dEnd <= 0 "
        + "and a.indUitreiking = true";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    return query.getResultList();
  }

  public static Optional<RdmAmp> findByAanvrNr(String aanvrNr) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.aanvrNr = :aanvr_nr";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    query.setParameter("aanvr_nr", aanvrNr);
    return query.getResultList().stream().findFirst();
  }

  public static List<RdmAmp> findByOrderRefNr(String orderRefNr) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a where a.orderRefNr = :order_ref_nr";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    query.setParameter("order_ref_nr", orderRefNr);
    return query.getResultList();
  }

  public static List<RdmAmp> findOtherByRefNr(RdmAmp melding) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.bundelRefNr = :ref_nr "
        + "and a.bezorgingGewenst = true "
        + "and a.aanvrNr <> :aanvr_nr";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    query.setParameter("ref_nr", melding.getBundelRefNr());
    query.setParameter("aanvr_nr", melding.getAanvrNr());
    return query.getResultList();
  }

  public static List<RdmAmp> findOtherByAddress(RdmAmp melding) {
    EntityManager em = GbaJpa.getManager();
    String sql = "select a from RdmAmp a "
        + "where a.dIn = :d_in "
        + "and a.hnr = :hnr "
        + "and a.hnrL = :hnr_l "
        + "and a.hnrT = :hnr_t "
        + "and a.pc = :pc "
        + "and a.bezorgingGewenst = true "
        + "and a.aanvrNr <> :aanvr_nr";
    TypedQuery<RdmAmp> query = em.createQuery(sql, RdmAmp.class);
    query.setParameter("d_in", melding.getDIn());
    query.setParameter("hnr", melding.getHnr());
    query.setParameter("hnr_l", melding.getHnrL());
    query.setParameter("hnr_t", melding.getHnrT());
    query.setParameter("pc", melding.getPc());
    query.setParameter("aanvr_nr", melding.getAanvrNr());
    return query.getResultList();
  }
}
