/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Nrd;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakAfhaalbaar;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class RijbewijsAanvraag extends Nrd implements ContactZaak, ZaakAfhaalbaar {

  private static final long serialVersionUID = -3468735287544148942L;

  private final GenericZaak          zaak            = new GenericZaak();
  private Contactgegevens            contactgegevens = new Contactgegevens(this);
  private RijbewijsAanvraagStatussen statussen       = new RijbewijsAanvraagStatussen();

  public RijbewijsAanvraag() {
    setIndBezorgen(false);
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  public String getAanvraagNummer() {
    return getAanvrNr();
  }

  public void setAanvraagNummer(String aanvraagNummer) {
    setAanvrNr(aanvraagNummer);
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(getAnr());
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    setAnr(FieldValue.from(anr).getStringValue());
  }

  @Override
  public BasePLExt getBasisPersoon() {
    return zaak.getBasisPersoon();
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    zaak.setBasisPersoon(basisPersoon);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public int getCodeRas() {
    return aval(getCRaas());
  }

  public void setCodeRas(int codeRas) {
    setCRaas(toBigDecimal(codeRas));
  }

  public long getCodeVerblijfstitel() {
    return along(getCVbt());
  }

  public void setCodeVerblijfstitel(long cvbt) {
    setCVbt(toBigDecimal(cvbt));
  }

  @Override
  public ZaakContact getContact() {
    return zaak.getContact();
  }

  @Override
  public void setContact(ZaakContact contact) {
    zaak.setContact(contact);
  }

  public Contactgegevens getContactgegevens() {
    return contactgegevens;
  }

  public void setContactgegevens(Contactgegevens contactgegevens) {
    this.contactgegevens = contactgegevens;
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getDatumTijdInvoer().getLongDate());
  }

  @Override
  public void setDatumIngang(DateTime dt) {
    setDAanvr(toBigDecimal(dt.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDAanvr(), getTAanvr());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {

    setDAanvr(toBigDecimal(dt.getLongDate()));
    setTAanvr(toBigDecimal(dt.getLongTime()));
  }

  public DateTime getDatumVertrek() {
    return new DateTime(getDVertrek());
  }

  public void setDatumVertrek(DateTime date) {
    setDVertrek(toBigDecimal(date.getLongDate()));
  }

  public DateTime getDatumVestiging() {
    return new DateTime(getDVestiging());
  }

  public void setDatumVestiging(DateTime date) {
    setDVestiging(toBigDecimal(date.getLongDate()));
  }

  public UsrFieldValue getGebruikerAanvraag() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  public void setGebruikerAanvraag(UsrFieldValue gebruiker) {
    setUsr(new Usr(along(gebruiker.getValue()), gebruiker.getDescription()));
  }

  public UsrFieldValue getGebruikerUitgifte() {
    return new UsrFieldValue(getUsrUitgifte().getCUsr(), getUsrUitgifte().getUsrfullname());
  }

  public void setGebruikerUitgifte(UsrFieldValue gebruiker) {
    setUsrUitgifte(new Usr(along(gebruiker.getValue()), gebruiker.getDescription()));
  }

  @Override
  public Gemeente getGemeente() {
    return zaak.getGemeente();
  }

  @Override
  public void setGemeente(Gemeente gemeente) {
    zaak.setGemeente(gemeente);
  }

  @Override
  public Identificatie getIdentificatie() {
    return zaak.getIdentificatie();
  }

  @Override
  public void setIdentificatie(Identificatie identificatie) {
    zaak.setIdentificatie(identificatie);
  }

  @Override
  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  @Override
  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
  }

  public long getLandVertrek() {
    return along(getLVertrek());
  }

  public void setLandVertrek(long cLand) {
    setLVertrek(toBigDecimal(cLand));
  }

  public long getLandVestiging() {
    return along(getLVestiging());
  }

  public void setLandVestiging(long cLand) {
    setLVestiging(toBigDecimal(cLand));
  }

  @Override
  public Locatie getLocatieAfhaal() {
    return zaak.getLocatieAfhaal(getAfhaalLocation());
  }

  @Override
  public void setLocatieAfhaal(Locatie locatieAfhaal) {
    zaak.setLocatieAfhaal(locatieAfhaal);
    setAfhaalLocation(ReflectionUtil.deepCopyBean(Location.class, locatieAfhaal));
  }

  @Override
  public Locatie getLocatieInvoer() {
    return zaak.getLocatieInvoer(getLocation());
  }

  @Override
  public void setLocatieInvoer(Locatie locatieInvoer) {
    zaak.setLocatieInvoer(locatieInvoer);
    setLocation(ReflectionUtil.deepCopyBean(Location.class, locatieInvoer));
  }

  public NaamgebruikType getNaamgebruik() {
    return NaamgebruikType.getByRdwCode(along(getCNaamgebruik()));
  }

  public void setNaamgebruik(NaamgebruikType naamgebruik) {
    setCNaamgebruik(astr(naamgebruik.getRdwCode()));
  }

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public String getProcesVerbaalVerlies() {
    return getPvVerlies();
  }

  public void setProcesVerbaalVerlies(String procesVerbaalVerlies) {
    setPvVerlies(procesVerbaalVerlies);
  }

  public RijbewijsAanvraagReden getRedenAanvraag() {
    return RijbewijsAanvraagReden.get(along(getRdnAanvr()));
  }

  public void setRedenAanvraag(RijbewijsAanvraagReden redenAanvraag) {
    setRdnAanvr(toBigDecimal(redenAanvraag.getCode()));
  }

  public String getRijbewijsnummer() {

    String nieuwNr = astr(getRbwNr());
    String oudNr = astr(getVervangingsRbwNr());

    return (fil(nieuwNr) && nieuwNr.equals(oudNr)) ? "" : getRbwNr();
  }

  public void setRijbewijsnummer(String rijbewijsnummer) {
    setRbwNr(rijbewijsnummer);
  }

  @Override
  public String getSoort() {
    return getSoortAanvraag().getOms();
  }

  public RijbewijsAanvraagSoort getSoortAanvraag() {
    return RijbewijsAanvraagSoort.get(along(getSrtAanvr()));
  }

  // Algemene methodes

  public void setSoortAanvraag(RijbewijsAanvraagSoort soortAanvraag) {
    setSrtAanvr(toBigDecimal(soortAanvraag.getCode()));
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(along(getIndVerwerkt()));
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  public RijbewijsAanvraagStatussen getStatussen() {
    return statussen;
  }

  public void setStatussen(RijbewijsAanvraagStatussen statussen) {
    this.statussen = statussen;
  }

  @Override
  public ZaakType getType() {
    return ZaakType.RIJBEWIJS;
  }

  public String getVervangingsRbwNr() {
    return getRbwNrVerv();
  }

  public void setVervangingsRbwNr(String vervangingsRbwNr) {
    setRbwNrVerv(vervangingsRbwNr);
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public boolean isGbaBestendig() {
    return isTru(getGbaBest());
  }

  public void setGbaBestendig(boolean gbaBestendig) {
    setGbaBest(gbaBestendig ? "J" : "N");
  }

  public boolean isIndicatie185() {
    return isTru(getInd185dagen());
  }

  public void setIndicatie185(boolean indicatie185) {
    setInd185dagen(indicatie185 ? "J" : "N");
  }

  public boolean isSpoed() {
    return isTru(getSpoedAfh());
  }

  public void setSpoed(boolean spoed) {
    setSpoedAfh(spoed ? "J" : "N");
  }

  public VermeldTitelType getVermeldingTitel() {
    return VermeldTitelType.get(getVermeldTp().intValue());
  }

  public void setVermeldingTitel(VermeldTitelType type) {
    setVermeldTp(toBigDecimal(type.getCode()));
  }

  public boolean isThuisbezorgingGewenst() {
    return getIndBezorgen();
  }
}
