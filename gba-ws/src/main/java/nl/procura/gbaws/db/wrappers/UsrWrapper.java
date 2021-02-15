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

package nl.procura.gbaws.db.wrappers;

import static nl.procura.standard.Globalfunctions.pos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personenws.db.*;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;

public class UsrWrapper extends AbstractTableWrapper<Usr, Integer> {

  private static final Logger LOGGER     = LoggerFactory.getLogger(UsrWrapper.class);
  private Attributen          attributen = new Attributen();

  public UsrWrapper() {
    this(new Usr());
  }

  public UsrWrapper(int code) {
    this(GbaWsJpa.getManager().find(Usr.class, code));
  }

  public UsrWrapper(Usr table) {
    super(table);
  }

  @Override
  public Integer getPk() {
    return getTable().getCUsr();
  }

  public void addParameter(EntityManager m, ParmWrapper parameter) {
    addParameter(m, parameter.getNaam(), parameter.getWaarde());
  }

  public void addParameter(EntityManager m, String name, String value) {

    final UsrParmPK pk = new UsrParmPK(getPk(), name);
    UsrParm p = m.find(UsrParm.class, pk);
    final Usr u = m.find(Usr.class, getPk());

    if (p == null) {
      p = new UsrParm(pk);
      u.getUsrParmCollection().add(p);
    }

    p.setValue(value);
    p = merge(m, p);
    m.refresh(u);
    setTable(u);
  }

  public void delete(EntityManager m) {

    final Usr usr = m.find(Usr.class, getTable().getCUsr());

    for (final UsrParm p : usr.getUsrParmCollection()) {
      m.remove(p);
    }
    for (final Request r : usr.getRequestCollection()) {
      m.remove(r);
    }

    m.remove(usr);
  }

  public void deleteAndCommit() {
    final EntityManager m = GbaWsJpa.getManager();
    m.getTransaction().begin();
    delete(m);
    m.getTransaction().commit();
  }

  public void removeParameter(EntityManager m, String name) {
    final Usr u = m.find(Usr.class, getPk());
    final UsrParmPK pk = new UsrParmPK(getPk(), name);
    final UsrParm p = m.find(UsrParm.class, pk);

    if (p != null) {
      u.getUsrParmCollection().remove(p);
      m.remove(p);
    }

    m.refresh(u);
    setTable(u);
  }

  @Override
  public String toString() {
    return getTable().getDisplay();
  }

  public Attributen getAttributen() {
    return attributen;
  }

  public void setAttributen(Attributen attributen) {
    this.attributen = attributen;
  }

  public ParmWrapper getParameter(EntityManager m, String name) {
    final UsrParmPK pk = new UsrParmPK(getPk(), name);
    final UsrParm p = m.find(UsrParm.class, pk);

    if (p != null) {
      return new ParmWrapper(p.getUsrParmPK().getParm(), p.getValue());
    }
    return null;
  }

  public List<ParmWrapper> getParameters() {

    final List<ParmWrapper> l = new ArrayList<>();

    for (final UsrParm p : getTable().getUsrParmCollection()) {
      l.add(new ParmWrapper(p.getUsrParmPK().getParm(), p.getValue()));
    }
    return l;
  }

  public String getParameterWaarde(EntityManager m, String parameter) {
    final ParmWrapper p = getParameter(m, parameter);

    return p != null ? p.getWaarde() : "";
  }

  public ProfileWrapper getProfiel() {
    if (getTable().getCProfile() != null) {
      return new ProfileWrapper(getTable().getCProfile());
    }
    return null;
  }

  public List<RequestWrapper> getWSVragen() {

    final List<RequestWrapper> l = new ArrayList<>();

    for (final Request r : getTable().getRequestCollection()) {
      l.add(new RequestWrapper(r));
    }
    return l;
  }

  public String getGebruikersNaam() {
    return getTable().getUsr();
  }

  public void setGebruikersNaam(String naam) {
    getTable().setUsr(naam);
  }

  public String getVolledigeNaam() {
    return getTable().getDisplay();
  }

  public void setVolledigeNaam(String volledigeNaam) {
    this.getTable().setDisplay(volledigeNaam);
  }

  public String getWachtwoord() {
    return getTable().getPw();
  }

  public void setWachtwoord(String wachtwoord) {
    this.getTable().setPw(wachtwoord);
  }

  public boolean isAdmin() {
    return pos(this.getTable().getAdmin());
  }

  public void setAdmin(boolean admin) {
    getTable().setAdmin(BigInteger.valueOf(admin ? 1 : 0));
  }

  public boolean isParameter(EntityManager m, String name) {
    final UsrParmPK pk = new UsrParmPK(getPk(), name);
    final UsrParm p = m.find(UsrParm.class, pk);

    return p != null;
  }

  public boolean isPassword(String input) {
    try {
      return this.getTable().getPw().equals(input);
    } catch (final RuntimeException e) {

      LOGGER.debug(e.toString());
      return false;
    }
  }

  public boolean isProfiel() {
    return getProfiel() != null;
  }

  public void setProfiel(ProfileWrapper profiel) {

    final EntityManager m = GbaWsJpa.getManager();
    Profile p = null;

    if (profiel != null) {
      p = m.find(Profile.class, profiel.getPk());
    }

    this.getTable().setCProfile(p);
  }

  public boolean isSaved() {
    return getPk() != null;
  }

  public void setUsr(String usr) {
    this.getTable().setUsr(usr);
  }

  public class Attributen implements Serializable {

    private static final long serialVersionUID = 1039281588485150858L;

    public void save(EntityManager m) {
      m.getTransaction().commit();
      m.getTransaction().begin();
    }
  }
}
