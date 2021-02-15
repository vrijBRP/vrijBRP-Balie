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

package nl.procura.gba.web.services.bs.naamskeuze;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossNk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierKinderen;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierLatereVermelding;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.erkenning.EersteKindType;
import nl.procura.gba.web.services.bs.erkenning.KindLeeftijdsType;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.naamskeuze.LatereVermeldingNaamskeuze;
import nl.procura.gba.web.services.bs.lv.naamskeuze.LvNaamskeuze;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierNaamskeuze extends DossNk
    implements DossierKinderen,
    DossierNamenrecht,
    DossierLatereVermelding,
    DossierMetAkte {

  private static final long serialVersionUID = 1970514541656898692L;

  private Dossier         dossier         = new Dossier(ZaakType.NAAMSKEUZE);
  private FieldValue      gemeente        = new FieldValue();
  private FieldValue      titel           = new FieldValue();
  private FieldValue      landNaamRecht   = new FieldValue();
  private DossierGeboorte dossierGeboorte = null;

  public DossierNaamskeuze() {
    super();
    setDossier(new Dossier(ZaakType.NAAMSKEUZE, this));
    getDossier().toevoegenPersoon(MOEDER);
    getDossier().toevoegenPersoon(PARTNER);
  }

  @Override
  public void beforeSave() {
    setCDossNk(getDossier().getCode());
  }

  @Override
  public String getAkteAanduiding() {
    return "W";
  }

  @Override
  public DateTime getAkteDatum() {
    return getDossier().getDatumTijdInvoer();
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {
    List<DossierPersoon> personen = new ArrayList<>();
    personen.add(getMoeder());
    personen.add(getPartner());
    return personen;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getPartner().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getPartner().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getPartner().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getPartner().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossNk();
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
    return EersteKindType.get(getEersteKind());
  }

  @Override
  public void setEersteKindType(EersteKindType type) {
    setEersteKind(toBigDecimal(type.getCode()));
  }

  public NaamskeuzeType getDossierNaamskeuzeType() {
    return NaamskeuzeType.get(getType());
  }

  public void setDossierNaamskeuzeType(NaamskeuzeType type) {
    setType(type.getCode());
  }

  public String getNaamskeuzeTypeOmschrijving() {
    return getDossierNaamskeuzeType().getOms();
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    setcCGemNk(this.gemeente.getBigDecimalValue());
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

  public DossierPersoon getKind() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(KIND));
  }

  public DossierPersoon getPartner() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(PARTNER));
  }

  @Override
  public List<DossierPersoon> getKinderen() {
    return getDossier().getPersonen(KIND);
  }

  @Override
  public DossierPersoon getMoeder() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(MOEDER));
  }

  @Override
  public KindLeeftijdsType getKindLeeftijdsType() {
    return getKinderen().stream()
        .findFirst()
        .map(kind -> KindLeeftijdsType.get(kind.getLeeftijd()))
        .orElse(KindLeeftijdsType.JONGER_DAN_7);
  }

  // Begin naamskeuze

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
  public LatereVermelding<LvNaamskeuze> getLatereVermelding() {
    return new LatereVermeldingNaamskeuze(this);
  }

  @Override
  public NaamsPersoonType getNaamskeuzePersoon() {
    return NaamsPersoonType.get(getPersonType());
  }

  @Override
  public void setNaamskeuzePersoon(NaamsPersoonType type) {
    setPersonType(toBigDecimal(type.getCode()));
  }

  @Override
  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return NaamskeuzeVanToepassingType.NVT;
  }

  @Override
  public void setNaamskeuzeType(NaamskeuzeVanToepassingType type) {
  }

  // Einde naamskeuze

  @Override
  public DossierPersoon getVaderErkenner() {
    return getPartner();
  }

  @Override
  public boolean isAktePerKind() {
    return getDossierNaamskeuzeType() != NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE;
  }

  public boolean isBestaandKind() {
    return getDossierNaamskeuzeType().is(NaamskeuzeType.NAAMSKEUZE_BESTAAND_KIND) && getKinderen().size() > 0;
  }

  @Override
  public boolean isGeborenBinnenHuwelijk() {
    return true;
  }

  public boolean isVoorGeboorte() {
    return getDossierNaamskeuzeType().is(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE) && getKinderen().size() == 0;
  }

  @Override
  public boolean isSprakeLatereVermelding() {
    return getDossierNaamskeuzeType().is(NaamskeuzeType.NAAMSKEUZE_BESTAAND_KIND) && (getKinderen().size() > 0);
  }

  @Override
  public boolean isVolledig() {
    return true;
  }
}
