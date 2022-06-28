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

package nl.procura.gba.web.services.beheer.log;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_LOGIN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.LogDao;
import nl.procura.gba.jpa.personen.db.Log;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.standard.Globalfunctions;

public class LogService extends AbstractService {

  public LogService() {
    super("Log");
  }

  public void addLoginAttempt(String userAgent, String remoteAddress, String username, Gebruiker gebruiker) {
    if (!ignoreLogin(gebruiker)) {
      InLogpoging log = new InLogpoging();
      log.setDatumTijd(new DateTime());
      log.setUsr(gebruiker != null ? gebruiker.getGebruikersnaam() : username);
      log.setUsrBean(findEntity(Usr.class, (gebruiker != null ? gebruiker.getCUsr() : 0L)));
      log.setBlok(BigDecimal.valueOf(gebruiker != null && gebruiker.isGeblokkeerd() ? 1 : 0));
      log.setIp(remoteAddress);
      log.setUserAgent(userAgent);
      saveEntity(log);
    }
  }

  private static boolean ignoreLogin(Gebruiker gebruiker) {
    if (gebruiker == null) {
      return false;
    }
    String protIgnoreParameter = gebruiker.getParameters().get(PROT_IGNORE_LOGIN).getValue();
    return Globalfunctions.isTru(protIgnoreParameter);
  }

  public List<InLogpoging> getLogs(long dIn, long dEnd) {

    String systemAccountsParm = Services.getInstance().getParameterService().getSysteemParameter(
        PROT_IGNORE_LOGIN).getValue();

    List<String> systemAccountList = Arrays.asList(systemAccountsParm.split("\\s*,\\s*"));
    List<InLogpoging> list = new ArrayList<>();

    for (Log log : LogDao.findLogs(dIn, dEnd)) {
      if (!systemAccountList.contains(log.getUsr())) {
        InLogpoging impl = copy(log, InLogpoging.class);
        impl.setGebruiker(new UsrFieldValue(log.getUsrBean().getCUsr(), log.getUsrBean().getUsrfullname()));
        list.add(impl);
      }
    }

    return list;
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van de inlogpogingen")
  public void delete(List<InLogpoging> inlogpogingen) {
    for (InLogpoging inlogpoging : inlogpogingen) {
      removeEntity(inlogpoging);
    }
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van de inlogpogingen")
  public int delete(long min, long max) {
    return LogDao.removeLogs(min, max);
  }
}
