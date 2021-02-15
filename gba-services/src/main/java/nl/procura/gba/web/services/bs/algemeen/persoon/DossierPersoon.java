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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.EXPARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.common.NormalNumberConverter;
import nl.procura.gba.common.NormalNumberConverter.Taal;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.*;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierPersoon extends DossPer implements DossierPersonen, DossierNationaliteiten {

  private static final long serialVersionUID = -7283898372211667344L;

  private final BsPersonenHandler bsDossierPersonen        = new BsPersonenHandler();
  private final BsNatioHandler    bsDossierNationaliteiten = new BsNatioHandler();

  private DocumentPL                persoon              = null;
  private BurgerlijkeStaatType      burgerlijkeStaat     = null;
  private FieldValue                verblijfstitel       = new FieldValue();
  private FieldValue                woonplaats           = new FieldValue();
  private FieldValue                woongemeente         = new FieldValue();
  private FieldValue                geboorteplaats       = new FieldValue();
  private FieldValue                geboorteAktePlaats   = new FieldValue();
  private FieldValue                geboortegemeente     = new FieldValue();
  private FieldValue                land                 = new FieldValue();
  private FieldValue                titel                = new FieldValue();
  private FieldValue                partnerTitel         = new FieldValue();
  private FieldValue                geboorteland         = new FieldValue();
  private FieldValue                immigratieland       = new FieldValue();
  private FieldValue                issuingCountry       = new FieldValue();
  private boolean                   inGemeente           = false;
  private DossierPersoonVerbintenis verbintenis          = new DossierPersoonVerbintenis(this);
  private boolean                   isIdentificatieNodig = true;

  public DossierPersoon() {
    this(DossierPersoonType.ONBEKEND);
  }

  public DossierPersoon(DossierPersoonType type) {
    setDossierPersoonType(type);
    setUid(UUID.randomUUID().toString());
    setVolgorde(-1L);
  }

  public void addAkte(DossierAkte akte) {
    akte.setPersoon(this);
    getDossAktes().add(ReflectionUtil.deepCopyBean(DossAkte.class, akte));
  }

  public Adresformats getAdres() {
    final String wpl = getWoonplaats().getDescription();
    final String gem = getWoongemeente().getDescription();
    final String land = getLand().getDescription();

    String buitenland1 = "";
    String buitenland2 = "";

    if (!BsPersoonUtils.isWoonachtigInNederland(this)) {
      buitenland1 = wpl;
      buitenland2 = land;
    }

    return new Adresformats().setValues(getStraat(), astr(getHnr()), getHnrL(), getHnrT(), getHnrA(), "", getPc(), "",
        wpl, gem, "", "", "", "", buitenland1, buitenland2);
  }

  @Override
  public String getAktenaam() {
    if (emp(super.getAktenaam())) {
      return getNaam().getInit_nen() + " " + getNaam().getNaam_naamgebruik_geslachtsnaam_voorv_aanschrijf();
    }

    return super.getAktenaam();
  }

  // Personen management
  @Override
  public List<DossierPersoon> getAllePersonen() {
    return bsDossierPersonen.getAllePersonen();
  }

  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(astr(getAnr()));
  }

  public void setAnummer(AnrFieldValue anr) {
    setAnr(FieldValue.from(anr).getBigDecimalValue());
  }

  public BurgerlijkeStaatType getBurgerlijkeStaat() {
    return burgerlijkeStaat;
  }

  public void setBurgerlijkeStaat(BurgerlijkeStaatType bs) {
    burgerlijkeStaat = bs;
    setBurgStaat(astr(bs != null ? bs.getBs() : ""));
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public Long getCode() {
    return getCDossPers();
  }

  public void setCode(Long code) {
    setCDossPers(code);
  }

  public DateTime getDatumBurgerlijkeStaat() {
    return new DateTime(getDBurgStaat());
  }

  public void setDatumBurgerlijkeStaat(DateTime datum) {
    setDBurgStaat(toBigDecimal(datum.getLongDate()));
  }

  public GbaDateFieldValue getDatumGeboorte() {
    return new GbaDateFieldValue(astr(getDGeb()));
  }

  public void setDatumGeboorte(GbaDateFieldValue datum) {
    setDGeb(FieldValue.from(datum).getBigDecimalValue());
  }

  public String getDatumGeboorteStandaard() {
    return getDatumGeboorte().getFormatDate();
  }

  // Nationaliteiten management

  public String getDatumGeboorteTekst() {
    return NormalNumberConverter.toString(Taal.NL,
        new ProcuraDate(astr(getDatumGeboorte().getLongDate())).getDateFormat());
  }

  public String getDatumGeboorteTekstFries() {
    return NormalNumberConverter.toString(Taal.FRIES,
        new ProcuraDate(astr(getDatumGeboorte().getLongDate())).getDateFormat());
  }

  public GbaDateFieldValue getDatumImmigratie() {
    return new GbaDateFieldValue(astr(getDImmigratie()));
  }

  public void setDatumImmigratie(GbaDateFieldValue datum) {
    setDImmigratie(datum.getBigDecimalValue());
  }

  public DateTime getDatumMoment() {
    return new DateTime(getDMoment());
  }

  public void setDatumMoment(DateTime datum) {
    setDMoment(toBigDecimal(datum.getLongDate()));
  }

  public GbaDateFieldValue getDatumOverlijden() {
    return new GbaDateFieldValue(astr(getDOverl()));
  }

  public void setDatumOverlijden(GbaDateFieldValue datum) {
    setDOverl(FieldValue.from(datum).getBigDecimalValue());
  }

  public DossierPersoonType getDossierPersoonType() {
    return DossierPersoonType.get(along(getTypePersoon()));
  }

  public void setDossierPersoonType(DossierPersoonType persoonType) {
    setTypePersoon(toBigDecimal(persoonType.getCode()));
  }

  @Override
  public List<DossierPersoon> getExPartners() {
    return getPersonen(EXPARTNER);
  }

  public Geboorteformats getGeboorte() {
    return new Geboorteformats().setValues(getDatumGeboorteStandaard(), getGeboorteplaatsStandaard(),
        getGeboortelandStandaard());
  }

  public BigDecimal getGeboorteAkteJaar() {
    return getGebAkteJaar();
  }

  public void setGeboorteAkteJaar(BigDecimal jaar) {
    setGebAkteJaar(jaar);
  }

  public String getGeboorteAkteNummer() {
    return getGebAkteNr();
  }

  public void setGeboorteAkteNummer(String nummer) {
    setGebAkteNr(BsAkteUtils.getBsAktenummer(nummer));
  }

  public String getGeboorteAkteBrpNummer() {
    return getGebAkteBrpNr();
  }

  public void setGeboorteAkteBrpNummer(String nummer) {
    setGebAkteBrpNr(nummer);
  }

  public FieldValue getGeboorteAktePlaats() {
    return geboorteAktePlaats;
  }

  public void setGeboorteAktePlaats(FieldValue geboorteAkteplaats) {
    geboorteAktePlaats = FieldValue.from(geboorteAkteplaats);
    setcGebAktePlaats(geboorteAktePlaats.getBigDecimalValue());
  }

  public FieldValue getGeboortegemeente() {
    return geboortegemeente;
  }

  public void setGeboortegemeente(FieldValue geboortegemeente) {
    this.geboortegemeente = FieldValue.from(geboortegemeente);
  }

  public FieldValue getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(FieldValue geboorteland) {
    this.geboorteland = FieldValue.from(geboorteland);
    setCGebLand(this.geboorteland.getBigDecimalValue());
  }

  public String getGeboortelandAkte() {
    return astr(getGebLandAkte());
  }

  public void setGeboortelandAkte(String land) {
    setGebLandAkte(land);
  }

  public String getGeboortelandStandaard() {
    final String gebLand = geboorteland.getDescription();
    final String akteLand = getGeboortelandAkte();
    return fil(astr(akteLand)) ? astr(akteLand) : (fil(gebLand) ? gebLand : "");
  }

  public FieldValue getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(FieldValue geboorteplaats) {

    this.geboorteplaats = FieldValue.from(geboorteplaats);
    setCGebPlaats(this.geboorteplaats.getBigDecimalValue());
    setGebPlaats(this.geboorteplaats.getDescription());
  }

  public String getGeboorteplaatsAkte() {
    return astr(getGebPlaatsAkte());
  }

  public void setGeboorteplaatsAkte(String plaats) {
    setGebPlaatsAkte(plaats);
  }

  public String getGeboorteplaatsStandaard() {
    final String gebPlaats = getGeboorteplaats().getDescription();
    final String aktePlaats = getGeboorteplaatsAkte();
    return fil(astr(aktePlaats)) ? astr(aktePlaats) : (fil(gebPlaats) ? gebPlaats : "");
  }

  public Geslacht getGeslacht() {
    return Geslacht.get(getGesl());
  }

  public void setGeslacht(Geslacht geslacht) {
    setGesl(geslacht != null ? geslacht.getAfkorting() : "");
  }

  public long getHuisnummer() {
    return along(getHnr());
  }

  public void setHuisnummer(long hnr) {
    setHnr(BigDecimal.valueOf(hnr));
  }

  public String getHuisnummerAand() {
    return getHnrA();
  }

  public void setHuisnummerAand(String hnrA) {
    setHnrA(hnrA);
  }

  public String getHuisnummerLetter() {
    return getHnrL();
  }

  public void setHuisnummerLetter(String hnrL) {
    setHnrL(hnrL);
  }

  public String getHuisnummerToev() {
    return getHnrT();
  }

  public void setHuisnummerToev(String hnrT) {
    setHnrT(hnrT);
  }

  public FieldValue getImmigratieland() {
    return immigratieland;
  }

  public void setImmigratieland(FieldValue immigratieland) {
    this.immigratieland = FieldValue.from(immigratieland);
    setCImmigratieLand(this.immigratieland.getBigDecimalValue());
  }

  @Override
  public List<DossierPersoon> getKinderen() {
    return getPersonen(KIND);
  }

  @Override
  public String getKinderenSamenvatting() {
    return getPersonenSamenvatting(KIND);
  }

  public FieldValue getLand() {
    return land;
  }

  public FieldValue getIssueingCountry() {
    return issuingCountry;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    setCLand(this.land.getBigDecimalValue());
  }

  public void setIssueingCountry(FieldValue country) {
    issuingCountry = FieldValue.from(country);
    setCIssueCountry(issuingCountry.getBigDecimalValue());
  }

  /**
   * Het land
   */

  public String getLandStandaard() {
    final FieldValue land = getLand();
    final String akteLand = getWoonlandAkte();
    return fil(astr(akteLand)) ? astr(akteLand) : (land != null ? land.getDescription() : "");
  }

  public int getLeeftijd() {
    return MiscUtils.getLeeftijd(getDatumGeboorte().getFormatDate(), "");
  }

  public Naamformats getNaam() {
    final Naamformats partner = new Naamformats(getPartnerVoornaam(), getPartnerGeslachtsnaam(),
        getPartnerVoorvoegsel(), getPartnerTitel().getDescription(), "", null);
    return new Naamformats(getVoornaam(), getGeslachtsnaam(), getVoorvoegsel(), getTitel().getDescription(),
        getNaamgebruik(), fil(getPartnerGeslachtsnaam()) ? partner : null);
  }

  public String getNaamgebruik() {
    return getNg();
  }

  public void setNaamgebruik(String ng) {
    setNg(ng);
  }

  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return NaamskeuzeVanToepassingType.get(getbNaamskeuze());
  }

  public void setNaamskeuzeType(NaamskeuzeVanToepassingType type) {
    setbNaamskeuze(toBigDecimal(type.getCode()));
  }

  @Override
  public DossierNationaliteit getNationaliteit() {
    return BsNatioUtils.getNationaliteit(this);
  }

  @Override
  public List<DossierNationaliteit> getNationaliteiten() {
    return bsDossierNationaliteiten.getNationaliteiten();
  }

  @Override
  public String getNationaliteitenOmschrijving() {
    return BsNatioUtils.getNationaliteitOmschrijving(this);
  }

  public String getOuderGeslacht() {
    return Geslacht.VROUW.equals(getGeslacht()) ? "moeder" : "vader";
  }

  public String getPartnerGeslacht() {
    return Geslacht.VROUW.equals(getGeslacht()) ? "echtgenote" : "echtgenoot";
  }

  @Override
  public List<DossierPersoon> getPersonen() {
    return bsDossierPersonen.getPersonen();
  }

  @Override
  public List<DossierPersoon> getPersonen(DossierPersoonType... types) {
    return bsDossierPersonen.getPersonen(types);
  }

  @Override
  public String getPersonenSamenvatting(DossierPersoonType... types) {
    return bsDossierPersonen.getSamenvatting(types);
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  @Override
  public DossierPersoon getPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.getPersoon(persoon);
  }

  @Override
  public DossierPersoon getPersoon(DossierPersoonFilter filter) {
    return bsDossierPersonen.getPersoon(filter);
  }

  public FieldValue getPostcode() {
    return new FieldValue(getPc());
  }

  public void setPostcode(FieldValue pc) {
    setPc(FieldValue.from(pc).getStringValue());
  }

  public DateTime getTijdGeboorte() {
    return new DateTime(toBigDecimal(0), getTGeb());
  }

  public void setTijdGeboorte(DateTime tijd) {
    setTGeb(toBigDecimal(tijd.getLongTime()));
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = FieldValue.from(titel);
    setTp(this.titel.getStringValue());
  }

  public String getVaderOmschrijving() {
    return (getGeslacht() == Geslacht.VROUW) ? "moeder" : "vader";
  }

  public DossierPersoonVerbintenis getVerbintenis() {
    return verbintenis;
  }

  public void setVerbintenis(DossierPersoonVerbintenis verbintenis) {
    this.verbintenis = verbintenis;
  }

  public FieldValue getVerblijfstitel() {
    return verblijfstitel;
  }

  public void setVerblijfstitel(FieldValue verblijfstitel) {
    this.verblijfstitel = FieldValue.from(verblijfstitel);
    setCVbt(this.verblijfstitel.getBigDecimalValue());
  }

  public String getVerblijfstitelOmschrijving() {
    final String vbt = getVerblijfstitel().getDescription();
    return fil(vbt) ? vbt : "Geen";
  }

  public Long getVolgorde() {
    return getvDossPers();
  }

  public void setVolgorde(Long code) {
    setvDossPers(code);
  }

  public String getVoornaam() {
    return getVoorn();
  }

  public void setVoornaam(String voornaam) {
    setVoorn(voornaam);
  }

  public String getVoorvoegsel() {
    return getVoorv();
  }

  public void setVoorvoegsel(String voorvoegsel) {
    setVoorv(voorvoegsel);
  }

  public FieldValue getWoongemeente() {
    return woongemeente;
  }

  public void setWoongemeente(FieldValue woongemeente) {
    this.woongemeente = FieldValue.from(woongemeente);
    setCWoonGemeente(this.woongemeente.getBigDecimalValue());
  }

  public String getWoonlandAkte() {
    return astr(getWoonLandAkte());
  }

  public void setWoonlandAkte(String land) {
    setWoonLandAkte(land);
  }

  public FieldValue getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(FieldValue woonplaats) {
    this.woonplaats = FieldValue.from(woonplaats);
    setWoonPlaats(this.woonplaats.getDescription());
  }

  public String getWoonplaatsAkte() {
    return astr(getWoonPlaatsAkte());
  }

  public void setWoonplaatsAkte(String plaats) {
    setWoonPlaatsAkte(plaats);
  }

  public String getWoonplaatsLand() {

    final String wpl = getWoonplaats().getDescription();
    final String land = getLand().getDescription();

    if (BsPersoonUtils.isWoonachtigInNederland(this)) {
      return wpl;
    }

    return (wpl + ", " + land);
  }

  public String getWoonplaatsLandStandaard() {

    final String wpl = getWoonplaatsStandaard();
    final String land = getLandStandaard();

    if (BsPersoonUtils.isWoonachtigInNederland(this)) {
      return wpl;
    }

    return (wpl + ", " + land);
  }

  /**
   * De woonplaats
   */

  public String getWoonplaatsStandaard() {
    final String wplPlaats = getWoonPlaats();
    final String aktePlaats = getWoonplaatsAkte();
    return fil(astr(aktePlaats)) ? astr(aktePlaats) : (fil(wplPlaats) ? wplPlaats : "");
  }

  @Override
  public boolean heeftPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.heeftPersoon(persoon);
  }

  public boolean isDefinitief() {
    return pos(getChecked());
  }

  public void setDefinitief(boolean definitief) {
    if (!isDefinitief()) {
      setChecked(definitief ? 1 : 0);
    }
  }

  public boolean isGeboren() {
    return pos(getDatumGeboorte().getLongDate());
  }

  public boolean isIdentificatieNodig() {
    return isIdentificatieNodig;
  }

  public void setIdentificatieNodig(boolean isIdentificatieNodig) {
    this.isIdentificatieNodig = isIdentificatieNodig;
  }

  public boolean isInGemeente() {
    return inGemeente;
  }

  public void setInGemeente(boolean inGemeente) {
    this.inGemeente = inGemeente;
  }

  public boolean isIngeschreven() {
    return pos(getBsn());
  }

  @Override
  public boolean isNationaliteit(DossierNationaliteit nationaliteit) {
    return bsDossierNationaliteiten.isNationaliteit(nationaliteit);
  }

  public boolean isOnderCuratele() {
    return pos(getCuratele());
  }

  public void setOnderCuratele(boolean onderCuratele) {
    setCuratele(onderCuratele ? 1 : 0);
  }

  public boolean isOverleden() {
    return getDatumOverlijden().getLongDate() >= 0;
  }

  @Override
  public boolean isPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.isPersoon(this, persoon);
  }

  public boolean isRNI() {
    return aval(getCWoonGemeente()) == Landelijk.RNI;
  }

  public boolean isVerstrekkingsbeperking() {
    return pos(getGeheim());
  }

  public void setVerstrekkingsbeperking(boolean verstrekkingsbeperking) {
    setGeheim(toBigDecimal(verstrekkingsbeperking ? 7 : 0));
  }

  public String getPartnerVoornaam() {
    return getpVoorn();
  }

  public void setPartnerVoornaam(String voornaam) {
    setpVoorn(voornaam);
  }

  public String getPartnerVoorvoegsel() {
    return getpVoorv();
  }

  public void setPartnerVoorvoegsel(String voorvoegsel) {
    setpVoorv(voorvoegsel);
  }

  public String getPartnerGeslachtsnaam() {
    return getpNaam();
  }

  public void setPartnerGeslachtsnaam(String naam) {
    setpNaam(naam);
  }

  public FieldValue getPartnerTitel() {
    return partnerTitel;
  }

  public void setPartnerTitel(FieldValue titel) {
    partnerTitel = FieldValue.from(titel);
    setpTp(partnerTitel.getStringValue());
  }

  public boolean isVolledig() {
    if (KIND.is(getDossierPersoonType())) {
      return fil(getGeslachtsnaam()) && (fil(getVoornaam()) || isVoornaamBevestigd());
    }

    return fil(getGeslachtsnaam());
  }

  public boolean isVoornaamBevestigd() {
    return pos(getVoornControle());
  }

  public void setVoornaamBevestigd(boolean bevestigd) {
    setVoornControle(toBigDecimal(bevestigd ? 1 : 0));
  }

  public void koppelenAan(DossierPersonen dossier) {

    if (dossier instanceof Dossier) {
      setDoss(ReflectionUtil.deepCopyBean(Doss.class, dossier));
    } else if (dossier instanceof DossierPersoon) {
      setParentDossPer(ReflectionUtil.deepCopyBean(DossPer.class, dossier));
    }
  }

  public void ontkoppelAkte(DossierAkte akte) {
    getDossAktes().remove(ReflectionUtil.deepCopyBean(DossAkte.class, akte));
  }

  @Override
  public DossierNationaliteit toevoegenNationaliteit(DossierNationaliteit nationaliteit) {
    return bsDossierNationaliteiten.toevoegenNationaliteit(this, nationaliteit);
  }

  @Override
  public List<DossierPersoon> toevoegenPersonen(List<DossierPersoon> personen) {
    return bsDossierPersonen.addPersonen(this, personen);
  }

  @Override
  public DossierPersoon toevoegenPersoon(DossierPersoon persoon) {
    return bsDossierPersonen.addPersoon(this, persoon);
  }

  @Override
  public DossierPersoon toevoegenPersoon(DossierPersoonType type) {
    return toevoegenPersoon(new DossierPersoon(type));
  }

  @Override
  public void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
    bsDossierNationaliteiten.verwijderNationaliteit(this, nationaliteit);
  }

  @Override
  public void verwijderPersoon(DossierPersoon persoon) {
    bsDossierPersonen.verwijderPersoon(this, persoon);
  }
}
