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

import java.io.Serializable;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personenws.db.Parm;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.misc.ParmValues;
import nl.procura.gbaws.db.wrappers.ParmWrapper;
import nl.procura.gbaws.db.wrappers.ProcuraDbWrapper;

public class ParmDao implements Serializable {

  public static ParmWrapper getParameter(String name) {

    final EntityManager m = GbaWsJpa.getManager();

    Parm p;
    p = m.find(Parm.class, name);

    if (p != null) {
      return new ParmWrapper(p.getParm(), p.getValue());
    }

    return new ParmWrapper(name, "");
  }

  public static boolean isParameter(String name) {
    final EntityManager m = GbaWsJpa.getManager();
    return m.find(Parm.class, name) != null;
  }

  public static String getParameterWaarde(String parameter) {
    return getParameter(parameter).getWaarde();
  }

  public static void addParameter(EntityManager m, ParmWrapper parameter) {
    addParameter(m, parameter.getNaam(), parameter.getWaarde());
  }

  public static void addParameter(EntityManager m, String name, String value) {

    Parm p = m.find(Parm.class, name);

    if (p == null) {
      p = new Parm(name);
    }

    p.setValue(value);
    m.merge(p);
  }

  public static ProcuraDbWrapper getProcuraDb() {

    ProcuraDbWrapper db = new ProcuraDbWrapper();

    db.setDatabase(ParmDao.getParameter(ParmValues.PROCURA.DB.DB).getWaarde());
    db.setSid(ParmDao.getParameter(ParmValues.PROCURA.DB.SID).getWaarde());
    db.setServer(ParmDao.getParameter(ParmValues.PROCURA.DB.SERVER).getWaarde());
    db.setPort(ParmDao.getParameter(ParmValues.PROCURA.DB.PORT).getWaarde());
    db.setTnsAdminDir(ParmDao.getParameter(ParmValues.PROCURA.DB.TNS_ADMIN_DIR).getWaarde());
    db.setUrl(ParmDao.getParameter(ParmValues.PROCURA.DB.CUSTOM_URL).getWaarde());
    db.setDriver(ParmDao.getParameter(ParmValues.PROCURA.DB.CUSTOM_DRIVER).getWaarde());
    db.setUsername(ParmDao.getParameter(ParmValues.PROCURA.DB.USERNAME).getWaarde());
    db.setPassword(ParmDao.getParameter(ParmValues.PROCURA.DB.PW).getWaarde());
    db.setMinConnections(ParmDao.getParameter(ParmValues.PROCURA.DB.CONNECTIONS_READ_MIN).getWaarde());
    db.setMaxConnections(ParmDao.getParameter(ParmValues.PROCURA.DB.CONNECTIONS_READ_MAX).getWaarde());

    return db;
  }

  public static void mergeAndCommitProcuraDb(ProcuraDbWrapper procuraDb) {

    final EntityManager em = GbaWsJpa.getManager();

    em.getTransaction().begin();

    addParameter(em, ParmValues.PROCURA.DB.DB, procuraDb.getDatabase());
    addParameter(em, ParmValues.PROCURA.DB.SID, procuraDb.getSid());
    addParameter(em, ParmValues.PROCURA.DB.SERVER, procuraDb.getServer());
    addParameter(em, ParmValues.PROCURA.DB.PORT, procuraDb.getPort());
    addParameter(em, ParmValues.PROCURA.DB.TNS_ADMIN_DIR, procuraDb.getTnsAdminDir());
    addParameter(em, ParmValues.PROCURA.DB.CUSTOM_URL, procuraDb.getUrl());
    addParameter(em, ParmValues.PROCURA.DB.CUSTOM_DRIVER, procuraDb.getDriver());
    addParameter(em, ParmValues.PROCURA.DB.USERNAME, procuraDb.getUsername());
    addParameter(em, ParmValues.PROCURA.DB.PW, procuraDb.getPassword());
    addParameter(em, ParmValues.PROCURA.DB.CONNECTIONS_READ_MIN, procuraDb.getMinConnections());
    addParameter(em, ParmValues.PROCURA.DB.CONNECTIONS_READ_MAX, procuraDb.getMaxConnections());

    em.getTransaction().commit();
  }
}
