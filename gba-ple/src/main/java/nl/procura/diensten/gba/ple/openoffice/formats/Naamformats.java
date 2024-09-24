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

package nl.procura.diensten.gba.ple.openoffice.formats;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;

public class Naamformats {

  private String      geslachtsnaam = "";
  private String      naamgebruik   = "";
  private String      titel         = "";
  private String      voornamen     = "";
  private String      voorvoegsel   = "";
  private Naamformats partner       = null;

  public Naamformats() {
  }

  public Naamformats(String voornamen, String geslachtsnaam, String voorvoegsel, String titel, String naamgebruik,
      Naamformats partner) {

    this.voornamen = astr(voornamen);
    this.geslachtsnaam = astr(geslachtsnaam);
    this.voorvoegsel = astr(voorvoegsel);
    this.titel = astr(titel);
    this.naamgebruik = astr(naamgebruik);
    this.partner = partner;
  }

  public String getVoorv_gesl() {
    return trim(MessageFormat.format("{0} {1}", getVoorvoegsel(), getGeslachtsnaam()));
  }

  public String getGesl_voorv() {
    return trim(MessageFormat.format("{0}, {1}", getGeslachtsnaam(), getVoorvoegsel()));
  }

  public String getGesl_init_voorv() {
    return trim(MessageFormat.format("{0}, {1} {2}", getGeslachtsnaam(), getInit(), getVoorvoegsel()));
  }

  public String getGesl_init_nen_voorv() {
    return trim(MessageFormat.format("{0}, {1} {2}", getGeslachtsnaam(), getInit_nen(), getVoorvoegsel()));
  }

