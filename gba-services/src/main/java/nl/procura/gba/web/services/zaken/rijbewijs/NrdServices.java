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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static java.lang.String.format;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.rdw.client.RdwClient;
import nl.procura.rdw.functions.*;
import nl.procura.rdw.messages.P1660;
import nl.procura.rdw.messages.P1722;
import nl.procura.rdw.messages.P1914;
import nl.procura.rdw.processen.p1914.f02.AANVRRYBZGEG;
import nl.procura.rdw.processen.p1914.f02.NATPAANVRGEG;
import nl.procura.rdw.processen.p1914.f02.NATPAANVRTAB;
import nl.procura.rdw.processen.p1914.f02.RYBAANVROVERZ;
import nl.procura.standard.exceptions.ProException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NrdServices {

  /**
   * P1914
   */
  public static List<RYBAANVROVERZ> getNrdAanvragenByStatus(RijbewijsService service, RijbewijsStatusType status,
      int max, ControlesListener listener,
      ControlesListener cutoffListener) {
    List<RYBAANVROVERZ> list = new ArrayList<>();

    long totaal = 0;
    BigInteger laatsteAanvraag = null;

    do {
      RYBAANVROVERZ overzicht = getNrdAanvragenByStatus(service, status, laatsteAanvraag);
      laatsteAanvraag = null; // default is null so no more requests
      if (overzicht != null) {
        AANVRRYBZGEG aanvrrybgeg = overzicht.getAanvrrybzgeg();
        NATPAANVRTAB natpaanvrtab = overzicht.getNatpaanvrtab();
        if (natpaanvrtab != null) {
          List<NATPAANVRGEG> natpaanvrgeg = natpaanvrtab.getNatpaanvrgeg();
          if (natpaanvrgeg != null && !natpaanvrgeg.isEmpty()) {
            List<NATPAANVRGEG> subList = new ArrayList<>(natpaanvrgeg);
            if ((totaal + natpaanvrgeg.size()) > max) {
              long sub = (max - totaal);
              subList = natpaanvrgeg.stream().limit(sub).collect(Collectors.toList());
              overzicht.getNatpaanvrtab().setNatpaanvrgeg(subList);
            } else {
              laatsteAanvraag = aanvrrybgeg.getAanvrnrrybl(); // update laatsteAanvraag
            }
            totaal += subList.size();
            list.add(overzicht);
          }
        }
      }

      // Break if number becomes too high
      if (totaal >= max) {
        laatsteAanvraag = null;
        if (cutoffListener != null) {
          cutoffListener.info(
              format("Er zijn meer dan <b>%s</b> rijbewijsaanvragen gevonden met status '%s'. " +
                  "<br/>De zoekopdracht is daarom afgebroken. Verhoog het max. aantal indien nodig.", max, status));
        }
      }
    } while (laatsteAanvraag != null && laatsteAanvraag.longValue() > 0);

    if (listener != null) {
      listener.info(
          format("RDW: %d aanvragen met status %d (%s)", list.size(), status.getCode(), status.getOms()));
    }

    return list;
  }

  /**
   * P1722
   */
  public static List<nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ> getNrdAanvragenByStatusEnPeriode(
      RijbewijsService service, RijbewijsStatusType status, long cStat, long dIn, long dEnd, int max,
      ControlesListener listener, ControlesListener cutoffListener) {

    List<nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ> list = new ArrayList<>();
    long totaal = 0;
    BigInteger laatsteAanvraag = null;

    do {
      nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ overzicht = getNrdAanvragenByStatusEnPeriode(service,
          status,
          laatsteAanvraag,
          cStat, dIn,
          dEnd);
      laatsteAanvraag = null; // default is null so no more requests
      if (overzicht != null) {
        nl.procura.rdw.processen.p1722.f02.AANVRRYBZGEG aanvrrybgeg = overzicht.getAanvrrybzgeg();
        nl.procura.rdw.processen.p1722.f02.NATPAANVRTAB natpaanvrtab = overzicht.getNatpaanvrtab();
        if (natpaanvrtab != null) {
          List<nl.procura.rdw.processen.p1722.f02.NATPAANVRGEG> natpaanvrgeg = natpaanvrtab.getNatpaanvrgeg();
          if (natpaanvrgeg != null && !natpaanvrgeg.isEmpty()) {
            List<nl.procura.rdw.processen.p1722.f02.NATPAANVRGEG> subList = new ArrayList<>(natpaanvrgeg);
            if ((totaal + natpaanvrgeg.size()) > max) {
              long sub = (max - totaal);
              subList = natpaanvrgeg.stream().limit(sub).collect(Collectors.toList());
              overzicht.getNatpaanvrtab().setNatpaanvrgeg(subList);
            } else {
              laatsteAanvraag = aanvrrybgeg.getAanvrnrrybl(); // update laatsteAanvraag
            }
            totaal += subList.size();
            list.add(overzicht);
          }
        }
      }

      // Break if number becomes too high
      if (totaal >= max) {
        laatsteAanvraag = null;
        if (cutoffListener != null) {
          cutoffListener.info(format("Er zijn meer dan <b>%s</b> rijbewijsaanvragen gevonden. " +
              "<br/>De zoekopdracht is daarom afgebroken. Verhoog het max. aantal indien nodig.", max));
        }
      }
    } while (laatsteAanvraag != null && laatsteAanvraag.longValue() > 0);

    if (listener != null) {
      listener.info(format("RDW: %d aanvragen met status %d (%s)", totaal, status.getCode(), status.getOms()));
    }

    return list;
  }

  /**
   * Zoek bij RDW het aanvraagnummer op basis van het documentnummer
   */
  public static String getBackOfficeAanvraag(RijbewijsService service, String documentnummer) {
    P1660 p1660 = new P1660();
    p1660.newF1(documentnummer);

    if (sendMessage(service, p1660)) {
      nl.procura.rdw.processen.p1660.f02.AANVRRYBKRT antwoord = (nl.procura.rdw.processen.p1660.f02.AANVRRYBKRT) p1660
          .getResponse()
          .getObject();
      return antwoord.getAanvrrybkgeg().getAanvrnrrybk().toString();
    }

    return null;
  }

  private static RYBAANVROVERZ getNrdAanvragenByStatus(RijbewijsService service, RijbewijsStatusType status,
      BigInteger laatsteAanvraagNummer) {

    P1914 p1914 = new P1914();
    Gebruiker gebruiker = service.getServices().getGebruiker();
    p1914.newF1(status.getCode(), along(gebruiker.getGemeenteCode()), laatsteAanvraagNummer);

    if (sendMessage(service, p1914)) {
      RYBAANVROVERZ overzicht = (RYBAANVROVERZ) p1914.getResponse().getObject();
      List<NATPAANVRGEG> natpaanvrgeg = overzicht.getNatpaanvrtab().getNatpaanvrgeg();
      if (CollectionUtils.isNotEmpty(natpaanvrgeg)) {
        return overzicht;
      }
    }

    return null;
  }

  /**
   * Zoek aanvragen op basis van periode + status + laatsteAanvraagnummer
   */
  private static nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ getNrdAanvragenByStatusEnPeriode(
      RijbewijsService service, RijbewijsStatusType status, BigInteger laatsteAanvraagNummer, long cStat,
      long dIn, long dEnd) {

    P1722 p1722 = new P1722();
    Gebruiker gebruiker = service.getServices().getGebruiker();
    long codeGemeente = along(gebruiker.getGemeenteCode());
    p1722.newF1(cStat, dIn, dEnd, codeGemeente, laatsteAanvraagNummer);

    if (sendMessage(service, p1722)) {
      nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ overzicht = (nl.procura.rdw.processen.p1722.f02.RYBAANVROVERZ) p1722
          .getResponse()
          .getObject();

      List<nl.procura.rdw.processen.p1722.f02.NATPAANVRGEG> natpaanvrgeg = overzicht.getNatpaanvrtab()
          .getNatpaanvrgeg();

      if (CollectionUtils.isNotEmpty(natpaanvrgeg)) {
        return overzicht;
      }
    }

    return null;
  }

  /**
   * Stuur bericht naar RDW en toon foutmelding indien van toepassing
   */
  public static boolean sendMessage(RijbewijsService service, RdwMessage message) {

    if (!message.isIgnoreBlock() && service.getAccount().isGeblokkeerd()) {
      throw new ProException(WARNING, "Het RDW account is momenteel geblokkeerd. " +
          "De applicatiebeheerder kan het deblokkeren.</br> ");
    }

    sendToRdw(service, message, isTru(service.getParm(ParameterConstant.RYB_TEST)));
    RdwProces proces = RdwProces.get(message.getResponse());
    return !isError(service, message, proces);
  }

  /**
   * Stuur aanvraag naar het RDW
   */
  private static void sendToRdw(RijbewijsService service, RdwMessage message, boolean isTest) {
    try {
      Object o = message.getRequest().getObject();
      BigInteger procId = BigInteger.valueOf(message.getRequest().getProces());
      BigInteger procFunc = BigInteger.valueOf(message.getRequest().getFunctie());

      String rdwEndpoint = service.getProxyUrl(RYB_URL, true);
      String rdwUsername = service.getSysteemParm(RYB_GEBRUIKERSNAAM, true);
      String rdwPassword = service.getSysteemParm(RYB_WACHTWOORD, true);
      String info = "Nvt"; // Niet noodzakelijk

      RdwUtils.init(o, procId, procFunc, rdwUsername, rdwPassword, info);

      if (isTest) {

        log.info("===========================================");
        log.info("LET OP! Rijbewijzen gebruikt testberichten!");
        log.info("===========================================");

        System.out.println("\n==> OUT ==>\n");
        RdwUtils.toStream(message.getRequest(), System.out, false, true);
        System.out.println("\n<== IN <==\n");
        message.setResponse(new RdwTester().getResponse(RdwTester.getMeerderePersonen(), message.getRequest()));
      } else {
        message.setResponse(RdwClient.send(rdwEndpoint, message.getRequest()));
      }
    } catch (RuntimeException e) {
      throw new ProException("Fout bij communicatie met het RDW", e);
    }
  }

  /**
   * Geeft de RDW een foutmelding terug?
   */
  private static boolean isError(RijbewijsService service, RdwMessage message, RdwProces proces) {

    Proces response = message.getResponse();

    if (response.isRipMelding() || (response.isMelding() && proces.t == RdwProcesType.ERROR)) {

      // Geen personen is geen melding
      if (proces == RdwProces.P1651_F9 && response.getMelding().getNr() == RdwMeldingNr.GEEN_PERSONEN.code) {
        return false;
      }

      // Meerdere personen is geen melding
      if (proces == RdwProces.P1651_F9 && response.getMelding().getNr() == RdwMeldingNr.MEEDERE_PERSONEN.code) {
        return false;
      }

      // Blokkeer het RDW account
      if (response.isRipMelding() && response.getMelding().getRipNr() == RdwMeldingNr.WACHTWOORD_ONJUIST.code) {
        service.blokkeer();
      }

      return true;
    }

    return false;
  }
}
