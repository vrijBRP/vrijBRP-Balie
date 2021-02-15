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

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.*;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.*;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestDocumentType;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestOverlijdenAangifte;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.gemeente.GbaRestOverlijdenInGemeente;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestLijkvinding;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestLijkvindingAangifte;
import nl.procura.gba.web.rest.v2.model.zaken.overlijden.lijkvinding.GbaRestSchriftelijkeAangeverType;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestTelefoonBuitenland;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.proweb.rest.utils.JsonUtils;

public class OverlijdenData {

  public static void main(String[] args) {

    GbaRestZaakToevoegenVraag request = new GbaRestZaakToevoegenVraag();
    GbaRestZaak zaak = new GbaRestZaak();
    zaak.setAlgemeen(getZaakAlgemeen());
    zaak.setOverlijden(getOverlijden());
    request.setZaak(zaak);

    System.out.println(JsonUtils.getPrettyObject(request));
  }

  private static GbaRestOverlijden getOverlijden() {
    GbaRestOverlijden overlijden = new GbaRestOverlijden();

    GbaRestOverlijdenInGemeente overlijdenInGemeente = new GbaRestOverlijdenInGemeente();
    overlijdenInGemeente.setAangever(getAangever(getContactgegevens()));
    overlijdenInGemeente.setOverledene(getOverledene(getContactgegevens()));
    overlijdenInGemeente.setAangifte(getAangifteInGemeente());
    overlijdenInGemeente.setVerzoek(getVerzoek());
    overlijdenInGemeente.setLijkbezorging(getLijkbezorging());

    GbaRestLijkvinding lijkvinding = new GbaRestLijkvinding();
    lijkvinding.setOverledene(getOverledene(getContactgegevens()));
    lijkvinding.setAangifte(getAangifteLijkvinding());
    lijkvinding.setVerzoek(getVerzoek());
    lijkvinding.setLijkbezorging(getLijkbezorging());

    overlijden.setOverlijdenInGemeente(overlijdenInGemeente);
    overlijden.setLijkvinding(lijkvinding);

    return overlijden;
  }

  private static GbaRestLijkbezorging getLijkbezorging() {
    GbaRestLijkbezorging lijkbezorging = new GbaRestLijkbezorging();
    lijkbezorging.setDatum(2020_08_26);
    lijkbezorging.setTijd(11_12);
    lijkbezorging.setLijkbezorgingType(GbaRestLijkbezorgingType.BEGRAVING_CREMATIE);
    lijkbezorging.setBuitenBenelux(true);
    lijkbezorging.setDoodsoorzaakType(GbaRestDoodsoorzaakType.NATURAL_CAUSES);
    lijkbezorging.setLandVanBestemming(new GbaRestTabelWaarde("5010", "België"));
    lijkbezorging.setPlaatsVanBestemming("Alkmaar");
    lijkbezorging.setPlaatsVanOntleding("Heiloo");
    lijkbezorging.setVia("Vliegveld");
    lijkbezorging.setVervoermiddel("Vliegtuig");
    return lijkbezorging;
  }

  private static GbaRestVerzoek getVerzoek() {
    GbaRestVerzoek verzoek = new GbaRestVerzoek();
    verzoek.setCorrespondentie(getCorrespondentie());
    verzoek.setUittreksels(getUittreksels());
    return verzoek;
  }

  private static List<GbaRestOverlijdenUittreksel> getUittreksels() {
    List<GbaRestOverlijdenUittreksel> uittreksels = new ArrayList<>();
    GbaRestOverlijdenUittreksel uitt1 = new GbaRestOverlijdenUittreksel();
    uitt1.setCode("uitt-1");
    uitt1.setOmschrijving("Mooi uittreksel 1");
    uitt1.setAantal(1);

    GbaRestOverlijdenUittreksel uitt2 = new GbaRestOverlijdenUittreksel();
    uitt2.setCode("uitt-2");
    uitt2.setOmschrijving("Mooi uittreksel 2");
    uitt2.setAantal(2);

    uittreksels.add(uitt1);
    uittreksels.add(uitt2);
    return uittreksels;
  }

