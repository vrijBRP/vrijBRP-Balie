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

package nl.procura.gba.web.services.beheer.gebruiker.info;

import static nl.procura.standard.Globalfunctions.eq;

public enum GebruikerInfoType {

  email("email", "E-mail"),
  ondertekening_naam("ondertekening_naam", "Naam ondertekening documenten"),
  afdelingsnaam("afdelingsnaam", "Afdelingsnaam"),
  bezoekadres_adres_pc("bezoekadres_adres_pc", "Bezoekadres straatnaam, huisnummer en postcode"),
  bezoekadres_plaats("bezoekadres_plaats", "Bezoekadres plaats"),
  fax("fax", "Faxnummer"),
  functie_afdeling("functie_afdeling", "Functie en/of afdeling"),
  gemeentecode("gemeentecode", "Gemeentecode"),
  kenmerk("kenmerk", "Kenmerk"),
  korpschef1("korpschef1", "Adressering korpschef tbv naturalisatie/optie"),
  korpschef2("korpschef2", "Adressering korpschef tbv naturalisatie/optie (plaats)"),
  openingstijden_avondopenstelling("openingstijden_avondopenstelling", "Avondopenstelling"),
  openingstijden_regulier("openingstijden_regulier", "Reguliere openingstijden"),
  openingstijden_bijzonder("openingstijden_bijzonder", "Bijzondere openingstijden"),
  postadres_adres_pc("postadres_adres_pc", "Postadres postbus en postcode"),
  postadres_plaats("postadres_plaats", "Postadres plaats"),
  postadres2("postadres2", "Postadres voor voettekst"),
  telefoon("telefoon", "Doorkiesnummer"),
  telefoon_algemeen("telefoon_algemeen", "Algemeen telefoonnummer evt 140-nr."),
  ovj1("ovj1", "Adressering Officier van justitie"),
  ovj2("ovj2", "Adressering Officier van justitie (plaats)"),
  rb3("rb1", "Adressering Rechtbank"),
  rb4("rb2", "Adressering Rechtbank (plaats)"),
  rechtbank_bezwaarclausule("rechtbank_bezwaarclausule",
      "Adressering rechtbank bezwaarclausule ivm voorlopige voorziening"),
  leges_BS("leges_BS", "Legesbedrag uittreksel BS"),
  provincie("provincie", "Provincie"),
  webadres("webadres", "Webadres");

  private String descr = "";
  private String key   = "";

  GebruikerInfoType(String key, String descr) {

    setKey(key);
    setDescr(descr);
  }

  public static boolean exists(String key) {
    return get(key) != null;
  }

  public static GebruikerInfoType get(String key) {

    for (GebruikerInfoType u : values()) {
      if (eq(u.getKey(), key)) {
        return u;
      }
    }
    return null;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
