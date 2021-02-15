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

import java.io.Serializable;

public class VogAanvraagDoel implements Serializable {

  private static final long serialVersionUID = 8752423753248448302L;

  private VogDoel doel      = new VogDoel();
  private String  functie   = "";
  private String  doelTekst = "";

  private VogAanvraag aanvraag;

  public VogAanvraagDoel(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public VogDoel getDoel() {
    return doel;
  }

  public void setDoel(VogDoel doel) {
    this.doel = doel;
  }

  public String getDoelTekst() {
    return doelTekst;
  }

  public void setDoelTekst(String doelTekst) {
    this.doelTekst = doelTekst;
    getAanvraag().setDoelTekst(doelTekst);
  }

  public String getFunctie() {
    return functie;
  }

  public void setFunctie(String functie) {

    this.functie = functie;
    getAanvraag().setDoelFunc(functie);
  }
}
