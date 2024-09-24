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

package nl.procura.gbaws.web.rest.v1_0.gbav;

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.diensten.gbav.utils.GbavAntwoord;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.gba.common.BrpPasswordGenerator;
import nl.procura.gbaws.db.handlers.GbavProfileDao;
import nl.procura.gbaws.db.handlers.PasswordHandler;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper.Attributen;
import nl.procura.gbaws.web.rest.GbaWsRestDienstenbusResource;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccountsAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbavType;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.genereren.GbaWsRestGbavWachtwoordGenererenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenVraag;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.proweb.rest.v1_0.Rol;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionType;

@RequestScoped
@Path("v1.0/gbav")
@AuthenticatieVereist(rollen = { Rol.BEHEERDER })
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class GbaWsRestGbavResources extends GbaWsRestDienstenbusResource {

  @GET
  @Path("/accounts")
  public GbaWsRestGbavAccountsAntwoord getAccounts() {

    GbaWsRestGbavAccountsAntwoord antwoord = new GbaWsRestGbavAccountsAntwoord();

    for (GbavProfileWrapper profile : GbavProfileDao.getProfiles()) {

      Attributen gbavAttributen = profile.getAttributen();

      GbaWsRestGbavAccount account = new GbaWsRestGbavAccount();
      account.setCode(profile.getPk());
      account.setNaam(profile.getNaam());
      account.setOmschrijving(profile.getOmschrijving());
      account.setType(GbavType.get(profile.getType()));
      account.setDatumGewijzigd(gbavAttributen.getAanpassingDatum());
      account.setGebruikersnaam(gbavAttributen.getGebruikersnaam());
      account.setWachtwoord(gbavAttributen.getWachtwoord());

      final String dagen90 = new ProcuraDate(account.getDatumGewijzigd()).addDays(90).getSystemDate();
      account.setDatumVerloop(pos(account.getDatumGewijzigd()) ? dagen90 : "0");
      account.setDagenGeldig(new ProcuraDate().diffInDays(new ProcuraDate(account.getDatumVerloop())));
      account.setGeblokkeerd(profile.isGeblokkeerd());

      antwoord.getAccounts().add(account);
    }

    return antwoord;
  }

  @POST
  @Path("/account/deblokkeren")
  public GbaWsRestGbavDeblokkerenAntwoord deblokkeren(GbaWsRestGbavDeblokkerenVraag vraag) {

    GbavProfileWrapper gbavDao = GbavProfileDao.getProfile(vraag.getCode());

    if (gbavDao == null) {
      throw new ProException(ProExceptionType.SELECT, "Geen GBA-V profiel met code: " + vraag.getCode());
    }

    gbavDao.deblokkeer();

    return new GbaWsRestGbavDeblokkerenAntwoord();
  }

  @POST
  @Path("/account/updaten")
  public GbaWsRestGbavAccountUpdatenAntwoord updateAccount(GbaWsRestGbavAccountUpdatenVraag vraag) {

    GbavProfileWrapper gbavDao = GbavProfileDao.getProfile(vraag.getCode());

    if (gbavDao == null) {
      throw new ProException(ProExceptionType.SELECT, "Geen GBA-V profiel met code: " + vraag.getCode());
    }

    if (!pos(vraag.getDatum())) {
      throw new ProException(ProExceptionType.SELECT, "Datum wijziging wachtwoord is niet gevuld");
    }

    if (emp(vraag.getWachtwoord())) {
      throw new ProException(ProExceptionType.SELECT, "Wachtwoord is niet gevuld");
    }

    Attributen attributen = gbavDao.getAttributen();
    attributen.setAanpassingDatum(astr(vraag.getDatum()));
    attributen.setWachtwoord(vraag.getWachtwoord());
    attributen.mergeAndCommit();
    attributen.mergeAndCommitOthers();

    return new GbaWsRestGbavAccountUpdatenAntwoord();
  }

  @GET
  @Path("/wachtwoord/genereren")
  public GbaWsRestGbavWachtwoordGenererenAntwoord genereerWachtwoord() {
    return new GbaWsRestGbavWachtwoordGenererenAntwoord(BrpPasswordGenerator.newPassword());
  }

  @POST
  @Path("/wachtwoord/versturen")
  public GbaWsRestGbavWachtwoordVersturenAntwoord nieuwWachtwoord(GbaWsRestGbavWachtwoordVersturenVraag vraag) {

    GbavProfileWrapper gbavDao = GbavProfileDao.getProfile(vraag.getCode());

    if (gbavDao == null) {
      throw new ProException(ProExceptionType.SELECT, "Geen GBA-V profiel met code: " + vraag.getCode());
    }

    if (emp(vraag.getWachtwoord())) {
      throw new ProException(ProExceptionType.SELECT, "Wachtwoord is niet gevuld");
    }

    final GbavAntwoord antwoord = PasswordHandler.send(gbavDao, vraag.getWachtwoord());
    final GbavResultaat result = antwoord.getResultaat();
    final int code = result.getCode();

    if (code == 0) {

      Attributen attributen = gbavDao.getAttributen();
      attributen.setAanpassingDatum(getSystemDate());
      attributen.setWachtwoord(vraag.getWachtwoord());
      attributen.mergeAndCommit();
      attributen.mergeAndCommitOthers();

      return new GbaWsRestGbavWachtwoordVersturenAntwoord();
    }

    throw new ProException(ERROR,
        "Wijzigen wachtwoord mislukt: " + result.getOmschrijving() + " (code: " + code + ")");
  }
}
