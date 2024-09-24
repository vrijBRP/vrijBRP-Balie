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

package nl.procura.diensten.gba.ple.openoffice;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.gba.common.MiscUtils.trimNr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.converters.persoonlijst.BasePLToPersoonsLijstConverter;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.diensten.zoekpersoon.objecten.Afnemer;
import nl.procura.diensten.zoekpersoon.objecten.Afnemergegevens;
import nl.procura.diensten.zoekpersoon.objecten.Gezaggegevens;
import nl.procura.diensten.zoekpersoon.objecten.Huwelijk;
import nl.procura.diensten.zoekpersoon.objecten.Huwelijkgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Inschrijvinggegevens;
import nl.procura.diensten.zoekpersoon.objecten.Kiesrechtgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Kind;
import nl.procura.diensten.zoekpersoon.objecten.Kindgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Kladblokaantekening;
import nl.procura.diensten.zoekpersoon.objecten.Lokaleafnemersindicatie;
import nl.procura.diensten.zoekpersoon.objecten.Nationaliteit;
import nl.procura.diensten.zoekpersoon.objecten.Nationaliteitgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Oudergegevens;
import nl.procura.diensten.zoekpersoon.objecten.Overlijdengegevens;
import nl.procura.diensten.zoekpersoon.objecten.Persoonsgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Persoonslijst;
import nl.procura.diensten.zoekpersoon.objecten.Reisdocument;
import nl.procura.diensten.zoekpersoon.objecten.Reisdocumentgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Rijbewijsgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Verblijfplaatsgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Verblijfstitelgegevens;
import nl.procura.diensten.zoekpersoon.objecten.Verwijzinggegevens;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Anr;

public class DocumentPL implements Serializable {

  private static final long serialVersionUID = 4803848497650863821L;

  private OOPersoon                       persoon          = new OOPersoon();
  private OOOuder_1                       ouder_2          = new OOOuder_1();
  private OOOuder_1                       ouder_1          = new OOOuder_1();
  private List<OONationaliteit>           nationaliteiten  = new ArrayList<>();
  private List<OOKind>                    kinderen         = new ArrayList<>();
  private List<OOHuwelijk>                huwelijken       = new ArrayList<>();
  private List<OOVerblijfplaats>          verblijfplaatsen = new ArrayList<>();
  private List<OOVerblijfstitel>          verblijfstitels  = new ArrayList<>();
  private List<OOReisdocument>            reisdocumenten   = new ArrayList<>();
  private List<OOAfnemer>                 afnemers         = new ArrayList<>();
  private List<OOLokaleafnemersindicatie> Lok_afn_ind      = new ArrayList<>();
  private OOGezag                         gezag;
  private OOKiesrecht                     kiesrecht;
  private OOInschrijving                  inschrijving;
  private OOKladBlok                      kladblok;
  private OOOverlijden                    overlijden;
  private OORijbewijs                     rijbewijs;
  private OOVerwijzing                    verwijzing;

  private final BasePLExt basisPl;

  public DocumentPL(BasePLExt basisPl) {
    this.basisPl = basisPl;
    convert(basisPl, null);
  }

  public DocumentPL(BasePLExt basisPl, HashMap<String, BasePLExt> relatiePls) {
    this.basisPl = basisPl;
    convert(basisPl, relatiePls);
  }

  public void copyValues(Object obj1, Object obj2) {
    try {
      for (Method m : obj1.getClass().getMethods()) {
        copyValue(obj1, obj2, m, "get", "set");
        // Workaround for methods with _ prefix.
        // _ prefix methods are ignored by AXIS 2
        copyValue(obj1, obj2, m, "_get", "_set");
      }
    } catch (Exception e) {}
  }

