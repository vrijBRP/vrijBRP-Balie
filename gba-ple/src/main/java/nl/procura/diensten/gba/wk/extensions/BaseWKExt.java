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

package nl.procura.diensten.gba.wk.extensions;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;

public class BaseWKExt {

  private BaseWK basisWk = null;

  public BaseWKExt(BaseWK basisWk) {
    setBasisWk(basisWk);
  }

  public String getAdres() {
    String locatie = getBasisWk().getLocatie().getDescr();
    String pc = getBasisWk().getPostcode().getDescr();
    String gemDeel = getBasisWk().getGemeentedeel().getDescr();
    String woonplaats = getBasisWk().getWoonplaats().getDescr();
    String plaats = fil(gemDeel) ? gemDeel : woonplaats;
    String straat = getBasisWk().getStraat().getDescr();
    String hnr = getBasisWk().getHuisnummer().getDescr();
    String hnr_l = getBasisWk().getHuisletter().getDescr();
    String hnr_t = getBasisWk().getToevoeging().getDescr();
    String hnr_a = getBasisWk().getAanduiding().getDescr();

    if (fil(locatie)) {
      return trim(locatie + ", " + pc + " " + plaats);
    }

    return trim(trim(straat + " " + hnr + " " + hnr_l + " " + hnr_t + " " + hnr_a) + ", " + pc + " " + plaats);
  }

  public boolean isGeschikvoorBewoning() {
    return aval(getBasisWk().getWoning_indicatie().getCode()) == 0;
  }

  public BaseWK getBasisWk() {
    return basisWk;
  }

  public void setBasisWk(BaseWK basisWk) {
    this.basisWk = basisWk;
  }

  public int getCurrentResidentsCount() {
    return (int) getBasisWk().getPersonen().stream()
        .filter(BaseWKPerson::isCurrentResident)
        .count();
  }

}
