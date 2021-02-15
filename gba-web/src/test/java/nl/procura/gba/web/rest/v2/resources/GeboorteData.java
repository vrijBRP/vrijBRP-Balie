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

import nl.procura.gba.web.rest.v2.model.base.GbaRestGeslacht;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakAlgemeen;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakStatusType;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakType;
import nl.procura.gba.web.rest.v2.model.zaken.base.namenrecht.GbaRestNamenrecht;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.geboorte.*;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestTelefoonBuitenland;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.proweb.rest.utils.JsonUtils;

public class GeboorteData {

  public static void main(String[] args) {

    GbaRestZaakToevoegenVraag request = new GbaRestZaakToevoegenVraag();
    GbaRestZaak zaak = new GbaRestZaak();
    zaak.setAlgemeen(getZaakAlgemeen());
    zaak.setGeboorte(getGeboorte());
    request.setZaak(zaak);

    System.out.println(JsonUtils.getPrettyObject(request));
  }

  private static GbaRestGeboorte getGeboorte() {
    GbaRestGeboorte geboorte = new GbaRestGeboorte();
    geboorte.setAangever(getAangever(getContactgegevens()));
    geboorte.setMoeder(getMoeder(getContactgegevens()));
    geboorte.setVaderOfDuoMoeder(getVaderOfDuoMoeder(getContactgegevens()));
    geboorte.setAangifte(getAangifte(geboorte));
    geboorte.setGezinssituatie(GbaRestGezinssituatieType.BINNEN_HETERO_HUWELIJK);
    geboorte.setNamenrecht(getNamenrecht());
    geboorte.setKinderen(getKinderen());
    return geboorte;
  }

  private static GbaRestZaakAlgemeen getZaakAlgemeen() {
    GbaRestZaakAlgemeen algemeen = new GbaRestZaakAlgemeen();
    algemeen.setZaakId("");
    algemeen.setType(GbaRestZaakType.GEBOORTE);
    algemeen.setStatus(GbaRestZaakStatusType.INCOMPLEET);
    algemeen.setBron("PROWEB Personen");
    algemeen.setLeverancier("PROCURA");
    algemeen.setDatumIngang(2020_02_01);
    algemeen.setDatumInvoer(2020_04_28);
    algemeen.setTijdInvoer(10_11_12);
    return algemeen;
  }

  private static GbaRestPersoon getAangever(GbaRestContactgegevens cg) {
    GbaRestPersoon aangever = new GbaRestPersoon();
    aangever.setBsn(Testdata.TEST_BSN_1);
    aangever.setContactgegevens(cg);
    return aangever;
  }

  private static GbaRestPersoon getMoeder(GbaRestContactgegevens cg) {
    GbaRestPersoon aangever = new GbaRestPersoon();
    aangever.setBsn(Testdata.TEST_BSN_2);
    aangever.setContactgegevens(cg);
    return aangever;
  }

  private static GbaRestPersoon getVaderOfDuoMoeder(GbaRestContactgegevens cg) {
    GbaRestPersoon aangever = new GbaRestPersoon();
    aangever.setBsn(Testdata.TEST_BSN_1);
    return aangever;
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

  private static GbaRestGeboorteAangifte getAangifte(GbaRestGeboorte geboorte) {
    GbaRestGeboorteAangifte aangifte = new GbaRestGeboorteAangifte();
    aangifte.setRedenVerplichtOfBevoegd(GbaRestRedenVerplichtOfBevoegdType.VADER);
    return aangifte;
  }

  private static GbaRestNamenrecht getNamenrecht() {
    GbaRestNamenrecht namenrecht = new GbaRestNamenrecht();
    namenrecht.setGeslachtsnaam("Vries");
    namenrecht.setVoorvoegsel("de");
    namenrecht.setTitelPredikaat("B");
    return namenrecht;
  }

  private static List<GbaRestKind> getKinderen() {
    List<GbaRestKind> kinderen = new ArrayList<>();
    GbaRestKind kind1 = new GbaRestKind();
    kind1.setVoornamen("Piet");
    kind1.setGeslacht(GbaRestGeslacht.MAN);
    kind1.setGeboortedatum(2020_042_9);
    kind1.setGeboortetijd(10_11_12);

    GbaRestKind kind2 = new GbaRestKind();
    kind2.setVoornamen("Truus");
    kind2.setGeslacht(GbaRestGeslacht.VROUW);
    kind2.setGeboortedatum(2020_04_29);
    kind2.setGeboortetijd(14_30_00);

    kinderen.add(kind1);
    kinderen.add(kind2);
    return kinderen;
  }
}
