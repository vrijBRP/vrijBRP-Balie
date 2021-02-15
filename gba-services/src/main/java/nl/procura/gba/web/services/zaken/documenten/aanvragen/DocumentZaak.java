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

package nl.procura.gba.web.services.zaken.documenten.aanvragen;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.UittAanvr;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DocumentZaak extends UittAanvr implements ContactZaak {

  private static final long serialVersionUID = -5270291579061411687L;

  private final List<DocumentZaakPersoon> personen = new ArrayList<>();
  private final GenericZaak               zaak     = new GenericZaak();
  private DocumentRecord                  doc      = null;

  public DocumentZaak() {
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  public void setId(Long code) {
    setCUittAanvr(code);
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
    return new BsnFieldValue(getBsn().toString());
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  @Override
  public ZaakContact getContact() {
    return zaak.getContact();
  }

  @Override
  public void setContact(ZaakContact contact) {
    zaak.setContact(contact);
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getDAanvr());
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
  public void setDatumTijdInvoer(DateTime dateTime) {

    setDAanvr(toBigDecimal(dateTime.getLongDate()));
    setTAanvr(toBigDecimal(dateTime.getLongTime()));
  }

  public DocumentRecord getDoc() {
    return doc;
  }

  public void setDoc(DocumentRecord doc) {
    this.doc = doc;
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

  // Algemene methodes

  @Override
  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
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

  public List<DocumentZaakPersoon> getPersonen() {
    return personen;
  }

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public String getRelatieAnummer() {
    return getRelAnr();
  }

  public BsnFieldValue getRelatieBurgerServiceNummer() {
    return new BsnFieldValue(astr(getRelBsn()));
  }

  @Override
  public String getSoort() {
    return getDocument().getDocument();
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(along(getIndVerwerkt()));
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  @Override
  public ZaakType getType() {
    return ZaakType.UITTREKSEL;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public void setRelatieType(RelatieType relatieType) {
    setRelatie(toBigDecimal(relatieType.getCode()));
  }

  @Override
  public boolean isToevoegenAanZaaksysteem() {
    return ofNullable(getDoc())
        .map(value -> PL_UITTREKSEL == value.getDocumentType())
        .orElse(false); // Only document cases 'uittreksel document' are applicable
  }
}
