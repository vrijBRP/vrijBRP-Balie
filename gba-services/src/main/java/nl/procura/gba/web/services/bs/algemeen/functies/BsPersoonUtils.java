/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.bs.algemeen.functies;

import static ch.lambdaj.Lambda.joinFrom;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_EINDE_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_OPN_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_VERBINTENIS;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ANDERS;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat10VbtExt;
import nl.procura.diensten.gba.ple.extensions.Cat11GezagExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.diensten.gba.ple.extensions.Cat6OverlExt;
import nl.procura.diensten.gba.ple.extensions.Cat8VbExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaat.BurgerlijkeStandStatus;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonComparators;
import nl.procura.gba.web.services.bs.erkenning.KindLeeftijdsType;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BsPersoonUtils extends BsUtils {

  /**
   * Controleer of de persoon overleden is
   */
  public static void controleerBurgerlijkeStaat(DossierPersoon persoon) {

    DossierPersoonType persoonType = persoon.getDossierPersoonType();
    BurgerlijkeStaatType bsType = BurgerlijkeStaatType.get(persoon.getBurgerlijkeStaat());
    String bsTypeOms = bsType.getOms().toLowerCase();

    if (!persoonType.heeftMogelijkeBurgerlijkeStaat(bsType)) {
      throw new ProException(WARNING,
          "Persoonstype <b>{0}</b> correspondeert niet met de burgerlijke staat <b>{1}</b>.",
          persoonType.getDescr(), bsTypeOms);
    }
  }

  /**
   * Alleen personen selecteren die horen bij het type (vader/erkenner = man, moeder = vrouw)
   */
  public static void controleerGeslacht(DossierPersoon persoon) {

    DossierPersoonType persoonType = persoon.getDossierPersoonType();

    String persoonGeslacht = persoonType.getGeslacht();
    String geslacht = persoon.getGeslacht().getAfkorting();

    if (fil(persoonGeslacht) && !equalsIgnoreCase(geslacht, persoonGeslacht)) {

      String type = geslacht.equalsIgnoreCase("m") ? "man" : "vrouw";
      String naam = persoon.getNaam().getNaam_naamgebruik_eerste_voornaam();
      String persoonTypeOms = persoonType.getDescrExtra().toLowerCase();

      throw new ProException(WARNING, "{0} is een {1} en kan dus niet {2} zijn", naam, type, persoonTypeOms);
    }
  }

  /**
   * Controleer of de persoon overleden is
   */
  public static void controleerOverlijden(BasePLExt pl, DossierPersoon persoon) {

    boolean isOverleden = pl.getOverlijding().isOverleden();
    DossierPersoonType persoonType = persoon.getDossierPersoonType();
    boolean magOverleden = persoonType.isMagOverledenZijn();

    if (isOverleden && !magOverleden) {
      throw new ProException(WARNING, "Deze persoon is overleden.");
    }
  }

  /**
   * Geeft lege DossierPersoon terug
   */
  public static DossierPersoon empty(DossierPersoon persoon) {

    if (persoon != null) {
      return persoon;
    }

    persoon = new DossierPersoon();
    persoon.setVoornaam("");
    persoon.setGeslachtsnaam("");

    return persoon;
  }

  /**
   * Geeft de landen terug die gedeeld worden tussen de personen
   */
  public static String getGedeeldeLanden(List<DossierPersoon> personen) {

    Set<FieldValue> landen = new HashSet<>();

    for (DossierPersoon dp1 : personen) {
      if (personen.size() == 1) {
        return dp1.getLand().getDescription();
      }

      for (DossierPersoon dp2 : personen) {
        if (dp1.isPersoon(dp2)) {
          continue;
        }

        if (dp1.getLand().equals(dp2.getLand())) {
          landen.add(dp1.getLand());
        }
      }
    }

    return landen.size() > 0 ? joinFrom(landen).getDescription() : "";
  }

  /**
   * Geeft de leeftijden terug die gedeeld worden tussen de personen.
   */
  public static String getGedeeldeLeeftijden(List<DossierPersoon> personen) {

    Set<KindLeeftijdsType> leeftijden = new HashSet<>();
    StringBuilder join = new StringBuilder();

    for (DossierPersoon dp : personen) {

      KindLeeftijdsType lt = KindLeeftijdsType.get(dp.getLeeftijd());

      if (!leeftijden.contains(lt)) {
        leeftijden.add(lt);
        join.append(lt.getOms());
        join.append(", ");
      }
    }

    return leeftijden.size() == 1 ? trim(join.toString()) : "";
  }

  /**
   * Geeft de nationaliteiten terug die gedeeld worden tussen de personen
   */
  public static String getGedeeldeNationaliteiten(List<DossierPersoon> personen) {

    Set<FieldValue> nationaliteiten = new HashSet<>();

    for (DossierPersoon dp1 : personen) {

      for (DossierNationaliteit dn : dp1.getNationaliteiten()) {

        if (personen.size() == 1) {
          return dn.getNationaliteitOmschrijving();
        }

        boolean komtVoor = true;

        for (DossierPersoon dp2 : personen) {

          if (dp1.isPersoon(dp2)) {
            continue;
          }

          if (!dp2.getNationaliteiten().contains(dn)) {
            komtVoor = false;
          }
        }

        if (komtVoor) {
          nationaliteiten.add(dn.getNationaliteit());
        }
      }
    }

    return nationaliteiten.size() > 0 ? joinFrom(nationaliteiten).getDescription() : "";
  }

  public static FieldValue getGedeeldLand(DossierNamenrecht afstammingsrecht) {

    FieldValue land1 = afstammingsrecht.getMoeder().getLand();
    FieldValue land2 = afstammingsrecht.getVaderErkenner().getLand();

    if (land1.equals(land2)) {
      return land1;
    }

    return new FieldValue();
  }

  /**
   * Geeft een relevante omschrijving van de naam terug.
   * Dit wilt zeggen de aktenaam als deze gevuld is anders de normale naam.
   * Daarnaast wordt getoond of de persoon overleden is.
   */
  public static String getNaam(DossierPersoon persoon) {
    StringBuilder out = new StringBuilder();
    if (DossierPersoonType.AMBTENAAR.is(persoon.getDossierPersoonType()) && fil(persoon.getAktenaam())) {
      out.append(persoon.getAktenaam());
    } else {
      out.append(persoon.getNaam().getNaam_naamgebruik_eerste_voornaam());
    }
    if (persoon.isOverleden()) {
      out.append(" (overleden)");
    }
    return out.toString();
  }

  public static boolean heeftGedeeldLand(DossierNamenrecht afstammingsrecht) {
    return pos(getGedeeldLand(afstammingsrecht).getValue());
  }

  public static boolean isGeborenInNederland(DossierPersoon persoon) {
    return Landelijk.getNederland().equals(persoon.getGeboorteland());
  }

  /**
   * Is min. één van de personen overleden?
   */
  public static boolean isOverleden(List<DossierPersoon> personen) {
    if (personen != null) {
      for (DossierPersoon persoon : personen) {
        if (persoon != null && persoon.isOverleden()) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isWoonachtigInNederland(DossierPersoon persoon) {
    return Landelijk.getNederland().equals(persoon.getLand());
  }

  public static DossierPersoon kopieDossierPersoon(BasePLRec persoonRecord, DossierPersoon persoon) {

    DossierPersoon kopiePersoon;
    switch (persoonRecord.getCatType()) {
      case HUW_GPS:
        kopiePersoon = kopiePartnerDossierPersoon(persoonRecord, persoon);
        break;

      case KINDEREN:
        kopiePersoon = kopieKindDossierPersoon(persoonRecord, persoon);
        break;

      case OUDER_1:
      case OUDER_2:
        kopiePersoon = kopieOuderDossierPersoon(persoonRecord, persoon);
        break;

      default:
        throw new ProException(ERROR, "Onbekende categorie: " + persoonRecord.getCatType());
    }

    // Personen die uit een PL komen altijd de voornaam bevestigen
    return kopiePersoon;
  }

  public static DossierPersoon kopieDossierPersoon(BasePLExt pl) {
    return kopieDossierPersoon(pl, new DossierPersoon());
  }

  /**
   * Kopieert basispl naar persoon
   */
  public static DossierPersoon kopieDossierPersoon(BasePLExt pl, DossierPersoon persoon) {
    reset(persoon);

    if (pl != null) {
      Cat1PersoonExt p = pl.getPersoon();
      Cat6OverlExt overl = pl.getOverlijding();
      Cat8VbExt vb = pl.getVerblijfplaats();
      Cat10VbtExt vbt = pl.getVerblijfstitel();
      Cat11GezagExt gezag = pl.getGezag();

      // Naw
      persoon.setBurgerServiceNummer(new BsnFieldValue(p.getBsn().getCode()));
      persoon.setAnummer(new AnrFieldValue(p.getAnr().getCode()));
      persoon.setGeslachtsnaam(p.getNaam().getGeslachtsnaam().getValue().getVal());
      persoon.setVoornaam(p.getNaam().getVoornamen().getValue().getVal());
      persoon.setVoornaamBevestigd(true);
      persoon.setNaamgebruik(p.getNaam().getNaamgebruik().getValue().getVal());
      persoon.setVoorvoegsel(p.getNaam().getVoorvoegsel().getValue().getVal());
      persoon.setAktenaam(trim(p.getNaam().getInitNen() + " " +
          p.getNaam().getNaamNaamgebruikGeslachtsnaamVoorvAanschrijf()));

      // Vul aktenaam met die van de ambtenaar als BSN matched
      if (persoon.getDossierPersoonType() == DossierPersoonType.AMBTENAAR) {
        BsnFieldValue bsnFieldValue = persoon.getBurgerServiceNummer();
        if (bsnFieldValue.isCorrect()) {
          HuwelijksAmbtenaar ambtenaar = Services.getInstance()
              .getHuwelijkService()
              .getHuwelijksambtenaar(bsnFieldValue);
          if (ambtenaar != null) {
            persoon.setAktenaam(ambtenaar.getHuwelijksAmbtenaar());
          }
        }
      }

      BasePLValue tp = p.getTitel().getValue();
      persoon.setTitel(new FieldValue(tp.getVal(), tp.getDescr()));
      persoon.setGeslacht(Geslacht.get(p.getGeslacht().getCode()));

      Naam partner = p.getNaam().getPartner();
      if (partner != null) {
        persoon.setPartnerVoornaam(partner.getVoornamen().getValue().getVal());
        persoon.setPartnerVoorvoegsel(partner.getVoorvoegsel().getValue().getVal());
        persoon.setPartnerGeslachtsnaam(partner.getGeslachtsnaam().getValue().getVal());
        persoon.setPartnerTitel(new FieldValue(partner.getTitel().getValue().getVal(),
            partner.getTitel().getValue().getDescr()));
      }

      BurgerlijkeStandStatus bsType = p.getBurgerlijkeStand().getStatus();
      BasePLValue vTitel = vbt.getVerblijfstitel();

      // Geboorte
      BasePLValue gebP = p.getGeboorte().getGeboorteplaats();
      BasePLValue gebL = p.getGeboorte().getGeboorteland();

      persoon.setDatumGeboorte(new GbaDateFieldValue(p.getGeboorte().getGeboortedatum()));
      persoon.setGeboorteplaats(new FieldValue(gebP.getVal(), gebP.getDescr()));
      persoon.setGeboorteland(new FieldValue(gebL.getVal(), gebL.getDescr()));

      // Overlijden
      persoon.setDatumOverlijden(new GbaDateFieldValue(overl.getDatumOverlijden()));

      // Akte
      BasePLValue ag = pl.getPersoon().getAkteGemeente();
      persoon.setGeboorteAkteNummer(pl.getPersoon().getAkteNummer().getVal());
      persoon.setGeboorteAkteBrpNummer(pl.getPersoon().getAkteNummer().getVal());
      persoon.setGeboorteAktePlaats(new FieldValue(ag.getVal(), ag.getDescr()));
      persoon.setGeboorteAkteJaar(
          toBigDecimal(new ProcuraDate(persoon.getDatumGeboorte().getSystemDate()).getYear()));

      // Immigratie
      BasePLValue immL = vb.getImmigratieland();
      persoon.setDatumImmigratie(new GbaDateFieldValue(vb.getImmigratiedatum()));
      persoon.setImmigratieland(new FieldValue(immL.getVal(), immL.getDescr()));

      // Adres
      Adres adres = vb.getAdres();
      BasePLValue wp = adres.getWoonplaats().getValue();
      BasePLValue wg = adres.getGemeente().getValue();
      BasePLValue lnd = vb.getLand();

      persoon.setStraat(adres.getStraat().getValue().getVal());
      persoon.setHuisnummer(along(adres.getHuisnummer().getValue().getVal()));
      persoon.setHuisnummerLetter(adres.getHuisletter().getValue().getVal());
      persoon.setHuisnummerToev(adres.getHuisnummertoev().getValue().getVal());
      persoon.setHuisnummerAand(adres.getHuisnummeraand().getValue().getVal());
      persoon.setPostcode(new FieldValue(adres.getPostcode().getValue().getVal()));

      persoon.setWoonplaats(new FieldValue(wp.getVal(), wp.getDescr()));
      persoon.setWoongemeente(new FieldValue(wg.getVal(), wg.getDescr()));
      persoon.setInGemeente(Services.getInstance().getGebruiker().isGemeente(wg.toLong()));
      persoon.setLand(new FieldValue(lnd.getVal(), lnd.getDescr()));
      persoon.setDatumMoment(new DateTime());

      persoon.setBurgerlijkeStaat(bsType.getType());
      persoon.setDatumBurgerlijkeStaat(new DateTime(bsType.getDatum()));

      persoon.setVerblijfstitel(new FieldValue(vTitel.getVal(), vTitel.getDescr()));
      persoon.setVerstrekkingsbeperking(p.getStatus().isGeheim());
      persoon.setOnderCuratele(gezag.staatOnderCuratele());

      // Nationaliteiten
      persoon.getNationaliteiten().clear();

      for (BasePLSet set : pl.getCat(GBACat.NATIO).getSets()) {

        DossierNationaliteit natio = new DossierNationaliteit();
        BasePLRec a = set.getLatestRec();

        BasePLValue sNat = a.getElemVal(GBAElem.NATIONALITEIT);
        BasePLValue dNat = a.getElemVal(GBAElem.INGANGSDAT_GELDIG);
        BasePLValue rVerkrijg = a.getElemVal(REDEN_OPN_NATIO);
        BasePLValue rVerlies = a.getElemVal(REDEN_EINDE_NATIO);

        // Bij verlies overslaan
        if (fil(rVerlies.getDescr())) {
          continue;
        }

        natio.setNationaliteit(new FieldValue(sNat.getVal(), sNat.getDescr()));
        natio.setRedenverkrijgingNederlanderschap(
            new FieldValue(rVerkrijg.getVal(), rVerkrijg.getDescr()));
        natio.setDatumVerkrijging(new DateTime(along(dNat.getVal())));
        natio.setVerkrijgingType(pos(dNat.getVal()) ? ANDERS : DossierNationaliteitDatumVanafType.ONBEKEND);

        BsNatioUtils.checkNationaliteit(natio);

        if (aval(natio.getNationaliteit().getValue()) >= 0) {
          to(persoon, DossierPersoon.class).toevoegenNationaliteit(natio);
        }
      }

      controleerGeslacht(persoon);
    }

    return persoon;
  }

  public static DossierPersoon kopieDossierPersoonFromDatabase(DossierPersoon dossierPersoon, DossierPersoon targetPersoon) {
    BsnFieldValue bsn = dossierPersoon.getBurgerServiceNummer();
    if (bsn.isCorrect()) {
      BasePLExt pl = Services.getInstance().getPersonenWsService().getPersoonslijst(bsn.getStringValue());
      if (pl.getCats().isNotEmpty()) {
        return kopieDossierPersoon(pl, targetPersoon);
      }
    }
    return kopieDossierPersoon(dossierPersoon, targetPersoon);
  }

  /**
   * Kopieert de ene dossierPersoon naar de andere
   */
  public static DossierPersoon kopieDossierPersoon(DossierPersoon dossierPersoon, DossierPersoon targetPersoon) {

    DossierPersoon dp = dossierPersoon;
    DossierPersoon tp = targetPersoon;

    // Unique identifier
    tp.setUid(dp.getUid());

    // Naw
    tp.setBurgerServiceNummer(dp.getBurgerServiceNummer());
    tp.setAnummer(dp.getAnummer());
    tp.setAktenaam(dp.getAktenaam());
    tp.setGeslachtsnaam(dp.getGeslachtsnaam());
    tp.setVoornaam(dp.getVoornaam());
    tp.setVoornaamBevestigd(dp.isVoornaamBevestigd());
    tp.setNaamgebruik(dp.getNaamgebruik());
    tp.setVoorvoegsel(dp.getVoorvoegsel());
    tp.setTitel(dp.getTitel());
    tp.setGeslacht(dp.getGeslacht());

    // Geboorte
    tp.setDatumGeboorte(dp.getDatumGeboorte());
    tp.setGeboorteplaats(dp.getGeboorteplaats());
    tp.setGeboorteplaatsAkte(dp.getGeboorteplaatsAkte());
    tp.setGeboorteland(dp.getGeboorteland());

    // Overlijden
    tp.setDatumOverlijden(dp.getDatumOverlijden());

    // Immigratie
    tp.setDatumImmigratie(dp.getDatumImmigratie());
    tp.setImmigratieland(dp.getImmigratieland());

    // Adres
    tp.setStraat(dp.getStraat());
    tp.setHuisnummer(dp.getHuisnummer());
    tp.setHuisnummerLetter(dp.getHuisnummerLetter());
    tp.setHuisnummerToev(dp.getHuisnummerToev());
    tp.setHuisnummerAand(dp.getHuisnummerAand());
    tp.setPostcode(dp.getPostcode());
    tp.setWoonplaats(dp.getWoonplaats());
    tp.setWoongemeente(dp.getWoongemeente());
    tp.setInGemeente(Services.getInstance().getGebruiker().isGemeente(along(tp.getCWoonGemeente())));
    tp.setWoonplaatsAkte(dp.getWoonplaatsAkte());
    tp.setLand(dp.getLand());
    tp.setWoonlandAkte(dp.getWoonlandAkte());
    tp.setDatumMoment(tp.getDatumMoment());

    tp.setBurgerlijkeStaat(dp.getBurgerlijkeStaat());
    tp.setDatumBurgerlijkeStaat(dp.getDatumBurgerlijkeStaat());

    tp.setVerblijfstitel(dp.getVerblijfstitel());
    tp.setVerstrekkingsbeperking(dp.isVerstrekkingsbeperking());
    tp.setOnderCuratele(dp.isOnderCuratele());

    for (DossierNationaliteit nationaliteit : dp.getNationaliteiten()) {
      tp.toevoegenNationaliteit(nationaliteit);
    }

    return targetPersoon;
  }

  /**
   * Kopieert DossierPersoon naar een lege DossierPersoon
   */
  public static DossierPersoon kopieNieuwDossierPersoon(DossierPersoon dossierPersoon) {
    return kopieDossierPersoon(dossierPersoon, new DossierPersoon());
  }

  /**
   * Kopieert een lege DossierPersoon naar DossierPersoon
   */
  public static DossierPersoon reset(DossierPersoon dossierPersoon) {
    return kopieDossierPersoon(new DossierPersoon(), dossierPersoon);
  }

  /**
   * Sorteer DossierPersonen
   */
  public static List<DossierPersoon> sort(List<DossierPersoon> personen) {
    personen.sort(DossierPersoonComparators.getDefault());
    return personen;
  }

  /**
   * Kopieert kind gegevens naar persoon
   */
  private static DossierPersoon kopieKindDossierPersoon(BasePLRec persoonRecord,
      DossierPersoon persoon) {

    BasePLValue tp = persoonRecord.getElemVal(GBAElem.TITEL_PREDIKAAT);
    BasePLValue gebPlaats = persoonRecord.getElemVal(GBAElem.GEBOORTEPLAATS);
    BasePLValue gebLand = persoonRecord.getElemVal(GBAElem.GEBOORTELAND);

    persoon.setAnummer(new AnrFieldValue(persoonRecord.getElemVal(GBAElem.ANR).getVal()));
    persoon.setBurgerServiceNummer(
        new BsnFieldValue(persoonRecord.getElemVal(GBAElem.BSN).getVal()));
    persoon.setGeslachtsnaam(persoonRecord.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
    persoon.setVoornaam(persoonRecord.getElemVal(GBAElem.VOORNAMEN).getVal());
    persoon.setVoornaamBevestigd(true);
    persoon.setNaamgebruik("");
    persoon.setTitel(new FieldValue(tp.getVal(), tp.getDescr()));
    persoon.setVoorvoegsel(persoonRecord.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal());
    persoon.setDatumGeboorte(
        new GbaDateFieldValue(persoonRecord.getElemVal(GBAElem.GEBOORTEDATUM).getVal()));
    persoon.setGeboorteplaats(new FieldValue(gebPlaats.getVal(), gebPlaats.getDescr()));
    persoon.setGeboorteplaatsAkte(persoonRecord.getElemVal(GBAElem.GEBOORTEPLAATS).getDescr());
    persoon.setGeboorteland(new FieldValue(gebLand.getVal(), gebLand.getDescr()));

    return persoon;
  }

  /**
   * Kopieert ouder gegevens naar persoon
   */
  private static DossierPersoon kopieOuderDossierPersoon(BasePLRec partnerRecord,
      DossierPersoon persoon) {

    BasePLValue tp = partnerRecord.getElemVal(GBAElem.TITEL_PREDIKAAT);
    BasePLValue gebPlaats = partnerRecord.getElemVal(GBAElem.GEBOORTEPLAATS);
    BasePLValue gebLand = partnerRecord.getElemVal(GBAElem.GEBOORTELAND);

    persoon.setAnummer(new AnrFieldValue(partnerRecord.getElemVal(GBAElem.ANR).getVal()));
    persoon.setBurgerServiceNummer(
        new BsnFieldValue(partnerRecord.getElemVal(GBAElem.BSN).getVal()));

    persoon.setGeslachtsnaam(partnerRecord.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
    persoon.setVoornaam(partnerRecord.getElemVal(GBAElem.VOORNAMEN).getVal());
    persoon.setNaamgebruik("");
    persoon.setTitel(new FieldValue(tp.getVal(), tp.getDescr()));
    persoon.setVoorvoegsel(partnerRecord.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal());
    persoon.setGeslacht(Geslacht.get(partnerRecord.getElemVal(GBAElem.GESLACHTSAAND).getVal()));

    persoon.setDatumGeboorte(
        new GbaDateFieldValue(partnerRecord.getElemVal(GBAElem.GEBOORTEDATUM).getVal()));
    persoon.setGeboorteplaats(new FieldValue(gebPlaats.getVal(), gebPlaats.getDescr()));
    persoon.setGeboorteplaatsAkte(partnerRecord.getElemVal(GBAElem.GEBOORTEPLAATS).getDescr());
    persoon.setGeboorteland(new FieldValue(gebLand.getVal(), gebLand.getDescr()));

    return persoon;
  }

  /**
   * Kopieert partner gegevens naar persoon
   */
  private static DossierPersoon kopiePartnerDossierPersoon(BasePLRec partnerRecord,
      DossierPersoon persoon) {

    BasePLValue tp = partnerRecord.getElemVal(GBAElem.TITEL_PREDIKAAT);
    BasePLValue gebPlaats = partnerRecord.getElemVal(GBAElem.GEBOORTEPLAATS);
    BasePLValue gebLand = partnerRecord.getElemVal(GBAElem.GEBOORTELAND);

    persoon.setAnummer(new AnrFieldValue(partnerRecord.getElemVal(GBAElem.ANR).getVal()));
    persoon.setBurgerServiceNummer(
        new BsnFieldValue(partnerRecord.getElemVal(GBAElem.BSN).getVal()));
    persoon.setGeslachtsnaam(partnerRecord.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
    persoon.setVoornaam(partnerRecord.getElemVal(GBAElem.VOORNAMEN).getVal());
    persoon.setNaamgebruik("");
    persoon.setTitel(new FieldValue(tp.getVal(), tp.getDescr()));
    persoon.setVoorvoegsel(partnerRecord.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal());
    persoon.setGeslacht(Geslacht.get(partnerRecord.getElemVal(GBAElem.GESLACHTSAAND).getVal()));
    persoon.setDatumGeboorte(
        new GbaDateFieldValue(partnerRecord.getElemVal(GBAElem.GEBOORTEDATUM).getVal()));
    persoon.setGeboorteplaats(new FieldValue(gebPlaats.getVal(), gebPlaats.getDescr()));
    persoon.setGeboorteplaatsAkte(gebPlaats.getDescr());
    persoon.setGeboorteland(new FieldValue(gebLand.getVal(), gebLand.getDescr()));

    // Burgerlijke staat bepalen
    long datumHuwelijk = along(
        partnerRecord.getElemVal(DATUM_VERBINTENIS).getVal());
    long datumOntbinding = along(
        partnerRecord.getElemVal(DATUM_ONTBINDING).getVal());

    if (datumHuwelijk > 0 && datumOntbinding < 0) {

      persoon.setDatumBurgerlijkeStaat(new DateTime(datumHuwelijk));

      SoortVerbintenis soort = SoortVerbintenis.get(partnerRecord.getElemVal(SOORT_VERBINTENIS).getVal());

      switch (soort) {
        case GPS:
          persoon.setBurgerlijkeStaat(PARTNERSCHAP);
          break;

        case HUWELIJK:
          persoon.setBurgerlijkeStaat(HUWELIJK);
          break;

        case ONBEKEND:
        default:
          break;
      }
    }

    return persoon;
  }
}
