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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.VogAanvr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringType;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringZaak;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class VogAanvraag extends VogAanvr implements ContactZaak, GoedkeuringZaak {

  private static final long serialVersionUID = 2383631675323624860L;

  private final GenericZaak          zaak           = new GenericZaak();
  private VogNummer                  vogNummer      = new VogNummer();
  private VogAanvrager               aanvrager      = new VogAanvrager(this);
  private VogAanvraagBelanghebbende  belanghebbende = new VogAanvraagBelanghebbende(this);
  private VogAanvraagDoel            doel           = new VogAanvraagDoel(this);
  private VogAanvraagScreening       screening      = new VogAanvraagScreening(this);
  private VogAanvraagOpmerkingen     opmerkingen    = new VogAanvraagOpmerkingen(this);
  private List<VogAanvragerHistorie> historie       = new ArrayList<>();

  public VogAanvraag() {
    setUsr(new Usr(BaseEntity.DEFAULT));
    setStatus(ZaakStatusType.INCOMPLEET);
  }

  public long getAanvraagId() {
    return along(getAanvrId());
  }

  public void setAanvraagId(long aanvraagId) {
    setAanvrId(toBigDecimal(aanvraagId));
  }

  public VogAanvrager getAanvrager() {
    return aanvrager;
  }

  public void setAanvrager(VogAanvrager aanvrager) {
    this.aanvrager = aanvrager;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getAanvrager().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getAanvrager().setAnummer(anummer);
  }

  @Override
  public BasePLExt getBasisPersoon() {
    return zaak.getBasisPersoon();
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    zaak.setBasisPersoon(basisPersoon);
  }

  public VogAanvraagBelanghebbende getBelanghebbende() {
    return belanghebbende;
  }

  public void setBelanghebbende(VogAanvraagBelanghebbende belanghebbende) {
    this.belanghebbende = belanghebbende;
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getAanvrager().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getAanvrager().setBurgerServiceNummer(burgerServiceNummer);
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

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDAanvr(), getTAanvr());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {
    setDAanvr(toBigDecimal(dt.getLongDate()));
    setTAanvr(toBigDecimal(dt.getLongTime()));
  }

  public VogAanvraagDoel getDoel() {
    return doel;
  }

  public void setDoel(VogAanvraagDoel doel) {
    this.doel = doel;
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
  public GoedkeuringType getGoedkeuringsType() {
    return GoedkeuringType.get(getGoedkeuring());
  }

  @Override
  public void setGoedkeuringsType(GoedkeuringType type) {
    setGoedkeuring(type.getCode());
  }

  public List<VogAanvragerHistorie> getHistorie() {
    return historie;
  }

  public void setHistorie(List<VogAanvragerHistorie> historie) {
    this.historie = historie;
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

  public VogAanvraagOpmerkingen getOpmerkingen() {
    return opmerkingen;
  }

  public void setOpmerkingen(VogAanvraagOpmerkingen opmerkingen) {
    this.opmerkingen = opmerkingen;
  }

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public VogAanvraagScreening getScreening() {
    return screening;
  }

  public void setScreening(VogAanvraagScreening screening) {
    this.screening = screening;
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
    return ZaakType.COVOG;
  }

  public VogNummer getVogNummer() {
    return vogNummer;
  }

  public void setVogNummer(VogNummer vogNummer) {

    this.vogNummer = vogNummer;
    setDAanvr(toBigDecimal(vogNummer.getDatumAanvraag()));
    setVAanvr(toBigDecimal(vogNummer.getVolgnummer()));
    setCGem(toBigDecimal(vogNummer.getCodeGemeente()));
    setCLoc(toBigDecimal(vogNummer.getCodeLocatie()));
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }
}
