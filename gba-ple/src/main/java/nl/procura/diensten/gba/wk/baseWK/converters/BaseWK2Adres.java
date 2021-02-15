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

package nl.procura.diensten.gba.wk.baseWK.converters;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.woningkaart.objecten.Adres;
import nl.procura.diensten.woningkaart.objecten.Persoon;

public class BaseWK2Adres {

  private List<Adres> adressen = new ArrayList<>();

  public List<Adres> convertToAdressen(List<BaseWK> basisWoningkaarten) {

    for (BaseWK basisWK : basisWoningkaarten) {

      Adres adres = new Adres();
      List<Persoon> personen = new ArrayList<>();

      adres.setAdres_indicatie(basisWK.getAdres_indicatie().getCode());
      adres.setBuurt_code(basisWK.getBuurt().getCode());

      adres.setGemeentedeel(basisWK.getGemeentedeel().getValue());
      adres.setGemeentedeel_code(basisWK.getGemeentedeel().getCode());

      adres.setWoonplaatsnaam(basisWK.getWoonplaats().getValue());
      adres.setHuisletter(basisWK.getHuisletter().getValue());
      adres.setHuisnummer(basisWK.getHuisnummer().getValue());
      adres.setToevoeging(basisWK.getToevoeging().getValue());
      adres.setAanduiding(basisWK.getAanduiding().getValue());

      adres.setLocatie(basisWK.getLocatie().getValue());
      adres.setLocatie_code(basisWK.getLocatie().getCode());
      adres.setPostcode(basisWK.getPostcode().getValue());
      adres.setPpd_code(basisWK.getPpd().getCode());
      adres.setStemdistrict_code(basisWK.getStemdistrict().getCode());
      adres.setStraat(basisWK.getStraat().getValue());
      adres.setStraat_code(basisWK.getStraat().getCode());
      adres.setSub_buurt_code(basisWK.getSub_buurt().getCode());
      adres.setWijk_code(basisWK.getWijk().getCode());
      adres.setWoning_code(basisWK.getWoning().getCode());
      adres.setWoning_indicatie(basisWK.getWoning_indicatie().getCode());
      adres.setDatum_ingang(basisWK.getDatum_ingang().getValue());
      adres.setDatum_einde(basisWK.getDatum_einde().getValue());
      adres.setVolgcode_einde(basisWK.getVolgcode_einde().getValue());
      adres.setCode_object(basisWK.getCode_object().getValue());

      for (BaseWKPerson basisP : basisWK.getPersonen()) {

        Persoon p = new Persoon();
        p.setAnummer(basisP.getAnummer().getValue());
        p.setBurgerservicenummer(basisP.getBsn().getValue());
        p.setDatum_ingang(basisP.getDatum_ingang().getCode());
        p.setDatum_vertrek(basisP.getDatum_vertrek().getCode());
        p.setGezin_code(basisP.getGezin_code().getCode());
        p.setVolg_code(basisP.getVolg_code().getCode());
        p.setDatum_geboren(basisP.getDatum_geboren().getCode());

        personen.add(p);
      }

      adres.setPersonen(personen.toArray(new Persoon[personen.size()]));

      getAdressen().add(adres);
    }

    return adressen;
  }

  public List<Adres> getAdressen() {
    return adressen;
  }

  public void setAdressen(ArrayList<Adres> adressen) {
    this.adressen = adressen;
  }
}
