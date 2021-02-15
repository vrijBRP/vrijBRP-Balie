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

package nl.procura.gba.web.services.zaken.legezaak;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakHistorie;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class LegeZaak implements Zaak {

  private final ZaakHistorie zaakHistorie  = null;
  private BasePLExt          pl;
  private Gebruiker          gebruiker;
  private Gemeente           gemeente      = null;
  private BasePLExt          basisPersoon  = null;
  private Identificatie      identificatie = null;

  public LegeZaak(BasePLExt pl, Gebruiker gebruiker) {
    this.pl = pl;
    this.gebruiker = gebruiker;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(pl.getPersoon().getAnr().getCode());
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
  }

  @Override
  public BasePLExt getBasisPersoon() {
    return basisPersoon;
  }

  @Override
  public void setBasisPersoon(BasePLExt basisPersoon) {
    this.basisPersoon = basisPersoon;
  }

  @Override
  public String getBron() {
    return "";
  }

  @Override
  public void setBron(String bron) {
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(pl.getPersoon().getBsn().getCode());
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime();
  }

  @Override
  public void setDatumIngang(DateTime date) {
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime();
  }

  @Override
  public void setDatumTijdInvoer(DateTime dateTime) {
  }

  @Override
  public Gemeente getGemeente() {
    return gemeente;
  }

  @Override
  public void setGemeente(Gemeente gemeente) {
    this.gemeente = gemeente;
  }

  @Override
  public Identificatie getIdentificatie() {
    return identificatie;
  }

  @Override
  public void setIdentificatie(Identificatie identificatie) {
    this.identificatie = identificatie;
  }

  @Override
  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(gebruiker);
  }

  @Override
  public void setIngevoerdDoor(UsrFieldValue usr) {
  }

  @Override
  public String getLeverancier() {
    return "";
  }

  @Override
  public void setLeverancier(String leverancier) {
  }

  @Override
  public Locatie getLocatieInvoer() {
    return null;
  }

  @Override
  public void setLocatieInvoer(Locatie locatie) {
  }

  @Override
  public String getSoort() {
    return "";
  }

  @Override
  public ZaakStatusType getStatus() {
    return ZaakStatusType.OPGENOMEN;
  }

  @Override
  public void setStatus(ZaakStatusType status) {
  }

  @Override
  public ZaakType getType() {
    return ZaakType.LEGE_PERSOONLIJST;
  }

  @Override
  public ZaakHistorie getZaakHistorie() {
    return zaakHistorie;
  }

  @Override
  public String getZaakId() {
    return "";
  }

  @Override
  public void setZaakId(String zaakId) {
  }

  @Override
  public Object getId() {
    return null;
  }

  @Override
  public boolean isStored() {
    return false;
  }
}
