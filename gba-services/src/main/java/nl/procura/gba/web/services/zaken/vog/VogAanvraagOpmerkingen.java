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

import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;

public class VogAanvraagOpmerkingen implements Serializable {

  private static final long serialVersionUID = 6009130265538386679L;

  private String             byzonderhedenTekst = "";
  private String             persisterenTekst   = "";
  private String             covogAdviesTekst   = "";
  private String             toelichtingTekst   = "";
  private BurgemeesterAdvies burgemeesterAdvies = BurgemeesterAdvies.ONBEKEND;

  private VogAanvraag aanvraag;

  public VogAanvraagOpmerkingen(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public BurgemeesterAdvies getBurgemeesterAdvies() {
    return burgemeesterAdvies;
  }

  public void setBurgemeesterAdvies(BurgemeesterAdvies burgemeesterAdvies) {
    this.burgemeesterAdvies = burgemeesterAdvies;
    getAanvraag().setBurgAdvies(burgemeesterAdvies.getCode());
  }

  public String getByzonderhedenTekst() {
    return byzonderhedenTekst;
  }

  public void setByzonderhedenTekst(String byzonderhedenTekst) {
    this.byzonderhedenTekst = byzonderhedenTekst;
    getAanvraag().setBijzonderTekst(byzonderhedenTekst);
  }

  public String getCovogAdviesTekst() {
    return covogAdviesTekst;
  }

  public void setCovogAdviesTekst(String covogAdviesTekst) {
    this.covogAdviesTekst = covogAdviesTekst;
    getAanvraag().setVogAdviesTekst(covogAdviesTekst);
  }

  public String getOpmerkingenGemeente() {

    String opm = "";

    opm += addOp("Omstandigheden: " + getAanvraag().getScreening().getOmstandighedenTekst());
    opm += addOp("Bijzonderheden: " + getByzonderhedenTekst());
    opm += addOp("COVOG advies: " + getCovogAdviesTekst());
    opm += addOp("Persisteren: " + getPersisterenTekst());
    opm += addOp("Toelichting: " + getToelichtingTekst());

    return opm;
  }

  public String getPersisterenTekst() {
    return persisterenTekst;
  }

  public void setPersisterenTekst(String persisterenTekst) {
    this.persisterenTekst = persisterenTekst;
    getAanvraag().setVogPersistTekst(persisterenTekst);
  }

  public String getToelichtingTekst() {
    return toelichtingTekst;
  }

  public void setToelichtingTekst(String toelichtingTekst) {
    this.toelichtingTekst = toelichtingTekst;
    getAanvraag().setToelichtingTekst(toelichtingTekst);
  }

  public boolean isByzonderheden() {
    return fil(getByzonderhedenTekst());
  }

  public boolean isCovogAdvies() {
    return fil(getCovogAdviesTekst());
  }

  public boolean isPersisteren() {
    return fil(getPersisterenTekst());
  }

  public boolean isToelichting() {
    return fil(getToelichtingTekst());
  }

  private String addOp(String s) {

    return s + "\n";
  }
}
