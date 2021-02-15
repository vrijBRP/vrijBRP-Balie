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

package nl.procura.diensten.zoekpersoon.objecten;

public class Woningkaartgegevens {

  private String volgorde_gezin             = "";
  private String volgorde_persoon           = "";
  private String datum_aanvang_adreshouding = "";
  private String datum_einde_adreshouding   = "";
  private String datum_inschrijving         = "";
  private String woning_code                = "";

  public Woningkaartgegevens() {
  }

  public String getVolgorde_gezin() {
    return volgorde_gezin;
  }

  public void setVolgorde_gezin(String volgordeGezin) {
    volgorde_gezin = volgordeGezin;
  }

  public String getVolgorde_persoon() {
    return volgorde_persoon;
  }

  public void setVolgorde_persoon(String volgordePersoon) {
    volgorde_persoon = volgordePersoon;
  }

  public String getDatum_aanvang_adreshouding() {
    return datum_aanvang_adreshouding;
  }

  public void setDatum_aanvang_adreshouding(String datumAanvangAdreshouding) {
    datum_aanvang_adreshouding = datumAanvangAdreshouding;
  }

  public String getDatum_einde_adreshouding() {
    return datum_einde_adreshouding;
  }

  public void setDatum_einde_adreshouding(String datumEindeAdreshouding) {
    datum_einde_adreshouding = datumEindeAdreshouding;
  }

  public String getDatum_inschrijving() {
    return datum_inschrijving;
  }

  public void setDatum_inschrijving(String datumInschrijving) {
    datum_inschrijving = datumInschrijving;
  }

  public String getWoning_code() {
    return woning_code;
  }

  public void setWoning_code(String woning_code) {
    this.woning_code = woning_code;
  }
}
