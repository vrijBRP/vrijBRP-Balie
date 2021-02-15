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

package nl.procura.gba.web.services.bs.erkenning;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossErk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierKinderen;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierLatereVermelding;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.erkenning.LatereVermeldingErkenning;
import nl.procura.gba.web.services.bs.lv.erkenning.LvErkenning;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierErkenning extends DossErk
    implements DossierKinderen, DossierNamenrecht, DossierLatereVermelding, DossierMetAkte {

  private static final long serialVersionUID = 1970514541656898692L;

  private Dossier         dossier                     = new Dossier(ZaakType.ERKENNING);
  private FieldValue      gemeente                    = new FieldValue();
  private FieldValue      titel                       = new FieldValue();
  private FieldValue      landToestemmingsRechtMoeder = new FieldValue();
  private FieldValue      landToestemmingsRechtKind   = new FieldValue();
  private FieldValue      landAfstammingsRecht        = new FieldValue();
  private FieldValue      landNaamRecht               = new FieldValue();
  private DossierGeboorte dossierGeboorte             = null;

  public DossierErkenning() {
    super();
    setDossier(new Dossier(ZaakType.ERKENNING, this));
    getDossier().toevoegenPersoon(MOEDER);
    getDossier().toevoegenPersoon(ERKENNER);
    setbGeboorte(toBigDecimal(0));
  }

  @Override
  public void beforeSave() {
    setCDossErk(getDossier().getCode());
  }

  @Override
  public String getAkteAanduiding() {
    return isErkenningBijAangifte() ? "B" : "C";
  }

  @Override
  public DateTime getAkteDatum() {
    return getDossier().getDatumTijdInvoer();
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {

    List<DossierPersoon> personen = new ArrayList<>();
    personen.add(getMoeder());
    personen.add(getErkenner());

    return personen;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getErkenner().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getErkenner().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getErkenner().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getErkenner().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossErk();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  public DossierGeboorte getDossierGeboorte() {
    return dossierGeboorte;
  }

  public void setDossierGeboorte(DossierGeboorte dossierGeboorte) {
    this.dossierGeboorte = dossierGeboorte;
  }

  @Override
  public EersteKindType getEersteKindType() {
    return EersteKindType.get(getbEersteKind());
  }

  @Override
  public void setEersteKindType(EersteKindType type) {
    setbEersteKind(toBigDecimal(type.getCode()));
  }

  public DossierPersoon getErkenner() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(ERKENNER, VADER_DUO_MOEDER));
  }

  public ErkenningsType getErkenningsType() {
    return ErkenningsType.get(getTypeErkenning());
  }

  public void setErkenningsType(ErkenningsType type) {
    setTypeErkenning(type.getCode());
  }

  public String getErkenningsTypeOmschrijving() {
    return getErkenningsType().getOms();
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    setcGemErk(this.gemeente.getBigDecimalValue());
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
    return getKinderen().stream()
        .findFirst()
        .map(kind -> KindLeeftijdsType.get(kind.getLeeftijd()))
        .orElse(KindLeeftijdsType.JONGER_DAN_7);
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

  public FieldValue getLandToestemmingsRechtKind() {
    return landToestemmingsRechtKind;
  }

  public void setLandToestemmingsRechtKind(FieldValue land) {
    this.landToestemmingsRechtKind = FieldValue.from(land);
    setcLandToestRechtKind(this.landToestemmingsRechtKind.getBigDecimalValue());
  }

  public FieldValue getLandToestemmingsRechtMoeder() {
    return landToestemmingsRechtMoeder;
  }

  public void setLandToestemmingsRechtMoeder(FieldValue land) {
    this.landToestemmingsRechtMoeder = FieldValue.from(land);
    setcLandToestRechtMoeder(landToestemmingsRechtMoeder.getBigDecimalValue());
  }

  @Override
  public LatereVermelding<LvErkenning> getLatereVermelding() {
    return new LatereVermeldingErkenning(this);
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

  // Einde naamskeuze

  @Override
  public void setNaamskeuzeType(NaamskeuzeVanToepassingType type) {
    setbNaamskeuze(toBigDecimal(type.getCode()));
  }

  public String getToestemming() {
    if (getErkenningsType() == ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT) {
      if (getToestemminggeverType() == ToestemminggeverType.MOEDER) {
        return getMoeder().getNaam().getPred_adel_voorv_gesl_voorn();
      }
    }

    return getToestemminggeverType().getOms() + " " + astr(getRechtbank());
  }

  public ToestemminggeverType getToestemminggeverType() {
    return ToestemminggeverType.get(getToestId());
  }

  public void setToestemminggeverType(ToestemminggeverType toestemminggever) {
    setToestId(toestemminggever != null ? toestemminggever.getCode() : ToestemminggeverType.ONBEKEND.getCode());
  }

  @Override
  public DossierPersoon getVaderErkenner() {
    return getErkenner();
  }

  public boolean isAfstammingsRechtBepaald() {
    return getLandAfstammingsRecht() != null && pos(getLandAfstammingsRecht().getValue());
  }

  @Override
  public boolean isAktePerKind() {
    return getErkenningsType() != ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT;
  }

  public boolean isBestaandKind() {
    return getErkenningsType().is(ErkenningsType.ERKENNING_BESTAAND_KIND,
        ErkenningsType.ERKENNING_BIJ_AANGIFTE) && getKinderen().size() > 0;
  }

  public boolean isErkenningBijAangifte() {
    return getErkenningsType().is(ErkenningsType.ERKENNING_BIJ_AANGIFTE);
  }

  @Override
  public boolean isGeborenBinnenHuwelijk() {
    return false; // Een kind dat erkent wordt is NOOIT geboren binnen een
    // huwelijk. Dan is erkenning namelijk niet mogelijk.
  }

  public boolean isGelijkMetGeboorte() {
    return pos(getbGeboorte());
  }

  public void setGelijkMetGeboorte(boolean b) {
    setbGeboorte(toBigDecimal(b ? 1 : 0));
  }

  public boolean isOngeborenVrucht() {
    return getErkenningsType().is(ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT) && getKinderen().size() == 0;
  }

  @Override
  public boolean isSprakeLatereVermelding() {
    return getErkenningsType().is(ErkenningsType.ERKENNING_BESTAAND_KIND) && (getKinderen().size() > 0);
  }

  public boolean isToestemmingsRechtBepaald() {
    return getLandToestemmingsRechtMoeder() != null && pos(getLandToestemmingsRechtMoeder().getValue());
  }

  @Override
  public boolean isVolledig() {
    return true;
  }
}
