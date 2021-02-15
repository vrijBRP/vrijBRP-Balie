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

package nl.procura.gba.web.services.zaken.gv;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Gv;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ContactZaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Postcode;

public class GvAanvraag extends Gv implements ContactZaak {

  private static final long serialVersionUID = 2383631675323624860L;

  private final GenericZaak   zaak      = new GenericZaak();
  private DocumentRecord      docBa     = null;
  private DocumentRecord      docGv     = null;
  private GvAanvraagProcessen processen = new GvAanvraagProcessen();

  public GvAanvraag() {
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
    return new DateTime(getDWijz());
  }

  @Override
  public void setDatumIngang(DateTime dt) {
    setDWijz(toBigDecimal(dt.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getDIn(), getTIn());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {
    setDIn(toBigDecimal(dt.getLongDate()));
    setTIn(toBigDecimal(dt.getLongTime()));
  }

  public DocumentRecord getDocBa() {
    return docBa;
  }

  public void setDocBa(DocumentRecord docBa) {
    this.docBa = docBa;
  }

  public DocumentRecord getDocGv() {
    return docGv;
  }

  public void setDocGv(DocumentRecord docGv) {
    this.docGv = docGv;
  }

  // Algemene methodes

  @Override
  public Gemeente getGemeente() {
    return zaak.getGemeente();
  }

  @Override
  public void setGemeente(Gemeente gemeente) {
    zaak.setGemeente(gemeente);
  }

  public KoppelEnumeratieType getGrondslagType() {
    return KoppelEnumeratieType.get(along(getcGrondslag()));
  }

  public void setGrondslagType(KoppelEnumeratieType grondslag) {
    setcGrondslag(toBigDecimal(grondslag == null ? null : grondslag.getCode()));
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

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public FieldValue getPostcode() {
    return new FieldValue(getPc(), Postcode.getFormat(getPc()));
  }

  public void setPostcode(FieldValue postcode) {
    setPc(FieldValue.from(postcode).getStringValue());
  }

  public GvAanvraagProcessen getProcessen() {
    return processen;
  }

  public void setProcessen(GvAanvraagProcessen processen) {
    this.processen = processen;
  }

  @Override
  public String getSoort() {
    if (KoppelEnumeratieType.TK_JA_VOORWAARDELIJK.is(getToekenningType())) {
      return "belangenafweging";
    }

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

  public String getTav() {
    return trim(getTavAanhef() + " " + getTavVoorl() + " " + getTavNaam());
  }

  public FieldValue getTerAttentieVanAanhef() {
    return new FieldValue(getTavAanhef());
  }

  public void setTerAttentieVanAanhef(FieldValue tav) {
    setTavAanhef(FieldValue.from(tav).getStringValue());
  }

  public String getToekenningMotivering() {
    return getMotiveringTk();
  }

  public void setToekenningMotivering(String toekenningMotivering) {
    setMotiveringTk(toekenningMotivering);
  }

  public KoppelEnumeratieType getToekenningType() {
    return KoppelEnumeratieType.get(along(getcToek()));
  }

  public void setToekenningType(KoppelEnumeratieType toekenning) {
    setcToek(toBigDecimal(toekenning == null ? null : toekenning.getCode()));
  }

  @Override
  public ZaakType getType() {
    return ZaakType.GEGEVENSVERSTREKKING;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public boolean isDatumEindeTermijn() {
    return getDatumEindeTermijn() != null && getDatumEindeTermijn().getLongDate() > 0;
  }

  public DateTime getDatumEindeTermijn() {
    if (getProcessen().getProces().isStored()) {
      return getProcessen().getProces().getDatumEindeTermijn();
    }
    return null;
  }
}
