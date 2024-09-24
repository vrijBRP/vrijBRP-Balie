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

package nl.procura.gba.web.services.bs.onderzoek.document;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;

public class DossierOnderzoekTemplateData extends DocumentTemplateData {

  private DossierOnderzoekTemplateData(DossierOnderzoek onderzoek) {
    put("items", Stream.of(onderzoek).map(DossierOnderzoekData::new).collect(Collectors.toList()));
  }

  public DossierOnderzoekTemplateData(DossierOnderzoek onderzoek, String aanschrijftekst,
      DossierPersoon aanschrijfpersoon) {
    this(onderzoek);
    put("aanschrijfpersoon", aanschrijfpersoon);
    put("aanschrijftekst", aanschrijftekst);
  }

  public DossierOnderzoekTemplateData(DossierOnderzoek onderzoek, String aanschrijftekst, DossierOnderzoekBron bron) {
    this(onderzoek);
    put("bron", new AanschrijfBron(bron));
    put("aanschrijftekst", aanschrijftekst);
  }

  private class AanschrijfBron extends DocumentTemplateData {

    public AanschrijfBron(DossierOnderzoekBron bron) {
      put("bron", bron.getBron());
      put("gesprek", bron.getGesprek());
      put("instantie", bron.getInst());
      put("afdeling", bron.getInstAfdeling());
      put("aanhef", bron.getInstAanhef());
      put("voorl", bron.getInstVoorl());
      put("email", bron.getInstEmail());
      put("naam", bron.getInstNaam());
      put("tav", bron.getInstTav());
      put("adres", bron.getAdres());
    }
  }

  private class DossierOnderzoekData extends DocumentTemplateData {

    public DossierOnderzoekData(DossierOnderzoek p) {
      put("zaak", new ZaakData(p));
      put("aanleiding", new AanleidingData(p));
      put("betreft", new BetreftData(p));
      put("beoordeling", new BeoordelingData(p));
      put("uitbreiding", new UitbreidingData(p));
      put("resultaat", new ResultaatData(p));
      put("aanschrijving", new AanschrijvingData(p));
    }
  }

  private class ZaakData extends DocumentTemplateData {

    public ZaakData(DossierOnderzoek p) {
      put("zaakId", p.getDossier().getZaakId());
      put("datumTijdInvoer", p.getDossier().getDatumTijdInvoer());
      put("ingevoerdDoor", p.getDossier().getIngevoerdDoor());
      put("locatieInvoer", p.getDossier().getLocatieInvoer());
    }
  }

  private class AanleidingData extends DocumentTemplateData {

    public AanleidingData(DossierOnderzoek p) {
      put("bron", p.getOnderzoekBron());
      put("relatie", p.getAanlRelatie());
      put("kenmerk", p.getAanlKenmerk());
      put("toelichting", p.getAanlKenmerk());
      put("burger", p.getAangever());
      put("tmv", new TmvData(p));
      put("instantie", new InstantieData(p));
      put("ambtshalve", new AmbtshalveData(p));
      put("melding", new MeldingData(p));

      Adresformats adresformats = new Adresformats().setValues(p.getAanleidingAdres().getDescription(),
          p.getAanleidingHnr(), p.getAanleidingHnrL(),
          p.getAanleidingHnrT(),
          p.getAanleidingHnrA().getDescription(), "",
          p.getAanleidingPc().getDescription(), "",
          p.getAanleidingPlaats().getDescription(),
          p.getAanleidingGemeente().getDescription(), "",
          p.getAanleidingLand().getDescription(), "",
          p.getAanleidingBuitenl1(),
          p.getAanleidingBuitenl2(),
          p.getAanleidingBuitenl3());
      put("adres", adresformats);
      put("gemeenteadres", p.getVermoedelijkeGemeentePostbus());
    }
  }

  private class TmvData extends DocumentTemplateData {

    public TmvData(DossierOnderzoek p) {
      put("nr", p.getAanlTmvNr());
    }
  }

  private class InstantieData extends DocumentTemplateData {

    public InstantieData(DossierOnderzoek p) {
      put("instantie", p.getAanlInst());
      put("aanhef", p.getAanlInstAanhef());
      put("voorl", p.getAanlInstVoorl());
      put("naam", p.getAanlInstNaam());
      put("tav", trim(p.getAanlInstAanhef() + " " + p.getAanlInstVoorl() + " " + p.getAanlInstNaam()));
      put("adres", p.getAanlInstAdres());
      put("pc", p.getAanlInstPc());
      put("plaats", p.getAanlInstPlaats());
    }
  }

  private class AmbtshalveData extends DocumentTemplateData {

    public AmbtshalveData(DossierOnderzoek p) {
      put("afdeling", p.getAanlAfdeling());
    }
  }

  private class MeldingData extends DocumentTemplateData {

    public MeldingData(DossierOnderzoek p) {
      put("datumOntvangst", p.getDatumOntvangstMelding());
      put("aard", trim(p.getOnderzoekAard() +
          (fil(p.getOnderzoekAardAnders()) ? (": " + p.getOnderzoekAardAnders()) : "")));
      put("vermoedAdres", p.getVermoedelijkAdres());
    }
  }

