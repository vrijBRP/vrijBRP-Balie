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

package nl.procura.gba.web.services.bs.geboorte;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.ERKENNING_BIJ_AANGIFTE;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.GEEN_ERKENNING;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BINNEN_HOMO_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BUITEN_HUWELIJK;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossErk;
import nl.procura.gba.jpa.personen.db.DossGeb;
import nl.procura.gba.jpa.personen.db.DossNk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierKinderen;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierLatereVermelding;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.erkenning.*;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.geboorte.LatereVermeldingErkenningGeboorte;
import nl.procura.gba.web.services.bs.lv.geboorte.LatereVermeldingNaamskeuzeGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierGeboorte extends DossGeb
    implements DossierKinderen,
    DossierGeboorteVerzoek,
    DossierLatereVermelding,
    DossierMetAkte {

  private static final long serialVersionUID = -5092094547362276886L;

  private Dossier                dossier                       = null;
  private DossierErkenning       dossierErkenningVoorGeboorte  = null;
  private DossierNaamskeuze      dossierNaamskeuzeVoorGeboorte = null;
  private DossierErkenning       dossierErkenningBijGeboorte   = null;
  private FieldValue             gemeente                      = new FieldValue();
  private FieldValue             landAfstammingVb              = new FieldValue();
  private FieldValue             landAfstammingsRecht          = new FieldValue();
  private FieldValue             landNaamRecht                 = new FieldValue();
  private FieldValue             titel                         = new FieldValue();
  private ErkenningBuitenProweb  erkenningBuitenProweb         = new ErkenningBuitenProweb(this);
  private NaamskeuzeBuitenProweb naamskeuzeBuitenProweb        = new NaamskeuzeBuitenProweb(this);

  public DossierGeboorte() {
    super();
    setDossier(new Dossier(ZaakType.GEBOORTE, this));
    getDossier().toevoegenPersoon(AANGEVER);
    getDossier().toevoegenPersoon(VADER_DUO_MOEDER);
    getDossier().toevoegenPersoon(MOEDER);
  }

  @Override
  public void beforeSave() {
    setCDossGeb(getDossier().getCode());
  }

  public DossierPersoon getAangever() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(AANGEVER));
  }

  @Override
  public String getAkteAanduiding() {
    return "A"; // Derde positie geboorte
  }

  @Override
  public DateTime getAkteDatum() {
    return new DateTime();
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {

    List<DossierPersoon> personen = new ArrayList<>();

    personen.add(getAangever());

    if (isVaderVanToepassing()) {
      personen.add(getVader());
    }

    personen.add(getMoeder());

    return personen;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getAangever().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getAangever().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getAangever().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getAangever().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossGeb();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public EersteKindType getEersteKindType() {
    return EersteKindType.get(getbEersteKind());
  }

  @Override
  public void setEersteKindType(EersteKindType type) {
    setbEersteKind(toBigDecimal(type.getCode()));
  }

  public DossierErkenning getErkenningBijGeboorte() {
    return (dossierErkenningBijGeboorte != null && dossierErkenningBijGeboorte.isGelijkMetGeboorte())
        ? dossierErkenningBijGeboorte
        : null;
  }

  public void setErkenningBijGeboorte(DossierErkenning dossierErkenningGeboorte) {
    this.dossierErkenningBijGeboorte = dossierErkenningGeboorte;
    setDossErkGeb(ReflectionUtil.deepCopyBean(DossErk.class, dossierErkenningGeboorte));
  }

  public ErkenningBuitenProweb getErkenningBuitenProweb() {
    return erkenningBuitenProweb;
  }

  public void setErkenningBuitenProweb(ErkenningBuitenProweb erkenningBuitenProweb) {
    this.erkenningBuitenProweb = erkenningBuitenProweb;
  }

  public NaamskeuzeBuitenProweb getNaamskeuzeBuitenProweb() {
    return naamskeuzeBuitenProweb;
  }

  public void setNaamskeuzeBuitenProweb(NaamskeuzeBuitenProweb naamskeuzeBuitenProweb) {
    this.naamskeuzeBuitenProweb = naamskeuzeBuitenProweb;
  }

  public ErkenningsType getErkenningsType() {
    return ErkenningsType.get(getTypeErkenning());
  }

  public void setErkenningsType(ErkenningsType type) {
    setTypeErkenning(type != null ? type.getCode() : ErkenningsType.ONBEKEND.getCode());
  }

  public NaamskeuzeType getNaamskeuzeSoort() {
    return NaamskeuzeType.get(getTypeNk());
  }

  public void setNaamskeuzeSoort(NaamskeuzeType type) {
    setTypeNk(type != null ? type.getCode() : NaamskeuzeType.ONBEKEND.getCode());
  }

  public DossierErkenning getErkenningVoorGeboorte() {
    return (dossierErkenningVoorGeboorte != null && !dossierErkenningVoorGeboorte.isGelijkMetGeboorte())
        ? dossierErkenningVoorGeboorte
        : null;
  }

  public void setErkenningVoorGeboorte(DossierErkenning dossierErkenning) {
    this.dossierErkenningVoorGeboorte = dossierErkenning;
    setDossErk(ReflectionUtil.deepCopyBean(DossErk.class, dossierErkenning));
  }

  public DossierNaamskeuze getNaamskeuzeVoorGeboorte() {
    return dossierNaamskeuzeVoorGeboorte;
  }

  public void setNaamskeuzeVoorGeboorte(DossierNaamskeuze dossierNaamskeuze) {
    this.dossierNaamskeuzeVoorGeboorte = dossierNaamskeuze;
    setDossNk(ReflectionUtil.deepCopyBean(DossNk.class, dossierNaamskeuze));
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    setcGemGeb(gemeente.getBigDecimalValue());
  }

  public GezinssituatieType getGezinssituatie() {
    return GezinssituatieType.get(getGezinType());
  }

  public void setGezinssituatie(GezinssituatieType type) {
    setGezinType(type != null ? type.getCode() : "");
  }

  @Override
  public String getKeuzeGeslachtsnaam() {
    return getKeuzeNaamGesl();
  }

  @Override
  public void setKeuzeGeslachtsnaam(String geslachtsnaam) {
    setKeuzeNaamGesl(geslachtsnaam);
  }

  @Override
  public String getKeuzeNaam() {
    return trim(getKeuzeTitel() + " " + getKeuzeVoorvoegsel() + " " + getKeuzeGeslachtsnaam());
  }

  @Override
  public FieldValue getKeuzeTitel() {
    return titel;
  }

  @Override
  public void setKeuzeTitel(FieldValue titel) {
    this.titel = FieldValue.from(titel);
    setKeuzeNaamTp(this.titel.getStringValue());
  }

  @Override
  public String getKeuzeVoorvoegsel() {
    return getKeuzeNaamVoorv();
  }

  @Override
  public void setKeuzeVoorvoegsel(String voorvoegsel) {
    setKeuzeNaamVoorv(voorvoegsel);
  }

  @Override
  public List<DossierPersoon> getKinderen() {
    return getDossier().getPersonen(KIND);
  }

  @Override
  public KindLeeftijdsType getKindLeeftijdsType() {
    return KindLeeftijdsType.JONGER_DAN_7;
  }

  // Begin naamskeuze

  public FieldValue getLandAfstammingsRecht() {
    return landAfstammingsRecht;
  }

  public void setLandAfstammingsRecht(FieldValue land) {
    this.landAfstammingsRecht = FieldValue.from(land);
    setcLandAfstamRecht(landAfstammingsRecht.getBigDecimalValue());
  }

  @Override
  public FieldValue getLandNaamRecht() {
    return landNaamRecht;
  }

  @Override
  public void setLandNaamRecht(FieldValue land) {
    this.landNaamRecht = FieldValue.from(land);
    setcLandNaamRecht(this.landNaamRecht.getBigDecimalValue());
  }

  @Override
  public LatereVermelding getLatereVermelding() {
    if (isSprakeLatereVermeldingNaamskeuze()) {
      return new LatereVermeldingNaamskeuzeGeboorte(this);
    } else {
      return new LatereVermeldingErkenningGeboorte(this);
    }
  }

  @Override
  public DossierPersoon getMoeder() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(MOEDER));
  }

  @Override
  public NaamsPersoonType getNaamskeuzePersoon() {
    return NaamsPersoonType.get(gettNaamskeuze());
  }

  @Override
  public void setNaamskeuzePersoon(NaamsPersoonType type) {
    settNaamskeuze(toBigDecimal(type.getCode()));
  }

  @Override
  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return NaamskeuzeVanToepassingType.get(getbNaamskeuze());
  }

  @Override
  public void setNaamskeuzeType(NaamskeuzeVanToepassingType type) {
    setbNaamskeuze(toBigDecimal(type.getCode()));
  }

  public RedenVerplicht getRedenVerplichtBevoegd() {
    return RedenVerplicht.get(getRedenVerplicht());
  }

  public void setRedenVerplichtBevoegd(RedenVerplicht redenVerplicht) {
    setRedenVerplicht(redenVerplicht.getCode());
  }

  public String getTardieveReden() {
    return getRedenTardieve();
  }

  public void setTardieveReden(String reden) {
    setRedenTardieve(reden);
  }

  public DossierPersoon getVader() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(VADER_DUO_MOEDER));
  }

  @Override
  public DossierPersoon getVaderErkenner() {
    return getVader();
  }

  public FieldValue getVerblijfsLandAfstamming() {
    return landAfstammingVb;
  }

  public void setVerblijfsLandAfstamming(FieldValue land) {
    this.landAfstammingVb = FieldValue.from(land);
    setcLandAfstamVb(this.landAfstammingVb.getBigDecimalValue());
  }

  public DossierGeboorteVragen getVragen() {
    return new DossierGeboorteVragen(this);
  }

  public boolean isAfstammingsRechtBepaald() {
    return getLandAfstammingsRecht() != null && pos(getLandAfstammingsRecht().getValue());
  }

  @Override
  public boolean isAktePerKind() {
    return true;
  }

  @Override
  public boolean isGeborenBinnenHuwelijk() {
    return getGezinssituatie() == GezinssituatieType.BINNEN_HETERO_HUWELIJK;
  }

  @Override
  public boolean isSprakeLatereVermelding() {
    return isSprakeLatereVermeldingErkenning()
        || isSprakeLatereVermeldingNaamskeuze();
  }

  public boolean isSprakeLatereVermeldingErkenning() {
    boolean sit1 = getVragen().heeftErkenningVoorGeboorte();
    boolean sit2 = getVragen().heeftErkenningBijGeboorte();
    boolean sit3 = getVragen().heeftErkenningBuitenProweb();
    return sit1 || sit2 || sit3;
  }

  public boolean isSprakeLatereVermeldingNaamskeuze() {
    boolean sit1 = getVragen().heeftNaamskeuzeVoorGeboorte();
    boolean sit2 = getVragen().heeftNaamskeuzeBuitenProweb();
    return sit1 || sit2;
  }

  public boolean isTardieveAangifte() {
    return pos(getTardieve());
  }

  public void setTardieveAangifte(boolean b) {
    setTardieve(toBigDecimal(b ? 1 : 0));
  }

  public boolean isVaderMogelijk() {
    GezinssituatieType gezinType = getGezinssituatie();
    ErkenningsType erkenningsType = getErkenningsType();
    return !(gezinType.is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK) && erkenningsType.is(GEEN_ERKENNING,
        ERKENNING_BIJ_AANGIFTE));
  }

  public boolean isVaderVanToepassing() {
    GezinssituatieType gezinType = getGezinssituatie();
    ErkenningsType erkenningsType = getErkenningsType();

    return !(gezinType.is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK) && erkenningsType.is(GEEN_ERKENNING,
        ERKENNING_BIJ_AANGIFTE));
  }

  @Override
  public boolean isVolledig() {
    return true;
  }
}
