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

import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.misc.ParmValues;
import nl.procura.gbaws.db.wrappers.ParmWrapper;

public class GbaDao extends GenericDao {

  private ProcuraGBA procuraGBA;

  public GbaDao() {
    setProcuraGBA(new ProcuraGBA());
  }

  public ProcuraGBA getProcuraGBA() {
    return procuraGBA;
  }

  public void setProcuraGBA(ProcuraGBA procura) {
    this.procuraGBA = procura;
  }

  public class ProcuraGBA implements Serializable {

    private ParmWrapper db                   = new ParmWrapper();
    private ParmWrapper name                 = new ParmWrapper();
    private ParmWrapper server               = new ParmWrapper();
    private ParmWrapper port                 = new ParmWrapper();
    private ParmWrapper username             = new ParmWrapper();
    private ParmWrapper password             = new ParmWrapper();
    private ParmWrapper connections_read_min = new ParmWrapper();
    private ParmWrapper connections_read_max = new ParmWrapper();

    public ProcuraGBA() {

      final EntityManager m = GbaWsJpa.getManager();

      m.getTransaction().begin();

      setDb(ParmDao.getParameter(ParmValues.PROCURA.DB.DB));
      setName(ParmDao.getParameter(ParmValues.PROCURA.DB.SID));
      setServer(ParmDao.getParameter(ParmValues.PROCURA.DB.SERVER));
      setPort(ParmDao.getParameter(ParmValues.PROCURA.DB.PORT));
      setUsername(ParmDao.getParameter(ParmValues.PROCURA.DB.USERNAME));
      setPassword(ParmDao.getParameter(ParmValues.PROCURA.DB.PW));
      setConnections_read_min(ParmDao.getParameter(ParmValues.PROCURA.DB.CONNECTIONS_READ_MIN));
      setConnections_read_max(ParmDao.getParameter(ParmValues.PROCURA.DB.CONNECTIONS_READ_MAX));

      m.getTransaction().commit();
    }

    public void save() {

      final EntityManager m = GbaWsJpa.getManager();

      m.getTransaction().begin();

      ParmDao.addParameter(m, getDb());
      ParmDao.addParameter(m, getName());
      ParmDao.addParameter(m, getServer());
      ParmDao.addParameter(m, getPort());
      ParmDao.addParameter(m, getUsername());
      ParmDao.addParameter(m, getPassword());
      ParmDao.addParameter(m, getConnections_read_min());
      ParmDao.addParameter(m, getConnections_read_max());

      m.getTransaction().commit();
    }

    public ParmWrapper getName() {
      return name;
    }

    public void setName(ParmWrapper name) {
      this.name = name;
    }

    public ParmWrapper getDb() {
      return db;
    }

    public void setDb(ParmWrapper db) {
      this.db = db;
    }

    public ParmWrapper getServer() {
      return server;
    }

    public void setServer(ParmWrapper server) {
      this.server = server;
    }

    public ParmWrapper getPort() {
      return port;
    }

    public void setPort(ParmWrapper port) {
      this.port = port;
    }

    public ParmWrapper getUsername() {
      return username;
    }

    public void setUsername(ParmWrapper username) {
      this.username = username;
    }

    public ParmWrapper getPassword() {
      return password;
    }

    public void setPassword(ParmWrapper password) {
      this.password = password;
    }

    public ParmWrapper getConnections_read_min() {
      return connections_read_min;
    }

    public void setConnections_read_min(ParmWrapper connections_read_min) {
      this.connections_read_min = connections_read_min;
    }

    public ParmWrapper getConnections_read_max() {
      return connections_read_max;
    }

    public void setConnections_read_max(ParmWrapper connections_read_max) {
      this.connections_read_max = connections_read_max;
    }
  }
}
