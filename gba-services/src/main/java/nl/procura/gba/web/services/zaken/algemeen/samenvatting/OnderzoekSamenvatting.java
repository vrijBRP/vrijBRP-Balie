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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een onderzoek
 */
public class OnderzoekSamenvatting extends ZaakSamenvattingTemplate<DossierOnderzoek> {

  public OnderzoekSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierOnderzoek zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierOnderzoek zaak) {

    ZaakSamenvatting.Deelzaken deelZaken = addDeelzaken("Betreffende personen");
    List<DossierPersoon> persons = zaak.getDossier().getPersonen(DossierPersoonType.BETROKKENE);
    for (DossierPersoon dp : persons) {
      ZaakSamenvatting.Deelzaak deelZaak = new ZaakSamenvatting.Deelzaak();
      deelZaak.add("Naam", getNormalizedNameWithAge(dp));
      deelZaak.add("BSN", dp.getBurgerServiceNummer());
      deelZaak.add("Adres", dp.getAdres().getAdres_pc_wpl_gem());
      deelZaken.add(deelZaak);
    }
  }

  @Override
  public void addZaakItems(DossierOnderzoek zaak) {
    addAanleidingData(zaak);
    addBeoordelingData(zaak);
    addUitbreidingData(zaak);
    addResultaatData(zaak);
    addAanschrijvingData(zaak);
  }

  private void addAanleidingData(DossierOnderzoek zaak) {
    String naam = zaak.getAangever().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
    String bsn = zaak.getBurgerServiceNummer().getDescription();
    String leeftijd = zaak.getAangever().getGeboorte().getDatum_leeftijd();

    ZaakItemRubriek rubriek = addRubriek("Aanleiding");
    rubriek.add("Bron", zaak.getOnderzoekBron());
    rubriek.add("Relatie", zaak.getAanlRelatie());

    switch (zaak.getOnderzoekBron()) {
      case BURGER:
        rubriek.add("Burger", naam + " - " + bsn + " - " + leeftijd);
        break;
      case TMV:
        rubriek.add("Kenmerk", zaak.getAanlKenmerk());
        break;
      case AMBTSHALVE:
        rubriek.add("Naam instantie", zaak.getAanlAfdeling());
        rubriek.add("Kenmerk", zaak.getAanlKenmerk());
        break;
      case INSTANTIE:
        addInstantieData(rubriek, zaak);
        break;
      case LAA:
        rubriek.add("Toelichting", zaak.getAanlKenmerk());
        break;
      case ONBEKEND:
        break;
    }

    addMeldingData(zaak);
    addVermoedelijkAdres(zaak);
  }

  private void addInstantieData(ZaakItemRubriek rubriek, DossierOnderzoek zaak) {
    rubriek.add("Instantie", zaak.getAanlInst());
    rubriek.add("T.a.v.", trim(zaak.getAanlInstAanhef()
        + " " + zaak.getAanlInstVoorl() + " " + zaak.getAanlInstNaam()));
    rubriek.add("Adres", zaak.getAanlInstAdres());
    rubriek.add("Postcode / Plaats ", zaak.getAanlInstPc() + " / " + zaak.getAanlInstPlaats());
    rubriek.add("Kenmerk", zaak.getAanlKenmerk());
  }

  private void addMeldingData(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Aanleiding - melding");
    rubriek.add("Datum ontvangst", zaak.getDatumOntvangstMelding());
    rubriek.add("Reden", trim(zaak.getOnderzoekAard() +
        (fil(zaak.getOnderzoekAardAnders()) ? (": " + zaak.getOnderzoekAardAnders()) : "")));
  }

  private void addVermoedelijkAdres(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Aanleiding - vermoedelijk adres");
    rubriek.add("Type adres", zaak.getVermoedelijkAdres());
    Adresformats adresformats = new Adresformats().setValues(zaak.getAanleidingAdres().getDescription(),
        zaak.getAanleidingHnr(), zaak.getAanleidingHnrL(),
        zaak.getAanleidingHnrT(),
        zaak.getAanleidingHnrA().getDescription(), "",
        zaak.getAanleidingPc().getDescription(), "",
        zaak.getAanleidingPlaats().getDescription(),
        zaak.getAanleidingGemeente().getDescription(), "",
        zaak.getAanleidingLand().getDescription(), "",
        zaak.getAanleidingBuitenl1(),
        zaak.getAanleidingBuitenl2(),
        zaak.getAanleidingBuitenl3());

    rubriek.add("Adres", adresformats.getAdres_pc_wpl_gem());

    if (zaak.getVermoedelijkAdres() == VermoedAdresType.ANDERE_GEMEENTE) {
      Gemeente vm = zaak.getVermoedelijkeGemeentePostbus();
      rubriek.add("(Post)adres gemeente",
          vm.getAdres() + " " + vm.getPostcode() + " " + vm.getPlaats() + " " + vm.getGemeente());
    }
  }

  private void addBeoordelingData(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Beoordeling - af te handelen binnen 5 werkdagen?");
    rubriek.add("Datum ontvangst", zaak.getDatumOntvangstMelding());
    rubriek.add("Datum einde", zaak.getDatumEindeTermijn());
    rubriek.add("Binnen termijn", getBooleanValue(zaak.getBinnenTermijn()));
    rubriek.add("Reden termijn", zaak.getRedenTermijn());

    rubriek = addRubriek("Beoordeling - start onderzoek");
    rubriek.add("Onderzoek datum aanvang", zaak.getDatumAanvangOnderzoek());
    rubriek.add("Onderzoek aanduidingGegevens", zaak.getAanduidingGegevensOnderzoek());

    rubriek = addRubriek("Beoordeling - kunnen onderdelen van het onderzoek worden overgeslagen?");
    rubriek.add("Gedegen onderzoek", getBooleanValue(zaak.getGedegenOnderzoek()));
    rubriek.add("Reden overslaan", getBooleanValue(zaak.getRedenOverslaan()));
    rubriek.add("Toelichting overslaan", zaak.getToelichtingOverslaan());
  }

  private void addUitbreidingData(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Uitbreiding - 1e fase / tussentijdse beoordeling");

    rubriek.add("Datum ingang", zaak.getFase1DatumIngang());
    rubriek.add("Datum einde", zaak.getFase1DatumEinde());
    rubriek.add("Reactie", getBooleanValue(zaak.getFase1Reactie()));
    rubriek.add("Toelichting", zaak.getFase1Toelichting());
    rubriek.add("Vervolgacties", getBooleanValue(zaak.getFase1Vervolg()));

    rubriek = addRubriek("Uitbreiding - 2e fase / vervolgactie(s)");
    rubriek.add("Datum ingang", zaak.getFase2DatumIngang());
    rubriek.add("Datum einde", zaak.getFase2DatumEinde());
    rubriek.add("Onderzoek gewenst", getBooleanValue(zaak.getFase2OnderzoekGewenst()));
    rubriek.add("Datum onderzoek", zaak.getFase2DatumOnderzoek());
    rubriek.add("Toelichting", zaak.getFase2Toelichting());

    for (DossierOnderzoekBron bron : zaak.getBronnen()) {
      ZaakItemRubriek bronr = addRubriek("Uitbreiding - externe bron - " + bron.getBron());
      bronr.add("Gesprek", bron.getGesprek());
    }
  }

  private void addResultaatData(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Resultaat onderzoek");
    rubriek.add("Gelijk aan het vermoedelijke adres?", getBooleanValue(zaak.getResAdresGelijk()));
    rubriek.add("Betrokkene(n) is/zijn", zaak.getResultaatOnderzoekBetrokkene());
    rubriek.add("Toelichting", zaak.getResultaatToelichting());

    switch (zaak.getResultaatOnderzoekBetrokkene()) {
      case ONBEKEND:
        break;
      case ZELFDE:
        rubriek.add("Datum einde", zaak.getDatumEindeOnderzoek());
        break;
      case IMMIGRATIE:
      case BINNEN_INTER_WOON:
      case BINNEN_INTER_BRIEF:
        rubriek.add("Nogmaals aanschrijven", getBooleanValue(zaak.getNogmaalsAanschrijven()));
        addResultaatAdres(zaak, "Adresgegevens binnen de gemeente");
        break;
      case NAAR_ANDERE:
        rubriek.add("Nogmaals aanschrijven", getBooleanValue(zaak.getNogmaalsAanschrijven()));
        Gemeente vm = zaak.getResultaatGemeentePostbus();
        ZaakItemRubriek arubriek = addResultaatAdres(zaak, "Adresgegevens in een andere gemeente");
        arubriek.add("(Post)adres gemeente", vm.getAdres() + " " + vm.getPostcode()
            + " " + vm.getPlaats() + " " + vm.getGemeente());
        break;
      case EMIGRATIE:
        rubriek.add("Nogmaals aanschrijven", getBooleanValue(zaak.getNogmaalsAanschrijven()));
        addResultaatAdres(zaak, "Adresgegevens in het buitenland");
        break;
      case NAAR_ONBEKEND:
        break;
    }
  }

  private ZaakItemRubriek addResultaatAdres(DossierOnderzoek zaak, String caption) {

    ZaakItemRubriek rubriek = addRubriek("Resultaat - " + caption);

    Adresformats adresformats = new Adresformats().setValues(zaak.getResultaatAdres().getDescription(),
        zaak.getResultaatHnr(), zaak.getResultaatHnrL(),
        zaak.getResultaatHnrT(),
        zaak.getResultaatHnrA().getDescription(), "",
        zaak.getResultaatPc().getDescription(), "",
        zaak.getResultaatPlaats().getDescription(),
        zaak.getResultaatGemeente().getDescription(), "",
        zaak.getResultaatLand().getDescription(), "",
        zaak.getResultaatBuitenl1(), zaak.getResultaatBuitenl2(),
        zaak.getResultaatBuitenl3());

    rubriek.add("Adres", adresformats.getAdres_pc_wpl());
    return rubriek;
  }

  private void addAanschrijvingData(DossierOnderzoek zaak) {
    ZaakItemRubriek rubriek = addRubriek("Aanschrijving");
    rubriek.add("Soort", zaak.getAanschrijvingFase());
    rubriek.add("Fase 1", getTermijn(zaak.getAanschrDatumInFase1(), zaak.getAanschrDatumEindFase1()));
    rubriek.add("Fase 2", getTermijn(zaak.getAanschrDatumInFase2(), zaak.getAanschrDatumEindFase2()));
    rubriek.add("Extra", getTermijn(zaak.getAanschrDatumInExtra(), zaak.getAanschrDatumEindExtra()));
    rubriek.add("Voornemen", getTermijn(zaak.getAanschrDatumInVoornemen(), zaak.getAanschrDatumEindVoornemen()));
    rubriek.add("Besluit", zaak.getAanschrDatumBesluit());
  }

  private String getTermijn(DateTime datumIngang, DateTime datumEinde) {

    if (fil(datumIngang.toString())) {
      return "Van " + datumIngang + " - t/m " + datumEinde.toString();
    }
    return "";
  }
}
