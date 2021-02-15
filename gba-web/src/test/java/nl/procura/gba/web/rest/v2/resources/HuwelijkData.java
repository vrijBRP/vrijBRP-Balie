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

package nl.procura.gba.web.rest.v2.resources;

import static nl.procura.gba.web.rest.v2.model.zaken.huwelijk.GbaRestHuwelijkOptieType.*;

import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakAlgemeen;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakStatusType;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakType;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.huwelijk.*;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestTelefoonBuitenland;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.proweb.rest.utils.JsonUtils;

public class HuwelijkData {

  public static void main(String[] args) {

    GbaRestZaakToevoegenVraag request = new GbaRestZaakToevoegenVraag();
    GbaRestZaak zaak = new GbaRestZaak();
    zaak.setAlgemeen(getZaakAlgemeen());
    zaak.setHuwelijk(getHuwelijk());
    request.setZaak(zaak);

    System.out.println(JsonUtils.getPrettyObject(request));
  }

  private static GbaRestHuwelijk getHuwelijk() {
    GbaRestHuwelijk huwelijk = new GbaRestHuwelijk();
    huwelijk.setPartner1(getPartner1(getContactgegevens()));
    huwelijk.setPartner2(getPartner2(getContactgegevens()));
    huwelijk.setPlanning(getPlanning());
    huwelijk.setLocatie(getLocatie());
    huwelijk.setGetuigen(getGetuigen());
    huwelijk.setAmbtenaren(getAmbtenaren());
    return huwelijk;
  }

  private static GbaRestHuwelijkAmbtenaren getAmbtenaren() {
    GbaRestHuwelijkAmbtenaren ambtenaren = new GbaRestHuwelijkAmbtenaren();
    GbaRestHuwelijkAmbtenaar ambt = new GbaRestHuwelijkAmbtenaar();
    ambt.getAliassen().add("alias1");
    ambtenaren.getVoorkeuren().add(ambt);
    return ambtenaren;
  }

  private static GbaRestHuwelijkGetuigen getGetuigen() {

    GbaRestHuwelijkGetuige getuige1 = new GbaRestHuwelijkGetuige();
    GbaRestPersoon p1 = new GbaRestPersoon();
    p1.setVoornamen("Firstname 1");
    p1.setGeslachtsnaam("Lastname 1");
    p1.setVoorvoegsel("prefix 1");
    p1.setGeboortedatum(20041210);
    p1.setTitelPredikaat("B");
    p1.setToelichting("Witness 1");
    getuige1.setPersoon(p1);

    GbaRestHuwelijkGetuige getuige2 = new GbaRestHuwelijkGetuige();
    GbaRestPersoon p2 = new GbaRestPersoon();
    p2.setBsn(255_690_228L);
    p2.setToelichting("Witness 2");
    getuige2.setPersoon(p2);

    GbaRestHuwelijkGetuigen getuigen = new GbaRestHuwelijkGetuigen();
    getuigen.addEigenGetuige(getuige1);
    getuigen.addEigenGetuige(getuige2);

    getuigen.setAantalGemeenteGetuigen(2);
    return getuigen;
  }

  private static GbaRestHuwelijkLocatie getLocatie() {
    GbaRestHuwelijkLocatie locatie = new GbaRestHuwelijkLocatie();
    locatie.setNaam("Het Generaalshuis (Theater aan het Vrijthof)");
    locatie.getAliassen().add("generaalshuis");
    locatie.getOpties().add(getOptie(TEKST,
        "Taalceremonie",
        "taal_ceremonie",
        "Spaans",
        "Taalceremonie"));
    locatie.getOpties().add(getOptie(NUMMER,
        "Max. aantal personen",
        "max_personen",
        "75",
        "Max. aantal personen (1 - 250)"));
    locatie.getOpties().add(getOptie(BOOLEAN,
        "Trouwboekje/Partnerschapsboekje",
        "trouwboekje",
        "true",
        "Trouwboekje/Partnerschapsboekje"));
    return locatie;
  }

