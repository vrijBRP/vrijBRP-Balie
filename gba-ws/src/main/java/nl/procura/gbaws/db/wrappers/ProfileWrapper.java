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

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ObjectLevelReadQuery;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personenws.db.*;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.enums.PersonenWsDatabaseType;
import nl.procura.gbaws.db.misc.ParmValues;

public class ProfileWrapper extends AbstractTableWrapper<Profile, Integer> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileWrapper.class);

  private Attributen attributen = new Attributen();

  public ProfileWrapper() {
    super(new Profile());
  }

  public ProfileWrapper(int code) {
    this(GbaWsJpa.getManager().find(Profile.class, code));
  }

  public ProfileWrapper(Profile table) {
    super(table);
    getAttributen().load();
  }

  @Override
  public Integer getPk() {
    return getTable().getCProfile();
  }

  public void addElement(int catCode, int elemCode, PersonenWsDatabaseType database, int c_ref) {

    EntityManager m = GbaWsJpa.getManager();
    final ProfileElementPK pk = getProfileElementPK(catCode, elemCode, database.getCode(), c_ref);
    final ProfileElement pe = m.find(ProfileElement.class, pk);

    if (pe != null) {
      return;
    }

    ProfileElement npe = new ProfileElement(pk);

    npe = merge(m, npe);

    setTable(m.find(Profile.class, getTable().getCProfile()));
  }

  public void addGebruiker(UsrWrapper gebruiker) {

    EntityManager m = GbaWsJpa.getManager();

    if (pos(gebruiker.getPk())) {

      Usr g = m.find(Usr.class, gebruiker.getPk());
      g.setCProfile(getTable());
      g = merge(m, g);
      setTable(m.find(Profile.class, getPk()));
    }
  }

  public void addParameter(String name, String value) {

    EntityManager m = GbaWsJpa.getManager();
    m.getTransaction().begin();

    ProfileParmPK pk = new ProfileParmPK(getPk(), name);
    ProfileParm p = m.find(ProfileParm.class, pk);
    Profile u = m.find(Profile.class, getPk());

    if (p == null) {
      p = new ProfileParm(pk);
      u.getProfileParmCollection().add(p);
    }

    p.setValue(value);
    m.merge(p);
    m.refresh(u);
    setTable(u);

    m.getTransaction().commit();
  }

  public void delete(EntityManager m) {
    final Profile profile = m.find(Profile.class, getTable().getCProfile());

    for (final ProfileParm parm : profile.getProfileParmCollection()) {
      m.remove(parm);
    }
    for (final Usr usr : profile.getUsrCollection()) {
      usr.setCProfile(null);
    }

    m.remove(profile);
  }

  public void deleteAndCommit() {
    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();
    delete(m);
    m.getTransaction().commit();
  }

  public void removeElement(int catCode, int elemCode, PersonenWsDatabaseType database, int c_ref) {

    EntityManager m = GbaWsJpa.getManager();

    final ProfileElementPK pk = getProfileElementPK(catCode, elemCode, database.getCode(), c_ref);
    final ProfileElement pe = m.find(ProfileElement.class, pk);

    if (pe != null) {
      m.remove(pe);
      final Profile profile = m.find(Profile.class, getTable().getCProfile());
      setTable(profile);
    }
  }

  public void removeElements(int c_profile) {
    removeElements(c_profile, PersonenWsDatabaseType.PROCURA, 0);
  }

  @SuppressWarnings("unchecked")
  public void removeElements(int c_profile, PersonenWsDatabaseType databaseType, int c_ref) {

    EntityManager m = GbaWsJpa.getManager();

    final String sql = "select e from ProfileElement e where e.profileElementPK.cProfile = :c_profile "
        + "and e.profileElementPK.database = :c_database and e.profileElementPK.cRef = :c_ref";
    final Query q = m.createQuery(sql);

    q.setParameter("c_profile", c_profile);
    q.setParameter("c_database", databaseType.getCode());
    q.setParameter("c_ref", c_ref);
    final List<ProfileElement> pe = q.getResultList();

    for (final ProfileElement e : pe) {
      m.remove(e);
    }
    final Profile profile = m.find(Profile.class, getTable().getCProfile());

    setTable(profile);
  }

  public void removeGebruiker(UsrWrapper gebruiker) {

    if (pos(gebruiker.getPk())) {

      EntityManager m = GbaWsJpa.getManager();
      final Usr g = m.find(Usr.class, gebruiker.getPk());

      g.setCProfile(null);
      m.merge(g);
      setTable(m.find(Profile.class, getPk()));
    }
  }

  public void removeParameter(String name) {

    EntityManager m = GbaWsJpa.getManager();
    final Profile u = m.find(Profile.class, getPk());
    final ProfileParmPK pk = new ProfileParmPK(getPk(), name);
    final ProfileParm p = m.find(ProfileParm.class, pk);

    if (p != null) {
      u.getProfileParmCollection().remove(p);
      m.remove(p);
    }

    m.refresh(u);
    setTable(u);
  }

  public void save(EntityManager m) {
    setTable(m.merge(getTable()));
    m.flush();
  }

  public void saveAndCommit() {
    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();
    save(m);
    m.getTransaction().commit();
  }

  @Override
  public String toString() {
    return getNaam() + " (" + getOmschrijving() + ")";
  }

  public Attributen getAttributen() {
    return attributen;
  }

  public void setAttributen(Attributen attributen) {
    this.attributen = attributen;
  }

  @SuppressWarnings("unchecked")
  public List<ProfielElement> getElementen(int database, int database_ref) {

    final List<ProfielElement> elementen = new ArrayList<>();
    final Expression e3 = new ExpressionBuilder().get("profileElementPK").get("cRef").equal(database_ref);
    final Expression e2 = new ExpressionBuilder().get("profileElementPK").get("database").equal(database);
    final Expression e1 = new ExpressionBuilder().get("profileElementPK").get("cProfile").equal(
        getTable().getCProfile()).and(e2).and(e3);
    final ReadAllQuery query = new ReadAllQuery();

    query.setReferenceClass(ProfileElement.class);
    query.setSelectionCriteria(e1);
    query.setCacheUsage(ObjectLevelReadQuery.CheckCacheThenDatabase);
    query.cacheStatement();

    for (final ProfileElement pe : (List<ProfileElement>) GbaWsJpa.getSession().executeQuery(query)) {
      if (pe.getProfileElementPK().getDatabase().intValue() != database
          || pe.getProfileElementPK().getCRef().intValue() != database_ref) {
        continue;
      }

      ProfielElement oe = new ProfielElement();

      String e = pe.getProfileElementPK().getElement();
      oe.setElement(e);

      e = e.replaceAll("\\D", "");
      if (e.length() == 6) {

        oe.setCode_cat(aval(e.substring(0, 2)));
        oe.setCode_element(aval(e.substring(2, 6)));

        elementen.add(oe);
      }
    }

    return elementen;
  }

  public GbavProfileWrapper getGBAvProfiel() {
    try {
      final GbavProfileWrapper p = new GbavProfileWrapper(
          aval(getParameterWaarde(ParmValues.PROFILE.CODE_GBAV_PROFILE)));
      if (p.getTable() != null) {
        return p;
      }
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
    }

    return null;
  }

  public void setGBAvProfiel(GbavProfileWrapper p) {
    addParameter(ParmValues.PROFILE.CODE_GBAV_PROFILE, astr(p.getPk()));
  }

  public List<UsrWrapper> getGebruikers() {
    final List<UsrWrapper> l = new ArrayList<>();
    if (getTable().getUsrCollection() != null) {
      for (final Usr usr : getTable().getUsrCollection()) {
        if (usr != null && pos(usr.getCUsr())) {
          l.add(new UsrWrapper(usr));
        }
      }
    }
    return l;
  }

  public String getNaam() {
    return getTable().getProfile();
  }

  public void setNaam(String naam) {
    getTable().setProfile(naam);
  }

  public String getOmschrijving() {
    return getTable().getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    getTable().setDescr(omschrijving);
  }

  public ParmWrapper getParameter(String name) {

    EntityManager m = GbaWsJpa.getManager();

    final ProfileParmPK pk = new ProfileParmPK(getPk(), name);
    final ProfileParm p = m.find(ProfileParm.class, pk);

    if (p != null) {
      return new ParmWrapper(p.getProfileParmPK().getParm(), p.getValue());
    }
    return null;
  }

  public List<ParmWrapper> getParameters() {

    final List<ParmWrapper> l = new ArrayList<>();
    for (final ProfileParm p : getTable().getProfileParmCollection()) {
      l.add(new ParmWrapper(p.getProfileParmPK().getParm(), p.getValue()));
    }
    return l;
  }

  public String getParameterWaarde(String parameter) {
    final ParmWrapper p = getParameter(parameter);
    return p != null ? p.getWaarde() : "";
  }

  private ProfileElementPK getProfileElementPK(int c_cat, int c_elem, int database, int c_ref) {
    final String e = String.format("%02d%04d", c_cat, c_elem);
    final String trim_elem = e.substring(0, 2) + "." + e.substring(2, 4) + "." + e.substring(4, 6);
    final ProfileElementPK pk = new ProfileElementPK();

    pk.setCProfile(getPk());
    pk.setElement(trim_elem);
    pk.setDatabase(BigInteger.valueOf(database));
    pk.setCRef(BigInteger.valueOf(c_ref));
    return pk;
  }

  public boolean isGebruiker(EntityManager m, UsrWrapper gebruiker) {
    final Profile p = m.find(Profile.class, getPk());

    if (pos(gebruiker.getPk()) && getTable().getUsrCollection() != null) {
      return p.getUsrCollection().contains(gebruiker.getTable());
    }

    return false;
  }

  public boolean isParameter(EntityManager m, String name) {
    final ProfileParmPK pk = new ProfileParmPK(getPk(), name);
    final ProfileParm p = m.find(ProfileParm.class, pk);

    return p != null;
  }

  public boolean isSaved() {
    return getPk() != null;
  }

  public class Attributen implements Serializable {

    private static final long serialVersionUID = 7489052420988094455L;
    private int               zoekVolgorde     = 0;
    private int               gbavBron         = 0;
    private boolean           databron1        = false;
    private boolean           databron2        = false;

    public void load() {

      setGbavBron(aval(getParameterWaarde(ParmValues.PROFILE.CODE_GBAV_PROFILE)));
      setZoekVolgorde(aval(getParameterWaarde(ParmValues.PROFILE.SEARCH_ORDER)));
      setDatabron1(isTru(getParameterWaarde(ParmValues.PROFILE.ENABLE_DATABRON_1)));
      setDatabron2(isTru(getParameterWaarde(ParmValues.PROFILE.ENABLE_DATABRON_2)));
    }

    public void mergeAndCommit() {

      set(ParmValues.PROFILE.CODE_GBAV_PROFILE, astr(getGbavBron()));
      set(ParmValues.PROFILE.SEARCH_ORDER, astr(getZoekVolgorde()));
      set(ParmValues.PROFILE.ENABLE_DATABRON_1, isDatabron1() ? "1" : "0");
      set(ParmValues.PROFILE.ENABLE_DATABRON_2, isDatabron2() ? "1" : "0");
    }

    public int getZoekVolgorde() {
      return zoekVolgorde;
    }

    public void setZoekVolgorde(int zoekVolgorde) {
      this.zoekVolgorde = zoekVolgorde;
    }

    private void set(String n, String v) {

      if (fil(v)) {
        addParameter(n, v);
      } else {
        removeParameter(n);
      }
    }

    public boolean isDatabron1() {
      return databron1;
    }

    public void setDatabron1(boolean databron1) {
      this.databron1 = databron1;
    }

    public boolean isDatabron2() {
      return databron2;
    }

    public void setDatabron2(boolean databron2) {
      this.databron2 = databron2;
    }

    public int getGbavBron() {
      return gbavBron;
    }

    public void setGbavBron(int gbavBron) {
      this.gbavBron = gbavBron;
    }
  }

  public class ProfielElement {

    private String element      = "";
    private int    code_cat     = 0;
    private int    code_element = 0;

    public ProfielElement() {
    }

    @Override
    public String toString() {
      return astr(getCode_cat() + " " + getCode_element());
    }

    public int getCode_cat() {
      return code_cat;
    }

    public void setCode_cat(int code_cat) {
      this.code_cat = code_cat;
    }

    public int getCode_element() {
      return code_element;
    }

    public void setCode_element(int code_element) {
      this.code_element = code_element;
    }

    public String getElement() {
      return element;
    }

    public void setElement(String element) {
      this.element = element;
    }
  }
}
