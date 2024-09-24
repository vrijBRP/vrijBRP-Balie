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

package nl.procura.gba.web.services.beheer.bsm;

import static java.text.MessageFormat.format;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.BSM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ENABLED;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.WEBSERVICE;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.bsm.rest.client.BsmRestClient;
import nl.procura.bsm.rest.client.BsmRestClientException;
import nl.procura.bsm.rest.client.BsmRestClientResponse;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementAntwoord;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementVraag;
import nl.procura.bsm.rest.v1_0.objecten.bestand.BsmRestBestandVraag;
import nl.procura.bsm.rest.v1_0.objecten.taak.*;
import nl.procura.bsm.taken.BsmTaak;
import nl.procura.gba.web.common.misc.ExceptionUtils;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.applicatie.meldingen.types.BsmMelding;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.ServiceControle;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingType;
import nl.procura.proweb.rest.v1_0.sessie.ProRestTicketAntwoord;
import nl.procura.commons.core.exceptions.ProException;

public class BsmService extends AbstractService implements ServiceControle {

  public BsmService() {
    super("BSM");
  }

  @Override
  public void check() {

    boolean isBsmInProfiel = getServices().getGebruiker()
        .getProfielen()
        .isProfielActie(ProfielActie.SELECT_HOOFD_BSM);

    if (isBsmEnabled() && isBsmInProfiel && !isBsmCorrect()) {
      String melding = "Er is iets aan de hand met de taakplanner (BSM).";
      getServices().getMeldingService().add(new BsmMelding(melding));
    }
  }

  /**
   * Sla een document op als dossier in de BSM
   */
  public void addBestand(BsmRestBestandVraag vraag, BsmBestand bestand) {
    vraag.addBestand(bestand.getBestandsnaam(), Base64.encodeBase64String(bestand.getBytes()));
    BsmRestElementAntwoord antwoord = new BsmRestElementAntwoord();
    bsmQuery(bestand.getBsmId(), vraag, antwoord);
  }

  /**
   * Voer een query uit
   */
  public void bsmQuery(String query, BsmRestElementVraag vraag, BsmRestElementAntwoord antwoord) {

    try {
      BsmRestClient client = getBsmClient(false);
      BsmRestClientResponse<BsmRestElementAntwoord> response = client.getAlgemeen().get(query, vraag);
      check(response.getClientResponse());
      BsmRestElementAntwoord entity = response.check().getEntity();
      BsmRestElement antwoordElement = entity.getAntwoordElement();
      antwoord.setAntwoordElement(antwoordElement);
      antwoord.setMeldingen(entity.getMeldingen());

      for (ProRestMelding melding : antwoord.getMeldingen()) {
        if (ProRestMeldingType.WAARSCHUWING.equals(melding.getType())) {
          throw new ProException(WARNING, melding.getOmschrijving());
        }
      }

    } catch (BsmRestClientException e) {
      handleException(e);
    }
  }

  public void checkWaarschuwingsIcoon() {
    callListeners(ServiceEvent.CHANGE);
  }

  public BsmRestClient getBsmClient(boolean autenticeer) {
    BsmRestClient client = new BsmRestClient(getBsmInternalUrl(), "BSM");
    Gebruiker gebruiker = getServices().getGebruiker();
    getServices().getGebruikerService().getCurrentPassword(gebruiker)
        .filter(password -> autenticeer)
        .ifPresent(password -> {
          client.setGebruikersnaam(gebruiker.getGebruikersnaam());
          client.setWachtwoord(password);
          client.setConnectieTimeout(10000);
          client.setLeesTimeout(10000);
        });

    return client;
  }

  public String getBsmExternalUrl() {
    return getParm(ParameterConstant.BSM_EXTERNAL_URL, true);
  }

  public String getBsmInternalUrl() {
    return getParm(ParameterConstant.BSM_INTERNAL_URL, true);
  }

  public BsmRestTaak getBsmRestTaak(BsmTaak bsmTaak) {
    return getBsmRestTaakByJobId(bsmTaak, "");
  }

  public BsmRestTaak getBsmRestTaak(Zaak zaak) {
    return getBsmRestTaak(BsmZaakTypes.getBsmTaak(zaak));
  }

  public BsmRestTaak getBsmRestTaakByJobId(BsmTaak bsmTaak, String jobId) {

    for (BsmRestTaak taak : getBsmTaken()) {
      if (emp(jobId) || taak.getTaak().equals(jobId)) {
        for (BsmRestSubTaak subTaak : taak.getSubtaken()) {
          if (subTaak != null) {

            boolean isId = bsmTaak.getId().equals(subTaak.getSubtaak());
            boolean isVersie = bsmTaak.getVersie().equals(subTaak.getVersie());

            if (isId && isVersie) {
              return taak;
            }
          }
        }
      }
    }

    throw new ProException(WARNING, "De taakplanner (BSM) is niet geconfigureerd voor deze taak");
  }

