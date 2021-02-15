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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TMV_URL;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.*;
import static nl.procura.standard.exceptions.ProExceptionType.*;

import java.util.List;

import nl.bprbzk.gba.terugmeldvoorziening.version1.Antwoord;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Dossier;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Ontvangstbevestiging;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Statusoverzicht;
import nl.procura.gba.jpa.personen.db.TerugmTmvPK;
import nl.procura.gba.web.common.misc.GbaLogger;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbavType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.tmv.TmvActionTemplate;
import nl.procura.tmv.actions.TmvInzageVerzoekAction;
import nl.procura.tmv.actions.TmvRegistratieAction;
import nl.procura.tmv.soap.TmvSoapHandler;

public class TerugmeldingBerichtService extends AbstractService {

  public TerugmeldingBerichtService() {
    super("Terugmeldingberichten");
  }

  public TerugmeldingRegistratie intrekken(TerugmeldingAanvraag melding) {

    TmvRegistratieAction action = new TmvRegistratieAction();

    action.nieuw(getSystemTime() + melding.getCTerugmelding(), astr(melding.getIngevoerdDoor().getValue()), 0L,
        along(melding.getAnummer().getValue()));

    // Nieuwe waarde hetzelfde maken als oude waarde
    for (TerugmeldingDetail detail : melding.getDetails()) {
      action.addDetail(detail.getCCat(), detail.getCElem(), astr(detail.getValOrigineel()),
          astr(detail.getValOrigineel()));
    }

    action.setToelichting("Registratie intrekken");

    send(action);

    if (action.isVerwerkingSuccess()) {

      Ontvangstbevestiging b = action.getResponseObject();

      TerugmeldingRegistratie tmv = nieuweRegistratie(b);

      // Intrekking
      tmv.setBerichtcode(TmvActie.INTREKKING.getBerichtcode());
      tmv.getId().setDossiernummer(b.getDossiernummer());
      tmv.setToelichting(astr(melding.getTerugmelding()));

      return tmv;
    }

    String msg = action.getResponseObject().getVerwerkingcode() + ": "
        + action.getResponseObject().getVerwerkingomschrijving();
    throw new ProException(WEBSERVICE, ERROR, "Foutmelding ontvangen." + msg);
  }

  public TerugmeldingRegistratie inzage(TerugmeldingAanvraag melding) {

    TmvInzageVerzoekAction action = new TmvInzageVerzoekAction();

    action.nieuw(getSystemDate() + "_" + getSystemTime(), TmvInzageVerzoekAction.SOORT_OVERZICHT_DOSSIER);

    if (melding.getRegistraties().isEmpty()) {
      throw new ProException(ENTRY, INFO, "Registreer eerst de melding");
    }

    if (melding.getRegistraties().size() > 0) {
      action.setDossiernummer(melding.getRegistraties().get(0).getDossiernummer());
    }

    send(action);

    if (action.isVerwerkingSuccess()) {

      Statusoverzicht b = action.getResponseObject();

      TerugmeldingRegistratie tmv = nieuweRegistratie(b);

      if (b.getStatusoverzicht().getDossier().size() > 0) {

        Dossier d = b.getStatusoverzicht().getDossier().get(0);

        tmv.getId().setDossiernummer(d.getDossiernummer());
        tmv.setDAanleg(toBigDecimal(d.getDatumVanAanleggenDossier()));
        tmv.setDWijz(toBigDecimal(d.getDatumVanWijzigenDossier()));
        tmv.setCGemBeh(toBigDecimal(d.getBehandelendeGemeente()));
        tmv.setDVerwAfh(toBigDecimal(d.getDatumVerwachteAfhandelingDossier()));
        tmv.setDossierstatus(toBigDecimal(d.getStatusDossier()));
        tmv.setResultaatonderzoek(toBigDecimal(d.getResultaatOnderzoek()));
        tmv.setResultaat(astr(d.getToelichting()));
      }

      return tmv;
    }

    String msg = action.getResponseObject().getVerwerkingcode() + ": "
        + action.getResponseObject().getVerwerkingomschrijving();
    throw new ProException(WEBSERVICE, ERROR, "Foutmelding ontvangen." + msg);
  }

