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

import static nl.procura.standard.Globalfunctions.astr;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.zaken.algemeen.IdentificatieNummers;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class VerhuisAangever implements IdentificatieNummers, Serializable {

  private static final long serialVersionUID = 2462249944290337558L;

  private boolean    hoofdInstelling = false;
  private boolean    ambtshalve      = false;
  private DocumentPL persoon;

  private VerhuisAanvraag aanvraag;

  public VerhuisAangever(VerhuisAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(aanvraag.getAangifteAnr());
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    getAanvraag().setAangifteAnr(anr.getStringValue());
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(aanvraag.getAangifteBsn()));
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    getAanvraag().setAangifteBsn(bsn.getBigDecimalValue());
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public String getToelichting() {
    return aanvraag.getAangeverToelichting();
  }

  public void setToelichting(String toelichting) {
    aanvraag.setAangeverToelichting(toelichting);
  }

  public boolean isAmbtshalve() {
    return ambtshalve;
  }

  public void setAmbtshalve(boolean ambtshalve) {
    this.ambtshalve = ambtshalve;
  }

  public boolean isHoofdInstelling() {
    return hoofdInstelling;
  }

  public void setHoofdInstelling(boolean hoofdInstelling) {
    this.hoofdInstelling = hoofdInstelling;
  }

  public String toString() {

    if (isHoofdInstelling()) {
      return AangifteSoort.HOOFDINSTELLING.getOms();
    }
    if (isAmbtshalve()) {
      return AangifteSoort.AMBTSHALVE.getOms();
    } else if (getPersoon() != null) {
      return getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
    }

    return "";
  }
}
