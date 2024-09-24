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

package nl.procura.ws.zoekpersoonws;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.converters.persoonlijst.BasePLToPersoonsLijstConverter;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.zoekpersoon.objecten.Persoonslijst;
import nl.procura.gbaws.requests.gba.GbaRequestHandlerWS;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class ZoekpersoonWS {

  public ZoekPersoonWSAntwoord zoeken(ZoekArgumenten zoekArgumenten, CategorieArgumenten categorieArgumenten,
      LoginArgumenten loginArgumenten) {

    final List<String> meldingen = new ArrayList<>();
    final ZoekPersoonWSAntwoord antwoord = new ZoekPersoonWSAntwoord();

    if (zoekArgumenten == null) {
      meldingen.add("0|Geen zoekargumenten meegegeven.");
    } else if (categorieArgumenten == null) {
      meldingen.add("0|Geen categorieArgumenten meegegeven.");
    } else if (loginArgumenten == null) {
      meldingen.add("0|Geen loginArgumenten meegegeven.");
    } else {

      final String u = loginArgumenten.getGebruikersnaam();
      final String p = loginArgumenten.getWachtwoord();
      final PLEArgs a = getPleArgumenten(zoekArgumenten, categorieArgumenten);

      final BasePLBuilder plBuilder = search(u, p, a);

      final List<BasePL> basisPlen = plBuilder.getResult().getBasePLs();
      boolean doNotExpand = categorieArgumenten.isNiet_expanderen();
      boolean onlyCurrent = categorieArgumenten.isAlleen_actueel();

      final List<Persoonslijst> personLists = new BasePLToPersoonsLijstConverter().convert(basisPlen,
          !doNotExpand, !onlyCurrent, true);

      antwoord.setPersoonslijsten(personLists.toArray(new Persoonslijst[personLists.size()]));
      antwoord.setDatabron(plBuilder.getResult().getDatasource().getCode());

      for (final PLEMessage message : plBuilder.getResult().getMessages()) {
        meldingen.add(message.getCode() + "|" + message.getDescr());
      }
    }

    antwoord.setMeldingen(meldingen.toArray(new String[meldingen.size()]));
    return antwoord;
  }

  private PLEArgs getPleArgumenten(ZoekArgumenten zkargs, CategorieArgumenten cargs) {
    final PLEArgs a = new PLEArgs();
    for (String anr : zkargs.getAnummers()) {
      if (Anr.isCorrect(anr)) {
        a.addNummer(anr);
      }
    }

    for (String bsn : zkargs.getBurgerservicenummers()) {
      if (Bsn.isCorrect(bsn)) {
        a.addNummer(bsn);
      }
    }

    a.setGeboortedatum(zkargs.getGeboortedatum());
    a.setGemeentedeel(zkargs.getGemeentedeel());
    a.setGemeente(zkargs.getGemeente());
    a.setGeslachtsnaam(zkargs.getGeslachtsnaam());
    a.setGeslacht(zkargs.getGeslacht());
    a.setHuisnummer(zkargs.getHuisnummer());
    a.setPostcode(zkargs.getPostcode());
    a.setStraat(zkargs.getStraat());
    a.setTitel(zkargs.getTitel());
    a.setVoornaam(zkargs.getVoornaam());
    a.setVoorvoegsel(zkargs.getVoorvoegsel());

    a.setShowHistory(!cargs.isAlleen_actueel());
    a.setShowArchives(cargs.isArchief());
    a.setDatasource(PLEDatasource.get(cargs.getDatabron()));
    a.setSearchOnAddress(cargs.isZoek_op_adres());
    a.setSearchRelations(cargs.isGerelateerden());

    a.setCat(PERSOON, cargs.isCat_persoon());
    a.setCat(OUDER_1, cargs.isCat_ouder_1());
    a.setCat(OUDER_2, cargs.isCat_ouder_2());
    a.setCat(NATIO, cargs.isCat_nationaliteiten());
    a.setCat(HUW_GPS, cargs.isCat_huwelijken());
    a.setCat(OVERL, cargs.isCat_overlijden());
    a.setCat(INSCHR, cargs.isCat_inschrijving());
    a.setCat(VB, cargs.isCat_verblijfplaats());
    a.setCat(KINDEREN, cargs.isCat_kinderen());
    a.setCat(VBTITEL, cargs.isCat_verblijfstitel());
    a.setCat(GEZAG, cargs.isCat_gezag());
    a.setCat(REISDOC, cargs.isCat_reisdocumenten());
    a.setCat(KIESR, cargs.isCat_kiesrecht());
    a.setCat(AFNEMERS, cargs.isCat_afnemers());
    a.setCat(VERW, cargs.isCat_verwijzing());
    a.setCat(DIV, cargs.isCat_diversen());
    a.setCat(WK, cargs.isCat_woningkaart());
    a.setCat(KLADBLOK, cargs.isCat_kladblokaantekening());
    a.setCat(LOK_AF_IND, cargs.isCat_lokaleafnemersindicaties());

    return a;
  }

  private BasePLBuilder search(String u, String p, PLEArgs args) {
    return new GbaRequestHandlerWS(u, p, args).getBuilder();
  }
}
