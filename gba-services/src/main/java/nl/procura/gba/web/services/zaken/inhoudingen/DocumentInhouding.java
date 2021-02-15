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

package nl.procura.gba.web.services.zaken.inhoudingen;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DocInh;
import nl.procura.gba.jpa.personen.db.DocInhPK;
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
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DocumentInhouding extends DocInh implements ContactZaak {

  private static final long serialVersionUID = -6275820126498536480L;

  private final GenericZaak zaak         = new GenericZaak();
  private boolean           opPlVerwerkt = false;
  private String            autoriteit   = "";

  public DocumentInhouding() {
    setStatus(ZaakStatusType.OPGENOMEN);
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(getId().getAnr());
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    getId().setAnr(FieldValue.from(anr).getStringValue());
  }

  public String getAutoriteit() {
    return autoriteit;
  }

  public void setAutoriteit(String autoriteit) {
    this.autoriteit = autoriteit;
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
    long dInneming = getDInneming();
    long dInvoer = getDIn().longValue();
    return new DateTime(pos(dInneming) ? dInneming : dInvoer);
  }

  @Override
  public void setDatumIngang(DateTime td) {
    setDInneming(td.getLongDate());
  }

  @Override
  public DateTime getDatumTijdInvoer() {

    if (pos(getDIn())) {
      return new DateTime(along(getDIn()), along(getTIn()));
    }

    return new DateTime(getDInneming(), along(getTInneming()));
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {

    setDIn(toBigDecimal(dt.getLongDate()));
    setTIn(toBigDecimal(dt.getLongTime()));
  }

  public ReisdocumentType getDocumentType() {
    return ReisdocumentType.get(getDocType());
  }

  public void setDocumentType(ReisdocumentType type) {
    setDocType(type.getCode());
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
  public DocInhPK getId() {

    if (super.getId() == null) {
      setId(new DocInhPK());
    }

    return super.getId();
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

  public InhoudingType getInhoudingType() {
    return InhoudingType.get(getAandVi());
  }

  public void setInhoudingType(InhoudingType inh) {
    setAandVi(inh.getCode());
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

  public String getNummerDocument() {
    return getId().getNrNlDoc();
  }

  public void setNummerDocument(String nummerDocument) {
    getId().setNrNlDoc(nummerDocument);
  }

  // Algemene methodes

  public DocumentPL getPersoon() {
    return zaak.getPersoon();
  }

  public String getProcesVerbaalNummer() {
    return getPvNr();
  }

  public void setProcesVerbaalNummer(String pvNr) {
    setPvNr(pvNr);
  }

  public String getProcesVerbaalOms() {
    return getPvOms();
  }

  public void setProcesVerbaalOms(String pvOms) {
    setPvOms(pvOms);
  }

  @Override
  public String getSoort() {
    StringBuilder out = new StringBuilder();
    out.append(getInhoudingType().getOms());
    if (isSprakeVanRijbewijs()) {
      out.append(" rijbewijs");
    } else {
      out.append(" reisdocument");
    }
    return out.toString();
  }

  @Override
  public ZaakStatusType getStatus() {

    ZaakStatusType status = ZaakStatusType.get(along(getIndVerwerkt()));

    switch (status) {

      case INBEHANDELING:
        return ZaakStatusType.VERWERKT;

      case INCOMPLEET:
        return ZaakStatusType.OPGENOMEN;

      default:
        return status;
    }
  }

  @Override
  public void setStatus(ZaakStatusType status) {
    setIndVerwerkt(toBigDecimal(status.getCode()));
  }

  @Override
  public ZaakType getType() {
    return ZaakType.INHOUD_VERMIS;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }

  public boolean isMoetNogPv() {
    return (getInhoudingType() == InhoudingType.VERMISSING) && emp(getProcesVerbaalNummer());
  }

  public boolean isOpPlVerwerkt() {
    return opPlVerwerkt;
  }

  public void setOpPlVerwerkt(boolean opPlVerwerkt) {
    this.opPlVerwerkt = opPlVerwerkt;
  }

  public boolean isSprakeVanRijbewijs() {
    return pos(getIndRijbewijs());
  }

  public void setSprakeVanRijbewijs(boolean sprakeVanRijbewijs) {
    setIndRijbewijs(toBigDecimal(sprakeVanRijbewijs ? 1 : 0));
  }
}
