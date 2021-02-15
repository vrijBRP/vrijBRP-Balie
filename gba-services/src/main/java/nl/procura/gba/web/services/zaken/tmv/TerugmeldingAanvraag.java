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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Terugmelding;
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

public class TerugmeldingAanvraag extends Terugmelding implements ContactZaak {

  private static final long serialVersionUID = 58771955842942963L;

  private final GenericZaak             zaak         = new GenericZaak();
  private List<TerugmeldingDetail>      details      = new ArrayList<>();
  private List<TerugmeldingRegistratie> registraties = new ArrayList<>();
  private List<TerugmeldingReactie>     reacties     = new ArrayList<>();

  public TerugmeldingAanvraag() {
    setUsrAfh(new Usr(BaseEntity.DEFAULT));
    setUsrToev(new Usr(BaseEntity.DEFAULT));
    setUsrVerantw(new Usr(BaseEntity.DEFAULT));
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  public UsrFieldValue getAfgehandeldDoor() {
    return new UsrFieldValue(getUsrAfh().getCUsr(), getUsrAfh().getUsrfullname());
  }

  public void setAfgehandeldDoor(UsrFieldValue ingevoerdDoor) {
    Usr usr = new Usr();
    usr.setCUsr(along(ingevoerdDoor.getValue()));
    usr.setUsrfullname(ingevoerdDoor.getDescription());
    setUsrAfh(usr);
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
    return new BsnFieldValue(getSnr());
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setSnr(FieldValue.from(bsn).getStringValue());
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
    return new DateTime(getDIn());
  }

  @Override
  public void setDatumIngang(DateTime dt) {
    setDIn(toBigDecimal(dt.getLongDate()));
  }

  public DateTime getDatumRappel() {
    return new DateTime(getDRap());
  }

  public void setDatumRappel(DateTime datum) {
    setDRap(toBigDecimal(datum.getLongDate()));
  }

  public DateTime getDatumTijdAfhandeling() {
    return new DateTime(getDEnd(), getTEnd());
  }

  public void setDatumTijdAfhandeling(DateTime dateTime) {

    setDEnd(toBigDecimal(dateTime.getLongDate()));
    setTEnd(toBigDecimal(dateTime.getLongTime()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDIn(), getTIn());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dateTime) {

    setDIn(toBigDecimal(dateTime.getLongDate()));
    setTIn(toBigDecimal(dateTime.getLongTime()));
  }

  public List<TerugmeldingDetail> getDetails() {
    return details;
  }

  public void setDetails(List<TerugmeldingDetail> details) {
    this.details = details;
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
    return new UsrFieldValue(getUsrToev().getCUsr(), getUsrToev().getUsrfullname());
  }

  @Override
  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsrToev(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
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

  public List<TerugmeldingReactie> getReacties() {
    Collections.sort(reacties);
    return reacties;
  }

  // Algemene methodes

  public void setReacties(List<TerugmeldingReactie> reacties) {
    this.reacties = reacties;
  }

  public List<TerugmeldingRegistratie> getRegistraties() {
    return registraties;
  }

  public void setRegistraties(List<TerugmeldingRegistratie> registraties) {
    this.registraties = registraties;
  }

  public TerugmeldingRegistratie getRegistratieTmv() {

    for (TerugmeldingRegistratie r : getRegistraties()) {
      if (r.getActie() == TmvActie.REGISTRATIE) {
        return r;
      }
    }

    return new TerugmeldingRegistratie();
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

  public TerugmeldingRegistratie getStatusTmv() {

    for (TerugmeldingRegistratie r : getRegistraties()) {
      if (r.getActie() != TmvActie.REGISTRATIE) {
        return r;
      }
    }

    return new TerugmeldingRegistratie();
  }

  @Override
  public ZaakType getType() {
    return ZaakType.TERUGMELDING;
  }

  public UsrFieldValue getVerantwoordelijke() {
    return new UsrFieldValue(getUsrVerantw().getCUsr(), getUsrVerantw().getUsrfullname());
  }

  public void setVerantwoordelijke(UsrFieldValue ingevoerdDoor) {
    Usr usr = new Usr();
    usr.setCUsr(along(ingevoerdDoor.getValue()));
    usr.setUsrfullname(ingevoerdDoor.getDescription());
    setUsrVerantw(usr);
  }

  public TmvWaarschuwing getWaarschuwing() {

    TmvWaarschuwing w = new TmvWaarschuwing();

    if (getStatusTmv().isStored()) {

      TmvStatus tmvStatus = getStatusTmv().getStatus();
      String s = getStatus().getOms();
      String x = getStatusTmv().getStatus().getOms();

      if (getStatus().isKleinerDan(ZaakStatusType.INBEHANDELING) && pos(tmvStatus.getCode())) {

        w.setMsg(MessageFormat.format(
            "De interne status <b>{0}</b> loopt achter bij de externe status <b>{1}</b>. Wijzig de interne status in <b>{2}</b>",
            s.toLowerCase(), x.toLowerCase(), "in behandeling"));
      }

      if (getStatus().isKleinerDan(ZaakStatusType.VERWERKT) && (tmvStatus == TmvStatus.GESLOTEN)) {

        w.setMsg(MessageFormat.format(
            "De interne status <b>{0}</b> loopt achter bij de externe status <b>{1}</b>. Wijzig de interne status in <b>{2}</b>",
            s.toLowerCase(), x.toLowerCase(), "verwerkt"));
      }
    }

    return w;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }
}