  private static GbaRestOverlijdenCorrespondentie getCorrespondentie() {
    GbaRestOverlijdenCorrespondentie correspondentie = new GbaRestOverlijdenCorrespondentie();
    correspondentie.setType(GbaRestCommunicatieType.EMAIL);
    correspondentie.setOrganisatie("Procura B.V.");
    correspondentie.setAfdeling("Burgerzaken");
    correspondentie.setNaam("Dhr. F. Janssen");
    correspondentie.setEmail("test@procura.nl");
    correspondentie.setStraat("Parelweg");
    correspondentie.setHnr(12);
    correspondentie.setHnrL("A");
    correspondentie.setHnrT("TO");
    correspondentie.setPostcode("1821RS");
    correspondentie.setPlaats("Alkmaar");
    return correspondentie;
  }

  private static GbaRestOverlijdenAangifte getAangifteInGemeente() {
    GbaRestOverlijdenAangifte aangifte = new GbaRestOverlijdenAangifte();
    aangifte.setDatum(2020_08_26);
    aangifte.setTijd(910);
    aangifte.setPlaats(new GbaRestTabelWaarde("0398", "Heerhugowaard"));
    aangifte.setDocumentType(GbaRestDocumentType.NATUURLIJK_DOOD);
    return aangifte;
  }

  private static GbaRestLijkvindingAangifte getAangifteLijkvinding() {
    GbaRestLijkvindingAangifte aangifte = new GbaRestLijkvindingAangifte();
    aangifte.setSchriftelijkeAangever(GbaRestSchriftelijkeAangeverType.HULPOFFICIER_VAN_JUSTITIE);
    aangifte.setPlaats(new GbaRestTabelWaarde("0398", "Heerhugowaard"));
    aangifte.setDatum(2020_08_26);
    aangifte.setTijd(910);
    aangifte.setToevoeging("Gevonden in de huiskamer");
    aangifte.setDocumentType(GbaRestDocumentType.NATUURLIJK_DOOD);
    return aangifte;
  }

  private static GbaRestZaakAlgemeen getZaakAlgemeen() {
    GbaRestZaakAlgemeen algemeen = new GbaRestZaakAlgemeen();
    algemeen.setZaakId("");
    algemeen.setType(GbaRestZaakType.OVERLIJDEN_IN_GEMEENTE);
    algemeen.setStatus(GbaRestZaakStatusType.INCOMPLEET);
    algemeen.setBron("PROWEB Personen");
    algemeen.setLeverancier("PROCURA");
    algemeen.setDatumIngang(2020_02_01);
    algemeen.setDatumInvoer(2020_04_28);
    algemeen.setTijdInvoer(10_11_12);
    return algemeen;
  }

  private static GbaRestPersoon getAangever(GbaRestContactgegevens cg) {
    GbaRestPersoon persoon = new GbaRestPersoon();
    persoon.setBsn(Testdata.TEST_BSN_1);
    persoon.setContactgegevens(cg);
    return persoon;
  }

  private static GbaRestPersoon getOverledene(GbaRestContactgegevens cg) {
    GbaRestPersoon persoon = new GbaRestPersoon();
    persoon.setBsn(Testdata.TEST_BSN_2);
    persoon.setGeslachtsnaam("Duck");
    persoon.setVoornamen("Donald");
    persoon.setTitelPredikaat("B");
    persoon.setVoorvoegsel("van der");
    persoon.setGeboortedatum(19900203);
    persoon.setGeboorteplaats(new GbaRestTabelWaarde("Antwerpen"));
    persoon.setGeboorteland(new GbaRestTabelWaarde("5010"));
    persoon.setContactgegevens(cg);
    return persoon;
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