  private class BetreftData extends DocumentTemplateData {

    public BetreftData(DossierOnderzoek p) {
      put("personen", p.getDossier().getPersonen(DossierPersoonType.BETROKKENE));
      put("adres", getAdresData(p));
    }
  }

  public Adresformats getAdresData(DossierOnderzoek o) {
    return o.getDossier()
        .getPersonen(DossierPersoonType.BETROKKENE)
        .stream()
        .findFirst()
        .map(DossierPersoon::getAdres)
        .orElse(null);
  }

  private class BeoordelingData extends DocumentTemplateData {

    public BeoordelingData(DossierOnderzoek p) {
      put("datumOntvangst", p.getDatumOntvangstMelding());
      put("datumEinde", p.getDatumEindeTermijn());
      put("binnenTermijn", getBooleanValue(p.getBinnenTermijn()));
      put("redenTermijn", p.getRedenTermijn());
      put("onderzoekDatumAanvang", p.getDatumAanvangOnderzoek());
      put("onderzoekAanduidingGegevens", p.getAanduidingGegevensOnderzoek());
      put("gedegenOnderzoek", getBooleanValue(p.getGedegenOnderzoek()));
      put("redenOverslaan", getBooleanValue(p.getRedenOverslaan()));
      put("toelichtingOverslaan", p.getToelichtingOverslaan());
    }
  }

  private class UitbreidingData extends DocumentTemplateData {

    public UitbreidingData(DossierOnderzoek p) {
      putData("fase1", e -> {
        e.put("datumIngang", p.getFase1DatumIngang());
        e.put("datumEinde", p.getFase1DatumEinde());
        e.put("reactie", getBooleanValue(p.getFase1Reactie()));
        e.put("toelichting", p.getFase1Toelichting());
        e.put("vervolgActies", getBooleanValue(p.getFase1Vervolg()));
      });
      putData("fase2", e -> {
        e.put("datumIngang", p.getFase2DatumIngang());
        e.put("datumEinde", p.getFase2DatumEinde());
        e.put("onderzoekGewenst", getBooleanValue(p.getFase2OnderzoekGewenst()));
        e.put("datumOnderzoek", p.getFase2DatumOnderzoek());
        e.put("toelichting", p.getFase2Toelichting());
        e.put("bronnen", getBronnen(p));
      });
    }

    private List<Bron> getBronnen(DossierOnderzoek p) {
      List<Bron> out = new ArrayList<>();
      for (DossierOnderzoekBron bron : p.getBronnen()) {
        out.add(new Bron(bron));
      }
      return out;
    }
  }

  private class Bron extends DocumentTemplateData {

    public Bron(DossierOnderzoekBron bron) {
      put("bron", bron.getBron());
      put("gesprek", bron.getGesprek());
    }
  }

  private class ResultaatData extends DocumentTemplateData {

    public ResultaatData(DossierOnderzoek p) {
      put("betrokkene", p.getResultaatOnderzoekBetrokkene());
      put("datumEinde", p.getDatumEindeOnderzoek());
      put("nogmaalsAanschrijven", getBooleanValue(p.getNogmaalsAanschrijven()));

      Adresformats adresformats = new Adresformats().setValues(p.getResultaatAdres().getDescription(),
          p.getResultaatHnr(), p.getResultaatHnrL(),
          p.getResultaatHnrT(),
          p.getResultaatHnrA().getDescription(), "",
          p.getResultaatPc().getDescription(), "",
          p.getResultaatPlaats().getDescription(),
          p.getResultaatGemeente().getDescription(), "",
          p.getResultaatLand().getDescription(), "",
          p.getResultaatBuitenl1(), p.getResultaatBuitenl2(),
          p.getResultaatBuitenl3());
      put("adres", adresformats);
      put("gemeenteadres", p.getResultaatGemeentePostbus());
      put("toelichting", p.getResultaatToelichting());
    }
  }

  private class AanschrijvingData extends DocumentTemplateData {

    public AanschrijvingData(DossierOnderzoek p) {
      put("soort", p.getAanschrijvingFase());
      put("fase1", new TermijnData(p.getAanschrDatumInFase1(), p.getAanschrDatumEindFase1()));
      put("fase2", new TermijnData(p.getAanschrDatumInFase2(), p.getAanschrDatumEindFase2()));
      put("extra", new TermijnData(p.getAanschrDatumInExtra(), p.getAanschrDatumEindExtra()));
      put("voornemen", new TermijnData(p.getAanschrDatumInVoornemen(), p.getAanschrDatumEindVoornemen()));
      put("besluit", p.getAanschrDatumBesluit());
    }
  }

  private class TermijnData extends DocumentTemplateData {

    public TermijnData(DateTime datumIngang, DateTime datumEinde) {
      super();
      put("van", datumIngang);
      put("tm", datumEinde);
    }
  }
}
