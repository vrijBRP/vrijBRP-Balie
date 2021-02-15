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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

/**
 * Maakt samenvatting van gegevens over een specifieke verhuizing
 */
public class VerhuizingSamenvatting extends ZaakSamenvattingTemplate<VerhuisAanvraag> {

  public VerhuizingSamenvatting(ZaakSamenvatting zaakSamenvatting, VerhuisAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  private static String getAangifte(VerhuisToestemminggever toest) {
    String aangifte = "";
    switch (toest.getAangifteStatus()) {

      case GEACCEPTEERD:
        aangifte = "Ja";
        break;

      case GEACCEPTEERD_ZONDER_TOESTEMMING:
        aangifte = "Ja, zonder toestemming verwerken";
        break;

      case NIET_GEACCEPTEERD:
        aangifte = "Nee";
        break;

      case NIET_INGEVULD:
        aangifte = "Nog niet aangegeven";
        break;

      default:
        break;
    }
    return aangifte;
  }

  private static String getBsnHoofdbewoner(VerhuisAanvraag aanvraag) {
    String bsnHoofdbewoner = "";
    BsnFieldValue bsnH = aanvraag.getHoofdbewoner().getBurgerServiceNummer();
    if (bsnH.isCorrect()) {
      bsnHoofdbewoner = bsnH.getDescription();
    } else {
      if (aanvraag.isSprakeVanInwoning()) {
        bsnHoofdbewoner = "Geen hoofdbewoner aangemerkt.";
      }
    }
    return bsnHoofdbewoner;
  }

  private static String getInwoning(VerhuisAanvraag aanvraag) {
    return aanvraag.isSprakeVanInwoning() ? "Ja" : "Nee";
  }

  private static String getToestemming(VerhuisToestemminggever toest) {
    String toestemming = "";
    switch (toest.getToestemmingStatus()) {
      case JA:
        String naam = toest.getAnders();
        if (toest.isNatuurlijkPersoon()) {
          naam = toest.getPersoon().getPersoon().getFormats().getNaam().getNaam_naamgebruik_eerste_voornaam();
        }

        toestemming = "Ja, " + naam;
        break;

      case NEE:
        toestemming = "Nee";
        break;

      case NIET_INGEVULD:
        toestemming = "Niet ingevuld";
        break;

      case NIET_VAN_TOEPASSING:
        toestemming = "Niet van toepassing";
        break;

      default:
        break;
    }
    return toestemming;
  }

  @Override
  public void addDeelzaken(VerhuisAanvraag aanvraag) {

    Deelzaken deelZaken = addDeelzaken("Verhuizende personen");
    for (VerhuisPersoon p : aanvraag.getPersonen()) {
      String naam = p.getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
      String leeftijd = p.getPersoon().getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

      Deelzaak deelZaak = new Deelzaak();
      deelZaak.add("Naam", naam);
      deelZaak.add("BSN", p.getBurgerServiceNummer().getDescription());
      deelZaak.add("Geboren", leeftijd);
      deelZaak.add("Verwerking", p.isGeenVerwerking() ? "Nee" : "Ja");
      deelZaken.add(deelZaak);
    }
  }

  @Override
  public void addZaakItems(VerhuisAanvraag aanvraag) {

    ZaakItemRubriek rubriek;
    if (aanvraag.getTypeVerhuizing() == VerhuisType.EMIGRATIE) {
      rubriek = addRubriek("Emigratiegegevens");
      rubriek.add("Adres", aanvraag.getEmigratie().getAdres());
      rubriek.add("Duur", aanvraag.getEmigratie().getDuur());
    } else {
      rubriek = addRubriek("Nieuw adres");
      rubriek.add("Adres", aanvraag.getNieuwAdres().getAdres().getAdres());
      rubriek.add("Postcode / plaats", aanvraag.getNieuwAdres().getAdres().getPc_wpl_gem());
      rubriek.add("Functie adres", aanvraag.getNieuwAdres().getFunctieAdres());
      rubriek.add("Aantal personen op nieuw adres", aanvraag.getNieuwAdres().getAantalPersonen());
    }

    rubriek = addRubriek("Inwoning");
    rubriek.add("Sprake van inwoning", getInwoning(aanvraag));
    rubriek.add("BSN hoofdbewoner", getBsnHoofdbewoner(aanvraag));
    rubriek.add("Bestemming huidige bewoners", aanvraag.getBestemmingHuidigeBewoners());
    rubriek.add("Toestemming", getToestemming(aanvraag.getToestemminggever()));
    rubriek.add("Aangifte geaccepteerd", getAangifte(aanvraag.getToestemminggever()));
  }
}
