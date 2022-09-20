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

package nl.procura.gba.web.services.bs.overlijden;

import static nl.procura.gba.common.MiscUtils.getLeeftijd;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.NormalNumberConverter;
import nl.procura.gba.common.NormalNumberConverter.Taal;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.jpa.personen.db.DossOverl;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.*;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.SchriftelijkeAangever;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class AbstractDossierOverlijden
    extends DossOverl
    implements DossierOverlijden {

  private static final long serialVersionUID = -1252109599795822477L;

  private Dossier                        dossier           = null;
  private FieldValue                     plaatsOverlijden  = new FieldValue();
  private FieldValue                     plaatsLijkvinding = new FieldValue();
  private FieldValue                     landOverlijden    = new FieldValue();
  private FieldValue                     landAfgifte       = new FieldValue();
  private FieldValue                     landBestemming    = new FieldValue();
  private final DossierOverlijdenVerzoek verzoek;

  public AbstractDossierOverlijden(ZaakType zaakType) {

    super();

    setDossier(new Dossier(zaakType, this));
    getDossier().toevoegenPersoon(AANGEVER);
    getDossier().toevoegenPersoon(AFGEVER);
    getDossier().toevoegenPersoon(OVERLEDENE);

    setbBuitBnlx(toBigDecimal(-1));
    setdLijkbez(toBigDecimal(-1));
    setdOverl(toBigDecimal(-1));
    settLijkbez(toBigDecimal(-1));
    settOverl(toBigDecimal(-1));
    setcOverlGem(toBigDecimal(0));
    setcLandBest(toBigDecimal(0));
    setTermijnBez("");
    setWijzeBez("");
    setOntvDoc("");
    setOntvDoc1("");
    setVervrmid("");
    settLijkvin(toBigDecimal(-1));
    setdLijkvin(toBigDecimal(-1));
    setPlaatsToev("");
    setcJustAang("");

    // Correspondentie
    setDossCorrDest(DossCorrDest.newDefault(CommunicatieType.NVT.getCode()));

    // Verzoek
    setVerzoekInd(false);
    setVerzoekOverlVoorn("");
    setVerzoekOverlGeslNaam("");
    setVerzoekOverlVoorv("");
    setVerzoekOverlTitel("");
    setVerzoekOverlGeboortedatum(BigDecimal.valueOf(-1L));
    setVerzoekOverlGeboorteplaats("");
    setVerzoekOverlGeboorteland(BigDecimal.valueOf(-1L));
    verzoek = new DossierOverlijdenVerzoek(this);
  }

  @Override
  public void beforeSave() {
    setcDossOverl(getDossier().getCode());
    getDossCorrDest().setCDossCorrDest(getDossier().getCode());
  }

  @Override
  public DossierPersoon getAangever() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(AANGEVER));
  }

  public DossierPersoon getAfgever() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(AFGEVER));
  }

  public TypeAfgever getAfgeverType() {
    return TypeAfgever.get(getcAfgever());
  }

  public void setAfgeverType(TypeAfgever afgever) {
    setcAfgever(afgever.getCode());
  }

  @Override
  public String getAkteAanduiding() {
    return "A"; // Overlijden
  }

  @Override
  public DateTime getAkteDatum() {
    return new DateTime();
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {

    List<DossierPersoon> personen = new ArrayList<>();

    personen.add(getAangever());
    personen.addAll(getOverledene().getPersonen(OUDER));
    personen.add(getOverledene());
    personen.addAll(getOverledene().getPersonen(PARTNER));

    return personen;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getAangever().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    getAangever().setAnummer(anr);
  }

  public String getBeschrijving() {
    return getBeschr();
  }

  public void setBeschrijving(String beschrijving) {
    setBeschr(beschrijving);
  }

  public String getBestemming() {
    return trim(getPlaatsBestemming() + ", " + getLandBestemming());
  }

  public String getBuitenBeneluxTekst() {

    StringBuilder lijkb = new StringBuilder();

    String bestBene = isBuitenBenelux() ? "Ja" : "Nee";
    String bestLand = getLandBestemming().getDescription();
    String bestPlaats = getPlaatsBestemming();
    String bestVia = getViaBestemming();
    String bestVervoer = getVervoermiddel();

    lijkb.append(bestBene);

    if (fil(bestLand)) {
      lijkb.append(", land: ")
          .append(bestLand)
          .append(", ");
    }

    if (fil(bestPlaats)) {
      lijkb.append("plaats: ")
          .append(bestPlaats)
          .append(", ");
    }

    if (fil(bestVia)) {
      lijkb.append("via: ")
          .append(bestVia)
          .append(", ");
    }

    if (fil(bestVervoer)) {
      lijkb.append("met: ")
          .append(bestVervoer);
    }

    return trim(lijkb.toString());
  }

  public String getBuitenlandPlaatsOverlijden() {
    return getBtlPlaats();
  }

  public void setBuitenlandPlaatsOverlijden(String plaatsOverlijden) {
    setBtlPlaats(plaatsOverlijden);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getAangever().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getAangever().setBurgerServiceNummer(burgerServiceNummer);
  }

  public DateTime getDatumLijkbezorging() {
    return new DateTime(getdLijkbez());
  }

  public void setDatumLijkbezorging(DateTime dateTime) {
    setdLijkbez(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumLijkvinding() {
    return new DateTime(getdLijkvin());
  }

  public void setDatumLijkvinding(DateTime dateTime) {

    setdLijkvin(BigDecimal.valueOf(dateTime.getLongDate()));

    // Datum lijkvinding is ook datum ingang zaak

    if (dateTime.getLongDate() > 0) {
      getDossier().setDatumIngang(dateTime);
    }
  }

  public String getDatumLijkvindingTekst() {
    return NormalNumberConverter.toString(Taal.NL, new ProcuraDate(
        astr(getDatumLijkvinding().getLongDate())).getDateFormat());
  }

  public String getDatumLijkvindingTekstFries() {
    return NormalNumberConverter.toString(Taal.FRIES, new ProcuraDate(
        astr(getDatumLijkvinding().getLongDate())).getDateFormat());
  }

  public DateTime getDatumOntvangst() {
    return new DateTime(getdOntvangst());
  }

  public void setDatumOntvangst(DateTime dateTime) {
    setdOntvangst(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public String getDatumOntvangstTekst() {
    return NormalNumberConverter.toString(Taal.NL,
        new ProcuraDate(astr(getDatumOntvangst().getLongDate())).getDateFormat());
  }

  public String getDatumOntvangstTekstFries() {
    return NormalNumberConverter.toString(Taal.FRIES,
        new ProcuraDate(astr(getDatumOntvangst().getLongDate())).getDateFormat());
  }

  @Override
  public DateTime getDatumOverlijden() {
    return new DateTime(getdOverl());
  }

  public void setDatumOverlijden(DateTime dateTime) {
    setdOverl(BigDecimal.valueOf(dateTime.getLongDate()));
    // Datum overlijden is ook datum ingang zaak
    if (dateTime.getLongDate() > 0) {
      getDossier().setDatumIngang(dateTime);
    }
  }

  public String getDatumOverlijdenTekst() {
    return NormalNumberConverter.toString(Taal.NL, new ProcuraDate(
        astr(getDatumOverlijden().getLongDate())).getDateFormat());
  }

  public String getDatumOverlijdenTekstFries() {
    return NormalNumberConverter.toString(Taal.FRIES, new ProcuraDate(
        astr(getDatumOverlijden().getLongDate())).getDateFormat());
  }

  public Doodsoorzaak getDoodsoorzaak() {
    return Doodsoorzaak.get(getDoodsoorz());
  }

  public void setDoodsoorzaak(Doodsoorzaak doodsoorzaak) {
    setDoodsoorz(doodsoorzaak.getCode());
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
  public List<DossierPersoon> getExPartners() {
    return getOverledene().getPersonen(EXPARTNER);
  }

  @Override
  public String getExPartnersSamenvatting() {
    return getOverledene().getPersonenSamenvatting(EXPARTNER);
  }

  @Override
  public DossierPersoon getGehuwdePartner() {

    for (DossierPersoon persoon : getPartners()) {
      if (PARTNER.equals(persoon.getDossierPersoonType())) {
        if (BurgerlijkeStaatType.HUWELIJK.equals(persoon.getBurgerlijkeStaat())) {
          return persoon;
        }
      }
    }

    return new DossierPersoon();
  }

  public FieldValue getGemeenteOverlijden() {
    return plaatsOverlijden;
  }

  public void setGemeenteOverlijden(FieldValue plaatsOverlijden) {
    this.plaatsOverlijden = FieldValue.from(plaatsOverlijden);
    setcOverlGem(this.plaatsOverlijden.getBigDecimalValue());
  }

  @Override
  public DossierPersoon getGeregistreerdePartner() {

    for (DossierPersoon persoon : getPartners()) {
      if (PARTNER.equals(persoon.getDossierPersoonType())) {
        if (BurgerlijkeStaatType.PARTNERSCHAP.equals(persoon.getBurgerlijkeStaat())) {
          return persoon;
        }
      }
    }

    return new DossierPersoon();
  }

  @Override
  public List<DossierPersoon> getHuidigePartners() {

    List<DossierPersoon> partners = new ArrayList<>();
    partners.add(getGehuwdePartner());
    partners.add(getGeregistreerdePartner());

    return partners;
  }

  @Override
  public List<DossierPersoon> getKinderen() {
    return getOverledene().getPersonen(KIND);
  }

  @Override
  public String getKinderenSamenvatting() {
    return getOverledene().getPersonenSamenvatting(KIND);
  }

  public FieldValue getLandAfgifte() {
    return landAfgifte;
  }

  public void setLandAfgifte(FieldValue land) {
    landAfgifte = FieldValue.from(land);
    setcLandAfg(this.landAfgifte.getBigDecimalValue());
  }

  public FieldValue getLandBestemming() {
    return landBestemming;
  }

  public void setLandBestemming(FieldValue landBestemming) {
    this.landBestemming = FieldValue.from(landBestemming);
    setcLandBest(this.landBestemming.getBigDecimalValue());
  }

  public FieldValue getLandOverlijden() {
    return landOverlijden;
  }

  public void setLandOverlijden(FieldValue land) {
    landOverlijden = FieldValue.from(land);
    setcLandOverl(this.landOverlijden.getBigDecimalValue());
  }

  public String getLCSchriftelijkeAangever() {
    return getSchriftelijkeAangever().getOms().toLowerCase();
  }

  @Override
  public int getLeeftijdOverledene() {
    return getLeeftijd(astr(getOverledene().getDatumGeboorte().getLongDate()),
        astr(getDatumOverlijden().getLongDate()));
  }

  public OntvangenDocument getOntvangenDocument() {
    return OntvangenDocument.get(getOntvDoc());
  }

  public void setOntvangenDocument(OntvangenDocument ontvangenDocument) {
    setOntvDoc(ontvangenDocument.getCode());
  }

  public OntvangenDocument getOntvangenDocumentLijkbezorging() {
    return OntvangenDocument.get(getOntvDoc1());
  }

  public void setOntvangenDocumentLijkbezorging(OntvangenDocument document) {
    setOntvDoc1(document != null ? document.getCode() : "");
  }

  @Override
  public DossierPersoon getOuder1() {
    DossierPersoon persoon = getOverledene().getPersoon(DossierPersoonFilter.filter(0, Geslacht.VROUW, OUDER));
    return BsPersoonUtils.empty(persoon);
  }

  @Override
  public DossierPersoon getOuder2() {
    return BsPersoonUtils.empty(getOverledene().getPersoon(DossierPersoonFilter.filter(1, Geslacht.MAN, OUDER)));
  }

  @Override
  public DossierPersoon getOverledene() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(OVERLEDENE));
  }

  @Override
  public List<DossierPersoon> getPartners() {
    return getOverledene().getPersonen(PARTNER, EXPARTNER);
  }

  public FieldValue getPlaatsLijkvinding() {
    return plaatsLijkvinding;
  }

  public void setPlaatsLijkvinding(FieldValue plaatsLijkvinding) {
    this.plaatsLijkvinding = FieldValue.from(plaatsLijkvinding);
    setcOverlGem(this.plaatsLijkvinding.getBigDecimalValue());
  }

  public String getPlaatsOntleding() {
    return getpOntleding();
  }

  public void setPlaatsOntleding(String plaats) {
    setpOntleding(plaats);
  }

  public String getPlaatsToevoeging() {
    return getPlaatsToev();
  }

  public void setPlaatsToevoeging(String plaatsToevoeging) {
    setPlaatsToev(plaatsToevoeging);
  }

  public SchriftelijkeAangever getSchriftelijkeAangever() {
    return SchriftelijkeAangever.get(getcJustAang());
  }

  public void setSchriftelijkeAangever(SchriftelijkeAangever schriftelijkeAangever) {
    setcJustAang(schriftelijkeAangever.getCode());
  }

  public TermijnLijkbezorging getTermijnLijkbezorging() {
    return TermijnLijkbezorging.get(getTermijnBez());
  }

  public void setTermijnLijkbezorging(TermijnLijkbezorging termijnLijkbezorging) {
    setTermijnBez(termijnLijkbezorging.getCode());
  }

  public DateTime getTijdLijkbezorging() {
    return new DateTime(0, gettLijkbez().longValue());
  }

  public void setTijdLijkbezorging(DateTime dateTime) {
    settLijkbez(BigDecimal.valueOf(dateTime.getLongTime()));
  }

  public String getTijdLijkbezorgingStandaard() {
    return gettLijkbez().longValue() >= 0 ? (" om " + getTijdLijkbezorging().getFormatTime("HH.mm")) : "";
  }

  public DateTime getTijdLijkvinding() {
    return new DateTime(0, gettLijkvin().longValue());
  }

  public void setTijdLijkvinding(DateTime dateTime) {
    settLijkvin(BigDecimal.valueOf(dateTime.getLongTime()));
  }

  public String getTijdLijkvindingStandaard() {
    return gettLijkvin().longValue() >= 0 ? getTijdLijkvinding().getFormatTime("HH.mm") : "";
  }

  public DateTime getTijdOverlijden() {
    return new DateTime(0, gettOverl().longValue());
  }

  public void setTijdOverlijden(DateTime dateTime) {
    settOverl(BigDecimal.valueOf(dateTime.getLongTime()));
  }

  public String getTijdOverlijdenStandaard() {
    return gettOverl().longValue() >= 0 ? getTijdOverlijden().getFormatTime("HH.mm") : "";
  }

  public TypeBronDocument getTypeBronDocument() {
    return TypeBronDocument.get(getTypeDoc());
  }

  public void setTypeBronDocument(TypeBronDocument typeBronDocument) {
    setTypeDoc(typeBronDocument.getCode());
  }

  public String getVervoermiddel() {
    return getVervrmid();
  }

  public void setVervoermiddel(String vervoermiddel) {
    setVervrmid(vervoermiddel);
  }

  // Lijkbezorging

  public WijzeLijkbezorging getWijzeLijkBezorging() {
    return WijzeLijkbezorging.get(getWijzeBez());
  }

  public void setWijzeLijkBezorging(WijzeLijkbezorging wijzeLijkBezorging) {
    setWijzeBez(wijzeLijkBezorging.getCode());
  }

  @Override
  public boolean isAantalExPartners() {
    return getExPartners().size() > 0;
  }

  public boolean isBuitenBenelux() {
    return pos(getbBuitBnlx());
  }

  public void setBuitenBenelux(boolean buitenBenelux) {
    setbBuitBnlx(toBigDecimal(buitenBenelux ? 1 : 0));
  }

  public boolean isVoldoetAanEisen() {
    return pos(getbVoldoet());
  }

  public void setVoldoetAanEisen(boolean voldoetAanEisen) {
    setbVoldoet(toBigDecimal(voldoetAanEisen ? 1 : 0));
  }

  @Override
  public DossierOverlijdenVerzoek getVerzoek() {
    return verzoek;
  }

  @Override
  public boolean isVolledig() {
    return true;
  }

  protected boolean isAllTrue(Boolean... values) {
    return Arrays.stream(values).allMatch(value -> value);
  }
}