  public String getTitel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1}", getAdeltitel(), getVoorv_gesl()));
  }

  public String getGesl_pred_init_adel_voorv() {
    return trim(MessageFormat.format("{0}, {1} {2} {3} {4}", getGeslachtsnaam(), getPredikaattitel(), getInit(),
        getAdeltitel(), getVoorvoegsel()));
  }

  public String getGesl_pred_init_nen_adel_voorv() {
    return trim(MessageFormat.format("{0}, {1} {2} {3} {4}", getGeslachtsnaam(), getPredikaattitel(), getInit_nen(),
        getAdeltitel(), getVoorvoegsel()));
  }

  public String getGesl_titel_voorv() {
    return trim(MessageFormat.format("{0}, {1} {2}", getGeslachtsnaam(), getAdeltitel(), getVoorvoegsel()));
  }

  private boolean isPredikaat() {
    return titel.toLowerCase().startsWith("j");
  }

  public String getInit_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1}", getInit(), getVoorv_gesl()));
  }

  public String getInit_nen_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1}", getInit_nen(), getVoorv_gesl()));
  }

  public String getPred_adel_voorv_gesl_voorn() {
    return trim(MessageFormat.format("{0} {1}, {2} {3}", getAdeltitel(), getVoorv_gesl(), getPredikaattitel(),
        getVoornamen()));
  }

  public String getPred_init_adel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getPredikaattitel(), getInit(), getAdeltitel(),
        getVoorv_gesl()));
  }

  public String getPred_init_nen_adel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getPredikaattitel(), getInit_nen(), getAdeltitel(),
        getVoorv_gesl()));
  }

  public String getPred_eersteinit_adel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getPredikaattitel(), getEersteinit(), getAdeltitel(),
        getVoorv_gesl()));
  }

  public String getPred_eersteinit_nen_adel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getPredikaattitel(), getEersteinit_nen(), getAdeltitel(),
        getVoorv_gesl()));
  }

  public String getPred_eerstevoorn_adel_voorv_gesl() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getPredikaattitel(), getEerstevoornaam(), getAdeltitel(),
        getVoorv_gesl()));
  }

  private boolean isAdel() {
    return fil(titel) && !isPredikaat();
  }

  public String getPredikaattitel() {
    return (isPredikaat() ? titel : "");
  }

  public String getAdeltitel() {
    return (isAdel() ? titel : "");
  }

  public String getNaam_naamgebruik_geslachtsnaam_voorv_aanschrijf() {

    StringBuilder s = new StringBuilder();

    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_geslachtsnaam_voorv_aanhef() {

    StringBuilder s = new StringBuilder();

    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_geslachtsnaam_voorv() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2}", getAdeltitel(), getGeslachtsnaam(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4}", getPartner().getGeslachtsnaam(), getAdeltitel(),
          getVoorvoegsel(), getGeslachtsnaam(), getPartner().getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3}, {4}", getGeslachtsnaam(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getAdeltitel(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1}, {2}", getAdeltitel(), getPartner().getGeslachtsnaam(),
          getPartner().getVoorvoegsel()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_aanschrijf() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(),
          getPredikaattitel(), getInit()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(), getInit()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(), getVoorvoegsel(),
          getGeslachtsnaam(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_aanhef() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(),
          getPredikaattitel(), getInit()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik() {

    String s = "";
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s = MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getGeslachtsnaam(), getPredikaattitel(),
          getInit(), getVoorvoegsel());
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s = MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getPartner().getGeslachtsnaam(), getAdeltitel(),
          getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(), getInit(),
          getPartner().getVoorvoegsel());
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s = MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getGeslachtsnaam(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getInit(), getAdeltitel(), getVoorvoegsel());
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s = MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getInit(), getPartner().getVoorvoegsel());
    }

    return trim(s);
  }

  public String getNaam_naamgebruik_nen_aanschrijf() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(),
          getPredikaattitel(), getInit_nen()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(), getInit_nen()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(), getVoorvoegsel(),
          getGeslachtsnaam(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit_nen()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit_nen()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_nen_aanhef() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(),
          getPredikaattitel(), getInit_nen()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(), getPredikaattitel(), getInit_nen()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_nen() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getGeslachtsnaam(),
          getPredikaattitel(), getInit_nen(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getPartner().getGeslachtsnaam(),
          getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(),
          getInit_nen(), getPartner().getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getGeslachtsnaam(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getInit_nen(), getAdeltitel(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getInit_nen(), getPartner().getVoorvoegsel()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_eerste_voornaam() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getPartner().getGeslachtsnaam(),
          getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(),
          getEerstevoorn_overiginit(), getPartner().getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getGeslachtsnaam(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit(), getAdeltitel(),
          getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit(),
          getPartner().getVoorvoegsel()));
    }

    return trim(s.toString());
  }

  public String getGeslachtsnaam_naamgebruik() {
    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}",
          getAdeltitel(),
          getVoorvoegsel(),
          getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}",
          getAdeltitel(),
          getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam(),
          getVoorvoegsel(),
          getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}",
          getAdeltitel(),
          getVoorvoegsel(),
          getGeslachtsnaam(),
          getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1} {2}",
          getAdeltitel(),
          getPartner().getVoorvoegsel(),
          getPartner().getGeslachtsnaam()));
    }

    return trim(s.toString());
  }

  public String getNaam_naamgebruik_nen_eerste_voornaam() {

    StringBuilder s = new StringBuilder();
    if (emp(getNaamgebruik()) || getNaamgebruik().equalsIgnoreCase("e") || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit_nen(), getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("v")) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getPartner().getGeslachtsnaam(),
          getAdeltitel(), getVoorvoegsel(), getGeslachtsnaam(), getPredikaattitel(),
          getEerstevoorn_overiginit_nen(), getPartner().getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("n")) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getGeslachtsnaam(),
          getPartner().getVoorvoegsel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit_nen(), getAdeltitel(),
          getVoorvoegsel()));
    } else if (getNaamgebruik().equalsIgnoreCase("p")) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getPartner().getGeslachtsnaam(),
          getPredikaattitel(), getEerstevoorn_overiginit_nen(),
          getPartner().getVoorvoegsel()));
    }

    return trim(s.toString());
  }

  private String getInitPart(String vn, boolean nen) {

    if (fil(vn)) {
      vn = vn.replaceAll("^-", "");
    }

    if (emp(vn)) {
      return "";
    }

    if (nen) {
      return vn.substring(0, 1);
    }

    HashMap<String, String> hm = new HashMap<>();
    hm.put("chr", "Chr");
    hm.put("ph", "Ph");
    hm.put("th", "Th");
    hm.put("ij", "IJ");

    for (Entry<String, String> entry : hm.entrySet()) {
      if (vn.toLowerCase().startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    return vn.substring(0, 1);
  }

  private String getInit(String vn, boolean nen) {
    StringBuilder sb = new StringBuilder();
    if (emp(vn)) {
      return "";
    }

    if (nen) {
      String initPart = getInitPart(vn, true);
      if (fil(initPart)) {
        sb.append(initPart);
      }
    } else {
      String[] sps = vn.trim().split("-");
      for (String sp : sps) {
        String initPart = getInitPart(sp, false);
        if (fil(initPart)) {
          sb.append(initPart);
          sb.append("-");
        }
      }
    }

    return trim(sb.toString().replaceAll("-$", "") + ". ");
  }

  public String getInit_nen() {
    StringBuilder sb = new StringBuilder();
    if (emp(getVoornamen())) {
      return "";
    }

    String[] vns = getVoornamen().split("\\s+");
    for (String voornaam : vns) {
      String initPart = getInitPart(voornaam, true);
      if (fil(initPart)) {
        sb.append(initPart);
        sb.append(".");
      }
    }

    return trim(sb.toString());
  }

  public String getInit() {

    String s = "";
    if (emp(getVoornamen())) {
      return "";
    }

    String[] vns = getVoornamen().split("\\s+");
    for (String voornaam : vns) {
      s += getInit(voornaam, false);
    }

    s = (s + " ");
    return trim(s);
  }

  public String getInitialen() {
    return getInit();
  }

  public String getEersteinit() {
    return getInit(getEerstevoornaam(), false);
  }

  public String getEersteinit_nen() {
    return getInit(getEerstevoornaam(), true);
  }

  public String getOverigeinitialen() {

    StringBuilder s = new StringBuilder();
    String[] vns = getVoornamen().split("\\s+");
    int i = 0;
    for (String voornaam : vns) {
      i++;
      if (i == 1) {
        continue;
      }

      s.append(getInit(voornaam, false));
    }

    return trim(s.toString());
  }

  public String getOverigeinitialen_nen() {

    StringBuilder s = new StringBuilder();
    String[] vns = getVoornamen().split("\\s+");
    int i = 0;
    for (String vn : vns) {
      i++;
      if (i == 1) {
        continue;
      }

      s.append(getInit(vn, true));
    }

    return trim(s.toString());
  }

  public String getEerstevoornaam() {
    return trim(getVoornamen().split("\\s+")[0]);
  }

  public String getEerstevoorn_overiginit() {
    return trim(MessageFormat.format("{0} {1}", getEerstevoornaam(), getOverigeinitialen()));
  }

  public String getEerstevoorn_overiginit_nen() {
    return trim(MessageFormat.format("{0} {1}", getEerstevoornaam(), getOverigeinitialen_nen()));
  }

  public String getPred_voornamen() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), voornamen));
  }

  public String getPred_eerstevoorn_overiginit() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getEerstevoorn_overiginit()));
  }

  public String getPred_eerstevoorn_overiginit_nen() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getEerstevoorn_overiginit_nen()));
  }

  public String getPred_init_nen() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getInit_nen()));
  }

  public String getPred_eersteinit() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getInit(getEerstevoornaam(), false)));
  }

  public String getPred_eersteinit_nen() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getInit(getEerstevoornaam(), true)));
  }

  public String getPred_init() {
    return trim(MessageFormat.format("{0} {1}", getPredikaattitel(), getInit()));
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getNaamgebruik() {
    return naamgebruik;
  }

  public void setNaamgebruik(String naamgebruik) {
    this.naamgebruik = naamgebruik;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getVoornamen() {
    return voornamen.trim();
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public Naamformats getPartner() {
    return partner;
  }

  public void setPartner(Naamformats partner) {
    this.partner = partner;
  }

  private String trim(String input) {
    return input.trim()
        .replaceAll(" - ", "-")
        .replaceAll("\\s+", " ");
  }
}
