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

import nl.procura.gba.jpa.personenws.db.GbavProfile;
import nl.procura.gba.jpa.personenws.db.GbavProfileParm;
import nl.procura.gba.jpa.personenws.db.GbavProfileParmPK;
import nl.procura.gba.jpa.personenws.db.ProfileElement;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.handlers.GbavProfileDao;
import nl.procura.gbaws.db.misc.ParmValues;
import nl.procura.standard.ProcuraDate;

public class GbavProfileWrapper extends AbstractTableWrapper<GbavProfile, Integer> {

  private Attributen attributen = new Attributen();

  public GbavProfileWrapper() {
    super(new GbavProfile());
  }

  public GbavProfileWrapper(int code) {
    this(GbaWsJpa.getManager().find(GbavProfile.class, code));
  }

  public GbavProfileWrapper(GbavProfile profile) {
    super(profile);
    getAttributen().load();
  }

  @Override
  public Integer getPk() {
    return getTable().getCGbavProfile();
  }

  public void addParameter(ParmWrapper parameter) {
    addParameter(parameter.getNaam(), parameter.getWaarde());
  }

  public void addParameter(String name, String value) {

    EntityManager m = GbaWsJpa.getManager();

    final GbavProfileParmPK pk = new GbavProfileParmPK(getPk(), name);
    GbavProfileParm p = m.find(GbavProfileParm.class, pk);
    final GbavProfile u = m.find(GbavProfile.class, getPk());

    if (p == null) {
      p = new GbavProfileParm(pk);
    }

    p.setValue(trim(value));
    p = merge(m, p);
    m.refresh(u);
    setTable(u);
  }

  public void deblokkeer() {

    getTable().setBlock(BigInteger.valueOf(0));

    final EntityManager m = GbaWsJpa.getManager();
    m.getTransaction().begin();

    save(m);

    m.getTransaction().commit();
  }

  public boolean isVerlopen() {

    String aanpassingDatum = getAttributen().getAanpassingDatum();

    if (fil(aanpassingDatum)) {
      ProcuraDate expirationDate = new ProcuraDate(aanpassingDatum).addDays(90);
      return new ProcuraDate().diffInDays(expirationDate) <= 0;
    }

    return false;
  }

  @SuppressWarnings("unchecked")
  public void delete(EntityManager m) {

    final GbavProfile profile = m.find(GbavProfile.class, getTable().getCGbavProfile());

    if (pos(getPk())) {

      String sql = "select e from ProfileElement e where e.profileElementPK.database = 1 and e.profileElementPK.cRef = :code";
      final Query q = m.createQuery(sql);

      q.setParameter("code", getPk());
      final List<ProfileElement> l = q.getResultList();

      for (final ProfileElement pe : l) {
        m.remove(pe);
      }
    }

    m.remove(profile);
  }

  public void deleteAndCommit() {

    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();

    delete(m);

    m.getTransaction().commit();
  }

  public void removeParameter(String name) {

    EntityManager m = GbaWsJpa.getManager();
    final GbavProfile u = m.find(GbavProfile.class, getPk());
    final GbavProfileParmPK pk = new GbavProfileParmPK(getPk(), name);
    final GbavProfileParm p = m.find(GbavProfileParm.class, pk);

    if (p != null) {
      u.getGbavProfileParmCollection().remove(p);
      m.remove(p);
    }

    m.refresh(u);
    setTable(u);
  }

