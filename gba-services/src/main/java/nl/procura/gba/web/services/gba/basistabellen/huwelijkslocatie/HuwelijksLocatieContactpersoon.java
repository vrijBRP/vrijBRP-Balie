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

package nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie;

import static java.text.MessageFormat.format;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class HuwelijksLocatieContactpersoon {

  private final HuwelijksLocatie locatie;
  private FieldValue             land = new FieldValue();

  public HuwelijksLocatieContactpersoon(HuwelijksLocatie locatie) {
    this.locatie = locatie;
  }

  public String getAdres() {
    return locatie.getAdres();
  }

  public void setAdres(String adres) {
    locatie.setAdres(adres);
  }

  public String getEmail() {
    return locatie.getEmail();
  }

  public void setEmail(String email) {
    locatie.setEmail(email);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue waarde) {
    this.land = FieldValue.from(waarde);
    locatie.setcLand(land.getBigDecimalValue());
  }

  public String getPlaats() {
    return locatie.getPlaats();
  }

  public void setPlaats(String plaats) {
    locatie.setPlaats(plaats);
  }

  public String getPostcode() {
    return locatie.getPc();
  }

  public void setPostcode(String postcode) {
    locatie.setPc(postcode);
  }

  public String getTav() {
    return getTavAanhef() + " " + getTavVoorl() + " " + getTavNaam();
  }

  public String getTavAanhef() {
    return locatie.getTavAanhef();
  }

  public void setTavAanhef(String aanhef) {
    locatie.setTavAanhef(aanhef);
  }

  public String getTavNaam() {
    return locatie.getTavNaam();
  }

  public void setTavNaam(String naam) {
    locatie.setTavNaam(naam);
  }

  public String getTavVoorl() {
    return locatie.getTavVoorl();
  }

  public void setTavVoorl(String voorl) {
    locatie.setTavVoorl(voorl);
  }

  public String getTelefoon() {
    return locatie.getTel();
  }

  public void setTelefoon(String telefoon) {
    locatie.setTel(telefoon);
  }

  public FieldValue getTerAttentieVanAanhef() {
    return new FieldValue(locatie.getTavAanhef());
  }

  public void setTerAttentieVanAanhef(FieldValue tav) {
    locatie.setTavAanhef(FieldValue.from(tav).getStringValue());
  }

  @Override
  public String toString() {
    return format("{0} {1} {2}", getTavAanhef(), getTavVoorl(), getTavNaam());
  }
}