  private static GbaRestHuwelijkOptie getOptie(GbaRestHuwelijkOptieType type,
      String naam, String alias, String waarde, String oms) {
    GbaRestHuwelijkOptie opt = new GbaRestHuwelijkOptie();
    opt.setType(type);
    opt.setNaam(naam);
    opt.setOmschrijving(oms);
    opt.getAliassen().add(alias);
    opt.setWaarde(waarde);
    return opt;
  }

  private static GbaRestHuwelijkPlanning getPlanning() {
    GbaRestHuwelijkPlanning planning = new GbaRestHuwelijkPlanning();
    planning.setSoort(GbaRestHuwelijkVerbintenisType.HUWELIJK);
    planning.setDatumVerbintenis(2020_06_07);
    planning.setTijdVerbintenis(8_09_10);
    planning.setDatumVoornemen(2020_04_01);
    planning.setToelichting("Nice Wedding");
    return planning;
  }

  private static GbaRestZaakAlgemeen getZaakAlgemeen() {
    GbaRestZaakAlgemeen algemeen = new GbaRestZaakAlgemeen();
    algemeen.setZaakId("");
    algemeen.setType(GbaRestZaakType.HUWELIJK_GPS_GEMEENTE);
    algemeen.setStatus(GbaRestZaakStatusType.INCOMPLEET);
    algemeen.setBron("PROWEB Personen");
    algemeen.setLeverancier("PROCURA");
    algemeen.setDatumIngang(2020_02_01);
    algemeen.setDatumInvoer(2020_04_28);
    algemeen.setTijdInvoer(10_11_12);
    return algemeen;
  }

  private static GbaRestHuwelijkPartner getPartner1(GbaRestContactgegevens cg) {
    GbaRestHuwelijkPartner partner = new GbaRestHuwelijkPartner();
    GbaRestPersoon persoon = new GbaRestPersoon();
    persoon.setBsn(Testdata.TEST_BSN_1);
    persoon.setContactgegevens(cg);

    GbaRestHuwelijkNaamgebruik ng = new GbaRestHuwelijkNaamgebruik();
    ng.setGeslachtsnaam("Geld");
    ng.setVoorvoegsel("de");
    ng.setTitelPredikaat("JH");
    ng.setType(GbaRestHuwelijkNaamgebruikType.V);

    partner.setPersoon(persoon);
    partner.setNaamgebruik(ng);
    return partner;
  }

  private static GbaRestHuwelijkPartner getPartner2(GbaRestContactgegevens cg) {
    GbaRestHuwelijkPartner partner = new GbaRestHuwelijkPartner();
    GbaRestPersoon persoon = new GbaRestPersoon();
    persoon.setBsn(Testdata.TEST_BSN_2);
    persoon.setContactgegevens(cg);

    GbaRestHuwelijkNaamgebruik ng = new GbaRestHuwelijkNaamgebruik();
    ng.setGeslachtsnaam("Duck");
    ng.setVoorvoegsel("");
    ng.setTitelPredikaat("B");
    ng.setType(GbaRestHuwelijkNaamgebruikType.V);

    partner.setPersoon(persoon);
    partner.setNaamgebruik(ng);
    return partner;
  }

  private static GbaRestContactgegevens getContactgegevens() {
    GbaRestContactgegevens cg = new GbaRestContactgegevens();
    cg.setEmail("burgerzaken@procura.nl");
    cg.setTelefoonThuis("1234");
    cg.setTelefoonWerk("5678");
    cg.setTelefoonMobiel("9876");

    GbaRestTelefoonBuitenland bl = new GbaRestTelefoonBuitenland();
    bl.setTelefoon("3232");
    bl.setLandCode(5010);
    cg.setTelefoonBuitenland(bl);
    return cg;
  }
}
