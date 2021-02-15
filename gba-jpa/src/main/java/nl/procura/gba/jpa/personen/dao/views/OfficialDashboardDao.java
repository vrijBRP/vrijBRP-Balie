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
import static nl.procura.gba.jpa.personen.dao.views.ZakenAantalDao.*;

import java.util.List;

import javax.persistence.EntityManager;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class OfficialDashboardDao extends DashboardDao {

  // Soorten verhuizingen
  public static final int VERHUIS_BINNEN       = 1;
  public static final int VERHUIS_BUITEN       = 2;
  public static final int VERHUIS_EMIGRATIE    = 3;
  public static final int VERHUIS_HERVESTIGING = 4;

  // Redenen aanvraag rijbewijzen
  private static final int WEGENS_VERLIES_OF_DIEFSTAL = 1;
  // Status rijbewijs uitgereikt
  private static final int RIJBEWIJS_UITGEREIKT = 90;

  /**
   * Geboortes op datum invoer
   */
  public static List<ZaakKey> getGeboorten12(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(GEBOORTE.getCode()));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), GEBOORTE);
  }

  /**
   * Erkenningen op datum invoer
   */
  public static List<ZaakKey> getErkenningen13(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(ERKENNING.getCode()));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ERKENNING);
  }

  /**
   * Overlijdens op datum invoer
   */
  public static List<ZaakKey> getOverlijden14(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_OVERL_BL, C_OVERL_GEMEENTE, C_LIJKV, C_LEVENLOOS));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ZaakType.ONBEKEND);
  }

  /**
   * Huwelijken/GPS op datum invoer
   */
  public static List<ZaakKey> getHuwelijken21(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_HUW, C_OMZET));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ZaakType.ONBEKEND);
  }

  /**
   * Ontbinding/einde huwelijk/gps
   */
  public static List<ZaakKey> getOntbindingen22(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_ONTB));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ZaakType.ONBEKEND);
  }

  /**
   * Uittreksels met type pl
   */
  public static List<ZaakKey> getUittreksels31(boolean digitaal, DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();

    StringBuilder sql = new StringBuilder();

    sql.append(select("uitt_aanvr, document"));
    sql.append(add("where c_uitt_aanvr > 0"));
    sql.append(add("and uitt_aanvr.c_document = document.c_document"));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and document.document_type = 'pl'"));
    sql.append(addBronnen(digitaal, periode.getBronnen()));
    sql.append(addLeveranciers(digitaal, periode.getLeveranciers()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), UITTREKSEL);
  }

  /**
   * Attestaties de Vita + in lezen zijn
   */
  public static List<ZaakKey> getBewijsInLeven32(boolean digitaal, DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("uitt_aanvr, document"));
    sql.append(add("where c_uitt_aanvr > 0"));
    sql.append(add("and uitt_aanvr.c_document = document.c_document"));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and document.document_type = 'pl' "));
    sql.append("and (document like '%Attestatie%' or document like '%in%leven%zijn%')");
    sql.append(addBronnen(digitaal, periode.getBronnen()));
    sql.append(addLeveranciers(digitaal, periode.getLeveranciers()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), UITTREKSEL);
  }

  /**
   * COVOG aanvragen
   */
  public static List<ZaakKey> getVog33(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("vog_aanvr"));
    sql.append(add("where c_vog_aanvr > 0"));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), COVOG);
  }

  /**
   * Naamgebruik aanvragen
   */
  public static List<ZaakKey> getNaamgebruik34(boolean digitaal, DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("naamgebruik"));
    sql.append(add("where c_naamgebruik > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(addBronnen(digitaal, periode.getBronnen()));
    sql.append(addLeveranciers(digitaal, periode.getLeveranciers()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), NAAMGEBRUIK);
  }

  /**
   * Geheimhoudingsverzoeken
   */
  public static List<ZaakKey> getGeheimhouding35(boolean digitaal, DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("geheimhouding"));
    sql.append(add("where c_geheimhouding > 0"));
    sql.append(dIn("d_in", periode));
    sql.append(addBronnen(digitaal, periode.getBronnen()));
    sql.append(addLeveranciers(digitaal, periode.getLeveranciers()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), VERSTREKKINGSBEPERKING);
  }

  /**
   * Binnen- / Intergemeentelijke verhuizingen
   */
  public static List<ZaakKey> getVerhuizingen41(boolean digitaal, DashboardPeriode periode) {
    return getVerhuizingen(digitaal, periode, VERHUIS_BINNEN, VERHUIS_BUITEN);
  }

  /**
   * Hervestigingen
   */
  public static List<ZaakKey> getVerhuizingen42(DashboardPeriode periode) {
    List<ZaakKey> verhuizingen = getVerhuizingen(false, periode, VERHUIS_HERVESTIGING);
    verhuizingen.addAll(getVestigingen(periode));
    return verhuizingen;
  }

  /**
   * Emigraties
   */
  public static List<ZaakKey> getVerhuizingen43(boolean digitaal, DashboardPeriode periode) {
    return getVerhuizingen(digitaal, periode, VERHUIS_EMIGRATIE);
  }

  /**
   * Eerste inschrijvingen
   */
  private static List<ZaakKey> getVestigingen(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_REGISTRATION));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), REGISTRATION);
  }

  private static List<ZaakKey> getVerhuizingen(boolean digitaal, DashboardPeriode periode, long... types) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("bvh_park"));
    sql.append(add("where c_bvh_park > 0"));
    sql.append(add("and verhuis_type in"));
    sql.append(add(in(types)));
    sql.append(dIn("d_opn", periode));
    sql.append(addBronnen(digitaal, periode.getBronnen()));
    sql.append(addLeveranciers(digitaal, periode.getLeveranciers()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), VERHUIZING);
  }

  /**
   * Aangevraagde reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten61(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("rdm01"));
    sql.append(add("where rdm01_id > 0"));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), REISDOCUMENT);
  }

  /**
   * Afgehaalde reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten62(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("rdm01"));
    sql.append(add("where rdm01_id > 0"));
    sql.append(dIn("d_afsl", periode));
    sql.append(add("and stat_afsl in (1,3)")); // Document uitgereikt / Document uitgereikt door andere instantie

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), REISDOCUMENT);
  }

  /**
   * Vermiste reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten63(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doc_inh"));
    sql.append(add("where d_inneming > 0"));
    sql.append(add("and ind_rijbewijs < 1"));
    sql.append(dIn("d_inneming", periode));
    sql.append(add("and aand_vi = '%s'", "V"));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), INHOUD_VERMIS);
  }

  /**
   * Aangevraagde rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen71(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("nrd"));
    sql.append(add("where c_aanvr > 0"));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RIJBEWIJS);
  }

  /**
   * Afgehaalde rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen72(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("nrd"));
    sql.append(add("where c_aanvr > 0"));
    sql.append(add("and c_aanvr in (select c_aanvr from nrd_status where c_stat = %d", RIJBEWIJS_UITGEREIKT));
    sql.append(dIn("rdw_d_stat", periode));
    sql.append(")");

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), RIJBEWIJS);
  }

  /**
   * Vermiste rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen73(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();

    // zoeken in NRD tabel, tenzij de vermissing ook als aparte vermissing
    // in doc_inh is opgeslagen
    StringBuilder sql1 = new StringBuilder();
    sql1.append(select("nrd"));
    sql1.append(add("where c_aanvr > 0"));
    sql1.append(dIn("d_aanvr", periode));
    sql1.append(add("and rdn_aanvr = %d", WEGENS_VERLIES_OF_DIEFSTAL));
    sql1.append(add("and rbw_nr_verv not in (select nr_nl_doc from doc_inh)"));

    List zaakKeys1 = toZaakKeys(em.createNativeQuery(sql1.toString()).getResultList(), RIJBEWIJS);

    StringBuilder sql2 = new StringBuilder();
    sql2.append(select("doc_inh"));
    sql2.append(add("where d_inneming > 0"));
    sql2.append(add("and ind_rijbewijs > 0"));
    sql2.append(dIn("d_inneming", periode));
    sql2.append(add("and aand_vi = '%s'", "V"));
    List zaakKeys2 = toZaakKeys(em.createNativeQuery(sql2.toString()).getResultList(), INHOUD_VERMIS);

    zaakKeys1.addAll(zaakKeys2);
    return zaakKeys1;
  }

  /**
   * Aangevraagde onderzoeken
   */
  public static List<ZaakKey> getOnderzoeken102(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_ONDERZOEK));
    sql.append(dIn("d_aanvr", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ONDERZOEK);
  }

  /**
   * Uitgevoerde onderzoeken
   */
  public static List<ZaakKey> getOnderzoeken103(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("doss"));
    sql.append(typeDoss(C_ONDERZOEK));
    sql.append(dIn("d_aanvr", periode));
    sql.append(add("and zaak_id in (select zaak_id " +
        "from ind_verwerkt where ind_verwerkt = %s)", ZaakStatusType.VERWERKT.getCode()));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), ONDERZOEK);
  }

  /**
   * Verzonden terugmeldingen BRP
   */
  public static List<ZaakKey> getTerugmeldingen105(DashboardPeriode periode) {

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(select("terugmelding"));
    sql.append(add("where c_terugmelding > 0"));
    sql.append(add("and c_terugmelding in (select c_terugmelding from terugm_tmv where berichtcode like 'ONBV')"));
    sql.append(dIn("d_in", periode));

    return toZaakKeys(em.createNativeQuery(sql.toString()).getResultList(), TERUGMELDING);
  }
}
