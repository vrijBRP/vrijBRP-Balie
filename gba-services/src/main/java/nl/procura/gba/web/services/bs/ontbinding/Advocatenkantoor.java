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

package nl.procura.gba.web.services.bs.ontbinding;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Postcode;

public class Advocatenkantoor {

  private final DossierOntbinding zaakDossier;

  public Advocatenkantoor(DossierOntbinding zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  public String getAdres() {
    return zaakDossier.getAdvAdres();
  }

  public void setAdres(String adres) {
    zaakDossier.setAdvAdres(adres);
  }

  public String getKenmerk() {
    return zaakDossier.getAdvKenmerk();
  }

  public void setKenmerk(String kenmerk) {
    zaakDossier.setAdvKenmerk(kenmerk);
  }

  public String getKenmerk2() {
    return zaakDossier.getAdvKenmerk2();
  }

  public void setKenmerk2(String kenmerk2) {
    zaakDossier.setAdvKenmerk2(kenmerk2);
  }

  public FieldValue getLand() {
    return new FieldValue(zaakDossier.getAdvLand());
  }

  public void setLand(FieldValue land) {
    zaakDossier.setAdvLand(FieldValue.from(land).getStringValue());
  }

  public String getNaam() {
    return zaakDossier.getAdvNaam();
  }

  public void setNaam(String naam) {
    zaakDossier.setAdvNaam(naam);
  }

  public String getPlaats() {
    return zaakDossier.getAdvPlaats();
  }

  public void setPlaats(String plaats) {
    zaakDossier.setAdvPlaats(plaats);
  }

  public FieldValue getPostcode() {
    String advPc = zaakDossier.getAdvPc();
    return new FieldValue(advPc, Postcode.getFormat(advPc));
  }

  public void setPostcode(FieldValue postcode) {
    zaakDossier.setAdvPc(FieldValue.from(postcode).getStringValue());
  }

  public FieldValue getTavAanhef() {
    return new FieldValue(zaakDossier.getAdvTavAanhef());
  }

  public void setTavAanhef(FieldValue tav) {
    zaakDossier.setAdvTavAanhef(FieldValue.from(tav).getStringValue());
  }

  public String getTavNaam() {
    return zaakDossier.getAdvTavNaam();
  }

  public void setTavNaam(String tav) {
    zaakDossier.setAdvTavNaam(tav);
  }

  public String getTavVoorl() {
    return zaakDossier.getAdvTavVoorl();
  }

  public void setTavVoorl(String tav) {
    zaakDossier.setAdvTavVoorl(tav);
  }

  public DossierOntbinding getZaakDossier() {
    return zaakDossier;
  }

  public boolean isVanToepassing() {
    return fil(zaakDossier.getAdvNaam());
  }
}
