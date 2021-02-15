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
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentStatus;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemming;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemmingen;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.Clausules;

/**
 * Maakt samenvatting van gegevens over een specifieke rijbewijsaanvraag
 */
public class ReisdocumentSamenvatting extends ZaakSamenvattingTemplate<ReisdocumentAanvraag> {

  public ReisdocumentSamenvatting(ZaakSamenvatting zaakSamenvatting, ReisdocumentAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(ReisdocumentAanvraag zaak) {
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
  public void addZaakItems(ReisdocumentAanvraag zaak) {
    setReisdocument(zaak);
    setHuidigeStatus(zaak);
    setToestemmingen(zaak);
    setClausules(zaak);
  }

  private void setToestemmingen(ReisdocumentAanvraag zaak) {

    Toestemmingen toestemmingen = zaak.getToestemmingen();
    ZaakItemRubriek rubriek = addRubriek("Toestemmingen");
    for (Toestemming toestemming : toestemmingen.getGegevenToestemmingen()) {
      rubriek.add(toestemming.getType().getOms(), toestemming.getTekstNaam());
    }
  }

  private void setClausules(ReisdocumentAanvraag zaak) {

    Clausules c = zaak.getClausules();
    ZaakItemRubriek rubriek = addRubriek("Clausules");
    rubriek.add("Vermelding partner", c.getVermeldingPartner());
    rubriek.add("Pseudoniem", c.getPseudoniem());
    rubriek.add("Ondertekening", c.getOndertekening().getOms());
    rubriek.add("Uitgezonderde landen", c.getUitzonderingLanden());
    rubriek.add("Geldig voor reizen", c.getGeldigVoorReizen());
    rubriek.add("Staatloos", c.getStaatloos().getOms());
    rubriek.add("Ter vervanging van", c.getTvv());
  }

  private void setHuidigeStatus(ReisdocumentAanvraag zaak) {

    ReisdocumentStatus status = zaak.getReisdocumentStatus();

    String aflevering = status.getStatusLevering().toString();
    String afsluiting = status.getStatusAfsluiting().toString();

    if (pos(status.getDatumTijdLevering().getLongDate())) {
      aflevering += ", gemeld op " + status.getDatumTijdLevering();
    }

    if (pos(status.getDatumTijdAfsluiting().getLongDate())) {
      afsluiting += ", afgesloten op " + status.getDatumTijdAfsluiting();
    }

    ZaakItemRubriek rubriek = addRubriek("Huidige status reisdocument en aanvraag");
    rubriek.add("Document", fil(status.getNrNederlandsDocument()) ? status.getNrNederlandsDocument() : "Onbekend");
    rubriek.add("Aflevering", aflevering);
    rubriek.add("Afsluiting", afsluiting);
  }

  private void setReisdocument(ReisdocumentAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Aanvraag reisdocument");
    rubriek.add("Reisdocument", zaak.getReisdocumentType().getOms());
    rubriek.add("Aanvraagnummer", zaak.getAanvraagnummer().getFormatNummer());
    rubriek.add("Lengte persoon", zaak.getLengte().intValue() > 0 ? (zaak.getLengte() + " cm") : "Niet ingevuld");
    rubriek.add("Toestemming(en)", zaak.getToestemmingen().getOmschrijving());
    rubriek.add("Spoed", zaak.getSpoed().getOms());
    rubriek.add("Signalering", zaak.getSignalering().getOms());
    rubriek.add("Jeugdtarief", zaak.isGratis() ? "Ja" : "Nee");
    rubriek.add("Reden niet aanwezig", zaak.getRedenAfwezig());
    rubriek.add("Geldigheid", zaak.getGeldigheid());
  }
}
