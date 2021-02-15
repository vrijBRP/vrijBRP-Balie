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
  private String id           = "";
  private String gezag        = "";

  @JsonIgnore
  private Formats formats = new Formats();

  public LandTabJsonRecord() {
  }

  public String getC_plaats() {
    return c_plaats;
  }

  public void setC_plaats(String cPlaats) {
    c_plaats = cPlaats;
  }

  public String getOpm() {
    return opm;
  }

  public void setOpm(String opm) {
    this.opm = opm;
  }

  public String getD_end() {
    return d_end;
  }

  public void setD_end(String dEnd) {
    d_end = dEnd;
  }

  public String getMut() {
    return mut;
  }

  public void setMut(String mut) {
    this.mut = mut;
  }

  public String getTabel() {
    return tabel;
  }

  public void setTabel(String tabel) {
    this.tabel = tabel;
  }

  public String getD_in() {
    return d_in;
  }

  public void setD_in(String dIn) {
    d_in = dIn;
  }

  public String getC_provin() {
    return c_provin;
  }

  public void setC_provin(String cProvin) {
    c_provin = cProvin;
  }

  public String getPlaats_nen() {
    return plaats_nen;
  }

  public void setPlaats_nen(String plaatsNen) {
    plaats_nen = plaatsNen;
  }

  public String getPlaats_l() {
    return plaats_l;
  }

  public void setPlaats_l(String plaatsL) {
    plaats_l = plaatsL;
  }

  public String getC_kanton() {
    return c_kanton;
  }

  public void setC_kanton(String cKanton) {
    c_kanton = cKanton;
  }

  public String getC_regio() {
    return c_regio;
  }

  public void setC_regio(String cRegio) {
    c_regio = cRegio;
  }

  public String getC_plaats_n() {
    return c_plaats_n;
  }

  public void setC_plaats_n(String cPlaatsN) {
    c_plaats_n = cPlaatsN;
  }

  public String getC_natio() {
    return c_natio;
  }

  public void setC_natio(String cNatio) {
    c_natio = cNatio;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getNatio() {
    return natio;
  }

  public void setNatio(String natio) {
    this.natio = natio;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getC_land() {
    return c_land;
  }

  public void setC_land(String c_land) {
    this.c_land = c_land;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getVoorv() {
    return voorv;
  }

  public void setVoorv(String voorv) {
    this.voorv = voorv;
  }

  public String getC_voorv() {
    return c_voorv;
  }

  public void setC_voorv(String c_voorv) {
    this.c_voorv = c_voorv;
  }

  public String getSrt() {
    return srt;
  }

  public void setSrt(String srt) {
    this.srt = srt;
  }

  public String getVk_vl() {
    return vk_vl;
  }

  public void setVk_vl(String vkVl) {
    vk_vl = vkVl;
  }

  public String getC_vk_vl() {
    return c_vk_vl;
  }

  public void setC_vk_vl(String cVkVl) {
    c_vk_vl = cVkVl;
  }

  public String getTp() {
    return tp;
  }

  public void setTp(String tp) {
    this.tp = tp;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getS_tp() {
    return s_tp;
  }

  public void setS_tp(String sTp) {
    s_tp = sTp;
  }

  public String getC_tp() {
    return c_tp;
  }

  public void setC_tp(String cTp) {
    c_tp = cTp;
  }

  public String getS_reisdoc() {
    return s_reisdoc;
  }

  public void setS_reisdoc(String sReisdoc) {
    s_reisdoc = sReisdoc;
  }

  public String getC_reisdoc() {
    return c_reisdoc;
  }

  public void setC_reisdoc(String cReisdoc) {
    c_reisdoc = cReisdoc;
  }

  public String getDuur() {
    return duur;
  }

  public void setDuur(String duur) {
    this.duur = duur;
  }

  public String getReisdoc() {
    return reisdoc;
  }

  public void setReisdoc(String reisdoc) {
    this.reisdoc = reisdoc;
  }

  public String getC_autorit() {
    return c_autorit;
  }

  public void setC_autorit(String cAutorit) {
    c_autorit = cAutorit;
  }

  public String getAutorit() {
    return autorit;
  }

  public void setAutorit(String autorit) {
    this.autorit = autorit;
  }

  public Formats getFormats() {
    return formats;
  }

  public void setFormats(Formats formats) {
    this.formats = formats;
  }

  public String getAand() {
    return aand;
  }

  public void setAand(String aand) {
    this.aand = aand;
  }

  public String getRdn() {
    return rdn;
  }

  public void setRdn(String rdn) {
    this.rdn = rdn;
  }

  public String getC_vbt() {
    return c_vbt;
  }

  public void setC_vbt(String c_vbt) {
    this.c_vbt = c_vbt;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getD_end_gem() {
    return d_end_gem;
  }

  public void setD_end_gem(String d_end_gem) {
    this.d_end_gem = d_end_gem;
  }

  public String getD_in_gem() {
    return d_in_gem;
  }

  public void setD_in_gem(String d_in_gem) {
    this.d_in_gem = d_in_gem;
  }

  public String getC_gem() {
    return c_gem;
  }

  public void setC_gem(String c_gem) {
    this.c_gem = c_gem;
  }

  public String getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(String woonplaats) {
    this.woonplaats = woonplaats;
  }

  public String getC_woonplaats() {
    return c_woonplaats;
  }

  public void setC_woonplaats(String c_woonplaats) {
    this.c_woonplaats = c_woonplaats;
  }

  public String getS_gem() {
    return s_gem;
  }

  public void setS_gem(String s_gem) {
    this.s_gem = s_gem;
  }

  public String getD_mut() {
    return d_mut;
  }

  public void setD_mut(String d_mut) {
    this.d_mut = d_mut;
  }

  public String getD_aansluit() {
    return d_aansluit;
  }

  public void setD_aansluit(String d_aansluit) {
    this.d_aansluit = d_aansluit;
  }

  public String getGba_dn() {
    return gba_dn;
  }

  public void setGba_dn(String gba_dn) {
    this.gba_dn = gba_dn;
  }

  public String getC_gba_dn() {
    return c_gba_dn;
  }

  public void setC_gba_dn(String c_gba_dn) {
    this.c_gba_dn = c_gba_dn;
  }

  public String getRni_dn() {
    return rni_dn;
  }

  public void setRni_dn(String rni_dn) {
    this.rni_dn = rni_dn;
  }

  public String getC_rni_dn() {
    return c_rni_dn;
  }

  public void setC_rni_dn(String c_rni_dn) {
    this.c_rni_dn = c_rni_dn;
  }

  public String getNetw() {
    return netw;
  }

  public void setNetw(String netw) {
    this.netw = netw;
  }

  public String getEu() {
    return eu;
  }

  public void setEu(String eu) {
    this.eu = eu;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getGezag() {
    return gezag;
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

        case WOONPLAATS:
          return p(4, getC_woonplaats());

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

        case WOONPLAATS:
          return getWoonplaats();

        default:
          throw new ProException("Onbekende tabel: " + getTabel());
      }
    }
  }
}
