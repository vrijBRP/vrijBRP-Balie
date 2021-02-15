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

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;

/**
 * Maakt samenvatting van gegevens over een specifieke rijbewijsaanvraag
 */
public class RdwSamenvatting extends ZaakSamenvattingTemplate<RijbewijsAanvraag> {

  public RdwSamenvatting(ZaakSamenvatting zaakSamenvatting, RijbewijsAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(RijbewijsAanvraag zaak) {
    Deelzaken deelZaken = addDeelzaken(1);

    DocumentPL dp = zaak.getPersoon();
    String naam = dp.getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
    String leeftijd = dp.getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

    Deelzaak deelZaak = new Deelzaak();
    deelZaak.add("Naam", naam);
    deelZaak.add("BSN", zaak.getBurgerServiceNummer().getDescription());
    deelZaak.add("Geboren", leeftijd);
    deelZaken.add(deelZaak);
  }

  @Override
  public void addZaakItems(RijbewijsAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Aanvraag rijbewijs");
    rubriek.add("Aanvraagnummer", zaak.getAanvraagNummer());
    rubriek.add("Rijbewijsnummer", fil(zaak.getRijbewijsnummer()) ? zaak.getRijbewijsnummer() : "Nog niet bekend");
    rubriek.add("Ras code", zaak.getCodeRas());
    rubriek.add("Soort aanvraag", zaak.getSoortAanvraag());
    rubriek.add("Reden aanvraag", zaak.getRedenAanvraag());
    rubriek.add("Vervangt rijbewijs", zaak.getVervangingsRbwNr());
    rubriek.add("Spoed", zaak.isSpoed() ? "Ja" : "Nee");
    rubriek.add("Proces-verbaal", zaak.getProcesVerbaalVerlies());
    rubriek.add("Naamgebruik", zaak.getNaamgebruik());
    rubriek.add("BRP bestendig", zaak.isGbaBestendig() ? "Ja" : "Nee");
    rubriek.add("185 dagen in NL", zaak.isIndicatie185() ? "Ja" : "Nee");
  }
}