  public void save(EntityManager m) {
    setTable(merge(m, getTable()));
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

  public int getType() {
    return this.getTable().getType();
  }

  public void setType(int type) {
    this.getTable().setType(type);
  }

  public String getNaam() {
    return this.getTable().getGbavProfile();
  }

  public void setNaam(String naam) {
    this.getTable().setGbavProfile(naam);
  }

  public String getOmschrijving() {
    return this.getTable().getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    this.getTable().setDescr(omschrijving);
  }

  public ParmWrapper getParameter(String name) {

    EntityManager m = GbaWsJpa.getManager();
    final GbavProfileParmPK pk = new GbavProfileParmPK(getPk(), name);
    final GbavProfileParm p = m.find(GbavProfileParm.class, pk);

    return (p != null) ? new ParmWrapper(p.getGbavProfileParmPK().getParm(), p.getValue()) : null;
  }

  public List<ParmWrapper> getParameters() {

    final List<ParmWrapper> l = new ArrayList<>();
    for (final GbavProfileParm p : getTable().getGbavProfileParmCollection()) {
      l.add(new ParmWrapper(p.getGbavProfileParmPK().getParm(), p.getValue()));
    }

    return l;
  }

  public String getParameterWaarde(String parameter) {
    final ParmWrapper p = getParameter(parameter);
    return p != null ? trim(p.getWaarde()) : "";
  }

  public boolean isGeblokkeerd() {
    return pos(this.getTable().getBlock());
  }

  public void setGeblokkeerd(boolean b) {
    this.getTable().setBlock(BigInteger.valueOf(b ? 1 : 0));
  }

  public boolean isParameter(String name) {

    EntityManager m = GbaWsJpa.getManager();
    final GbavProfileParmPK pk = new GbavProfileParmPK(getPk(), name);
    final GbavProfileParm p = m.find(GbavProfileParm.class, pk);
    return p != null;
  }

  public boolean isSaved() {
    return getPk() != null;
  }

  public class Attributen implements Serializable {

    private static final long serialVersionUID = 1508783000459208455L;

    private String aanpassingDatum           = "";
    private String gebruikersnaam            = "";
    private String zoekEndpoint              = "";
    private String afnemerIndicatiesEndpoint = "";
    private String wachtwoordEndpoint        = "";
    private String wachtwoord                = "";

    public Attributen load() {

      setGebruikersnaam(getParameterWaarde(ParmValues.GBAV.GBAV_USERNAME));
      setWachtwoord(getParameterWaarde(ParmValues.GBAV.GBAV_PW));
      setWachtwoordEndpoint(getParameterWaarde(ParmValues.GBAV.GBAV_WW_URL));
      setZoekEndpoint(getParameterWaarde(ParmValues.GBAV.GBAV_URL));
      setAfnemerIndicatiesEndpoint(getParameterWaarde(ParmValues.GBAV.GBAV_AFN_IND_URL));
      setAanpassingDatum(getParameterWaarde(ParmValues.GBAV.GBAV_CHANGE_DATE));

      return this;
    }

    public void mergeAndCommit() {

      final EntityManager m = GbaWsJpa.getManager();

      m.getTransaction().begin();

      set(ParmValues.GBAV.GBAV_USERNAME, getGebruikersnaam());
      set(ParmValues.GBAV.GBAV_PW, getWachtwoord());
      set(ParmValues.GBAV.GBAV_WW_URL, getWachtwoordEndpoint());
      set(ParmValues.GBAV.GBAV_URL, getZoekEndpoint());
      set(ParmValues.GBAV.GBAV_AFN_IND_URL, getAfnemerIndicatiesEndpoint());
      set(ParmValues.GBAV.GBAV_CHANGE_DATE, getAanpassingDatum());

      m.getTransaction().commit();
    }

    /**
     * Sla alle gba-v profielen op met dezelfde GBA-V gebruikersnaam
     */
    public void mergeAndCommitOthers() {
      String gbavGebruiker = getAttributen().getGebruikersnaam();
      for (GbavProfileWrapper otherGbavProfiel : GbavProfileDao.getProfileByGbavUser(gbavGebruiker)) {
        if (!otherGbavProfiel.getPk().equals(getPk())) {
          otherGbavProfiel.getAttributen().setAanpassingDatum(getAttributen().getAanpassingDatum());
          otherGbavProfiel.getAttributen().setWachtwoord(getAttributen().getWachtwoord());
          otherGbavProfiel.getAttributen().mergeAndCommit();
        }
      }
    }

    private void set(String n, String v) {
      if (fil(v)) {
        addParameter(n, v);
      } else {
        removeParameter(n);
      }
    }

    public String getAanpassingDatum() {
      return aanpassingDatum;
    }

    public void setAanpassingDatum(String aanpassingDatum) {
      this.aanpassingDatum = aanpassingDatum;
    }

    public String getGebruikersnaam() {
      return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
      this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoordEndpoint() {
      return wachtwoordEndpoint;
    }

    public void setWachtwoordEndpoint(String wachtwoordEndpoint) {
      this.wachtwoordEndpoint = wachtwoordEndpoint;
    }

    public String getWachtwoord() {
      return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
      this.wachtwoord = wachtwoord;
    }

    public String getZoekEndpoint() {
      return zoekEndpoint;
    }

    public void setZoekEndpoint(String zoekEndpoint) {
      this.zoekEndpoint = zoekEndpoint;
    }

    public String getAfnemerIndicatiesEndpoint() {
      return afnemerIndicatiesEndpoint;
    }

    public void setAfnemerIndicatiesEndpoint(String afnemerIndicatiesEndpoint) {
      this.afnemerIndicatiesEndpoint = afnemerIndicatiesEndpoint;
    }
  }

  public class GbavProfielElement implements Serializable {

    private static final long serialVersionUID = 6001569522882032498L;
    private int               code_cat         = 0;
    private int               code_element     = 0;

    public GbavProfielElement(int code_cat, int code_element) {
      setCode_cat(code_cat);
      setCode_element(code_element);
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
  }
}