  public TerugmeldingRegistratie registeer(TerugmeldingAanvraag melding) {

    TmvRegistratieAction action = new TmvRegistratieAction();

    String ingevoerdDoor = melding.getIngevoerdDoor().getStringValue();
    long anummer = melding.getAnummer().getLongValue();
    action.nieuw(getSystemTime() + melding.getCTerugmelding(), ingevoerdDoor, 0L, anummer);

    for (TerugmeldingDetail detail : melding.getDetails()) {
      action.addDetail(detail.getCCat(), detail.getCElem(), astr(detail.getValOrigineel()),
          astr(detail.getValNieuw()));
    }

    action.setToelichting(melding.getTerugmelding());

    send(action);

    if (action.isVerwerkingSuccess()) {

      Ontvangstbevestiging b = action.getResponseObject();

      TerugmeldingRegistratie tmv = nieuweRegistratie(b);

      // Registratie
      tmv.getId().setDossiernummer(b.getDossiernummer());
      tmv.setToelichting(astr(melding.getTerugmelding()));

      return tmv;
    }

    String msg = action.getResponseObject().getVerwerkingcode() + ": "
        + action.getResponseObject().getVerwerkingomschrijving();
    throw new ProException(WEBSERVICE, ERROR, "Foutmelding ontvangen." + msg);
  }

  private TmvSoapHandler getSoapHandler() {

    List<GbaWsRestGbavAccount> accounts = getServices().getPersonenWsService().getGbavAccounts(PROFIEL_STANDAARD);
    String tmvEndpoint = getProxyUrl(TMV_URL, true);

    for (GbaWsRestGbavAccount account : accounts) {

      if (GbavType.BEPERKT == account.getType()) {

        String tmvGebruikersnaam = account.getGebruikersnaam();
        String tmvWachtwoord = account.getWachtwoord();

        if (emp(tmvGebruikersnaam)) {
          throw new ProException(CONFIG, WARNING, "De parameter <b>TMV gebruikersnaam</b> is niet ingevuld.");
        }

        if (emp(tmvWachtwoord)) {
          throw new ProException(CONFIG, WARNING, "De parameter <b>TMV wachtwoord</b> is niet ingevuld.");
        }

        return new TmvSoapHandler(tmvEndpoint, tmvGebruikersnaam, tmvWachtwoord);
      }
    }

    throw new ProException(CONFIG, "Terugmeldingen sturen is niet mogelijk. De accountgegevens zijn niet ingevuld");
  }

  private TerugmeldingRegistratie nieuweRegistratie(Antwoord b) {

    TerugmeldingRegistratie tmv = new TerugmeldingRegistratie();
    TerugmTmvPK id = new TerugmTmvPK();
    id.setDIn(along(getSystemDate()));
    id.setTIn(along(getSystemTime()));
    tmv.setId(id);

    tmv.setCUsr(toBigDecimal(getServices().getGebruiker().getCUsr()));
    tmv.setBerichtcode(b.getBerichtcode());
    tmv.setBerichtreferentie(astr(b.getBerichtreferentie()));
    tmv.setBerichtnummer(toBigDecimal(b.getTmvBerichtnummer()));
    tmv.setVerwerkingcode(toBigDecimal(b.getVerwerkingcode()));
    tmv.setVerwerkingomschrijving(astr(b.getVerwerkingomschrijving()));
    tmv.setDossierstatus(toBigDecimal(TmvStatus.ONBEKEND.getCode()));

    return tmv;
  }

  private void send(TmvActionTemplate action) {

    TmvSoapHandler soapHandler = getSoapHandler();

    try {
      action.setSoapHandler(soapHandler);
      action.send();
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, ERROR, "Fout bij het versturen van de terugmelding", e);
    } finally {
      GbaLogger.log("terugmelding", soapHandler);
    }
  }
}
