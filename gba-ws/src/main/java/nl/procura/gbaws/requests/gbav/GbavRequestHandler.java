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

package nl.procura.gbaws.requests.gbav;

import static nl.procura.standard.Globalfunctions.*;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.base.*;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gbav.utils.GbavAutorisatie;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.diensten.gbav.utils.GbavService;
import nl.procura.diensten.gbav.utils.acties.GbavAfnemerIndicatiesActie;
import nl.procura.diensten.gbav.utils.acties.GbavBeperkteZoekActie;
import nl.procura.diensten.gbav.utils.acties.GbavVolledigeZoekActie;
import nl.procura.diensten.gbav.utils.vraag.GbavVraagAntwoord;
import nl.procura.gba.jpa.personenws.db.LandtabElement;
import nl.procura.gba.jpa.personenws.db.LandtabRecord;
import nl.procura.gba.jpa.personenws.db.LandtabRecordPK;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.handlers.EmailConfigDao;
import nl.procura.gbaws.db.wrappers.EmailConfigWrapper;
import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper.Attributen;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.mail.EmailError;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbavType;
import nl.procura.gbaws.web.servlets.RequestException;

public class GbavRequestHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GbavRequestHandler.class);

  private final UsrWrapper user;

  public GbavRequestHandler(BasePLBuilder builder, UsrWrapper user, PLEArgs args) {

    this.user = user;

    try {
      checkGbavProfile();

      GbavProfileWrapper gbavProfiel = getGbavProfiel();
      if (gbavProfiel != null) {

        final String gebruiker = gbavProfiel.getAttributen().getGebruikersnaam();
        final String wachtwoord = gbavProfiel.getAttributen().getWachtwoord();
        final String urlZoeken = gbavProfiel.getAttributen().getZoekEndpoint();
        final String urlIndicaties = gbavProfiel.getAttributen().getAfnemerIndicatiesEndpoint();

        GbavVraagAntwoord antwoord;

        switch (GbavType.get(gbavProfiel.getType())) {
          case VOLLEDIG:
            // Zoek afnemers indicatie
            if (args.isSearchIndications()) {
              GbavService gbav = new GbavService(gebruiker, wachtwoord, urlIndicaties);
              GbavAfnemerIndicatiesActie afnemerindicatiesActie = gbav.getActies().getAfnemerIndicatiesActie();
              antwoord = afnemerindicatiesActie.zoek(args, builder);

            } else {
              // Zoek persoonslijsten
              GbavService gbav = new GbavService(gebruiker, wachtwoord, urlZoeken);
              GbavVolledigeZoekActie volledigeActie = gbav.getActies().getVolledigeZoekActie();
              antwoord = volledigeActie.zoek(args, builder);
            }
            break;

          case BEPERKT:
          default:
            GbavService gbav = new GbavService(gebruiker, wachtwoord, urlZoeken);
            GbavBeperkteZoekActie beperkteActie = gbav.getActies().getBeperkteZoekActie();
            antwoord = beperkteActie.zoek(args, getAutorisatie(user), builder);
            break;
        }

        new Formatter(builder.getResult());
        processResult(antwoord.getResultaat());
        BasePLUtils.removeDuplicates(builder);

        for (final BasePL bp : builder.getResult().getBasePLs()) {
          builder.getResult().setDatasource(bp.getDatasource());
        }
      }
    } catch (final RequestException e) {
      throw e;
    } catch (final RuntimeException e) {
      throw new RequestException(1200, e);
    }
  }

  private static void sendMail(GbavResultaat result) {

    try {
      EmailConfigWrapper config = EmailConfigDao.getConfig();
      if (pos(config.getPk()) && config.isTypeFoutWW()) {
        final EmailLogWrapper log = new EmailLogWrapper();
        log.save(new EmailError(result.getCode(), result.getOmschrijving()));
        log.send();
      }
    } catch (final RuntimeException e) {
      LOG.trace(e.getMessage(), e);
    }
  }

  private void blockGBAV() {

    EntityManager m;

    try {

      m = GbaWsJpa.getManager();
      final GbavProfileWrapper p = user.getProfiel().getGBAvProfiel();

      p.setGeblokkeerd(true);
      m.getTransaction().begin();
      p.save(m);

      m.getTransaction().commit();
    } catch (final RuntimeException e) {
      LOG.trace(e.getMessage(), e);
    }
  }

  private void processResult(GbavResultaat resultaat) {

    if (pos(resultaat.getCode()) && aval(resultaat.getCode()) != 33) {
      // 10 = Ongeldige combinatie gebruikersnaam/wachtwoord
      // 11 = Service is niet geactiveerd
      // 12 = Wachtwoord verlopen

      if (resultaat.isCode(10, 12)) {
        blockGBAV();
        sendMail(resultaat);
      }

      throw new RequestException(resultaat.getCode(), "GBA-V: " + resultaat.getOmschrijving());
    }
  }

  private GbavAutorisatie getAutorisatie(UsrWrapper g) {
    final GbavAutorisatie autorisatie = new GbavAutorisatie();

    if (g.getProfiel() != null) {
      GbavProfileWrapper gbavProfiel = getGbavProfiel();
      if (gbavProfiel != null) {
        for (final ProfielElement pe : g.getProfiel().getElementen(1, gbavProfiel.getPk())) {
          autorisatie.addElement(pe.getCode_cat(), pe.getCode_element());
        }
      }
    }

    return autorisatie;
  }

  private GbavProfileWrapper getGbavProfiel() {
    GbavProfileWrapper p = null;
    try {
      p = user.getProfiel().getGBAvProfiel();
    } catch (final RuntimeException e) {
      LOG.trace(e.getMessage(), e);
    }

    return p;
  }

  private void checkGbavProfile() {

    GbavProfileWrapper gbavProfiel = getGbavProfiel();

    if (gbavProfiel != null) {
      final Attributen a = gbavProfiel.getAttributen();
      final String errorMsg = "GBA-V configuratiefout: geen GBA-V ";

      if (emp(a.getZoekEndpoint())) {
        throw new RequestException(1004, errorMsg + "url.");
      }

      if (emp(a.getGebruikersnaam())) {
        throw new RequestException(1004, errorMsg + "gebruikersnaam.");
      }

      if (emp(a.getWachtwoord())) {
        throw new RequestException(1004, errorMsg + "wachtwoord.");
      }

      if (gbavProfiel.isGeblokkeerd()) {
        throw new RequestException(1005,
            "De GBA-V is tijdelijk geblokkeerd. Waarschijnlijk vanwege een foutief wachtwoord. " +
                "Dit kan door applicatiebeheer worden verholpen.");
      }

    } else {
      throw new RequestException(1003, "GBA-V: GBA-V is geen onderdeel van uw profiel.");
    }
  }

  // Zoek omschrijving
  private class Formatter extends BasePLFormatter {

    public Formatter(PLEResult ple) {
      super(ple);
    }

    @Override
    public String getNationalValue(int elementCode, BasePLValue value) {
      try {

        EntityManager em = GbaWsJpa.getManager();
        final int c_table = em.find(LandtabElement.class, elementCode).getCTable().getCTable();
        return em.find(LandtabRecord.class, new LandtabRecordPK(c_table, value.getVal())).getOms();
      } catch (final RuntimeException e) {
        LOG.trace(e.getMessage(), e);
      }

      return value.getVal();
    }
  }
}
