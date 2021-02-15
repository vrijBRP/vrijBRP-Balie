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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.functies.Geslacht;

import lombok.Data;

public class GezagUtils {

  /**
   * Ouder <code>nr</code> heeft gezag
   */
  public static GezagAfleiding getGezagsStatusOuder(BasePLExt kindPl, BasePLExt ouderPl,
      BasePLExt partnerPl,
      ToestemmingConstateringen constateringen, int ouderNr) {

    if (isPersoonInBrp(ouderPl)) {
      constateringen.getPersoon().add("is gevonden in de BRP");

      if (ouderPl.getPersoon().getStatus().isOverleden()) {
        constateringen.getPersoon().add("is overleden");
        return new GezagAfleiding(GezagStatus.NEE);
      }

      constateringen.getPersoon().add("is niet overleden");
    } else {
      if (".".equals(kindPl.getOuders().getOuder(ouderNr).getNaam().getGeslachtsnaam().getValue().getVal())) {
        constateringen.getPersoon().add("is onbekend");
      } else {
        constateringen.getPersoon().add("is niet gevonden in de BRP");
      }
    }

    if (kindPl.getGezag().heeftGezag()) {

      String ind = kindPl.getGezag().getIndicatieGezag().getVal();
      boolean isOuder1 = ouderNr == 1 && ind.matches("1|1D|12");
      boolean isOuder2 = ouderNr == 2 && ind.matches("2|2D|12");

      if (isOuder1 || isOuder2) {
        if (isPersoonInBrp(ouderPl)) {
          constateringen.getAanvrager().add("valt onder de gezagsindicatie van de ouder");
          return new GezagAfleiding(GezagStatus.JA);
        }

        constateringen.getAanvrager().add("valt onder de gezagsindicatie van de ouder");
        return new GezagAfleiding(GezagStatus.JA);
      }

      if (isPersoonInBrp(ouderPl)) {
        constateringen.getAanvrager().add("valt niet onder het gezag van deze persoon");
        return new GezagAfleiding(GezagStatus.NEE);
      }

      constateringen.getAanvrager().add("valt niet onder het gezag van deze persoon");
      return new GezagAfleiding(GezagStatus.NEE);
    }

    if (isPersoonInBrp(ouderPl)) {

      if (kindPl.getOuders().getAantalOuders() == 1) {
        constateringen.getPersoon().add("is de enige ouder");
        return new GezagAfleiding(GezagStatus.JA);

      } else if (kindPl.getOuders().getAantalOuders() == 2) {

        boolean isOuderVrouw = Geslacht.VROUW.getAfkorting().equalsIgnoreCase(
            ouderPl.getPersoon().getGeslacht().getVal());

        boolean partnerVrouw = Geslacht.VROUW.getAfkorting().equalsIgnoreCase(
            partnerPl.getPersoon().getGeslacht().getVal());

        Verbintenis verbintenis = getVerbintenis(constateringen, kindPl, ouderNr, isOuderVrouw, ouderPl,
            partnerPl);

        String ouderType = ouderNr == 1 ? "ouder 2" : "ouder 1";
        String naamPartner = verbintenis.getNaamPartner();

        if (verbintenis.isMomenteelZekerVerbintenis()) {
          constateringen.getPersoon().add("is huidige partner van " + ouderType + naamPartner);
          return new GezagAfleiding(GezagStatus.JA);
        }

        if (verbintenis.isVerbintenisOntbondenVoorGeboorte()) {
          constateringen.getPersoon().add(
              "is partner geweest van " + ouderType + naamPartner + ", maar verbintenis ontbonden vóór geboorte kind");

          if (isOuderVrouw) {
            constateringen.getPersoon().add("deze ouder is vrouw");
            return new GezagAfleiding(GezagStatus.JA);
          }

          constateringen.getPersoon().add("deze ouder is geen vrouw");
          return new GezagAfleiding(GezagStatus.NEE);
        }

        if (verbintenis.isVerbintenisOntbondenNaGeboorte()) {
          constateringen.getPersoon().add(
              "is partner geweest van " + ouderType + naamPartner + ", en verbintenis ontbonden ná geboorte kind");
          return new GezagAfleiding(GezagStatus.JA);
        }

        if (verbintenis.isMomenteelMisschienVerbintenis()) {
          constateringen.getPersoon().add("is mogelijk partner van " + ouderType + naamPartner);
          return new GezagAfleiding(GezagStatus.JA);
        }

        if (verbintenis.isNooitVerbintenisGeweest()) {

          if (heeftMomenteelVerbintenis(ouderPl)) {
            String huidigeNaamPartner = getPartner(ouderPl);
            constateringen.getPersoon().add("is huidige partner van " + huidigeNaamPartner);
          }

          constateringen.getPersoon().add("is nooit partner geweest van " + ouderType + naamPartner);

          if (isOuderVrouw && partnerVrouw) {
            constateringen.getPersoon().add("heeft geen verbintenis, beide ouders zijn vrouw");
            return new GezagAfleiding(GezagStatus.MOGELIJK_JA);
          } else if (isOuderVrouw) {
            constateringen.getPersoon().add("heeft geen verbintenis, deze ouder is vrouw");
            return new GezagAfleiding(GezagStatus.JA);
          } else if (Geslacht.MAN.getAfkorting().equalsIgnoreCase(
              ouderPl.getPersoon().getGeslacht().getVal())) {
            constateringen.getPersoon().add("heeft geen verbintenis, deze ouder is man");
            return new GezagAfleiding(GezagStatus.NEE);
          }
        }
      }
    } else {
      if (kindPl.getOuders().heeftOuder(ouderNr)) {
        return new GezagAfleiding(GezagStatus.MOGELIJK_JA);
      }

      constateringen.getPersoon().add("bestaat niet");
      return new GezagAfleiding(GezagStatus.NEE);
    }

    return new GezagAfleiding(GezagStatus.NEE);
  }

