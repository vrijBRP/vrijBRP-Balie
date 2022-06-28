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

package nl.procura.gba.web.services.bs.huwelijk;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter.filter;
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossHuw;
import nl.procura.gba.jpa.personen.db.HuwLocatie;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNaamgebruik;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierHuwelijk extends DossHuw implements ZaakDossier, DossierNaamgebruik, DossierMetAkte {

  private static final long serialVersionUID = -3460297576993570131L;

  private Dossier                    dossier          = new Dossier(ZaakType.HUWELIJK_GPS_GEMEENTE);
  private List<DossierHuwelijkOptie> opties           = new UniqueList<>();
  private HuwelijksLocatie           huwelijksLocatie = HuwelijksLocatie.getDefault();

  private FieldValue rechtPartner1     = new FieldValue();
  private FieldValue rechtPartner2     = new FieldValue();
  private FieldValue titelPartner1     = new FieldValue();
  private FieldValue titelPartner2     = new FieldValue();
  private FieldValue huwelijksGemeente = new FieldValue();

  public DossierHuwelijk() {

    super();

    setDossier(new Dossier(ZaakType.HUWELIJK_GPS_GEMEENTE, this));
    getDossier().toevoegenPersoon(PARTNER1);
    getDossier().toevoegenPersoon(PARTNER2);
    getDossier().toevoegenPersoon(AMBTENAAR).setVolgorde(1L);
    getDossier().toevoegenPersoon(AMBTENAAR).setVolgorde(2L);
    getDossier().toevoegenPersoon(AMBTENAAR).setVolgorde(3L);
  }

  @Override
  public void beforeSave() {
    setCDossHuw(getDossier().getCode());
  }

  @Override
  public String getAkteAanduiding() {
    return "A"; // Huwelijk / Partnerschap
  }

  @Override
  public DateTime getAkteDatum() {
    return getDatumVerbintenis();
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {

    List<DossierPersoon> personen = new ArrayList<>();

    personen.add(getPartner1());
    personen.add(getPartner2());
    personen.add(getAmbtenaar3());
    personen.addAll(getGetuigen());

    return personen;
  }

  public DossierPersoon getAmbtenaar1() {
    return getDossier().getPersoon(filter(0, AMBTENAAR));
  }

  public DossierPersoon getAmbtenaar2() {
    return getDossier().getPersoon(filter(1, AMBTENAAR));
  }

  public DossierPersoon getAmbtenaar3() {
    return getDossier().getPersoon(filter(2, AMBTENAAR));
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getPartner1().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getPartner1().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getPartner1().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getPartner1().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossHuw();
  }

  public DateTime getDatumVerbintenis() {
    return new DateTime(getDSl(), getTSl());
  }

  public void setDatumVerbintenis(DateTime dateTime) {
    setDSl(BigDecimal.valueOf(dateTime.getLongDate()));

    if (dateTime.getLongDate() > 0) {
      getDossier().setDatumIngang(dateTime);
    }
  }

  public DateTime getDatumVoornemen() {
    return new DateTime(getDVn());
  }

  public void setDatumVoornemen(DateTime dateTime) {
    setDVn(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public List<DocumentNationaliteit> getDocumentNationaliteiten() {

    List<DocumentNationaliteit> list = new ArrayList<>();

    boolean vrouw1 = Geslacht.VROUW.equals(getPartner1().getGeslacht());
    boolean vrouw2 = Geslacht.VROUW.equals(getPartner2().getGeslacht());

    if (getSoortVerbintenis() == SoortVerbintenis.GPS) {
      addDocumentNationaliteit(list, "eerstgenoemde partner", getPartner1());
      addDocumentNationaliteit(list, "laatstgenoemde partner", getPartner2());
    } else {
      addDocumentNationaliteit(list, "eerstgenoemde " + (vrouw1 ? "echtgenote" : "echtgenoot"), getPartner1());
      addDocumentNationaliteit(list, "laatstgenoemde " + (vrouw2 ? "echtgenote" : "echtgenoot"), getPartner2());
    }

    return list;
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  public DateTime getEinddatumStatus() {
    return new DateTime(getDEndStatus());
  }

  public void setEinddatumStatus(DateTime dt) {
    setDEndStatus(BigDecimal.valueOf(dt.getLongDate()));
  }

  public int getGemeenteGetuigen() {
    return aval(getGemGetuigen());
  }

  public void setGemeenteGetuigen(int gemeenteGetuigen) {
    setGemGetuigen(BigDecimal.valueOf(gemeenteGetuigen));
  }

  public List<DossierPersoon> getGetuigen() {
    return getDossier().getPersonen(GETUIGE);
  }

  public String getGetuigenSamenvatting() {
    return getDossier().getPersonenSamenvatting(DossierPersoonType.GETUIGE);
  }

  public FieldValue getHuwelijksGemeente() {
    return huwelijksGemeente;
  }

  public void setHuwelijksGemeente(FieldValue huwelijksGemeente) {
    this.huwelijksGemeente = FieldValue.from(huwelijksGemeente);
    setcHuwGem(this.huwelijksGemeente.getBigDecimalValue());
  }

  public HuwelijksLocatie getHuwelijksLocatie() {
    return huwelijksLocatie;
  }

  public void setHuwelijksLocatie(HuwelijksLocatie huwelijksLocatie) {
    this.huwelijksLocatie = huwelijksLocatie;
    setHuwLocatie(
        huwelijksLocatie == null ? null : ReflectionUtil.deepCopyBean(HuwLocatie.class, huwelijksLocatie));
  }

  @Override
  public String getNaamGebruikPartner1() {
    return getP1Ng();
  }

  @Override
  public void setNaamGebruikPartner1(String ng) {
    setP1Ng(ng);
  }

  @Override
  public String getNaamGebruikPartner2() {
    return getP2Ng();
  }

  @Override
  public void setNaamGebruikPartner2(String ng) {
    setP2Ng(ng);
  }

  @Override
  public String getNaamPartner1() {
    return getP1Naam();
  }

  @Override
  public void setNaamPartner1(String naam) {
    setP1Naam(naam);
  }

  @Override
  public String getNaamPartner2() {
    return getP2Naam();
  }

  @Override
  public void setNaamPartner2(String naam) {
    setP2Naam(naam);
  }

  public List<DossierHuwelijkOptie> getOpties() {
    return opties;
  }

  public void setOpties(List<DossierHuwelijkOptie> opties) {
    this.opties = opties;
  }

  @Override
  public DossierPersoon getPartner1() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(DossierPersoonType.PARTNER1));
  }

  public DossierPersoon getPartner1Ouder1() {
    return getOuderPersoon(getPartner1().getPersoon(filter(0, OUDER)));
  }

  public DossierPersoon getPartner1Ouder2() {
    return getOuderPersoon(getPartner1().getPersoon(filter(1, OUDER)));
  }

  @Override
  public DossierPersoon getPartner2() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(DossierPersoonType.PARTNER2));
  }

  public DossierPersoon getPartner2Ouder1() {
    return getOuderPersoon(getPartner2().getPersoon(filter(0, OUDER)));
  }

  public DossierPersoon getPartner2Ouder2() {
    return getOuderPersoon(getPartner2().getPersoon(filter(1, OUDER)));
  }

  @Override
  public List<DossierPersoon> getPartners() {
    return asList(getPartner1(), getPartner2());
  }

  public FieldValue getRechtPartner1() {
    return rechtPartner1;
  }

  public void setRechtPartner1(FieldValue rechtPartner1) {
    this.rechtPartner1 = FieldValue.from(rechtPartner1);
    setP1Recht(this.rechtPartner1.getBigDecimalValue());
  }

  public FieldValue getRechtPartner2() {
    return rechtPartner2;
  }

  public void setRechtPartner2(FieldValue rechtPartner2) {
    this.rechtPartner2 = FieldValue.from(rechtPartner2);
    setP2Recht(this.rechtPartner2.getBigDecimalValue());
  }

  public SoortVerbintenis getSoortVerbintenis() {
    return SoortVerbintenis.get(getSoortVb());
  }

  public void setSoortVerbintenis(SoortVerbintenis soort) {
    setSoortVb(soort.getCode());
  }

  public StatusVerbintenis getStatusVerbintenis() {
    return StatusVerbintenis.get(getStatusSl());
  }

  public void setStatusVerbintenis(StatusVerbintenis vb) {
    setStatusSl(vb != null ? vb.getCode() : "");
  }

  public DateTime getTijdVerbintenis() {
    return new DateTime(0, along(getTSl()));
  }

  public void setTijdVerbintenis(DateTime dt) {
    setTSl(BigDecimal.valueOf(dt.getLongTime()));
  }

  @Override
  public FieldValue getTitelPartner1() {
    return titelPartner1;
  }

  @Override
  public void setTitelPartner1(FieldValue titel) {
    this.titelPartner1 = FieldValue.from(titel);
    setP1Titel(titel.getStringValue());
  }

  @Override
  public FieldValue getTitelPartner2() {
    return titelPartner2;
  }

  @Override
  public void setTitelPartner2(FieldValue titel) {
    this.titelPartner2 = FieldValue.from(titel);
    setP2Titel(titel.getStringValue());
  }

  public String getToelichtingVerbintenis() {
    return getCeremonieToel();
  }

  public void setToelichtingVerbintenis(String toelichting) {
    setCeremonieToel(toelichting);
  }

  public List<DossierPersoon> getVolledigIngevuldeGetuigen() {

    List<DossierPersoon> list = new ArrayList<>();
    for (DossierPersoon dp : getGetuigen()) {
      if (dp.isVolledig()) {
        list.add(dp);
      }
    }

    return list;
  }

  @Override
  public String getVoorvPartner1() {
    return getP1Voorv();
  }

  @Override
  public void setVoorvPartner1(String voorv) {
    setP1Voorv(voorv);
  }

  @Override
  public String getVoorvPartner2() {
    return getP2Voorv();
  }

  @Override
  public void setVoorvPartner2(String voorv) {
    setP2Voorv(voorv);
  }

  @Override
  public boolean isAdelPartner1() {
    return BsDossierNaamgebruikUtils.isAdel(titelPartner1);
  }

  @Override
  public boolean isAdelPartner2() {
    return BsDossierNaamgebruikUtils.isAdel(titelPartner2);
  }

  public boolean isHuwelijk() {
    return getSoortVerbintenis() == SoortVerbintenis.HUWELIJK;
  }

  @Override
  public boolean isPredikaatPartner1() {
    return BsDossierNaamgebruikUtils.isPredikaat(titelPartner1);
  }

  @Override
  public boolean isPredikaatPartner2() {
    return BsDossierNaamgebruikUtils.isPredikaat(titelPartner2);
  }

  @Override
  public boolean isVolledig() {

    if (getDatumVerbintenis().getLongDate() <= 0 || getTijdVerbintenis().getLongTime() <= 0) {
      throw new ProException(ProExceptionSeverity.WARNING,
          "De datum en/of het tijdstip van de verbintenis is niet gevuld");
    }

    if (getHuwelijksLocatie() == null || !pos(getHuwelijksLocatie().getCodeHuwelijksLocatie())) {
      throw new ProException(ProExceptionSeverity.WARNING, "De huwelijkslocatie is niet gevuld");
    }

    if (getStatusVerbintenis() != StatusVerbintenis.DEFINITIEF) {
      throw new ProException(ProExceptionSeverity.WARNING, "De status van het dossier is niet definitief");
    }

    int aantalGetuigen = getHuwelijksLocatie().getLocatieSoort().getAantalGetuigenMin();
    int ingevoerd = getVolledigIngevuldeGetuigen().size();

    if (ingevoerd < aantalGetuigen) {
      throw new ProException(ProExceptionSeverity.WARNING,
          "Huwelijksdossier is niet volledig. <br/>Niet alle getuigen zijn opgegeven. Voer minimaal " + aantalGetuigen
              + " getuigen in.");
    }

    for (DossierVereiste v : getDossier().getVereisten()) {
      if (!v.isHeeftVoldaan()) {
        throw new ProException(ProExceptionSeverity.WARNING, "Er is niet voldaan aan alle vereisten.");
      }
    }

    return true;
  }

  private void addDocumentNationaliteit(List<DocumentNationaliteit> list, String type, DossierPersoon persoon) {

    DocumentNationaliteit dn = new DocumentNationaliteit();
    dn.setOms("Nationaliteit(en) " + type);

    StringBuilder nat = new StringBuilder();
    for (DossierNationaliteit n : persoon.getNationaliteiten()) {
      if (Landelijk.isNederland(n)) {
        return;
      }
      nat.append(n.getNationaliteitOmschrijving() + ", ");
    }
    dn.setNationaliteiten(trim(nat.toString()));

    list.add(dn);
  }

  private DossierPersoon getOuderPersoon(DossierPersoon persoon) {
    return persoon != null ? persoon : new DossierPersoon(OUDER);
  }

  public void resetGemeenteGetuigen() {
    setGemeenteGetuigen(-1);
  }

  public void resetAmbtenaren() {
    BsPersoonUtils.reset(getAmbtenaar1());
    BsPersoonUtils.reset(getAmbtenaar2());
  }

  public void resetNaamGebruikPartner1() {
    setNaamGebruikPartner1("");
    setTitelPartner1(new FieldValue());
    setVoorvPartner1("");
    setNaamPartner1("");
  }

  public void resetNaamGebruikPartner2() {
    setNaamGebruikPartner2("");
    setTitelPartner2(new FieldValue());
    setVoorvPartner2("");
    setNaamPartner2("");
  }

  public class DocumentNationaliteit {

    private String oms             = "";
    private String nationaliteiten = "";

    public String getNationaliteiten() {
      return nationaliteiten;
    }

    public void setNationaliteiten(String nationaliteiten) {
      this.nationaliteiten = nationaliteiten;
    }

    public String getOms() {
      return oms;
    }

    public void setOms(String oms) {
      this.oms = oms;
    }
  }
}
