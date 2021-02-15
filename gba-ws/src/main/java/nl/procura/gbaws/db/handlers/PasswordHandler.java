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

package nl.procura.gbaws.db.handlers;

import static nl.procura.standard.Globalfunctions.*;

import javax.persistence.EntityManager;

import nl.procura.diensten.gbav.utils.GbavAntwoord;
import nl.procura.diensten.gbav.utils.GbavService;
import nl.procura.diensten.gbav.utils.acties.GbavWachtwoordActie;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.EmailConfigWrapper;
import nl.procura.gbaws.db.wrappers.EmailLogWrapper;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper.Attributen;
import nl.procura.gbaws.mail.EmailNewPass;
import nl.procura.standard.ProcuraDate;

public class PasswordHandler {

  public static GbavAntwoord send(GbavProfileWrapper dao, String nieuwWachtwoord) {

    if (fil(nieuwWachtwoord)) {

      Attributen attributen = dao.getAttributen();
      final String gebruikersnaam = attributen.getGebruikersnaam();
      final String huidigWachtwoord = attributen.getWachtwoord();

      final String endpointZoek = attributen.getZoekEndpoint();
      final String endpointWw = attributen.getWachtwoordEndpoint();
      String endpoint = fil(endpointWw) ? endpointWw : endpointZoek;

      final GbavService gbav = new GbavService(gebruikersnaam, huidigWachtwoord, endpoint);
      final GbavWachtwoordActie actie = gbav.getActies().getWachtwoordActie();

      GbavAntwoord antwoord = actie.wijzig(nieuwWachtwoord);
      nl.procura.diensten.gbav.utils.GbavResultaat result = antwoord.getResultaat();

      if (!result.isFout()) {
        opslaan(dao, nieuwWachtwoord);
        newEmailLogRecord(gebruikersnaam, nieuwWachtwoord);
      }

      return antwoord;
    }

    return null;
  }

  private static void newEmailLogRecord(String gebruiker, String ww) {

    EmailConfigWrapper config = EmailConfigDao.getConfig();

    if (pos(config.getPk()) && config.isTypeNieuwWW()) {

      final String d_wijz = new ProcuraDate().getFormatDate();
      final String d_end = new ProcuraDate().addDays(90).getFormatDate();

      final EmailLogWrapper log = new EmailLogWrapper();
      log.save(new EmailNewPass(gebruiker, ww, d_wijz, d_end));
      log.send();
    }
  }

  private static void opslaan(GbavProfileWrapper dao, String nieuwWachtwoord) {

    if (fil(nieuwWachtwoord)) {

      final EntityManager m = GbaWsJpa.getManager();
      m.getTransaction().begin();

      dao.getAttributen().setWachtwoord(nieuwWachtwoord);
      dao.getAttributen().setAanpassingDatum(getSystemDate());
      dao.save(m);

      m.getTransaction().commit();
      dao.getAttributen().mergeAndCommit();
      dao.getAttributen().mergeAndCommitOthers();
    }
  }
}