  private static boolean heeftMomenteelVerbintenis(BasePLExt ouderPl) {
    return fil(getPartner(ouderPl));
  }

  private static String getPartner(BasePLExt ouderPl) {

    BasePLSet set = ouderPl.getHuwelijk().getHuwelijkSet();
    BasePLRec sluit = ouderPl.getHuwelijk().getSluiting(set, "");
    BasePLRec ontb = ouderPl.getHuwelijk().getOntbinding(set, "");
    String dHuw = sluit.getElemVal(DATUM_VERBINTENIS).getDescr();
    String dOntb = ontb.getElemVal(DATUM_ONTBINDING).getDescr();

    if (emp(dOntb) && fil(dHuw)) {
      return new Naam(sluit).getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf();
    }

    return "";
  }

  /**
   * Persoonslijst van de ouder
   */
  public static BasePLExt getOuder(BasePLExt kindPl, int nr, Services db) {

    String anr = kindPl.getOuders().getOuder(nr).getAnr().getVal();
    BasePLExt pl = null;

    if (pos(anr)) {
      pl = db.getPersonenWsService().getPersoonslijst(anr);
    }

    return (pl != null) ? pl : new BasePLExt();
  }

  /**
   * Persoonslijsten van de ouders
   */
  public static List<BasePLExt> getOuders(BasePLExt kindPl, Services db) {

    List<BasePLExt> l = new ArrayList<>();

    l.add(getOuder(kindPl, 1, db));
    l.add(getOuder(kindPl, 2, db));

    return l;
  }

  public static boolean heeftOuder(BasePLExt kindPl, BasePLExt ouderPl) {
    return getOuderNr(kindPl, ouderPl) > 0;
  }

