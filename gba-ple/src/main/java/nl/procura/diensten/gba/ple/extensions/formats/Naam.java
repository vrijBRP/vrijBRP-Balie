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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.gba.common.MiscUtils;
import nl.procura.standard.Globalfunctions;

public class Naam {

  private static final String NG_EERST_PARTNER = "v";
  private static final String NG_EERST_EIGEN   = "n";
  private static final String NG_PARTNER       = "p";
  private static final String NG_EIGEN         = "e";

  private BasePLElem geslachtsnaam;
  private BasePLElem naamgebruik;
  private BasePLElem titel;
  private BasePLElem voornamen;
  private BasePLElem voorvoegsel;
  private Naam       partner;

  public Naam(BasePLRec r) {
    this(r, new BasePLElem(), null);
  }

  public Naam(BasePLRec r, BasePLElem naamgebruik, Naam partner) {
    this(r.getElem(VOORNAMEN),
        r.getElem(GESLACHTSNAAM),
        r.getElem(VOORV_GESLACHTSNAAM),
        r.getElem(TITEL_PREDIKAAT), naamgebruik, partner);
  }

  public Naam(BasePLElem voornamen, BasePLElem geslachtsnaam, BasePLElem voorvoegsel,
      BasePLElem titel, BasePLElem naamgebruik, Naam partner) {

    this.voornamen = voornamen;
    this.geslachtsnaam = geslachtsnaam;
    this.voorvoegsel = voorvoegsel;
    this.titel = titel;
    this.naamgebruik = naamgebruik;
    this.partner = partner;
  }

