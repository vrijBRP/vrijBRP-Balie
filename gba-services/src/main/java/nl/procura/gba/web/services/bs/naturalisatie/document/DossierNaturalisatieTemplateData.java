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

package nl.procura.gba.web.services.bs.naturalisatie.document;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AANGEVER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_PARTNER;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierNaturalisatieTemplateData extends DocumentTemplateData {

  public DossierNaturalisatieTemplateData(
      DossierNaturalisatie dossier,
      DossierPersoon aanschrijfpersoon,
      Ceremonie ceremonie) {

    put("zaak", new ZaakData(dossier));
    put("aanschrijfpersoon", getVerzoekerData(dossier, aanschrijfpersoon));
    put("aangever", getVerzoekerData(dossier, dossier.getAangever()));
    put("ceremonie", ceremonie);
    put("procedurekeuze", new Procedurekeuze(dossier));
    put("basis", new Basis(aanschrijfpersoon, dossier));
    put("toetsing", new Toetsing(dossier));
    put("behandeling", new Behandeling(dossier));
  }

  private static class VerzoekerData extends DocumentTemplateData {

    public VerzoekerData(DossierPersoon persoon, DossierNaturalisatieVerzoeker gegevens) {
      put("persoon", persoon);
      put("aangever", persoon.getDossierPersoonType().is(AANGEVER));
      put("partner", persoon.getDossierPersoonType().is(MEDE_VERZOEKER_PARTNER));
      put("kind", persoon.getDossierPersoonType().is(MEDE_VERZOEKER_KIND));

      if (gegevens != null) {
        put("toestemminggever", gegevens.getToestemminggever());

        // Naamsvaststelling
        put("vnummer", gegevens.getVnr());
        put("geslachtsnaamVastgesteld", gegevens.getNaamstGesl());
        put("voornamenVastgesteld", gegevens.getNaamstVoorn());
        put("geslachtsnaamGewijzigd", gegevens.getNaamstGeslGew());
        put("voornamenGewijzigd", gegevens.getNaamstVoornGew());

        // Behandeling
        put("andereOuderAkkoord", toFieldValue(gegevens.getAndereOuderAkkoordType()));
        put("advies", toFieldValue(gegevens.getAdviesBurgermeesterType()));
        put("datumKoninklijkBesluit", toFieldValue(gegevens.getBehDKoningBesluit()));
        put("nummerKoninklijkBesluit", gegevens.getBehNrKoningBesluit());
        put("beslissing", toFieldValue(gegevens.getBeslissingType()));
        put("datumBevestiging", toFieldValue(gegevens.getBehDBevest()));
        put("ceremonies", new Ceremonies(gegevens));
      }
    }
  }

  private static class ZaakData extends DocumentTemplateData {

    public ZaakData(DossierNaturalisatie d) {
      put("zaakId", d.getDossier().getZaakId());
      put("datumTijdInvoer", d.getDossier().getDatumTijdInvoer());
      put("ingevoerdDoor", d.getDossier().getIngevoerdDoor());
      put("locatieInvoer", d.getDossier().getLocatieInvoer());
    }
  }

  private static class Procedurekeuze extends DocumentTemplateData {

    public Procedurekeuze(DossierNaturalisatie d) {
      put("aangever", d.getAangever());
      put("bevoegdTotIndienenVerzoek", d.getBevoegdTotVerzoekType());
      put("toelichting1", d.getBevoegdIndienenToel());
      put("optie", toFieldValue(d.getOptie()));
      put("toelichting2", d.getOptieToel());
    }
  }

  private static class Basis extends DocumentTemplateData {

    public Basis(DossierPersoon aanschrijfpersoon, DossierNaturalisatie dossier) {
      put("basisVerzoek", dossier.getBasisVerzoekType());
      put("vertegenwoordiger", dossier.getVertegenwoordiger());
      put("dossiernr", dossier.getDossiernr());
      put("verzoeken", new Verzoeken(aanschrijfpersoon, dossier));
      put("partners", dossier.getDossier().getPersonen(MEDE_VERZOEKER_PARTNER));
      put("kinderen", dossier.getDossier().getPersonen(MEDE_VERZOEKER_KIND));
    }

    private static class Verzoeken extends DocumentTemplateData {

      public Verzoeken(DossierPersoon aanschrijfpersoon, DossierNaturalisatie dossier) {
        put("verzoekers", getVerzoekers(dossier, AANGEVER, MEDE_VERZOEKER_PARTNER, MEDE_VERZOEKER_KIND));
        put("partners", getVerzoekers(dossier, MEDE_VERZOEKER_PARTNER));
        put("kinderen", getVerzoekers(dossier, MEDE_VERZOEKER_KIND));
        put("kinderenVanAanschrijfpersoon", filterByPersoon(aanschrijfpersoon, dossier));
      }

      private List<VerzoekerData> filterByPersoon(DossierPersoon aanschrijfpersoon, DossierNaturalisatie dossier) {
        return dossier.getDossier().getPersonen(MEDE_VERZOEKER_KIND)
            .stream()
            .map(kind -> getKindByAanschrijfPersoon(dossier, aanschrijfpersoon, kind))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
      }

      private static VerzoekerData getKindByAanschrijfPersoon(DossierNaturalisatie dossier,
          DossierPersoon aanschrijfpersoon,
          DossierPersoon kind) {
        DossierNaturalisatieVerzoeker gegevens = dossier.getVerzoekerGegevens(kind.getBurgerServiceNummer());
        return gegevens.getBsnToestemminggever()
            .equals(aanschrijfpersoon.getBurgerServiceNummer())
                ? new VerzoekerData(kind, gegevens)
                : null;
      }

      private List<VerzoekerData> getVerzoekers(DossierNaturalisatie dossier, DossierPersoonType... type) {
        return dossier.getDossier().getPersonen(type)
            .stream()
            .map(verzoeker -> getVerzoekerData(dossier, verzoeker))
            .collect(Collectors.toList());
      }
    }
  }

  private static VerzoekerData getVerzoekerData(DossierNaturalisatie dossier, DossierPersoon persoon) {
    BsnFieldValue bsn = persoon.getBurgerServiceNummer();
    return new VerzoekerData(persoon, dossier.getVerzoekerGegevens(bsn));
  }

  private static class Toetsing extends DocumentTemplateData {

    public Toetsing(DossierNaturalisatie dossier) {
      put("verklaringVerblijf", toFieldValue(dossier.getToetsverklOndertekend()));
      put("bereidAfleggenVerklaring", toFieldValue(dossier.getToetsBereidVerkl()));
      put("betrokkeneBekendMetBetaling", toFieldValue(dossier.getToetsBetrokkBekend()));
      put("bereidAfstandNationaliteit", dossier.getBereidAfstandType());
      put("bewijsVanIdentiteit", toFieldValue(dossier.getToetsBewijsIdAanw()));
      put("bewijsVanNationaliteit", dossier.getBewijsBewijsNationaliteitType());
      put("bewijsnoodAangetoond", toFieldValue(dossier.getToetsBewijsnood()));
      put("bewijsnoodAangetoondToel", dossier.getToetsBewijsnoodToel());
      put("geldigeVerblijfsvergunning", dossier.getGeldigeVerblijfsvergunningType());

      // Naamvaststelling
      put("naamsvaststellingOfWijziging", dossier.getNaamVaststellingType());
      put("geslachtsnaamGewijzigdToel", dossier.getNaamstGeslGewToel());
    }
  }

  private static class Behandeling extends DocumentTemplateData {

    public Behandeling(DossierNaturalisatie d) {
      put("berichtOmtrentToelating", toFieldValue(d.getBehBotOpgevraagd()));
      put("minderjarigeKinderen12", toFieldValue(d.getKinderen12AkkoordType()));
      put("minderjarigeKinderen16", toFieldValue(d.getBehMinderjKind2()));
      put("toelichting1", d.getBehAndereVertToel());
      put("informatieJustis", toFieldValue(d.getBehOpgevrJustis()));
      put("datumAanvraag", toFieldValue(d.getBehDAanvr()));
      put("eindeTermijn", toFieldValue(d.getBehTermDEnd()));
      put("toelichting2", toFieldValue(d.getToetsverklOndertekend()));
    }
  }

  private static class Ceremonies extends DocumentTemplateData {

    public Ceremonies(DossierNaturalisatieVerzoeker verzoeker) {
      put("ceremonie1", new Ceremonie(1, verzoeker.getCeremonie1DIn(),
          verzoeker.getCeremonie1TIn(), verzoeker.getCeremonie1Bijgewoond()));

      put("ceremonie2", new Ceremonie(2, verzoeker.getCeremonie2DIn(),
          verzoeker.getCeremonie2TIn(), verzoeker.getCeremonie2Bijgewoond()));

      put("ceremonie3", new Ceremonie(3, verzoeker.getCeremonie3DIn(),
          verzoeker.getCeremonie3TIn(), verzoeker.getCeremonie3Bijgewoond()));

      put("datumUitgereikt", new DateTime(verzoeker.getCeremonieDUitreik()));
      put("vervaldatum", toFieldValue(verzoeker.getCeremonieDVerval()));
    }
  }

  private static FieldValue toFieldValue(EnumWithCode<?> value) {
    if (value != null) {
      return new FieldValue(value.getCode(), value.toString());
    }
    return new FieldValue();
  }

  private static DateTime toFieldValue(Date value) {
    return new DateTime(value);
  }

  private static FieldValue toFieldValue(Boolean value) {
    if (value != null) {
      return new FieldValue(value, value ? "Ja" : "Nee");
    }
    return new FieldValue();
  }
}