  private static int getOuderNr(BasePLExt kindPl, BasePLExt ouderPl) {

    String ouderanr = ouderPl.getPersoon().getAnr().getVal();
    String ouderAnr1 = kindPl.getOuders().getOuder(1).getAnr().getVal();
    String ouderAnr2 = kindPl.getOuders().getOuder(2).getAnr().getVal();

    if (eq(ouderanr, ouderAnr1)) {
      return 1;
    }

    if (eq(ouderanr, ouderAnr2)) {
      return 2;
    }

    return 0;
  }

  private static boolean isPersoonInBrp(BasePLExt pl) {
    return pl != null && pl.getCat(GBACat.PERSOON).hasSets();
  }

  /**
   * @param constateringen
   * @param ouderNr
   * @param isOuderVrouw
   * @param ouder1Pl
   * @param partnerPl
   * @return
   */
  private static Verbintenis getVerbintenis(ToestemmingConstateringen constateringen, BasePLExt kindPl,
      int ouderNr, boolean isOuderVrouw, BasePLExt ouder1Pl,
      BasePLExt partnerPl) {

    long datumGeboorte = kindPl.getCat(GBACat.PERSOON).getLatestRec().getElemVal(
        GBAElem.GEBOORTEDATUM).toLong();

    Verbintenis verbintenis = new Verbintenis();

    long datumVerbintenis = -1;
    long datumOntbinding = -1;
    String naamPartner = "";

    String anummerPartner = kindPl.getLatestRec(
        ouderNr == 2 ? GBACat.OUDER_1 : GBACat.OUDER_2).getElemVal(
            GBAElem.ANR)
        .getVal();

    if (isPersoonInBrp(partnerPl)) {
      naamPartner = " (" + partnerPl.getPersoon().getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf() + ")";
    }

    for (BasePLSet set1 : ouder1Pl.getCat(GBACat.HUW_GPS).getSets()) {
      if (isSprakeVanVerbintenis(set1, anummerPartner)) {
        for (BasePLRec r1 : set1.getRecs()) {
          if (datumVerbintenis < 0) {
            datumVerbintenis = r1.getElemVal(
                GBAElem.DATUM_VERBINTENIS).toLong();
          }
          if (datumOntbinding < 0) {
            datumOntbinding = r1.getElemVal(
                GBAElem.DATUM_ONTBINDING).toLong();
          }
          if (emp(naamPartner)) {
            naamPartner = " (" + new Naam(r1).getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf() + ")";
          }
        }
      }
    }

    verbintenis.setNaamPartner(naamPartner);

    if (datumVerbintenis >= 0 && datumOntbinding < 0) { // Ze zijn momenteel gehuwd
      verbintenis.setMomenteelZekerVerbintenis(true);
    }

    if (datumVerbintenis >= 0 && datumOntbinding < datumGeboorte) { // Verbintenis is ontbonden
      verbintenis.setVerbintenisOntbondenVoorGeboorte(true);
    }

    if (datumVerbintenis >= 0 && datumOntbinding > datumGeboorte) { // Was sprake van partnerschap, maar ontbonden
      verbintenis.setVerbintenisOntbondenNaGeboorte(true);
    }

    if (datumVerbintenis == 0 && datumOntbinding == 0) {
      verbintenis.setMomenteelMisschienVerbintenis(true);
    }

    if (datumVerbintenis < 0 && datumOntbinding < 0) {
      verbintenis.setNooitVerbintenisGeweest(true);
    }

    return verbintenis;
  }

  private static boolean isSprakeVanVerbintenis(BasePLSet set1, String anr) {
    for (BasePLRec r1 : set1.getRecs()) {
      if (r1.getElemVal(GBAElem.ANR).getVal().equals(anr)) {
        return true;
      }
    }
    return false;
  }

  @Data
  public static class Verbintenis {

    private String  naamPartner;
    private boolean momenteelZekerVerbintenis;
    private boolean momenteelMisschienVerbintenis;
    private boolean verbintenisOntbondenVoorGeboorte;
    private boolean verbintenisOntbondenNaGeboorte;
    private boolean nooitVerbintenisGeweest;
  }
}