  public String getVoorvGesl() {
    return trimM("{0} {1}", getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()));
  }

  public String getGeslVoorv() {
    return trimM("{0}, {1}", getOms(getGeslachtsnaam()), getOms(getVoorvoegsel()));
  }

  public String getGeslInitVoorv() {
    return trimM("{0}, {1} {2}", getOms(getGeslachtsnaam()), getInit(), getOms(getVoorvoegsel()));
  }

  public String getGeslInitNenVoorv() {
    return trimM("{0}, {1} {2}", getOms(getGeslachtsnaam()), getInitNen(), getOms(getVoorvoegsel()));
  }

  public String getTitelVoorvGesl() {
    return trimM("{0} {1}", getAdeltitel(), getVoorvGesl());
  }

  public String getGeslPredInitAdelVoorv() {

    return trimM("{0}, {1} {2} {3} {4}", getOms(getGeslachtsnaam()), getPredikaattitel(), getInit(), getAdeltitel(),
        getOms(getVoorvoegsel()));
  }

  public String getGeslPredInitNenAdelVoorv() {

    return trimM("{0}, {1} {2} {3} {4}", getOms(getGeslachtsnaam()), getPredikaattitel(), getInitNen(),
        getAdeltitel(), getOms(getVoorvoegsel()));
  }

  public String getGeslTitelVoorv() {
    return trimM("{0}, {1} {2}", getOms(getGeslachtsnaam()), getAdeltitel(), getOms(getVoorvoegsel()));
  }

  public String getInitVoorvGesl() {
    return trimM("{0} {1}", getInit(), getVoorvGesl());
  }

  public String getInitNenVoorvGesl() {
    return trimM("{0} {1}", getInitNen(), getVoorvGesl());
  }

  public String getPredAdelVoorvGeslVoorn() {
    return trim(MessageFormat.format("{0} {1}, {2} {3}", getAdeltitel(), getVoorvGesl(), getPredikaattitel(),
        getVoornamen().getValue().getDescr()));
  }

  public String getPredInitAdelVoorvGesl() {
    return trimM("{0} {1} {2} {3}", getPredikaattitel(), getInit(), getAdeltitel(), getVoorvGesl());
  }

  public String getPredInitNenAdelVoorvGesl() {
    return trimM("{0} {1} {2} {3}", getPredikaattitel(), getInitNen(), getAdeltitel(), getVoorvGesl());
  }

  public String getPredEersteinitAdelVoorvGesl() {
    return trimM("{0} {1} {2} {3}", getPredikaattitel(), getEersteInit(), getAdeltitel(), getVoorvGesl());
  }

  public String getPredEersteinitNenAdelVoorvGesl() {
    return trimM("{0} {1} {2} {3}", getPredikaattitel(), getEersteInitNen(), getAdeltitel(), getVoorvGesl());
  }

  public String getPredEerstevoornAdelVoorvGesl() {
    return trimM("{0} {1} {2} {3}", getPredikaattitel(), getEersteVoornaam(), getAdeltitel(), getVoorvGesl());
  }

  public String getPredikaattitel() {
    return (isPredikaat() ? getOms(titel) : "");
  }

  public String getAdeltitel() {
    return (isAdel() ? getOms(titel) : "");
  }

  public String getNaamNaamgebruikGeslachtsnaamVoorvAanschrijf() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(
          MessageFormat.format("{0} {1} {2} - {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
              getOms(getPartner().getGeslachtsnaam()), getOms(getVoorvoegsel()),
              getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikGeslachtsnaamVoorvAanhef() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikGeslachtsnaamVoorv() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2}", getAdeltitel(), getOms(getGeslachtsnaam()),
          getOms(getVoorvoegsel())));
    } else if (getCode(getNaamgebruik()).equalsIgnoreCase(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4}", getOms(getPartner().getGeslachtsnaam()),
          getAdeltitel(), getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel())));
    } else if (getCode(getNaamgebruik()).equalsIgnoreCase(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3}, {4}", getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getAdeltitel(),
          getOms(getVoorvoegsel())));
    } else if (getCode(getNaamgebruik()).equalsIgnoreCase(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1}, {2}", getAdeltitel(), getOms(getPartner().getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel())));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikAanschrijf() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInit()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInit()));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInit()));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInit()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikAanhef() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInit()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInit()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getEersteVoornOverigInit()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getEersteVoornOverigInit()));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(),
          getEersteVoornOverigInit()));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(),
          getEersteVoornOverigInit()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikGeslachtsnaamVoornaamAanhef() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getEersteVoornOverigInit()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(),
          getEersteVoornOverigInit()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruik() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getInit(), getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getOms(getPartner().getGeslachtsnaam()),
          getAdeltitel(), getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getInit(), getOms(getPartner().getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInit(),
          getAdeltitel(), getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInit(),
          getOms(getPartner().getVoorvoegsel())));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikNenAanschrijf() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2} - {3} {4}, {5} {6}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikNenAanhef() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} {1} {2}", getAdeltitel(), getOms(getVoorvoegsel()),
          getOms(getGeslachtsnaam())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInitNen()));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikNen() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getInitNen(), getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getOms(getPartner().getGeslachtsnaam()),
          getAdeltitel(), getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getInitNen(), getOms(getPartner().getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInitNen(),
          getAdeltitel(), getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(), getInitNen(),
          getOms(getPartner().getVoorvoegsel())));
    }

    return trim(s.toString());
  }

  public String getNaamNaamgebruikEersteVoornaam() {

    String s = "";
    if (isEigenNaam() || (getPartner() == null)) {
      s = MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInit(), getOms(getVoorvoegsel()));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s = MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getOms(getPartner().getGeslachtsnaam()),
          getAdeltitel(), getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInit(),
          getOms(getPartner().getVoorvoegsel()));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s = MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel()), getOms(getPartner().getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInit(), getAdeltitel(),
          getOms(getVoorvoegsel()));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s = MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getOms(getPartner().getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInit(),
          getOms(getPartner().getVoorvoegsel()));
    }

    return trim(s);
  }

  public String getNaamNaamgebruikNenEersteVoornaam() {

    StringBuilder s = new StringBuilder();
    if (isEigenNaam() || (getPartner() == null)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInitNen(),
          getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_PARTNER)) {
      s.append(MessageFormat.format("{0} - {1} {2} {3}, {4} {5}, {6}", getOms(getPartner().getGeslachtsnaam()),
          getAdeltitel(), getOms(getVoorvoegsel()), getOms(getGeslachtsnaam()),
          getPredikaattitel(), getEersteVoornOverigInitNen(),
          getOms(getPartner().getVoorvoegsel())));
    } else if (isNaamgebruik(NG_EERST_EIGEN)) {
      s.append(MessageFormat.format("{0} - {1} {2}, {3} {4} {5}, {6}", getOms(getGeslachtsnaam()),
          getOms(getPartner().getVoorvoegsel()),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(),
          getEersteVoornOverigInitNen(), getAdeltitel(), getOms(getVoorvoegsel())));
    } else if (isNaamgebruik(NG_PARTNER)) {
      s.append(MessageFormat.format("{0} {1}, {2} {3} {4}", getAdeltitel(),
          getOms(getPartner().getGeslachtsnaam()), getPredikaattitel(),
          getEersteVoornOverigInitNen(), getOms(getPartner().getVoorvoegsel())));
    }

    return trim(s.toString());
  }

  public String getInitNen() {

    StringBuilder s = new StringBuilder();
    if (emp(getVoornInit())) {
      return "";
    }

    String[] vns = getVoornInit().split("\\s+");
    for (String voornaam : vns) {
      s.append(getInitPart(voornaam, true));
      s.append(".");
    }

    return trim(s.toString());
  }

  public String getInit() {

    StringBuilder s = new StringBuilder();
    if (emp(getOms(voornamen))) {
      return "";
    }

    String[] vns = getVoornInit().split("\\s+");
    for (String voornaam : vns) {
      s.append(getInit(voornaam, false));
    }

    s.append(" ");
    return trim(s.toString());
  }

  public String getInitialen() {
    return getInit();
  }

  public String getEersteInit() {
    return getInit(getEersteVoornaam(), false);
  }

  public String getEersteInitNen() {
    return getInit(getEersteVoornaam(), true);
  }

  public String getOverigeInitialen() {

    StringBuilder s = new StringBuilder();
    String[] vns = getVoornInit().split("\\s+");
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

  public String getOverigeInitialenNen() {

    StringBuilder s = new StringBuilder();
    String[] vns = getVoornInit().split("\\s+");
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

  public String getEersteVoornaam() {
    return getOms(voornamen).split("\\s+")[0].trim();
  }

  public String getEersteVoornOverigInit() {
    return trimM("{0} {1}", getEersteVoornaam(), getOverigeInitialen());
  }

  public String getEersteVoornOverigInitNen() {
    return trimM("{0} {1}", getEersteVoornaam(), getOverigeInitialenNen());
  }

  public String getPredVoornamen() {
    return trimM("{0} {1}", getPredikaattitel(), voornamen);
  }

  public String getAdelTitelVoorvGeslVoornOverigeInit() {
    return MessageFormat.format("{0} {1} {2}, {3} {4}", getAdeltitel(), getOms(getVoorvoegsel()),
        getOms(getGeslachtsnaam()), getPredikaattitel(), getEersteVoornOverigInit());
  }

  public String getPredEerstevoornOverigInit() {
    return trimM("{0} {1}", getPredikaattitel(), getEersteVoornOverigInit());
  }

  public String getPredEersteVoornOverigInitNen() {
    return trimM("{0} {1}", getPredikaattitel(), getEersteVoornOverigInitNen());
  }

  public String getPredInitNen() {
    return trimM("{0} {1}", getPredikaattitel(), getInitNen());
  }

  public String getPredEersteInit() {
    return trimM("{0} {1}", getPredikaattitel(), getInit(getEersteVoornaam(), false));
  }

  public String getPredEersteInitNen() {
    return trimM("{0} {1}", getPredikaattitel(), getInit(getEersteVoornaam(), true));
  }

  public String getPredInit() {
    return trimM("{0} {1}", getPredikaattitel(), getInit());
  }

  public BasePLElem getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public boolean isEigenNaam() {
    return emp(getCode(getNaamgebruik())) || isNaamgebruik(NG_EIGEN);
  }

  public BasePLElem getNaamgebruik() {
    return naamgebruik;
  }

  public BasePLElem getTitel() {
    return titel;
  }

  public BasePLElem getVoornamen() {
    return voornamen;
  }

  public BasePLElem getVoorvoegsel() {
    return voorvoegsel;
  }

  public Naam getPartner() {
    return partner;
  }

  public void setPartner(Naam partner) {
    this.partner = partner;
  }

  private String getOms(BasePLElem element) {
    return element.getValue().getDescr();
  }

  private String getCode(BasePLElem element) {
    return element.getValue().getCode();
  }

  private boolean isCode(BasePLElem element, String... codes) {

    for (String code : codes) {
      if (getCode(element).toLowerCase().startsWith(code)) {
        return true;
      }
    }
    return false;
  }

  private String trimM(String pattern, Object... arguments) {
    return trim(MiscUtils.trimAllowed(MessageFormat.format(pattern, arguments)));
  }

  private boolean isPredikaat() {
    return isCode(titel, "j");
  }

  private boolean isAdel() {
    return fil(getCode(titel)) && !isPredikaat();
  }

  private boolean isNaamgebruik(String... ngs) {
    for (String ng : ngs) {
      if (ng.equalsIgnoreCase(getCode(getNaamgebruik()))) {
        return true;
      }
    }

    return false;
  }

  private String getInitPart(String vn, boolean nen) {

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
      sb.append(getInitPart(vn, true));
    } else {
      String[] sps = vn.split("-");
      for (String sp : sps) {
        sb.append(getInitPart(sp, false));
        sb.append("-");
      }
    }

    return trim(sb.toString().replaceAll("-$", "") + ".");
  }

  private String getVoornInit() {
    return getOms(this.voornamen);
  }

  private String trim(String input) {
    return Globalfunctions.trim(input).replaceAll(" - ", "-");
  }
}