  public List<BsmRestTaakLog> getBsmTaakLog(String sessie) {

    if (isBsmEnabled()) {

      BsmRestClientResponse<BsmRestTaakLogZoekenAntwoord> antwoord;

      try {
        antwoord = getBsmClient(false).getTaak().getLog().get(sessie);

        check(antwoord.getClientResponse());

        return antwoord.check().getEntity().getLogs();
      } catch (BsmRestClientException e) {
        handleException(e);
      }
    }

    throw new ProException(WARNING, "De taakplanner (BSM) is niet geconfigureerd");
  }

  public List<BsmRestTaak> getBsmTaken() {

    if (isBsmEnabled()) {
      BsmRestClientResponse<BsmRestTaakZoekenAntwoord> antwoord;

      try {
        antwoord = getBsmClient(false).getTaak().get();
        check(antwoord.getClientResponse());
        return antwoord.check().getEntity().getTaken();
      } catch (BsmRestClientException e) {
        handleException(e);
      }
    }

    return new ArrayList<>();
  }

  public String getBsmTicket() {
    return check(getBsmClient(true).getTicket()).getEntity(ProRestTicketAntwoord.class).getTicket();
  }

  public boolean isBsmCorrect() {
    if (isBsmEnabled()) {
      try {
        for (BsmRestTaak taak : getBsmTaken()) {
          if (taak.getStatus() == BsmRestTaakStatus.ONDERBROKEN) {
            if (!taak.isUitgezet()) {
              return false;
            }
          }
        }
      } catch (Exception e) {
        return false;
      }
    }

    getServices().getMeldingService().delete(BSM);
    return true;
  }

  public boolean isBsmEnabled() {
    return isTru(getSysteemParm(BSM_ENABLED, false)) && fil(getParm(ParameterConstant.BSM_INTERNAL_URL));
  }

  @Override
  public boolean isCorrect() {
    return isBsmCorrect();
  }

  /**
   * Mijn-overheid bestand toevoegen
   */
  @SuppressWarnings("deprecation")
  public void addMijnOverheidBestand(BsmMijnOverheidBestand bestand) {
    Zaak zaak = bestand.getZaak();
    if (zaak == null) {
      throw new ProException(WARNING, "Er is geen zaak gekoppeld aan deze actie");
    }

    String bsn = zaak.getBurgerServiceNummer().getStringValue();
    String berichttype = bestand.getBerichttype();

    BsmRestBestandVraag vraag = new BsmRestBestandVraag();
    vraag.setKenmerk(BsmRestBestandVraag.KENMERK1, "mijn-overheid");
    vraag.setKenmerk(BsmRestBestandVraag.KENMERK2, bsn);
    vraag.setKenmerk(BsmRestBestandVraag.KENMERK3, bestand.getExtension());
    vraag.setKenmerk(BsmRestBestandVraag.KENMERK4, berichttype);

    // Specifieke bestandsnaam zetten voor mijn-overheid
    String guid = UUID.randomUUID().toString();
    String bestandsnaam = format("{0}.{1}.{2}.{3}", bsn, berichttype, guid, bestand.getExtension());
    bestand.setBestandsnaam(bestandsnaam);

    boolean wel = zaak.getZaakHistorie().getAttribuutHistorie().is(ZaakAttribuutType.MIJN_OVERHEID_WEL);
    boolean niet = zaak.getZaakHistorie().getAttribuutHistorie().is(ZaakAttribuutType.MIJN_OVERHEID_NIET);

    if (wel) {
      vraag.setKenmerk(BsmRestBestandVraag.KENMERK5, ZaakAttribuutType.MIJN_OVERHEID_WEL.getCode());
    } else if (niet) {
      vraag.setKenmerk(BsmRestBestandVraag.KENMERK5, ZaakAttribuutType.MIJN_OVERHEID_NIET.getCode());
    } else {
      throw new ProException(WARNING,
          "Van deze persoon is niet bekend of hij/zij abonnee is van 'mijn-overheid'");
    }

    addBestand(vraag, bestand);
  }

  public String uitvoeren(BsmRestTaakVraag vraag) {

    if (isBsmEnabled()) {
      BsmRestClientResponse<BsmRestTaakUitvoerenAntwoord> antwoord;

      try {
        antwoord = getBsmClient(false).getTaak().uitvoeren(vraag).check();
        check(antwoord.getClientResponse());
        return antwoord.check().getEntity().getSessie();
      } catch (BsmRestClientException e) {
        handleException(e);
      }
    }

    throw new ProException(WARNING, "De taakplanner (BSM) is niet geconfigureerd");
  }

  private ClientResponse check(ClientResponse response) {

    switch (Status.fromStatusCode(response.getStatus())) {
      case OK:
        return response;

      case NOT_FOUND:
        throw new ProException(WARNING, "De BSM kan niet worden benaderd (controleer de parameters)");

      case UNAUTHORIZED:
        throw new ProException(WARNING, "U heeft momenteel geen toegang tot de taakplanner (BSM)");

      default:
        throw new ProException(ERROR, response.getStatusInfo().getReasonPhrase());
    }
  }

  private void handleException(BsmRestClientException e) {

    if (ExceptionUtils.getCause(e, ConnectException.class) != null) {
      throw new ProException(WARNING, "Er kan geen contact worden gemaakt met de taakplanner (BSM)");
    }

    throw new ProException(WEBSERVICE, "Fout bij aanroepen van de taakplanner (BSM)", e);
  }
}
