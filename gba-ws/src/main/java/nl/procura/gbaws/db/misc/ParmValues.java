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

package nl.procura.gbaws.db.misc;

public class ParmValues {

  public static class PROCURA {

    public static class DB {

      public static final String TNS_ADMIN_DIR         = "procura.db.tns-admin-dir";
      public static final String CUSTOM_URL            = "procura.db.custom-url";
      public static final String CUSTOM_DRIVER         = "procura.db.custom-driver";
      public static final String DB                    = "procura.db.db";
      public static final String SID                   = "procura.db.sid";
      public static final String SERVER                = "procura.db.server";
      public static final String SCHEMA                = "procura.db.schema";
      public static final String PORT                  = "procura.db.port";
      public static final String USERNAME              = "procura.db.username";
      public static final String PW                    = "procura.db.password";
      public static final String CONNECTIONS_READ_MIN  = "procura.db.connections.read.min";
      public static final String CONNECTIONS_READ_MAX  = "procura.db.connections.read.max";
    }
  }

  public static class SEARCH {

    public static final String SEARCH_URL = "search.url";

  }

  public static class GBAV {

    public static final String GBAV_WW_URL      = "gbav.gbav_ww_url";
    public static final String GBAV_URL         = "gbav.gbav_url";
    public static final String GBAV_AFN_IND_URL = "gbav.afn_ind_url";
    public static final String GBAV_USERNAME    = "gbav.gbav_username";
    public static final String GBAV_PW          = "gbav.gbav_pw";
    public static final String GBAV_CHANGE_DATE = "gbav.gbav_change_date";
  }

  public static class BRP {

    public static final String BRP_URL         = "brp.brp_url";
    public static final String BRP_ORGANISATIE = "brp.brp_organisatie";
  }

  public static class PROFILE {

    public static final String CODE_GBAV_PROFILE = "profile.code_gbav_profile";
    public static final String CODE_BRP_PROFILE  = "profile.code_brp_profile";
    public static final String SEARCH_ORDER      = "profile.search_order";
    public static final String ENABLE_DATABRON_1 = "profile.enable.databron.1";
    public static final String ENABLE_DATABRON_2 = "profile.enable.databron.2";
  }
}
