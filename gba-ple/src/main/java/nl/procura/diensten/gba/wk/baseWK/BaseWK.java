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

package nl.procura.diensten.gba.wk.baseWK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseWK implements Serializable {

  private static final long serialVersionUID = 8641526500781189887L;

  private BaseWKValue gemeentedeel = new BaseWKValue();
  private BaseWKValue straat       = new BaseWKValue();
  private BaseWKValue huisnummer   = new BaseWKValue();
  private BaseWKValue huisletter   = new BaseWKValue();
  private BaseWKValue toevoeging   = new BaseWKValue();
  private BaseWKValue aanduiding   = new BaseWKValue();
  private BaseWKValue postcode     = new BaseWKValue();
  private BaseWKValue locatie      = new BaseWKValue();

  private BaseWKValue ppd          = new BaseWKValue();
  private BaseWKValue stemdistrict = new BaseWKValue();
  private BaseWKValue pnd          = new BaseWKValue();

  private BaseWKValue wijk             = new BaseWKValue();
  private BaseWKValue buurt            = new BaseWKValue();
  private BaseWKValue sub_buurt        = new BaseWKValue();
  private BaseWKValue adres_indicatie  = new BaseWKValue();
  private BaseWKValue woning_indicatie = new BaseWKValue();
  private BaseWKValue woning           = new BaseWKValue();
  private BaseWKValue datum_ingang     = new BaseWKValue();
  private BaseWKValue datum_einde      = new BaseWKValue();
  private BaseWKValue code_object      = new BaseWKValue();
  private BaseWKValue volgcode_einde   = new BaseWKValue();
  private BaseWKValue opmerking        = new BaseWKValue();

  // Bag elementen
  private BaseWKValue woonplaats     = new BaseWKValue();
  private BaseWKValue openbareRuimte = new BaseWKValue();
  private BaseWKValue aon            = new BaseWKValue();
  private BaseWKValue ina            = new BaseWKValue();

  private List<BaseWKPerson> personen = new ArrayList<>();

  public BaseWKValue getGemeentedeel() {
    return gemeentedeel;
  }

  public void setGemeentedeel(BaseWKValue gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public BaseWKValue getStraat() {
    return straat;
  }

  public void setStraat(BaseWKValue straat) {
    this.straat = straat;
  }

  public BaseWKValue getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(BaseWKValue huisnummer) {
    this.huisnummer = huisnummer;
  }

  public BaseWKValue getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(BaseWKValue huisletter) {
    this.huisletter = huisletter;
  }

  public BaseWKValue getToevoeging() {
    return toevoeging;
  }

  public void setToevoeging(BaseWKValue toevoeging) {
    this.toevoeging = toevoeging;
  }

  public BaseWKValue getAanduiding() {
    return aanduiding;
  }

  public void setAanduiding(BaseWKValue aanduiding) {
    this.aanduiding = aanduiding;
  }

  public BaseWKValue getPostcode() {
    return postcode;
  }

  public void setPostcode(BaseWKValue postcode) {
    this.postcode = postcode;
  }

  public BaseWKValue getLocatie() {
    return locatie;
  }

  public void setLocatie(BaseWKValue locatie) {
    this.locatie = locatie;
  }

  public BaseWKValue getStemdistrict() {
    return stemdistrict;
  }

  public void setStemdistrict(BaseWKValue stemdistrict) {
    this.stemdistrict = stemdistrict;
  }

  public BaseWKValue getWijk() {
    return wijk;
  }

  public void setWijk(BaseWKValue wijk) {
    this.wijk = wijk;
  }

  public BaseWKValue getBuurt() {
    return buurt;
  }

  public void setBuurt(BaseWKValue buurt) {
    this.buurt = buurt;
  }

  public BaseWKValue getSub_buurt() {
    return sub_buurt;
  }

  public void setSub_buurt(BaseWKValue sub_buurt) {
    this.sub_buurt = sub_buurt;
  }

  public BaseWKValue getAdres_indicatie() {
    return adres_indicatie;
  }

  public void setAdres_indicatie(BaseWKValue adres_indicatie) {
    this.adres_indicatie = adres_indicatie;
  }

  public BaseWKValue getWoning_indicatie() {
    return woning_indicatie;
  }

  public void setWoning_indicatie(BaseWKValue woning_indicatie) {
    this.woning_indicatie = woning_indicatie;
  }

  public BaseWKValue getDatum_ingang() {
    return datum_ingang;
  }

  public void setDatum_ingang(BaseWKValue datum_ingang) {
    this.datum_ingang = datum_ingang;
  }

  public BaseWKValue getDatum_einde() {
    return datum_einde;
  }

  public void setDatum_einde(BaseWKValue datum_einde) {
    this.datum_einde = datum_einde;
  }

  public BaseWKValue getCode_object() {
    return code_object;
  }

  public void setCode_object(BaseWKValue code_object) {
    this.code_object = code_object;
  }

  public BaseWKValue getVolgcode_einde() {
    return volgcode_einde;
  }

  public void setVolgcode_einde(BaseWKValue volgcode_einde) {
    this.volgcode_einde = volgcode_einde;
  }

  public List<BaseWKPerson> getPersonen() {
    return personen;
  }

  public void setPersonen(ArrayList<BaseWKPerson> personen) {
    this.personen = personen;
  }

  public BaseWKValue getWoning() {
    return woning;
  }

  public void setWoning(BaseWKValue woning) {
    this.woning = woning;
  }

  public BaseWKValue getPpd() {
    return ppd;
  }

  public void setPpd(BaseWKValue ppd) {
    this.ppd = ppd;
  }

  public BaseWKValue getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(BaseWKValue woonplaats) {
    this.woonplaats = woonplaats;
  }

  public BaseWKValue getOpmerking() {
    return opmerking;
  }

  public void setOpmerking(BaseWKValue opmerking) {
    this.opmerking = opmerking;
  }

  public BaseWKValue getOpenbareRuimte() {
    return openbareRuimte;
  }

  public void setOpenbareRuimte(BaseWKValue openbareRuimte) {
    this.openbareRuimte = openbareRuimte;
  }

  public BaseWKValue getAon() {
    return aon;
  }

  public void setAon(BaseWKValue aon) {
    this.aon = aon;
  }

  public BaseWKValue getIna() {
    return ina;
  }

  public void setIna(BaseWKValue ina) {
    this.ina = ina;
  }

  public BaseWKValue getPnd() {
    return pnd;
  }

  public void setPnd(BaseWKValue pnd) {
    this.pnd = pnd;
  }
}
