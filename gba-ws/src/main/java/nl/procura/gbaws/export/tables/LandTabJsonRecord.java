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

package nl.procura.gbaws.export.tables;

import static nl.procura.standard.Globalfunctions.aval;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import nl.procura.gbaws.db.enums.LandTabType;
import nl.procura.standard.exceptions.ProException;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandTabJsonRecord {

  private String c_vbt        = "";
  private String rdn          = "";
  private String oms          = "";
  private String aand         = "";
  private String autorit      = "";
  private String c_autorit    = "";
  private String c_kanton     = "";
  private String c_land       = "";
  private String c_natio      = "";
  private String c_plaats     = "";
  private String c_plaats_n   = "";
  private String c_provin     = "";
  private String c_regio      = "";
  private String c_reisdoc    = "";
  private String c_tp         = "";
  private String c_vk_vl      = "";
  private String c_voorv      = "";
  private String d_end        = "-1";
  private String d_in         = "-1";
  private String d_mut        = "-1";
  private String duur         = "";
  private String geslacht     = "";
  private String icao         = "";
  private String land         = "";
  private String mut          = "";
  private String natio        = "";
  private String opm          = "";
  private String plaats       = "";
  private String plaats_l     = "";
  private String plaats_nen   = "";
  private String reisdoc      = "";
  private String s_reisdoc    = "";
  private String s_tp         = "";
  private String srt          = "";
  private String tabel        = "";
  private String tp           = "";
  private String vk_vl        = "";
  private String voorv        = "";
  private String d_end_gem    = "";
  private String woonplaats   = "";
  private String d_in_gem     = "";
  private String c_gem        = "";
  private String c_woonplaats = "";
  private String s_gem        = "";
  private String d_aansluit   = "";
  private String gba_dn       = "";
  private String c_gba_dn     = "";
  private String rni_dn       = "";
  private String c_rni_dn     = "";
  private String netw         = "";
  private String eu           = "";
  private String eer          = "";
  private String id           = "";
  private String gezag        = "";

  @JsonIgnore
  private Formats formats = new Formats();

  public LandTabJsonRecord() {
  }

  public class Formats {

    public String getCode() {

      switch (LandTabType.get(aval(getTabel()))) {

        case NATIONALITEIT:
          return p(4, getC_natio());

        case PLAATS:
          return p(4, getC_plaats());

        case LAND:
          return p(4, getC_land());

        case VOORVOEGSEL:
          return getVoorv();

        case RDN_VERKR_NED:
          return p(3, getC_vk_vl());

        case TITEL_PRED:
          return getTp();

        case AKTE_AAND:
          return getAand();

        case RDN_HUW_ONTB:
          return getRdn();

        case NED_REISD:
          return getReisdoc();

        case AUT_VERSTR_NED_REISD:
          return getC_autorit();

        case VERBLIJFSTITEL:
          return p(2, getC_vbt());

        case GBA_DEELNEMER:
          return p(6, getC_gba_dn());

        case RNI_DEELNEMER:
          return p(4, getC_rni_dn());

        case GEZAG:
          return getGezag();

        default:
          throw new ProException("Onbekende tabel: " + getTabel());
      }
    }

    private String p(int length, String v) {
      return aval(v) >= 0 ? String.format("%0" + length + "d", aval(v)) : v;
    }

    public String getDescription() {

      switch (LandTabType.get(aval(getTabel()))) {

        case NATIONALITEIT:
          return getNatio();

        case PLAATS:
          return getPlaats();

        case LAND:
          return getLand();

        case VOORVOEGSEL:
          return getVoorv();

        case RDN_VERKR_NED:
          return getVk_vl();

        case TITEL_PRED:
          return getS_tp();

        case AKTE_AAND:
        case RDN_HUW_ONTB:
        case VERBLIJFSTITEL:
        case GEZAG:
          return getOms();

        case NED_REISD:
          return getS_reisdoc();

        case AUT_VERSTR_NED_REISD:
          return getAutorit();

        case GBA_DEELNEMER:
          return getGba_dn();

        case RNI_DEELNEMER:
          return getRni_dn();

        default:
          throw new ProException("Onbekende tabel: " + getTabel());
      }
    }
  }
}
