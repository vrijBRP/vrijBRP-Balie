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

package nl.procura.gbaws.db.jpa;

import static nl.procura.standard.Globalfunctions.aval;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpa;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaEclipseLink;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.gbaws.db.handlers.ParmDao;
import nl.procura.gbaws.db.misc.ParmValues;

public class PleJpaUtils {

  private static PLEJpa jpa = new PLEJpa();

  public static PLEJpa getJpa() {
    return jpa;
  }

  public static PLEJpaManager createManager() {
    return jpa.createManager();
  }

  public static void init() {

    String database = ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.DB);
    if (isNotBlank(database) && !jpa.isConnected()) {
      PLEJpaEclipseLink jpaImpl = new PLEJpaEclipseLink();
      jpaImpl.setDatabase(database);
      jpaImpl.setSid(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.SID));
      jpaImpl.setServer(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.SERVER));
      jpaImpl.setPort(aval(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.PORT)));
      jpaImpl.setTnsAdminDir(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.TNS_ADMIN_DIR));
      jpaImpl.setCustomUrl(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.CUSTOM_URL));
      jpaImpl.setCustomDriver(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.CUSTOM_DRIVER));
      jpaImpl.setUsername(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.USERNAME));
      jpaImpl.setPassword(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.PW));
      jpaImpl.setMaxConnections(aval(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.CONNECTIONS_READ_MAX)));
      jpaImpl.setMinConnections(aval(ParmDao.getParameterWaarde(ParmValues.PROCURA.DB.CONNECTIONS_READ_MIN)));

      jpa.setJpaImplementation(jpaImpl);
      jpa.connect("probev-jpa");
      jpa.createManager().close();
    }
  }
}