  private void copyValue(Object obj1, Object obj2, Method m, String getter, String setter)
      throws NoSuchMethodException {
    if ((m.getName().startsWith(getter) && !m.getName().equals("getClass"))) {
      String name = m.getName();
      String setname = setter + name.replaceAll(getter, "");
      Method subMethod = obj1.getClass().getMethod(name);
      try {
        String value = subMethod.invoke(obj1).toString();
        Method subMethod2 = obj2.getClass().getMethod(setname, value.getClass());
        subMethod2.invoke(obj2, getValue(setname, value));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String zoekSluitingsGegevens(Huwelijk huwelijk, String type) {

    Huwelijkgegevens huw_gev = null;
    List<Huwelijkgegevens> l = new ArrayList<>();

    l.add(huwelijk.getHuwelijk_actueel());
    if (huwelijk.getHuwelijk_historie() != null) {
      l.addAll(Arrays.asList(huwelijk.getHuwelijk_historie()));
    }

    for (Huwelijkgegevens hg : l) {
      if (pos(trimNr(hg.getHuwelijksdatum()))) {
        huw_gev = hg;
        break;
      }
    }

    if (huw_gev != null) {
      if (type.equals("datum")) {
        return huw_gev.getHuwelijksdatum();
      }
      if (type.equals("plaats")) {
        return huw_gev.getHuwelijksplaats();
      }
      if (type.equals("land")) {
        return huw_gev.getHuwelijksland();
      }
    }

    return "";
  }

  public void zoekAdres(List<OOVerblijfplaats> vbs) {

    for (OOVerblijfplaats gegevens : vbs) {
      if ((gegevens != null) && emp(gegevens.getIndicatie_onjuist())) {
        gegevens.getFormats()
            .getAdres()
            .setValues(gegevens.getStraatnaam_boco(), gegevens.getHuisnummer(), gegevens.getHuisletter(),
                gegevens.getToevoeging(), gegevens.getAanduiding(), gegevens.getLocatie(),
                gegevens.getPostcode(), gegevens.getGemeentedeel(), gegevens.getWoonplaatsnaam(),
                gegevens.getGemeente_inschrijving(), gegevens.getDatum_aanvang_adreshouding(),
                gegevens.getEmigratieland(), gegevens.getEmigratiedatum(),
                gegevens.getAdres_buitenland_waarnaar_vertrokken_1(),
                gegevens.getAdres_buitenland_waarnaar_vertrokken_2(),
                gegevens.getAdres_buitenland_waarnaar_vertrokken_3());
      }
    }
  }

  public String formatPostcode(String input) {
    String newPc = input;
    if (input.length() == 6) {
      newPc = input.substring(0, 4) + " " + input.substring(4, 6);
    }
    return newPc;
  }

  public String getNationaliteit(Nationaliteit nationaliteit) {

    Nationaliteitgegevens geg = nationaliteit.getNationaliteit_actueel();
    if (geg.getAanduiding_bijzonder_nederlanderschap().equalsIgnoreCase("b")) {
      return "Behandeld als Nederlander";
    } else if (geg.getAanduiding_bijzonder_nederlanderschap().equalsIgnoreCase("v")) {
      return "Vastgesteld niet-Nederlander";
    }

    return geg.getNationaliteit();
  }

  public OOOuder_1 getOuder_1() {
    return ouder_1;
  }

  public void setOuder_1(OOOuder_1 ouder1) {
    this.ouder_1 = ouder1;
  }

  public OOPersoon getPersoon() {
    return persoon;
  }

  public void setPersoon(OOPersoon persoon) {
    this.persoon = persoon;
  }

  public OOOuder_1 getOuder_2() {
    return ouder_2;
  }

  public void setOuder_2(OOOuder_1 ouder_2) {
    this.ouder_2 = ouder_2;
  }

  public List<OOHuwelijk> getHuwelijken() {
    return huwelijken;
  }

  public void setHuwelijken(List<OOHuwelijk> huwelijken) {
    this.huwelijken = huwelijken;
  }

  public List<OOKind> getKinderen() {
    return kinderen;
  }

  public void setKinderen(List<OOKind> kinderen) {
    this.kinderen = kinderen;
  }

  public List<OONationaliteit> getNationaliteiten() {
    return nationaliteiten;
  }

  public void setNationaliteiten(List<OONationaliteit> nationaliteiten) {
    this.nationaliteiten = nationaliteiten;
  }

  public List<OOVerblijfplaats> getVerblijfplaatsen() {
    return verblijfplaatsen;
  }

  public void setVerblijfplaatsen(List<OOVerblijfplaats> verblijfplaatsen) {
    this.verblijfplaatsen = verblijfplaatsen;
  }

  public List<OOVerblijfstitel> getVerblijfstitels() {
    return verblijfstitels;
  }

  public void setVerblijfstitels(List<OOVerblijfstitel> verblijfstitels) {
    this.verblijfstitels = verblijfstitels;
  }

  public OOOverlijden getOverlijden() {
    return overlijden;
  }

  public void setOverlijden(OOOverlijden overlijden) {
    this.overlijden = overlijden;
  }

  public OOGezag getGezag() {
    return gezag;
  }

  public void setGezag(OOGezag gezag) {
    this.gezag = gezag;
  }

  public OOKiesrecht getKiesrecht() {
    return kiesrecht;
  }

  public void setKiesrecht(OOKiesrecht kiesrecht) {
    this.kiesrecht = kiesrecht;
  }

  public OOKladBlok getKladblok() {
    return kladblok;
  }

  public void setKladblok(OOKladBlok kladblok) {
    this.kladblok = kladblok;
  }

  public List<OOLokaleafnemersindicatie> getLok_afn_ind() {
    return Lok_afn_ind;
  }

  public void setLok_afn_ind(List<OOLokaleafnemersindicatie> lok_afn_ind) {
    Lok_afn_ind = lok_afn_ind;
  }

  public List<OOReisdocument> getReisdocumenten() {
    return reisdocumenten;
  }

  public void setReisdocumenten(List<OOReisdocument> reisdocumenten) {
    this.reisdocumenten = reisdocumenten;
  }

  public OORijbewijs getRijbewijs() {
    return rijbewijs;
  }

  public void setRijbewijs(OORijbewijs rijbewijs) {
    this.rijbewijs = rijbewijs;
  }

  public OOVerwijzing getVerwijzing() {
    return verwijzing;
  }

  public void setVerwijzing(OOVerwijzing verwijzing) {
    this.verwijzing = verwijzing;
  }

  public OOInschrijving getInschrijving() {
    return inschrijving;
  }

  public void setInschrijving(OOInschrijving inschrijving) {
    this.inschrijving = inschrijving;
  }

  public List<OOAfnemer> getAfnemers() {
    return afnemers;
  }

  public void setAfnemers(List<OOAfnemer> afnemers) {
    this.afnemers = afnemers;
  }

  public BasePLExt getBasisPl() {
    return basisPl;
  }

  private void convert(BasePLExt basisPl, HashMap<String, BasePLExt> relatiePls) {

    Relaties relaties = new Relaties(relatiePls);
    Persoonslijst pl = new BasePLToPersoonsLijstConverter().convert(basisPl, true, true, false);

    // Persoon
    if (pl.getPersoon() != null) {
      Persoonsgegevens gegevens = pl.getPersoon().getPersoon_actueel();
      copyValues(gegevens, persoon);

      persoon.getFormats().setCode_burgerlijke_staat(getCode_burgerlijke_staat(basisPl));
      persoon.getFormats().setBurgerlijke_staat(getburgerlijke_staat(basisPl));
      persoon.getFormats()
          .setNaam(
              new Naamformats(persoon.getVoornaam(), persoon.getGeslachtsnaam(), persoon.getVoorvoegsel(),
                  persoon.getTitel_predikaat(), persoon.getNaamgebruik(),
                  getPartnerFormats(basisPl)));
      persoon.getFormats()
          .getGeboorte()
          .setValues(persoon.getGeboortedatum(), persoon.getGeboorteplaats(), persoon.getGeboorteland());

      StringBuilder s_natio = new StringBuilder();
      if (pl.getNationaliteiten() != null) {
        for (Nationaliteit natio : pl.getNationaliteiten()) {
          s_natio.append(getNationaliteit(natio));
          s_natio.append(", ");
        }

        persoon.getFormats().setNationaliteit(trim(s_natio.toString()));
      }
    }

    // Ouder 1
    if (pl.getOuder_2() != null) {
      copyValues(pl.getOuder_1().getOuder_actueel(), ouder_1);
      ouder_1.getFormats()
          .setNaam(
              new Naamformats(ouder_1.getVoornaam(), ouder_1.getGeslachtsnaam(), ouder_1.getVoorvoegsel(),
                  ouder_1.getTitel_predikaat(), "", null));
      ouder_1.getFormats()
          .getGeboorte()
          .setValues(ouder_1.getGeboortedatum(), ouder_1.getGeboorteplaats(), ouder_1.getGeboorteland());
    }

    // Ouder 2
    if (pl.getOuder_2() != null) {
      copyValues(pl.getOuder_2().getOuder_actueel(), ouder_2);
      ouder_2.getFormats()
          .setNaam(
              new Naamformats(ouder_2.getVoornaam(), ouder_2.getGeslachtsnaam(), ouder_2.getVoorvoegsel(),
                  ouder_2.getTitel_predikaat(), "", null));
      ouder_2.getFormats()
          .getGeboorte()
          .setValues(ouder_2.getGeboortedatum(), ouder_2.getGeboorteplaats(), ouder_2.getGeboorteland());
    }

    // Nationaliteiten
    if (pl.getNationaliteiten() != null) {
      for (Nationaliteit natio : pl.getNationaliteiten()) {
        OONationaliteit oonatio = new OONationaliteit();
        copyValues(natio.getNationaliteit_actueel(), oonatio);
        oonatio.setNationaliteit(getNationaliteit(natio));
        getNationaliteiten().add(oonatio);
      }
    }

    // Huwelijken
    if (pl.getHuwelijken() != null) {
      for (Huwelijk huw : pl.getHuwelijken()) {

        OOHuwelijk oohuw = new OOHuwelijk();
        copyValues(huw.getHuwelijk_actueel(), oohuw);

        Huwelijkgegevens h_gev = huw.getHuwelijk_actueel();

        oohuw.getFormats()
            .setNaam(new Naamformats(oohuw.getVoornaam(), oohuw.getGeslachtsnaam(), oohuw.getVoorvoegsel(),
                oohuw.getTitel_predikaat(), "", null));

        oohuw.getFormats()
            .getGeboorte()
            .setValues(oohuw.getGeboortedatum(), oohuw.getGeboorteplaats(), oohuw.getGeboorteland());
        oohuw.setSoort(h_gev.getSoort());
        oohuw.setHuwelijksdatum(zoekSluitingsGegevens(huw, "datum"));
        oohuw.setHuwelijksplaats(zoekSluitingsGegevens(huw, "plaats"));
        oohuw.setHuwelijksland(zoekSluitingsGegevens(huw, "land"));
        oohuw.setOntbindingsreden(h_gev.getOntbindingsreden());

        if (fil(oohuw.getHuwelijksdatum())) {

          String huw_land = (fil(oohuw.getHuwelijksland()) &&
              !eq(oohuw.getHuwelijksland().toLowerCase(), "nederland")) ? "(" + oohuw.getHuwelijksland() + ")" : "";
          oohuw.getFormats()
              .setHuw_datum_te_plaats_land(
                  oohuw.getHuwelijksdatum() + " te " + oohuw.getHuwelijksplaats() + " " + huw_land);
        }

        if (fil(oohuw.getOntbindingsdatum())) {
          String ontb_land = (fil(oohuw.getOntbindingsland()) &&
              !eq(oohuw.getOntbindingsland().toLowerCase(), "nederland")) ? "(" + oohuw.getOntbindingsland() + ")" : "";
          oohuw.getFormats()
              .setOntb_datum_te_plaats_land(
                  oohuw.getOntbindingsdatum() + " te " + oohuw.getOntbindingsplaats() + " " +
                      ontb_land);
        }

        getHuwelijken().add(oohuw);
      }
    }

    // Overlijden
    if (pl.getOverlijden() != null) {
      setOverlijden(new OOOverlijden());
      copyValues(pl.getOverlijden().getOverlijden_actueel(), overlijden);
    }

    // Inschrijving
    if (pl.getInschrijving() != null) {
      setInschrijving(new OOInschrijving());
      copyValues(pl.getInschrijving().getInschrijving_actueel(), inschrijving);

      switch (aval(inschrijving.getIndicatie_geheim())) {
        case 0:
          inschrijving.getFormats().setOms_indicatie_geheim("Geen beperking");
          break;

        case 1:
          inschrijving.getFormats()
              .setOms_indicatie_geheim(
                  "Niet zonder toestemming aan derden ter uitvoering van een algemeen verbindend voorschrift");
          break;

        case 2:
          inschrijving.getFormats().setOms_indicatie_geheim("Niet aan kerken");
          break;

        case 3:
          inschrijving.getFormats().setOms_indicatie_geheim("Niet aan vrije derden");
          break;

        case 4:
          inschrijving.getFormats()
              .setOms_indicatie_geheim(
                  "Niet zonder toestemming aan derden ten uitvoering van een algemeen verbindend voorschrift en niet aan kerken");
          break;

        case 5:
          inschrijving.getFormats()
              .setOms_indicatie_geheim(
                  "Niet zonder toestemming aan derden ten uitvoering van een algemeen verbindend voorschrift en niet aan vrije derden");
          break;

        case 6:
          inschrijving.getFormats().setOms_indicatie_geheim("Niet aan kerken en niet aan vrije derden");
          break;

        case 7:
          inschrijving.getFormats()
              .setOms_indicatie_geheim(
                  "Niet zonder toestemming aan derden ten uitvoering van een algemeen " +
                      "verbindend voorschrift en niet aan vrije derden en niet aan kerken");
          break;

        default:
          break;
      }
    }

    // Verblijfplaats
    if (pl.getVerblijfplaats() != null) {
      OOVerblijfplaats oovb = new OOVerblijfplaats();
      copyValues(pl.getVerblijfplaats().getVerblijfplaats_actueel(), oovb);
      getVerblijfplaatsen().add(oovb);
      if (pl.getVerblijfplaats().getVerblijfplaats_historie() != null) {
        for (Verblijfplaatsgegevens vb : pl.getVerblijfplaats().getVerblijfplaats_historie()) {
          oovb = new OOVerblijfplaats();
          copyValues(vb, oovb);
          getVerblijfplaatsen().add(oovb);
        }
      }

      zoekAdres(getVerblijfplaatsen());
    }

    // Kinderen
    if (pl.getKinderen() != null) {
      for (Kind kind : pl.getKinderen()) {
        OOKind ookind = new OOKind();
        copyValues(kind.getKind_actueel(), ookind);
        ookind.getFormats()
            .setNaam(new Naamformats(ookind.getVoornaam(), ookind.getGeslachtsnaam(),
                ookind.getVoorvoegsel(), ookind.getTitel_predikaat(), "", null));
        ookind.getFormats()
            .getGeboorte()
            .setValues(ookind.getGeboortedatum(), ookind.getGeboorteplaats(), ookind.getGeboorteland());
        ookind.setGeslacht(relaties.zoekGeslacht(ookind.getAnummer()));
        getKinderen().add(ookind);
      }
    }

    // Verblijfstitel
    if (pl.getVerblijfstitel() != null) {
      OOVerblijfstitel oovb = new OOVerblijfstitel();
      copyValues(pl.getVerblijfstitel().getVerblijfstitel_actueel(), oovb);
      getVerblijfstitels().add(oovb);
      if (pl.getVerblijfstitel().getVerblijfstitel_historie() != null) {
        for (Verblijfstitelgegevens vt : pl.getVerblijfstitel().getVerblijfstitel_historie()) {
          if (emp(vt.getIndicatie_onjuist())) {
            oovb = new OOVerblijfstitel();
            copyValues(vt, oovb);
            getVerblijfstitels().add(oovb);
          }
        }
      }

      zoekAdres(getVerblijfplaatsen());
    }

    // Gezag
    if (pl.getGezag() != null) {
      setGezag(new OOGezag());
      copyValues(pl.getGezag().getGezag_actueel(), gezag);
      getGezag().setCuratele(pos(gezag.getCuratele()) ? "Ja" : "Nee");
      getGezag().setGezag_minderjarige(getGezag().getGezag_minderjarige());
    }

    // Kiesrecht
    if (pl.getKiesrecht() != null) {
      setKiesrecht(new OOKiesrecht());
      copyValues(pl.getKiesrecht().getKiesrecht_actueel(), kiesrecht);
    }

    // Reisdocumenten
    if (pl.getReisdocumenten() != null) {
      for (Reisdocument reisd : pl.getReisdocumenten()) {
        OOReisdocument ooreisd = new OOReisdocument();
        copyValues(reisd.getReisdocument_actueel(), ooreisd);
        String sAand = ooreisd.getAanduiding_inhouding_vermissing();
        String dInh = ooreisd.getDatum_inhouding_vermissing();

        if (sAand.equals("I")) {
          sAand = "Ingehouden  " + dInh;
        }

        if (sAand.equals("V")) {
          sAand = "Vermist     " + dInh;
        }

        if (sAand.equals("R")) {
          sAand = "Vervallen     " + dInh;
        }

        if (sAand.equals(".")) {
          sAand = "Onbekend     " + dInh;
        }

        ooreisd.setAanduiding_inhouding_vermissing(sAand);
        getReisdocumenten().add(ooreisd);
      }
    }

    // Afnemers
    if (pl.getAfnemers() != null) {
      for (Afnemer afnemer : pl.getAfnemers()) {
        OOAfnemer ooafnemer = new OOAfnemer();
        copyValues(afnemer.getAfnemer_actueel(), ooafnemer);
        getAfnemers().add(ooafnemer);
      }
    }

    // Rijbewijs
    if (pl.getRijbewijs() != null) {
      setRijbewijs(new OORijbewijs());
      copyValues(pl.getRijbewijs().getRijbewijs_actueel(), rijbewijs);
    }

    // Kladblok
    if (pl.getKladblokaantekening() != null) {
      setKladblok(new OOKladBlok());
      StringBuilder sb = new StringBuilder();
      if (pl.getKladblokaantekening().getRegels() != null) {
        for (String s : pl.getKladblokaantekening().getRegels()) {
          sb.append(s + "\n");
          getKladblok().setRegels(pl.getKladblokaantekening().getRegels());
        }
      }

      kladblok.setInhoud(sb.toString());
    }

    // Kinderen
    if (pl.getLokaleafnemersindicaties() != null) {
      for (Lokaleafnemersindicatie ind : pl.getLokaleafnemersindicaties()) {
        OOLokaleafnemersindicatie ooind = new OOLokaleafnemersindicatie();
        copyValues(ind, ooind);
        getLok_afn_ind().add(ooind);
      }
    }

    // Verwijzing
    if (pl.getVerwijzing() != null) {
      OOVerwijzing ooverw = new OOVerwijzing();
      setVerwijzing(ooverw);
      copyValues(pl.getVerwijzing().getVerwijzing_actueel(), verwijzing);
      ooverw.getFormats()
          .setNaam(new Naamformats(ooverw.getVoornaam(), ooverw.getGeslachtsnaam(), ooverw.getVoorvoegsel(),
              ooverw.getTitel_predikaat(), "", null));
      ooverw.getFormats()
          .getGeboorte()
          .setValues(ooverw.getGeboortedatum(), ooverw.getGeboorteplaats(), ooverw.getGeboorteland());
    }

    // Verblijfsrecht
    boolean vbr = true;
    for (OONationaliteit n : getNationaliteiten()) {
      if (n.getNationaliteit().toLowerCase().matches("(.*)nederlandse(.*)|(.*)behandeld als nederlander(.*)")) {
        vbr = false;
        break;
      }
    }

    if ((getInschrijving() != null) && getInschrijving().getReden_opschorting_pl().equalsIgnoreCase("m")) {
      vbr = false;
    }

    if ((getVerblijfstitels() != null) && (getVerblijfstitels().size() > 0)) {
      for (OOVerblijfstitel vt : getVerblijfstitels()) {
        if (fil(vt.getAanduiding_verblijfstitel()) &&
            !vt.getAanduiding_verblijfstitel().equalsIgnoreCase("98")) {
          String datumEinde = new ProcuraDate(vt.getDatum_einde_verblijfstitel()).getSystemDate();
          if (!pos(datumEinde) || !new ProcuraDate(datumEinde).isExpired()) {
            vbr = false;
          }
        }
        break;
      }
    }

    getPersoon().getFormats().setVerblijfsrecht(vbr);
  }

  private String getValue(String method, String value) {
    if (pos(value)) {
      ProcuraDate date = new ProcuraDate();
      if (method.toLowerCase().contains("geboorte")) {
        date.setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY);
        date.setAllowedFormatExceptions(true);
      }
      date.setStringDate(value);
      if (date.isCorrect()) {
        return date.getFormatDate();
      }
    }

    return value;
  }

  private String getCode_burgerlijke_staat(BasePLExt pl) {
    switch (pl.getPersoon().getBurgerlijkeStand().getStatus().getType()) {
      case HUWELIJK:
        return "H"; // Gehuwd

      case GESCHEIDEN:
        return "G"; // Gescheiden

      case WEDUWE:
        return "W"; // Weduw(e)(naar)

      case PARTNERSCHAP:
        return "P"; // Partnerschap

      case ONTBONDEN:
        return "G"; // Ontbonden partnerschap

      case ACHTERGEBLEVEN:
        return "A"; // Achtergebleven

      case ONGEHUWD:
        return "O"; // Ongehuwd

      default:
        return "X"; // Onbekend
    }
  }

  private String getburgerlijke_staat(BasePLExt pl) {
    return pl.getPersoon().getBurgerlijkeStand().getStatus().getType().getOms();
  }

  private Naamformats getPartnerFormats(BasePLExt basisPl) {

    BasePLRec huwgev = basisPl.getHuwelijk().getActueelOfMutatieRecord();
    Naamformats partner = null;

    if (huwgev.hasElems()) {
      String pVoornamen = huwgev.getElemVal(GBAElem.VOORNAMEN).getVal();
      String pGeslachtsnaam = huwgev.getElemVal(GBAElem.GESLACHTSNAAM).getVal();
      String pVoorv = huwgev.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal();
      String pTitel = huwgev.getElemVal(GBAElem.TITEL_PREDIKAAT).getVal();
      partner = new Naamformats(pVoornamen, pGeslachtsnaam, pVoorv, pTitel, "", null);
    }

    return partner;
  }

  public class OOAfnemer extends Afnemergegevens {
  }

  public class OOGezag extends Gezaggegevens {
  }

  public class OOKiesrecht extends Kiesrechtgegevens {

    public String getAdres_eu_lidstaat_van_herkomst() {
      return getVal(ADRES_EU_LIDSTAAT_VAN_HERKOMST).getValue().getDescr();
    }

    public String getPlaats_eu_lidstaat_van_herkomst() {
      return getVal(PLAATS_EU_LIDSTAAT_VAN_HERKOMST).getValue().getDescr();
    }

    public String getLand_eu_lidstaat_van_herkomst() {
      return getVal(LAND_EU_LIDSTAAT_VAN_HERKOMST).getValue().getDescr();
    }

    private BasePLElem getVal(GBAElem elem) {
      BasePLElem val = basisPl.getLatestRec(GBACat.KIESR).getElem(elem);
      return val.isAllowed() ? val : new BasePLElem();
    }
  }

  public class OOHuwelijk extends Huwelijkgegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      public Naamformats     naam                      = new Naamformats();
      public Geboorteformats geboorte                  = new Geboorteformats();
      private String         huw_datum_te_plaats_land  = "";
      private String         ontb_datum_te_plaats_land = "";

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }

      public Geboorteformats getGeboorte() {
        return geboorte;
      }

      public void setGeboorte(Geboorteformats geboorte) {
        this.geboorte = geboorte;
      }

      public String getHuw_datum_te_plaats_land() {
        return huw_datum_te_plaats_land;
      }

      public void setHuw_datum_te_plaats_land(String huw_datum_te_plaats_land) {
        this.huw_datum_te_plaats_land = huw_datum_te_plaats_land;
      }

      public String getOntb_datum_te_plaats_land() {
        return ontb_datum_te_plaats_land;
      }

      public void setOntb_datum_te_plaats_land(String ontb_datum_te_plaats_land) {
        this.ontb_datum_te_plaats_land = ontb_datum_te_plaats_land;
      }
    }
  }

  public class OOInschrijving extends Inschrijvinggegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      private String oms_indicatie_geheim = "";

      public String getOms_indicatie_geheim() {
        return oms_indicatie_geheim;
      }

      public void setOms_indicatie_geheim(String oms_indicatie_geheim) {
        this.oms_indicatie_geheim = oms_indicatie_geheim;
      }
    }
  }

  public class OOKind extends Kindgegevens {

    private String  geslacht = "";
    private Formats formats  = new Formats();

    public boolean isStillborn() {
      return "L".equals(_getRegistratie_betrekking());
    }

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public String getGeslacht() {
      return geslacht;
    }

    public void setGeslacht(String geslacht) {
      this.geslacht = geslacht;
    }

    public class Formats {

      public Naamformats     naam     = new Naamformats();
      public Geboorteformats geboorte = new Geboorteformats();

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }

      public Geboorteformats getGeboorte() {
        return geboorte;
      }

      public void setGeboorte(Geboorteformats geboorte) {
        this.geboorte = geboorte;
      }
    }
  }

  public class OOKladBlok extends Kladblokaantekening {

    String inhoud = "";

    public String getInhoud() {
      return inhoud;
    }

    public void setInhoud(String totaal) {
      this.inhoud = totaal;
    }
  }

  public class OOLokaleafnemersindicatie extends Lokaleafnemersindicatie {
  }

  public class OONationaliteit extends Nationaliteitgegevens {
  }

  public class OOOuder_1 extends Oudergegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      public Naamformats     naam     = new Naamformats();
      public Geboorteformats geboorte = new Geboorteformats();

      public Geboorteformats getGeboorte() {
        return geboorte;
      }

      public void setGeboorte(Geboorteformats geboorte) {
        this.geboorte = geboorte;
      }

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }
    }
  }

  public class OOOuder_2 extends Oudergegevens {

    public Geboorteformats geboorte = new Geboorteformats();
    private Formats        formats  = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public Geboorteformats getGeboorte() {
      return geboorte;
    }

    public void setGeboorte(Geboorteformats geboorte) {
      this.geboorte = geboorte;
    }

    public class Formats {

      public Naamformats naam = new Naamformats();

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }
    }
  }

  public class OOOverlijden extends Overlijdengegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      public String getDatum_plaats() {
        return trim(getOverlijdensdatum() + " " + getOverlijdensplaats());
      }

      public String getDatum_te_plaats() {
        return trim(getOverlijdensdatum() + " te " + getOverlijdensplaats());
      }

      public String getDatum_plaats_land() {

        String overl_land = (fil(getOverlijdensland()) &&
            !eq(getOverlijdensland().toLowerCase(), "nederland")) ? "(" + getOverlijdensland() + ")" : "";

        return trim(getOverlijdensdatum() + " " + getOverlijdensplaats() + " " + overl_land);
      }

      public String getDatum_te_plaats_land() {

        String overl_land = (fil(getOverlijdensland()) &&
            !eq(getOverlijdensland().toLowerCase(), "nederland")) ? "(" + getOverlijdensland() + ")" : "";

        return trim(getOverlijdensdatum() + " te " + getOverlijdensplaats() + " " + overl_land);
      }
    }
  }

  public class OOPersoon extends Persoonsgegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      public Naamformats     naam                   = new Naamformats();
      public Geboorteformats geboorte               = new Geboorteformats();
      private String         burgerlijke_staat      = "";
      private String         code_burgerlijke_staat = "";
      private String         nationaliteit          = "";
      private boolean        verblijfsrecht         = false;

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }

      public Geboorteformats getGeboorte() {
        return geboorte;
      }

      public void setGeboorte(Geboorteformats geboorte) {
        this.geboorte = geboorte;
      }

      public String getBurgerlijke_staat() {
        return burgerlijke_staat;
      }

      public void setBurgerlijke_staat(String burgerlijke_staat) {
        this.burgerlijke_staat = burgerlijke_staat;
      }

      public String getNationaliteit() {
        return nationaliteit;
      }

      public void setNationaliteit(String nationaliteit) {
        this.nationaliteit = nationaliteit;
      }

      public boolean isVerblijfsrecht() {
        return verblijfsrecht;
      }

      public void setVerblijfsrecht(boolean verblijfsrecht) {
        this.verblijfsrecht = verblijfsrecht;
      }

      public String getCode_burgerlijke_staat() {
        return code_burgerlijke_staat;
      }

      public void setCode_burgerlijke_staat(String code_burgerlijke_staat) {
        this.code_burgerlijke_staat = code_burgerlijke_staat;
      }
    }
  }

  public class OOReisdocument extends Reisdocumentgegevens {
  }

  public class OORijbewijs extends Rijbewijsgegevens {
  }

  public class OOVerblijfplaats extends Verblijfplaatsgegevens {

    private Formats formats = new Formats();

    private int aantalBewoners;

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public int getAantal_bewoners() {
      return aantalBewoners;
    }

    public void setAantalBewoners(int aantalBewoners) {
      this.aantalBewoners = aantalBewoners;
    }

    public class Formats {

      public Adresformats adres = new Adresformats();

      public String getImmigratie() {

        return getImmigratiedatum() + ", " + getImmigratieland();
      }

      public String getEmmigratie() {

        return getEmigratiedatum() + ", " + getEmigratieland();
      }

      public Adresformats getAdres() {
        return adres;
      }

      public void setAdres(Adresformats adres) {
        this.adres = adres;
      }
    }
  }

  public class OOVerblijfstitel extends Verblijfstitelgegevens {
  }

  public class OOVerwijzing extends Verwijzinggegevens {

    private Formats formats = new Formats();

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public class Formats {

      public Naamformats     naam     = new Naamformats();
      public Geboorteformats geboorte = new Geboorteformats();

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }

      public Geboorteformats getGeboorte() {
        return geboorte;
      }

      public void setGeboorte(Geboorteformats geboorte) {
        this.geboorte = geboorte;
      }
    }
  }

  class Relaties {

    HashMap<String, BasePLExt> rel_pls;

    public Relaties(HashMap<String, BasePLExt> rel_pls) {
      setRel_pls(rel_pls);
    }

    public String zoekGeslacht(String anr) {

      BasePLExt pl = zoekPL(anr);
      return (pl != null) ? pl.getPersoon().getGeslacht().getDescr() : "";
    }

    public HashMap<String, BasePLExt> getRel_pls() {
      return rel_pls;
    }

    public void setRel_pls(HashMap<String, BasePLExt> rel_pls) {
      this.rel_pls = rel_pls;
    }

    private BasePLExt zoekPL(String anr) {

      if ((rel_pls == null) || emp(anr)) {
        return null;
      }

      for (Iterator<String> iter = rel_pls.keySet().iterator(); iter.hasNext();) {
        String rel_anr = iter.next();
        Anr a = new Anr(anr);
        Anr a2 = new Anr(rel_anr);
        if (a.getLongAnummer().equals(a2.getLongAnummer())) {
          return rel_pls.get(rel_anr);
        }
      }

      return null;
    }
  }
}
