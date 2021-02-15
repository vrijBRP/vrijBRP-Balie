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

package nl.procura.gba.web.services.zaken.rijbewijs;

import java.io.Serializable;

public class Contactgegevens implements Serializable {

  private static final long serialVersionUID = -8004743576364692300L;

  private RijbewijsAanvraag aanvraag;

  public Contactgegevens(RijbewijsAanvraag aanvraag) {
    this.setAanvraag(aanvraag);
  }

  public RijbewijsAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(RijbewijsAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getEmail() {
    return getAanvraag().getEmail();
  }

  public void setEmail(String email) {
    getAanvraag().setEmail(email);
  }

  public String getTelMobiel() {
    return getAanvraag().getTelnrMob();
  }

  public void setTelMobiel(String telMobiel) {
    getAanvraag().setTelnrMob(telMobiel);
  }

  public String getTelThuis() {
    return getAanvraag().getTelnrThuis();
  }

  public void setTelThuis(String telThuis) {
    getAanvraag().setTelnrThuis(telThuis);
  }

  public String getTelWerk() {
    return getAanvraag().getTelnrWerk();
  }

  public void setTelWerk(String telWerk) {
    getAanvraag().setTelnrWerk(telWerk);
  }

  public String toString() {

    return "ContactgegevensImpl [getTelThuis()=" + getTelThuis() + ", getTelWerk()=" + getTelWerk()
        + ", getTelMobiel()=" + getTelMobiel() + ", getEmail()=" + getEmail() + "]";
  }
}
