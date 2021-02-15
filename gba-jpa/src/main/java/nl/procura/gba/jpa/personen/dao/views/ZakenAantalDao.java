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

import static nl.procura.standard.Globalfunctions.along;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.views.ZakenAantalView;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.ProcuraDate;

public class ZakenAantalDao {

  public static final long C_UITTREKSEL     = ZaakType.UITTREKSEL.getCode();
  public static final long C_GEHEIM         = ZaakType.VERSTREKKINGSBEPERKING.getCode();
  public static final long C_NAAMG          = ZaakType.NAAMGEBRUIK.getCode();
  public static final long C_VERH           = ZaakType.VERHUIZING.getCode();
  public static final long C_VOG            = ZaakType.COVOG.getCode();
  public static final long C_GPK            = ZaakType.GPK.getCode();
  public static final long C_INBOX          = ZaakType.INBOX.getCode();
  public static final long C_REISD          = ZaakType.REISDOCUMENT.getCode();
  public static final long c_INH            = ZaakType.INHOUD_VERMIS.getCode();
  public static final long C_RIJB           = ZaakType.RIJBEWIJS.getCode();
  public static final long C_TMV            = ZaakType.TERUGMELDING.getCode();
  public static final long C_GEB            = ZaakType.GEBOORTE.getCode();
  public static final long C_ERK            = ZaakType.ERKENNING.getCode();
  public static final long C_NK             = ZaakType.NAAMSKEUZE.getCode();
  public static final long C_HUW            = ZaakType.HUWELIJK_GPS_GEMEENTE.getCode();
  public static final long C_OMZET          = ZaakType.OMZETTING_GPS.getCode();
  public static final long C_ONTB           = ZaakType.ONTBINDING_GEMEENTE.getCode();
  public static final long C_NAAM           = ZaakType.NAAMGEBRUIK.getCode();
  public static final long C_OVERL_GEMEENTE = ZaakType.OVERLIJDEN_IN_GEMEENTE.getCode();
  public static final long C_LIJKV          = ZaakType.LIJKVINDING.getCode();
  public static final long C_OVERL_BL       = ZaakType.OVERLIJDEN_IN_BUITENLAND.getCode();
  public static final long C_LEVENLOOS      = ZaakType.LEVENLOOS.getCode();
  public static final long C_IND            = ZaakType.INDICATIE.getCode();
  public static final long C_CORR           = ZaakType.CORRESPONDENTIE.getCode();
  public static final long C_GV             = ZaakType.GEGEVENSVERSTREKKING.getCode();
  public static final long C_ONDERZOEK      = ZaakType.ONDERZOEK.getCode();
  public static final long C_RISK_ANALYSIS  = ZaakType.RISK_ANALYSIS.getCode();
  public static final long C_REGISTRATION   = ZaakType.REGISTRATION.getCode();
  public static final long C_PL_MUT         = ZaakType.PL_MUTATION.getCode();

  /**
   * Zoek de aantallen met 1 union join
   */
  public static List<ZakenAantalView> getRealTimeAantallen() {

    List<ZakenAantalView> aantallen = new ArrayList<>();

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();

    sql.append(addArgs(dIn(selectDistinct("uitt_aanvr, document "), "d_aanvr"), C_UITTREKSEL));
    sql.append(groupBy("and uitt_aanvr.c_document = document.c_document and document.document_type = 'pl'"));
    sql.append(groupBy(dIn(selectDistinctU("geheimhouding"), "d_in"), C_GEHEIM));
    sql.append(groupBy(dIn(selectU("naamgebruik"), "d_in"), C_NAAMG));
    sql.append(groupBy(dIn(selectDistinctU("bvh_park"), "d_opn"), C_VERH));
    sql.append(groupBy(dIn(selectU("vog_aanvr"), "d_aanvr"), C_VOG));
    sql.append(groupBy(dIn(selectU("gpk"), "d_aanvr"), C_GPK));
    sql.append(groupBy(dIn(selectU("inbox"), "d_invoer"), C_INBOX));
    sql.append(groupBy(dIn(selectU("rdm01"), "d_aanvr"), C_REISD));
    sql.append(groupBy(dIn(selectU("doc_inh"), "d_inneming"), c_INH));
    sql.append(groupBy(dIn(selectU("nrd"), "d_aanvr"), C_RIJB));
    sql.append(groupBy(dIn(selectU("terugmelding"), "d_in"), C_TMV));
    sql.append(groupBy(dIn(selectU("pl_mut"), "d_in"), C_PL_MUT));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_GEB, C_GEB));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_ERK, C_ERK));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_NK, C_NK));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_HUW, C_HUW));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_OMZET, C_OMZET));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_ONTB, C_ONTB));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_NAAM, C_NAAM));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_OVERL_GEMEENTE, C_OVERL_GEMEENTE));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_LIJKV, C_LIJKV));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_OVERL_BL, C_OVERL_BL));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_LEVENLOOS, C_LEVENLOOS));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_ONDERZOEK, C_ONDERZOEK));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_RISK_ANALYSIS, C_RISK_ANALYSIS));
    sql.append(groupBy(dIn(selectU("doss where type_doss = %d"), "d_aanvr"), C_REGISTRATION, C_REGISTRATION));
    sql.append(groupBy(dIn(selectU("indicatie"), "d_in"), C_IND));
    sql.append(groupBy(dIn(selectU("correspondentie"), "d_in"), C_CORR));
    sql.append(groupBy(dIn(selectU("gv"), "d_in"), C_GV));

    for (Object obj : em.createNativeQuery(sql.toString()).getResultList()) {
      Object[] row = (Object[]) obj;
      aantallen.add(new ZakenAantalView(along(row[0]), along(row[1]), along(row[2])));
    }

    return aantallen;
  }

  private static String selectDistinctU(String sql) {
    return " union select %d as zaak_type, ind_verwerkt, count (distinct (zaak_id)) as aantal from " + sql;
  }

  private static String selectDistinct(String sql) {
    return " select %d as zaak_type, ind_verwerkt, count (distinct (zaak_id)) as aantal from " + sql;
  }

  private static String selectU(String sql) {
    return " union select %d as zaak_type, ind_verwerkt, count (zaak_id) as aantal from " + sql;
  }

  private static String dIn(String sql, String date) {
    StringBuilder rsql = new StringBuilder();
    rsql.append(" ");
    rsql.append(sql);
    rsql.append(sql.contains("where") ? " and " : " where ");
    rsql.append(date);
    rsql.append(" >=  " + getYear());
    return rsql.toString();
  }

  private static long getYear() {
    return along(new ProcuraDate().addYears(-1).getSystemDate());
  }

  private static String addArgs(String sql, Object... args) {
    return " " + String.format(sql, args);
  }

  private static String groupBy(String sql, Object... args) {
    return " " + addArgs(sql + " group by ind_verwerkt ", args);
  }
}
