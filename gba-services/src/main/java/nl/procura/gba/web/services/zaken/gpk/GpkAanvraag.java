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

package nl.procura.gba.web.services.zaken.gpk;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Gpk;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GpkAanvraag extends Gpk implements ContactZaak {

  private static final long serialVersionUID = 2383631675323624860L;

  private final GenericZaak zaak = new GenericZaak();

  public GpkAanvraag() {
    setUsr(new Usr(BaseEntity.DEFAULT));
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(astr(getAnr()));
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    setAnr(FieldValue.from(anr).getBigDecimalValue());
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
    return new DateTime(getDatumTijdInvoer().getLongDate());
  }

  @Override
  public void setDatumIngang(DateTime dt) {
    setDAanvr(toBigDecimal(dt.getLongDate()));
  }

  public DateTime getDatumPrint() {
    return new DateTime(getDPrint());
  }

  public void setDatumPrint(DateTime dt) {
    setDPrint(toBigDecimal(dt.getLongDate()));
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

  public DateTime getDatumVerval() {
    return new DateTime(getDVerval());
  }

  public void setDatumVerval(DateTime dt) {
    setDVerval(toBigDecimal(dt.getLongDate()));
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

  // Algemene methodes

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

  public GpkKaartType getKaart() {
    return GpkKaartType.get(getKaarttype());
  }

  public void setKaart(GpkKaartType kaart) {
    setKaarttype(kaart.getCode());
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

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  @Override
  public String getSoort() {
    return "";
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
    return ZaakType.GPK;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }
}
