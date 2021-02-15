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

package nl.procura.diensten.gba.ple.procura.utils.diacrits;

import java.util.HashMap;

public class DiacList {

  private final static HashMap<String, Diacref> diacs = new HashMap<>();

  public static void init() {

    if (diacs.isEmpty()) {

      synchronized (diacs) {

        if (diacs.isEmpty()) {

          add(Diacs.ABO, "cAbo", "abo", "diac", "v_00ca02");
          add(Diacs.ABO_PLAATS, "cAbo", "plaats", "diacP", "v_00ca10");
          add(Diacs.ABO_STRAAT, "cAbo", "straat", "diacS", "v_00ca04");
          add(Diacs.ADRESIND, "adrInd", "oms", "diac", "v_00av01");
          add(Diacs.AFNEMER_NAAM, "afn", "naam", "", "v_359520");
          add(Diacs.AFNEMER_STRAAT, "afn", "straat", "", "v_359530");
          add(Diacs.AG_ACT_OMS, "cActOms", "actOms", "diac", "ag_act_o");
          add(Diacs.AG_AFH_RT, "cAfhRt", "tekst", "diac", "ag_tekst");
          add(Diacs.AG_HUW_LOC, "cHuwLoc", "huwLoc", "diac", "ag_huw_l");
          add(Diacs.AG_STUKKEN, "cStukken", "stukken", "diac", "ag_stuk");
          add(Diacs.AKTEAAND, "aand", "oms", "diac", "v_003901");
          add(Diacs.AKTETEXT, "cTekst", "tekst", "diac", "v_00cj01");
          add(Diacs.ARRON, "cArron", "plaats", "diac", "v_00al03");
          add(Diacs.AUTORIT, "cAutorit", "autorit", "", "v_004902");
          add(Diacs.B_ADRES, "cBAdres", "adr", "diac", "b_adres");
          add(Diacs.CORR_ADRES, "cCorrAdres", "adr", "diac", "c_adres");
          add(Diacs.DOC, "cDoc", "doc", "diac", "v_00bk01");
          add(Diacs.GEM_DEEL, "cGemDeel", "gemDeel", "diac", "v_00bd02");
          add(Diacs.GEM_DEEL_NEN, "cGemDeel", "gemDeelNen", "diacNen", "v_00bd05");
          add(Diacs.HUW_ONTB, "rdn", "oms", "diac", "v_004101");
          add(Diacs.KANTON, "cKanton", "plaats", "diac", "v_00am03");
          add(Diacs.KENMERK, "cKenmerk", "kenmerk", "", "v_00aj01");
          add(Diacs.LAND, "cLand", "land", "diac", "v_003402");
          add(Diacs.LOCATIE, "cLocatie", "locatie", "diac", "v_00be01");
          add(Diacs.NAAM, "cNaam", "naam", "diac", "v_00ai01");
          add(Diacs.NATIO, "cNatio", "natio", "diac", "v_003202");
          add(Diacs.OBJ_1, "cObj", "obj", "diac", "v_00bh01");
          add(Diacs.OBJ_2, "cObj", "obj", "diac", "v_00bi01");
          add(Diacs.OBJ_3, "cObj", "obj", "diac", "v_00bj01");
          add(Diacs.OBR, "cObr", "obr", "diac", "obr");
          add(Diacs.PK, "cPk", "oms", "diac", "v_005502");
          add(Diacs.PLAATS, "cPlaats", "plaats", "diac", "v_003302");
          add(Diacs.PLAATS_L, "cPlaats", "plaatsL", "diacL", "v_003313");
          add(Diacs.PLAATS_NEN, "cPlaats", "plaatsNen", "diacNen", "v_003315");
          add(Diacs.PPD, "cPpd", "ppd", "diac", "v_00aq01");
          add(Diacs.PROT_AFN, "cProtAfn", "afnemer", "diacA", "prot_a_a");
          add(Diacs.PROT_AFN_PLAATS, "cProtAfn", "plaats", "diacP", "prot_a_p");
          add(Diacs.PROT_AFN_STRAAT, "cProtAfn", "straat", "diacS", "prot_a_s");
          add(Diacs.PROT_DOEL, "cProtDoel", "oms", "diacO", "prot_dl");
          add(Diacs.RDM_OMS, "cRdmOms", "rdmOms", "diac", "rdm_oms");
          add(Diacs.REISDOC, "cReisdoc", "reisdoc", "diac", "v_004802");
          add(Diacs.REISDOC_ZKARG, "cReisdoc", "zoekarg", "", "v_004801");
          add(Diacs.STEM, "cStem", "stem", "diac", "v_00ar01");
          add(Diacs.STRAAT, "cStraat", "straat", "diac", "v_00bc02");
          add(Diacs.STRAAT_L, "cStraat", "straatL", "diacL", "v_00bc05");
          add(Diacs.STRAAT_NEN, "cStraat", "straatNen", "diacNen", "v_00bc07");
          add(Diacs.TMV_MELD_INH_N, "cDiac", "elemN", "diacN", "tmv_elmn");
          add(Diacs.TMV_MELD_INH_O, "cDiac", "elemO", "diacO", "tmv_elmo");
          add(Diacs.TMV_MELD_KOP_M, "cDiac", "meldId", "diacM", "tmv_mid");
          add(Diacs.TMV_MELD_KOP_T1, "cDiac", "toel1", "diac1", "tmv_toe1");
          add(Diacs.TMV_MELD_KOP_T2, "cDiac", "toel2", "diac2", "tmv_toe2");
          add(Diacs.TP, "cTp", "tp", "", "v_003802");
          add(Diacs.TP_ZKARG, "cTp", "zoekarg", "", "v_003801");
          add(Diacs.USR, "cUsr", "usr", "diac", "v_00bt01");
          add(Diacs.VBTIT, "cVbt", "oms", "diac", "v_005601");
          add(Diacs.VK_VL, "cVkVl", "vkVl", "diac", "v_003701");
          add(Diacs.VOG_AANVR_AANSCHR, "cAanvr", "aanschrijf", "diacO1", "vog_o1");
          add(Diacs.VOG_AANVR_DEL, "cAanvr", "doelFunc", "diacA1", "vog_a1");
          add(Diacs.VOG_AANVR_NAAM, "cAanvr", "naamB", "diacB1", "vog_b1");
          add(Diacs.VOG_AANVR_NAAMV, "cAanvr", "naamV", "diacB2", "vog_b2");
          add(Diacs.VOG_AANVR_PLAATS, "cAanvr", "plaats", "diacO3", "vog_o3");
          add(Diacs.VOG_AANVR_PLAATSB, "cAanvr", "plaatsB", "diacB4", "vog_b4");
          add(Diacs.VOG_AANVR_STRAAT, "cAanvr", "straat", "diacO2", "vog_o2");
          add(Diacs.VOG_AANVR_STRAATB, "cAanvr", "straatB", "diacB3", "vog_b3");
          add(Diacs.VOG_BELANG_DOEL, "cBelang", "doelFunc", "diacA1", "vog_b_a1");
          add(Diacs.VOG_BELANG_NAAMB, "cBelang", "naamB", "diacB1", "vog_b_b1");
          add(Diacs.VOG_BELANG_NAAMV, "cBelang", "naamV", "diacB2", "vog_b_b2");
          add(Diacs.VOG_BELANG_PLAATSB, "cBelang", "plaatsB", "diacB4", "vog_b_b4");
          add(Diacs.VOG_BELANG_STRAATB, "cBelang", "straatB", "diacB3", "vog_b_b3");
          add(Diacs.VOG_FOUT_TAB, "cFout", "oms", "diac", "vog_fout");
          add(Diacs.VOG_FUNC_TAB, "cFuncAsp", "oms", "diac", "vog_func");
          add(Diacs.VOG_PROF_TAB, "cScrProf", "oms", "diac", "vog_prof");
          add(Diacs.VOOGDIJ, "cVoogdij", "voogdij", "diac", "v_006101");
          add(Diacs.VOORN, "cVoorn", "voorn", "diac", "v_00ah01");
          add(Diacs.VOORV, "cVoorv", "voorv", "", "v_003601");
          add(Diacs.WONING, "cWoning", "woning", "diac", "v_00as01");
          add(Diacs.WON_IND, "wonInd", "oms", "diac", "v_00ak01");
          add(Diacs.WPL, "cWpl", "wpl", "diac", "wpl");
        }
      }
    }
  }

  private static void add(String code, String codeField, String targetField, String diac, String desc) {

    Diacref d = new Diacref();
    d.setCodefield(codeField);
    d.setTargetfield(targetField);
    d.setDiac(diac);
    d.setDescr(desc);

    diacs.put(code, d);
  }

  public static Diacref getRef(String s) {

    init();

    return diacs.get(s);
  }

  public static class Diacref {

    private String codefield   = "";
    private String targetfield = "";
    private String descr       = "";
    private String diac        = "";

    public String getDiac() {
      return diac;
    }

    public void setDiac(String diac) {
      this.diac = diac;
    }

    public String getCodefield() {
      return codefield;
    }

    public void setCodefield(String codefield) {
      this.codefield = codefield;
    }

    public String getTargetfield() {
      return targetfield;
    }

    public void setTargetfield(String targetfield) {
      this.targetfield = targetfield;
    }

    public String getDescr() {
      return descr;
    }

    public void setDescr(String descr) {
      this.descr = descr;
    }
  }
}
