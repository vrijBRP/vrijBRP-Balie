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

package nl.procura.gba.web.services.zaken.gemeenteinbox;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Inbox;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.GenericZaak;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GemeenteInboxRecord extends Inbox implements Zaak {

  private final GenericZaak         zaak           = new GenericZaak();
  private final List<DMSDocument>   documents      = new ArrayList<>();
  private List<GemeenteInboxRecord> relatedRecords = new ArrayList<>();
  private InboxItem                 inboxItem;
  private byte[]                    bestandsbytes;

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue();
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
  }

  @Override
  public BasePLExt getBasisPersoon() {
    return zaak.getBasisPersoon();
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    zaak.setBasisPersoon(basisPersoon);
  }

  public byte[] getBestandsbytes() {
    return bestandsbytes;
  }

  public void setBestandsbytes(byte[] bestandsbytes) {
    this.bestandsbytes = bestandsbytes;
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getdIngang());
  }

  @Override
  public void setDatumIngang(DateTime date) {
    setdIngang(toBigDecimal(date.getLongDate()));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getdInvoer(), gettInvoer());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {
    setdInvoer(toBigDecimal(dt.getLongDate()));
    settInvoer(toBigDecimal(dt.getLongTime()));
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

  @Override
  public String getSoort() {
    return getVerwerkingsTypeOmschrijving();
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
    return ZaakType.INBOX;
  }

  public String getVerwerkingId() {
    return getVerwerkId();
  }

  public void setVerwerkingId(String verwerkingId) {
    setVerwerkId(verwerkingId);
  }

  public boolean isNieuwVerzoekType() {
    return Arrays.stream(InboxItemTypeName.values())
        .anyMatch(type -> type.getId().equals(getVerwerkingId()));
  }

  public List<DMSDocument> getDocuments() {
    return documents;
  }

  public List<GemeenteInboxRecord> getRelatedRecords() {
    return relatedRecords;
  }

  public void setRelatedRecords(List<GemeenteInboxRecord> relatedRecords) {
    this.relatedRecords = relatedRecords;
  }

  public String getVerwerkingsTypeOmschrijving() {
    String verwerkingsType = "";
    InboxItemTypeName requestInboxType = InboxItemTypeName.getByName(getVerwerkingId());
    if (requestInboxType.isUnknown()) {
      GbaRestInboxVerwerkingType type = GbaRestInboxVerwerkingType.get(getVerwerkingId());
      if (type != GbaRestInboxVerwerkingType.ONBEKEND) {
        verwerkingsType = type.getDescr();
      }
    } else {
      verwerkingsType = requestInboxType.getDescr();
    }

    return StringUtils.isNotBlank(verwerkingsType)
        ? verwerkingsType
        : "Onbekend: " + getVerwerkingId();
  }

  public InboxItem getInboxItem() {
    return inboxItem;
  }

  public void setInboxItem(InboxItem inboxItem) {
    this.inboxItem = inboxItem;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaak.getZaakHistorie();
  }
}
