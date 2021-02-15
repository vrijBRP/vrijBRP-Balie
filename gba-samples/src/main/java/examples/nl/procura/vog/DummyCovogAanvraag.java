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

package examples.nl.procura.vog;

import nl.procura.covog.objecten.verzendAanvraagNp.*;
import nl.procura.gbaws.testdata.Testdata;

public class DummyCovogAanvraag extends CovogAanvraag {

  public DummyCovogAanvraag() {

    setAanvraagNummer("039801201204180002");
    setGemeenteCode("394");
    setLocatieCode("0");
    setAanvraagdatum("20120418");
    setDoelCode("WRE");
    setToelichtingDoel("a");
    setIndicatiePersisteren("N");
    setIndicatieBijzonderheden("N");
    setIndicatieOmstandigheden("N");
    setIndicatieCovogAdvies("N");
    setOpmerkingGemeente("Geen");
    setBurgemeestersadvies("O");

    CovogOnderzoeksPersoonNP v = new CovogOnderzoeksPersoonNP();

    v.setGeslachtsnaam("Duck");
    v.setFunctie("Directeur");
    v.setVoorvoegsel("Aan den");
    v.setVoornamen("Donald");
    v.setGeboortedatum("19571009");
    v.setGeboorteplaatsBuitenland("Ošló");
    v.setLandCodeGeboren("6027");
    v.setGeslacht("M");
    v.setAanschrijfnaam("Duck, Donald");
    v.setStraat("Dorpsstraat");
    v.setHuisnummer("1");
    v.setHuisnummerToevoeging("bij het water");
    v.setPostcode("1234 AD");
    v.setPlaats("HHW");
    v.setLandCode("6030");
    v.setBurgerServiceNummer(Testdata.TEST_BSN_1.toString());
    // v.setScreeningsprofiel ("0");

    CovogHistorie h = new CovogHistorie();

    h.setBurgerServiceNummer(Testdata.TEST_BSN_1.toString());
    h.setGeboortedatum("19571009");
    h.setGeboorteplaatsBuitenland("Ošló");
    h.setGeslachtsnaam("Duck");
    h.setGeslacht("M");
    h.setLandCodeGeboren("6027");
    h.setVoornamen("Donald");
    h.setVoorvoegsel("Aan den");

    v.setHistories(new CovogHistorie[]{ h });

    v.setNationaliteiten(new String[]{ "0001", "0446" });

    v.setFunctieaspecten(new CovogFunctieaspect[]{ new CovogFunctieaspect("11"), new CovogFunctieaspect(
        "41"), new CovogFunctieaspect("42") });

    setOnderzoeksPersoonNP(v);

    CovogBelanghebbende b = new CovogBelanghebbende();

    b.setNaamBelanghebbende("PROCURA");
    b.setNaamVertegenwoordiger("Vertegenwoordiger");
    b.setStraat("straat");
    b.setHuisnummer("123");
    b.setHuisnummerToevoeging("wdasdas");
    b.setPostcode("4567 DE");
    b.setPlaats("dsadasd");
    b.setLandCode("6030");
    b.setTelefoon("1234");

    setBelanghebbende(b);
  }
}
