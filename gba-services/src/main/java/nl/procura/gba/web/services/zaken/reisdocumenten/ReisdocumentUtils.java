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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat10VbtExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ReisdocumentUtils {

  private static final int JAAR_TWEEDE_PASPOORT     = 2;
  private static final int JAAR_VLUCHT_PP_VBT_26    = 3;
  private static final int JAAR_VLUCHT_PP           = 5;
  private static final int JAAR_REISDOC_MINDERJARIG = 5;
  private static final int JAAR_REISDOC_VOLWASSEN   = 10;

  public static ProcuraDate getDatumGeldigMax(ReisdocumentService reisdocumenten, ReisdocumentType type,
      BasePLExt pl) {
    return new ProcuraDate().addYears(getGeldigJaar(reisdocumenten, type, pl));
  }

  public static ProcuraDate getDatumGeldigMin(BasePLExt pl) {

    switch (aval(pl.getVerblijfstitel().getVerblijfstitel().getVal())) {

      case 21:
      case 22:
      case 23:
      case 24:
      case 35:
        return new ProcuraDate();

      case 26:
        return new ProcuraDate().addYears(1);

      case 25:
      case 27:
      default:
        return new ProcuraDate();
    }
  }

  public static int getGeldigJaar(ReisdocumentService reisdocumenten, ReisdocumentType type, BasePLExt pl) {

    boolean is18jaar = getReisdocumentenLeeftijd(pl) >= 18;
    boolean verstreken = isNieuweReisdocumentenRegelsVanToepassing(reisdocumenten);

    if (type.isDocument(TWEEDE_ZAKENPASPOORT, TWEEDE_NATIONAAL_PASPOORT)) {
      return JAAR_TWEEDE_PASPOORT;
    }

    if (type.isDocument(VREEMDELINGEN_PASPOORT, VLUCHTELINGEN_PASPOORT)) {
      if (aval(pl.getVerblijfstitel().getVerblijfstitel().getVal()) == 26) {
        return JAAR_VLUCHT_PP_VBT_26;
      }

      return JAAR_VLUCHT_PP;
    }

    return (verstreken && is18jaar) ? JAAR_REISDOC_VOLWASSEN : JAAR_REISDOC_MINDERJARIG;
  }

  public static int getGeldigJaar(ReisdocumentType type, int leeftijd) {

    if (type.isDocument(TWEEDE_ZAKENPASPOORT, TWEEDE_NATIONAAL_PASPOORT)) {
      return JAAR_TWEEDE_PASPOORT;
    }

    if (type.isDocument(VREEMDELINGEN_PASPOORT, VLUCHTELINGEN_PASPOORT)) {
      return JAAR_VLUCHT_PP;
    }

    return leeftijd >= 18 ? JAAR_REISDOC_VOLWASSEN : JAAR_REISDOC_MINDERJARIG;
  }

  public static List<FieldValue> getGeldigTermijnen(ReisdocumentService reisdocumenten, ReisdocumentType type,
      BasePLExt pl) {

    List<FieldValue> termijnen = new ArrayList<>();
    int jaar = getGeldigJaar(reisdocumenten, type, pl);

    if (isToonJaar(reisdocumenten, type, pl)) {
      termijnen.add(new FieldValue(jaar, jaar + " jaar"));
    }

    termijnen.add(new FieldValue(0, "anders"));

    return termijnen;
  }

  public static String getInhoudingOmschrijving(BasePLRec record) {

    String sAanduiding = record.getElemVal(
        GBAElem.AAND_INH_VERMIS_NL_REISDOC).getCode();
    BasePLValue dInhouding = record.getElemVal(
        GBAElem.DATUM_INH_VERMIS_NL_REISDOC);

    switch (InhoudingType.get(sAanduiding)) {
      case INHOUDING:
        return "Ingehouden op " + dInhouding.getDescr();

      case VERMISSING:
        return "Vermist op " + dInhouding.getDescr();

      case VAN_RECHTSWEGE_VERVALLEN:
        return "Van rechtswege vervallen op " + dInhouding.getDescr();

      case ONBEKEND:
        return "Onbekend op " + dInhouding.getDescr();

      default:
        break;
    }

    return "";
  }

  /**
   * De leeftijd volgens de reisdocumentmethode. Dus de laatste van de maand / jaar
   */
  public static int getReisdocumentenLeeftijd(BasePLExt pl) {

    String dGeb = pl.getPersoon().getGeboorte().getGeboortedatum().getCode();
    String dOverl = pl.getOverlijding().getElem(GBACat.OVERL,
        GBAElem.DATUM_OVERL).getVal();

    if (!pos(dGeb.replaceAll("\\D", ""))) {
      return 0;
    }

    int forceFormatType = dGeb.contains("-") ? ProcuraDate.FORMATDATE_ONLY : ProcuraDate.SYSTEMDATE_ONLY;
    ProcuraDate pd = new ProcuraDate(dGeb).setAllowedFormatExceptions(true).setForceFormatType(forceFormatType);
    int d_in = aval(pd.getSystemDate());

    if (!pos(pd.getMonth())) {
      d_in = aval(pd.getYear() + "1231");
    } else if (!pos(pd.getDay())) {
      d_in = aval(pd.getYear() + pd.getMonth() + "32");
    }

    int d_sys = aval(pos(dOverl) ? new ProcuraDate(dOverl).getSystemDate() : new ProcuraDate().getSystemDate());
    return (d_sys - d_in) / 10000;
  }

  public static boolean isAanvraagbaar(BasePLExt pl, ReisdocumentType rd, Services db) {

    if (isReedsAangevraagd(pl, rd, db)) {
      return false;
    }

    Cat10VbtExt vbt = pl.getVerblijfstitel();
    boolean heeftVreemdelijkeVbt = vbt.isVerblijfstitelCode(21, 22, 23, 24, 25, 28, 29, 30, 35, 36, 37, 38);
    boolean heeftVluchtelingVbt = vbt.isVerblijfstitelCode(26, 27);
    boolean isEuOnderdaan = vbt.isVerblijfstitelCode(40);

    // Als staatloos is dan mag alleen PB gekozen worden
    if (pl.getNatio().isStaatloos() && (rd != VREEMDELINGEN_PASPOORT) && !heeftVluchtelingVbt) {
      return false;
    }

    if ((rd == EERSTE_NATIONAAL_PASPOORT) && pl.getNatio().isNietNederlander()) {
      return false;
    }

    if ((rd == NEDERLANDSE_IDENTITEITSKAART) && pl.getNatio().isNietNederlander()) {
      return false;
    }

    if ((rd == EERSTE_ZAKENPASPOORT) && pl.getNatio().isNietNederlander()) {
      return false;
    }

    if ((rd == VREEMDELINGEN_PASPOORT) && pl.getNatio().isNederlander()) {
      return false;
    }

    if (isEuOnderdaan && (rd == VREEMDELINGEN_PASPOORT || rd == VLUCHTELINGEN_PASPOORT)) {
      return false;
    }

    if ((rd == VREEMDELINGEN_PASPOORT) && !(pl.getNatio().isBehandeldAlsNederlander() || heeftVreemdelijkeVbt)) {
      return false;
    }

    if ((rd == VLUCHTELINGEN_PASPOORT) && !(pl.getNatio().isNietNederlander() && heeftVluchtelingVbt)) {
      return false;
    }

    if ((rd == TWEEDE_ZAKENPASPOORT) && pl.getNatio().isNietNederlander()) {
      return false;
    }

    if ((rd == TWEEDE_ZAKENPASPOORT) && !isTweedeReisdocMogelijk(pl, db)) {
      return false;
    }

    if ((rd == TWEEDE_NATIONAAL_PASPOORT) && pl.getNatio().isNietNederlander()) {
      return false;
    }

    if ((rd == TWEEDE_NATIONAAL_PASPOORT) && !isTweedeReisdocMogelijk(pl, db)) {
      return false;
    }

    return (rd != FACILITEITEN_PASPOORT) || pl.getNatio().isBehandeldAlsNederlander();
  }

  public static boolean isGereduceerdTarief(ReisdocumentService reisdocumenten, BasePLExt pl, ReisdocumentType type) {
    return getGeldigJaar(reisdocumenten, type, pl) == 5;
  }

  public static boolean isJeugdTarief(BasePLExt pl, ReisdocumentType type) {
    return (pl.getPersoon().getGeboorte().getLeeftijd() < 14) && (type == NEDERLANDSE_IDENTITEITSKAART);
  }

  /**
   * Zijn de nieuwe regels van 09-03-2014 van toepassing
   */
  public static boolean isNieuweReisdocumentenRegelsVanToepassing(ReisdocumentService reisdocumenten) {
    String datumIngang = str2date(reisdocumenten.getParm(ParameterConstant.REISD_TERMIJN_WIJZIGING));
    return pos(datumIngang) && aval(new ProcuraDate().diffInDays(astr(datumIngang))) <= 0;
  }

  public static boolean isToonJaar(ReisdocumentService reisdocumenten, ReisdocumentType type, BasePLExt pl) {
    return getGeldigJaar(reisdocumenten, type, pl) != JAAR_VLUCHT_PP_VBT_26;
  }

  public static void setDocumentAanvraagToestemming(BasePLExt pl, boolean leeftijdToestemming, Toestemming t,
      Services db) {

    BasePLExt ouder1Pl = GezagUtils.getOuder(pl, ToestemmingType.OUDER_1.getCode(), db);
    BasePLExt ouder2Pl = GezagUtils.getOuder(pl, ToestemmingType.OUDER_2.getCode(), db);

    BasePLExt ouderPl = ouder1Pl;
    BasePLExt partnerPl = ouder2Pl;

    if (ToestemmingType.OUDER_2.equals(t.getType())) {
      ouderPl = ouder2Pl;
      partnerPl = ouder1Pl;
    }

    switch (t.getType()) {

      case OUDER_1:
      case OUDER_2:
        if (ouderPl.getLatestRec(PERSOON).hasElems()) {
          t.setAnummer(ouderPl.getPersoon().getAnr().getVal());
          t.setNaam(ouderPl.getPersoon().getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf());
        } else {
          Naam naam = pl.getOuders().getOuder(t.getType().getCode()).getNaam();
          if (".".equals(naam.getGeslachtsnaam().getValue().getVal())) {
            t.setNaam("Onbekend (standaardwaarde)");
          } else {
            t.setNaam(naam.getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf());
          }
        }

        if (leeftijdToestemming) {
          GezagAfleiding gafleiding = GezagUtils.getGezagsStatusOuder(pl, ouderPl, partnerPl,
              t.getConstateringen(),
              t.getType().getCode());
          GezagStatus gs = gafleiding.getGezagStatus();
          t.setGezagStatus(gs);
        } else {
          t.getConstateringen().getAanvrager().add("is wettelijke leeftijd voor toestemming gepasseerd");
        }

        break;

      case DERDE:
      case PARTNER:
        if (pl.getGezag().heeftGezagDerden()) {
          t.setGezagStatus(GezagStatus.JA);
          t.getConstateringen().getAanvrager().add("heeft gezag derden");
        } else {
          t.getConstateringen().getAanvrager().add("heeft geen gezag derden");
        }
        break;

      case CURATOR:
        if (pl.getGezag().staatOnderCuratele()) {
          t.setGezagStatus(GezagStatus.JA);
          t.getConstateringen().getAanvrager().add("staat onder curatele");
        } else {
          t.getConstateringen().getAanvrager().add("staat niet onder curatele");
        }

        break;

      default:
        break;
    }

    if (emp(t.getNaam()) && GezagStatus.NEE.equals(t.getGezagStatus())) {
      t.setToelichting("Niet van toepassing");
    }
  }

  private static boolean isMaandenGeldigheid(long dEnd, int aantalMaanden) {
    return pos(dEnd) && (new ProcuraDate().addMonths(aantalMaanden).diffInDays(astr(dEnd)) >= 0);
  }

  /**
   * Heeft al een nationaal paspoort aanvraag
   */
  private static boolean isReedsAangevraagd(BasePLExt pl, ReisdocumentType rd, Services db) {

    ReisdocumentService reisdocumenten = db.getReisdocumentService();

    for (Zaak zaak : reisdocumenten.getMinimalZaken(new ZaakArgumenten(pl))) {

      ReisdocumentAanvraag a = (ReisdocumentAanvraag) zaak;

      if (a.getStatus().isKleinerDan(ZaakStatusType.VERWERKT)) {

        // Er kunnen niet 2 dezelfde documenten worden aangevraagd
        if (a.getReisdocumentType().isDocument(rd)) {
          return true;
        }

        // Een document kan niet worden aangevraagd bij een specifiek oververwerkte aanvraag.
        if (rd.isDocument(EERSTE_NATIONAAL_PASPOORT, EERSTE_ZAKENPASPOORT)) {
          if (a.getReisdocumentType().isDocument(EERSTE_NATIONAAL_PASPOORT, EERSTE_ZAKENPASPOORT)) {
            return true;
          }
        }

        if (rd.isDocument(TWEEDE_NATIONAAL_PASPOORT, TWEEDE_ZAKENPASPOORT)) {
          if (a.getReisdocumentType().isDocument(TWEEDE_NATIONAAL_PASPOORT, TWEEDE_ZAKENPASPOORT)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private static boolean isTweedeReisdocMogelijk(BasePLExt pl, Services db) {

    // PROBEV
    DocumentInhoudingenService inhoudingen = db.getDocumentInhoudingenService();
    ReisdocumentService reisdocumenten = db.getReisdocumentService();

    for (Reisdocument document : inhoudingen.getReisdocumentHistorie(pl)) {
      ReisdocumentType nDoc = ReisdocumentType.get(document.getNederlandsReisdocument().getVal());
      long dEnd = along(document.getDatumEindeGeldigheid().getVal());
      String inh = astr(document.getAanduidingInhoudingVermissing().getVal());

      if (nDoc.isDocument(EERSTE_NATIONAAL_PASPOORT, EERSTE_ZAKENPASPOORT)) {
        if (!inhoudingen.isReisdocumentIngehouden(pl, document) && isMaandenGeldigheid(dEnd, 6) && emp(inh)) {
          return true;
        }
      }
    }

    // Proweb
    ZaakArgumenten za = new ZaakArgumenten(pl, OPGENOMEN, INBEHANDELING);
    for (Zaak zaak : reisdocumenten.getMinimalZaken(za)) {
      if (zaak instanceof ReisdocumentAanvraag) {

        ReisdocumentAanvraag aanvraag = (ReisdocumentAanvraag) zaak;
        ReisdocumentType nDoc = aanvraag.getReisdocumentType();

        if (nDoc.isDocument(EERSTE_NATIONAAL_PASPOORT, EERSTE_ZAKENPASPOORT)) {
          return true;
        }
      }
    }

    return false;
  }
}
