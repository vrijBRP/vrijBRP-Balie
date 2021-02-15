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

package nl.procura.gba.web.services.gba.presentievraag.document;

import static nl.procura.standard.Globalfunctions.date2str;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.bprbzk.bcgba.v14.BeheerIdenGegVraagDE;
import nl.bprbzk.bcgba.v14.MatchIdenGegAntwoordDE;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch.Onderzoeksgegeven;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;

public class PresentievraagTemplateData extends DocumentTemplateData {

  public PresentievraagTemplateData(List<Presentievraag> presentievragen) {
    put("items", presentievragen.stream().map(PresentievraagData::new).collect(Collectors.toList()));
  }

  private String getRedenOpschorting(String code) {

    String oms = "";
    if (code != null) {
      switch (code) {
        case "O":
          oms = "Overlijden";
          break;

        case "E":
          oms = "Emigratie";
          break;

        case "M":
          oms = "ministrieel besluit";
          break;

        case "R":
          oms = "PL aangelegd in de RNI";
          break;

        default:
          break;
      }
    }
    return oms;
  }

  private class Formats extends DocumentTemplateData {

    public Formats(MatchIdenGegAntwoordDE match) {
      getNaam(match);
      getAdres(match);
      getGeboorte(match);
    }

    private void getAdres(MatchIdenGegAntwoordDE match) {

      String straat = match.getStraatnaam();
      String hnr = match.getHuisnummer();
      String hnrL = match.getHuisletter();
      String hnrT = match.getHuisnummertoevoeging();
      String hnrA = match.getAanduidingBijHuisnummer();
      String loc = match.getLocatiebeschrijving();
      String pc = match.getPostcode();
      String gemDeel = match.getGemeentedeel();
      String wpl = match.getWoonplaatsnaam();
      String gem = match.getGemeenteVanInschrijving();
      String buit1 = match.getRegel1AdresBuitenland();
      String buit2 = match.getRegel2AdresBuitenland();
      String buit3 = match.getRegel3AdresBuitenland();

      put("adres",
          new Adresformats().setValues(straat, hnr, hnrL, hnrT, hnrA, loc, pc, gemDeel, wpl, gem, "", "", "",
              buit1, buit2, buit3));
    }

    private void getGeboorte(MatchIdenGegAntwoordDE match) {
      String datum = date2str(match.getGeboortedatum());
      String plaats = match.getGeboorteplaats();
      String land = match.getGeboorteland();
      put("geboorte", new Geboorteformats().setValues(datum, plaats, land));
    }

    private void getNaam(MatchIdenGegAntwoordDE match) {
      String voornamen = match.getVoornamen();
      String geslachtsnaam = match.getGeslachtsnaam();
      String voorvoegsel = match.getVoorvoegselGeslachtsnaam();
      String titel = match.getAdellijkeTitelPredikaat();
      put("naam", new Naamformats(voornamen, geslachtsnaam, voorvoegsel, titel, "", null));
    }
  }

  private class MatchData extends DocumentTemplateData {

    public MatchData(PresentievraagMatch m) {
      put("score", m.getScore());
      put("registratie", m.getMatch().getRegistratie());
      put("volgnr", m.getMatch().getVolgnummerMatch());
      put("persoon", new PersoonData(m));
      put("onderzoeksgegevens", getOnderzoeksgegevens(m));
    }

    private List<OnderzoeksgegevensData> getOnderzoeksgegevens(PresentievraagMatch m) {
      List<OnderzoeksgegevensData> list = new ArrayList<>();
      for (Onderzoeksgegeven onderzoeksgegevens : m.getOnderzoeksgegevens()) {
        list.add(new OnderzoeksgegevensData(onderzoeksgegevens));
      }
      return list;
    }
  }

  private class MatchesData extends ArrayList<MatchData> {

    public MatchesData(Presentievraag p) {
      PresentievraagAntwoord antwoord = p.getPresentievraagAntwoord();
      if (antwoord != null) {
        for (PresentievraagMatch match : antwoord.getMatches()) {
          add(new MatchData(match));
        }
      }
    }
  }

  private class OnderzoeksgegevensData extends DocumentTemplateData {

    public OnderzoeksgegevensData(PresentievraagMatch.Onderzoeksgegeven o) {
      put("id", o.getId());
      put("datum", date2str(o.getDatum()));
      put("aanduiding", o.getAanduiding());
    }
  }

  private class PersoonData extends DocumentTemplateData {

