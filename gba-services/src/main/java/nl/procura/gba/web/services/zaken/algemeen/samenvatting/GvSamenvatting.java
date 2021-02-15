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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;

/**
 * Maakt samenvatting van gegevens over een specifieke gegevensverstrekking
 */
public class GvSamenvatting extends ZaakSamenvattingTemplate<GvAanvraag> {

  public GvSamenvatting(ZaakSamenvatting zaakSamenvatting, GvAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(GvAanvraag zaak) {
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
  public void addZaakItems(GvAanvraag zaak) {
    setVerzoek(zaak);
    setAdressering(zaak);
  }

  private void setAdressering(GvAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Adressering");
    rubriek.add("Informatievrager", zaak.getAanvrager());
    rubriek.add("Ter attentie van", zaak.getTav());
    rubriek.add("Adres", zaak.getAdres());
    rubriek.add("Postcode", zaak.getPostcode());
    rubriek.add("Plaats", zaak.getPlaats());
    rubriek.add("Kenmerk", zaak.getKenmerk());
  }

  private void setVerzoek(GvAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Verzoek tot gegevensverstrekking");
    rubriek.add("Datum ontvangst", zaak.getDatumIngang());
    rubriek.add("Door", zaak.getAanvrager());
    rubriek.add("Grondslag", zaak.getGrondslagType());

    StringBuilder toek = new StringBuilder();
    toek.append(astr(zaak.getToekenningType()));

    if (fil(zaak.getToekenningMotivering())) {
      toek.append(", motivering: ");
      toek.append(zaak.getToekenningMotivering());
    }

    rubriek.add("Toegekend", toek);
  }
}
