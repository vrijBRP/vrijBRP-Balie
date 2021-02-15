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

package nl.procura.gbaws.requests.gba;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgsLogger;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.gbaws.db.handlers.ParmDao;
import nl.procura.gbaws.db.misc.ParmValues;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.Attributen;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.requests.gbav.GbavRequestHandler;
import nl.procura.gbaws.requests.ple.ProcuraDBRequestHandler;
import nl.procura.gbaws.web.servlets.RequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRequestHandler {

  private final nl.procura.gbaws.requests.Logger logger  = new nl.procura.gbaws.requests.Logger();
  private UsrWrapper                             user    = null;
  private PLEArgs                                args    = null;
  private BasePLBuilder                          builder = new BasePLBuilder();

  /**
   * Zoek
   */
  public void find() {

    long timeProcura = 0;
    long timeGbav = 0;

    try {
      getLogger().chapter("Zoekargumenten");

      // Log de argumenten

      for (final Entry<String, Object> e : new PLEArgsLogger(args).getMap().entrySet()) {
        getLogger().item(e.getKey(), e.getValue());
      }

      final PLEDatasource requestBron = args.getDatasource();
      final PLEDatasource profileBron = PLEDatasource.get(getProfielBron());

      final boolean isDatabronProcura = isProcuraBron();
      final boolean isDatabronGbav = isGBAVBron();

      if ((requestBron.is(PROCURA) || requestBron.is(STANDAARD)
          && profileBron.is(PROCURA)) && !isDatabronProcura) { // Procura
        throw new RequestException(1003, "PROCURA DB is niet geconfigureerd als databron.");

      } else if ((requestBron.is(GBAV) || requestBron.is(STANDAARD)
          && profileBron.is(GBAV)) && !isDatabronGbav) { // gbav
        throw new RequestException(1003, "GBA-V is niet geconfigureerd als databron.");

      } else if (!isDatabronProcura && !isDatabronGbav) { // alle databronnen
        throw new RequestException(1003, "Er is geen databron geconfigureerd");
      }

      if (args.isSearchIndications()) {
        if (isDatabronGbav) {
          if (requestBron.is(GBAV) || requestBron.is(STANDAARD) && profileBron.is(GBAV, STANDAARD)) {
            long st = System.currentTimeMillis();
            findGBAV();
            timeGbav = (System.currentTimeMillis() - st);
            getArgs().setNumbers(getNotFoundNumbers(args.getNumbers()));
          }
        }
      } else {

        /**
         * gebruiker wilt zoeken in procura db of geen voorkeur. In dat geval moet de standaard procura zijn.
         */
        if (isDatabronProcura) {
          if (requestBron.is(PROCURA) || requestBron.is(STANDAARD) && profileBron.is(PROCURA, STANDAARD)) {
            long st = System.currentTimeMillis();
            findProcuraDB();
            timeProcura = (System.currentTimeMillis() - st);
            getArgs().setNumbers(getNotFoundNumbers(args.getNumbers()));
          }
        }

        /**
         * gebruiker wilt zoeken in gbav db of geen voorkeur. In dat geval moet de standaard gbav zijn.
         */
        if (isDatabronGbav) {
          if (requestBron.is(GBAV) || requestBron.is(STANDAARD) && profileBron.is(GBAV, STANDAARD)) {
            long st = System.currentTimeMillis();
            findGBAV();
            timeGbav = (System.currentTimeMillis() - st);
            getArgs().setNumbers(getNotFoundNumbers(args.getNumbers()));
          }
        }

        verwijderVerkeerdeAdressen();
      }
    } catch (RequestException e) {
      getLogger().chapter("Foutmelding");
      getLogger().item("Melding", e.getMessage());

      throw e;
    } finally {
      getLogger().chapter("Resultaat");

      if (timeProcura > 0) {
        getLogger().item("Duur Procura DB", timeProcura + " ms.");
      }

      if (timeGbav > 0) {
        getLogger().item("Duur GBA-V DB", timeGbav + " ms.");
      }

      getLogger().item("Aantal persoonslijsten", builder.getResult().getBasePLs().size());
      getLogger().item("Bron", builder.getResult().getDatasource());
    }
  }

  @Override
  public String toString() {
    return "GBARequestHandler2 [gebruiker=" + user + ", args=" + args + ", basisPlHandler=" + builder + "]";
  }

  public UsrWrapper getGebruiker() {
    return user;
  }

  public void setGebruiker(UsrWrapper gebruiker) {
    this.user = gebruiker;
  }

  public PLEArgs getArgs() {
    return args;
  }

  public void setArgs(PLEArgs args) {
    this.args = args;
  }

  public void setBuilder(BasePLBuilder builder) {
    this.builder = builder;
  }

  public nl.procura.gbaws.requests.Logger getLogger() {
    return logger;
  }

  /**
   * Verwijder de personen die niet overeenkomen voor wat betreft het adres.
   */
  private void verwijderVerkeerdeAdressen() {

    for (final BasePL basisPl : new ArrayList<>(builder.getResult().getBasePLs())) {

      if (basisPl.getLatestRec(VB).hasElems()) {

        final String zHnrL = args.getHuisletter();
        final String zHnrT = args.getHuisnummertoevoeging();
        final String zHnrA = args.getAanduiding();

        final String hnrL = basisPl.getLatestRec(VB).getElemVal(HNR_L).getVal();
        final String hnrT = basisPl.getLatestRec(VB).getElemVal(HNR_T).getVal();
        final String hnrA = basisPl.getLatestRec(VB).getElemVal(HNR_A).getVal();

        boolean remove = false;

        if (isVerschillendAdres(zHnrL, hnrL)) {
          remove = true;
        }

        if (remove || isVerschillendAdres(zHnrT, hnrT)) {
          remove = true;
        }

        if (remove || isVerschillendAdres(zHnrA, hnrA)) {
          remove = true;
        }

        if (remove) {
          builder.getResult().getBasePLs().remove(basisPl);
        }
      }
    }
  }

  private boolean isVerschillendAdres(String zArg, String adresArg) {
    final boolean eq1 = args.isSearchOnAddress() && !zArg.equals(adresArg);
    final boolean eq2 = fil(zArg) && !zArg.equalsIgnoreCase(adresArg) || zArg.equals(" ") && fil(adresArg);
    return eq1 || eq2;
  }

  /**
   * Zoek in PROCURA DB
   */
  private void findProcuraDB() {
    log.debug("PROCURA search");
    new ProcuraDBRequestHandler(builder, user, args);
  }

  /**
   * Zoek in GBA-V
   */
  private void findGBAV() {

    final boolean isNormalArgs = getArgs().isNawGevuld() && builder.getResult().getBasePLs().isEmpty();
    final boolean isNumberArgs = getArgs().getNumbers().size() > 0;
    final boolean isMaxResults = builder.getResult().getBasePLs().size() >= getArgs().getMaxFindCount();

    if (!isMaxResults && (isNormalArgs || isNumberArgs)) {
      log.debug("GBA-V search");
      new GbavRequestHandler(builder, user, args);
    }
  }

  /**
   * Geeft de nummers terug die nog niet zijn gevonden door
   */
  private Set<PLNumber> getNotFoundNumbers(Set<PLNumber> nummers) {
    final Set<PLNumber> nrs = new LinkedHashSet<>();
    for (final PLNumber zoekNr : nummers) {
      if (!isNumberAlreadyFound(zoekNr)) {
        nrs.add(zoekNr);
      }
    }

    return nrs;
  }

  /**
   * Controleer of nummer al gevonden is. Dan hoeft deze niet door GBA-V worden gezocht
   */
  private boolean isNumberAlreadyFound(PLNumber zoekNr) {
    for (final BasePL pl : builder.getResult().getBasePLs()) {
      for (final BasePLCat soort : pl.getCats(GBACat.PERSOON,
          GBACat.VERW)) {
        for (final BasePLSet set : soort.getSets()) {
          for (final BasePLRec rec : set.getRecs()) {
            final long bsn = along(rec.getElem(GBAElem.BSN).getValue().getCode());
            final long anr = along(rec.getElem(GBAElem.ANR).getValue().getCode());
            if (anr == zoekNr.getAnr() || bsn == zoekNr.getBsn()) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  /**
   * Geeft bron terug. 0 = PROCURA + GBA-V, 1 = PROCURA, 2 = GBA-V
   */
  private int getProfielBron() {
    try {
      return aval(getProfielAttributen().getZoekVolgorde());
    } catch (final RuntimeException e) {
      log.debug(e.toString());
    }

    return 0;
  }

  private Attributen getProfielAttributen() {
    return user.getProfiel().getAttributen();
  }

  /**
   * Is PROCURA beschikbaar als bron
   */
  private boolean isProcuraBron() {
    try {
      return !ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.DB)
          .equalsIgnoreCase("nvt") && getProfielAttributen().isDatabron1();
    } catch (final RuntimeException e) {
      log.debug(e.toString());
    }
    return false;
  }

  /**
   * Is GBA-V beschikbaar als bron
   */
  private boolean isGBAVBron() {
    try {
      return pos(user.getProfiel().getGBAvProfiel().getPk()) && getProfielAttributen().isDatabron2();
    } catch (final RuntimeException e) {
      log.debug(e.toString());
    }
    return false;
  }
}
