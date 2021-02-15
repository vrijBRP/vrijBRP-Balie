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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.gba.common.ZaakType.*;

import java.util.List;

import javax.persistence.EntityManager;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class UnOfficialDashboardDao extends DashboardDao {

  // Toegekende gegevensverstrekking
  private static final int GV_TK_JA = 501;
  // Niet toegekende verstrekkking
  private static final int GV_TK_NEE = 502;
  // Belangenafweging
  private static final int GV_TK_BELANG = 503;

  //Overheidsorgaan, art. 3.5
  private static final int GRONDSLAG_3_5 = 101;
  //(Landelijk aangewezen) derde, art. 3.6
  private static final int GRONDSLAG_3_6 = 102;
  //(Gemeentelijk aangewezen) derde, art. 3.9
  private static final int GRONDSLAG_3_9 = 103;

  private static final int NIET_VERSTREKKEN = 202;
  private static final int NU_VERSTREKKEN   = 204;

  private static int RES_ONDERZ_IMMIGRATIE    = 2; //Vanuit het buitenland gevestigd in de gemeente
  private static int RES_ONDERZ_VERHUISD      = 3; //Verhuisd binnen de gemeente
  private static int RES_ONDERZ_EMIGRATIE     = 5; //Vertrokken naar het buitenland
  private static int RES_ONDERZ_EMIGRATIE_ONB = 6; //Vertrokken met onbekende bestemming

  /**
   * Gegevensverstrekking (totaal aantal aanvragen)
   */
  public static List<ZaakKey> getGv121(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal verstrekkingen totaal
   */
  public static List<ZaakKey> getGv122(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and (c_toek in"));
    sql.append(add(in(GV_TK_JA)));
    sql.append(add("or c_gv in (select c_gv from gv_proces where"));
    sql.append(add("c_procesactie in"));
    sql.append(add(in(NU_VERSTREKKEN)));
    sql.append(add("))"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Overheidsorgaan art. 3.5
   */
  public static List<ZaakKey> getGv123(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(add("and c_grondslag in"));
    sql.append(add(in(GRONDSLAG_3_5)));
    sql.append(dIn("d_in", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Derde art. 3.6
   */
  public static List<ZaakKey> getGv124(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_grondslag in"));
    sql.append(add(in(GRONDSLAG_3_6)));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Derde art. 3.9
   */
  public static List<ZaakKey> getGv125(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_grondslag in"));
    sql.append(add(in(GRONDSLAG_3_9)));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal aanvragen met een belangenafweging
   */
  public static List<ZaakKey> getGv126(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_toek in"));
    sql.append(add(in(GV_TK_BELANG)));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal verstrekkingen na belangenafweging
   */
  public static List<ZaakKey> getGv127(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_toek in"));
    sql.append(add(in(GV_TK_BELANG)));
    sql.append(add("and c_gv in (select c_gv from gv_proces where"));
    sql.append(add("c_procesactie in"));
    sql.append(add(in(NU_VERSTREKKEN)));
    sql.append(add(")"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal weigeringen na belangenafweging
   */
  public static List<ZaakKey> getGv128(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_toek in"));
    sql.append(add(in(GV_TK_BELANG)));
    sql.append(add("and c_gv in (select c_gv from gv_proces where"));
    sql.append(add("c_procesactie in"));
    sql.append(add(in(NIET_VERSTREKKEN)));
    sql.append(add(")"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal directe weigeringen
   */
  public static List<ZaakKey> getGv129(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("gv"));
    sql.append(add("where c_gv > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(add("and c_toek in"));
    sql.append(add(in(GV_TK_NEE)));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal inschrijvingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv131(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_onderz"));
    sql.append(typeDoss(ONDERZOEK.getCode()));
    sql.append(add("and doss.c_doss = doss_onderz.c_doss_onderz", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and res_onderz_betrok in"));
    sql.append(in(RES_ONDERZ_IMMIGRATIE));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ONDERZOEK);
  }

  /**
   * Aantal uitschrijvingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv132(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_onderz"));
    sql.append(typeDoss(ONDERZOEK.getCode()));
    sql.append(add("and doss.c_doss = doss_onderz.c_doss_onderz", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and res_onderz_betrok in"));
    sql.append(in(RES_ONDERZ_EMIGRATIE, RES_ONDERZ_EMIGRATIE_ONB));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ONDERZOEK);
  }

  /**
   * Aantal verhuizingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv133(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_onderz"));
    sql.append(typeDoss(ONDERZOEK.getCode()));
    sql.append(add("and doss.c_doss = doss_onderz.c_doss_onderz", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and res_onderz_betrok in"));
    sql.append(in(RES_ONDERZ_VERHUISD));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ONDERZOEK);
  }

  /**
   * Aantal uitgevoerde risicoanalyses
   */
  public static List<ZaakKey> getRiskAnalysis141(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_ra"));
    sql.append(typeDoss(RISK_ANALYSIS.getCode()));
    sql.append(add("and doss.c_doss = doss_ra.c_doss_ra", periode));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses beneden drempelwaarde
   */
  public static List<ZaakKey> getRiskAnalysis142(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_ra, rp"));
    sql.append(typeDoss(RISK_ANALYSIS.getCode()));
    sql.append(add("and doss.c_doss = doss_ra.c_doss_ra", periode));
    sql.append(add("and doss_ra.c_rp = rp.c_rp", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and c_doss_ra not in (select c_doss_ra from doss_ra_subject where score > rp.threshold)"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde
   */
  public static List<ZaakKey> getRiskAnalysis143(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_ra, rp"));
    sql.append(typeDoss(RISK_ANALYSIS.getCode()));
    sql.append(add("and doss.c_doss = doss_ra.c_doss_ra", periode));
    sql.append(add("and doss_ra.c_rp = rp.c_rp", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and c_doss_ra in (select c_doss_ra from doss_ra_subject where score > rp.threshold)"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde en gekoppelde zaak alsnog verwerkt
   */
  public static List<ZaakKey> getRiskAnalysis144(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_ra, rp"));
    sql.append(typeDoss(RISK_ANALYSIS.getCode()));
    sql.append(add("and doss.c_doss = doss_ra.c_doss_ra", periode));
    sql.append(add("and doss_ra.c_rp = rp.c_rp", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and c_doss_ra in (select c_doss_ra from doss_ra_subject where score > rp.threshold)"));
    sql.append(add("and zaak_id in (select zaak_id from zaak_rel where zaak_id_rel in"));
    sql.append(add("(select zaak_id from bvh_park where ind_verwerkt in"));
    sql.append(in(ZaakStatusType.VERWERKT.getCode()));
    sql.append(add("))"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde en gekoppelde zaak niet verwerkt (geweigerd/geannuleerd)
   */
  public static List<ZaakKey> getRiskAnalysis145(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss, doss_ra, rp"));
    sql.append(typeDoss(RISK_ANALYSIS.getCode()));
    sql.append(add("and doss.c_doss = doss_ra.c_doss_ra", periode));
    sql.append(add("and doss_ra.c_rp = rp.c_rp", periode));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and c_doss_ra in (select c_doss_ra from doss_ra_subject where score > rp.threshold)"));
    sql.append(add("and zaak_id in (select zaak_id from zaak_rel where zaak_id_rel in"));
    sql.append(add("(select zaak_id from bvh_park where ind_verwerkt in"));
    sql.append(in(ZaakStatusType.GEANNULEERD.getCode(), ZaakStatusType.GEWEIGERD.getCode()));
    sql.append(add("))"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RISK_ANALYSIS);
  }
}
