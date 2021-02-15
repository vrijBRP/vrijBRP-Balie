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

import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.tmv.*;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

/**
 * Maakt samenvatting van gegevens over een specifieke terugmelding
 */
public class TmvSamenvatting extends ZaakSamenvattingTemplate<TerugmeldingAanvraag> {

  public TmvSamenvatting(ZaakSamenvatting zaakSamenvatting, TerugmeldingAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(TerugmeldingAanvraag zaak) {
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
  public void addZaakItems(TerugmeldingAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Terugmelding");
    rubriek.add("Melding", zaak.getTerugmelding());

    for (TerugmeldingDetail e : zaak.getDetails()) {
      rubriek = addRubriek("Element: " + e.getCat().getDescr() + " - "
          + e.getPleType().getElem().getDescr() + ", set: " + (e.getVolgnr() + 1));
      rubriek.add("Huidig", e.getFormatOrigineel());
      rubriek.add("Nieuw", e.getFormatNieuw());
    }

    setInterneBehandeling(zaak);
    setExterneBehandeling(zaak);
    setAfhandeling(zaak);
    setLandelijkeRegistratie(zaak);
    setReacties(zaak);
  }

  private void setAfhandeling(TerugmeldingAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Afhandeling");
    rubriek.add("Verantwoordelijke", zaak.getVerantwoordelijke());
    rubriek.add("Rappeldatum", new DateFieldValue(zaak.getDatumRappel().getLongDate()));
    rubriek.add("Resultaat", zaak.getResultaat());
    rubriek.add("Status", zaak.getStatus());
  }

  private void setExterneBehandeling(TerugmeldingAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Externe registratie");
    TerugmeldingRegistratie r = zaak.getRegistratieTmv();
    rubriek.add("Melding", r.getToelichtingOmschrijving());
    rubriek.add("Verstuurd door", r.getUsr());

    rubriek.add("Datum aanleg dossier", r.getAanleg());
    rubriek.add("Datum wijziging dossier", r.getWijz());
    rubriek.add("Behandelende gemeente", r.getGemBeh());
    rubriek.add("Datum verwachte afhandeling", r.getVerwAfh());
    rubriek.add("Status dossier", r.getStatus());
    rubriek.add("Resultaatcode onderzoek", r.getOnderzoekResultaat());
    rubriek.add("Toelichting resultaat", r.getToelichtingResultaat());
  }

  private void setInterneBehandeling(TerugmeldingAanvraag zaak) {

    String status = zaak.getStatus().getOms();
    if (zaak.getStatus().isMinimaal(ZaakStatusType.VERWERKT)) {
      if (pos(zaak.getDatumTijdAfhandeling().getLongDate())) {
        status += (" op " + zaak.getDatumTijdAfhandeling().toString());
        if (pos(zaak.getAfgehandeldDoor().getValue())) {
          status += (" door " + zaak.getAfgehandeldDoor().getDescription());
        }
      }
    }

    ZaakItemRubriek rubriek = addRubriek("Interne behandeling");
    rubriek.add("Verantwoordelijke", zaak.getVerantwoordelijke());
    rubriek.add("Rappeldatum", zaak.getDatumRappel());
    rubriek.add("Status", status);
    rubriek.add("Resultaat", zaak.getResultaat());
  }

  private void setLandelijkeRegistratie(TerugmeldingAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Landelijke registratie");

    List<TerugmeldingRegistratie> l = zaak.getRegistraties();
    int i = l.size();
    for (TerugmeldingRegistratie reg : l) {
      rubriek = addRubriek("Actie " + i + " - " + reg.getActie().getOms());
      rubriek.add("Tijdstip", reg.getIn());
      rubriek.add("Status", reg.getStatus());
      rubriek.add("Verwerking", reg.getVerwerkingcode() + ": " + reg.getVerwerkingomschrijving());
      rubriek.add("Dossiernr.", reg.getDossiernummer());

      if (TmvActie.INZAG.equals(reg.getActie())) {
        rubriek.add("Verstuurd door.", reg.getUsr());
        rubriek.add("Datum aanleg", reg.getAanleg());
        rubriek.add("Datum wijziging", reg.getWijz());
        rubriek.add("Gemeente", reg.getGemBeh());
        rubriek.add("Datum verwachte afhandeling", reg.getVerwAfh());
        rubriek.add("Status dossier", reg.getStatus());
        rubriek.add("Resultaat", reg.getOnderzoekResultaat());
      }

      if (TmvActie.REGISTRATIE.equals(reg.getActie())) {
        rubriek.add("Toelichting resultaat", reg.getToelichtingOmschrijving());
      }

      i--;
    }
  }

  private void setReacties(TerugmeldingAanvraag zaak) {

    List<TerugmeldingReactie> l = zaak.getReacties();
    int i = l.size();

    addRubriek("Reacties");
    for (TerugmeldingReactie reg : l) {
      ZaakItemRubriek rubriek = addRubriek(
          "Reactie " + i + " - " + reg.getIngevoerdDoor() + " " + reg.getDatumTijdInvoer());
      rubriek.add("Inhoud reactie", reg.getTerugmReactie());
      i--;
    }
  }
}
