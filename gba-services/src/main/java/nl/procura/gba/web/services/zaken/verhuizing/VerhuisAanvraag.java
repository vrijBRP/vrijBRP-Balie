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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.BvhPark;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
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

public class VerhuisAanvraag extends BvhPark implements ContactZaak, GoedkeuringZaak {

  private static final long serialVersionUID = 2383631675323624860L;

  private final GenericZaak       zaak                 = new GenericZaak();
  private boolean                 bestemdVoorMidoffice = false;
  private List<VerhuisPersoon>    personen             = new ArrayList<>();
  private VerhuisAanvraagAdres    huidigAdres          = new VerhuisAanvraagAdres(this, false);
  private VerhuisAanvraagAdres    nieuwAdres           = new VerhuisAanvraagAdres(this, true);
  private VerhuisHerVestiging     hervestiging         = new VerhuisHerVestiging(this);
  private VerhuisEmigratie        emigratie            = new VerhuisEmigratie(this);
  private VerhuisToestemminggever toestemminggever     = new VerhuisToestemminggever(this);
  private VerhuisHoofdbewoner     hoofdbewoner         = new VerhuisHoofdbewoner(this);
  private VerhuisAangever         aangever             = new VerhuisAangever(this);

  public VerhuisAanvraag() {
    setUsr(new Usr(BaseEntity.DEFAULT));
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  public void setId(Long code) {
    setCBvhPark(code);
  }

  public VerhuisAangever getAangever() {
    return aangever;
  }

  public void setAangever(VerhuisAangever aangever) {
    this.aangever = aangever;
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
  public BasePLExt getBasisPersoon() {
    return zaak.getBasisPersoon();
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    zaak.setBasisPersoon(basisPersoon);
  }

  public String getBestemmingHuidigeBewoners() {
    return getNweBestem();
  }

  public void setBestemmingHuidigeBewoners(String bestemming) {
    setNweBestem(bestemming);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getAangever().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getAangever().setBurgerServiceNummer(burgerServiceNummer);
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
    return new DateTime(getDAanv());
  }

  @Override
  public void setDatumIngang(DateTime td) {
    setDAanv(toBigDecimal(td.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDOpn(), getTOpn());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {

    setDOpn(toBigDecimal(dt.getLongDate()));
    setTOpn(toBigDecimal(dt.getLongTime()));
  }

  public VerhuisEmigratie getEmigratie() {
    return emigratie;
  }

  public void setEmigratie(VerhuisEmigratie emigratie) {
    this.emigratie = emigratie;
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

  public VerhuisHerVestiging getHervestiging() {
    return hervestiging;
  }

  public void setHervestiging(VerhuisHerVestiging hervestiging) {
    this.hervestiging = hervestiging;
  }

  public VerhuisHoofdbewoner getHoofdbewoner() {
    return hoofdbewoner;
  }

  public void setHoofdbewoner(VerhuisHoofdbewoner hoofdbewoner) {
    this.hoofdbewoner = hoofdbewoner;
  }

  public VerhuisAanvraagAdres getHuidigAdres() {
    return huidigAdres;
  }

  public void setHuidigAdres(VerhuisAanvraagAdres huidigAdres) {
    this.huidigAdres = huidigAdres;
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

  @Override
  public Locatie getLocatieInvoer() {
    return zaak.getLocatieInvoer(getLocation());
  }

  @Override
  public void setLocatieInvoer(Locatie locatieInvoer) {
    zaak.setLocatieInvoer(locatieInvoer);
    setLocation(ReflectionUtil.deepCopyBean(Location.class, locatieInvoer));
  }

  public VerhuisAanvraagAdres getNieuwAdres() {
    return nieuwAdres;
  }

  public void setNieuwAdres(VerhuisAanvraagAdres nieuwAdres) {
    this.nieuwAdres = nieuwAdres;
  }

  public List<VerhuisPersoon> getPersonen() {
    return personen;
  }

  // Algemene methodes

  public void setPersonen(List<VerhuisPersoon> personen) {
    this.personen = personen;
  }

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  @Override
  public String getSoort() {
    return getTypeVerhuizing().getOms();
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.get(along(getIndVerwerkt()));
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  public VerhuisToestemminggever getToestemminggever() {
    return toestemminggever;
  }

  public void setToestemminggever(VerhuisToestemminggever toestemminggever) {
    this.toestemminggever = toestemminggever;
  }

  @Override
  public ZaakType getType() {
    return ZaakType.VERHUIZING;
  }

  public VerhuisType getTypeVerhuizing() {
    return VerhuisType.get(along(getVerhuisType()));
  }

  public void setTypeVerhuizing(VerhuisType typeVerhuizing) {
    setVerhuisType(toBigDecimal(typeVerhuizing.getCode()));
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public boolean isBestemdVoorMidoffice() {
    return bestemdVoorMidoffice;
  }

  public void setBestemdVoorMidoffice(boolean bestemdVoorMidoffice) {
    this.bestemdVoorMidoffice = bestemdVoorMidoffice;
  }

  public boolean isSprakeVanInwoning() {
    return fil(getWijzeBewon()) && getWijzeBewon().equalsIgnoreCase("I");
  }

  public void setSprakeVanInwoning(boolean isSprake) {
    setWijzeBewon(isSprake ? "I" : "");
    setToestGeg(toBigDecimal(isSprake ? 1 : 0));
  }
}