    public PersoonData(PresentievraagMatch m) {
      put("bsn", m.getMatch().getBsn());
      put("voornamen", m.getMatch().getVoornamen());
      put("voorvoegsel", m.getMatch().getVoorvoegselGeslachtsnaam());
      put("geslachtsnaam", m.getMatch().getGeslachtsnaam());
      put("titel", m.getMatch().getAdellijkeTitelPredikaat());
      put("geboortedatum", date2str(m.getMatch().getGeboortedatum()));
      put("geboorteplaats", m.getMatch().getGeboorteplaats());
      put("geboorteland", m.getMatch().getGeboorteland());
      put("geslachtsaanduiding", Geslacht.get(m.getMatch().getGeslachtsaanduiding()));
      put("datumOverlijden", date2str(m.getMatch().getDatumOverlijden()));
      put("indicatieGeheim", m.getMatch().getIndicatieGeheim());
      put("omschrijvingRedenOpschorting", getRedenOpschorting(m.getMatch().getOmschrijvingRedenOpschorting()));
      put("nationaliteit", m.getMatch().getNationaliteit());
      put("buitenlandsPersoonsnummer", m.getMatch().getBuitenlandsPersoonsnummer());
      put("straat", m.getMatch().getStraatnaam());
      put("huisnummer", m.getMatch().getHuisnummer());
      put("huisletter", m.getMatch().getHuisletter());
      put("huisnummerToevoeging", m.getMatch().getHuisnummertoevoeging());
      put("aanduidingBijHuisnummer", m.getMatch().getAanduidingBijHuisnummer());
      put("locatieBeschrijving", m.getMatch().getLocatiebeschrijving());
      put("postcode", m.getMatch().getPostcode());
      put("gemeentedeel", m.getMatch().getGemeentedeel());
      put("woonplaatsnaam", m.getMatch().getWoonplaatsnaam());
      put("gemeenteVanInschrijving", m.getMatch().getGemeenteVanInschrijving());
      put("adresBuitenland1", m.getMatch().getRegel1AdresBuitenland());
      put("adresBuitenland2", m.getMatch().getRegel2AdresBuitenland());
      put("adresBuitenland3", m.getMatch().getRegel3AdresBuitenland());
      put("formats", new Formats(m.getMatch()));
    }
  }

  private class PresentievraagData extends DocumentTemplateData {

    public PresentievraagData(Presentievraag p) {
      put("zaak", new ZaakData(p));
      put("vraag", new VraagData(p));
      put("resultaat", new ResultaatData(p));
      put("matches", new MatchesData(p));
      put("datumTijdInvoer", p.getDatumTijdInvoer());
      put("ingevoerdDoor", p.getIngevoerdDoor());
      put("locatieInvoer", p.getLocatieInvoer());
      put("toelichting", p.getToelichting());
    }
  }

  private class ResultaatData extends DocumentTemplateData {

    public ResultaatData(Presentievraag p) {
      PresentievraagAntwoord antwoord = p.getPresentievraagAntwoord();
      if (antwoord != null) {
        put("vraag", BcGbaVraagbericht.get(antwoord.getVraagBericht()).getOms());
        put("bericht", antwoord.toVerwerkingString());
        put("resultaat", antwoord.toResultaatString(false));
        put("resultaatPn", antwoord.toResultaatPn(false));
      }
    }
  }

  private class VraagData extends DocumentTemplateData {

    public VraagData(Presentievraag p) {
      PresentievraagAntwoord antwoord = p.getPresentievraagAntwoord();
      if (antwoord != null) {
        BeheerIdenGegVraagDE vraag = antwoord.getGegevensVraag();
        put("voornamen", vraag.getVoornamen());
        put("voorvoegsel", vraag.getVoorvoegselGeslachtsnaam());
        put("geslachtsnaam", vraag.getGeslachtsnaam());
        put("geslachtsaanduiding", Geslacht.get(vraag.getGeslachtsaanduiding()));
        put("datumAanvangAdresBuitenland", date2str(vraag.getDatumAanvangAdresBuitenland()));
        put("buitenlandsPersoonsnummer", vraag.getBuitenlandsPersoonsnummer());
        put("geboortedatum", date2str(vraag.getGeboortedatum()));
        put("geboorteplaats", vraag.getGeboorteplaats());
        put("geboorteland", vraag.getGeboorteland());
        put("gemeenteVanInschrijving", vraag.getGemeenteVanInschrijving());
        put("nationaliteit", vraag.getNationaliteit());
      }
    }
  }

  private class ZaakData extends DocumentTemplateData {

    public ZaakData(Presentievraag p) {
      put("zaakId", p.getZaakId());
      put("zaakOmschrijving", p.getZaakOmschrijving());
    }
  }
}
